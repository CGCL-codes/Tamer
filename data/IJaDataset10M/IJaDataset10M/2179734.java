package au.edu.archer.metadata.msf.mss.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import junit.framework.TestCase;
import au.edu.archer.metadata.msf.mss.ElementReference;
import au.edu.archer.metadata.msf.mss.Expression;
import au.edu.archer.metadata.msf.mss.Identifier;
import au.edu.archer.metadata.msf.mss.Literal;
import au.edu.archer.metadata.msf.mss.MetadataElement;
import au.edu.archer.metadata.msf.mss.MetadataSchema;
import au.edu.archer.metadata.msf.mss.Node;
import au.edu.archer.metadata.msf.mss.Operation;
import au.edu.archer.metadata.msf.mss.ScopedOperation;
import au.edu.archer.metadata.msf.mss.Self;
import au.edu.archer.metadata.msf.mss.SetLiteral;
import au.edu.archer.metadata.msf.mss.parser.ExpressionParserFactory;
import au.edu.archer.metadata.msf.mss.parser.ExpressionSyntaxException;
import au.edu.archer.metadata.msf.mss.util.MSSResourceUtils;
import au.edu.archer.metadata.msf.mss.util.Value;

/**
 * Unit tests for the 'simple' Expression parser and unparser
 * 
 * @author scrawley@itee.uq.edu.au
 */
public class SimpleExpressionParserTest extends TestCase {

    private MetadataSchema testSchema;

    private MetadataElement testElement;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        testSchema = loadTestSchema("NestedDataCollection_v1.0-flattened.mss");
        for (MetadataElement child : testSchema.getElements()) {
            if (child.getName().toString().equals("observation")) {
                testElement = child;
            }
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSimple() throws ExpressionSyntaxException {
        assertTrue(doTest("self") instanceof Self);
        assertTrue(doTest(" self ", "self") instanceof Self);
        doTestLiteral("1", Value.INTEGER);
        doTestLiteral("-1", Value.INTEGER);
        doTestLiteral("-1234", Value.INTEGER);
        doTestLiteral("-1234.", Value.DECIMAL);
        doTestLiteral("-1.0", Value.DECIMAL);
        doTestLiteral("-.0", Value.DECIMAL);
        doTestLiteral("1E99", Value.DECIMAL);
        doTestLiteral("1.0E99", Value.DECIMAL);
        doTestLiteral("1.0E+99", Value.DECIMAL);
        doTestLiteral("1.0E-99", Value.DECIMAL);
        doTestLiteral("\"1\"", Value.STRING);
        doTestLiteral("\"\"", Value.STRING);
    }

    public void testSetLiterals() throws ExpressionSyntaxException {
        assertTrue(doTest("{}") instanceof SetLiteral);
        assertTrue(doTest("{1}") instanceof SetLiteral);
        assertTrue(doTest("{1, 2}") instanceof SetLiteral);
        assertTrue(doTest("{1,2}", "{1, 2}") instanceof SetLiteral);
    }

    public void testOperations() throws ExpressionSyntaxException {
        assertTrue(doTest("Equals(1, 1)") instanceof Operation);
        assertTrue(doTest("Equals(1, \"1\")") instanceof Operation);
        assertTrue(doTest("Equals(1, {\"1\", 1})") instanceof Operation);
        assertTrue(doTest("And(Equals(1, 1), Equals(2, 2))") instanceof Operation);
        assertTrue(doTest("Equals(Add(1, 1), 2)") instanceof Operation);
    }

    public void testElementReferences() throws ExpressionSyntaxException {
        assertTrue(doTest("root.collectionDate", testSchema) instanceof ElementReference);
        assertTrue(doTest("root.radiation.diffractionSource", testSchema) instanceof ElementReference);
        assertTrue(doTest("root.radiation[2].diffractionSource", testSchema) instanceof ElementReference);
        assertTrue(doTest("root.observation[2].scan.numberOfFrames[2]", testSchema) instanceof ElementReference);
        assertTrue(doTest("self.scan.numberOfFrames[2]", testElement) instanceof ElementReference);
    }

    public void testLets() throws ExpressionSyntaxException {
        assertTrue(doTest("s", testSchema) instanceof Identifier);
        assertTrue(doTest("Let(s = self : s)", testSchema) instanceof ScopedOperation);
        assertTrue(doTest("Let(s = self : And(s, s))", testSchema) instanceof ScopedOperation);
        assertTrue(doTest("Let(s = And(self, self) : And(s, s))", testSchema) instanceof ScopedOperation);
    }

    public void testIterations() throws ExpressionSyntaxException {
        assertTrue(doTest("ForAll(s = self : s)", testSchema) instanceof ScopedOperation);
        assertTrue(doTest("Exists(s = self : And(s, s))", testSchema) instanceof ScopedOperation);
        assertTrue(doTest("Map(s = And(self, self) : And(s, s))", testSchema) instanceof ScopedOperation);
        assertTrue(doTest("Reduce(s = {1, 2, 3}, t = 0 : Add(t, s))", testSchema) instanceof ScopedOperation);
    }

    public void testComplicatedOperations() throws ExpressionSyntaxException {
        assertTrue(doTest("Equals(1, self)") instanceof Operation);
        assertTrue(doTest("And(Equals(1, self), Equals(2, 2), self)") instanceof Operation);
        assertTrue(doTest("And(Equals(root.radiation[2], self), Equals(2, root.observation[2].scan), self)", testSchema) instanceof Operation);
    }

    public void testBadLiterals() throws ExpressionSyntaxException {
        doTestBad("+1", "Misplaced token: expecting a word, string, number or '{' but got '+'", 1);
        doTestBad("1a", "Unexpected token at end: <word> 'a'", 2);
        doTestBad("1.a", "Unexpected token at end: <word> 'a'", 3);
        doTestBad("1.e 99", "Unexpected token at end: <word> 'e'", 3);
        doTestBad("1..2", "Unexpected token at end: <decimal> '.2'", 4);
        doTestBad("1.e99a", "Unexpected token at end: <word> 'a'", 6);
        doTestBad("-.e99", "Misplaced token: expecting a word, string, number or '{' but got '-'", 1);
        doTestBad(".", "Misplaced token: expecting a word, string, number or '{' but got '.'", 1);
        doTestBad("\"hello", "Unterminated string literal", 6);
    }

    public void testBadSetLiterals() throws ExpressionSyntaxException {
        doTestBad("{", "Misplaced token: expecting a number or string literal but got <EOF>", 1);
        doTestBad("{1", "Misplaced token: expecting ',' or '}' but got <EOF>", 2);
        doTestBad("{a", "Misplaced token: expecting a number or string literal but got <word> 'a'", 2);
        doTestBad("{,", "Misplaced token: expecting a number or string literal but got ','", 2);
        doTestBad("{1,", "Misplaced token: expecting a number or string literal but got <EOF>", 3);
        doTestBad("{1 1,", "Misplaced token: expecting ',' or '}' but got <integer> '1'", 4);
    }

    public void testBadOperations() throws ExpressionSyntaxException {
        doTestBad("Equals", "Misplaced token: expecting '(' but got <EOF>", 6);
        doTestBad("Equals(", "Misplaced token: expecting a word, string, number or '{' but got <EOF>", 7);
        doTestBad("Equals()", "Misplaced token: expecting a word, string, number or '{' but got ')'", 8);
        doTestBad("Equals(1", "Misplaced token: expecting ',' or ')' but got <EOF>", 8);
        doTestBad("Equals(1,", "Misplaced token: expecting a word, string, number or '{' but got <EOF>", 9);
    }

    public void testBadReferences() throws ExpressionSyntaxException {
        doTestBad("root", "Misplaced token: expecting '.' but got <EOF>", testSchema, 4);
        doTestBad("root.", "Misplaced token: expecting an element name but got <EOF>", testSchema, 5);
        doTestBad("root.a", "Malformed ElementPath: NestedDataCollection has no child called 'a'", testSchema, 6);
        doTestBad("root.radiation.a", "Malformed ElementPath: radiation has no child called 'a'", testSchema, 16);
        doTestBad("root.radiation.", "Misplaced token: expecting an element name but got <EOF>", testSchema, 15);
        doTestBad("root.radiation[.", "Misplaced token: expecting a word, string, number or '{' but got '.'", testSchema, 16);
        doTestBad("root.radiation[1.", "Misplaced token: expecting ']' but got <EOF>", testSchema, 17);
        doTestBad("root.radiation[1].", "Misplaced token: expecting an element name but got <EOF>", testSchema, 18);
        doTestBad("root.radiation[1].a", "Malformed ElementPath: radiation has no child called 'a'", testSchema, 19);
        doTestBad("root.radiation[1].diffractionSource.a", "Malformed ElementPath: diffractionSource cannot have any children", testSchema, 37);
        doTestBad("self.", "Misplaced token: expecting an element name but got <EOF>", testSchema, 5);
        doTestBad("self.a", "Cannot use a 'self-relative' ElementReference in a schema Constraint", testSchema, 6);
        doTestBad("self.a", "Malformed ElementPath: observation has no child called 'a'", testElement, 6);
        doTestBad("root.a", "Cannot analyse this expression as it currently has no parent MetadataSchema", null, 6);
    }

    public void testBadLets() throws ExpressionSyntaxException {
        doTestBad("Let", "Misplaced token: expecting '(' but got <EOF>", testSchema, 3);
        doTestBad("Let(", "Misplaced token: expecting a name but got <EOF>", testSchema, 4);
        doTestBad("Let(x", "Misplaced token: expecting '=' but got <EOF>", testSchema, 5);
        doTestBad("Let(x=", "Misplaced token: expecting a word, string, number or '{' but got <EOF>", testSchema, 6);
        doTestBad("Let(x=self", "Misplaced token: expecting ',' or ':' but got <EOF>", testSchema, 10);
        doTestBad("Let(x=self:", "Misplaced token: expecting a word, string, number or '{' but got <EOF>", testSchema, 11);
        doTestBad("Let(x=self:x", "Misplaced token: expecting ')' but got <EOF>", testSchema, 12);
    }

    private Expression doTest(String exprText) throws ExpressionSyntaxException {
        return doTest(exprText, exprText, null);
    }

    private Expression doTest(String exprText, Node context) throws ExpressionSyntaxException {
        return doTest(exprText, exprText, context);
    }

    private Expression doTest(String exprText, String expected) throws ExpressionSyntaxException {
        return doTest(exprText, expected, null);
    }

    /**
     * This method parses an expression in a given 'context', unparses it, and then compares
     * the unparsed string with the 'expected' string.
     * 
     * @param exprText the input expression
     * @param expected the expected output
     * @param context the Node that supplies the context for building ElementReferences.
     */
    private Expression doTest(String exprText, String expected, Node context) throws ExpressionSyntaxException {
        Expression expr = pf().createParser().parse(exprText, context);
        String unparsed = pf().createUnparser().unparse(expr);
        assertEquals(expected, unparsed);
        return expr;
    }

    private ExpressionParserFactory pf() {
        return new ExpressionParserFactory(ExpressionParserFactory.SIMPLE);
    }

    private void doTestBad(String exprText, String expectedErrorMessage, int expectedPos) {
        doTestBad(exprText, expectedErrorMessage, null, expectedPos);
    }

    /**
     * This method parses an (invalid) expression in the given 'context', then
     * compares the resulting error message withe expected one.  (If the expected
     * error message is an empty String, the actual message is written to 
     * standard error, from whence you can cut-and-paste it into the code.)
     * 
     * @param exprText the input expression
     * @param expectedErrorMessage the expected error message 
     * @param expectedPos the expected error position
     * @param context the Node that supplies the context for building ElementReferences.
     */
    private void doTestBad(String exprText, String expectedErrorMessage, Node context, int expectedPos) {
        try {
            pf().createParser().parse(exprText, context);
            fail("Did not fail");
        } catch (ExpressionSyntaxException ex) {
            if (expectedErrorMessage.length() == 0) {
                System.err.println(ex.getMessage());
            } else {
                assertEquals(expectedErrorMessage, ex.getMessage());
                assertEquals(expectedPos, ex.getPos());
            }
        }
    }

    private void doTestLiteral(String exprText, int kind) throws ExpressionSyntaxException {
        Expression expr = pf().createParser().parse(exprText, null);
        String unparsed = pf().createUnparser().unparse(expr);
        assertTrue(expr instanceof Literal);
        assertEquals(kind, ((Literal) expr).getValue().getKind());
        assertEquals(exprText, unparsed);
    }

    private MetadataSchema loadTestSchema(String resourceName) throws IOException {
        Reader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(resourceName)));
            StringBuilder sb = new StringBuilder(10000);
            int ch = r.read();
            while (ch != -1) {
                sb.append((char) ch);
                ch = r.read();
            }
            return (MetadataSchema) MSSResourceUtils.loadNode(sb.toString());
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
