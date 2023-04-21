package org.archive.state;

import java.io.File;

/**
 * @author pjack
 *
 */
public interface DirectoryModule {

    File getDirectory();

    /**
     * Makes a possibly relative path absolute.  If the given path is 
     * relative, then the returned string will be an absolute path 
     * relative to getDirectory().
     * 
     * <p>If the given path is already absolute, then it is returned unchanged.
     * 
     * @param path   the path to make absolute
     * @return   the absolute path
     */
    String toAbsolutePath(String path);

    /**
     * Makes a possibly absolute path relative.  If the given path is 
     * relative, then it is returned unchanged.  Otherwise, if the given 
     * absolute path represents a subdirectory of getDirectory, then 
     * the subdirectory path is returned as a relative path.
     * 
     * Eg, if getDirectory() returns <code>/foo/bar</code>, then:
     * 
     * <ul>
     * <li><code>toRelativePath("/foo/bar/baz/snafu")</code> should return
     * <codeE>baz/snafu</code>.</li>
     * <li><code>toRelativePath("/fnord/x")</code> should return 
     * <code>/fnord/x</code>.</li>
     * <li><code>toRelativePath("a/b/c")</code> should return <code>a/b/c</code>.
     * </ul>
     * 
     * @param path
     * @return
     */
    String toRelativePath(String path);
}
