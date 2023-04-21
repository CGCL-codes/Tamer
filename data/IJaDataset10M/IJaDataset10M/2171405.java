package net.sf.cybowmodeller.swing;

import javax.swing.Icon;
import javax.swing.KeyStroke;

/**
 *
 * @author SHIMAYOSHI Takao
 * @version $Revision: 30 $
 */
public abstract class AbstractAction extends javax.swing.AbstractAction {

    public AbstractAction() {
        super();
    }

    public AbstractAction(String name, Icon icon) {
        super(name, icon);
    }

    public AbstractAction(String name) {
        super(name);
    }

    public void setMnemonic(int mnemonic) {
        putValue(MNEMONIC_KEY, mnemonic);
    }

    public void setAccelerator(KeyStroke keyStroke) {
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    public static int getMenuShortcutKeyMask() {
        return LAFUtilities.getMenuShortcutKeyMask();
    }

    public KeyStroke getMenuKeyStroke(int keyCode) {
        return KeyStroke.getKeyStroke(keyCode, getMenuShortcutKeyMask());
    }
}
