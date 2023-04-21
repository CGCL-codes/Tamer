package net.m2technologies.open_arm.utilities.generic_object_functions;

/**
 * Copyright 2005 Mark Masterson<br> <br> Licensed under the Apache License, Version 2.0 (the "License");<br> you may
 * not use this file except in compliance with the License.<br> You may obtain a copy of the License at<br> <br>
 * http://www.apache.org/licenses/LICENSE-2.0<br> <br> Unless required by applicable law or agreed to in writing,
 * software<br> distributed under the License is distributed on an "AS IS" BASIS,<br> WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied.<br> See the License for the specific language governing permissions and<br>
 * limitations under the License.<br>
 * <p/>
 * <p>Description: </p>
 *
 * @author Mark Masterson
 * @version 0.010
 */
class IntegerObjectArrayWrapper extends AbstractPrimitiveObjectArrayWrapper {

    private int[] internalArray;

    protected boolean checkType(final Class type) {
        return null != type && Integer.TYPE.equals(type);
    }

    protected int getArrayLength(final Object originalObject) {
        this.internalArray = (int[]) originalObject;
        return this.internalArray.length;
    }

    protected Object getPrimitiveAsObject(final int arrayIndex) {
        return new Integer(this.internalArray[arrayIndex]);
    }
}
