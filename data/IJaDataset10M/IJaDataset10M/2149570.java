package org.tn5250j.keyboard.actions;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import org.tn5250j.TN5250jConstants;
import org.tn5250j.SessionGUI;
import org.tn5250j.keyboard.KeyMapper;

/**
 * Paste from the clipboard
 */
public class PasteAction extends EmulatorAction {

    private static final long serialVersionUID = 1L;

    public PasteAction(SessionGUI session, KeyMapper keyMap) {
        super(session, TN5250jConstants.MNEMONIC_PASTE, KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.ALT_MASK), keyMap);
    }

    public void actionPerformed(ActionEvent e) {
        session.getScreen().pasteMe(false);
    }
}
