package net.nchelluri.projectEuler;

import junit.framework.TestCase;

public class PalindromeCheckerTest extends TestCase {

    public void testIsPalindrome() {
        assertTrue(PalindromeChecker.test("123321"));
        assertFalse(PalindromeChecker.test("123421"));
        assertTrue(PalindromeChecker.test("1234321"));
        assertTrue(PalindromeChecker.test("racecar"));
    }

    public void testReverse() {
        assertEquals("dog", PalindromeChecker.reverse("god"));
    }
}
