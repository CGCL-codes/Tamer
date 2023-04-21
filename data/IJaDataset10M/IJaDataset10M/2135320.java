package net.matmas.pneditor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import net.matmas.pneditor.PNEditor;

/**
 *
 * @author matmas
 */
public class QuitAction extends Action {

    public QuitAction() {
        String name = "Quit";
        putValue(NAME, name);
        putValue(SHORT_DESCRIPTION, name);
        putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl Q"));
    }

    public void actionPerformed(ActionEvent e) {
        PNEditor.getInstance().quitApplication();
    }
}
