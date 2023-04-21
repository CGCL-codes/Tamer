package de.iph.arbeitsgruppenassistent.client.taskmanagement.utils;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JViewport;

/**
 * 
 * @author Andreas Bruns
 */
public class ResourceTable extends JTable {

    private static final long serialVersionUID = 1L;

    /**
	 * Makes it possible to set the height relativ to the
	 * viewport height.
	 */
    public boolean getScrollableTracksViewportHeight() {
        setAutoscrolls(true);
        Component parent = getParent();
        if (parent instanceof JViewport) {
            return parent.getHeight() > getPreferredSize().height;
        }
        return false;
    }
}
