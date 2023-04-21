package net.sourceforge.cruisecontrol.bootstrappers;

import junit.framework.TestCase;
import net.sourceforge.cruisecontrol.CruiseControlException;
import java.io.File;
import java.io.IOException;

/**
 * @see    <a href="http://subversion.tigris.org/">subversion.tigris.org</a>
 * @author <a href="etienne.studer@canoo.com">Etienne Studer</a>
 */
public class SVNBootstrapperTest extends TestCase {

    private SVNBootstrapper bootStrapper;

    protected void setUp() {
        bootStrapper = new SVNBootstrapper();
    }

    public void testValidate() throws IOException {
        try {
            bootStrapper.validate();
            fail("should throw an exception when no attributes are set");
        } catch (CruiseControlException e) {
        }
        bootStrapper = new SVNBootstrapper();
        bootStrapper.setFile("some filename");
        try {
            bootStrapper.validate();
        } catch (CruiseControlException e) {
            fail("should not throw an exception when at least the 'filename' attribute is set");
        }
        bootStrapper = new SVNBootstrapper();
        bootStrapper.setLocalWorkingCopy("invalid directory");
        try {
            bootStrapper.validate();
            fail("should throw an exception when an invalid 'localWorkingCopy' attribute is set");
        } catch (CruiseControlException e) {
        }
        File tempFile = File.createTempFile("temp", "txt");
        tempFile.deleteOnExit();
        bootStrapper = new SVNBootstrapper();
        bootStrapper.setLocalWorkingCopy(tempFile.getParent());
        try {
            bootStrapper.validate();
        } catch (CruiseControlException e) {
            fail("should not throw an exception when at least a valid 'localWorkingCopy' " + "attribute is set");
        }
        bootStrapper = new SVNBootstrapper();
        bootStrapper.setLocalWorkingCopy(tempFile.getAbsolutePath());
        try {
            bootStrapper.validate();
            fail("should throw an exception when 'localWorkingCopy' is a file instead of a " + "directory.");
        } catch (CruiseControlException e) {
        }
    }

    public void testBuildUpdateCommand() throws CruiseControlException {
        String tempDir = System.getProperty("java.io.tmpdir");
        bootStrapper.setLocalWorkingCopy(tempDir);
        String command = bootStrapper.buildUpdateCommand().toString();
        assertEquals("svn update --non-interactive", command);
        bootStrapper.setFile("foo.txt");
        command = bootStrapper.buildUpdateCommand().toString();
        assertEquals("svn update --non-interactive foo.txt", command);
        bootStrapper.setUsername("lee");
        command = bootStrapper.buildUpdateCommand().toString();
        assertEquals("svn update --non-interactive --username lee foo.txt", command);
        bootStrapper.setPassword("secret");
        command = bootStrapper.buildUpdateCommand().toString();
        assertEquals("svn update --non-interactive --username lee --password secret foo.txt", command);
    }
}
