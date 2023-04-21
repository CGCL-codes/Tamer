package org.openscience.jmol.util;

import java.util.Vector;
import java.util.StringTokenizer;
import java.lang.Integer;
import java.util.*;
import java.awt.*;

public class FormatedText {

    public static final int FONT_SYMBOL = 0;

    public static final int FONT_NORMAL = 1;

    public static final int POS_NORMAL = 10;

    public static final int POS_EXP = 11;

    String text;

    int fontSize;

    int width;

    Vector textDef = new Vector(0);

    public FormatedText(String text, int fontSize) {
        this.text = text;
        this.fontSize = fontSize;
        parse();
        computeWidth();
    }

    /**
   * This methode parse the String passed in argument and
   * return a Vector containing easily usable formating information
   *
   * spaces are used as token separator
   * \N : switch to NORMAL font
   * \S : switch to SYMBOL font
   * ^  : put in exponent 
   * \  : (backslash space) put a space character
   *
   * For instance:
   *
   * parse("Wavelength \ \S l \ \N ( cm^-1 )", 20) will return
   *
   * Vector(0) = FONT_NORMAL     (int)
   * Vector(1) = POS_NORMAL
   * Vector(2) = "Wavelength"    (String)
   * Vector(3) = " "
   * Vector(4) = FONT_SYMBOL
   * Vector(5) = "l"             (this will give a lambda)
   * Vector(6) = " "
   * Vector(7) = FONT_NORMAL
   * Vector(8) = "(cm"
   * Vector(9) = POS_EXP
   * Vector(10) = "-1"
   * Vector(11)= POS_NORMAL
   * Vector(12)= ")"
   * Vector(13)= 234             (String width)
   * Vector(14)= 22              (String height)
   */
    private Vector parse() {
        String s;
        StringTokenizer st;
        int length = 0;
        int height = 0;
        textDef.addElement(new Integer(FONT_NORMAL));
        textDef.addElement(new Integer(POS_NORMAL));
        st = new StringTokenizer(text, "\\^ ", true);
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            if (s.equals("\\")) {
                s = st.nextToken();
                if (s.equals("S")) {
                    textDef.addElement(new Integer(FONT_SYMBOL));
                } else if (s.equals("N")) {
                    textDef.addElement(new Integer(FONT_NORMAL));
                } else if (s.equals(" ")) {
                    textDef.addElement(" ");
                }
            } else if (s.equals("^")) {
                textDef.addElement(new Integer(POS_EXP));
            } else if (s.equals(" ")) {
            } else {
                textDef.addElement(s);
                textDef.addElement(new Integer(POS_NORMAL));
            }
        }
        return textDef;
    }

    private void computeWidth() {
        Frame dummyFrame = new Frame();
        Object token;
        Font f = new Font("Times-Roman", Font.PLAIN, fontSize);
        width = 0;
        for (Enumeration e = textDef.elements(); e.hasMoreElements(); ) {
            token = e.nextElement();
            if (token instanceof Integer) {
                switch(((Integer) token).intValue()) {
                    case FONT_NORMAL:
                        f = new Font("Times-Roman", Font.PLAIN, fontSize);
                        break;
                    case FONT_SYMBOL:
                        f = new Font("Symbol", Font.PLAIN, fontSize);
                        break;
                }
            } else if (token instanceof String) {
                FontMetrics fm = dummyFrame.getFontMetrics(f);
                width = width + fm.stringWidth((String) token);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public Vector getTextDef() {
        return textDef;
    }

    public int getFontSize() {
        return fontSize;
    }
}
