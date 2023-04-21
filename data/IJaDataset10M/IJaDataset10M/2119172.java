package com.volantis.mcs.protocols.css;

import junit.framework.TestCase;

public class ExpressionParserTestCase extends TestCase {

    public void testExpectedExpression() throws Exception {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser("length(css('" + cssProperty + "'),'px')");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testInvalidExpressionWithBadCSSLiteral() {
        try {
            new ExpressionParser("length(css-2('width'),'px')");
            fail("expression is invalid");
        } catch (InvalidExpressionException expected) {
        }
    }

    public void testInvalidExpressionWithBadLengthLiteral() throws Exception {
        try {
            new ExpressionParser("length-2(css('width'),'px')");
            fail("expression is invalid");
        } catch (InvalidExpressionException expected) {
        }
    }

    public void testSpacesInExpressionAreOK() throws InvalidExpressionException {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser("length  (   css    (  '" + cssProperty + "'  )  ,  'px'   )");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testHyphensAreLegal() throws Exception {
        final String cssProperty = "border-width";
        ExpressionParser parser = new ExpressionParser("length(css('" + cssProperty + "'),'px')");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testSpacesInCSSPropertyAreIllegal() throws Exception {
        try {
            new ExpressionParser("length(css('" + "  height   " + "'),'px')");
            fail("Spaces aren't allowed in the CSSProperty - " + "only lower-case letters and hyphens");
        } catch (InvalidExpressionException expected) {
        }
    }

    public void testDoubleQuoteIsAcceptable() throws Exception {
        final String cssProperty = "height";
        ExpressionParser parser = new ExpressionParser("length(css(\"" + cssProperty + "\"),\"px\")");
        assertEquals(cssProperty, parser.getCSSProperty());
    }

    public void testMismatchedQuotesFails() throws Exception {
        try {
            new ExpressionParser("length(css(\"height'),\"px\")");
            fail("Expression is invalid due to mixed use of \" and '");
        } catch (IllegalArgumentException expected) {
        }
    }

    /**
     * Verify that an ExpressionParser created with an empty expression
     * throws an IllegalArgumentException.
     */
    public void testEmptyExpressionFails() {
        try {
            new ExpressionParser("");
            fail("Expression is invalid because it is empty");
        } catch (InvalidExpressionException e) {
        }
    }

    /**
     * Verify that an ExpressionParser created with an null expression
     * throws an NullPointerException.
     */
    public void testNullExpressionFails() throws InvalidExpressionException {
        try {
            new ExpressionParser(null);
            fail("Expression is invalid because it is null");
        } catch (NullPointerException expected) {
        }
    }
}
