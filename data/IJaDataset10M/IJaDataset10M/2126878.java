package jgloss.ui;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class MatchHighlighter implements Highlighter {

    private char[] match;

    private char[] text;

    /**
   * Paint the given text, highlighting the portion that appears in
   * the match string.
   *
   * @param g Graphics object to paint on
   * @param text text to paint  
   */
    public void paintHighlight(Graphics g, String entryText, String searchText) {
        if (text == null || text.length < entryText.length()) text = new char[entryText.length() * 2];
        if (match == null || match.length < searchText.length()) match = new char[searchText.length() * 2];
        entryText.getChars(0, entryText.length(), text, 0);
        searchText.getChars(0, searchText.length(), match, 0);
        FontMetrics fm = g.getFontMetrics();
        Color normal = Color.blue;
        Color highlight = Color.red;
        int max = Math.min(searchText.length(), entryText.length());
        int x = 0;
        int y = fm.getAscent();
        for (int begin = 0, end = begin; begin < max; begin = end) {
            while (begin < max && text[begin] != match[begin]) begin++;
            if (end < begin) {
                g.setColor(normal);
                g.drawString(entryText.substring(end, begin), x, y);
                x += fm.charsWidth(text, end, begin - end);
            }
            for (end = begin; end < max && text[end] == match[end]; end++) ;
            g.setColor(highlight);
            g.drawString(entryText.substring(begin, end), x, y);
            x += fm.charsWidth(text, begin, end - begin);
        }
        g.setColor(normal);
        g.drawString(entryText.substring(max), x, y);
    }
}
