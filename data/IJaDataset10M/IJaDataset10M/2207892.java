package com.teletalk.jserver.rmi.remote;

import com.teletalk.jserver.property.PropertyConstants;

/**
 * Interface containing constants for remote properties.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta 2
 */
public interface RemotePropertyConstants extends PropertyConstants {

    /** Type constant for custom properties. */
    public static final int CUSTOM_TYPE = 0;

    /** Type constant for BooleanProperty properties. */
    public static final int BOOLEAN_TYPE = 1;

    /** Type constant for DateProperty properties. */
    public static final int DATE_TYPE = 2;

    /** Type constant for EnumProperty properties. */
    public static final int ENUM_TYPE = 3;

    /** Type constant for NumberProperty properties. */
    public static final int NUMBER_TYPE = 4;

    /** Type constant for StringProperty properties. */
    public static final int STRING_TYPE = 5;

    /** Type constant for VectorProperty properties. */
    public static final int VECTOR_TYPE = 6;

    /** Type constant for MultiValueProperty properties. */
    public static final int MULTIVALUE_TYPE = 7;
}
