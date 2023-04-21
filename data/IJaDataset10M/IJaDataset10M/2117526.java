package net.sf.jmms.plugins.swingui;

import net.sf.jmms.*;

public class SwingGuiPlugin implements Plugin {

    private SwingGui gui;

    public void init(jmms jmms) {
        gui = new SwingGui(jmms);
        gui.pack();
        gui.show();
    }

    public void deinit() {
        gui.close();
    }
}
