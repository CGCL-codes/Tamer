package xml.parser;

import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import org.fest.swing.fixture.*;
import org.fest.swing.core.*;
import org.fest.swing.finder.*;
import org.fest.swing.edt.*;
import org.fest.swing.timing.*;
import static org.fest.assertions.Assertions.*;
import org.gjt.sp.jedit.testframework.Log;
import static xml.XMLTestUtils.*;
import static org.gjt.sp.jedit.testframework.EBFixture.*;
import static org.gjt.sp.jedit.testframework.TestUtils.*;
import org.gjt.sp.jedit.testframework.PluginOptionsFixture;
import org.gjt.sp.jedit.testframework.TestUtils;
import org.gjt.sp.jedit.EBMessage;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import java.io.*;
import java.net.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import javax.xml.validation.ValidatorHandler;
import com.thaiopensource.xml.sax.DraconianErrorHandler;

/**
 * $Id: XmlTagTest.java 18330 2010-08-11 19:53:08Z kerik-sf $
 */
public class XmlTagTest {

    private static File testData;

    @BeforeClass
    public static void setUpjEdit() throws IOException {
        TestUtils.beforeClass();
        testData = new File(System.getProperty("test_data")).getCanonicalFile();
        assertTrue(testData.exists());
    }

    @AfterClass
    public static void tearDownjEdit() {
        TestUtils.afterClass();
    }

    @Test
    public void testSimple() throws SAXException, IOException {
        File xml = new File(testData, "simple/actions.xml");
        TestUtils.openFile(xml.getPath());
        parseAndWait();
        action("sidekick.parser.xml-switch");
        FrameFixture sidekick = TestUtils.findFrameByTitle("Sidekick");
        JTreeFixture sourceTree = sidekick.tree();
        selectPath(sourceTree, "ACTIONS");
        selectPath(sourceTree, "ACTIONS/ACTION/CODE");
        JEditTextArea area = TestUtils.view().getTextArea();
        assertEquals("<CODE", area.getBuffer().getText(area.getCaretPosition(), 5));
    }

    @Test
    public void testDisplayModes() {
        PluginOptionsFixture options = pluginOptions();
        options.optionPane("XML/XML", "xml.general").comboBox("showAttributes").selectItem(1);
        options.OK();
        File xml = new File(testData, "xinclude/conventions.xml");
        TestUtils.openFile(xml.getPath());
        parseAndWait();
        action("sidekick.parser.xml-switch");
        Pause.pause(1000);
        FrameFixture sidekick = TestUtils.findFrameByTitle("Sidekick");
        JTreeFixture sourceTree = sidekick.tree();
        selectPath(sourceTree, "chapter id=\"conventions\"");
        options = pluginOptions();
        options.optionPane("XML/XML", "xml.general").comboBox("showAttributes").selectItem(0);
        options.OK();
        parseAndWait();
        action("sidekick.parser.xml-switch");
        sidekick = TestUtils.findFrameByTitle("Sidekick");
        sourceTree = sidekick.tree();
        Pause.pause(1000);
        selectPath(sourceTree, "chapter");
        options = pluginOptions();
        options.optionPane("XML/XML", "xml.general").comboBox("showAttributes").selectItem(2);
        options.OK();
        parseAndWait();
        sidekick = TestUtils.findFrameByTitle("Sidekick");
        sourceTree = sidekick.tree();
        Pause.pause(1000);
        selectPath(sourceTree, "chapter id=\"conventions\" xml:lang=\"en\"");
    }
}
