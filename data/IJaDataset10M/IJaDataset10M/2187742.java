package test.net.sf.japi.io.args;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import net.sf.japi.io.args.ArgParser;
import net.sf.japi.io.args.ArgumentFileNotFoundException;
import net.sf.japi.io.args.BasicCommand;
import net.sf.japi.io.args.Command;
import net.sf.japi.io.args.MissingArgumentException;
import net.sf.japi.io.args.Option;
import net.sf.japi.io.args.OptionType;
import net.sf.japi.io.args.RequiredOptionsMissingException;
import net.sf.japi.io.args.TerminalException;
import net.sf.japi.io.args.UnknownOptionException;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for {@link ArgParser}.
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ArgParserTest {

    /** The path that needs to be used for the test to find its files. */
    private static String pathPrefix;

    /** Finds the {@link #pathPrefix}. */
    @BeforeClass
    public static void initPathPrefix() {
        final String testPathname = "src/tst/test/net/sf/japi/io/args/ArgParserTest_OptionsFileSingleLine";
        final File f = new File(testPathname);
        if (f.exists()) {
            pathPrefix = "";
        } else {
            pathPrefix = "libs/argparser/trunk/";
            assert new File(pathPrefix, testPathname).exists();
        }
    }

    /** Tests that {@link ArgParser#getOptionMethods(Command)} works. */
    @Test
    public void testGetOptionMethodsCommand() {
        final Command commandDummy = new CommandDummy();
        final Set<Method> optionMethods = ArgParser.getOptionMethods(commandDummy);
        Assert.assertFalse("There are some option methods in CommandDummy.", optionMethods.isEmpty());
    }

    /** Tests that {@link ArgParser#getOptionMethods(Class)} works. */
    @Test
    public void testGetOptionMethodsClass() {
        final Set<Method> optionMethods = ArgParser.getOptionMethods(CommandDummy.class);
        Assert.assertFalse("There are some option methods in CommandDummy.", optionMethods.isEmpty());
    }

    /** Tests that {@link ArgParser#simpleParseAndRun(Command, String[])} works. */
    @SuppressWarnings({ "JUnitTestMethodWithNoAssertions" })
    @Test
    public void testSimpleParseAndRun() {
        final Command commandDummy = new CommandDummy();
        ArgParser.simpleParseAndRun(commandDummy);
    }

    /**
     * Tests that {@link ArgParser#parseAndRun(Command, String[])} works.
     * @throws ArgumentFileNotFoundException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     */
    @SuppressWarnings({ "JUnitTestMethodWithNoAssertions" })
    @Test
    public void testParseAndRun() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command commandDummy = new CommandDummy();
        ArgParser.parseAndRun(commandDummy);
    }

    /**
     * Tests that supplying a required option with argument in short form works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testCommandWithShortOption() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-i", "fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that supplying a required option with argument in long form with separate argument works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testCommandWithLongOption() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "--input", "fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that supplying a required option with argument in long form with integrated argument works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testCommandWithLongOptEq() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "--input=fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that it's detected that a required option is missing.
     * @throws RequiredOptionsMissingException (expected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = RequiredOptionsMissingException.class)
    public void testCommandRequiredOptionMissing() throws RequiredOptionsMissingException, TerminalException, UnknownOptionException, MissingArgumentException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        try {
            ArgParser.parseAndRun(command);
        } finally {
            Assert.assertFalse("Run must not be called in exception case.", command.isRunCalled());
        }
    }

    /**
     * Tests that it's not detected that a required option is missing if the command doesn't want it.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=1751332&amp;group_id=149894&amp;atid=776740">[ 1751332 ] Required options check should be optional / configurable</a>
     */
    @SuppressWarnings({ "JUnitTestMethodWithNoAssertions" })
    @Test
    public void testCommandRequiredOptionMissingDisabled() throws RequiredOptionsMissingException, TerminalException, UnknownOptionException, MissingArgumentException, ArgumentFileNotFoundException {
        final BasicCommand command = new MockCommand();
        command.setCheckRequiredOptions(false);
        ArgParser.parseAndRun(command);
    }

    /**
     * Tests that it's detected that an unknown option was given.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (expected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = UnknownOptionException.class)
    public void testCommandUnknownOption() throws RequiredOptionsMissingException, TerminalException, UnknownOptionException, MissingArgumentException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        try {
            ArgParser.parseAndRun(command, "--output");
        } finally {
            Assert.assertFalse("Run must not be called in exception case.", command.isRunCalled());
        }
    }

    /**
     * Tests that it's detected that the argument of an option that requires an argument is missing.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (expected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = MissingArgumentException.class)
    public void testCommandMissingArgument() throws RequiredOptionsMissingException, TerminalException, UnknownOptionException, MissingArgumentException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        try {
            ArgParser.parseAndRun(command, "--input");
        } finally {
            Assert.assertFalse("Run must not be called in exception case.", command.isRunCalled());
        }
    }

    /**
     * Tests that specifying an option twice works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testCommandDuplicateOption() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-i", "barBuzz", "-i", "fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that help works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (expected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = TerminalException.class)
    public void testHelp() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final Command command = new MockCommand();
        ArgParser.parseAndRun(command, "-h");
    }

    /**
     * Tests that stopping option parsing with -- works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testStopOptionParsing() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-i", "foo", "--", "--input");
        final List<String> args = command.getArgs();
        Assert.assertEquals("--input must be stored as argument after --", 1, args.size());
        Assert.assertEquals("--input must be stored as argument after --", "--input", args.get(0));
    }

    /**
     * Tests that reading options from a file works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=1750193&amp;group_id=149894&amp;atid=776740">[ 1750193 ] Partial read of command line arguments from a file</a>
     */
    @Test
    public void testOptionsFromFileSingleLine() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "@" + pathPrefix + "src/tst/test/net/sf/japi/io/args/ArgParserTest_OptionsFileSingleLine");
        final List<String> args = command.getArgs();
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Arguments must be stored.", 2, args.size());
        Assert.assertEquals("Argument foo must be stored.", "foo", args.get(0));
        Assert.assertEquals("Argument bar must be stored.", "bar", args.get(1));
    }

    /**
     * Tests that including multiple command files from a command file works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=1758846&amp;group_id=149894&amp;atid=776737">[ 1758846 ] Multiple command file inclusion fails</a>
     */
    @Test
    public void testOptionsFromFileMultiple() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "@" + pathPrefix + "src/tst/test/net/sf/japi/io/args/ArgParserTest_MultipleOptionsFileMaster");
        final List<String> args = command.getArgs();
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Arguments must be stored.", 2, args.size());
        Assert.assertEquals("Argument foo must be stored.", "foo", args.get(0));
        Assert.assertEquals("Argument bar must be stored.", "bar", args.get(1));
    }

    /**
     * Tests that including multiple command files from a command file works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (expected).
     */
    @Test(expected = ArgumentFileNotFoundException.class)
    public void testOptionsFromFileNotFound() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final Command command = new MockCommand();
        ArgParser.parseAndRun(command, "@ThisFileDoesNotExist.txt");
    }

    /**
     * Tests that single dash options also work.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=1750198&amp;group_id=149894&amp;atid=776740">[ 1750198 ] Allow single dash instead of double dash</a>
     */
    @Test
    public void testSingleDashOption() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-input", "fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that single dash options also work.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     * @see <a href="http://sourceforge.net/tracker/index.php?func=detail&amp;aid=1750198&amp;group_id=149894&amp;atid=776740">[ 1750198 ] Allow single dash instead of double dash</a>
     */
    @Test
    public void testSingleDashOptionWithEquals() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-input=fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that GNU style options with -W are accepted and processed properly.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testPosixStyleLong() throws RequiredOptionsMissingException, MissingArgumentException, TerminalException, UnknownOptionException, ArgumentFileNotFoundException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-W", "input", "fooInput");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that -W is not allowed.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoWOption() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command command = new MockCommand() {

            @Option({ "W" })
            public void minusW() {
            }
        };
        ArgParser.parseAndRun(command);
    }

    /**
     * Tests that option names do not start with '-'.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNoMinusStart() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command command = new MockCommand() {

            @Option({ "-o" })
            public void minusW() {
            }
        };
        ArgParser.parseAndRun(command);
    }

    /**
     * Tests that an option without text is detected.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyOption() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command emptyCommand = new MockCommand() {

            @Option({  })
            public void someOption() {
            }
        };
        ArgParser.parseAndRun(emptyCommand);
    }

    /**
     * Tests that an option with empty name is detected.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyStringOption() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command emptyCommand = new MockCommand() {

            @Option({ "" })
            public void someOption() {
            }
        };
        ArgParser.parseAndRun(emptyCommand);
    }

    /**
     * Tests that a single dash is not treated as option.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testSingleDash() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-i", "foo", "-");
        final List<String> args = command.getArgs();
        Assert.assertEquals("Expecting '-' to be passed as argument.", 1, args.size());
        Assert.assertEquals("Expecting '-' to be passed as argument.", "-", args.get(0));
    }

    /**
     * Tests that reading an argument file that doesn't exist throws an ArgumentFileNotFoundexception.
     * @throws ArgumentFileNotFoundException (expected).
     */
    @Test(expected = ArgumentFileNotFoundException.class)
    public void testArgumentFileNotFound() throws ArgumentFileNotFoundException {
        ArgParser.readFromFile(new File("This file does not exist.txt"));
    }

    /**
     * Tests that declaring an option twice is detected.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testDoubleOptionDetected() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        ArgParser.parseAndRun(new BasicCommand() {

            /** {@inheritDoc} */
            @SuppressWarnings({ "InstanceMethodNamingConvention" })
            public int run(@NotNull final List<String> args) {
                return 0;
            }

            @SuppressWarnings({ "QuestionableName" })
            @Option({ "foo" })
            public void foo1() {
            }

            @SuppressWarnings({ "QuestionableName" })
            @Option({ "foo" })
            public void foo2() {
            }
        });
    }

    /**
     * Tests that putting short options together works.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (unexpected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testShortOptionsInOneOption() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-if", "fooInput", "optionValue");
        Assert.assertEquals("Option value must be stored.", "fooInput", command.getInput());
        Assert.assertEquals("Option value must be stored.", "optionValue", command.getFoo());
        Assert.assertTrue("Run must be called even with zero arguments.", command.isRunCalled());
        Assert.assertEquals("Argument list for invocation without arguments must be empty.", 0, command.getArgs().size());
    }

    /**
     * Tests that a missing argument is detected.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (expected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final Command command = new MockCommand();
        ArgParser.parseAndRun(command, "-i");
    }

    /**
     * Tests that double-@ is unescaped to one single @.
     * @throws RequiredOptionsMissingException (unexpected).
     * @throws TerminalException (unexpected).
     * @throws UnknownOptionException (unexpected).
     * @throws MissingArgumentException (expected).
     * @throws ArgumentFileNotFoundException (unexpected).
     */
    @Test
    public void testDoubleAt() throws ArgumentFileNotFoundException, UnknownOptionException, MissingArgumentException, RequiredOptionsMissingException, TerminalException {
        final MockCommand command = new MockCommand();
        ArgParser.parseAndRun(command, "-i", "foo", "@@");
        final List<String> args = command.getArgs();
        Assert.assertEquals("Expecting '@' to be passed as argument.", 1, args.size());
        Assert.assertEquals("Expecting '@' to be passed as argument.", "@", args.get(0));
    }

    /**
     * This MockCommand serves as a command for performing simple tests.
     * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
     */
    @SuppressWarnings({ "InstanceVariableMayNotBeInitialized" })
    public static class MockCommand extends BasicCommand {

        /** The input option value. */
        private String input;

        /** Remembers whether {@link #run(List)} was called. */
        private boolean runCalled;

        /** The command line arguments received from the parser. */
        private List<String> args;

        /** Option value, saved for verification. */
        private String foo;

        /** {@inheritDoc} */
        @SuppressWarnings({ "InstanceMethodNamingConvention" })
        public int run(@NotNull final List<String> args) {
            runCalled = true;
            this.args = args;
            return 0;
        }

        /**
         * Get the command line arguments.
         * @return Command line arguments.
         */
        private List<String> getArgs() {
            return args;
        }

        /**
         * Set the value of the input option.
         * @param input Value of the input option.
         */
        @Option(value = { "i", "input" }, type = OptionType.REQUIRED)
        public void setInput(final String input) {
            this.input = input;
        }

        /**
         * Get the value of the foo option.
         * @return Value of the foo option.
         */
        public String getFoo() {
            return foo;
        }

        /**
         * Set the value of the foo option.
         * @param foo Value of the foo option.
         */
        @Option({ "f", "b", "foo", "bar", "buzz" })
        public void setFoo(final String foo) {
            this.foo = foo;
        }

        /**
         * Get the value of the input option.
         * @return Value of the input option.
         */
        @SuppressWarnings({ "TypeMayBeWeakened" })
        private String getInput() {
            return input;
        }

        /**
         * Return whether run was called.
         * @return whether run was called.
         */
        private boolean isRunCalled() {
            return runCalled;
        }
    }
}
