package unittests;

import java.io.IOException;
import org.junit.Test;
import src.project.file.VirtualWikiFile;
import src.tasks.Tasks.Task;

public class CleanupTester extends junit.framework.TestCase {

    @Test
    public void testNewlinesAtBeginning() throws IOException {
        assertEquals("a", p("   a"));
        assertEquals("a ", p("\n\r\r\n a "));
    }

    private static final String p(String testString) throws IOException {
        TestObject to = new TestObject(testString, "");
        return to.real();
    }

    private static class TestObject extends unittests.TestObject {

        public TestObject(String test, String result) throws IOException {
            super(test, result);
        }

        public TestObject(String test, String result, StringBuffer settings) throws IOException {
            super(test, result, settings);
        }

        void fillTasks(VirtualWikiFile vf) {
            vf.addTask(Task.Cleanup);
        }
    }
}
