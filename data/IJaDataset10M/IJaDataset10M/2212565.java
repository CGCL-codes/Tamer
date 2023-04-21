package net.sf.homebank.menus.items;

import javax.swing.JMenuItem;
import net.sf.homebank.listeners.action.HelpAboutListener;

public class HelpAbout extends JMenuItem {

    public HelpAbout() {
        super("About");
        setToolTipText("About the Application");
        addActionListener(new HelpAboutListener());
    }
}
