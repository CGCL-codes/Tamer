package com.volantis.mcs.runtime.layouts.styling;

import com.volantis.mcs.layouts.Layout;
import com.volantis.mcs.themes.StyleSheet;

/**
 * @mock.generate
 */
public interface LayoutStyleSheetBuilder {

    /**
     * Build a {@link StyleSheet} for the {@link Layout}.
     *
     * @param layout The device layout for which the elements will be
     * applied.
     *
     * @return A {@link StyleSheet} that contains stylistic information from
     * the device layout.
     */
    StyleSheet build(Layout layout);
}
