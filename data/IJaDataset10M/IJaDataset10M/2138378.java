package com.grubby.generators;

import java.util.Random;
import junit.framework.*;

/**
 *
 * @author AHARMEL
 */
public class RandomGeneratorsTest extends TestCase {

    public RandomGeneratorsTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of getRandom method, of class com.grubby.generators.RandomGenerators.
     */
    public void testGetRandom() {
        System.out.println("getRandom");
        Object[] list = null;
        try {
            RandomGenerators.getRandom(list);
            fail("An IllegalArgumentException was not thrown when the array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        Object[] list2 = { "String 1", "String 2", "String 3" };
        Object result = RandomGenerators.getRandom(list2);
        try {
            String castResult = (String) result;
            assertTrue("The result cannot be from the provided options", castResult.startsWith("String "));
        } catch (ClassCastException ex) {
            fail("The result was not a String as expected");
        }
        Random rand = null;
        try {
            RandomGenerators.getRandom(list2, rand);
            fail("An IllegalArgumentException was not thrown when the Random was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        int a = 1;
        int b = 1;
        int expectedResult = 1;
        int intResult = RandomGenerators.getRandom(a, b);
        assertEquals("The result was not as expected: " + intResult, expectedResult, intResult);
        int c = 1;
        int d = 10;
        intResult = RandomGenerators.getRandom(c, d);
        assertTrue("The result was not as expected: " + intResult, intResult >= c);
        assertTrue("The result was not as expected: " + intResult, intResult <= d);
        int e = -10;
        int f = -1;
        intResult = RandomGenerators.getRandom(e, f);
        assertTrue("The result was not as expected: " + intResult, intResult >= e);
        assertTrue("The result was not as expected: " + intResult, intResult <= f);
        int g = 10;
        int h = 1;
        try {
            RandomGenerators.getRandom(g, h);
            fail("An IllegalArgumentException was not thrown despite the min and max being in the wrong order");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = null;
        try {
            RandomGenerators.getRandom(g, h, rand);
            fail("An IllegalArgumentException was not thrown when the rand was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = new Random();
        int i = -10;
        int j = -1;
        intResult = RandomGenerators.getRandom(i, j, rand);
        assertTrue("The result was not as expected: " + intResult, intResult >= i);
        assertTrue("The result was not as expected: " + intResult, intResult <= j);
        int[] intList = null;
        try {
            RandomGenerators.getRandom(list);
            fail("An IllegalArgumentException was not thrown when the array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        int[] intList2 = { 1, 2, 3 };
        int intListResult = RandomGenerators.getRandom(intList2);
        assertTrue("The result cannot be from the provided options", intListResult > 0);
        assertTrue("The result cannot be from the provided options", intListResult < 4);
        rand = null;
        try {
            RandomGenerators.getRandom(intList2, rand);
            fail("An IllegalArgumentException was not thrown when the Random was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        long longA = 1L;
        long longB = 1L;
        long expectedLongResult = 1L;
        long longResult = RandomGenerators.getRandom(longA, longB);
        assertEquals("The result was not as expected: " + longResult, expectedLongResult, longResult);
        long longC = 1L;
        long longD = 10L;
        longResult = RandomGenerators.getRandom(longC, longD);
        assertTrue("The result was not as expected: " + longResult, longResult >= longC);
        assertTrue("The result was not as expected: " + longResult, longResult <= longD);
        long longE = -10L;
        long longF = -1L;
        longResult = RandomGenerators.getRandom(longE, longF);
        assertTrue("The result was not as expected: " + longResult, longResult >= longE);
        assertTrue("The result was not as expected: " + longResult, longResult <= longF);
        long longG = 10L;
        long longH = 1L;
        try {
            RandomGenerators.getRandom(longG, longH);
            fail("An IllegalArgumentException was not thrown despite the min and max being in the wrong order");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = null;
        try {
            RandomGenerators.getRandom(longG, longH, rand);
            fail("An IllegalArgumentException was not thrown when the rand was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = new Random();
        long longI = -10L;
        long longJ = -1L;
        longResult = RandomGenerators.getRandom(longI, longJ, rand);
        assertTrue("The result was not as expected: " + longResult, longResult >= longI);
        assertTrue("The result was not as expected: " + longResult, longResult <= longJ);
        double doubleA = 1D;
        double doubleB = 1D;
        double expectedDoubleResult = 1D;
        double doubleResult = RandomGenerators.getRandom(doubleA, doubleB);
        assertEquals("The result was not as expected: " + doubleResult, expectedDoubleResult, doubleResult);
        double doubleC = 1D;
        double doubleD = 10D;
        doubleResult = RandomGenerators.getRandom(doubleC, doubleD);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult >= doubleC);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult <= doubleD);
        double doubleE = -10D;
        double doubleF = -1D;
        doubleResult = RandomGenerators.getRandom(doubleE, doubleF);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult >= doubleE);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult <= doubleF);
        double doubleG = 10D;
        double doubleH = 1D;
        try {
            RandomGenerators.getRandom(doubleG, doubleH);
            fail("An IllegalArgumentException was not thrown despite the min and max being in the wrong order");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = null;
        try {
            RandomGenerators.getRandom(doubleG, doubleH, rand);
            fail("An IllegalArgumentException was not thrown when the rand was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        rand = new Random();
        double doubleI = -10D;
        double doubleJ = -1D;
        doubleResult = RandomGenerators.getRandom(doubleI, doubleJ, rand);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult >= doubleI);
        assertTrue("The result was not as expected: " + doubleResult, doubleResult <= doubleJ);
    }

    /**
     * Test of getRandomBoolean method, of class com.grubby.generators.RandomGenerators.
     */
    public void testGetRandomBoolean() {
        System.out.println("getRandomBoolean");
        Boolean result = RandomGenerators.getRandomBoolean();
        assertNotNull("A null result was returned", result);
        assertTrue("The returned object was not a Boolean", result.getClass().toString().equals("class java.lang.Boolean"));
    }

    /**
     * Test of getRandomCharFromString method, of class com.grubby.generators.RandomGenerators.
     */
    public void testGetRandomCharFromString() {
        System.out.println("getRandomCharFromString");
        String myString = null;
        try {
            RandomGenerators.getRandomCharFromString(myString);
            fail("An IllegalArgumentException was not thrown when the String was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        myString = "abcde12345";
        char result = RandomGenerators.getRandomCharFromString(myString);
        assertTrue("The result cannot be from the provided String", myString.indexOf(result) != -1);
        Random rand = null;
        try {
            RandomGenerators.getRandomCharFromString(myString, rand);
            fail("An IllegalArgumentException was not thrown when the Random was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
    }

    /**
     * Test of getRandomStringFromArray method, of class com.grubby.generators.RandomGenerators.
     */
    public void testGetRandomStringFromArray() {
        System.out.println("getRandomStringFromArray");
        String[] myStrings1 = null;
        try {
            RandomGenerators.getRandomStringFromArray(myStrings1);
            fail("An IllegalArgumentException was not thrown when the array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        String[] myStrings2 = { "String A", "String B", "String C" };
        String result = RandomGenerators.getRandomStringFromArray(myStrings2);
        assertTrue("The result cannot be from the provided array", result.startsWith("String "));
        Random rand = null;
        try {
            RandomGenerators.getRandomStringFromArray(myStrings2, rand);
            fail("An IllegalArgumentException was not thrown when the Random was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
    }

    /**
     * Test of getStringFromPattern method, of class com.grubby.generators.RandomGenerators.
     */
    public void testGetStringFromPattern() {
        System.out.println("getStringFromPattern");
        String aPattern = null;
        try {
            RandomGenerators.getStringFromPattern(aPattern);
            fail("An IllegalArgumentException was not thrown when the String was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        aPattern = "";
        String myResult1 = RandomGenerators.getStringFromPattern(aPattern);
        assertTrue("The String returned was not of length 0", myResult1.length() == 0);
        aPattern = "AABBNNZZ";
        String myResult2 = RandomGenerators.getStringFromPattern(aPattern);
        System.out.println("char: " + myResult2.charAt(4));
        System.out.println("char: " + myResult2.charAt(5));
        assertTrue("The fifth character wasn't a number", myResult2.charAt(4) >= '0');
        assertTrue("The fifth character wasn't a number", myResult2.charAt(4) <= '9');
        assertTrue("The sixth character wasn't a number", myResult2.charAt(5) >= '0');
        assertTrue("The sixth character wasn't a number", myResult2.charAt(5) <= '9');
    }

    /**
     * Test of selectRandomPercentWeighted method, of class com.grubby.generators.RandomGenerators.
     */
    public void testSelectRandomPercentWeighted() {
        System.out.println("selectRandomPercentWeighted");
        int[] weighting = null;
        Object[] options = null;
        try {
            RandomGenerators.selectRandomPercentWeighted(weighting, options);
            fail("An IllegalArgumentException was not thrown when the weighting array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        int[] weighting2 = { 20, 20, 20 };
        try {
            RandomGenerators.selectRandomPercentWeighted(weighting2, options);
            fail("An IllegalArgumentException was not thrown when the options array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        Object[] options2 = { "option A", "option B" };
        try {
            RandomGenerators.selectRandomPercentWeighted(weighting2, options);
            fail("An IllegalArgumentException was not thrown when the options array was null");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        Object[] options3 = { "option A", "option B", "option C" };
        try {
            RandomGenerators.selectRandomPercentWeighted(weighting2, options3);
            fail("An IllegalArgumentException was not thrown when the weightings don't add up");
        } catch (IllegalArgumentException ex) {
            assertTrue("A IllegalArgumentException was thrown as expected", true);
        }
        int[] weighting3 = { 40, 30, 30 };
        try {
            String result = (String) RandomGenerators.selectRandomPercentWeighted(weighting3, options3);
            assertTrue("The result cannot be from the provided array", result.startsWith("option "));
        } catch (ClassCastException e) {
            fail("The result was not a String");
        }
    }
}
