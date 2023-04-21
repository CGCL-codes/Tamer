package jmathlibtests.core.interpreter;

import jmathlib.tools.junit.framework.*;
import jmathlib.core.interpreter.*;
import jmathlib.core.tokens.*;
import jmathlib.core.tokens.numbertokens.DoubleNumberToken;
import java.util.Vector;

public class testLexicalAnalyser extends TestCase {

    private LexicalAnalyser analyser;

    private Vector expectedResult;

    public testLexicalAnalyser(String name) {
        super(name);
    }

    public static void main(String[] args) {
        jmathlib.tools.junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(testLexicalAnalyser.class);
    }

    public void setUp() {
        analyser = new LexicalAnalyser();
        expectedResult = new Vector();
    }

    protected void tearDown() {
        analyser = null;
        expectedResult = null;
    }

    public void testNumber() {
        String expression = "3";
        expectedResult.add(new DoubleNumberToken(3));
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
    }

    public void testNegativeNumber() {
        String expression = "-3";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(-3));
    }

    public void testFloatNumber() {
        String expression = "3.45";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3.45));
    }

    public void testScientificNumber() {
        String expression = "3.3E2";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(330));
    }

    public void testScientificNumber2() {
        String expression = "3.3E-2";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(0.033));
    }

    public void testComplex() {
        String expression = "3+3i";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
        assertEquals(analyser.getNextToken(0), new AddSubOperatorToken('+'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(0, 3));
    }

    public void testString() {
        String expression = "'Hello World'";
        analyser.analyseExpression(expression);
        assertTrue(analyser.getNextToken(0).toString().equals("Hello World"));
    }

    public void testAdd() {
        String expression = "3+3";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
        assertEquals(analyser.getNextToken(0), new AddSubOperatorToken('+'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
    }

    public void testSubtract() {
        String expression = "3-3";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
        assertEquals(analyser.getNextToken(0), new AddSubOperatorToken('-'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
    }

    public void testMultiply() {
        String expression = "2*4";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(2));
        assertEquals(analyser.getNextToken(0), new MulDivOperatorToken('*'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(4));
    }

    public void testDivide() {
        String expression = "5/2";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(5));
        assertEquals(analyser.getNextToken(0), new MulDivOperatorToken('/'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(2));
    }

    public void testMPower() {
        String expression = "4^7;";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(4));
        assertEquals(analyser.getNextToken(0), new PowerOperatorToken('m'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(7));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken(';').toString());
    }

    public void testPower() {
        String expression = "6.^8;";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(6));
        assertEquals(analyser.getNextToken(0), new PowerOperatorToken('p'));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(8));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken(';').toString());
    }

    public void testFunction1() {
        String expression = "sin(2)";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new FunctionToken("sin"));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken('(').toString());
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(2));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken(')').toString());
    }

    public void testFunction2() {
        String expression = "min(1,2)";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new FunctionToken("min"));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken('(').toString());
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(1));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken(',').toString());
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(2));
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken(')').toString());
    }

    public void testReservedWord() {
        String expression = " break catch continue for foreach global if persistent return switch try while global isglobal ";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new FunctionToken("break"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("catch"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("continue"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("for"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("foreach"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("global"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("if"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("persistent"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("return"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("switch"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("try"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("while"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("global"));
        assertEquals(analyser.getNextToken(0), new FunctionToken("isglobal"));
    }

    public void testDelimiterWords() {
        String expression = "case default otherwise end else elseif return";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("case").toString());
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("default").toString());
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("otherwise").toString());
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("end").toString());
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("else").toString());
        assertEquals(analyser.getNextToken(0).toString(), new DelimiterToken("elseif").toString());
    }

    public void testComment1() {
        String expression = "3 //a number \n 4";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(3));
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(4));
    }

    public void testComment2() {
        String expression = "123 /*a number*/";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new DoubleNumberToken(123));
    }

    public void testSign01() {
        String expression = "-a";
        analyser.analyseExpression(expression);
        assertEquals(analyser.getNextToken(0), new AddSubOperatorToken('-'));
        assertEquals(analyser.getNextToken(0), new VariableToken("a"));
    }

    private boolean testResult(Vector result) {
        boolean okay = true;
        if (result.size() != expectedResult.size()) {
            okay = false;
            ErrorLogger.debugLine("!!!!!!!!! size of vectors do not match !!!!!!!!");
        }
        if (okay) {
            int length = result.size();
            for (int tokenNo = 0; tokenNo < length && okay; tokenNo++) {
                Token expectedValue = ((Token) expectedResult.elementAt(tokenNo));
                Token actualValue = ((Token) result.elementAt(tokenNo));
                if (expectedValue.getClass() != actualValue.getClass()) {
                    ErrorLogger.debugLine("!!!!!!!!! element classes do not match !!!!!!!!!");
                    ErrorLogger.debugLine("Expected " + expectedValue.getClass().toString() + " : actual " + actualValue.getClass().toString());
                    okay = false;
                } else if (!actualValue.toString().equals(expectedValue.toString())) {
                    ErrorLogger.debugLine("!!!!!!!!! elements values do not match !!!!!!!!!");
                    ErrorLogger.debugLine("Expected " + expectedValue.toString() + " : actual " + actualValue.toString());
                    okay = false;
                }
            }
        }
        return okay;
    }
}
