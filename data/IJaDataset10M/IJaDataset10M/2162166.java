package org.ms150hams.trackem.ui.commongui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.ms150hams.trackem.ui.actions.*;

public class MenuExitItem extends JMenuItem {

    private static final long serialVersionUID = 1L;

    public MenuExitItem() {
        super("Exit");
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        addActionListener(new ExitActionListener());
    }
}
