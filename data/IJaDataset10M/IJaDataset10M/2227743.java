package com.volantis.mcs.dom2theme.extractor;

import com.volantis.mcs.themes.ShorthandSet;
import com.volantis.styling.properties.PropertyDetailsSet;
import com.volantis.styling.sheet.CompiledStyleSheet;

/**
 * Contains configuration needed by the CSS extractor.
 */
public interface ExtractorConfiguration {

    /**
     * The details of the properties supported by the target device.
     *
     * @return The details of the properties supported by the target device.
     */
    PropertyDetailsSet getPropertyDetailsSet();

    /**
     * The set of shorthands supported by the target device.
     *
     * @return The set of shorthands supported by the target device.
     */
    ShorthandSet getSupportedShorthands();

    /**
     * A compiled representation of the style sheet used by the device.
     *
     * @return A compiled representation of the style sheet used by the device.
     */
    CompiledStyleSheet getDeviceStyleSheet();
}
