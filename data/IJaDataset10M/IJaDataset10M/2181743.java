package com.volantis.styling.impl.functions.counter;

import com.volantis.mcs.themes.StyleKeyword;

/**
 * Encapsulates a fixed formatter.
 *
 * <p>Simply returns the formatter that was supplied on the constructor.</p>
 */
public class FixedFormatterSelector implements CounterFormatterSelector {

    /**
     * The fixed formatter.
     */
    private final CounterFormatter formatter;

    /**
     * Initialise.
     *
     * @param formatter The fixed formatter.
     */
    public FixedFormatterSelector(CounterFormatter formatter) {
        this.formatter = formatter;
    }

    public CounterFormatter selectFormatter(StyleKeyword formatStyle, int counterValue) {
        return formatter;
    }
}
