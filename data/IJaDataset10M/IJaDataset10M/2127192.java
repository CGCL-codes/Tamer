package com.googlecode.gwtgl.wrapper.util;

import com.googlecode.gwtgl.wrapper.enums.IWebGLConstEnum;

/**
 * @author Steffen Schäfer
 *
 */
public final class EnumUtil {

    private EnumUtil() {
    }

    /**
	 * Finds and returnes the valueToFind in the given enum values.
	 * 
	 * @param <T>
	 * @param values
	 * @param valueToFind
	 * @return the enum value with the matching int value or null if no matching
	 *         value was found.
	 */
    public static <T extends IWebGLConstEnum> T getByIntValue(T[] values, int valueToFind) {
        for (T val : values) {
            if (val.getIntValue() == valueToFind) {
                return val;
            }
        }
        return null;
    }
}
