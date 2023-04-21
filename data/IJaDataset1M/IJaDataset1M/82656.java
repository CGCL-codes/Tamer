package com.aviadbd.essiyo.x2j.validate;

import com.aviadbd.essiyo.x2j.annotations.FixedSequence;
import com.aviadbd.essiyo.x2j.exceptions.ValidatorException;
import com.aviadbd.essiyo.x2j.utility.ReflectedArrayIterator;
import java.lang.reflect.InvocationTargetException;
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
public final class ArrayValidatorVisitor implements ValidatorVisitor {

    /**
     * Visits an instance being validated
     * and checks if the instance's member
     * meets the validation rules specified
     * by this validator visitor.
     *
     * @param obj    The instance being checked.
     * @param member The member being checked.
     * @return Error string if validation was
     *         unsuccessful, or null if it was successful.
     */
    public String visit(Object obj, Method member) throws ValidatorException {
        FixedSequence at = member.getAnnotation(FixedSequence.class);
        Object inspected;
        try {
            inspected = member.invoke(obj);
        } catch (IllegalAccessException e) {
            throw new ValidatorException(e);
        } catch (InvocationTargetException e) {
            throw new ValidatorException(e);
        }
        if (!inspected.getClass().isArray()) {
            return "Object passed is not an array, though annotated with @FixedSequence";
        }
        int nn = 0;
        for (Object item : ReflectedArrayIterator.createIterable(inspected)) {
            if (item != null) {
                nn++;
            }
        }
        if (at.value() != nn) {
            return String.format("Object passed does not have %1$d items as specified by @FixedSequence(%1$d), but has " + "%2$d items instead", at.value(), nn);
        }
        return null;
    }
}
