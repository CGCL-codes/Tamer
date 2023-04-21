package com.aviadbd.essiyo.x2j.analyse.ops;

import com.aviadbd.essiyo.x2j.analyse.ClassInfo;
import com.aviadbd.essiyo.x2j.analyse.ClassInfoOperations;
import com.aviadbd.essiyo.x2j.analyse.ElementInfo;
import com.aviadbd.essiyo.x2j.exceptions.X2JException;
import com.aviadbd.essiyo.x2j.utility.Accessor;
import java.util.Collections;

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
public final class InstanceOperations implements ClassInfoOperations {

    private static final InstanceOperations instance = new InstanceOperations();

    private InstanceOperations() {
    }

    /**
     * Creates a new instance of the class
     * described by this information instance.
     *
     * @param current The ClassInfo of the instance
     *                requesting the operation.
     * @return A new object according to the
     *         current specifications.
     * @throws com.aviadbd.essiyo.x2j.exceptions.X2JException
     *          If object could not
     *          be created.
     */
    public Object newInstance(ClassInfo current, ClassInfo parent, String member) throws X2JException {
        try {
            return current.getType().newInstance();
        } catch (InstantiationException e) {
            throw new X2JException(e);
        } catch (IllegalAccessException e) {
            throw new X2JException(e);
        }
    }

    /**
     * Assigns a child to an item so that the parent
     * would contain the child. A member name is
     * specified to help determine where in the parent
     * the child should be contained.
     *
     * @param current The ClassInfo of the instance
     *                requesting the operation.
     * @param parent  The parent to contain the child.
     * @param member  The member name to contain the child
     *                in.
     * @param child   The child instance to contain in the
     *                parent.
     * @throws com.aviadbd.essiyo.x2j.exceptions.X2JException
     *          If assignment was unsuccessful.
     */
    public void assignChild(ClassInfo current, Object parent, String member, Object child) throws X2JException {
        ElementInfo eiChild = current.getElement(member);
        Accessor.setter(parent, eiChild.getMemberName(), child, eiChild.getType().getType());
    }

    /**
     * Iterates the object if its an array or collection,
     * or returns a singleton collection if not.
     *
     * @param current The ClassInfo of the instance
     *                requesting the operation.
     * @param obj     The object to iterate.
     * @return An iterator to the object's contents.
     */
    public Iterable<Object> iterateObject(ClassInfo current, Object obj) {
        return Collections.singleton(obj);
    }

    public static InstanceOperations getInstance() {
        return instance;
    }
}
