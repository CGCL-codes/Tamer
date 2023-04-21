package com.phloc.commons.io.file.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import org.junit.Test;
import com.phloc.commons.filter.IFilter;
import com.phloc.commons.mock.PhlocTestUtils;

/**
 * Test class for class {@link FileFilterToIFilterAdapter}.
 * 
 * @author philip
 */
public final class FileFilterToIFilterAdapterTest {

    @Test
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value = "NP_NONNULL_PARAM_VIOLATION")
    public void testAll() {
        try {
            new FileFilterToIFilterAdapter((FilenameFilter) null);
            fail();
        } catch (final NullPointerException ex) {
        }
        try {
            new FileFilterToIFilterAdapter((FileFilter) null);
            fail();
        } catch (final NullPointerException ex) {
        }
        IFilter<File> aFilter = new FileFilterToIFilterAdapter(new FileFilterParentDirectoryPublic());
        assertTrue(aFilter.matchesFilter(new File("pom.xml")));
        assertTrue(aFilter.matchesFilter(new File("file.htm")));
        assertTrue(aFilter.matchesFilter(new File("src")));
        assertFalse(aFilter.matchesFilter(null));
        PhlocTestUtils.testToStringImplementation(aFilter);
        aFilter = FileFilterToIFilterAdapter.getANDChained(new FileFilterParentDirectoryPublic(), new FileFilterFileOnly());
        assertNotNull(aFilter);
        assertTrue(aFilter.matchesFilter(new File("pom.xml")));
        assertFalse(aFilter.matchesFilter(new File("file.htm")));
        assertFalse(aFilter.matchesFilter(new File("src")));
        assertFalse(aFilter.matchesFilter(null));
        PhlocTestUtils.testToStringImplementation(aFilter);
        aFilter = FileFilterToIFilterAdapter.getORChained(new FileFilterParentDirectoryPublic(), new FileFilterFileOnly());
        assertNotNull(aFilter);
        assertTrue(aFilter.matchesFilter(new File("pom.xml")));
        assertTrue(aFilter.matchesFilter(new File("file.htm")));
        assertTrue(aFilter.matchesFilter(new File("src")));
        assertFalse(aFilter.matchesFilter(null));
        PhlocTestUtils.testToStringImplementation(aFilter);
        try {
            FileFilterToIFilterAdapter.getANDChained((FileFilter[]) null);
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            FileFilterToIFilterAdapter.getANDChained(new FileFilter[0]);
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            FileFilterToIFilterAdapter.getORChained((FileFilter[]) null);
            fail();
        } catch (final IllegalArgumentException ex) {
        }
        try {
            FileFilterToIFilterAdapter.getORChained(new FileFilter[0]);
            fail();
        } catch (final IllegalArgumentException ex) {
        }
    }
}
