package org.carabiner.harness;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JPanel;

/**
 * A panel that holds dockable {@link org.carabiner.infopanel.InfoPanel InfoPanels}.
 *
 * @author Ben Rady (benrady@gmail.com)
 *
 */
public class InfoPanelContainer extends JPanel {

    protected InfoPanelContainer() {
        setLayout(new BorderLayout());
    }

    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
    }
}
