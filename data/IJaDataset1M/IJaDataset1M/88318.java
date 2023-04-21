package net.sf.gumshoe.indexer;

import java.util.List;
import junit.framework.TestCase;

/**
 * @author Gabor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FindClassesTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(FindClassesTest.class);
    }

    public void testFindClassesThatExtendClass() throws Exception {
        List subclasses = FindClasses.findClassesThatExtend(ContentReader.class);
        assertTrue("found at least one", subclasses.size() > 0);
    }
}
