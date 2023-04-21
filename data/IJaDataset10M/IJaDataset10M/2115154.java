package com.rwoar.moo.client.connection;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

public class ColorPane extends JTextPane {

    private static final long serialVersionUID = 2753212113409812637L;

    private final String escapeChar = "";

    private String remaining = "";

    private boolean currentBold = false;

    private boolean currentBlink = false;

    static final Color D_Black = new Color(0x000000);

    static final Color D_Red = new Color(0xaa0000);

    static final Color D_Blue = new Color(0x0000aa);

    static final Color D_Magenta = new Color(0xaa00aa);

    static final Color D_Green = new Color(0x00aa00);

    static final Color D_Yellow = new Color(0xaa5500);

    static final Color D_Cyan = new Color(0x00aaaa);

    static final Color D_White = new Color(0xcccccc);

    static final Color B_Black = Color.getHSBColor(new Float(0.000), new Float(0.000), new Float(0.502));

    static final Color B_Red = new Color(0xff5555);

    static final Color B_Blue = new Color(0x5555ff);

    static final Color B_Magenta = new Color(0xff55ff);

    static final Color B_Green = new Color(0x88ff88);

    static final Color B_Yellow = new Color(0xffff55);

    static final Color B_Cyan = new Color(0x55ffff);

    static final Color B_White = new Color(0xffffff);

    static Color cReset = new Color(187, 187, 187);

    static Color colorCurrent = cReset;

    public ColorPane(Color resetColor) {
        super();
        cReset = resetColor;
    }

    public ColorPane(StyledDocument doc) {
        super(doc);
    }

    public void appendColor(Color c, String s) {
        StyledDocument doc = getStyledDocument();
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);
        if (this.currentBlink) {
        }
        try {
            doc.insertString(doc.getLength(), s, aset);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (toolkit.getLockingKeyState(KeyEvent.VK_SCROLL_LOCK) == false) setCaretPosition(getDocument().getLength());
    }

    public void appendANSI(String s) {
        int aPos = 0;
        int aIndex = 0;
        int mIndex = 0;
        boolean stillSearching = true;
        String tmpString = "";
        String addString = remaining + s;
        remaining = "";
        if (addString.length() > 0) {
            aIndex = addString.indexOf(escapeChar);
            if (aIndex == -1) {
                appendColor(colorCurrent, addString);
                return;
            }
            if (aIndex > 0) {
                tmpString = addString.substring(0, aIndex);
                appendColor(colorCurrent, tmpString);
                aPos = aIndex;
            }
            stillSearching = true;
            while (stillSearching) {
                mIndex = addString.indexOf("m", aPos);
                if (mIndex < 0) {
                    remaining = addString.substring(aPos, addString.length());
                    stillSearching = false;
                    continue;
                } else {
                    tmpString = addString.substring(aPos, mIndex + 1);
                    colorCurrent = getANSIColor(tmpString);
                }
                aPos = mIndex + 1;
                aIndex = addString.indexOf(escapeChar, aPos);
                if (aIndex == -1) {
                    tmpString = addString.substring(aPos, addString.length());
                    appendColor(colorCurrent, tmpString);
                    stillSearching = false;
                    continue;
                }
                tmpString = addString.substring(aPos, aIndex);
                aPos = aIndex;
                appendColor(colorCurrent, tmpString);
            }
        }
    }

    public Color getANSIColor(String ANSIColor) {
        if (ANSIColor.equals(escapeChar + "[30m")) return D_Black; else if (ANSIColor.equals(escapeChar + "[31m")) {
            if (this.currentBold) return B_Red; else return D_Red;
        } else if (ANSIColor.equals(escapeChar + "[32m")) {
            if (this.currentBold) return B_Green; else return D_Green;
        } else if (ANSIColor.equals(escapeChar + "[33m")) {
            if (this.currentBold) return B_Yellow; else return D_Yellow;
        } else if (ANSIColor.equals(escapeChar + "[34m")) {
            if (this.currentBold) return B_Blue; else return D_Blue;
        } else if (ANSIColor.equals(escapeChar + "[35m")) {
            if (this.currentBold) return B_Magenta; else return D_Magenta;
        } else if (ANSIColor.equals(escapeChar + "[36m")) {
            if (this.currentBold) return B_Cyan; else return D_Cyan;
        } else if (ANSIColor.equals(escapeChar + "[37m")) {
            if (this.currentBold) return B_White; else return D_White;
        } else if (ANSIColor.equals(escapeChar + "[1m")) {
            this.currentBold = true;
            return toBoldColor(colorCurrent);
        } else if (ANSIColor.equals(escapeChar + "[5m")) {
            this.currentBlink = true;
            return colorCurrent;
        } else if (ANSIColor.equals(escapeChar + "[0m")) {
            this.currentBold = false;
            this.currentBlink = false;
            return cReset;
        } else return colorCurrent;
    }

    /**
	 * Will return the color bolded
	 * @param in 
	 * @return
	 */
    private Color toBoldColor(Color in) {
        if (in.equals(D_Black)) return B_Black; else if (in.equals(D_Red)) return B_Red; else if (in.equals(D_Blue)) return B_Blue; else if (in.equals(D_Magenta)) return B_Magenta; else if (in.equals(D_Green)) return B_Green; else if (in.equals(D_Yellow)) return B_Yellow; else if (in.equals(D_Cyan)) return B_Cyan; else if (in.equals(D_White)) return B_White; else return in;
    }
}
