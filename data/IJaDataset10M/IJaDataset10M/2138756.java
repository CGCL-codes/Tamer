package fitgoodies.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Iterator;
import fitgoodies.FitGoodiesTestCase;

/**
 * $Id: FilterDirectoryIteratorTest.java 185 2009-08-17 13:47:24Z jwierum $
 * @author jwierum
 */
public class FilterDirectoryIteratorTest extends FitGoodiesTestCase {

    private Iterator<FileInformation> dummy;

    @Override
    public final void setUp() throws Exception {
        super.setUp();
        dummy = (new DirectoryProviderMock()).getFiles();
    }

    public final void testBlankFilter() {
        FilterDirectoryIterator files;
        files = new FilterDirectoryIterator(dummy, new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return true;
            }
        });
        assertStringIterator(files, new String[] { "/test/file1.txt", "/test/file2.txt", "/test/file3.txt", "/f.txt.bat", "/dir/dir2/noext" });
    }

    public final void testFilenameNameFilter() {
        FilterDirectoryIterator files;
        files = new FilterDirectoryIterator(dummy, new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return name.matches("file.*|.*\\.bat");
            }
        });
        assertStringIterator(files, new String[] { "/test/file1.txt", "/test/file2.txt", "/test/file3.txt", "/f.txt.bat" });
    }

    public final void testFilenameDirFilter() {
        FilterDirectoryIterator files;
        files = new FilterDirectoryIterator(dummy, new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                return dir.getAbsolutePath().indexOf("test") >= 0;
            }
        });
        assertStringIterator(files, new String[] { "/test/file1.txt", "/test/file2.txt", "/test/file3.txt" });
    }
}
