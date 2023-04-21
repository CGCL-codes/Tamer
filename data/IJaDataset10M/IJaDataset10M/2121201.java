package com.volantis.mcs.css.renderer;

import com.volantis.mcs.themes.StyleProperties;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.mappers.KeywordMapper;

/**
 * Property renderer for counter-increment.
 */
public class CounterIncrementRenderer extends GenericCounterRenderer {

    public String getName() {
        return "counter-increment";
    }

    public KeywordMapper getKeywordMapper(RendererContext context) {
        return context.getKeywordMapper(StylePropertyDetails.COUNTER_INCREMENT);
    }

    public StyleValue getValue(StyleProperties properties) {
        return properties.getStyleValue(StylePropertyDetails.COUNTER_INCREMENT);
    }

    public PropertyValue getPropertyValue(StyleProperties properties) {
        return properties.getPropertyValue(StylePropertyDetails.COUNTER_INCREMENT);
    }
}
