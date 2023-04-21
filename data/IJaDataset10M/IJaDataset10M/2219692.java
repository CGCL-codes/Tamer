package net.sourceforge.chesstree.gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import net.sourceforge.chesstree.chainlogic.ChainManager;

/**
 * Listens and reacts to all the key events for the main window and it's dialogs
 */
public class CTKeyListener implements KeyListener {

    /**
	 * 
	 */
    public CTKeyListener() {
        super();
    }

    public void keyPressed(KeyEvent arg0) {
    }

    public void keyReleased(KeyEvent arg0) {
    }

    public void keyTyped(KeyEvent arg0) {
        switch(arg0.getKeyChar()) {
            case KeyEvent.VK_ESCAPE:
                CTWindowManager.getStatusBar().setMessage("");
                ChainManager.getChainManager().setBranchNextMove(false);
                break;
        }
    }
}
