package com.aviadbd.essiyo.x2j.parse.convert;

import com.aviadbd.essiyo.x2j.exceptions.ParserException;
import com.aviadbd.essiyo.x2j.parse.InstanceFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * Copyright 2005-2006 Aviad Ben Dov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class DefaultConverter implements X2JConverter {

    private static final DefaultConverter instance = new DefaultConverter();

    public static DefaultConverter getInstance() {
        return instance;
    }

    private DefaultConverter() {
    }

    /**
     * Converts a map between type names and type
     * instances to a new instance.
     *
     * @param values The values to construct the
     *               new instance from.
     * @return The new instance.
     * @throws com.aviadbd.essiyo.x2j.exceptions.ParserException
     *          If not all the required values
     *          were passed in the map.
     */
    public Object toObject(Map<String, Object> values) throws ParserException {
        return values.get(InstanceFactory.VALUE).toString();
    }

    /**
     * Converts an instance into a map between
     * type names and type instances determined
     * by the values in the instance.
     *
     * @param object The object to convert.
     * @return The map of type names and type
     *         instances.
     * @throws NullPointerException If the object
     *                              passed is null.
     * @throws com.aviadbd.essiyo.x2j.exceptions.ParserException
     *                              If the object is not of the
     *                              correct type.
     */
    public Map<String, Object> toValues(Object object) throws ParserException {
        return Collections.singletonMap(InstanceFactory.VALUE, object);
    }

    /**
     * The collection of values required to construct
     * a new instance. Can be used to validate that all
     * the values were passed to the initializer.
     *
     * @return The collection of values required.
     */
    public Collection<String> getRequiredValues() {
        return Collections.singleton(InstanceFactory.VALUE);
    }

    /**
     * The type of the instances constructed by
     * this converter.
     *
     * @return The type of instances.
     */
    public Class<?> getTargetClass() {
        return String.class;
    }
}
