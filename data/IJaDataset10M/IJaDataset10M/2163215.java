package org.xenon.resultNodeSet;

import java.util.*;

/**
 * if the return statement says $a , $b next(0) returns a and next(1) returns b
 * length() returns 1
 * @author  thomasK
 * @version
 */
public class ResultingRow extends Object {

    Map values;

    /** gets the Data as a Java Object
*/
    private Object getDataByName(String name) {
        return values.get(name);
    }

    /** this will be a shortcut for only one Object (and not a Vector
 */
    public Object getSingleObjectByName(String name) {
        return getDataByName(name);
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        for (Iterator it = values.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry n = (Map.Entry) it.next();
            buf.append(n.getKey() + " = " + n.getValue() + "\n");
        }
        return buf.toString();
    }

    /** Creates new ResultingRow */
    public ResultingRow(Map in) {
        values = in;
        for (Iterator it = in.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry v = (Map.Entry) it.next();
        }
    }
}
