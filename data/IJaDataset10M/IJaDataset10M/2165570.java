package net.sf.doolin.util;

import java.text.MessageFormat;
import java.text.ParseException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

/**
 * This pattern works with DOS like patterns.
 * 
 * @author Damien Coraboeuf
 */
public class PatternFormat {

    /**
	 * Format used
	 */
    private MessageFormat format;

    /**
	 * Pattern
	 */
    private String pattern;

    /**
	 * Initialization
	 * 
	 * @param pattern
	 *            Pattern to use
	 */
    public PatternFormat(String pattern) {
        this.pattern = pattern;
        StringBuffer buffer = new StringBuffer();
        int pos;
        int oldpos = 0;
        int count = 0;
        while ((pos = pattern.indexOf('*', oldpos)) >= 0) {
            buffer.append(pattern.substring(oldpos, pos));
            buffer.append('{').append(count++).append('}');
            oldpos = pos + 1;
        }
        buffer.append(pattern.substring(oldpos));
        format = new MessageFormat(buffer.toString());
    }

    /**
	 * Utility method
	 * 
	 * @param params
	 *            Parameters to fill the message
	 * @return Formatted string
	 */
    public String format(Object... params) {
        return format.format(params);
    }

    /**
	 * Utility method
	 * 
	 * @param path
	 *            String to check
	 * @return Result of the check
	 */
    public boolean accept(String path) {
        try {
            Object[] content = parse(path);
            String value = format.format(content);
            return path.equals(value);
        } catch (ParseException ex) {
            return false;
        }
    }

    /**
	 * Parse the path
	 * 
	 * @param path
	 *            String to parse
	 * @return Result of the parsing
	 * @throws ParseException
	 *             If the parsing cannot be done
	 */
    public Object[] parse(String path) throws ParseException {
        Object[] content = format.parse(path);
        return content;
    }

    /**
	 * @see java.lang.Object#toString()
	 */
    @Override
    public String toString() {
        return pattern;
    }

    /**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        return ObjectUtils.hashCode(pattern);
    }

    /**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PatternFormat) {
            PatternFormat p = (PatternFormat) obj;
            return StringUtils.equals(this.pattern, p.pattern);
        } else {
            return false;
        }
    }
}
