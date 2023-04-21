package com.elibera.m.xml;

import com.elibera.m.utils.HelperStd;
import com.elibera.m.xml.display.TextElement;
import com.elibera.m.xml.proc.ProcTextElement;

/**
 * Die Hilfsklasse zum Text-Parsen
 * @author meisi
 *
 */
public class XMLTextParser {

    public static int BORDER_LINES = 2;

    private static int[][] NO_LINE_BREAKS = { { 48, 57 }, { 65, 90 }, { 97, 122 }, { 192, 255 }, { 913, 982 } };

    private static char[] CHARS_DIE_AM_ZEILENENDE_KLEBEN = { ',', ' ', '.', ':', ';', '_', '-', ')', ']', '}', '!', '?', '"', '荤', '$', '~', '+', '\'', '=', '&', '%', '|', '>' };

    public static ProcTextElement procText;

    private static TextParseStruct tp;

    /**
	 * parst den Text, zu einem TextElement
	 */
    public static TextElement parseText(String _text, ParsingStruct p) {
        char[] text = _text.toCharArray();
        return parseText(text, p, -1);
    }

    public static TextElement parseText(char[] text, ParsingStruct p) {
        return parseText(text, p, -1);
    }

    public static TextElement parseText(char[] text, ParsingStruct p, int curLineWidth) {
        TextParseStruct tp = getLines(text, p, curLineWidth);
        return procText.getTextElement(tp.lineEckpunkte, tp.lineBreaks, text, p.valign, p);
    }

    /**
	 * parst den Text, so dass er auf dem Canvas platz hat
	 */
    public static TextParseStruct getLines(char[] text, ParsingStruct p, int curLineWidth) {
        TextParseStruct tp = null;
        if (p.parsingThread != null && HelperXMLParser.curParsingThread != null && !HelperXMLParser.curParsingThread.equals(p.parsingThread)) {
            tp = new TextParseStruct();
        }
        if (tp == null) {
            if (XMLTextParser.tp == null) XMLTextParser.tp = new TextParseStruct(); else {
                tp = XMLTextParser.tp;
                tp.curLineWidth = 0;
                tp.curTextPos = 0;
                tp.firstLine = true;
                tp.lastLBPos = 0;
                tp.lineBreaks = new int[0];
                tp.lineEckpunkte = new int[0][];
                tp.text = null;
            }
            tp = XMLTextParser.tp;
        }
        if (curLineWidth < 0) tp.curLineWidth = p.getCurLineWidth(p.curElementTopLeftY); else tp.curLineWidth = curLineWidth;
        p.curLineHeight = p.setAndGetLineHeightInTheCurrentLine(p.curElementTopLeftY, p.getCurFontHeight());
        tp.text = text;
        int[] eck1 = { p.curElementTopLeftX, p.curElementTopLeftY, 0, 0 };
        tp.lineEckpunkte = HelperStd.incArray(tp.lineEckpunkte, eck1);
        int maxWidth = p.canvasWidth;
        while (tp.curTextPos < text.length) {
            tp.curLineWidth = tp.curLineWidth + p.curFont.charWidth(text[tp.curTextPos]);
            if (tp.curLineWidth >= maxWidth) {
                int newLB = findLineBreak(tp, p);
                if (newLB == -1) {
                    p.checkAlignForElements(0);
                    p.insertLineBreakElement(p.getCurFontHeight());
                    tp.curTextPos = -1;
                    tp.curLineWidth = 0;
                    tp.lineEckpunkte[tp.lineEckpunkte.length - 1][0] = p.curElementTopLeftX;
                    tp.lineEckpunkte[tp.lineEckpunkte.length - 1][1] = p.curElementTopLeftY;
                    tp.firstLine = false;
                } else {
                    tp.curLineWidth = 0;
                    for (int i = tp.lastLBPos; i < newLB; i++) tp.curLineWidth += p.curFont.charWidth(tp.text[i]);
                    int lineWidthOfOtherElements = 0;
                    if (tp.firstLine && p.curElementTopLeftX > p.internalNewStartXForNewLine && p.align > 0) {
                        lineWidthOfOtherElements = p.getCurLineWidth(p.curElementTopLeftY);
                    }
                    tp.lastLBPos = newLB;
                    tp.curTextPos = newLB - 1;
                    tp.lineBreaks = HelperStd.incArray(tp.lineBreaks, newLB);
                    int addX = 0;
                    if (p.align > 0) {
                        if (p.align == 2) addX = p.canvasWidth - lineWidthOfOtherElements - tp.curLineWidth;
                        if (p.align == 1) addX = (p.canvasWidth - lineWidthOfOtherElements - tp.curLineWidth) / 2;
                        tp.lineEckpunkte[tp.lineEckpunkte.length - 1][0] += addX;
                    }
                    tp.lineEckpunkte[tp.lineEckpunkte.length - 1][2] = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][0] + tp.curLineWidth;
                    tp.lineEckpunkte[tp.lineEckpunkte.length - 1][3] = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][1] + p.curLineHeight;
                    int[] eck = { p.internalNewStartXForNewLine, tp.lineEckpunkte[tp.lineEckpunkte.length - 1][3] + BORDER_LINES, 0, 0 };
                    if (tp.firstLine && p.curElementTopLeftX > 0 && p.align > 0) {
                        p.checkAlignForElements(p.canvasWidth - tp.lineEckpunkte[tp.lineEckpunkte.length - 1][0]);
                    }
                    p.doInternalLineBreak(p.getCurFontHeight());
                    tp.lineEckpunkte = HelperStd.incArray(tp.lineEckpunkte, eck);
                    tp.curLineWidth = 0;
                    tp.firstLine = false;
                }
            }
            tp.curTextPos++;
        }
        if (tp.curTextPos > tp.lastLBPos || (tp.curTextPos == 0 && tp.lastLBPos == 0)) {
            if (tp.lastLBPos == 0) {
                tp.curLineWidth = 0;
                for (int i = tp.lastLBPos; i < tp.curTextPos; i++) tp.curLineWidth += p.curFont.charWidth(tp.text[i]);
            }
            tp.lastLBPos = tp.curTextPos;
            tp.lineBreaks = HelperStd.incArray(tp.lineBreaks, tp.curTextPos);
            tp.lineEckpunkte[tp.lineEckpunkte.length - 1][2] = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][0] + tp.curLineWidth;
            tp.lineEckpunkte[tp.lineEckpunkte.length - 1][3] = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][1] + p.curLineHeight;
            int[] eck = { p.internalNewStartXForNewLine, tp.lineEckpunkte[tp.lineEckpunkte.length - 1][3] + BORDER_LINES, 0, 0 };
            tp.lineEckpunkte = HelperStd.incArray(tp.lineEckpunkte, eck);
            p.doInternalLineBreak(p.curLineHeight - ParsingStruct.LINE_BORDER);
            tp.curLineWidth = 0;
        }
        tp.lineEckpunkte = HelperStd.decArray(tp.lineEckpunkte);
        p.curElementTopLeftX = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][2] + HelperXMLParser.ELEMENT_SPACER;
        p.curElementTopLeftY = tp.lineEckpunkte[tp.lineEckpunkte.length - 1][1];
        return tp;
    }

    /**
	 * sucht nach einer passenden Position für einen Line Break
	 * gibt es kein umbruchbares Zeichen, so wird entweder einfach am ene umgebrochen, oder
	 * es soll (falls es die erste zeile ist), davor ein Zeilenumbruch gemacht. (-1 als rückgabe) und die breite neu berechnet werden
	 */
    private static int findLineBreak(TextParseStruct tp, ParsingStruct p) {
        for (int i = tp.curTextPos - 1; i > tp.lastLBPos; i--) {
            int c = (int) tp.text[i];
            boolean legalChar = false;
            for (int n = 0; n < NO_LINE_BREAKS.length; n++) {
                if (c >= NO_LINE_BREAKS[n][0] && c <= NO_LINE_BREAKS[n][1]) {
                    legalChar = true;
                    break;
                }
            }
            if (!legalChar) return checkLineBreakChar(tp, i);
        }
        if (tp.firstLine && tp.lastLBPos == 0) {
            if (p.curElementTopLeftX > p.internalNewStartXForNewLine) {
                return -1;
            }
        }
        return tp.curTextPos;
    }

    /**
	 * bestimmte Chars sollen nicht in die nächste Zeile verschwinden, sondern kleben bleiben
	 * hier kann sich die Linebreak Position zum 1 nach hinten verschieben
	 */
    private static int checkLineBreakChar(TextParseStruct tp, int lineBreak) {
        if (lineBreak + 1 >= tp.text.length) return lineBreak;
        char c = tp.text[lineBreak];
        if (c == ' ') return lineBreak + 1;
        if (lineBreak + 2 < tp.text.length && tp.text[lineBreak + 1] == ' ') return lineBreak + 2;
        if (lineBreak > 0 && tp.text[lineBreak - 1] == ' ') return lineBreak;
        for (int i = 0; i < CHARS_DIE_AM_ZEILENENDE_KLEBEN.length; i++) {
            if (c == CHARS_DIE_AM_ZEILENENDE_KLEBEN[i]) return (lineBreak + 1);
        }
        return lineBreak;
    }

    public static class TextParseStruct {

        public char[] text;

        public int curTextPos = 0;

        public int lastLBPos = 0;

        public int curLineWidth = 0;

        public int[] lineBreaks = new int[0];

        public int[][] lineEckpunkte = new int[0][];

        public boolean firstLine = true;
    }
}
