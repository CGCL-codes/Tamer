package org.gvsig.symbology.fmap.rendering;

import java.io.StringReader;
import java.util.Enumeration;
import java.util.Hashtable;
import junit.framework.TestCase;
import org.gvsig.symbology.fmap.rendering.filter.operations.ExpressionException;
import org.gvsig.symbology.fmap.rendering.filter.parser.ExpressionParser;
import org.gvsig.symbology.fmap.rendering.filter.parser.ParseException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

public class TestExpressionParser extends TestCase {

    protected Hashtable<String, String> arithmeticExpressions = new Hashtable<String, String>();

    protected Hashtable<String, String> logicExpressions = new Hashtable<String, String>();

    protected Hashtable<String, String> filterEncodingExpressions = new Hashtable<String, String>();

    Hashtable<String, Value> symbolsTable = new Hashtable<String, Value>();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fillArithmeticExpressions();
        fillLogicExpressions();
        fillFilterEncodingExpressions();
        fillSymbolsTable();
        testArithmeticExpressions();
        testLogicExpressions();
        testFilterEncodingExpressions();
    }

    private void fillFilterEncodingExpressions() {
        filterEncodingExpressions.put("Ib(1,-1,3)", "true");
        filterEncodingExpressions.put("INull(1)", "false");
        filterEncodingExpressions.put("INull([RD_6])", "false");
        filterEncodingExpressions.put("INull(null)", "true");
        filterEncodingExpressions.put("Ib(4,-1,3)", "false");
    }

    private void fillLogicExpressions() {
        logicExpressions.put("true || false", "true");
        logicExpressions.put("true && false", "false");
        logicExpressions.put("(1<2) && true ", "true");
        logicExpressions.put("(1<2) && (1==1)", "true");
        logicExpressions.put("Not(false)", "true");
        logicExpressions.put("Not(true)", "false");
        logicExpressions.put("(1+2+3+4)<(1+2+3+4+5)", "true");
        logicExpressions.put("((2*3)+5)<(2+(3*5))", "true");
        logicExpressions.put("[RD_6]>1", "true");
        logicExpressions.put("2!=1", "true");
        logicExpressions.put("2<=1", "false");
        logicExpressions.put("3>=4", "false");
    }

    private void fillArithmeticExpressions() {
        arithmeticExpressions.put("1+2+3+4+5", "15");
        arithmeticExpressions.put("(2*4)+(2/1)", "10");
        arithmeticExpressions.put("(4/2)", "2");
        arithmeticExpressions.put("2*3*4", "24");
        arithmeticExpressions.put("--[RD_6]", "2");
        arithmeticExpressions.put("+-2", "-2");
        arithmeticExpressions.put("2*(-3)", "-6");
    }

    private void fillSymbolsTable() {
        symbolsTable.put("RD_6", ValueFactory.createValue(2));
    }

    public void testFilterEncodingExpressions() {
        Enumeration<String> enume = filterEncodingExpressions.keys();
        while (enume.hasMoreElements()) {
            try {
                ExpressionParser parser = new ExpressionParser(new StringReader(filterEncodingExpressions.get(enume.nextElement())), symbolsTable);
                parser.Expression();
                parser.getExpression().evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                fail();
            } catch (ExpressionException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public void testLogicExpressions() {
        Enumeration<String> enume = logicExpressions.keys();
        while (enume.hasMoreElements()) {
            try {
                ExpressionParser parser = new ExpressionParser(new StringReader(logicExpressions.get(enume.nextElement())), symbolsTable);
                parser.Expression();
                parser.getExpression().evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                fail();
            } catch (ExpressionException e) {
                e.printStackTrace();
                fail();
            }
        }
    }

    public void testArithmeticExpressions() {
        Enumeration<String> enume = arithmeticExpressions.keys();
        while (enume.hasMoreElements()) {
            try {
                ExpressionParser parser = new ExpressionParser(new StringReader(arithmeticExpressions.get(enume.nextElement())), symbolsTable);
                parser.Expression();
                parser.getExpression().evaluate();
            } catch (ParseException e) {
                e.printStackTrace();
                fail();
            } catch (ExpressionException e) {
                e.printStackTrace();
                fail();
            }
        }
    }
}
