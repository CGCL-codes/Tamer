package net.grade.averagegrade.parse;

/**
 * An interface for classes that are intended as parsers, who can extract
 * course-related information from text. The interface provides a
 * <code>containsEntries</code> method that can be used to check a text for
 * entries before actually trying to parse.
 * <p/>
 * The actual extraction of entries is provided through the <code>extractEntries</code>
 * method, which returns a <code>CourseEntry</code> array. Most of the times it's
 * recommended to first use the previously mentioned method for checking a text
 * for entries, before using the extraction method.
 *
 * @see CourseEntry
 */
public interface CourseEntriesParser {

    /**
     * Tries to extract entries from the specified text. If unable to
     * extract any entries, an empty <code>CourseEntry</code> array will
     * be returned instead. Most of the times, it's recommended to first
     * have used the <code>containsEntries</code> method, in order to
     * verify that the text actually contains entries.
     *
     * @param text The text to extract entries from.
     * @return A <code>CourseEntry</code> array with the extract entries.
     *         If no entries was found, an empty array is returned.
     */
    public CourseEntry[] extractEntries(String text);

    /**
     * Checks if the specified text contains entries that the parser
     * can detect and extract. If that is so, <tt>true</tt> will be
     * returned. For extraction, use the <code>extractEntries</code>
     * method instead.
     *
     * @param text The text to check for matching entries.
     * @return <tt>true</tt> if the specified text contains entries
     *         that the parser can detect and extract; otherwise
     *         <tt>false</tt>.
     */
    public boolean containsEntries(String text);
}
