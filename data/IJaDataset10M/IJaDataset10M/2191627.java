package com.aviadbd.essiyo.x2j.analyse.info.visitors;

import com.aviadbd.essiyo.x2j.analyse.info.MethodVisitor;
import com.aviadbd.essiyo.x2j.annotations.Factory;
import com.aviadbd.essiyo.x2j.exceptions.AnalyserException;
import com.aviadbd.essiyo.x2j.parse.factory.X2JFactory;
import com.aviadbd.essiyo.x2j.utility.Mould;
import java.lang.reflect.Method;

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
public class FactoryAnalyserVisitor implements MethodVisitor {

    /**
     * Visits a method and changes the mould to contain an
     * instance of the factory it has.
     *
     * @param item  The item to visit
     * @param mould The mould to change
     *              properties to.
     * @throws com.aviadbd.essiyo.x2j.exceptions.AnalyserException
     *          If the item checked
     *          should be checked but does not obey certain
     *          demands.
     * @since X2J-alpha-2
     */
    public void visit(Method item, Mould mould) throws AnalyserException {
        try {
            Factory factoryAnnotation = item.getAnnotation(Factory.class);
            X2JFactory factory = factoryAnnotation.factory().newInstance();
            String factoryTypeAttribute = factoryAnnotation.typeAttribute();
            if (!factory.getOriginalType().equals(item.getReturnType())) {
                throw new AnalyserException(String.format("Method %s returns %s, while the factory" + "states it handles %s", item.toString(), item.getReturnType().getName(), factory.getOriginalType().getName()));
            }
            mould.setValue("factory", factory);
            mould.setValue("factoryTypeAttribute", factoryTypeAttribute);
        } catch (InstantiationException e) {
            throw new AnalyserException(e);
        } catch (IllegalAccessException e) {
            throw new AnalyserException(e);
        }
    }

    /**
     * Checks whether an object has any properties
     * that are of interest to this visitor.
     *
     * @param item The item to check
     * @return Whether the object is interesting
     *         to this visitor.
     * @throws com.aviadbd.essiyo.x2j.exceptions.AnalyserException
     *          If the item checked
     *          should be checked but does not obey certain
     *          demands.
     * @since X2J-alpha-2
     */
    public boolean shouldVisit(Method item) throws AnalyserException {
        return item.isAnnotationPresent(Factory.class);
    }
}
