package ISS.Util.ObjParse;

import ISS.CodingStandards.*;
import java.util.*;

/** Class to represent a reference to some other value
 * @version $Id: Reference.java 131 1996-08-22 10:33:34Z mattw $
 * @author Matthew Watson
*/
public class Reference {

    /** The var we are referring to */
    public Vector to;

    /** Description of Reference contructor
     * @param parameter1 descripton of parameter 1
     * @return           description of the return value and significance
     * @exception SomeException Returned when ... description of exception case
     */
    public Reference(Vector to) {
        this.to = to;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Reference && to.equals(((Reference) obj).to));
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append('$');
        ObjHashtable.doToString(sb, to);
        return sb.toString();
    }

    /** Try and resolve the reference over the given object
     * <p>This function resolves only one reference or level of references at
     * a time, so it will need to be called repeatedly.
     * @returns the resolution, or null if can't be resolved */
    public Object resolve(Object obj) {
        int elems = to.size();
        ResolveVals vals = new ResolveVals();
        boolean resolvedAny = false;
        boolean someLeft = false;
        for (int i = 0; i < elems; i++) {
            vals.obj = to.elementAt(i);
            ObjParse.resolveObj(vals, obj);
            if (vals.resolvedAny) {
                to.setElementAt(vals.obj, i);
                resolvedAny = true;
            }
            someLeft = someLeft || ObjParse.recursiveReferenceCheck(vals.obj) != null;
        }
        Code.debug("Resolved any:" + resolvedAny + " SomeLeft? " + someLeft);
        if (someLeft) return (resolvedAny ? this : null);
        for (int i = 0; i < elems; i++) {
            Code.debug("Looking for:" + to.elementAt(i));
            if (obj instanceof Vector) {
                try {
                    int index = ((Number) (to.elementAt(i))).intValue();
                    obj = ((Vector) obj).elementAt(index);
                } catch (Exception ex) {
                    return (resolvedAny ? this : null);
                }
            } else if (obj instanceof Hashtable) {
                obj = ((Hashtable) obj).get(to.elementAt(i));
                if (obj == null) return (resolvedAny ? this : null);
            } else {
                return (resolvedAny ? this : null);
            }
            Code.debug("Found:" + obj);
        }
        return obj;
    }
}

;
