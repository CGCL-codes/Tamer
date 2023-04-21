package gui.generic;

import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * @author noamik
 *
 */
public class FixedJTable extends JTable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5420351985543138872L;

    public FixedJTable(GenericTable gt) {
        super(gt);
    }

    /**
     * Returns false to indicate that horizontal scrollbars are required
     * to display the table while honoring perferred column widths. Returns
     * true if the table can be displayed in viewport without horizontal
     * scrollbars.
     * 
     * @return true if an auto-resizing mode is enabled 
     *   and the viewport width is larger than the table's 
     *   preferred size, otherwise return false.
     * @see Scrollable#getScrollableTracksViewportWidth
     * Source of this fix: http://www.daniweb.com/software-development/java/threads/29263
     */
    public boolean getScrollableTracksViewportWidth() {
        if (autoResizeMode != AUTO_RESIZE_OFF) {
            if (getParent() instanceof JViewport) {
                return (((JViewport) getParent()).getWidth() > getPreferredSize().width);
            }
        }
        return false;
    }
}
