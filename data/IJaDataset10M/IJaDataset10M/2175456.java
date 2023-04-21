package net.sf.refactorit.test.audits.corrective;

import net.sf.refactorit.audit.rules.StringConcatOrderRule;
import net.sf.refactorit.test.audits.CorrectiveActionTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class ConcatEmptyStringExpressionTest extends CorrectiveActionTest {

    /**
 * @author Oleg Tsernetsov
 */
    public ConcatEmptyStringExpressionTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ConcatEmptyStringExpressionTest.class);
    }

    public String getTemplate() {
        return "Audit/corrective/StringConcatOrder/" + "ConcatEmptyStringExpression/<in_out>/<test_name>.java";
    }

    /******************************* Simple Tests *******************************/
    protected void performSimpleTest() throws Exception {
        super.performSimpleTest(StringConcatOrderRule.class, "refactorit.audit.action.string_concat_order.addemptystring");
    }

    public void testConcatEmptyStringExpression() throws Exception {
        performSimpleTest();
    }
}
