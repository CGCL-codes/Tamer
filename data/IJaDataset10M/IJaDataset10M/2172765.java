package de.offis.semanticmm4u.compositors.projectors;

import java.awt.Color;
import java.awt.Font;
import component_interfaces.semanticmm4u.realization.compositor.provided.IStyle;
import component_interfaces.semanticmm4u.realization.compositor.provided.ITypographicProjector;
import component_interfaces.semanticmm4u.realization.compositor.realization.IElement;
import de.offis.semanticmm4u.global.Constants;
import de.offis.semanticmm4u.global.Utilities;

public class TypographicProjector extends Projector implements ITypographicProjector {

    private Font font = null;

    private int size = Constants.UNDEFINED_INTEGER;

    private Style style = null;

    private Color backgroundColor = null;

    private Color foregroundColor = null;

    public TypographicProjector() {
        this.createNewTypographicProjector(Font.getFont("Arial"), 10, new Style(IStyle.ITALIC), Color.white, Color.black);
    }

    public TypographicProjector(Font myFont, int mySize, Style myStyle, Color myBackgroundColor, Color myForegroundColor) {
        this.createNewTypographicProjector(myFont, mySize, myStyle, myBackgroundColor, myForegroundColor);
    }

    private void createNewTypographicProjector(Font myFont, int mySize, Style myStyle, Color myBackgroundColor, Color myForegroundColor) {
        this.font = myFont;
        this.size = mySize;
        this.style = myStyle;
        this.backgroundColor = myBackgroundColor;
        this.foregroundColor = myForegroundColor;
    }

    public void setFont(Font myFont) {
        this.font = myFont;
    }

    public Font getFont() {
        return this.font;
    }

    public void setSize(int mySize) {
        this.size = mySize;
    }

    public int getSize() {
        return this.size;
    }

    public void setStyle(Style myStyle) {
        this.style = myStyle;
    }

    public Style getStyle() {
        return this.style;
    }

    public void setBackgroundColor(Color myBackgroundColor) {
        this.backgroundColor = myBackgroundColor;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setForegroundColor(Color myForegroundColor) {
        this.foregroundColor = myForegroundColor;
    }

    public Color getForegroundColor() {
        return this.foregroundColor;
    }

    public String getID() {
        return "typographicprojector_" + Utilities.getHexHashCode(this);
    }

    /**
     * Clone the object recursive.

     * @return a copy of the Object.
     * @see de.offis.semanticmm4u.compositors.AbstractElement#recursiveClone()
     */
    public IElement recursiveClone() {
        Color tempBackgroundColor = new Color(this.backgroundColor.getRed(), this.backgroundColor.getGreen(), this.backgroundColor.getBlue(), this.backgroundColor.getAlpha());
        Color tempForegroundColor = new Color(this.foregroundColor.getRed(), this.foregroundColor.getGreen(), this.foregroundColor.getBlue(), this.foregroundColor.getAlpha());
        Font tempFont = new Font(this.font.getFontName(), this.font.getStyle(), this.font.getSize());
        TypographicProjector object = new TypographicProjector(tempFont, this.size, this.style.recursiveClone(), tempBackgroundColor, tempForegroundColor);
        return object;
    }
}
