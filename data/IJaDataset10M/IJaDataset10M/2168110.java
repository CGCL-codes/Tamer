package net.sf.euphony.ui.menus;

import java.awt.Frame;
import javax.swing.JMenu;
import net.sf.euphony.ui.controls.AskOnExitAction;
import net.sf.euphony.ui.controls.ToolTipAction;

public class PreferencesMenu extends JMenu {

    Frame parent;

    public PreferencesMenu(Frame parent) {
        super("Preferences");
        this.parent = parent;
        add(new ToolTipAction());
        add(new AskOnExitAction());
    }
}
