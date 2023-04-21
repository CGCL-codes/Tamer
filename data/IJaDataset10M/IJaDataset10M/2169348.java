package unittests;

import java.io.IOException;
import org.junit.Test;
import src.project.file.VirtualWikiFile;
import src.tasks.Tasks.Task;

public class ParagraphTester extends junit.framework.TestCase {

    @Test
    public void testSimpleParagraphs() throws IOException {
        assertEquals("<p>Test</p>", p("Test"));
        assertEquals("<p>TestTest2</p>", p("Test\nTest2"));
        assertEquals("<p>Test</p><p>Test2</p>", p("Test\n\nTest2"));
    }

    @Test
    public void testHtmlNegativeTags() throws IOException {
        assertEquals("<br/>", p("<br/>"));
        assertEquals("<p>This.</p>", p("<p>This.</p>"));
        assertEquals("<!--This.-->", p("<!--This.-->"));
        assertEquals("<div style=\"width: 800px;\"></div>", p("<div style=\"width: 800px;\"></div>"));
    }

    @Test
    public void testHtmlPositiveTags() throws IOException {
        assertEquals("<p><span>Text</span></p>", p("<span>Text</span>"));
    }

    @Test
    public void testLongTags() throws IOException {
        assertEquals("<div style=\"width: 800px; height: 700px; font-weight: bold;\"></div>", p("<div style=\"width: 800px; height: 700px; font-weight: bold;\"></div>"));
    }

    private static final String p(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        return to.real();
    }

    private static class TestObject extends unittests.TestObject {

        public TestObject(String test, String result) throws IOException {
            super(test, result);
        }

        void fillTasks(VirtualWikiFile vf) {
            vf.addTask(Task.Paragraphs);
            vf.addTask(Task.Lists);
        }

        public String real() throws IOException {
            return super.real().replace("\n", "").replace(" <", "<");
        }
    }
}
