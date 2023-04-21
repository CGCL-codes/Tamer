package jmemorize.gui.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import jmemorize.core.Main;
import jmemorize.gui.Localization;
import jmemorize.gui.swing.CardCategoryTransferHandler;

/** 
 * An action that invokes the generic Swing CUT action on the current focus
 * owner.
 * 
 * @author djemili
 */
public class CutAction extends AbstractAction {

    public CutAction() {
        putValue(NAME, Localization.getString("MainFrame.CUT"));
        putValue(MNEMONIC_KEY, new Integer(2));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/resource/icons/edit_cut.gif")));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_MASK));
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        CardCategoryTransferHandler.getCutAction().actionPerformed(new ActionEvent(Main.getInstance().getFrame().getEditFocusOwner(), ActionEvent.ACTION_PERFORMED, "cut"));
    }
}
