package de.fu_berlin.inf.gmanda.gui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import de.fu_berlin.inf.gmanda.gui.docking.ViewManager;

public class FullscreenTextViewAction extends AbstractAction {

    ViewManager manager;

    public FullscreenTextViewAction(ViewManager manager) {
        super("Toggle fullscreen mode of current view");
        this.manager = manager;
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
        putValue(Action.MNEMONIC_KEY, new Integer(KeyEvent.VK_B));
    }

    public void actionPerformed(ActionEvent arg0) {
        manager.toggleFullscreen();
    }
}
