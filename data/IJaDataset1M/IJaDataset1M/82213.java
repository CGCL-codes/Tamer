package abbot.script.parsers;

import javax.swing.tree.TreePath;
import junit.extensions.abbot.TestHelper;
import junit.framework.TestCase;

public class TreePathParserTest extends TestCase {

    private TreePathParser parser;

    public void testConvertSingleNode() {
        String arg = "[root]";
        TreePath path = (TreePath) parser.parse(arg);
        assertEquals("Wrong path generated", arg, path.toString());
    }

    public void testConvertMultipleNodes() {
        String arg = "[root, parent, child]";
        TreePath path = (TreePath) parser.parse(arg);
        assertEquals("Wrong path generated", arg, path.toString());
    }

    public void testHiddenRoot() {
        String arg = "[null, parent, child]";
        TreePath path = (TreePath) parser.parse(arg);
        assertEquals("Wrong path generated", arg, path.toString());
    }

    protected void setUp() {
        parser = new TreePathParser();
    }

    public TreePathParserTest(String name) {
        super(name);
    }

    public static void main(String[] args) {
        TestHelper.runTests(args, TreePathParserTest.class);
    }
}
