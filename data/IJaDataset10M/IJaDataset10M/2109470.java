package symbols;

import java.util.Iterator;
import java.util.Vector;

/**
 * List of parameters (SCallableParameter) which contain
 * key, value/reference marker, and their HL type
 * 
 * Used as a list of paramater names and smart parameters
 * which know if there are reference or value
 * 
 * @author marc
 *
 */
public class ParameterList extends Vector {

    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        Iterator it = this.iterator();
        if (it.hasNext()) {
            SCallableParameter param = (SCallableParameter) it.next();
            if (param.isReference()) sb.append("*");
            sb.append(param.getName());
        }
        while (it.hasNext()) {
            SCallableParameter param = (SCallableParameter) it.next();
            if (param.isReference()) sb.append("*");
            sb.append(", ").append(param.getName());
        }
        sb.append("]");
        return sb.toString();
    }
}
