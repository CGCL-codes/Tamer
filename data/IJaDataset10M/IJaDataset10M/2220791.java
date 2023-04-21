package com.google.template.soy.coredirectives;

import com.google.common.collect.ImmutableList;
import com.google.template.soy.jssrc.restricted.JsExpr;
import com.google.template.soy.shared.AbstractSoyPrintDirectiveTestCase;

/**
 * Unit tests for IdDirective.
 *
 */
public class IdDirectiveTest extends AbstractSoyPrintDirectiveTestCase {

    public void testApplyForTofu() {
        IdDirective idDirective = new IdDirective();
        assertTofuOutput("", "", idDirective);
        assertTofuOutput("identName", "identName", idDirective);
        assertTofuOutput("<>&'\" \\", "<>&'\" \\", idDirective);
    }

    public void testApplyForJsSrc() {
        IdDirective idDirective = new IdDirective();
        JsExpr dataRef = new JsExpr("opt_data.myKey", Integer.MAX_VALUE);
        assertEquals("opt_data.myKey", idDirective.applyForJsSrc(dataRef, ImmutableList.<JsExpr>of()).getText());
    }
}
