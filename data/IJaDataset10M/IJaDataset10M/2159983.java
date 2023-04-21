package org.dishevelled.commandline;

import java.util.Collections;
import junit.framework.TestCase;

/**
 * Unit test for CommandLineParser.
 *
 * @author  Michael Heuer
 * @version $Revision: 1059 $ $Date: 2012-01-03 15:03:02 -0500 (Tue, 03 Jan 2012) $
 */
public final class CommandLineParserTest extends TestCase {

    public void testParse() {
        CommandLine commandLine = new CommandLine(new String[0]);
        ArgumentList arguments = new ArgumentList(Collections.<Argument<?>>emptyList());
        try {
            CommandLineParser.parse(null, arguments);
            fail("parse(null,) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        } catch (CommandLineParseException e) {
            fail("caught unexpected CommandLineParseException");
        }
        try {
            CommandLineParser.parse(commandLine, null);
            fail("parse(,null) expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        } catch (CommandLineParseException e) {
            fail("caught unexpected CommandLineParseException");
        }
    }
}
