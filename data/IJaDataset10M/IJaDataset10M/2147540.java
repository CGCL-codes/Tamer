package org.norecess.antlr;

import static org.junit.Assert.assertTrue;
import static org.norecess.antlr.Assert.assertToken;
import static org.norecess.antlr.Assert.refuteToken;
import org.junit.Before;
import org.junit.Test;

public class TestingSimpleLexerTest {

    private ANTLRTester myTester;

    @Before
    public void setUp() {
        myTester = new ANTLRTester(new SimpleFrontEnd());
    }

    @Test
    public void shouldRecognizeDigits() {
        assertToken(TestingSimpleLexer.DIGIT, "5", myTester.scanInput("5"));
        assertToken(TestingSimpleLexer.DIGIT, "9", myTester.scanInput("9"));
        assertToken(TestingSimpleLexer.DIGIT, "8", myTester.scanInput("8"));
    }

    @Test
    public void shouldRecognizeLetters() {
        assertToken(TestingSimpleLexer.LETTER, "a", myTester.scanInput("a"));
        assertToken(TestingSimpleLexer.LETTER, "x", myTester.scanInput("x"));
        assertToken(TestingSimpleLexer.LETTER, "q", myTester.scanInput("q"));
    }

    @Test
    public void shouldFailToken() {
        refuteToken(TestingSimpleLexer.DIGIT, myTester.scanInput("x"));
        refuteToken(TestingSimpleLexer.DIGIT, myTester.scanInput("55"));
        refuteToken(TestingSimpleLexer.DIGIT, myTester.scanInput("."));
    }

    @Test
    public void shouldFailTypeMatch() {
        try {
            assertToken(TestingSimpleLexer.EOF, "5", myTester.scanInput("5"));
            throw new Error("should have failed to match type");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().startsWith("failed to match token types,"));
        }
    }

    @Test
    public void shouldFailTextMatch() {
        try {
            assertToken(TestingSimpleLexer.DIGIT, "xxx", myTester.scanInput("5"));
            throw new Error("should have failed to match text");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().startsWith("failed to match token text,"));
        }
    }

    @Test
    public void shouldFailWhenStandardErrorAccessed() {
        try {
            myTester.scanInput(".").getSingleToken();
            throw new Error("should have failed when error sent to standard error");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().startsWith("unexpected error output"));
        }
    }

    @Test
    public void shouldCheckForSingleToken() {
        try {
            myTester.scanInput("5x").getSingleToken();
            throw new Error("should have failed when two (or more) tokens");
        } catch (AssertionError e) {
            assertTrue(e.getMessage().startsWith("failed to match EOF"));
        }
    }
}
