package org.wsml.reasoner.transformation.le.lloydtopor;

import java.util.Set;
import junit.framework.TestCase;
import org.omwg.logicalexpression.LogicalExpression;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsml.reasoner.transformation.le.LETestHelper;
import org.wsmo.wsml.ParserException;

public class TransformNestedImplicationTest extends TestCase {

    private TransformNestedImplication rule;

    public TransformNestedImplicationTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.rule = new TransformNestedImplication(new WSMO4JManager());
    }

    public void testIsApplicable() throws ParserException {
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" :- _\"urn:b\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" implies _\"urn:b\" :- _\"urn:c\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" and _\"urn:b\" and _\"urn:c\" or _\"urn:d\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" :-  _\"urn:b\" impliedBy _\"urn:c\" or _\"urn:d\" ")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("(_\"urn:a\" or _\"urn:b\" ) and (_\"urn:c\" impliedBy  _\"urn:d\")")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\"  :- _\"urn:c\" impliedBy _\"urn:b\" ")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" impliedBy _\"urn:b\"  :- _\"urn:c\" ")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("(naf _\"urn:a\") impliedBy (_\"urn:b\"  or _\"urn:c\") :- _\"urn:d\" ")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" impliedBy _\"urn:b\" impliedBy _\"urn:c\" :- _\"urn:d\" ")));
    }

    public void testApply() throws ParserException {
        LogicalExpression in = LETestHelper.buildLE("_\"urn:a\" impliedBy _\"urn:b\"  :- _\"urn:c\" ");
        Set<LogicalExpression> result = rule.apply(in);
        assertTrue(!result.toString().contains("_#"));
        assertEquals(1, result.size());
        assertTrue(result.contains(LETestHelper.buildLE("_\"urn:a\" :- _\"urn:b\" and _\"urn:c\"")));
        in = LETestHelper.buildLE("_\"urn:a\" impliedBy _\"urn:b\" impliedBy _\"urn:c\" :- _\"urn:d\" ");
        result = rule.apply(in);
        assertTrue(!result.toString().contains("_#"));
        assertEquals(1, result.size());
        assertTrue(result.contains(LETestHelper.buildLE("_\"urn:a\" impliedBy _\"urn:b\" :- _\"urn:c\" and _\"urn:d\" ")));
    }
}
