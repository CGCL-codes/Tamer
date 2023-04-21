package org.peaseplate;

/**
 * The abstract implementation of a template exception that is
 * linked to some tempalte source.
 * 
 * @author Manfred HANTSCHEL
 */
public abstract class AbstractLocatedTemplateException extends TemplateException {

    private static final long serialVersionUID = 1L;

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final TemplateLocator locator;

    private final int line;

    private final int column;

    /**
	 * Creates the exception using the specified locator, line and column number and the message.
	 * @param locator the locator
	 * @param line the line
	 * @param column the column
	 * @param message the message
	 */
    public AbstractLocatedTemplateException(TemplateLocator locator, int line, int column, String message) {
        this(locator, line, column, message, null);
    }

    /**
	 * Creates the exception using the specified locator, line and column number and the message.
	 * Additionally it gets the cause for the exception.
	 * @param locator the locator
	 * @param line the line
	 * @param column the column
	 * @param message the message
	 * @param cause the cause
	 */
    public AbstractLocatedTemplateException(TemplateLocator locator, int line, int column, String message, Throwable cause) {
        super(addHighlight(locator, line, column, message), cause);
        this.locator = locator;
        this.line = line;
        this.column = column;
    }

    /**
	 * Returns the locator of the template where the exception occurred 
	 * @return the locator
	 */
    public TemplateLocator getLocator() {
        return locator;
    }

    /**
	 * Returns the line of the template where the exception occurred
	 * @return the line
	 */
    public int getLine() {
        return line;
    }

    /**
	 * Returns the column of the template where the exception occurred
	 * @return the column
	 */
    public int getColumn() {
        return column;
    }

    private static String addHighlight(TemplateLocator locator, int line, int column, String message) {
        StringBuilder builder = new StringBuilder(message);
        if (locator != null) {
            String highlight = locator.highlight(line, column);
            if (highlight != null) {
                builder.append(LINE_SEPARATOR).append(LINE_SEPARATOR);
                builder.append(locator).append(" [").append(line).append(", ").append(column).append("]:").append(LINE_SEPARATOR);
                builder.append(highlight);
            } else {
                builder.append(" (").append(locator).append(" [").append(line).append(", ").append(column).append("])");
            }
        }
        return builder.toString();
    }
}
