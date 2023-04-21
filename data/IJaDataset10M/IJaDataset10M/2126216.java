package org.openscience.cdk.renderer.font;

/**
 * An interface for managing the drawing of fonts at different zoom levels.
 * 
 * @author maclean
 * @cdk.module render
 * @cdk.githash
 */
public interface IFontManager {

    /**
	 * Style of the font to use to draw text.
	 */
    public enum FontStyle {

        /** Regular font style. */
        NORMAL, /** Bold font style. */
        BOLD
    }

    /**
     * For a particular zoom level, set the appropriate font size to use.
     * 
     * @param zoom a real number in the range (0.0, INF)
     */
    public void setFontForZoom(double zoom);

    /**
     * Set the font style.
     * 
     * @param fontStyle an {@link FontStyle} type
     */
    public void setFontStyle(IFontManager.FontStyle fontStyle);

    /**
     * Set the font name ('Arial', 'Times New Roman') and so on. 
     * 
     * @param fontName name of the font to use
     */
    public void setFontName(String fontName);
}
