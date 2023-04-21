package sywico.core;

import java.io.File;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import sywico.core.CmdLineController;
import sywico.core.Util;
import sywico.core.Business.UpdateStatus;
import sywico.core.checksumreport.Filter;
import sywico.core.message.SubmitMessage;
import sywico.core.message.UpdateMessage;

/**
 * 
 * Class used to test on real projects
 */
public class ControllerBigTest extends TestCase {

    public static Logger logger = Logger.getLogger(ControllerBigTest.class.getName());

    static String backupDir = null;

    public static void testOnExistingProject(String projectDir) {
        CmdLineController controller = new CmdLineController();
        Util.delete(MockFiles.TMP_DIR + "", null);
        Util.copy("src/main/", MockFiles.TMP_DIR + "/developer/work/src", null);
        Util.copy(MockFiles.TMP_DIR + "/developer/work", MockFiles.TMP_DIR + "/developer/base", null);
        Util.copy(MockFiles.TMP_DIR + "/developer/work", MockFiles.TMP_DIR + "/company/work", null);
        MockFiles.createTextFile(MockFiles.TMP_DIR + "/developer/work/aaaa", 12);
        Filter filter = new Filter(MockFiles.TMP_DIR + "/filter.txt", "*.jar", "*.svn", "*.class");
        SubmitMessage submitMessage = controller.createSubmitMessage("test", MockFiles.TMP_DIR + "/developer/work", filter, MockFiles.TMP_DIR + "/developer/base", null);
        Object afterSubmit[] = controller.processSubmitMessage(submitMessage, MockFiles.TMP_DIR + "/company/work", backupDir, null);
        UpdateMessage updateMessage = (UpdateMessage) afterSubmit[0];
        Boolean changesApplied = (Boolean) afterSubmit[1];
        assertEquals(true, changesApplied.booleanValue());
        assertEquals(UpdateStatus.APPLIED, controller.processUpdateMessage(updateMessage, MockFiles.TMP_DIR + "/developer/work", MockFiles.TMP_DIR + "/developer/base", backupDir, null));
        assertNull(MockFiles.directoriesAreEqual(MockFiles.TMP_DIR + "/company/work", MockFiles.TMP_DIR + "/developer/base", null));
        assertNull(MockFiles.directoriesAreEqual(MockFiles.TMP_DIR + "/developer/work", MockFiles.TMP_DIR + "/developer/base", null));
        assertNull(MockFiles.directoriesAreEqual(MockFiles.TMP_DIR + "/developer/base", MockFiles.TMP_DIR + "/company/work", null));
        assertTrue(new File(MockFiles.TMP_DIR + "/company/work/aaaa").exists());
    }

    public static void testOnMyself() {
        testOnExistingProject("src");
    }
}
