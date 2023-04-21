package org.jcvi.common.util;

/**
 * An <code>ANSIFormatter</code> is a helper object which makes it easier to
 * format text with {@link ANSIAttribute}s.  ANSI attributes need
 * to be set and cleared around each block of text that is formatted.  However,
 * if a {@link ANSIAttribute#RESET} attribute is missed, it can leave the 
 * display in a stale state which may range from trivially annoying to nearly
 * unusable.  The <code>ANSIFormatter</code> improves this by providing easy
 * encapsulation for formatting and guaranteed style termination.
 * <p>
 * The <code>ANSIFormatter</code> follows a "fluent" interface style where each
 * method will return the original formatter object, allowing you to chain
 * additions one after another.
 *
 * @author jsitz@jcvi.org
 */
public class ANSIFormatter {

    /** The formatted string */
    private final StringBuilder formatted;

    /**
     * Creates a new <code>ANSIConsolePrintStream</code> with no text or 
     * formatting within it.
     */
    public ANSIFormatter() {
        super();
        this.formatted = new StringBuilder();
    }

    /**
     * Creates a new <code>ANSIFormatter</code> with the given text formatted
     * with the given attributes.
     * <p>
     * This is equivalent to building a new empty <code>ANSIFormatter</code>
     * and using {@link #text(String, ANSIAttribute...)} to add text.
     * 
     * @param text The text to format.
     * @param attrs The {@link ANSIAttribute}s to appy to the text.
     * @see #text(String, ANSIAttribute...)
     */
    public ANSIFormatter(CharSequence text, ANSIAttribute... attrs) {
        this();
        this.text(text, attrs);
    }

    /**
     * Adds a native line terminator to the formatted text.
     * 
     * @return This formatter.
     */
    public ANSIFormatter newline() {
        this.formatted.append("\n");
        return this;
    }

    /**
     * Adds formatted text to the formatter.
     * <p>
     * This is the base implementation for all other text additions.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(CharSequence data, ANSIAttribute... attrs) {
        final boolean hasFormat = attrs.length > 0;
        for (final ANSIAttribute attr : attrs) {
            this.formatted.append(attr);
        }
        this.formatted.append(data);
        if (hasFormat) {
            this.formatted.append(ANSIAttribute.RESET);
        }
        return this;
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(String data, ANSIAttribute... attrs) {
        return this.text((CharSequence) data, attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * <p>
     * This will accept generic objects and will use the result of their 
     * {@link Object#toString()} method as the text to add.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(Object data, ANSIAttribute... attrs) {
        return this.text(data.toString(), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(byte data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(short data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(int data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param radix The radix to use when converting the number to text.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(int data, int radix, ANSIAttribute... attrs) {
        return this.text(Integer.toString(data, radix), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(long data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param radix The radix to use when converting the number to text.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(long data, int radix, ANSIAttribute... attrs) {
        return this.text(Long.toString(data, radix), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(float data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(double data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds formatted text to the formatter.
     * 
     * @param data The data to be formatted.
     * @param attrs The attributes to apply to the text.
     * @return This formatter
     */
    public ANSIFormatter text(boolean data, ANSIAttribute... attrs) {
        return this.text(String.valueOf(data), attrs);
    }

    /**
     * Adds bolded text to the formatter.
     * 
     * @param text The text to add.
     * @return This formatter.
     */
    public ANSIFormatter bold(CharSequence text) {
        return this.text(text, ANSIAttribute.BOLD);
    }

    /**
     * Adds underlined text to the formatter.
     * 
     * @param text The text to add.
     * @return This formatter.
     */
    public ANSIFormatter underscore(String text) {
        return this.text(text, ANSIAttribute.UNDERSCORE);
    }

    /**
     * Adds blinking text to the formatter.
     * 
     * @param text The text to add.
     * @return This formatter.
     */
    public ANSIFormatter blink(String text) {
        return this.text(text, ANSIAttribute.BLINK);
    }

    /**
     * Adds reverse text to the formatter.  
     * <p>
     * <strong>Note:</strong> in this case "reversed" refers
     * to the ANSI color specification, where the background and foreground 
     * colors of this text will be swapped. This is often used for highlighting
     * text on displays without needing to re-set the color.  However, that 
     * functionality is not readily available through this API.
     * 
     * @param text The text to add.
     * @return This formatter.
     */
    public ANSIFormatter reverse(String text) {
        return this.text(text, ANSIAttribute.REVERSE);
    }

    /**
     * Adds concealed text to the formatter.  By specification, this should be
     * text which is not readily visible to the user, but can be easily read
     * once it is highlighted (usually by mouse selection).
     * 
     * @param text The text to add.
     * @return This formatter.
     */
    public ANSIFormatter conceal(String text) {
        return this.text(text, ANSIAttribute.CONCEAL);
    }

    /**
     * Resets the ANSI formatting for the preceding string.  
     * <p>
     * <strong>Note:</strong> This method should not normally be necessary as 
     * the formatter will automatically reset the display after each formatted
     * block.  However, in some situations, it may be useful to force a reset
     * at the start of some formatting blocks, in case previous operations 
     * left formatting in place.
     * 
     * @return This formatter.
     */
    public ANSIFormatter reset() {
        return this.text("", ANSIAttribute.RESET);
    }

    @Override
    public String toString() {
        return this.formatted.toString();
    }
}
