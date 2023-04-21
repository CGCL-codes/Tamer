package com.aviadbd.essiyo.x2j.parse;

import com.aviadbd.essiyo.x2j.analyse.Analyser;
import com.aviadbd.essiyo.x2j.analyse.AttributeInfo;
import com.aviadbd.essiyo.x2j.analyse.ClassInfo;
import com.aviadbd.essiyo.x2j.exceptions.X2JException;
import com.aviadbd.essiyo.x2j.parse.convert.DefaultConverter;
import com.aviadbd.essiyo.x2j.parse.convert.X2JConverter;
import com.aviadbd.essiyo.x2j.parse.factory.FactoryUtility;
import com.aviadbd.essiyo.x2j.utility.Accessor;
import com.aviadbd.essiyo.x2j.utility.Mould;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

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
public final class InstanceFactory {

    private static final InstanceFactory instance = new InstanceFactory();

    private static final String X2J = "x2j__";

    public static final String CONTAINER_PATH = X2J + "containerPath";

    public static final String NAME = X2J + "name";

    public static final String PATH = X2J + "path";

    public static final String ATTRIBUTES = X2J + "attributes";

    public static final String ELEMENTS = X2J + "elements";

    public static final String VALUE = X2J + "type";

    public static final String TYPE = X2J + "type";

    public static InstanceFactory getInstance() {
        return instance;
    }

    private InstanceFactory() {
    }

    public static Object create(Mould mould) throws X2JException {
        String containerPath = (String) mould.getValue(CONTAINER_PATH);
        String path = (String) mould.getValue(PATH);
        String name = (String) mould.getValue(NAME);
        ClassInfo ciParent = null;
        if (containerPath != null) {
            ciParent = Analyser.getInstance().analyse(containerPath);
        }
        ClassInfo ci = Analyser.getInstance().analyse(path);
        Object result;
        if (ci.isPrimitive()) {
            Map<String, Object> values = new TreeMap<String, Object>();
            Collection<String> elements = (Collection<String>) mould.getValue(ELEMENTS);
            if (elements != null) {
                for (String elemName : elements) {
                    values.put(elemName, mould.getValue(elemName));
                }
            }
            values.put(VALUE, mould.getValue(VALUE));
            X2JConverter converter = null;
            if (ciParent != null) {
                converter = ciParent.getConverter(ci.getType());
            }
            if (converter == null) {
                converter = DefaultConverter.getInstance();
            }
            result = converter.toObject(values);
        } else {
            if (ciParent != null && ciParent.getElement(name).getFactory() != null) {
                result = FactoryUtility.createUsingFactory(null, ciParent, ciParent.getElement(name));
            } else {
                result = ci.getOperations().newInstance(ci, ciParent, name);
            }
            Collection<String> attributes = (Collection<String>) mould.getValue(ATTRIBUTES);
            if (attributes != null) {
                for (String attrName : attributes) {
                    String attrValue = (String) mould.getValue(attrName);
                    AttributeInfo ai = ci.getAttribute(attrName);
                    Accessor.setter(result, ai.getMemberName(), attrValue, ai.getType().getType());
                }
            }
            Collection<String> elements = (Collection<String>) mould.getValue(ELEMENTS);
            if (elements != null) {
                for (String elemName : elements) {
                    Object elemValue = mould.getValue(elemName);
                    if (elemValue instanceof Iterable) {
                        for (Object elemValueIt : (Iterable) elemValue) {
                            ci.getOperations().assignChild(ci, result, elemName, elemValueIt);
                        }
                    } else {
                        ci.getOperations().assignChild(ci, result, elemName, elemValue);
                    }
                }
            }
        }
        return result;
    }
}
