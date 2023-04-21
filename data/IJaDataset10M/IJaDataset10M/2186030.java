package org.csvtools.base;

import org.csvtools.parsers.CsvFileParser;
import org.csvtools.writers.CsvFileWriter;
import java.util.List;

/**
 * CSVFile is a class used to handle <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Comma-Separated Values</a> files.
 * <p>
 * It is abstract because it is the base class used for {@link CsvFileParser} and {@link CsvFileWriter}
 * so you should use one of these (or both) according on what you need to do.
 *
 * @author  Fabrizio Fazzino
 * @version %I%, %G%
 */
public abstract class CsvParser {

    /**
	 * The default char used as field separator.
	 */
    protected static final char DEFAULT_FIELD_SEPARATOR = ',';

    /**
	 * The default char used as text qualifier
	 */
    protected static final char DEFAULT_TEXT_QUALIFIER = '"';

    /**
	 * The current char used as field separator.
	 */
    protected char fieldSeparator;

    /**
	 * The current char used as text qualifier.
	 */
    protected char textQualifier;

    /**
	 * CSVFile constructor with the default field separator and text qualifier.
	 */
    public CsvParser() {
        this(DEFAULT_FIELD_SEPARATOR, DEFAULT_TEXT_QUALIFIER);
    }

    /**
	 * CSVFile constructor with a given field separator and the default text qualifier.
	 *
	 * @param sep The field separator to be used; overwrites the default one
	 */
    public CsvParser(char sep) {
        this(sep, DEFAULT_TEXT_QUALIFIER);
    }

    /**
	 * CSVFile constructor with given field separator and text qualifier.
	 *
	 * @param sep  The field separator to be used; overwrites the default one
	 * @param qual The text qualifier to be used; overwrites the default one
	 */
    public CsvParser(char sep, char qual) {
        setFieldSeparator(sep);
        setTextQualifier(qual);
    }

    /**
	 * Set the current field separator.
	 *
	 * @param sep The new field separator to be used; overwrites the old one
	 */
    public void setFieldSeparator(char sep) {
        fieldSeparator = sep;
    }

    /**
	 * Set the current text qualifier.
	 *
	 * @param qual The new text qualifier to be used; overwrites the old one
	 */
    public void setTextQualifier(char qual) {
        textQualifier = qual;
    }

    /**
	 * Get the current field separator.
	 *
	 * @return The char containing the current field separator
	 */
    public char getFieldSeparator() {
        return fieldSeparator;
    }

    /**
	 * Get the current text qualifier.
	 *
	 * @return The char containing the current text qualifier
	 */
    public char getTextQualifier() {
        return textQualifier;
    }

    protected int parseLine(String line, List<String> list) {
        int count = 0;
        StringBuffer sb = new StringBuffer();
        if (line == null) line = "";
        int i = 0;
        while (i < line.length()) {
            sb.setLength(0);
            if (i < line.length() && line.charAt(i) == textQualifier) {
                i = handleQuotedField(line, sb, ++i);
            } else {
                i = handlePlainField(line, sb, i);
            }
            list.add(sb.toString());
            i++;
        }
        ;
        return count;
    }

    /**
	 * Handles a quoted field.
	 *
	 * @return index of next separator
	 */
    private int handleQuotedField(String s, StringBuffer sb, int i) {
        int j;
        int len = s.length();
        for (j = i; j < len; j++) {
            if ((s.charAt(j) == textQualifier) && (j + 1 < len)) {
                if (s.charAt(j + 1) == textQualifier) {
                    j++;
                } else if (s.charAt(j + 1) == fieldSeparator) {
                    j++;
                    break;
                }
            } else if ((s.charAt(j) == textQualifier) && (j + 1 == len)) {
                break;
            }
            sb.append(s.charAt(j));
        }
        return j;
    }

    /**
	 * Handles an unquoted field.
	 *
	 * @return index of next separator
	 */
    private int handlePlainField(String s, StringBuffer sb, int i) {
        int j = s.indexOf(fieldSeparator, i);
        if (j == -1) {
            sb.append(s.substring(i));
            return s.length();
        } else {
            sb.append(s.substring(i, j));
            return j;
        }
    }
}
