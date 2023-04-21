package unittests;

import java.io.IOException;
import org.junit.Test;
import src.project.file.VirtualWikiFile;
import src.tasks.Tasks.Task;

public class ListTester extends junit.framework.TestCase {

    @Test
    public void testUl() throws IOException {
        assertEquals("\n<ul>\n <li>bla</li>\n</ul>\n", p("*bla"));
        assertEquals("<ul><li>bla</li></ul>", p2("*bla"));
        assertEquals("<ul><li>bla</li></ul>.", p2("*bla\n."));
        assertEquals("<ul><li>bla</li><li>blah</li></ul>.", p2("*bla\n*blah\n."));
    }

    @Test
    public void testNestedUl() throws IOException {
        assertEquals("<ul><li>bla<ul><li>blah</li></ul></li></ul>.", p2("*bla\n**blah\n."));
        assertEquals("<ul><li>bla<ul><li>blah</li></ul></li><li>blubb</li></ul>.", p2("*bla\n**blah\n* blubb\n."));
    }

    @Test
    public void testOl() throws IOException {
        assertEquals("<ol><li>bla</li></ol>", p2("#bla"));
    }

    @Test
    public void testUlOl() throws IOException {
        assertEquals("<ol><li>bla</li></ol><ul><li>bla</li></ul>", p2("#bla\n*bla"));
    }

    @Test
    public void testDl() throws IOException {
        assertEquals("<dl><dt>Def</dt></dl>", p2(";Def"));
        assertEquals("<dl><dt>Def</dt><dd>Text</dd></dl>", p2(";Def\n:Text"));
        assertEquals("<dl><dt>Def</dt><dd>Text</dd><dd>Text2</dd></dl>", p2(";Def\n:Text\n: Text2"));
        assertEquals("<dl><dt>Def</dt></dl>.", p2(";Def\n."));
    }

    @Test
    public void testNewline() throws IOException {
        assertEquals("<ul><li>bla</li><li>blah</li></ul>.", p2("*bla\n\n*blah\n."));
    }

    private static final String p(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        return to.real();
    }

    private static final String p2(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        return to.real().replace("\n", "").replace(" ", "");
    }

    private static class TestObject extends unittests.TestObject {

        public TestObject(String test, String result) throws IOException {
            super(test, result);
        }

        void fillTasks(VirtualWikiFile vf) {
            vf.addTask(Task.Lists);
        }
    }
}
