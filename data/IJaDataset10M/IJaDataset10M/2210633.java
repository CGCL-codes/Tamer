package org.leviatan.dataharbour.gui.swingcomponents;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class VirtualBindingsTab extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
	 * This is the default constructor
	 */
    public VirtualBindingsTab() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(300, 200);
        this.setLayout(new GridBagLayout());
    }
}
