package tm.cpp.analysis;

import java.util.Hashtable;
import tm.clc.analysis.ExpressionPtr;
import tm.clc.analysis.ScopedName;
import tm.clc.ast.ExpId;
import tm.clc.ast.ExpressionNode;
import tm.clc.ast.TyAbstractArray;
import tm.clc.ast.TyAbstractFun;
import tm.clc.ast.TypeNode;
import tm.cpp.ast.ExpFunctionName;
import tm.utilities.Assert;

/**
 * Tests whether an expression is a <em>modifiable lvalue</em>. In C++, 
 * this is an lvalue that is "not a function name, an array name, or 
 * <code>const</code>" (ARM 3.7)
 */
public class CGRModifiableLvalueTest extends CGRLvalueTest {

    public CGRModifiableLvalueTest() {
        super();
    }

    public CGRModifiableLvalueTest(int expressionIndex) {
        super(expressionIndex);
    }

    /**
	 * Determines whether the expression contained in <code>exp</code>
	 * is a modifiable lvalue.
	 * @param exp expression being built
	 * @see tm.cpp.analysis.CGRLvalueTest#applies(ExpressionPtr, ScopedName, ExpressionPtr [])
	 */
    public boolean applies(ExpressionPtr exp) {
        boolean testResult = false;
        if (super.applies(exp)) {
            ExpressionNode e = exp.get(start);
            Assert.check(e != null);
            TypeNode t = exp.get_base_type();
            int tAttr = t.getAttributes();
            if (!((e instanceof ExpFunctionName) || (t instanceof TyAbstractFun) || (t instanceof TyAbstractArray && e instanceof ExpId) || ((tAttr & Cpp_Specifiers.CVQ_CONST) != 0))) {
                testResult = true;
            }
            if (testResult == false) fail_detail = "not a modifiable lvalue";
        }
        return testResult;
    }
}
