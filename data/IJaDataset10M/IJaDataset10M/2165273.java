package org.tzi.use.uml.ocl.type;

import java.util.HashSet;
import java.util.Set;

/**
 * The OclAny type.
 *
 * @version     $ProjectVersion: 0.393 $
 * @author  Mark Richters
 */
public final class OclAnyType extends Type {

    /** 
     * Returns true if this type is a subtype of <code>t</code>. 
     */
    public boolean isSubtypeOf(Type t) {
        return equals(t);
    }

    /** 
     * Returns the set of all supertypes (including this type).
     */
    public Set allSupertypes() {
        Set res = new HashSet(1);
        res.add(this);
        return res;
    }

    /**
     * Returns true if the passed type is equal.
     */
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass().equals(getClass())) return true;
        return false;
    }

    public int hashCode() {
        return getClass().hashCode();
    }

    /** 
     * Returns a complete printable type name.
     */
    public String toString() {
        return "OclAny";
    }
}
