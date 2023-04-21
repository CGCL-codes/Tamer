package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.CSSParserMessages;
import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.themes.Priority;
import com.volantis.mcs.themes.PropertyValue;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.MutableStyleProperties;
import com.volantis.mcs.themes.ThemeFactory;
import com.volantis.styling.properties.StyleProperty;

/**
 * Base for those property parsers that handle setting a single property.
 */
public abstract class AbstractSinglePropertyParser extends AbstractPropertyParser implements ShorthandValueHandler {

    private final StyleProperty property;

    protected final int expectedValueCount;

    public AbstractSinglePropertyParser(StyleProperty property, int expectedValueCount) {
        this.property = property;
        this.expectedValueCount = expectedValueCount;
    }

    public void setPropertyValue(MutableStyleProperties properties, StyleValue value, Priority priority) {
        PropertyValue propertyValue;
        if (value == null) {
            propertyValue = null;
        } else {
            propertyValue = ThemeFactory.getDefaultInstance().createPropertyValue(property, value, priority);
        }
        properties.setPropertyValue(propertyValue);
    }

    public StyleValue getInitial() {
        return property.getStandardDetails().getInitialValue();
    }

    public String getShorthandReference() {
        return "<'" + property.getName() + "'>";
    }

    protected void parseImpl(ParserContext context, StyleValueIterator iterator) {
        StyleValue value;
        int total = iterator.remaining();
        value = convert(context, iterator);
        if (value == null) {
            StyleValue error = iterator.value();
            context.addDiagnostic(error, CSSParserMessages.NOT_ALLOWABLE_VALUE, new Object[] { context.getCurrentPropertyName(), error.getStandardCSS() });
        } else {
            int remaining = iterator.remaining();
            if (remaining > 0) {
                context.addDiagnostic(iterator.value(), CSSParserMessages.TOO_MANY_VALUES, new Object[] { context.getCurrentPropertyName(), new Integer(expectedValueCount), new Integer(total) });
            } else {
                context.setPropertyValue(property, value);
            }
        }
    }
}
