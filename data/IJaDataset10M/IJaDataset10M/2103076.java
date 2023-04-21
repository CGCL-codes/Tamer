package unittests;

import java.io.IOException;
import org.junit.Test;
import junit.framework.TestCase;
import src.project.file.VirtualWikiFile;
import src.tasks.Tasks.Task;

public class FormattingsTester extends TestCase {

    private static final String templateCode = "{{{isBlock|}}}<code{{{args}}}>{{{text}}}</code>";

    private static final String templateCode2 = "{{{isBlock|}}}<code style=\"w: 100px;{{{style|}}}\" class=\"c {{{classes|}}}\"{{{args|}}}>{{{text}}}</code>";

    @Test
    public void testBoldItalic() throws IOException {
        assertEquals("<strong>just bold.</strong>", p("'''just bold.'''"));
        assertEquals("<strong>'ts bold too</strong>", p("''''ts bold too'''"));
        assertEquals("<strong>not 'new'.</strong>", p("'''not 'new'.'''"));
        assertEquals("<strong>bold.</strong>no more'''", p("'''bold.'''no more'''"));
        assertEquals("<strong><em>b/i</em>b</strong>", p("'''''b/i''b'''"));
        assertEquals("<strong>b<em>b/i</em></strong>", p("'''b''b/i'' '''"));
        assertEquals("<em>italic</em>", p("''italic''"));
        assertEquals("<em>'ts italic too</em>", p("'''ts italic too''"));
    }

    @Test
    public void testCode() throws IOException {
        assertEquals("<code>code here</code>", p("$$code here$$"));
        assertEquals("<code id=\"myID\">code here</code>", p("$$((args= id=\"myID\"))code here$$"));
        assertEquals("<code style=\"w: 100px;h: 200px;\" class=\"c d\">code here</code>", p2("$$((style=h: 200px;|classes=d))code here$$"));
    }

    @Test
    public void testCodeBlock() throws IOException {
        assertEquals("true<code>code here</code>", p("$$\ncode here\n$$"));
        assertEquals("no$$code here$$", p("no$$\ncode here\n$$"));
        assertEquals("true<code style=\"w: 100px;h: 200px;\" class=\"c d\">code here</code>", p2("$$((style=h: 200px;|classes=d))\ncode here\n$$"));
    }

    @Test
    public void testAppended() throws IOException {
        assertEquals("really<strong>not</strong>everything", p("really'''not'''everything"));
        assertEquals("really<em>not</em>everything", p("really''not''everything"));
        assertEquals("really<code>not</code>everything", p("really $$not$$ everything").replace(" ", ""));
    }

    private static final String p(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        to.writeFile("tplCode.txt", templateCode);
        return to.real();
    }

    private static final String p2(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        to.writeFile("tplCode.txt", templateCode2);
        return to.real();
    }

    private static class TestObject extends unittests.TestObject {

        public TestObject(String test, String result) throws IOException {
            super(test, result);
        }

        void fillTasks(VirtualWikiFile vf) {
            vf.addTask(Task.Formattings);
        }

        public String real() throws IOException {
            return super.real().replace("\n", "").replace(" <", "<");
        }
    }
}
