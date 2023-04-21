package net.sf.ts2.pm.cli;

import junit.framework.TestCase;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @author Nicolas
 * �todo this is a integration test
 */
public class VersionITTest extends TestCase {

    private String repoHome = System.getProperty("user.home") + "/TS2Repository";

    /**
     * @param arg0
     */
    public VersionITTest(String arg0) {
        super(arg0);
    }

    public void testCLIInvocation() {
        String[] args = { "-v" };
        int result = TS2PackageManagerCLI.main(args);
        assertEquals("Execution fail : non zero return", 0, result);
    }
}
