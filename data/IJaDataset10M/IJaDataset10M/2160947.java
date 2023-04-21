package org.datanucleus.enhancer;

import java.util.Arrays;
import org.datanucleus.util.Localiser;

/**
 * Representation of a method that an enhanced class requires.
 */
public abstract class ClassMethod {

    /** Localisation of messages */
    protected static final Localiser LOCALISER = Localiser.getInstance("org.datanucleus.enhancer.Localisation", ClassEnhancer.class.getClassLoader());

    /** The parent enhancer. */
    protected ClassEnhancer enhancer;

    /** Name of the method. */
    protected String methodName;

    /** Access flags for the method (public, protected etc). */
    protected int access;

    /** Return type for the method */
    protected Object returnType;

    /** Types of the arguments. */
    protected Object[] argTypes;

    /** Names of the arguments. */
    protected String[] argNames;

    /** Exceptions that can be thrown. */
    protected String[] exceptions;

    /**
     * Constructor.
     * @param enhancer ClassEnhancer
     * @param name Name of the method
     * @param access Access for the method (PUBLIC, PROTECTED etc)
     * @param returnType Return type
     * @param argTypes Argument type(s)
     * @param argNames Argument name(s)
     */
    public ClassMethod(ClassEnhancer enhancer, String name, int access, Object returnType, Object[] argTypes, String[] argNames) {
        this(enhancer, name, access, returnType, argTypes, argNames, null);
    }

    /**
     * Constructor.
     * @param enhancer ClassEnhancer
     * @param name Name of the method
     * @param access Access for the method (PUBLIC, PROTECTED etc)
     * @param returnType Return type
     * @param argTypes Argument type(s)
     * @param argNames Argument name(s)
     * @param exceptions Exceptions that can be thrown
     */
    public ClassMethod(ClassEnhancer enhancer, String name, int access, Object returnType, Object[] argTypes, String[] argNames, String[] exceptions) {
        this.enhancer = enhancer;
        this.methodName = name;
        this.access = access;
        this.returnType = returnType;
        this.argTypes = argTypes;
        this.argNames = argNames;
        this.exceptions = exceptions;
    }

    /**
     * Accessor for the method name
     * @return Name of the method
     */
    public String getName() {
        return methodName;
    }

    /**
     * Accessor for the access
     * @return Access for the method
     */
    public int getAccess() {
        return access;
    }

    /**
     * Method to initialise the class method.
     */
    public abstract void initialise();

    /**
     * Method to add the contents of the class method.
     */
    public abstract void execute();

    /**
     * Method to close the definition of the class method.
     * This implementation simply logs a debug message to category ENHANCER.
     */
    public void close() {
        if (DataNucleusEnhancer.LOGGER.isDebugEnabled()) {
            String msg = getMethodAdditionMessage(methodName, returnType, argTypes, argNames);
            DataNucleusEnhancer.LOGGER.debug(LOCALISER.msg("Enhancer.AddMethod", msg));
        }
    }

    /**
     * Return hash code of this instance.
     * @return hash code of this instance
     */
    public int hashCode() {
        return methodName.hashCode();
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param o the reference object with which to compare.
     * @return true if this object is the same as the obj argument; false otherwise.
     */
    public boolean equals(Object o) {
        if (o instanceof ClassMethod) {
            ClassMethod cb = (ClassMethod) o;
            if (cb.methodName.equals(methodName)) {
                return Arrays.equals(cb.argTypes, argTypes);
            }
        }
        return false;
    }

    /**
     * Convenience method to generate a message that a method has been added.
     * @param methodName Name of the method
     * @param returnType Return type of the method
     * @param argTypes arg types for the method
     * @param argNames arg names for the method
     * @return The message
     */
    public static String getMethodAdditionMessage(String methodName, Object returnType, Object[] argTypes, String[] argNames) {
        StringBuffer sb = new StringBuffer();
        if (returnType != null) {
            if (returnType instanceof Class) {
                sb.append(((Class) returnType).getName()).append(" ");
            } else {
                sb.append(returnType).append(" ");
            }
        } else {
            sb.append("void ");
        }
        sb.append(methodName).append("(");
        if (argTypes != null) {
            for (int i = 0; i < argTypes.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                sb.append(argTypes[i]).append(" ").append(argNames[i]);
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
