package eclant.ant.types;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.selectors.FileSelector;
import org.apache.tools.ant.types.selectors.SelectorUtils;

public class ResourceSelector {

    /**
	 * Set of all include patterns that are full file names and don't contain
	 * any wildcards.
	 * 
	 * <p>
	 * If this instance is not case sensitive, the file names get turned to
	 * lower case.
	 * </p>
	 * 
	 * <p>
	 * Gets lazily initialized on the first invocation of isIncluded or
	 * isExcluded and cleared at the end of the scan method (cleared in
	 * clearCaches, actually).
	 * </p>
	 * 
	 * @since Ant 1.6.3
	 */
    private final Set<String> includeNonPatterns = new HashSet<String>();

    /**
	 * Set of all include patterns that are full file names and don't contain
	 * any wildcards.
	 * 
	 * <p>
	 * If this instance is not case sensitive, the file names get turned to
	 * lower case.
	 * </p>
	 * 
	 * <p>
	 * Gets lazily initialized on the first invocation of isIncluded or
	 * isExcluded and cleared at the end of the scan method (cleared in
	 * clearCaches, actually).
	 * </p>
	 * 
	 * @since Ant 1.6.3
	 */
    private final Set<String> excludeNonPatterns = new HashSet<String>();

    /**
	 * Array of all include patterns that contain wildcards.
	 * 
	 * <p>
	 * Gets lazily initialized on the first invocation of isIncluded or
	 * isExcluded and cleared at the end of the scan method (cleared in
	 * clearCaches, actually).
	 * </p>
	 * 
	 * @since Ant 1.6.3
	 */
    private String[] includePatterns;

    /**
	 * Array of all exclude patterns that contain wildcards.
	 * 
	 * <p>
	 * Gets lazily initialized on the first invocation of isIncluded or
	 * isExcluded and cleared at the end of the scan method (cleared in
	 * clearCaches, actually).
	 * </p>
	 * 
	 * @since Ant 1.6.3
	 */
    private String[] excludePatterns;

    /** The patterns for the files to be included. */
    private String[] includes;

    /** The patterns for the files to be excluded. */
    private String[] excludes;

    /** Selectors that will filter which files are in our candidate list. */
    private FileSelector[] selectors = null;

    /**
	 * Whether or not the file system should be treated as a case sensitive one.
	 */
    private boolean isCaseSensitive = true;

    /**
	 * Have the non-pattern sets and pattern arrays for in- and excludes been
	 * initialized?
	 * 
	 * @since Ant 1.6.3
	 */
    private boolean areNonPatternSetsReady = false;

    /**
	 * Add default exclusions to the current exclusions set.
	 */
    public void addDefaultExcludes() {
        int excludesLength = excludes == null ? 0 : excludes.length;
        String[] newExcludes;
        String[] defaultExcludesTemp = DirectoryScanner.getDefaultExcludes();
        newExcludes = new String[excludesLength + defaultExcludesTemp.length];
        if (excludesLength > 0) {
            System.arraycopy(excludes, 0, newExcludes, 0, excludesLength);
        }
        for (int i = 0; i < defaultExcludesTemp.length; i++) {
            newExcludes[i + excludesLength] = normalizePattern(defaultExcludesTemp[i]);
        }
        excludes = newExcludes;
    }

    /**
	 * Set the list of include patterns to use. Separator chars are normalized
	 * to '/' to match Eclipse conventions.
	 * <p>
	 * When a pattern ends with a '/' or '\', "**" is appended.
	 * 
	 * @param includes
	 *            A list of include patterns. May be <code>null</code>,
	 *            indicating that all files should be included. If a non-
	 *            <code>null</code> list is given, all elements must be non-
	 *            <code>null</code>.
	 */
    public void setIncludes(String[] includes) {
        if (includes == null) {
            this.includes = null;
        } else {
            this.includes = new String[includes.length];
            for (int i = 0; i < includes.length; i++) {
                this.includes[i] = normalizePattern(includes[i]);
            }
        }
    }

    /**
	 * Set the list of exclude patterns to use. Separator chars are normalized
	 * to '/' to match Eclipse conventions.
	 * <p>
	 * When a pattern ends with a '/' or '\', "**" is appended.
	 * 
	 * @param excludes
	 *            A list of exclude patterns. May be <code>null</code>,
	 *            indicating that no files should be excluded. If a non-
	 *            <code>null</code> list is given, all elements must be non-
	 *            <code>null</code>.
	 */
    public void setExcludes(String[] excludes) {
        if (excludes == null) {
            this.excludes = null;
        } else {
            this.excludes = new String[excludes.length];
            for (int i = 0; i < excludes.length; i++) {
                this.excludes[i] = normalizePattern(excludes[i]);
            }
        }
    }

    /**
	 * Set the selectors that will select the filelist.
	 * 
	 * @param selectors
	 *            specifies the selectors to be invoked on a scan.
	 */
    public void setSelectors(FileSelector[] selectors) {
        this.selectors = selectors;
    }

    /**
	 * Set whether or not include and exclude patterns are matched in a case
	 * sensitive way.
	 * 
	 * @param isCaseSensitive
	 *            whether or not the file system should be regarded as a case
	 *            sensitive one.
	 */
    public void setCaseSensitive(boolean isCaseSensitive) {
        this.isCaseSensitive = isCaseSensitive;
    }

    /**
	 * Find out whether include exclude patterns are matched in a case sensitive
	 * way.
	 * 
	 * @return whether or not the scanning is case sensitive.
	 * @since Ant 1.6
	 */
    public boolean isCaseSensitive() {
        return isCaseSensitive;
    }

    /**
	 * Test whether or not a name matches against at least one include pattern.
	 * 
	 * @param name
	 *            The name to match. Must not be <code>null</code>.
	 * @return <code>true</code> when the name matches against at least one
	 *         include pattern, or <code>false</code> otherwise.
	 */
    protected boolean isIncluded(String name) {
        ensureNonPatternSetsReady();
        if (includePatterns == null) return true;
        if (isCaseSensitive() ? includeNonPatterns.contains(name) : includeNonPatterns.contains(name.toUpperCase())) {
            return true;
        }
        for (int i = 0; i < includePatterns.length; i++) {
            if (SelectorUtils.matchPath(includePatterns[i], name, isCaseSensitive)) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Test whether or not a name matches against at least one exclude pattern.
	 * 
	 * @param name
	 *            The name to match. Must not be <code>null</code>.
	 * @return <code>true</code> when the name matches against at least one
	 *         exclude pattern, or <code>false</code> otherwise.
	 */
    protected boolean isExcluded(String name) {
        ensureNonPatternSetsReady();
        if (excludePatterns == null) return false;
        if (isCaseSensitive() ? excludeNonPatterns.contains(name) : excludeNonPatterns.contains(name.toUpperCase())) {
            return true;
        }
        for (int i = 0; i < excludePatterns.length; i++) {
            if (SelectorUtils.matchPath(excludePatterns[i], name, isCaseSensitive())) {
                return true;
            }
        }
        return false;
    }

    /**
	 * Test whether a file should be selected.
	 * 
	 * @param name
	 *            the filename to check for selecting.
	 * @param file
	 *            the java.io.File object for this filename.
	 * @return <code>false</code> when the selectors says that the file should
	 *         not be selected, <code>true</code> otherwise.
	 */
    protected boolean isSelected(File basedir, String name, File file) {
        if (selectors != null) {
            for (int i = 0; i < selectors.length; i++) {
                if (!selectors[i].isSelected(basedir, name, file)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
	 * Separator chars are normalized to '/' to match Eclipse conventions.
	 * 
	 * <p>
	 * When a pattern ends with a '/' or '\', "**" is appended.
	 * 
	 * @since Ant 1.6.3
	 */
    protected String normalizePattern(String p) {
        String pattern = p.replace(File.separatorChar, '/');
        if (pattern.endsWith("/")) {
            pattern += "**";
        }
        return pattern;
    }

    /**
	 * Ensure that the in|exclude &quot;patterns&quot; have been properly
	 * divided up.
	 * 
	 * @since Ant 1.6.3
	 */
    private void ensureNonPatternSetsReady() {
        if (!areNonPatternSetsReady) {
            includePatterns = fillNonPatternSet(includeNonPatterns, includes);
            excludePatterns = fillNonPatternSet(excludeNonPatterns, excludes);
            areNonPatternSetsReady = true;
        }
    }

    /**
	 * Add all patterns that are not real patterns (do not contain wildcards) to
	 * the set and returns the real patterns.
	 * 
	 * @param set
	 *            Set to populate.
	 * @param patterns
	 *            String[] of patterns.
	 * @since Ant 1.6.3
	 */
    private String[] fillNonPatternSet(Set<String> set, String[] patterns) {
        if (patterns == null) return null;
        ArrayList<String> al = new ArrayList<String>(patterns.length);
        for (int i = 0; i < patterns.length; i++) {
            if (!SelectorUtils.hasWildcards(patterns[i])) {
                set.add(isCaseSensitive() ? patterns[i] : patterns[i].toUpperCase());
            } else {
                al.add(patterns[i]);
            }
        }
        return set.size() == 0 ? patterns : (String[]) al.toArray(new String[al.size()]);
    }
}
