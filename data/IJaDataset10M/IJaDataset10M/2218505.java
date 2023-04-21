package seco.notebook.syntax;

import java.awt.Font;
import java.awt.Color;
import java.io.Serializable;

/**
 * A simple text style class. It can specify the color, italic flag,
 * and bold flag of a run of text.
 * @author Slava Pestov
 * @version $Id: SyntaxStyle.java,v 1.6 2006/09/01 16:18:27 bizi Exp $
 */
public class SyntaxStyle {

    private Color fgColor;

    private Color bgColor;

    private Font font;

    public SyntaxStyle() {
    }

    /**
	 * Creates a new SyntaxStyle.
	 * @param fgColor The text color
	 * @param bgColor The background color
	 * @param font The text font
	 */
    public SyntaxStyle(Color fgColor, Color bgColor, Font font) {
        this.fgColor = fgColor;
        this.bgColor = bgColor;
        this.font = font;
    }

    /**
	 * Returns the text color.
	 */
    public Color getForegroundColor() {
        return fgColor;
    }

    public void setForegroundColor(Color c) {
        fgColor = c;
    }

    /**
	 * Returns the background color.
	 */
    public Color getBackgroundColor() {
        return bgColor;
    }

    public void setBackgroundColor(Color c) {
        bgColor = c;
    }

    /**
	 * Returns the style font.
	 */
    public Font getFont() {
        return font;
    }

    public void setFont(Font f) {
        font = f;
    }
}
