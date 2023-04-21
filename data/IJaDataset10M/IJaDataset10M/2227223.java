package net.sourceforge.obexftpfrontend.gui.action;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import net.sourceforge.obexftpfrontend.gui.AboutDialog;

/**
 * Action that is responsible to create and show the 'about' dialog.
 * @author Daniel F. Martins
 */
public class ShowAboutDialogAction extends DefaultAction {

    /**
     * Create a new instance of ShowAboutDialogAction.
     * @param parent Parent frame.
     */
    public ShowAboutDialogAction(Window parent) {
        super(new AboutDialog(parent));
        putValue(NAME, "About");
        putValue(SHORT_DESCRIPTION, "About this product");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                getParentWindow().setVisible(true);
            }
        });
    }
}
