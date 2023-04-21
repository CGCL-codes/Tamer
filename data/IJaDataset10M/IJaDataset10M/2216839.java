package org.speakmon.coffeehouse.swingui;

import org.speakmon.coffeehouse.CoffeehousePrefs;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.util.LinkedList;
import java.util.logging.Logger;
import java.util.regex.*;
import javax.swing.text.*;

/**
 *
 * Created: Thu Jan  2 12:32:22 2003
 *
 * @author <a href="mailto:ben@speakmon.org">Ben Speakmon</a>
 */
class ConversationDocument extends DefaultStyledDocument {

    /**
     * Regular text style, no decoration whatsoever.
     */
    public static final String REGULAR = "regular";

    public static final String ALIGN_RIGHT = "align_right";

    private static final int PLAIN = Font.PLAIN;

    private static final int BOLD = Font.BOLD;

    private static final int ITALIC = Font.ITALIC;

    private static final int BOLD_ITALIC = Font.BOLD | Font.ITALIC;

    private Style defaultStyle;

    private int bgcolor = 1;

    private static final Pattern colorPattern = Pattern.compile("(\\d{1,2}(,\\d{1,2})?)?");

    private static final CoffeehousePrefs prefs = CoffeehousePrefs.getInstance();

    private static Color[] colors = new Color[] { Color.WHITE, Color.BLACK, Color.BLUE, new Color(50, 205, 50), Color.RED, new Color(128, 0, 0), new Color(128, 0, 128), Color.ORANGE, Color.YELLOW, Color.GREEN, new Color(0, 128, 128), Color.CYAN, new Color(65, 105, 225), Color.PINK, Color.DARK_GRAY, Color.LIGHT_GRAY };

    private Logger channelLog;

    private final StyleContext styleContext = StyleContext.getDefaultStyleContext();

    private final TimestampProvider timestamp;

    protected ConversationDocument(Logger channelLog) {
        super();
        this.timestamp = new TimestampProvider();
        this.channelLog = channelLog;
        defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = styleContext.addStyle(REGULAR, defaultStyle);
        MutableAttributeSet alignRight = new SimpleAttributeSet(defaultStyle);
        StyleConstants.setAlignment(alignRight, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setFontFamily(defaultStyle, prefs.font());
        StyleConstants.setFontSize(defaultStyle, prefs.fontSize());
        switch(prefs.fontStyle()) {
            case PLAIN:
                break;
            case BOLD:
                StyleConstants.setBold(defaultStyle, true);
                break;
            case ITALIC:
                StyleConstants.setItalic(defaultStyle, true);
                break;
            case BOLD_ITALIC:
                StyleConstants.setBold(defaultStyle, true);
                StyleConstants.setItalic(defaultStyle, true);
                break;
        }
    }

    /**
     * Describe <code>insertString</code> method here.
     *
     * @param offset an <code>int</code> value
     * @param str a <code>String</code> value
     * @param a an <code>AttributeSet</code> value
     * @exception BadLocationException if an error occurs
     */
    public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
        Matcher matcher;
        StringBuffer buf = new StringBuffer();
        String cleanStr = str.replaceAll(colorPattern.pattern(), "");
        Style insertStyle;
        channelLog.info(cleanStr);
        if (prefs.stripMircColors()) {
            str = cleanStr;
        }
        if (prefs.showTimestamps()) {
            str = str.replaceAll("%t", timestamp.getTimestamp());
        } else {
            str = str.replaceAll("%t", "");
        }
        matcher = colorPattern.matcher(str);
        if (matcher.find()) {
            if (matcher.start() > 0) {
                buf.append(str.substring(0, matcher.start()));
                super.insertString(offset, buf.toString(), defaultStyle);
                offset += buf.length();
                buf.setLength(0);
            }
            matcher.reset();
            boolean last = false;
            int next = 0;
            while (matcher.find()) {
                next = str.indexOf("", matcher.end());
                if (next == -1) {
                    last = true;
                }
                AttributeSet textStyle = parseColors(matcher.group(1));
                if (last) {
                    buf.append(str.substring(matcher.end()));
                } else {
                    buf.append(str.substring(matcher.end(), next));
                }
                super.insertString(offset, buf.toString(), textStyle);
                offset += buf.length();
                buf.setLength(0);
            }
        } else {
            super.insertString(offset, str, defaultStyle);
        }
        bgcolor = 1;
    }

    private AttributeSet parseColors(String colorCode) {
        Style fgStyle;
        MutableAttributeSet textStyle;
        if (colorCode == null) {
            bgcolor = 1;
            return defaultStyle;
        }
        String[] parts = colorCode.split(",");
        if (parts.length == 2) {
            int fgcolor = Integer.parseInt(parts[0]);
            bgcolor = Integer.parseInt(parts[1]);
            if (fgcolor > 15) {
                fgcolor %= 16;
            }
            if (bgcolor > 15) {
                bgcolor %= 16;
            }
            textStyle = new SimpleAttributeSet(defaultStyle);
            StyleConstants.setForeground(textStyle, colors[fgcolor]);
            StyleConstants.setBackground(textStyle, colors[bgcolor]);
            return textStyle;
        } else if (parts.length == 1) {
            int fgcolor = Integer.parseInt(parts[0]);
            if (fgcolor > 15) {
                fgcolor %= 16;
            }
            textStyle = new SimpleAttributeSet(defaultStyle);
            StyleConstants.setForeground(textStyle, colors[fgcolor]);
            StyleConstants.setBackground(textStyle, colors[bgcolor]);
            return textStyle;
        } else {
            return defaultStyle;
        }
    }
}
