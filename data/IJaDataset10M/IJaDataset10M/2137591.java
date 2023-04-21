package org.gwt.advanced.client.ui.widget.cell;

/**
 * This is a cell implementation for <code>Long</code> numbers.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class LongCell extends NumberCell {

    /** Constructs a new LongCell. */
    public LongCell() {
        super(INTEGER_PATTERN, Long.MAX_VALUE, Long.MIN_VALUE);
    }

    /** {@inheritDoc} */
    protected Number convertToNumber(String text) {
        return Long.valueOf(text);
    }
}
