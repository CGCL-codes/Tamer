package org.wsml.reasoner.transformation.le.implicationreduction;

import junit.framework.TestCase;
import org.omwg.logicalexpression.LogicalExpression;
import org.wsml.reasoner.impl.WSMO4JManager;
import org.wsml.reasoner.transformation.le.LETestHelper;
import org.wsmo.wsml.ParserException;

public class RightImplicationReplacementRuleTest extends TestCase {

    private RightImplicationReplacementRule rule;

    public RightImplicationReplacementRuleTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        this.rule = new RightImplicationReplacementRule(new WSMO4JManager());
    }

    public void testIsApplicable() throws ParserException {
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" and _\"urn:b\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("_\"urn:a\" or  _\"urn:b\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("naf _\"urn:a\"")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("naf (_\"urn:a\" impliedBy _\"urn:b\")")));
        assertFalse(rule.isApplicable(LETestHelper.buildLE("naf (_\"urn:a\" implies _\"urn:a\")")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("(_\"urn:a\" implies  _\"urn:b\")")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("(naf _\"urn:a\" implies naf _\"urn:b\")")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("( _\"urn:a\" implies naf _\"urn:b\")")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("( (_\"urn:a\" and _\"urn:b\") implies ( _\"urn:c\" and _\"urn:d\") )")));
        assertTrue(rule.isApplicable(LETestHelper.buildLE("( (_\"urn:b\") implies (_\"urn:b\") )")));
    }

    public void testApply() throws ParserException {
        LogicalExpression in = LETestHelper.buildLE("_\"urn:a\" implies  _\"urn:b\"");
        LogicalExpression out = LETestHelper.buildLE(" _\"urn:b\" impliedBy _\"urn:a\" ");
        assertEquals(out, rule.apply(in));
        in = LETestHelper.buildLE("( (_\"urn:a\" and _\"urn:b\") implies ( _\"urn:c\" and _\"urn:d\") )");
        out = LETestHelper.buildLE("( ( _\"urn:c\" and _\"urn:d\" ) impliedBy ( _\"urn:a\" and _\"urn:b\" ))");
        assertEquals(out, rule.apply(in));
        in = LETestHelper.buildLE("( (_\"urn:b\") implies (_\"urn:b\") )");
        out = LETestHelper.buildLE("( (_\"urn:b\") impliedBy (_\"urn:b\") )");
        assertEquals(out, rule.apply(in));
    }
}
