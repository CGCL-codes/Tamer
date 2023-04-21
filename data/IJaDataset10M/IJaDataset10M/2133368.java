package org.zebrafish.feature;

import org.apache.commons.lang.StringUtils;
import org.zebrafish.field.Color;
import org.zebrafish.util.Separator;

/**
 * Title used in charts.
 * 
 * @author Max Chu
 * @author Joseph S. Kuo
 * @version $Revision: 29 $, $Date: 2008-09-11 05:35:27 -0400 (Thu, 11 Sep 2008) $
 * @since 0.1
 */
public class Title extends AbstractFeature {

    /** Default title color: 000000. */
    public static final String DEFAULT_TITLE_COLOR = "000000";

    /** Default title font size: 14. */
    public static final int DEFAULT_TITLE_FONT_SIZE = 14;

    private String text;

    private int fontSize;

    private Color color;

    /**
	 * Constructs a <code>Title</code> object with the specified text.
	 * 
	 * @param text the content of title
	 */
    public Title(String text) {
        setText(text);
    }

    /**
	 * Returns the content of this title.
	 * 
	 * @return the content of this title
	 */
    public String getText() {
        return text;
    }

    /**
	 * Sets the content of this title
	 * 
	 * @param text the content of this title
	 */
    public void setText(String text) {
        this.text = text;
    }

    /**
	 * Returns the font size of this title.
	 * 
	 * @return the font size of this title
	 */
    public int getFontSize() {
        return fontSize;
    }

    /**
	 * Sets the font size of this title. If the given font size is less then
	 * zero then it will be set to use default size
	 * {@link #DEFAULT_TITLE_FONT_SIZE}.
	 * 
	 * @param fontSize the font size of this title
	 */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    /**
	 * Returns the color of this title.
	 * 
	 * @return the color of this title
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Sets the color of this title.
	 *  
	 * @param color the color of this title
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    public final String toURLString() {
        if (text == null) {
            return StringUtils.EMPTY;
        }
        StringBuffer sb = createStringBuffer();
        sb.append(formatTitle());
        if (color != null || fontSize > 0) {
            sb.append("&chts=").append(color != null ? color.toURLString() : DEFAULT_TITLE_COLOR).append(Separator.COMMA).append(fontSize > 0 ? fontSize : DEFAULT_TITLE_FONT_SIZE);
        }
        return sb.toString();
    }

    private final String formatTitle() {
        return text.replaceAll("\n", "|").replaceAll("\\\\n", "|").replaceAll("\\s+", "+");
    }

    public String getParameterName() {
        return "chtt";
    }
}
