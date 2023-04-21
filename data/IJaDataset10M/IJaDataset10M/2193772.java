package javapoint.spellcheck;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Kyle
 */
public class WordSearcher {

    public WordSearcher(JTextComponent comp) {
        this.comp = comp;
        this.painter = new UnderlineHighlighter.UnderlineHighlightPainter(Color.red);
    }

    public int search(String word) {
        words.add(word);
        int firstOffset = -1;
        Highlighter highlighter = comp.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof UnderlineHighlighter.UnderlineHighlightPainter) {
                highlighter.removeHighlight(h);
            }
        }
        if (word == null || word.equals("")) {
            return -1;
        }
        String content = null;
        try {
            Document d = comp.getDocument();
            content = d.getText(0, d.getLength());
        } catch (BadLocationException e) {
            return -1;
        }
        for (int i = 0; i < words.size(); i++) {
            int lastIndex = 0;
            int wordSize = words.get(i).length();
            while ((lastIndex = content.indexOf(words.get(i), lastIndex)) != -1) {
                int endIndex = lastIndex + wordSize;
                try {
                    highlighter.addHighlight(lastIndex, endIndex, painter);
                } catch (BadLocationException e) {
                }
                if (firstOffset == -1) {
                    firstOffset = lastIndex;
                }
                lastIndex = endIndex;
            }
        }
        return firstOffset;
    }

    public void removeWord(String word) {
        words.remove(words.indexOf(word));
        Highlighter highlighter = comp.getHighlighter();
        Highlighter.Highlight[] highlights = highlighter.getHighlights();
        for (int i = 0; i < highlights.length; i++) {
            Highlighter.Highlight h = highlights[i];
            if (h.getPainter() instanceof UnderlineHighlighter.UnderlineHighlightPainter) {
                highlighter.removeHighlight(h);
            }
        }
    }

    protected JTextComponent comp;

    protected Highlighter.HighlightPainter painter;

    ArrayList<String> words = new ArrayList<String>();
}
