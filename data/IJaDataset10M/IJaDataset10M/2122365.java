package net.sourceforge.mazix.cli.option.argument.impl.filepattern;

import static net.sourceforge.mazix.components.constants.CharacterConstants.DOT_CHAR;
import static net.sourceforge.mazix.components.constants.CharacterConstants.SLASH_CHAR;
import static net.sourceforge.mazix.components.constants.CommonConstants.BLANK_STRING;
import static net.sourceforge.mazix.components.utils.file.FileUtils.writeIntoFile;
import static org.junit.Assert.assertEquals;
import java.io.File;
import net.sourceforge.mazix.cli.exception.CommandLineException;
import net.sourceforge.mazix.cli.option.argument.OptionArgument;
import net.sourceforge.mazix.components.ComparableAndDeepCloneableObjectTest;
import org.apache.tools.ant.types.FileSet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.DataPoint;

/**
 * JUnit test classes for {@link IncludeFilePatternOptionArgumentImpl}.
 *
 * @author Benjamin Croizet (<a href="mailto:graffity2199@yahoo.fr>graffity2199@yahoo.fr</a>)
 *
 * @since 1.0
 * @version 1.0.2
 */
public class IncludeFilePatternOptionArgumentImplTest extends ComparableAndDeepCloneableObjectTest<IncludeFilePatternOptionArgumentImpl> {

    /** Include file pattern option argument test 1 instance. */
    @DataPoint
    public static final OptionArgument<FileSet> FILESET_OPTION_ARGUMENT1 = new IncludeFilePatternOptionArgumentImpl(true);

    /** Include file pattern option argument test 2 instance. */
    @DataPoint
    public static final OptionArgument<FileSet> FILESET_OPTION_ARGUMENT2 = new IncludeFilePatternOptionArgumentImpl(false);

    /** Include file pattern option argument test 3 instance. */
    @DataPoint
    public static final OptionArgument<FileSet> FILESET_OPTION_ARGUMENT3 = new IncludeFilePatternOptionArgumentImpl(true);

    /** Include file pattern option argument test 4 instance. */
    @DataPoint
    public static final OptionArgument<FileSet> FILESET_OPTION_ARGUMENT4 = new IncludeFilePatternOptionArgumentImpl(true, new StringBuffer());

    /** Include file pattern option argument test 5 instance. */
    @DataPoint
    public static final OptionArgument<FileSet> FILESET_OPTION_ARGUMENT5 = null;

    /** Test file instance. */
    private static final File TEST_FILE = new File(DOT_CHAR + SLASH_CHAR + "log.zzz");

    /**
     * This method is called before each test to reset the create a test file.
     *
     * @since 1.0
     */
    @Before
    public void initializeFiles() {
        writeIntoFile("Test", TEST_FILE);
    }

    /**
     * This method is called after all tests, it is used to remove the temporary file used for the
     * tests.
     *
     * @since 1.0
     */
    @After
    public void removeFile() {
        TEST_FILE.delete();
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.AbstractOptionArgument#getMainUsage()}.
     */
    @Test
    public void testGetFullUsageDescription() {
        assertEquals("FILE_PATTERN specifies a file pattern, with the same syntax as ANT patterns. It means that \"**\", \"*\" or \"?\" special characters can be used. For more information, please consult http://ant.apache.org/manual/dirtasks.html.", FILESET_OPTION_ARGUMENT1.getFullUsageDescription().toString());
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.AbstractOptionArgument#getMainUsage()}.
     */
    @Test
    public void testGetMainUsageWithMandatory() {
        assertEquals("FILE_PATTERN", FILESET_OPTION_ARGUMENT1.getMainUsage().toString());
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.AbstractOptionArgument#getMainUsage()}.
     */
    @Test
    public void testGetMainUsageWithNoMandatory() {
        assertEquals("[FILE_PATTERN]", FILESET_OPTION_ARGUMENT2.getMainUsage().toString());
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.impl.filepattern.IncludeFilePatternOptionArgumentImpl#parseArgument(java.lang.String)}
     * . .
     *
     * @throws CommandLineException
     */
    @Test(expected = CommandLineException.class)
    public void testParseArgumentNull() throws CommandLineException {
        FILESET_OPTION_ARGUMENT1.parseArgument(null);
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.impl.filepattern.IncludeFilePatternOptionArgumentImpl#parseArgument(java.lang.String)}
     * . .
     *
     * @throws CommandLineException
     */
    @Test(expected = CommandLineException.class)
    public void testParseArgumentWithEmptyString() throws CommandLineException {
        FILESET_OPTION_ARGUMENT1.parseArgument(BLANK_STRING);
    }

    /**
     * Test method for
     * {@link net.sourceforge.mazix.cli.option.argument.impl.filepattern.IncludeFilePatternOptionArgumentImpl#parseArgument(java.lang.String)}
     * . .
     *
     * @throws CommandLineException
     */
    @Test
    public void testParseArgumentWithGoodPattern() throws CommandLineException {
        final FileSet argument = FILESET_OPTION_ARGUMENT1.parseArgument("**/*.zzz");
        assertEquals(1, argument.size());
    }
}
