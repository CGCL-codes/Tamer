package org.scilab.forge.jlatexmath;

/**
 * Signals a missing text style mapping.
 * 
 * @author Kurt Vermeulen
 */
public class TextStyleMappingNotFoundException extends JMathTeXException {

    protected TextStyleMappingNotFoundException(String styleName) {
        super("No mapping found for the text style '" + styleName + "'! " + "Insert a <" + DefaultTeXFontParser.STYLE_MAPPING_EL + ">-element in '" + DefaultTeXFontParser.RESOURCE_NAME + "'.");
    }
}
