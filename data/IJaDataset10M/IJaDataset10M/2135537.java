package com.w20e.socrates.data;

import com.w20e.socrates.expression.Undef;
import com.w20e.socrates.expression.XBoolean;

/**
 * @author dokter Create a Boolean value from several lexical interpretations.
 */
public class ToBoolean implements Transformation {

    /**
	 * Transform to boolean object. The result is false iff the argument is:
	 * <ul>
	 * <li>an empty String</li>
	 * <li>a String value 'false' in any lower/upper case combination</li>
	 * <li>an Numeric value that has an integer value 0</li>
	 * <li>a Boolean false</li>
	 * <li>null></li>
	 * </ul>
	 * and true in all other cases.
	 * 
	 * @param obj
	 *            object to transform
	 * @return boolean object.
	 */
    public final Object transform(final Object obj) {
        if (obj == null || obj instanceof Undef) {
            return Boolean.valueOf(false);
        }
        if (Number.class.isAssignableFrom(obj.getClass())) {
            if (((Number) obj).intValue() == 0) {
                return Boolean.valueOf(false);
            }
        } else if (obj instanceof String) {
            if ("".equals(obj) || "false".equals(((String) obj).toLowerCase())) {
                return Boolean.valueOf(false);
            }
            return Boolean.valueOf(true);
        } else if (obj instanceof Boolean) {
            return obj;
        } else if (obj instanceof XBoolean) {
            return Boolean.valueOf(((XBoolean) obj).toBoolean());
        }
        return Boolean.valueOf(true);
    }
}
