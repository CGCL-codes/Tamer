package com.volantis.mcs.protocols.hr;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.HorizontalRuleAttributes;
import com.volantis.mcs.protocols.ProtocolException;
import com.volantis.mcs.themes.StyleLength;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.values.LengthUnit;

/**
 * Emulates HR elements with heights greater than 1 pixel.
 */
public class HorizontalRuleEmulatorWithMultipleHRs extends DefaultHorizontalRuleEmulator {

    protected Element doEmulation(DOMOutputBuffer domOutputBuffer, HorizontalRuleAttributes hrAttrs) throws ProtocolException {
        int heightVal = getStyleValueAsInt(height);
        String colorValue = color.getStandardCSS();
        Element element = domOutputBuffer.openElement("div");
        for (int i = 0; i < heightVal; i++) {
            addHorizontalRule(domOutputBuffer, colorValue);
        }
        domOutputBuffer.closeElement("div");
        return element;
    }

    /**
     * Gets the style value as an int.  If the style value does not translate
     * to an int, an exception is thrown.
     *
     * @param styleValue the value to convert
     * @return the int value of the style value, or Integer.MIN_VALUE if
     * svStr is null.
     * @throws NumberFormatException if the style value cannot be represented
     * as an int.
     */
    private static int getStyleValueAsInt(StyleValue styleValue) throws NumberFormatException {
        int i = Integer.MIN_VALUE;
        if (styleValue instanceof StyleLength) {
            StyleLength length = (StyleLength) styleValue;
            if (length.getUnit() == LengthUnit.PX) {
                i = (int) length.getNumber();
            }
        }
        return i;
    }

    /**
     * Adds a horizontal rule to the buffer.
     *
     * @param domOutputBuffer the output buffer.
     * @param color the color value
     */
    private void addHorizontalRule(DOMOutputBuffer domOutputBuffer, String color) {
        Element hr = domOutputBuffer.openElement("hr");
        StringBuffer sBuf = new StringBuffer("padding:0px;margin:0px;");
        if (color != null && color.length() > 0) {
            sBuf.append("color:");
            sBuf.append(color);
            sBuf.append(";");
        }
        hr.setAttribute("style", sBuf.toString());
        domOutputBuffer.closeElement("hr");
    }
}
