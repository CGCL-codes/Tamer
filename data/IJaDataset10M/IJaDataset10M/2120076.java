package koala.dynamicjava.classfile;

/**
 * The classes derived from this one are used to represents a method
 *
 * @author Stephane Hillion
 * @version 1.0 - 1999/05/06
 */
public abstract class AbstractMethodIdentifier extends MemberIdentifier {

    /**
     * The parameters types
     */
    private String[] parameters;

    /**
     * Creates a new method identifier
     * @param dc the declaring class of this member
     * @param n  the name of this member
     * @param t  the type of this member in JVM format
     * @param p  the parameters types
     */
    public AbstractMethodIdentifier(String dc, String n, String t, String[] p) {
        super(dc, n, t);
        parameters = p;
    }

    /**
     * Returns the parameters types
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * Indicates whether some other object is equal to this one
     */
    public boolean equals(Object other) {
        if (super.equals(other)) {
            String[] p = ((AbstractMethodIdentifier) other).parameters;
            if (parameters.length != p.length) {
                return false;
            }
            for (int i = 0; i < p.length; i++) {
                if (!parameters[i].equals(p[i])) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a hash code value for this object
     */
    public int hashCode() {
        int result = super.hashCode();
        for (int i = 0; i < parameters.length; i++) {
            result += parameters[i].hashCode();
        }
        return result;
    }
}
