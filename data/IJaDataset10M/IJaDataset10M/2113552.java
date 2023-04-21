package herschel.phs.server.interaction.hifi;

import herschel.ccm.api.param.ParameterSet;
import herschel.ccm.api.param.*;
import herschel.ccm.api.CusDefinition;
import herschel.share.util.DynamicArray;
import herschel.share.util.Tuple;
import java.util.*;

/** A subclass of {@link ParameterTranslator}.
 * Inspects this instances ParameterSet and map it.
 * Non-structured parameters will have the same name in SPOT and CUS
 * CUS String Parameters that look like tuples or arrays
 * will be mapped to a set of SPOT parameters with underscore-index suffixes.
 * <br>
 * For example a CUS parameter declaration of 
 * <code>{bool,int[]} xxx = {true,[1,2]} ; double yyy = 1.234 ; </code>
 * will become
 * <pre>
 *      { { "xxx_0",     "xxx{0}",     Boolean.class },
 *        { "xxx_1_0",   "xxx{1}[0]",  Long.class    },
 *        { "xxx_1_1",   "xxx{1}[1]",  Long.class    },
 *        { "yyy",       "yyy",        Double.class  } }
 * </pre>
 * 
 */
public class IdentityTranslator extends ParameterTranslator {

    /** This defines the SPOT-CUS name equivalence.
     */
    protected Object[][] getEquivalentNames() {
        List<Object[]> result = new ArrayList<Object[]>();
        Map<String, Object> cusPars = getParameterSet().valueMap();
        for (String cusName : cusPars.keySet()) {
            Object value = cusPars.get(cusName);
            if (value instanceof String) {
                String s = (String) value;
                try {
                    char start = s.charAt(0);
                    value = start == '{' ? Tuple.parseTuple(s) : start == '[' ? DynamicArray.parseArray(s) : value;
                } catch (java.text.ParseException pe) {
                    System.out.println(" parse exception " + pe + "; ignored while parsing " + s);
                }
            }
            for (Object[] map : decodeValue(cusName, cusName, value)) {
                result.add(map);
            }
        }
        return result.toArray(new Object[0][]);
    }

    private List<Object[]> decodeValue(String sName, String cName, Object value) {
        List<Object[]> result = new ArrayList<Object[]>();
        List<Object[]> expansion;
        if (value instanceof Tuple) {
            Tuple v = (Tuple) value;
            for (int i = 0, n = v.size(); i < n; i++) {
                expansion = decodeValue(sName + "_" + i, cName + "{" + i + "}", v.get(i));
                result.addAll(expansion);
            }
        } else if (value instanceof DynamicArray) {
            DynamicArray v = (DynamicArray) value;
            for (int i = v.size() - 1; i >= 0; i--) {
                expansion = decodeValue(sName + "_" + i, cName + "[" + i + "]", v.get(i));
                result.addAll(expansion);
            }
        } else {
            result.add(new Object[] { sName, cName, value.getClass() });
        }
        return result;
    }

    static {
        java.util.logging.Logger.getLogger(IdentityTranslator.class.getName()).info(IdentityTranslator.class.getName() + " $Revision: 1.3 $");
    }

    /** Create a IdentityTranslator using a copy of the given ParameterSet.
     */
    public IdentityTranslator(ParameterSet p) {
        super(p);
    }

    /** Create a IdentityTranslator without a ParameterSet.
     * Requires a subsequent invocation of {@link #setParameterSet}
     */
    public IdentityTranslator() {
    }
}
