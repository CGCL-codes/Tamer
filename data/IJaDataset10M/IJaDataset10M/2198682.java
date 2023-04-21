package edu.isi.div2.metadesk.util;

import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.plaf.TabbedPaneUI;
import edu.isi.div2.metadesk.util.PopupMenuMouseListener;

/**
 * @author Sameer Maggon (maggon@isi.edu)
 * @version $Id: TabPopupMenuMouseListener.java,v 1.1 2005/05/24 16:36:41 maggon Exp $
 */
public abstract class TabPopupMenuMouseListener extends PopupMenuMouseListener {

    public TabPopupMenuMouseListener(JTabbedPane tabbedPane) {
        super(tabbedPane);
    }

    public void setSelection(JComponent c, int x, int y) {
        JTabbedPane tabbedPane = (JTabbedPane) c;
        int tabcount = tabbedPane.getTabCount();
        for (int i = 0; i < tabcount; i++) {
            TabbedPaneUI tpu = tabbedPane.getUI();
            Rectangle rect = tpu.getTabBounds(tabbedPane, i);
            if (x < rect.x || x > rect.x + rect.width || y < rect.y || y > rect.y + rect.height) continue;
            tabbedPane.setSelectedIndex(i);
            break;
        }
    }
}
