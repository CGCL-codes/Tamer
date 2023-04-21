package com.semp.gu.ui.elements;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class TopMenuBar extends JMenuBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public TopMenuBar() {
        super();
        JMenu menu1 = new JMenu("Codec");
        JMenuItem menuItem11 = new JMenuItem("Configuration Codec Image");
        menu1.add(menuItem11);
        JMenuItem menuItem12 = new JMenuItem("Configuration Codec Video");
        menu1.add(menuItem12);
        this.add(menu1);
        JMenu menu2 = new JMenu("Connecteurs");
        JMenuItem menuItem21 = new JMenuItem("Configuration Connecteur SSH");
        menu2.add(menuItem21);
        this.add(menu2);
    }
}
