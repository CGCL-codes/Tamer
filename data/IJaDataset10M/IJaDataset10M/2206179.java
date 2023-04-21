package com.aviadbd.essiyo.x2j.analyse.info.visitors;

import com.aviadbd.essiyo.x2j.analyse.ClassInfo;
import com.aviadbd.essiyo.x2j.analyse.ElementInfo;
import com.aviadbd.essiyo.x2j.analyse.defer.DeferredClassInfoFactory;
import com.aviadbd.essiyo.x2j.analyse.info.ClassVisitor;
import com.aviadbd.essiyo.x2j.analyse.info.MethodVisitor;
import com.aviadbd.essiyo.x2j.analyse.ops.CollectionOperations;
import com.aviadbd.essiyo.x2j.annotations.Name;
import com.aviadbd.essiyo.x2j.annotations.Sequence;
import com.aviadbd.essiyo.x2j.exceptions.AnalyserException;
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
public class SequenceAnalyserVisitor implements ClassVisitor, MethodVisitor {

    /**
     * Visits an object typed T and changes the mould
     * to fit to the object's properties, annotations
     * and values.
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
        mould.setValue("sequence", true);
        Sequence seq = item.getAnnotation(Sequence.class);
        mould.setValue("minimumSize", seq.minimum());
        mould.setValue("maximumSize", seq.maximum());
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
        return item.isAnnotationPresent(Sequence.class);
    }

    /**
     * Visits an object typed T and changes the mould
     * to fit to the object's properties, annotations
     * and values.
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
    public void visit(Class<?> item, Mould mould) throws AnalyserException {
        Class<?> componentType = item.getComponentType();
        mould.setValue("sequence", true);
        mould.setValue("operations", CollectionOperations.getInstance());
        mould.setValue("name", componentType.getSimpleName() + "Array");
        ClassInfo type = DeferredClassInfoFactory.createDefer(componentType);
        Name compoundName = componentType.getAnnotation(Name.class);
        ElementInfo ei = new ElementInfo(item, type, "?", compoundName != null ? compoundName.value() : componentType.getSimpleName(), false, 0, 0, null, null);
        DeferredClassInfoFactory.getInstance().addReplacingListener(ei);
        mould.addValue("elements", ei);
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
    public boolean shouldVisit(Class<?> item) throws AnalyserException {
        return false;
    }
}
