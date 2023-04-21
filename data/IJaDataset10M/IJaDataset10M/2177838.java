package org.dishevelled.commandline.argument;

import java.io.File;
import java.util.List;
import java.util.Arrays;
import junit.framework.TestCase;
import org.dishevelled.commandline.Argument;
import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.CommandLineParseException;

/**
 * Unit test for FileArgument.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public class FileArgumentTest extends TestCase {

    public void testFileArgument() {
        FileArgument fa = new FileArgument("f", "file", "File argument", true);
        assertNotNull("fa not null", fa);
        assertEquals("fa shortName == f", "f", fa.getShortName());
        assertEquals("fa longName == file", "file", fa.getLongName());
        assertEquals("fa description == File argument", "File argument", fa.getDescription());
        assertTrue("fa isRequired", fa.isRequired());
        assertFalse("fa wasFound == false", fa.wasFound());
        assertEquals("fa value == null", null, fa.getValue());
    }

    public void testValidArgumentShort() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", true);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        List<String> values = Arrays.asList(new String[] { "foo" });
        for (String value : values) {
            String[] args = new String[] { "-f", value };
            CommandLine commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);
            assertTrue("fileArgument wasFound == true", fileArgument.wasFound());
            assertNotNull("-d not null", fileArgument.getValue());
        }
    }

    public void testValidArgumentLong() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", true);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        List<String> values = Arrays.asList(new String[] { "foo" });
        for (String value : values) {
            String[] args = new String[] { "--file-argument", value };
            CommandLine commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);
            assertTrue("fileArgument wasFound == true", fileArgument.wasFound());
            assertNotNull("--file-argument " + value + " not null", fileArgument.getValue());
        }
    }

    public void testRequiredArgument() {
        try {
            Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", true);
            ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
            String[] args = new String[] { "not-an-argument" };
            CommandLine commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);
            fail("not-an-argument expected CommandLineParseException");
        } catch (CommandLineParseException e) {
        }
    }

    public void testNotRequiredArgument() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "not-an-argument" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertFalse("fileArgument isRequired == false", fileArgument.isRequired());
        assertFalse("fileArgument wasFound == false", fileArgument.wasFound());
        assertEquals("fileArgument value == null", null, fileArgument.getValue());
    }

    public void testMissingArgumentValueShort() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "-f" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueLong() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "--file-argument" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsShort() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "-f", "-s" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsShortAndLong() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "-f", "--some-other-argument" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsLongAndShort() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "--file-argument", "-s" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsLong() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "--file-argument", "--some-other-argument" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsLaterShort() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "-f", "-s", "some-other-value" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }

    public void testMissingArgumentValueMultipleArgumentsLaterLong() throws CommandLineParseException {
        Argument<File> fileArgument = new FileArgument("f", "file-argument", "File argument", false);
        ArgumentList arguments = new ArgumentList(Arrays.asList(new Argument<?>[] { fileArgument }));
        String[] args = new String[] { "--file-argument", "--some-other-argument", "some-other-value" };
        CommandLine commandLine = new CommandLine(args);
        CommandLineParser.parse(commandLine, arguments);
        assertTrue(fileArgument.wasFound());
        assertEquals(null, fileArgument.getValue());
    }
}
