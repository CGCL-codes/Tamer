package org.jimcat.tests.services.imageimport;

import java.io.File;
import org.jimcat.services.imageimport.ImportFileNameFilter;
import org.jimcat.tests.JimcatTestCase;

/**
 * 
 * 
 * $Id$
 * 
 * @author Christoph
 */
public class TestImportFileNameFilter extends JimcatTestCase {

    public void testAccept() {
        ImportFileNameFilter filter = new ImportFileNameFilter();
        File dir = new File(getProperty("testdirectory"));
        assertTrue(filter.accept(dir, "foo.jpg"));
        assertTrue(filter.accept(dir, "foo.JPG"));
        assertTrue(filter.accept(dir, "foo.JpG"));
        assertTrue(filter.accept(dir, "file name with lots of...spaces and dots... .jpg"));
        assertFalse(filter.accept(null, "A.exe"));
    }
}
