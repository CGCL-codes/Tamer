package org.xmlhammer.gui.actions;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.bounce.RunnableAction;
import org.xmlhammer.gui.XMLHammer;

/**
 * An action that can be used to cut content.
 *
 * @version	$Revision: 1.8 $, $Date: 2007/07/04 19:42:49 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class CutAction extends RunnableAction {

    private static final long serialVersionUID = 939638802470121637L;

    private XMLHammer parent = null;

    /**
	 * The constructor for the action which allows cutting content.
	 *
	 * @param parent the XML Hammer
	 */
    public CutAction(XMLHammer parent) {
        super("Cut");
        this.parent = parent;
        putValue(MNEMONIC_KEY, new Integer('t'));
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
        putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/org/xmlhammer/gui/icons/etool16/cut_edit.gif")));
        putValue(SHORT_DESCRIPTION, "Cut");
        setEnabled(false);
    }

    /**
	 * The implementation of the cut action, called 
	 * after a user action.
	 */
    public void run() {
        JComponent component = parent.getLastSelectedComponent();
        if (component instanceof JTextComponent) {
            ((JTextComponent) component).cut();
        }
    }
}
