package org.cyberaide.gridshell2.junit;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.JUnit4TestAdapter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.cyberaide.gridshell2.util.CliValues;
import org.cyberaide.gridshell2.util.CliExtended;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/** TestSuite for org.cyberaide.gridshell2.util.ExtendedCli */
public class CliExtendedTests {

    private final char flag1 = 't';

    private final char flag2 = 'x';

    private final String name1 = "name1";

    private final String name2 = "name2";

    private final String testValue1 = "value1";

    private final String testValue2 = "value2";

    private Option testOption1;

    private Option testOption2;

    private CliExtended cli;

    private final Logger subjectLogger = Logger.getLogger(CliExtended.class.getName());

    private final Level oldLevel = subjectLogger.getLevel();

    /** This method is run <b>before each</b> test. */
    @Before
    @SuppressWarnings("static-access")
    public void beforeTest() {
        subjectLogger.setLevel(oldLevel);
        cli = new CliExtended();
        testOption1 = OptionBuilder.withLongOpt(name1).hasArg().create(flag1);
        testOption2 = OptionBuilder.withLongOpt(name2).hasArg().create(flag2);
    }

    /** Checks if default parameters are working.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testDefaultParameter() throws ParseException {
        String[] args = { "" };
        cli.addOption(testOption1, testValue1);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue1, result.get(name1));
    }

    /** Checks if passing a real parameter works despite a default-value.
     * 
     * Background: Commandline-parameters have precedence over default-values.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testDefaultParameterWithCommandlineParameter() throws ParseException {
        String[] args = { "-" + flag1, testValue2 };
        cli.addOption(testOption1, testValue1);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue2, result.get(name1));
    }

    /** Checks if required arguments still work.
     * 
     * i.e.: empty cmdline ("") with a required parameter should throw Exception
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test(expected = MissingOptionException.class)
    public void testIfRequiredWithNonDefaultStillWorks() throws ParseException {
        String[] args = { "" };
        testOption1.setRequired(true);
        cli.addOption(testOption1, null);
        cli.parsePosixStyle(args);
    }

    /** Checks if an empty argument is refused for default-values.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test(expected = MissingArgumentException.class)
    public void testEmptyArgument() throws ParseException {
        String[] args = { "-" + flag1 };
        cli.addOption(testOption1, testValue1);
        cli.parsePosixStyle(args);
    }

    /** Test if options that take an argument but don't get one and have no default values work.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test(expected = MissingArgumentException.class)
    public void testOptionsThatTakeArgumentsButDontGetOne() throws ParseException {
        String[] args = { "-" + flag1 };
        cli.addOption(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
    }

    /** Checks if ordered Parameters are working
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOrderedParameters() throws ParseException {
        String[] args = { testValue1, testValue2 };
        cli.addOptionOrdered(testOption1);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue1, result.get(name1));
    }

    /** Checks if a flag has precedence over a ordered Option
     * 
     * i.e.: A cmdline "foo -t bar" must have bar as parameter for 't'
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOrderedParametersWithNormalAssignment() throws ParseException {
        String[] args = { testValue1, "-" + flag1, testValue2 };
        cli.addOptionOrdered(testOption1);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue2, result.get(name1));
    }

    /** Checks if a mixed case with ordered and flagged parameter works
     * 
     * i.e.: "foo -t bar" with optionorder {t, x} should result in {t->bar, x->foo}
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOrderedParametersWithMixedStyle() throws ParseException {
        String[] args = { testValue2, "-" + flag1, testValue1 };
        cli.addOptionOrdered(testOption1);
        cli.addOptionOrdered(testOption2);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue1, result.get(name1));
        assertEquals(testValue2, result.get(name2));
    }

    /** Checks if a flag has precedence over a ordered Option
     * 
     * i.e.: "foo" with optionorder {t, x} must work
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOrderedParametersWithTooFewOrdereds() throws ParseException {
        String[] args = { testValue1 };
        cli.addOptionOrdered(testOption1);
        cli.addOptionOrdered(testOption2);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue1, result.get(name1));
        assertNull(result.get(name2));
    }

    /** Checks required parameters as ordered Parameters with ordered cmdline
     * 
     * i.e.: "foo" with required and ordered parameter t must result in {t->foo}
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOrderedParametersWithRequiredParameters1() throws ParseException {
        String[] args = { testValue1 };
        testOption1.setRequired(true);
        cli.addOptionOrdered(testOption1);
        CliValues result = cli.parsePosixStyle(args);
        assertEquals(testValue1, result.get(name1));
    }

    /** Checks required parameters as ordered Parameters
     * 
     * i.e.: "" with required and ordered parameter must throw Exception
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test(expected = MissingOptionException.class)
    public void testOrderedParametersWithRequiredParameters2() throws ParseException {
        String[] args = {};
        testOption1.setRequired(true);
        cli.addOptionOrdered(testOption1);
        cli.parsePosixStyle(args);
    }

    /** Checks if multiple arguments are working
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testMultipleArguments() throws ParseException {
        String[] args = { "-" + flag1, testValue1, testValue2 };
        testOption1.setArgs(2);
        cli.addOption(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        List<String> result = cmd.getMulti(name1);
        assertTrue(result.size() == 2);
        assertEquals(result.get(0), testValue1);
        assertEquals(result.get(1), testValue2);
    }

    /** Checks if unlimited possible arguments are working
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testUnlimitedOrderedArguments() throws ParseException {
        String[] args = { testValue1, testValue2 };
        testOption1.setArgs(Option.UNLIMITED_VALUES);
        cli.addOptionOrdered(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        List<String> result = cmd.getMulti(name1);
        assertTrue(result.size() == 2);
        assertEquals(result.get(0), testValue1);
        assertEquals(result.get(1), testValue2);
    }

    /** Checks if multiple arguments are working with ordered options
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testMultipleOrderedArguments() throws ParseException {
        String[] args = { testValue1, testValue2 };
        testOption1.setArgs(2);
        cli.addOptionOrdered(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        List<String> result = cmd.getMulti(name1);
        assertTrue(result.size() == 2);
        assertEquals(result.get(0), testValue1);
        assertEquals(result.get(1), testValue2);
    }

    /** Test if options, that don't require arguments are working.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testOptionsWithoutArguments() throws ParseException {
        String[] args = { "-" + flag1 };
        testOption1.setArgs(0);
        cli.addOption(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        assertTrue(cmd.isSet(name1));
        assertEquals(cmd.get(name1), "");
    }

    /** Test if the list of required Options works. */
    @Test
    public void testGetRequiredOptions() {
        String[] args = { "-" + flag1 };
        testOption1.setRequired(true);
        cli.addOption(testOption1);
        assertTrue(cli.getRequiredOptions().get(0).equals(testOption1));
    }

    /** Test missing LongOpt.
     * 
     * Disables the Logger of the test-subject for this run.
     * 
     * @throws org.apache.commons.cli.ParseException 
     */
    @Test
    public void testMissingLongOpt() throws ParseException {
        subjectLogger.setLevel(Level.WARNING);
        String[] args = { "-" + flag1, testValue1 };
        testOption1.setLongOpt(null);
        cli.addOption(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        assertEquals(cmd.get(String.valueOf(flag1)), testValue1);
    }

    /** Empty arguments should not satify a required option.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test(expected = MissingOptionException.class)
    public void testOrderedArgumentsWithEmptyArgument() throws ParseException {
        String[] args = { "" };
        testOption1.setRequired(true);
        cli.addOptionOrdered(testOption1);
        cli.parsePosixStyle(args);
    }

    /** Test if a flagged option works (they take no arguments and can by on or off).
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testFlagOptions() throws ParseException {
        String[] args = { "-" + flag1 };
        testOption1.setArgs(0);
        cli.addOptionOrdered(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        assertEquals(cmd.get(name1), "");
    }

    /** Test if isSet() returns false when the arg is not set but there is an "" in the args.
     * 
     * @throws org.apache.commons.cli.ParseException
     */
    @Test
    public void testIsSet() throws ParseException {
        String[] args = { "" };
        cli.addOptionOrdered(testOption1);
        CliValues cmd = cli.parsePosixStyle(args);
        assertFalse(cmd.isSet(name1));
    }

    /** Utility-method to support JUnit3-Testrunners.
     * 
     * @return A TestAdapter for junit3
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CliExtendedTests.class);
    }
}
