package jmax.ui.mw;

import jmax.ui.*;
import javax.swing.*;

class MWMenuProvider implements MenuProvider {

    private WindowManager windowManager;

    private MWUIContext uiContext;

    private JMenu toolsMenu = null;

    private JMenu windowsMenu = null;

    private JMenu helpMenu = null;

    MWMenuProvider(WindowManager windowManager, MWUIContext uiContext) {
        this.uiContext = uiContext;
        this.windowManager = windowManager;
    }

    public JMenu getMenuByName(String name) {
        if (name.equals("Windows")) {
            if (windowsMenu == null) windowsMenu = new WindowMenu("Windows", windowManager);
            return windowsMenu;
        } else if (name.equals("Tools")) {
            if (toolsMenu == null) toolsMenu = new ToolsMenu("Tools", uiContext);
            return toolsMenu;
        } else if (name.equals("Help")) {
            if (helpMenu == null) helpMenu = new jmax.editors.patcher.menus.HelpMenu(uiContext);
            return helpMenu;
        } else return null;
    }

    /** Define the additional specific menu sections for this  style.
      No sections are defined, for now.
  */
    public JMenu[] getMenuSection(String name) {
        return null;
    }
}
