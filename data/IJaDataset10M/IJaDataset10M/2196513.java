package org.plugger.ui.base;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * A delegate class for an ActionCommand.
 * This class help delegate the actions of buttons to controllers.
 *
 * @author "Antonio Begue"
 * @version $Revision: 1.0 $
 */
public class DelegateAction extends AbstractAction {

    ActionListener listener;

    /**
     * A DelegateAction constructor
     * @param listener The ActionListener for delegate.
     */
    public DelegateAction(ActionListener listener) {
        this(null, listener);
    }

    /**
     * A DelegateAction constructor
     * @param title The component title string.
     * @param listener The ActionListener for delegate.
     */
    public DelegateAction(String title, ActionListener listener) {
        super(title);
        this.listener = listener;
    }

    /**
     * Set the title of the action component.
     * @param title The title string.
     * @return this.
     */
    public DelegateAction setTitle(String title) {
        super.putValue("Name", title);
        return this;
    }

    /**
     * A letter to execute the command as mnemonic.
     * @param letter A character letter.
     */
    public void setMnemonic(char letter) {
        putValue(Action.MNEMONIC_KEY, Integer.valueOf(letter));
    }

    public void actionPerformed(ActionEvent e) {
        this.listener.actionPerformed(e);
    }
}
