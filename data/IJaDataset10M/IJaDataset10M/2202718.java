package de.esoco.ewt.graphics;

import javax.microedition.lcdui.Graphics;

/********************************************************************
 * An abstract base class for implementations that perform the rendering of
 * fonts and the corresponding size calculations. For the latter purpose it
 * extends the {@link FontMetrics} interface.
 *
 * @author eso
 */
public abstract class FontRenderer implements FontMetrics {

    /***************************************
	 * Returns the name of the font.
	 *
	 * @return The font name
	 */
    public abstract String getName();

    /***************************************
	 * Returns the point size of the font.
	 *
	 * @return The size value
	 */
    public abstract int getSize();

    /***************************************
	 * Returns the font style.
	 *
	 * @return The style value
	 */
    public abstract int getStyle();

    /***************************************
	 * Compares this instance for equality with another object.
	 *
	 * @see Object#equals(Object)
	 */
    public boolean equals(Object rObject) {
        if (rObject == this) {
            return true;
        }
        if (rObject instanceof FontRenderer) {
            FontRenderer rOther = (FontRenderer) rObject;
            return getSize() == rOther.getSize() && getStyle() == rOther.getStyle() && getName().equals(rOther.getName());
        }
        return false;
    }

    /***************************************
	 * Returns a hash code that is consistent with the implementation of equals.
	 *
	 * @see Object#hashCode()
	 */
    public int hashCode() {
        return (getName().hashCode() * 37 + getStyle()) * 37 + getSize();
    }

    /***************************************
	 * Renders a text string in the given graphics context.
	 *
	 * @param rGraphics The graphics context to draw the text in
	 * @param sText     The text string to draw
	 * @param x         The horizontal position to draw at
	 * @param y         The vertical position to draw at
	 */
    protected abstract void drawString(Graphics rGraphics, String sText, int x, int y);

    /***************************************
	 * Renders a part of a text string in the given graphics context.
	 *
	 * @param rGraphics The graphics context to draw the text in
	 * @param sText     The text string to draw
	 * @param nOffset   The offset into the string of the text to draw
	 * @param nLength   The length of the text part to draw
	 * @param x         The horizontal position to draw at
	 * @param y         The vertical position to draw at
	 */
    protected abstract void drawSubstring(Graphics rGraphics, String sText, int nOffset, int nLength, int x, int y);

    /***************************************
	 * Must be implemented to return a string description of the renderers
	 * parameters.
	 *
	 * @return A string describing the renderer's parameters
	 */
    protected abstract String paramString();
}
