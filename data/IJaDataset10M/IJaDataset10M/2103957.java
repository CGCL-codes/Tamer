package tm.javaLang.analysis;

import tm.clc.analysis.CGRTest;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.javaLang.ast.TyJava;
import tm.utilities.Debug;

/**
 * Tests whether a conversion is widening or an identity
 */
public class CGRWideningOrEqualTest extends CGRTest {

    /**
	 * This method tests whether a conversion isn't narrowing.
	 * @param exp expression being tested
     * <P><strong>Precondition:</strong>
     * <ul><li>exp.get_base_type (0) is the type to convert to
     *     </li>
     *     <li>exp.get_base_type (1) is the type to convert from
     * </ul>
     * <p><strong>Postcondition:</strong>
     * <ul>
     *     <li> result iff exp.get_base_type (1) is the same as or 
     *          can be widened to exp.get_base_type (0)
     *     </li>
     * </ul>
	 */
    public boolean applies(ExpressionPtr exp) {
        TypeNode toType = exp.get_base_type(0);
        TypeNode fromType = exp.get_base_type(1);
        Debug.getInstance().msg(Debug.COMPILE, "WideningOrEqualTest: to-type is " + (toType).toString() + " and from-type is " + (fromType).toString());
        return toType.equal_types(fromType) || ((TyJava) toType).isReachableByWideningFrom((TyJava) fromType);
    }
}
