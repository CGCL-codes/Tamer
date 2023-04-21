package tm.cpp.analysis;

import tm.clc.analysis.ScopedName;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Company: </p>
 * @author Theo
 * @version 1.0
 */
public class Cpp_ScopedName extends ScopedName {

    public Cpp_ScopedName() {
    }

    /**
     * Creates a new <code>Cpp_ScopedName</code> instance.
     * 
     * @param rawName either an initial terminal id, a simple name, or
     *  a string representation of a qualified name.
     */
    public Cpp_ScopedName(String name) {
        super(name);
    }

    /**
     * Creates a new <code>ScopedName</code> instance.
     *
     * @param elements the components of the name.
     */
    protected Cpp_ScopedName(String elements[]) {
        super(elements);
    }

    /**
     * Copy constructor. 
     * @param orig a <code>ScopedName</code> value
     */
    public Cpp_ScopedName(ScopedName orig) {
        super(orig);
    }

    /**
     * Creates a new <code>ScopedName</code> initialized according to
     * the flag passed in.
     * @param flag controls initialization. Valid values defined in class.
     */
    public Cpp_ScopedName(int flag) {
        super(flag);
    }

    /**
     * Implementation of <code>clone</code> using the copy constructor.
     * @return a copy of this <code>ScopedName</code>
     */
    public synchronized Object clone() {
        return new Cpp_ScopedName(this);
    }

    /**
     * Get the language specific scope operator.
     * @return "::"
     */
    public String getScopeOp() {
        return "::";
    }
}
