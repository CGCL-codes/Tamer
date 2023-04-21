package com.aviadbd.essiyo.x2j.utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
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
public final class Mould {

    private final Map<String, Object> mould = new HashMap<String, Object>();

    /**
     * Sets the type of a key.
     * <p/>
     * If the key already exists, the type
     * is NOT replaced.
     * <p/>
     * If the type is null, the key is
     * removed and the <code>hasValue</code>
     * will return false for the given key.
     * <p/>
     * Note that keys cannot store null values.
     *
     * @param name  The key to set (or remove)
     * @param value The type to set
     *              (null if removing)
     */
    public final void setValue(String name, Object value) {
        if (!hasValue(name) && value != null) {
            mould.put(name, value);
        } else if (hasValue(name) && value == null) {
            mould.remove(name);
        }
    }

    /**
     * Retrieves a type from the mould.
     * If the type does not exist, null
     * is returned.
     * <p/>
     * Note that keys cannot store null
     * values.
     *
     * @param name The name of the key
     *             containig the required type.
     * @return The type or null if
     *         it doesn't exist.
     */
    public final Object getValue(String name) {
        return mould.get(name);
    }

    /**
     * Checks whether the mould contains
     * a type for the required key.
     *
     * @param name The required key
     * @return Whether a type is available.
     */
    public final boolean hasValue(String name) {
        return mould.containsKey(name);
    }

    /**
     * Adds a type to a list of values contained
     * in a key.
     * <p/>
     * If this is the first type, the key is
     * created and set as a collection containing
     * the type passed.
     * <p/>
     * If the key exists as a collection, the type
     * will be added.
     * <p/>
     * If the key exists as a type, both new
     * and old values will be added to a collection
     * which will be set as the new type for the
     * key.
     *
     * @param name  The set key.
     * @param value The new type to put in the
     *              collection.
     */
    public final void addValue(String name, Object value) {
        Object cur = getValue(name);
        if (cur == null) {
            Collection<Object> coll = new LinkedList<Object>();
            coll.add(value);
            setValue(name, coll);
        } else if (cur instanceof Collection) {
            Collection<Object> coll = (Collection<Object>) cur;
            coll.add(value);
        } else {
            setValue(name, null);
            Collection<Object> coll = new LinkedList<Object>();
            coll.add(cur);
            coll.add(value);
            setValue(name, coll);
        }
    }
}
