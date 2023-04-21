package com.gargoylesoftware.htmlunit.javascript.host.html;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * The JavaScript object "HTMLFontElement".
 *
 * @version $Revision: 6701 $
 * @author Ahmed Ashour
 */
public class HTMLFontElement extends HTMLElement {

    /**
     * Creates an instance.
     */
    public HTMLFontElement() {
    }

    /**
     * Gets the "color" attribute.
     * @return the "color" attribute
     */
    public String jsxGet_color() {
        return getDomNodeOrDie().getAttribute("color");
    }

    /**
     * Sets the "color" attribute.
     * @param color the "color" attribute
     */
    public void jsxSet_color(final String color) {
        getDomNodeOrDie().setAttribute("color", color);
    }

    /**
     * Gets the typeface family.
     * @return the typeface family
     */
    public String jsxGet_face() {
        return getDomNodeOrDie().getAttribute("face");
    }

    /**
     * Sets the typeface family.
     * @param face the typeface family
     */
    public void jsxSet_face(final String face) {
        getDomNodeOrDie().setAttribute("face", face);
    }

    /**
     * Gets the "size" attribute.
     * @return the "size" attribute
     */
    public int jsxGet_size() {
        return (int) Context.toNumber(getDomNodeOrDie().getAttribute("size"));
    }

    /**
     * Sets the "size" attribute.
     * @param size the "size" attribute
     */
    public void jsxSet_size(final int size) {
        getDomNodeOrDie().setAttribute("size", Context.toString(size));
    }
}
