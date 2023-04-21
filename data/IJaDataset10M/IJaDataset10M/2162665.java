package unclej.filepath;

import junit.framework.TestCase;
import unclej.util.FileUtil;
import java.io.File;

/**
 * @author scottv
 */
public class DependencyFilterTest extends TestCase {

    public void testFilter() {
        File root = new File("test/resources/path");
        File srcRoot = new File(root, "src");
        File classRoot = new File(root, "classes");
        DependencyFilter filter = new DependencyFilter(classRoot) {

            protected String getTargetName(String sourceName) {
                return FileUtil.replaceExtension(sourceName, ".class");
            }

            public boolean shouldRecurse(File dir) {
                return true;
            }
        };
        new File(srcRoot, "UpToDate.java").setLastModified(new File(classRoot, "UpToDate.class").lastModified() - 1);
        Filelike upToDate = Filelike.fromFile(srcRoot, "UpToDate.java");
        Filelike outOfDate = Filelike.fromFile(srcRoot, "OutOfDate.java");
        Filelike newClass = Filelike.fromFile(srcRoot, "pkg/NewClass.java");
        assertTrue(filter.accept(newClass));
        assertTrue(filter.accept(outOfDate));
        assertFalse(filter.accept(upToDate));
    }
}
