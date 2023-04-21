package org.springframework.binding.expression.beanwrapper;

import junit.framework.TestCase;
import org.springframework.beans.TypeMismatchException;
import org.springframework.binding.expression.Expression;
import org.springframework.binding.expression.ParserException;
import org.springframework.binding.expression.ValueCoercionException;
import org.springframework.binding.expression.ognl.TestBean;
import org.springframework.binding.expression.support.FluentParserContext;

public class BeanWrapperExpressionParserTests extends TestCase {

    private BeanWrapperExpressionParser parser = new BeanWrapperExpressionParser();

    private TestBean bean = new TestBean();

    public void testParseSimple() {
        String exp = "flag";
        Expression e = parser.parseExpression(exp, null);
        assertNotNull(e);
        Boolean b = (Boolean) e.getValue(bean);
        assertFalse(b.booleanValue());
    }

    public void testParseSimpleAllowDelimited() {
        parser.setAllowDelimitedEvalExpressions(true);
        String exp = "${flag}";
        Expression e = parser.parseExpression(exp, null);
        assertNotNull(e);
        Boolean b = (Boolean) e.getValue(bean);
        assertFalse(b.booleanValue());
    }

    public void testParseSimpleDelimitedNotAllowed() {
        String exp = "${flag}";
        try {
            parser.parseExpression(exp, null);
            fail("should have failed");
        } catch (ParserException e) {
        }
    }

    public void testParseTemplateSimpleLiteral() {
        String exp = "flag";
        Expression e = parser.parseExpression(exp, new FluentParserContext().template());
        assertNotNull(e);
        assertEquals("flag", e.getValue(bean));
    }

    public void testParseTemplateEmpty() {
        Expression e = parser.parseExpression("", new FluentParserContext().template());
        assertNotNull(e);
        assertEquals("", e.getValue(bean));
    }

    public void testParseTemplateComposite() {
        String exp = "hello ${flag} ${flag} ${flag}";
        Expression e = parser.parseExpression(exp, new FluentParserContext().template());
        assertNotNull(e);
        String str = (String) e.getValue(bean);
        assertEquals("hello false false false", str);
    }

    public void testTemplateEnclosedCompositeNotSupported() {
        String exp = "${hello ${flag} ${flag} ${flag}}";
        try {
            parser.parseExpression(exp, new FluentParserContext().template());
            fail("Should've failed - not intended use");
        } catch (ParserException e) {
        }
    }

    public void testGetValueType() {
        String exp = "flag";
        Expression e = parser.parseExpression(exp, null);
        assertEquals(boolean.class, e.getValueType(bean));
    }

    public void testGetValueTypeNullCollectionValue() {
        String exp = "list[0]";
        Expression e = parser.parseExpression(exp, null);
        assertEquals(null, e.getValueType(bean));
    }

    public void testSetValueWithCoersion() {
        Expression e = parser.parseExpression("date", null);
        e.setValue(bean, "2008-9-15");
    }

    public void testSetBogusValueWithCoersion() {
        Expression e = parser.parseExpression("date", null);
        try {
            e.setValue(bean, "bogus");
            fail("Should have failed tme");
        } catch (ValueCoercionException ex) {
            assertTrue(ex.getCause() instanceof TypeMismatchException);
        }
    }
}
