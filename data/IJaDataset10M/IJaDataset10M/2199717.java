package org.sourceforge.jemm.server;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

;

public class ServerOptionsTest {

    static class TestServerOptions extends ServerOptions {

        public TestServerOptions(CommandLine cmd) throws ParseException {
            super(cmd);
        }

        @Override
        protected boolean fileDirExistsAndIsWritable(File file) {
            return file.getName().equals("TESTDIR");
        }
    }

    @Test
    public void testModeDefault() throws ParseException {
        assertEquals(ServerMode.DEFAULT, parseOptions("-a start -d TESTDIR").getMode());
    }

    @Test
    public void testModePersistent() throws ParseException {
        assertEquals(ServerMode.PERSISTENT, parseOptions("-a start -m persistent -d TESTDIR").getMode());
    }

    @Test(expected = ParseException.class)
    public void testPersistentNoDataDir() throws ParseException {
        parseOptions("-a start -m persistent");
    }

    @Test
    public void testPersistentDataDir() throws ParseException {
        assertEquals(new File("TESTDIR"), parseOptions("-a start -m persistent -d TESTDIR").getDataDir());
    }

    @Test(expected = ParseException.class)
    public void testPersistentMissingDataDir() throws ParseException {
        parseOptions("-a start -m persistent -d ILLEGAL");
    }

    @Test
    public void testModeMemory() throws ParseException {
        assertEquals(ServerMode.MEMORY, parseOptions("-a start -m memory").getMode());
    }

    @Test(expected = ParseException.class)
    public void testModeIllegal() throws ParseException {
        parseOptions("-a start -m xxxxx ");
    }

    @Test(expected = ParseException.class)
    public void testIllegalMemoryMode() throws ParseException {
        parseOptions("-a start -m memory -d xxxx");
    }

    @Test(expected = ParseException.class)
    public void testIllegalControlPort() throws ParseException {
        parseOptions("-a start -m memory -c xxxx");
    }

    @Test(expected = ParseException.class)
    public void testIllegalControlPort2() throws ParseException {
        parseOptions("-a start -m memory -c -5");
    }

    @Test
    public void testValidControlPort() throws ParseException {
        assertEquals(5432, parseOptions("-a start -m memory -c 5432").getControlPort());
    }

    @Test
    public void testDefaultControlPort() throws ParseException {
        assertEquals(ServerOptions.DEFAULT_CONTROL_PORT, parseOptions("-a start -m memory").getControlPort());
    }

    @Test(expected = ParseException.class)
    public void testIllegalPort() throws ParseException {
        parseOptions("-a start -m memory -p xxxx");
    }

    /**
     * Tests that a negative port number generates an exception.
     * @throws ParseException Expected, arguments illegal.
     */
    @Test(expected = ParseException.class)
    public void testIllegalPort2() throws ParseException {
        parseOptions("-a start -m memory -p -5");
    }

    /**
     * Test that if set the client port is correctly parsed.
     * @throws ParseException On error
     */
    @Test
    public void testValidPort() throws ParseException {
        assertEquals(5432, parseOptions("-a start -m memory -p 5432").getPort());
    }

    /**
     * Test that if not set the default client port is correctly set.
     * @throws ParseException On error
     */
    @Test
    public void testDefaultPort() throws ParseException {
        assertEquals(ServerOptions.DEFAULT_CLIENT_PORT, parseOptions("-a start -m memory").getPort());
    }

    /**
     * Tests that if the client and control ports are set to the same an error is generated.
     * @throws ParseException Expected
     */
    @Test(expected = ParseException.class)
    public void testEqualPortError() throws ParseException {
        parseOptions("-a start -m memory -c 1234 -p 1234");
    }

    /**
     * Tests that the start action is correctly picked up.
     * @throws ParseException On error
     */
    @Test
    public void testStartAction() throws ParseException {
        assertEquals(ServerAction.START, parseOptions("-a start -d TESTDIR").getAction());
    }

    /**
     * Tests that the stop action is correctly picked up.
     * @throws ParseException On error
     */
    @Test
    public void testStopAction() throws ParseException {
        assertEquals(ServerAction.STOP, parseOptions("-a stop -d TESTDIR").getAction());
    }

    @Test(expected = ParseException.class)
    public void testIllegalAction() throws ParseException {
        parseOptions("-a abcd -d TESTDIR");
    }

    /** Test command line with no action defined */
    @Test(expected = ParseException.class)
    public void testNoAction() throws ParseException {
        parseOptions("-d TESTDIR");
    }

    private static ServerOptions parseOptions(String args) throws ParseException {
        CommandLine cm = processCommandLine(args);
        return new TestServerOptions(cm);
    }

    private static CommandLine processCommandLine(String string) throws ParseException {
        Options options = ServerOptions.generateOptions();
        GnuParser parser = new GnuParser();
        return parser.parse(options, string.split(" "));
    }

    @Test
    public void toStringTest() throws ParseException {
        String generated = parseOptions("-a start -m persistent -d TESTDIR -p 1234 -c 5678").toString();
        String expectedString = "mode=PERSISTENT\ndataDir=TESTDIR\nport=1234\ncontrolPort=5678";
        assertEquals(expectedString, generated);
    }
}
