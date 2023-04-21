package org.projectnotes.gui.panels;

import javax.swing.JLabel;

public final class PanelProjectView extends PanelBase {

    public PanelProjectView() {
        add(new JLabel(getClass().getCanonicalName()));
    }
}
