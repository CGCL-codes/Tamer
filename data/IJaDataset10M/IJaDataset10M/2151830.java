package org.photron.gui;

import org.photron.PhotronApp;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

/**
 * @author jon
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BrowseToolBar extends JToolBar {

    protected PhotronApp photronApp;

    protected JComboBox locationBar;

    public BrowseToolBar(PhotronApp photronApp) {
        super("Browse toolbar");
        this.photronApp = photronApp;
        this.locationBar = new JComboBox();
        this.setFloatable(false);
        this.add(photronApp.getAction(PhotronApp.BROWSE_BACK_ACTION));
        this.add(photronApp.getAction(PhotronApp.BROWSE_UP_ACTION));
        this.add(photronApp.getAction(PhotronApp.BROWSE_FORWARD_ACTION));
        this.add(this.locationBar);
    }
}
