package org.argus.gui;

import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 * @author Dan Peder Eriksen
 */
public class StatusBar extends JToolBar {

    private static final long serialVersionUID = 1L;

    public StatusBar() {
        super();
        this.add(new JLabel(" "));
    }
}
