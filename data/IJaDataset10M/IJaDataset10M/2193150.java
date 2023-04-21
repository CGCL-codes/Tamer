package dnb.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import dnb.analyze.MatchResult;
import dnb.analyze.MatchResult.Status;
import dnb.analyze.nfo.NfoField;
import dnb.analyze.nfo.NfoLyzeResult;

public class MatchViewPane extends JTextPane {

    private static final String STYLE_SUPER = "Super";

    private static final String STYLE_NORMAL = "Normal";

    private EnumMap<MatchResult.Status, String> status2StyleMap = new EnumMap<Status, String>(MatchResult.Status.class);

    private MatchResult highlightedResult = null;

    /**
	 * Saved in {@link #init(dnb.analyze.nfo.NfoLyzer.NfoLyzeResult)} to do highlighting.
	 */
    private List<Entry<NfoField, MatchResult>> filteredMatches = null;

    private Map<NfoField, MatchResult> filteredMatchMap = null;

    private final Map<MatchResult.Status, Color> statusColors;

    private String styleKey(MatchResult.Status status, boolean highligthed) {
        return status.toString() + (highligthed ? "1" : "0");
    }

    private Style createStyle(MatchResult.Status status, boolean highligthed) {
        Style style = addStyle(styleKey(status, highligthed), null);
        StyleConstants.setForeground(style, statusColors.get(status));
        if (highligthed) {
            StyleConstants.setBackground(style, Color.blue);
        }
        StyleConstants.setUnderline(style, true);
        StyleConstants.setFontSize(style, 13);
        return style;
    }

    public MatchViewPane(Map<MatchResult.Status, Color> statusColors) {
        this.statusColors = statusColors;
        setFont(new Font("Courier", Font.BOLD, 12));
        for (MatchResult.Status s : MatchResult.Status.values()) {
            createStyle(s, true);
            createStyle(s, false);
        }
        Style style = addStyle(STYLE_SUPER, null);
        StyleConstants.setSuperscript(style, true);
        style = addStyle(STYLE_NORMAL, null);
        setEditable(false);
    }

    public void highlight(NfoField field) {
        StyledDocument doc = getStyledDocument();
        if (highlightedResult != null) {
            doc.setCharacterAttributes(highlightedResult.getStart(), highlightedResult.getEnd() - highlightedResult.getStart(), getStyle(styleKey(highlightedResult.getStatus(), false)), true);
            highlightedResult = null;
        }
        if (filteredMatchMap == null) {
            return;
        }
        MatchResult m = filteredMatchMap.get(field);
        if (m == null) {
            return;
        }
        highlightedResult = m;
        doc.setCharacterAttributes(m.getStart(), m.getEnd() - m.getStart(), getStyle(styleKey(highlightedResult.getStatus(), true)), true);
        setCaretPosition(m.getStart());
    }

    private void buildFilteredMap() {
        filteredMatchMap = new HashMap<NfoField, MatchResult>();
        for (Entry<NfoField, MatchResult> e : filteredMatches) {
            filteredMatchMap.put(e.getKey(), e.getValue());
        }
    }

    /**
     * Initializes this text pane with the matches from the specified source
     * within passed result object.
     * @param result
     * @param source the source to show
     */
    public void init(NfoLyzeResult result, MatchResult.Source source) {
        filteredMatches = result.getMatchResults(source);
        highlightedResult = null;
        StyledDocument doc = getStyledDocument();
        try {
            doc.remove(0, doc.getLength());
            switch(source) {
                case NFO:
                    doc.insertString(0, result.getContent(), getStyle(STYLE_NORMAL));
                    break;
                case PATH:
                    doc.insertString(0, result.getPath().pathString(), getStyle(STYLE_NORMAL));
            }
        } catch (BadLocationException e2) {
            e2.printStackTrace();
        }
        buildFilteredMap();
        Collections.sort(filteredMatches, new Comparator<Entry<NfoField, MatchResult>>() {

            @Override
            public int compare(Entry<NfoField, MatchResult> o1, Entry<NfoField, MatchResult> o2) {
                if (o1.getValue().getEnd() < o2.getValue().getEnd()) {
                    return -1;
                } else if (o1.getValue().getEnd() > o2.getValue().getEnd()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        MatchResult m;
        int offs = 0;
        for (Entry<NfoField, MatchResult> e : filteredMatches) {
            m = e.getValue();
            if (m.getMatch().isEmpty()) {
                continue;
            }
            doc.setCharacterAttributes(m.getStart() + offs, m.getEnd() - m.getStart(), getStyle(styleKey(m.getStatus(), false)), true);
            try {
                m.setStart(m.getStart() + offs);
                m.setEnd(m.getEnd() + offs);
                doc.insertString(m.getEnd(), Integer.toString(e.getKey().ordinal()), getStyle(STYLE_SUPER));
                offs++;
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                scrollToMatches();
            }
        });
    }

    private void scrollToMatches() {
        if (filteredMatches != null && !filteredMatches.isEmpty()) {
            try {
                Rectangle r0 = modelToView(filteredMatches.get(0).getValue().getStart());
                Rectangle rn = modelToView(filteredMatches.get(filteredMatches.size() - 1).getValue().getEnd());
                if (r0 != null && rn != null) {
                    r0.add(rn);
                    scrollRectToVisible(r0);
                } else {
                    System.out.println("Scroll to macthes failed.");
                }
            } catch (BadLocationException e1) {
                e1.printStackTrace();
            }
        }
    }
}
