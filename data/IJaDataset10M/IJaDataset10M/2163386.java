package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.css.impl.parser.ParserContext;
import com.volantis.mcs.css.impl.parser.Separator;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValueType;
import com.volantis.mcs.themes.properties.FontFamilyKeywords;
import java.util.HashSet;
import java.util.Set;

/**
 * Parser for the font-family property.
 */
public class FontFamilyParser extends AbstractListParser {

    /**
     * The converter for items in the list.
     */
    private static final ValueConverter ITEM_CONVERTER;

    static {
        Set supportedTypes = new HashSet();
        supportedTypes.add(StyleValueType.KEYWORD);
        supportedTypes.add(StyleValueType.STRING);
        supportedTypes.add(StyleValueType.IDENTIFIER);
        ITEM_CONVERTER = new SimpleValueConverter(supportedTypes, FontFamilyKeywords.getDefaultInstance());
    }

    /**
     * Initialise.
     */
    public FontFamilyParser() {
        super(StylePropertyDetails.FONT_FAMILY, ITEM_CONVERTER, true);
    }

    protected void checkSeparator(ParserContext context, StyleValueIterator iterator) {
        iterator.separator(Separator.COMMA);
    }
}
