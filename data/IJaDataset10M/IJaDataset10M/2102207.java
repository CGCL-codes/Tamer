package jgnash.ui.register;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jgnash.engine.Engine;
import jgnash.engine.EngineFactory;
import jgnash.engine.Transaction;
import jgnash.ui.util.ValidationFactory;
import jgnash.util.Resource;

/**
 * Abstract transaction entry form class
 * 
 * @author Craig Cavanaugh
 * @version $Id: AbstractEntryFormPanel.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public abstract class AbstractEntryFormPanel extends JPanel {

    /**
     * Resource bundle
     */
    protected final Resource rb = Resource.get();

    /**
     * Validating key listener instance. Form components should install this instance for intelligent handling of the
     * enter key
     */
    protected final KeyListener keyListener = new FormKeyListener();

    /**
     * Helper method of displaying a validation error for the form, When the error is shown, further user input is
     * blocked and the user must click the form to continue and hide the message
     * 
     * @param error error message to display for the user
     * @param origin the component with the invalid data
     */
    protected final void showValidationError(final String error, final JComponent origin) {
        ValidationFactory.showValidationError(error, origin);
    }

    /**
     * This is a helper method to reduce code when adding labels to the layout
     * 
     * @param label label text
     * @param constraints layout constraints
     */
    protected void add(final String label, final Object constraints) {
        add(new JLabel(rb.getString(label)), constraints);
    }

    protected void focusFirstComponent() {
        if (getFocusTraversalPolicy() != null) {
            Component c = getFocusTraversalPolicy().getFirstComponent(this);
            if (c != null) {
                c.requestFocusInWindow();
            }
        }
    }

    protected void addRegisterListener(final RegisterListener l) {
        listenerList.add(RegisterListener.class, l);
    }

    protected void removeRegisterListener(final RegisterListener l) {
        listenerList.remove(RegisterListener.class, l);
    }

    /**
     * Notify all listeners of a cancel action
     */
    void fireCancelAction() {
        fireAction(RegisterEvent.Action.CANCEL);
    }

    /**
     * Notify all listeners of an OK action
     */
    void fireOkAction() {
        fireAction(RegisterEvent.Action.OK);
    }

    /**
     * Notify all listeners of an action
     * 
     * @param action action performed
     */
    private void fireAction(final RegisterEvent.Action action) {
        RegisterEvent e = null;
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == RegisterListener.class) {
                if (e == null) {
                    e = new RegisterEvent(this, action);
                }
                ((RegisterListener) listeners[i + 1]).registerEvent(e);
            }
        }
    }

    public abstract void clearForm();

    /**
     * Validates the form. This returns false by default to fo
     * 
     * @return True if the form is valid
     */
    protected abstract boolean validateForm();

    /**
     * This method is called to commit a transaction
     */
    public abstract void enterAction();

    /**
     * Modifies a transaction inside this form.<br>
     * The t must be assigned to <code>modTrans</code> if transaction modification is allowed
     * 
     * @param t The transaction to modify
     */
    public abstract void modifyTransaction(Transaction t);

    /**
     * An internal KeyListener that will call the enterAction method if the form is valid. If the form is not valid, the
     * next component is given the focus. If the escape key is pressed, the form is cleared
     */
    private class FormKeyListener extends KeyAdapter {

        @Override
        public void keyPressed(final KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (validateForm()) {
                    enterAction();
                } else {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                clearForm();
            }
        }
    }

    static Engine getEngine() {
        return EngineFactory.getEngine(EngineFactory.DEFAULT);
    }
}
