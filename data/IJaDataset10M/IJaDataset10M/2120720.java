package net.jetrix.spectator.ui;

import static java.awt.Color.*;
import net.jetrix.protocols.TetrinetProtocol;
import net.jetrix.Protocol;
import javax.swing.text.*;
import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

/**
 * @author Emmanuel Bourg
 * @version $Revision: 751 $, $Date: 2008-08-28 04:20:33 -0400 (Thu, 28 Aug 2008) $
 */
public class TetrinetColor {

    public static final Color LIGHT_GREEN = new Color(128, 128, 255);

    public static final Color DARK_BLUE = new Color(0, 0, 128);

    public static final Color AQUA = new Color(0, 128, 128);

    public static final Color KAKI = new Color(128, 128, 0);

    public static final Color BROWN = new Color(128, 0, 0);

    public static final Color PURPLE = new Color(128, 0, 128);

    public static final Color GREEN = new Color(0, 128, 0);

    private static void updateStyle(Style style, char code) {
        switch(code) {
            case '':
                StyleConstants.setForeground(style, RED);
                break;
            case '':
                StyleConstants.setForeground(style, BLACK);
                break;
            case '':
                StyleConstants.setForeground(style, GREEN);
                break;
            case '':
                StyleConstants.setForeground(style, LIGHT_GREEN);
                break;
            case '':
                StyleConstants.setForeground(style, DARK_BLUE);
                break;
            case '':
                StyleConstants.setForeground(style, Color.BLUE);
                break;
            case '':
                StyleConstants.setForeground(style, CYAN);
                break;
            case '':
                StyleConstants.setForeground(style, AQUA);
                break;
            case '':
                StyleConstants.setForeground(style, YELLOW);
                break;
            case '':
                StyleConstants.setForeground(style, KAKI);
                break;
            case '':
                StyleConstants.setForeground(style, BROWN);
                break;
            case '':
                StyleConstants.setForeground(style, LIGHT_GRAY);
                break;
            case '':
                StyleConstants.setForeground(style, GRAY);
                break;
            case '':
                StyleConstants.setForeground(style, MAGENTA);
                break;
            case '':
                StyleConstants.setForeground(style, PURPLE);
                break;
            case '':
                StyleConstants.setBold(style, true);
                break;
            case '':
                StyleConstants.setItalic(style, true);
                break;
            case '':
                StyleConstants.setUnderline(style, true);
                break;
            case '':
                StyleConstants.setForeground(style, WHITE);
                break;
        }
    }

    public static void append(StyledDocument document, String text) {
        Protocol protocol = new TetrinetProtocol();
        text = protocol.applyStyle(text);
        List<Character> styles = new ArrayList<Character>();
        StringBuffer chunk = new StringBuffer();
        try {
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                if (isStyleCode(c)) {
                    Style style = getComputedStyle(styles);
                    document.insertString(document.getLength(), chunk.toString(), style);
                    chunk = new StringBuffer();
                    if (!styles.remove(new Character(c))) {
                        styles.add(c);
                    }
                } else {
                    chunk.append(c);
                }
            }
            if (chunk.length() > 0) {
                Style style = getComputedStyle(styles);
                document.insertString(document.getLength(), chunk.toString(), style);
            }
            document.insertString(document.getLength(), "\n", null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static boolean isStyleCode(char c) {
        return c < ' ';
    }

    private static Style getComputedStyle(List<Character> styles) {
        Style style = getDefaultStyle();
        if (!styles.isEmpty()) {
            for (Character code : styles) {
                TetrinetColor.updateStyle(style, code);
            }
        }
        return style;
    }

    public static Style getDefaultStyle() {
        Style style = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setBold(style, false);
        StyleConstants.setUnderline(style, false);
        StyleConstants.setItalic(style, false);
        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setFontSize(style, 11);
        return style;
    }
}
