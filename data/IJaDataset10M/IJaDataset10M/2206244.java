package uk.co.ordnancesurvey.rabbitgui.editorpane.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.text.StyledDocument;
import uk.ac.leeds.comp.ui.bridge.JTextPaneAsUIComponentImpl;
import uk.ac.leeds.comp.ui.highlighter.SquigglyHighlighter;

public class SwingRbtEditorPaneViewImpl extends JTextPaneAsUIComponentImpl implements SwingRbtEditorPaneView {

    private static final long serialVersionUID = 54906353605273319L;

    private static final int BORDER_SIZE = 3;

    private final StyledDocument origDoc;

    public SwingRbtEditorPaneViewImpl() {
        setName("RbtEditorPaneView");
        final Font font = new Font("Monospaced", Font.PLAIN, getFont().getSize());
        setHighlighter(new SquigglyHighlighter());
        changeFont(font);
        origDoc = getStyledDocument();
    }

    /**
	 * Change the component's font, and change the size of the component to
	 * match.
	 */
    public final void changeFont(Font font) {
        setFont(font);
        final FontMetrics metrics = getFontMetrics(font);
        final int border = 2 * BORDER_SIZE;
        final int charWidth = metrics.charWidth('m');
        final int charHeight = metrics.getHeight();
        int paneWidth = MIN_COLS * charWidth + border;
        int paneHeight = MIN_ROWS * charHeight + border;
        final Dimension minSize = new Dimension(paneWidth, paneHeight);
        setMinimumSize(minSize);
        paneWidth = PREF_COLS * charWidth + border;
        paneHeight = PREF_ROWS * charHeight + border;
        final Dimension prefSize = new Dimension(paneWidth, paneHeight);
        setPreferredSize(prefSize);
        invalidate();
    }

    /**
	 * This method sets the {@link #getMinimumSize()} of this component to a
	 * value that allows all the lines in the document to be seen.
	 * 
	 * @see uk.co.ordnancesurvey.rabbitgui.editorpane.RbtEditorPaneView#resizeToFitStringToParse()
	 */
    public final void resizeToFitStringToParse() {
        String nl = System.getProperty("line.separator");
        String[] lines = getText().split(nl);
        int numRows = lines.length;
        int fittingHeight = numRows * getFontMetrics(getFont()).getHeight() + 2 * BORDER_SIZE;
        Dimension oldMin = getMinimumSize();
        Dimension newMin = new Dimension(oldMin.width, fittingHeight);
        setMinimumSize(newMin);
    }

    /**
	 * @see javax.swing.JTextPane#setStyledDocument(javax.swing.text.StyledDocument)
	 */
    @Override
    public void setStyledDocument(StyledDocument doc) {
        if (doc != null) {
            super.setStyledDocument(doc);
        } else {
            super.setStyledDocument(origDoc);
        }
    }

    public final void setPresentedData(StyledDocument aDocument) {
        setStyledDocument(aDocument);
    }

    public void setLongDescription(String aTooltipText) {
        throw new RuntimeException("not implemented");
    }

    public final StyledDocument getPresentedData() {
        return getStyledDocument();
    }

    public final void setAsPresentedData(Object aCandidate) throws IllegalArgumentException {
        if (!(aCandidate instanceof StyledDocument)) throw new IllegalArgumentException(" " + aCandidate);
        setPresentedData((StyledDocument) aCandidate);
    }
}
