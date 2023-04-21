package net.sf.uibuilder.samples.simple;

import java.awt.Point;
import javax.swing.JTable;
import javax.swing.JEditorPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SimpleFrameMouseAdapter - is the mouse adapter for the simple frame.
 *
 * @version   1.0 2005-3-23
 * @author    <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public class SimpleFrameMouseAdapter extends MouseAdapter {

    private static Log _log = LogFactory.getLog(SimpleFrameMouseAdapter.class);

    /**
     * Constructor function
     */
    public SimpleFrameMouseAdapter() {
    }

    /**
     * the release.
     */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            SimpleFramePopupMenu popupMenu = (SimpleFramePopupMenu) SimpleFrame.getPopupMenu4Table();
            _log.info("Popup triggered!");
            Object source = e.getSource();
            _log.debug("The source is: " + source.toString());
            if (source instanceof JTable) {
                JTable table = (JTable) source;
                int rowIndex = table.rowAtPoint(new Point(e.getX(), e.getY()));
                _log.debug("The selected row is: " + rowIndex);
                if (rowIndex > -1) {
                    table.changeSelection(rowIndex, 0, false, false);
                    _log.debug("The selected value is: " + table.getValueAt(rowIndex, 2).toString());
                }
                popupMenu.setContext(table);
            } else if (source instanceof JEditorPane) {
                popupMenu.setContext(null);
            }
            popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
