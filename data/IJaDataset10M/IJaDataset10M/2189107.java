package net.tomcatfort.loderogue.utils;

/**
 * Bass class for tag file related classes.
 * Provides basic definitions, but otherwise does nothing.
 * @see TagFileException
 * @see TagFileReader
 */
public abstract class TagFile {

    /**
     * Character that begins a tag.
     */
    protected static final char TAG_BEGIN = '[';

    /**
     * Character that separates tag name and tag attributes.
     */
    protected static final char TAG_DELIM = ':';

    /**
     * Character that marks the end of a tag.
     */
    protected static final char TAG_END = ']';

    /**
     * Marks an escape sequence in a tag.
     */
    protected static final char ESCAPE_SEQUENCE_BEGIN = '\\';

    /**
     * Marks a special escape sequence for arbitrary character code.
     */
    protected static final char CHARACTER_CODE_ESCAPING_BEGIN = '#';

    /**
     * Marks the end of a special escape sequence for arbitrary character code.
     */
    protected static final char CHARACTER_CODE_ESCAPING_END = ';';

    /**
     * Data file character set name.
     */
    protected static final String CHARSET_NAME = "UTF-8";

    /**
     * Creates the tag file.
     */
    TagFile() {
    }

    /**
     * Creates a string about the data file being read and the status of the reading.
     */
    public abstract String readingInformation();
}
