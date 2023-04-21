package physicalc;

import java.lang.String;
import java.io.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;

public class InterpreterTest {

    /** An interpreter instance used by each test. */
    private Interpreter interpreter;

    @Before
    public void setupInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    public void alwaysPasses() {
        assertEquals("This test always passes.", true, true);
    }

    @Test
    public void doNothing() {
        assertPrints("Should do nothing.", "", "");
    }

    @Test
    public void arithmetic() {
        assertPrints("print(2 + 3)\n", "5\n");
        assertPrints("print(1.1 + 2.2)\n", "3.3\n");
        assertPrints("print(9 - 5)\n", "4\n");
        assertPrints("print(3 * 4)\n", "12\n");
        assertPrints("print(2 ^ 8)\n", "256\n");
        assertPrints("print(-3)\n", "-3\n");
        assertPrints("print(4 ^ -2)\n", "0.0625\n");
    }

    @Test
    public void strings() {
        assertPrints("print(\"foo\" + \"bar\")\n", "foobar\n");
    }

    @Test
    public void ifStmt() {
        assertPrints("if true then\n print(\"yes\")\n done\n", "yes\n");
        assertPrints("if false then\n print(\"yes\")\n done\n", "");
        assertPrints("if true then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if false then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if true then \n print(\"a\") \n elsif true then \n print(\"b\") \n elsif true then \n print(\"c\") \n elsif true then \n print(\"d\") \n else \n print(\"e\") \n done \n ", "a\n");
        assertPrints("if false then \n print(\"a\") \n elsif true then \n print(\"b\") \n elsif true then \n print(\"c\") \n elsif true then \n print(\"d\") \n else \n print(\"e\") \n done \n ", "b\n");
        assertPrints("if false then \n print(\"a\") \n elsif false then \n print(\"b\") \n elsif true then \n print(\"c\") \n elsif true then \n print(\"d\") \n else \n print(\"e\") \n done \n ", "c\n");
        assertPrints("if false then \n print(\"a\") \n elsif false then \n print(\"b\") \n elsif false then \n print(\"c\") \n elsif true then \n print(\"d\") \n else \n print(\"e\") \n done \n ", "d\n");
        assertPrints("if false then \n print(\"a\") \n elsif false then \n print(\"b\") \n elsif false then \n print(\"c\") \n elsif false then \n print(\"d\") \n else \n print(\"e\") \n done \n ", "e\n");
    }

    @Test
    public void relational() {
        assertPrints("if 3 < 4 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if 4 < 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 3 <= 4 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if 4 <= 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 3 > 4 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 4 > 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if 3 >= 4 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 4 >= 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if 3 = 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
        assertPrints("if 3 != 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 4 = 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "no\n");
        assertPrints("if 4 != 3 then\n print(\"yes\")\n else\n print(\"no\")\n done\n ", "yes\n");
    }

    @Test
    public void helloWorld() {
        assertPrints("print(\"Hello, world!\")\n", "Hello, world!\n");
    }

    @Test
    public void testWhile() {
        assertPrints("while true do \n print(\"a\") \n break \n print(\"b\") \n done \n", "a\n");
        assertPrints("while false do \n print(\"a\") \n break \n print(\"b\") \n done \n", "");
    }

    /** The suite() method is required for compatibility with older
     * JUnit versions. */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(InterpreterTest.class);
    }

    /** The assertPrints() method is an assertion that runs the
     * interpreter and checks that it prints out a certain string.
     *
     * @param message A string explaining what the test does.
     * @param program A string of Physicalc source code.  Remember the
     *                terminating line break or semicolon!
     * @param expected A string of what the interpreter should print.
     */
    private void assertPrints(String message, String program, String expected) {
        StringReader code = new StringReader(program);
        OutputStream output = new ByteArrayOutputStream();
        interpreter.setOutputStream(output);
        interpreter.eval(code);
        assertEquals(message, expected, output.toString());
    }

    private void assertPrints(String program, String expected) {
        String message = "Should execute: \n" + program;
        StringReader code = new StringReader(program);
        OutputStream output = new ByteArrayOutputStream();
        interpreter.setOutputStream(output);
        interpreter.eval(code);
        assertEquals(message, expected, output.toString());
    }
}
