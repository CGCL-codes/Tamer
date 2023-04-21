package org.databene.feed4junit.beanval.equivalence;

import java.util.HashSet;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.databene.feed4junit.Feed4JUnitTestCase;
import org.databene.feed4junit.Feeder;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests String related Benerator annotations.<br/><br/>
 * Created: 04.07.2011 01:18:46
 * @since 1.0
 * @author Volker Bergmann
 */
@RunWith(Feeder.class)
public class StringTest extends Feed4JUnitTestCase {

    static HashSet<String> nullables = new HashSet<String>();

    @Test
    public void testNullable(@Pattern(regexp = "a") String name) {
        logger.debug("testSizeDistribution -> " + name);
        nullables.add(name);
    }

    @AfterClass
    public static void verifyNullable() {
        assertObjectSet(nullables, null, "a");
    }

    static HashSet<String> lengths = new HashSet<String>();

    @Test
    public void testLength(@NotNull @Pattern(regexp = "X{1,7}") String value) {
        lengths.add(value);
    }

    @AfterClass
    public static void verifyLengths() {
        assertObjectSet(lengths, "X", "XX", "XXXX", "XXXXXX", "XXXXXXX");
    }

    static HashSet<String> charClasses = new HashSet<String>();

    @Test
    public void testCharClasses(@NotNull @Pattern(regexp = "[0-9A-Z!#]") String value) {
        charClasses.add(value);
    }

    @AfterClass
    public static void verifyCharClasses() {
        assertObjectSet(charClasses, "0", "5", "9", "A", "N", "Z", "!", "#");
    }

    static HashSet<String> alternatives = new HashSet<String>();

    @Test
    public void testAlternatives(@NotNull @Pattern(regexp = "X|Y") String value) {
        alternatives.add(value);
    }

    @AfterClass
    public static void verifyAlternatives() {
        assertObjectSet(alternatives, "X", "Y");
    }

    static HashSet<String> combinations = new HashSet<String>();

    @Test
    public void testCombinations(@NotNull @Pattern(regexp = "(A|B)[12]") String value) {
        combinations.add(value);
    }

    @AfterClass
    public static void verifyCombinations() {
        assertObjectSet(combinations, "A1", "A2", "B1", "B2");
    }

    static HashSet<String> groups = new HashSet<String>();

    @Test
    public void testGroups(@NotNull @Pattern(regexp = "(AB){1,3}") String value) {
        groups.add(value);
    }

    @AfterClass
    public static void verifyGroups() {
        assertObjectSet(groups, "AB", "ABAB", "ABABAB");
    }
}
