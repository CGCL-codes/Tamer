package freeguide.plugins.program.freeguide.wizard;

import freeguide.common.lib.fgspecific.Application;
import javax.swing.JOptionPane;

/**
 * A JPanel to go on a WizardFrame. It is subclassed by TextWizardPanel,
 * FileWizardPanel, DirectoryWizardPanel, CommandsWizardPanel, or
 * ChoiceWizardPanel. The above classes may be used to link to a config entry
 * by calling the setConfig method. If this is done, the onExit and onEnter
 * methods are useful as they save and load the value respectively. You can
 * also provide a custom onEnter and/or onExit method to be executed using
 * the setOnEnter and setOnExist methods. These will be executed on enter and
 * exit.
 *
 * @author Andy Balaam
 * @version 2
 */
public class WizardPanel extends javax.swing.JPanel {

    /** The config group if there is to be a guess */
    protected String topMessage;

    protected int topMnemonic;

    /** The config entry if there is to be a guess */
    protected String bottomMessage;

    /** Description of the Field */
    protected String configGroup;

    /** Description of the Field */
    protected String configEntry;

    /** Description of the Field */
    protected OnEnter onEnter;

    protected OnExit onExit;

    /**
     * Constructor for the WizardPanel superclass. This panel can be linked to
     * a config entry, or it can have an onExit Method, or both.
     */
    public WizardPanel() {
    }

    /**
     * Set up the messages that will appear above and below the box on
     * the panel.
     *
     * @param topMessage The new messages value
     * @param bottomMessage The new messages value
     */
    public void setMessages(String topMessage, String bottomMessage) {
        this.topMessage = topMessage;
        this.bottomMessage = bottomMessage;
    }

    /**
     * DOCUMENT_ME!
     *
     * @param topMessage DOCUMENT_ME!
     * @param bottomMessage DOCUMENT_ME!
     * @param topMnemonic DOCUMENT_ME!
     */
    public void setMessages(String topMessage, String bottomMessage, int topMnemonic) {
        this.topMessage = topMessage;
        this.bottomMessage = bottomMessage;
        this.topMnemonic = topMnemonic;
    }

    /**
     * Sets up a method to be executed when we exit this panel. It
     * must take an single argument: the object that is being got from the
     * user by this panel e.g. a File object if this panel asks for a
     * directory. Its return value will be ignored if there is one.
     *
     * @param onExit The new onExit value
     */
    public void setOnExit(OnExit onExit) {
        this.onExit = onExit;
    }

    /**
     * Sets up a method to be executed when we enter this panel. It
     * must take no args and return value to be put in this panel's box.
     *
     * @param onEnter The new onEnter value
     */
    public void setOnEnter(OnEnter onEnter) {
        this.onEnter = onEnter;
    }

    /**
     * Construct the GUI of this Wizard Panel.
     */
    public void construct() {
    }

    /**
     * Prepare as we enter this panel.
     */
    public void onEnter() {
        if (onEnter != null) {
            onEnter.onEnter(this);
        }
    }

    /**
     * Clear up as we leave this panel. Return false if it's not ok to
     * leave, otherwise leave.
     *
     * @return Description of the Return Value
     */
    public boolean onExit() {
        if (configEntry != null) {
            String error = checkValue();
            if (error != null) {
                String lb = System.getProperty("line.separator");
                int ignore = JOptionPane.showConfirmDialog(this, error + lb + Application.getInstance().getLocalizedMessage("do_you_want_to_continue"), Application.getInstance().getLocalizedMessage("error"), JOptionPane.YES_NO_OPTION);
                if (ignore == JOptionPane.NO_OPTION) {
                    return false;
                }
            }
        }
        if (onExit != null) {
            onExit.onExit(this);
        }
        return true;
    }

    /**
     * Returns an error if this value is faulty, or null it's ok
     *
     * @return The error message if something went wrong, or null.
     */
    private String checkValue() {
        return null;
    }

    /**
     * Gets the value that's in this panel's box. The box just means
     * the textfield or whatever that the user is typing into or choosing
     * items in.
     *
     * @return The boxValue value
     */
    protected Object getBoxValue() {
        return null;
    }

    /**
     * Sets the value that's in this panel's box. The box just means
     * the textfield or whatever that the user is typing into or choosing
     * items in.
     *
     * @param val The new boxValue value
     */
    protected void setBoxValue(Object val) {
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision$
     */
    public interface OnEnter {

        void onEnter(WizardPanel panel);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision$
     */
    public interface OnExit {

        void onExit(WizardPanel panel);
    }
}
