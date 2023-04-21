package org.vesuf.runtime.uml.foundation.core.converters;

import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.util.*;
import java.text.*;
import java.util.*;

/**
 *  Interface for string converters, converting objects to
 *  string representation (and back).
 */
public class EnumStringConverter implements IStringConverter {

    /** The enumtype. */
    protected EnumType etype;

    /** The constructor for impl objects. */
    protected java.lang.reflect.Constructor ocon;

    /**
	 *  Create a string-string converter.
	 *  @param type	The type, for which to convert values.
	 *  @param props	Presentation properties.
	 */
    public EnumStringConverter(IClassifier type, Properties props) {
        this.etype = (EnumType) type;
        Class mytype = SUtil.getWrappedType(type.getImplementationType());
        try {
            ocon = mytype.getConstructor(new Class[] { String.class });
        } catch (NoSuchMethodException e) {
        }
    }

    /**
	 *  Convert from string to object.
	 *  @param string	The string to parse.
	 *  @return The object.
	 */
    public Object toObject(String string) throws ParseException {
        int idx = etype.getValueIndex(string);
        Object value;
        try {
            value = ocon.newInstance(new Object[] { "" + idx });
        } catch (Exception e) {
            throw new RuntimeException("Cannot create enum value: " + e);
        }
        return value;
    }

    /**
	 *  Convert from object to string.
	 *  @param object	The object.
	 *  @return The string representation.
	 */
    public String toString(Object object) {
        return etype.getValueName(((Number) object).intValue());
    }

    /**
	 *  Get the maximal possible string length.
	 *  @return The maximal string length (or -1 if unknown).
	 */
    public int getMaximumLength() {
        return -1;
    }
}
