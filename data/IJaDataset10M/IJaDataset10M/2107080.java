package net.zeminvaders.lang;

import java.io.IOException;
import net.zeminvaders.lang.runtime.ZemBoolean;
import net.zeminvaders.lang.runtime.ZemNumber;
import net.zeminvaders.lang.runtime.ZemObject;
import net.zeminvaders.lang.runtime.ZemString;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * @author <a href="mailto:grom@zeminvaders.net">Cameron Zemek</a>
 */
public class InterpreterTest {

    private Interpreter interpreter = new Interpreter();

    /**
     * Helper method to test the result of an expression
     *
     * @param script Script to evaluate
     * @param expected The expected result
     */
    private void assertResult(String script, ZemObject expected) {
        try {
            ZemObject actual = interpreter.eval(script);
            assertEquals(expected, actual);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testArithmetic() {
        assertResult("n = 2 + 3;", new ZemNumber("5"));
        assertResult("x = 3 - 2;", new ZemNumber("1"));
        assertResult("x = 2 - 3;", new ZemNumber("-1"));
        assertResult("x = 2 * 3;", new ZemNumber("6"));
        assertResult("x = 2 ^ 3;", new ZemNumber("8"));
        assertResult("x = 3 ^ 2;", new ZemNumber("9"));
        assertResult("x = 4 / 2;", new ZemNumber("2"));
        assertResult("x = 3 % 2;", new ZemNumber("1"));
        assertResult("x = 2 % 2;", new ZemNumber("0"));
        assertResult("x = 1 % 2;", new ZemNumber("1"));
        assertResult("x = 4 % 2;", new ZemNumber("0"));
        assertResult("x = 5 % 2;", new ZemNumber("1"));
        assertResult("x = -2;", new ZemNumber("-2"));
        assertResult("x = 1 - -1;", new ZemNumber("2"));
        assertResult("x = 0.1 + 0.2;", new ZemNumber("0.3"));
        assertResult("x = 0.1 - 0.2;", new ZemNumber("-0.1"));
        assertResult("x = 1 / 2;", new ZemNumber("0.5"));
        assertResult("x = 2 * 1.5;", new ZemNumber("3"));
        assertResult("x = 3 % 1.5;", new ZemNumber("0"));
        assertResult("x = 2 + 3 * 4;", new ZemNumber("14"));
        assertResult("x = (2 + 3) * 4;", new ZemNumber("20"));
        assertResult("x = 2 * 3 ^ 2;", new ZemNumber("18"));
    }

    @Test
    public void testNumbers() {
        assertResult("x = 0xA;", new ZemNumber("10"));
        assertResult("x = 0xB;", new ZemNumber("11"));
        assertResult("x = 0xC;", new ZemNumber("12"));
        assertResult("x = 0xD;", new ZemNumber("13"));
        assertResult("x = 0xE;", new ZemNumber("14"));
        assertResult("x = 0xF;", new ZemNumber("15"));
        assertResult("x = 0x3BE;", new ZemNumber("958"));
        assertResult("x = 0o52;", new ZemNumber("42"));
        assertResult("x = 0b101;", new ZemNumber("5"));
    }

    @Test
    public void testBooleanLogic() {
        assertResult("x = true && true;", ZemBoolean.TRUE);
        assertResult("x = false && true;", ZemBoolean.FALSE);
        assertResult("x = true && false;", ZemBoolean.FALSE);
        assertResult("x = false && false;", ZemBoolean.FALSE);
        assertResult("x = true || true;", ZemBoolean.TRUE);
        assertResult("x = false || true;", ZemBoolean.TRUE);
        assertResult("x = true || false;", ZemBoolean.TRUE);
        assertResult("x = false || false;", ZemBoolean.FALSE);
        assertResult("x = !true;", ZemBoolean.FALSE);
        assertResult("x = !false;", ZemBoolean.TRUE);
        assertResult("x = false || true && false;", ZemBoolean.FALSE);
        assertResult("x = false || true && !false;", ZemBoolean.TRUE);
    }

    @Test
    public void testRelationOps() {
        assertResult("x = 2 < 3;", ZemBoolean.TRUE);
        assertResult("x = 3 < 2;", ZemBoolean.FALSE);
        assertResult("x = 2 <= 3;", ZemBoolean.TRUE);
        assertResult("x = 3 <= 3;", ZemBoolean.TRUE);
        assertResult("x = 4 <= 3;", ZemBoolean.FALSE);
        assertResult("x = 2 == 2;", ZemBoolean.TRUE);
        assertResult("x = 2 == 3;", ZemBoolean.FALSE);
        assertResult("x = 2 == 1 + 1;", ZemBoolean.TRUE);
        assertResult("x = 2 != 2;", ZemBoolean.FALSE);
        assertResult("x = 2 != 3;", ZemBoolean.TRUE);
        assertResult("x = 2 > 3;", ZemBoolean.FALSE);
        assertResult("x = 3 > 2;", ZemBoolean.TRUE);
        assertResult("x = 2 >= 3;", ZemBoolean.FALSE);
        assertResult("x = 3 >= 3;", ZemBoolean.TRUE);
        assertResult("x = 4 >= 3;", ZemBoolean.TRUE);
    }

    @Test
    public void testConcat() {
        assertResult("x = 'hello' ~ ' world!';", new ZemString("hello world!"));
    }

    @Test
    public void testIf() {
        assertResult("if (true) { x = 'then'; }", new ZemString("then"));
        assertResult("if (false) { x = 'then'; } else { x = 'else'; }", new ZemString("else"));
        assertResult("if (false) { x = 'then'; } else if (true) { x = 'elseif'; } else { x = 'else'; }", new ZemString("elseif"));
    }

    @Test
    public void testWhile() {
        assertResult("i = 0; while (i < 9) { i = i + 1; } x = i;", new ZemNumber("9"));
    }

    @Test
    public void testForeach() {
        assertResult("array = [1, 2, 3]; t = 0; foreach (array as element) { t = t + element; } x = t;", new ZemNumber("6"));
        assertResult("dict = {'apples':1, 'oranges':3}; t = 0; foreach (dict as k : v) { t = t + v; } x = t;", new ZemNumber("4"));
    }

    @Test
    public void testFunction() {
        assertResult("add = function(a, b) { return a + b; }; x = add(2, 3);", new ZemNumber("5"));
        assertResult("sum = function(array) { t = 0; foreach (array as element) { t = t + element; } return t; }; t = sum([1, 2, 3]);", new ZemNumber("6"));
        assertResult("x = add(2, add(2, 3));", new ZemNumber("7"));
        assertResult("test = function() { i = 0; while (i < 9) { i = i + 1; if (i == 5) { return i; } } return i; }; x = test();", new ZemNumber("5"));
    }

    @Test
    public void testFunctionCall() {
        assertResult("f = function() { return function() { return function() { return 'hello world'; }; }; }; x = f()()();", new ZemString("hello world"));
        assertResult("function(msg) { return msg; }('hello world');", new ZemString("hello world"));
        assertResult("x = function(msg) { return msg; }('hello world');", new ZemString("hello world"));
        assertResult("obj = { 'greet' : function() { return 'hello world'; } }; msg = obj['greet']();", new ZemString("hello world"));
    }
}
