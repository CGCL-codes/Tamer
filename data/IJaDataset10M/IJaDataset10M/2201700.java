package fullGUI;

import java.awt.Dimension;
import java.io.IOException;
import java.net.URL;
import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * An extension of the <code>JEditorPane</code> class modified to display PSE's help texts. The
 * modifications are as follows:
 * <ol>
 * <li>Only one usable constructor - <code>HelpPane(URL initialPage)</code>. It both loads the URL and sets the inital params for the pane: non-editable and sized to a 300x400 rectangle.</li>
 * <li>It implements <code>HyperlinkListener</code> such that only anchor links within a page will be followed.</li>
 * </ol>
 * 
 * @author Peter Andrews
 * @version 1.5
 */
public class HelpPane extends JEditorPane implements HyperlinkListener {

    /**
     * Creates a <code>HelpPane</code> with the indicated URL as the displayed page. Note that only anchor
     * hyperlinks are usable.
     * 
     * @param initialPage The page to load.
     * @throws IOException
     */
    public HelpPane(URL initialPage) throws IOException {
        super(initialPage);
        setEditable(false);
        setMaximumSize(new Dimension(300, 400));
        setPreferredSize(new Dimension(300, 400));
        setMinimumSize(new Dimension(300, 400));
        addHyperlinkListener(this);
    }

    /**
     * Handles anchor hyperlinks (ones that point to a location on the current page).
     * 
     * @param HyperlinkEvent e The hyperlink event to handle, of course.
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            scrollToReference(e.getDescription().substring(1));
        }
    }
}
