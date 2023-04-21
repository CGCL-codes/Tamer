package org.apache.ws.jaxme.js.pattern;

import org.apache.ws.jaxme.js.JavaMethod;
import org.apache.ws.jaxme.js.Parameter;

/** This class is a key for generated methods. The main
 * purpose is to determine, whether some method is
 * present in more than one interface. In that case
 * we have to ensure, that it is generated only once.
 */
public class MethodKey implements Comparable {

    private JavaMethod method;

    /** Creates a new instance of {@link MethodKey}.
     */
    public MethodKey(JavaMethod pMethod) {
        method = pMethod;
    }

    /** <p>Returns whether this method key equals the object <code>o</code>.
     * This is the case, if <code>o != null</code>,
     * <code>o instanceof MethodKey</code>,
     * and <code>compareTo(o) == 0</code>.</p>
     */
    public boolean equals(Object o) {
        if (o == null || !(o instanceof MethodKey)) {
            return false;
        }
        return compareTo(o) == 0;
    }

    /** <p>Compares this GeneratedMethod to the given GeneratedMethod <code>o</code>.
     * More precise, compares the method name, the number of parameters
     * and the class names of the parameters, in that order.</p>
     * @throws ClassCastException The object o is not an instance of MethodKey.
     */
    public int compareTo(Object o) {
        MethodKey other = (MethodKey) o;
        int result = method.getName().compareTo(other.method.getName());
        if (result != 0) {
            return result;
        }
        Parameter[] params = method.getParams();
        Parameter[] oparams = other.method.getParams();
        result = params.length - oparams.length;
        if (result != 0) {
            return result;
        }
        for (int i = 0; i < params.length; i++) {
            result = params[i].getType().toString().compareTo(oparams[i].getType().toString());
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public int hashCode() {
        int result = method.getName().hashCode();
        Parameter[] params = method.getParams();
        result += params.length;
        for (int i = 0; i < params.length; i++) {
            result += params[i].getType().toString().hashCode();
        }
        return result;
    }
}
