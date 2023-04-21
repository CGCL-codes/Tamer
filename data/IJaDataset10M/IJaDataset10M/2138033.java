package de.tuberlin.cs.cis.ocl.eval.instance;

import de.tuberlin.cs.cis.ocl.type.Type;
import de.tuberlin.cs.cis.ocl.type.reflect.OclInteger;
import de.tuberlin.cs.cis.ocl.type.reflect.OclString;

/**
 * Represents an instance of the OCL type String.
 * 
 * @author fchabar
 */
public class StringInstance extends OclAnyInstance implements OclString {

    /**
	 * Determines the Java <code>OclString</code> representation of an 
	 * <code>OclString</code>.
	 * @param s2 an <code>OclString</code>
	 * @return the Java <code>OclString</code> representation of 
	 * the specified OCL String.
	 */
    public static String stringValue(OclString s2) {
        return ((StringInstance) s2).stringValue;
    }

    /** The Java representation of this OCL String */
    private String stringValue;

    /**
	 * Constructs an instance of the OCL type String via a 
	 * Java <code>String</code>.
	 * @param value a Java <code>String</code>.
	 */
    public StringInstance(String value) {
        super(Type.AString, value);
        stringValue = value;
    }

    public OclInteger size() {
        return new IntegerInstance(stringValue.length());
    }

    public OclString concat(OclString string2) {
        return new StringInstance(stringValue + stringValue(string2));
    }

    public OclString toUpper() {
        return new StringInstance(stringValue.toUpperCase());
    }

    public OclString toLower() {
        return new StringInstance(stringValue.toLowerCase());
    }

    public OclString substring(OclInteger lower, OclInteger upper) {
        int arg0 = IntegerInstance.intValue(lower) - 1;
        int arg1 = IntegerInstance.intValue(upper);
        return new StringInstance(stringValue.substring(arg0, arg1));
    }
}
