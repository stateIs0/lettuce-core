/*
 * Copyright 2011-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.lettuce.core.output;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;

import org.junit.Test;

import io.lettuce.core.GeoCoordinates;
import io.lettuce.core.Value;
import io.lettuce.core.codec.Utf8StringCodec;

/**
 * @author Mark Paluch
 */
public class GeoCoordinatesValueListOutputTest {

    private GeoCoordinatesValueListOutput<?, ?> sut = new GeoCoordinatesValueListOutput<>(new Utf8StringCodec());

    @Test
    public void defaultSubscriberIsSet() throws Exception {
        assertThat(sut.getSubscriber()).isNotNull().isInstanceOf(ListSubscriber.class);
    }

    @Test(expected = IllegalStateException.class)
    public void setIntegerShouldFail() throws Exception {
        sut.set(123L);
    }

    @Test
    public void commandOutputCorrectlyDecoded() {

        sut.multi(2);
        sut.set(ByteBuffer.wrap("1.234".getBytes()));
        sut.set(ByteBuffer.wrap("4.567".getBytes()));
        sut.multi(-1);

        assertThat(sut.get()).contains(Value.just(new GeoCoordinates(1.234, 4.567)));
    }
}
