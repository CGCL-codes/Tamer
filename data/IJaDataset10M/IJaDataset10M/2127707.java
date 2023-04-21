package jgnash.ui.register;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.prefs.Preferences;
import javax.swing.JLabel;
import javax.swing.JPanel;
import jgnash.engine.Account;
import jgnash.engine.ReconcileManager;
import jgnash.engine.Transaction;
import jgnash.ui.util.UIResource;

/** Abstract JPanel that implements common code used in all TransactionPanels.
 * This class does not perform any layout or container assignment.
 * <p>
 * Any extending class may assign the keyListener member to additional
 * components to improve focus traversal.
 * <p> 
 * $Id: AbstractTransactionPanel.java 1392 2008-12-04 14:06:36Z edelsont $
 *
 * @author Craig Cavanaugh
 * @author Tom Edelson
 */
public abstract class AbstractTransactionPanel extends JPanel {

    /** static UIResource of language support */
    public static UIResource rb = (UIResource) UIResource.get();

    private static final String NODE = "jgnash/ui/register";

    private static final String REMEMBER_DATE = "rememberDate";

    private static boolean rememberDate;

    /** 
         * Validating key listener instance.  Form components should install
	 * this instance for intelligent handling of the enter key.
         * 
         */
    protected final KeyListener keyListener = new FormKeyListener();

    static {
        Preferences p = Preferences.userRoot().node(NODE);
        rememberDate = p.getBoolean(REMEMBER_DATE, true);
    }

    public static void setRememberLastDate(boolean reset) {
        rememberDate = reset;
        Preferences p = Preferences.userRoot().node(NODE);
        p.putBoolean(REMEMBER_DATE, rememberDate);
    }

    /** Determines if the last date used for a transaction is reset 
	 * to the current date or remembered.
	 * @return true if the last date should be reused
	 */
    public static boolean getRememberLastDate() {
        return rememberDate;
    }

    /** Sets the reconciled state of the transaction using the rules set by the user
	 * 
	 * @param t transaction to set reconciled state
	 * @param reconciled new reconciled state
	 */
    public void reconcileTransaction(Transaction t, boolean reconciled) {
        ReconcileManager.reconcileTransaction(getAccount(), t, reconciled);
    }

    /** 
         * This is a helper method to reduce code 
         * when adding labels to the layout.
         * 
         * @param label       label text
         * @param constraints layout constraints
         * 
         */
    protected void add(String label, Object constraints) {
        add(new JLabel(rb.getString(label)), constraints);
    }

    /** A method to help create one row sub panels.  This helps to work around
	 * a layout limitation of components spaning multiple columns.
	 * If a String is passed as a component, it will be localized and
	 * converted to a JLabel.
	 * @param columnSpec The column spec for the layout
	 * @param components The components for the subpanel
	 * @return The resulting JPanel
	 */
    protected JPanel buildHorizontalSubPanel(String columnSpec, Object[] components) {
        FormLayout layout = new FormLayout(columnSpec, "f:d:g");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof String) {
                builder.append(new JLabel(rb.getString((String) components[i])));
            } else {
                builder.append((Component) components[i]);
            }
        }
        return builder.getPanel();
    }

    public abstract void clearForm();

    /** Creates a transaction using the contents of the form.
	 * @return The generated transaction
	 */
    protected abstract Transaction buildTransaction();

    /** Modifies a transaction inside this form.<br>
	 * The t must be assigned to <code>modTrans</code> if transaction
	 * modification is allowed
	 * @param t The transaction to modify
	 */
    public abstract void modifyTransaction(Transaction t);

    /**
	 * Validates the form.  This returns false by default to fo
	 * @return True if the form is valid
	 */
    public abstract boolean validateForm();

    /**
	 * This method is called to commit a transaction
	 */
    protected abstract void enterAction();

    protected abstract Account getAccount();

    public void focusFirstComponent() {
        if (getFocusTraversalPolicy() != null) {
            Component c = getFocusTraversalPolicy().getFirstComponent(this);
            if (c != null) {
                c.requestFocusInWindow();
            }
        }
    }

    /** An internal KeyListener that will call the enterAction method if the
	 * form is valid.  If the form is not valid, the next component is given
	 * the focus.  If the escape key is pressed, the form is cleared
	 */
    public class FormKeyListener extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (validateForm()) {
                    enterAction();
                } else {
                    KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                    kfm.focusNextComponent();
                }
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                clearForm();
            }
        }
    }
}
