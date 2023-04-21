package org.gamegineer.table.internal.ui.util.swing;

import static org.junit.Assert.assertEquals;
import java.io.File;
import org.junit.Test;

/**
 * A fixture for testing the
 * {@link org.gamegineer.table.internal.ui.util.swing.SwingTextUtilities} class.
 */
public final class SwingTextUtilitiesTest {

    /**
     * Initializes a new instance of the {@code SwingTextUtilitiesTest} class.
     */
    public SwingTextUtilitiesTest() {
        super();
    }

    private static String normalizePath(final String path) {
        assert path != null;
        return path.replace('\\', File.separatorChar);
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has two path components.
     */
    @Test
    public void testShortenPath_Components_2() {
        final String path = normalizePath("C:\\filename.ext");
        assertEquals(normalizePath("C:\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has three path components.
     */
    @Test
    public void testShortenPath_Components_3() {
        final String path = normalizePath("C:\\dir1\\filename.ext");
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 19));
        assertEquals(normalizePath("C:\\dir1\\filename.ext"), SwingTextUtilities.shortenPath(path, 20));
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has four path components.
     */
    @Test
    public void testShortenPath_Components_4() {
        final String path = normalizePath("C:\\dir1\\dir2\\filename.ext");
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 19));
        assertEquals(normalizePath("C:\\dir1\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 24));
        assertEquals(normalizePath("C:\\dir1\\dir2\\filename.ext"), SwingTextUtilities.shortenPath(path, 25));
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has five path components.
     */
    @Test
    public void testShortenPath_Components_5() {
        final String path = normalizePath("C:\\dir1\\dir2\\dir3\\filename.ext");
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 19));
        assertEquals(normalizePath("C:\\dir1\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 24));
        assertEquals(normalizePath("C:\\dir1\\...\\dir3\\filename.ext"), SwingTextUtilities.shortenPath(path, 29));
        assertEquals(normalizePath("C:\\dir1\\dir2\\dir3\\filename.ext"), SwingTextUtilities.shortenPath(path, 30));
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has six path components.
     */
    @Test
    public void testShortenPath_Components_6() {
        final String path = normalizePath("C:\\dir1\\dir2\\dir3\\dir4\\filename.ext");
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 19));
        assertEquals(normalizePath("C:\\dir1\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 24));
        assertEquals(normalizePath("C:\\dir1\\...\\dir4\\filename.ext"), SwingTextUtilities.shortenPath(path, 29));
        assertEquals(normalizePath("C:\\dir1\\dir2\\...\\dir4\\filename.ext"), SwingTextUtilities.shortenPath(path, 34));
        assertEquals(normalizePath("C:\\dir1\\dir2\\dir3\\dir4\\filename.ext"), SwingTextUtilities.shortenPath(path, 35));
    }

    /**
     * Ensures the {@code shortenPath} method correctly shortens an absolute
     * path that has seven path components.
     */
    @Test
    public void testShortenPath_Components_7() {
        final String path = normalizePath("C:\\dir1\\dir2\\dir3\\dir4\\dir5\\filename.ext");
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 8));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 14));
        assertEquals(normalizePath("C:\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 19));
        assertEquals(normalizePath("C:\\dir1\\...\\filename.ext"), SwingTextUtilities.shortenPath(path, 24));
        assertEquals(normalizePath("C:\\dir1\\...\\dir5\\filename.ext"), SwingTextUtilities.shortenPath(path, 29));
        assertEquals(normalizePath("C:\\dir1\\dir2\\...\\dir5\\filename.ext"), SwingTextUtilities.shortenPath(path, 34));
        assertEquals(normalizePath("C:\\dir1\\dir2\\...\\dir4\\dir5\\filename.ext"), SwingTextUtilities.shortenPath(path, 39));
        assertEquals(normalizePath("C:\\dir1\\dir2\\dir3\\dir4\\dir5\\filename.ext"), SwingTextUtilities.shortenPath(path, 40));
    }

    /**
     * Ensures the {@code shortenPath} method throws an exception when passed a
     * negative maximum length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testShortenPath_MaxLength_Negative() {
        SwingTextUtilities.shortenPath("path", -1);
    }

    /**
     * Ensures the {@code shortenPath} method throws an exception when passed a
     * {@code null} path.
     */
    @Test(expected = NullPointerException.class)
    public void testShortenPath_Path_Null() {
        SwingTextUtilities.shortenPath(null, 0);
    }
}
