package org.openscience.jmol;

import org.openscience.jmol.render.AtomShape;
import java.awt.Color;
import java.awt.Font;
import java.util.BitSet;

public class LabelManager {

    DisplayControl control;

    public LabelManager(DisplayControl control) {
        this.control = control;
    }

    public byte styleLabel = DisplayControl.NOLABELS;

    public void setStyleLabel(byte styleLabel) {
        this.styleLabel = styleLabel;
    }

    public String strFontFace = "Helvetica";

    public void setFontFace(String strFontFace) {
        this.strFontFace = strFontFace;
    }

    public static final int pointsMin = 6;

    public static final int pointsMax = 32;

    Font[] fonts = new Font[pointsMax - pointsMin + 1];

    public Font getFontOfSize(int points) {
        if (points < pointsMin) points = pointsMin; else if (points > pointsMax) points = pointsMax;
        int index = points - pointsMin;
        Font font = fonts[index];
        if (font == null) font = fonts[index] = new Font(strFontFace, Font.PLAIN, points);
        return font;
    }

    public Font getLabelFont(int diameter) {
        int points = diameter * 2 / 3;
        if (pointsLabelFontSize != 0) points = pointsLabelFontSize;
        return getFontOfSize(points);
    }

    public int pointsLabelFontSize = 0;

    public void setLabelFontSize(int points) {
        this.pointsLabelFontSize = points;
    }

    public String getLabelAtom(byte styleLabel, Atom atom) {
        String label = null;
        switch(styleLabel) {
            case DisplayControl.SYMBOLS:
                label = atom.getSymbol();
                break;
            case DisplayControl.TYPES:
                label = atom.getAtomTypeName();
                break;
            case DisplayControl.NUMBERS:
                label = "" + (atom.getAtomNumber() + 1);
                break;
        }
        return label;
    }

    public String getLabelAtom(String strFormat, Atom atom) {
        if (strFormat == null || strFormat.equals("")) return null;
        ProteinProp pprop = atom.getProteinProp();
        String strLabel = "";
        String strExpansion = "";
        int ich = 0;
        int cch = strFormat.length();
        char ch;
        int ichPercent;
        boolean percentFound = false;
        while ((ichPercent = strFormat.indexOf('%', ich)) != -1) {
            strFormat += strFormat.substring(ich, ichPercent);
            ich = ichPercent + 1;
            if (ich == cch) {
                --ich;
                break;
            }
            strExpansion = "";
            ch = strFormat.charAt(ich++);
            switch(ch) {
                case 'i':
                    strExpansion = "" + atom.getAtomNumber() + 1;
                    break;
                case 'a':
                case 'e':
                    strExpansion = atom.getSymbol();
                    break;
                case 'b':
                case 't':
                    if (pprop != null) strExpansion = "" + pprop.getTemperature();
                    break;
                case 'c':
                case 's':
                    if (pprop != null) strExpansion = "" + pprop.getChain();
                    break;
                case 'm':
                    strExpansion = "<X>";
                    break;
                case 'n':
                    if (pprop != null) strExpansion = "" + pprop.getResidue();
                    break;
                case 'r':
                    if (pprop != null) strExpansion = "" + pprop.getResno();
                    break;
                default:
                    strExpansion = "" + ch;
            }
            strLabel += strExpansion;
        }
        strLabel += strFormat.substring(ich);
        return strLabel;
    }
}
