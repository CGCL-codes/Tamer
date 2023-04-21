package flex.messaging.util;

import java.util.Arrays;

/**
 * Utility class used as a key into collections of Remote Object methods.  The 
 * key consists of the Remote Object class containing this method, the method name,
 * and the classes representing the parameters in the signature of this method.
 *
 * @exclude
 */
public class MethodKey {

    private Class enclosingClass;

    private String methodName;

    private Class[] parameterTypes;

    /**
     * Constructor.
     * 
     * @param enclosingClass remote Ooject class containing this method
     * @param methodName method name
     * @param parameterTypes classes representing the parameters in the signature of this method
     */
    public MethodKey(Class enclosingClass, String methodName, Class[] parameterTypes) {
        this.enclosingClass = enclosingClass;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }

    /** {@inheritDoc} */
    public boolean equals(Object object) {
        boolean result;
        if (this == object) {
            result = true;
        } else if (object instanceof MethodKey) {
            MethodKey other = (MethodKey) object;
            result = other.methodName.equals(this.methodName) && other.parameterTypes.length == this.parameterTypes.length && other.enclosingClass == this.enclosingClass && Arrays.equals(other.parameterTypes, this.parameterTypes);
        } else {
            result = false;
        }
        return result;
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return enclosingClass.hashCode() * 10003 + methodName.hashCode();
    }
}
