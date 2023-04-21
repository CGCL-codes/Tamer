package sidekick.html;

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
import java.io.*;
import org.gjt.sp.jedit.textarea.JEditTextArea;

/**
 * $Id: HtmlParserTest.java 18976 2010-11-16 21:02:10Z kerik-sf $
 */
public class HtmlParserTest {

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
    public void testErrorInCss() {
        File xml = new File(testData, "html/error_in_css.html");
        TestUtils.openFile(xml.getPath());
        parseAndWait();
        action("sidekick-tree");
        FrameFixture sidekick = TestUtils.findFrameByTitle("Sidekick");
        JTreeFixture sourceTree = sidekick.tree();
        selectPath(sourceTree, "<html>&lt;html&gt;/<head>/<STYLE>");
        JEditTextArea area = TestUtils.view().getTextArea();
        assertEquals("<style", area.getBuffer().getText(area.getCaretPosition(), 6));
        action("error-list-show", 1);
        FrameFixture errorlist = TestUtils.findFrameByTitle("Error List");
        errorlist.tree().selectRow(1);
        assertEquals(";", area.getSelectedText());
        assertEquals(2, area.getCaretLine());
    }
}
