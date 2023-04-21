package net.jfellow.gui;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.xml.JFCXMLTestCase;
import junit.extensions.xml.XMLException;
import junit.extensions.xml.XMLTestSuite;
import net.jfellow.common.util.FileUtil;
import net.jfellow.regexassistant.gui.JFrameRegExpAssistant;
import java.io.File;
import java.util.Enumeration;

/**
 * Robot for FBagFinishing.
 * Executes a XML test suite for FBagFinishing in an infinite loop.
 *
 * @author Tom Wiedenhoeft
 *
 */
public class JFrameRegExpAssistantTest extends JFCTestCase {

    String replayFileClasspath;

    String replayFileLocal;

    JFrameRegExpAssistant assistant;

    public JFrameRegExpAssistantTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        this.setHelper(new JFCTestHelper());
        String replayFileName = "replayDemo1.xml";
        this.replayFileClasspath = "/net/jfellow/regexassistant/test/" + replayFileName;
        this.replayFileLocal = System.getProperty("user.home") + File.separator + "smarttail" + File.separator + "tests" + File.separator + replayFileName;
        String content = FileUtil.copyRessourceFileToFileSystem(this.replayFileClasspath, this.replayFileLocal);
        this.assistant = JFrameRegExpAssistant.getInstance(true);
        this.assistant.setVisible(true);
        this.assistant.emptyTextAreasForTest();
        this.assistant.setTestText(content);
    }

    public void tearDown() throws Exception {
        TestHelper.cleanUp(this);
        File f = new File(this.replayFileLocal);
        f.delete();
        super.tearDown();
    }

    /**
     * It starts the JFC robot
     * <br>First it looks for the environment variables.
     * <br>Then it starts the test.
     */
    public void testStartRobot() {
        File f = new File(this.replayFileLocal);
        boolean fileDoesExist = f.exists();
        assertEquals("Reading file from local filesystem: " + this.replayFileLocal, true, fileDoesExist);
        XMLTestSuite xmlTestSuite = null;
        try {
            xmlTestSuite = new XMLTestSuite(this.replayFileLocal);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue("Create new XMLTestSuite from: " + this.replayFileLocal, false);
        }
        Enumeration enu = xmlTestSuite.tests();
        while (enu.hasMoreElements()) {
            JFCXMLTestCase testCase = (JFCXMLTestCase) enu.nextElement();
            try {
                testCase.runBare();
            } catch (XMLException e) {
                e.printStackTrace();
                assertTrue("Running test: " + this.replayFileLocal, false);
            } catch (Exception e) {
                e.printStackTrace();
                assertTrue("Running test: " + this.replayFileLocal, false);
            } catch (Throwable e) {
                e.printStackTrace();
                assertTrue("Running test: " + this.replayFileLocal, false);
            }
        }
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(JFrameRegExpAssistantTest.class);
    }
}
