package bias.gui;

import java.awt.Font;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.StyleSheet;

/**
 * @author kion
 */
public class CustomHTMLDocument extends HTMLDocument {

    private static final long serialVersionUID = 1L;

    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 12);

    public CustomHTMLDocument(StyleSheet ss) {
        super(ss);
        putProperty("IgnoreCharsetDirective", Boolean.TRUE);
        setPreservesUnknownTags(false);
    }

    @Override
    public Font getFont(AttributeSet attr) {
        Object family = attr.getAttribute(StyleConstants.FontFamily);
        Object size = attr.getAttribute(StyleConstants.FontSize);
        if (family == null && size == null) {
            int style = DEFAULT_FONT.getStyle();
            Object bold = attr.getAttribute(StyleConstants.Bold);
            if (bold != null && (Boolean) bold) {
                style = style | Font.BOLD;
            }
            Object italic = attr.getAttribute(StyleConstants.Italic);
            if (italic != null && (Boolean) italic) {
                style = style | Font.ITALIC;
            }
            return new Font(DEFAULT_FONT.getFamily(), style, DEFAULT_FONT.getSize());
        }
        return super.getFont(attr);
    }
}

;
