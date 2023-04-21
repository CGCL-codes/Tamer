package mipt.gui.controls;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.AbstractButton;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;
import mipt.gui.GUIResource;
import mipt.gui.IconFactory;

public abstract class EasyAction implements Action, ButtonActionOwner {

    private ButtonAction action;

    public static String rolloverIconSuffix = "Roll";

    public static ResourceBundle bundle;

    public static IconFactory icons;

    protected static GUIResource resource;

    protected static class EasyResource implements GUIResource {

        public Icon getIcon(String iconName) {
            return icons.getIcon(iconName);
        }

        public String getString(String stringKey) {
            return bundle.getString(stringKey);
        }
    }

    protected static GUIResource getResource() {
        if (resource == null) resource = new EasyResource();
        return resource;
    }

    /**
 *
 * @param textKey java.lang.String
 */
    public EasyAction(String textKey) {
        this(textKey, -1, false, false);
    }

    /**
 * 
 * @param textKey java.lang.String
 * @param mnemonicIndex int
 */
    public EasyAction(String textKey, int mnemonicIndex) {
        this(textKey, mnemonicIndex, false, false);
    }

    /**
 *
 * @param textKey java.lang.String
 * @param mnemonicIndex int
 * @param hasIcon boolean
 */
    public EasyAction(String textKey, int mnemonicIndex, boolean hasIcon) {
        this(textKey, mnemonicIndex, hasIcon, false);
    }

    /**
 *
 * @param textKey java.lang.String : not only defines text from bundle and icons from factory but is equal to action command
 * @param mnemonicIndex int
 * @param hasIcon boolean
 * @param hasRolloverIcon boolean
 */
    public EasyAction(String textKey, int mnemonicIndex, boolean hasIcon, boolean hasRolloverIcon) {
        this(getResource(), textKey, mnemonicIndex, hasIcon, false);
    }

    /**
 *
 * @param textKey java.lang.String
 */
    public EasyAction(GUIResource r, String textKey) {
        this(r, textKey, -1, false, false);
    }

    /**
 * 
 * @param textKey java.lang.String
 * @param mnemonicIndex int
 */
    public EasyAction(GUIResource r, String textKey, int mnemonicIndex) {
        this(r, textKey, mnemonicIndex, false, false);
    }

    /**
 *
 * @param textKey java.lang.String
 * @param mnemonicIndex int
 * @param hasIcon boolean
 */
    public EasyAction(GUIResource r, String textKey, int mnemonicIndex, boolean hasIcon) {
        this(r, textKey, mnemonicIndex, hasIcon, false);
    }

    /**
 *
 * @param textKey java.lang.String : not only defines text from bundle and icons from factory but is equal to action command
 * @param mnemonicIndex int
 * @param hasIcon boolean
 * @param hasRolloverIcon boolean
 */
    public EasyAction(GUIResource r, String textKey, int mnemonicIndex, boolean hasIcon, boolean hasRolloverIcon) {
        setAction(createAction(r.getString(textKey), mnemonicIndex, hasIcon ? r.getIcon(textKey) : null, hasRolloverIcon ? r.getIcon(textKey + rolloverIconSuffix) : null), r, textKey);
    }

    /**
 * See super.updateControl()
 * Note: control.getName() will be equal to control.getActionCommand()
 * @param control javax.swing.AbstractButton
 */
    public void updateControl(AbstractButton control) {
        action.updateControl(control);
        Object value = getValue(ACTION_COMMAND_KEY);
        if (value != null) control.setName((String) value);
    }

    /**
	 * @see javax.swing.Action#getValue(java.lang.String)
	 */
    public final Object getValue(String key) {
        return action.getValue(key);
    }

    /**
	 * @see javax.swing.Action#putValue(java.lang.String, java.lang.Object)
	 */
    public final void putValue(String key, Object value) {
        action.putValue(key, value);
    }

    /**
	 * @see javax.swing.Action#setEnabled(boolean)
	 */
    public final void setEnabled(boolean b) {
        action.setEnabled(b);
    }

    /**
	 * @see javax.swing.Action#isEnabled()
	 */
    public final boolean isEnabled() {
        return action.isEnabled();
    }

    /**
	 * @see javax.swing.Action#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
    public final void addPropertyChangeListener(PropertyChangeListener listener) {
        action.addPropertyChangeListener(listener);
    }

    /**
	 * @see javax.swing.Action#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
    public final void removePropertyChangeListener(PropertyChangeListener listener) {
        action.removePropertyChangeListener(listener);
    }

    /**
	 * @return
	 */
    public final ButtonAction getAction() {
        return action;
    }

    /**
	 * @param action
	 */
    protected void setAction(ButtonAction action, GUIResource r, String textKey) {
        action.setActionCommand(textKey);
        action.setShortDescription(r.getString(textKey));
        this.action = action;
    }

    /**
	 * Override to instantiate another action class
	 * Don't forget to call this.actionPerformed in it
	 *  or implement this.actionPerformed to call getAction().actionPerformed!
	 */
    public ButtonAction createAction(String text, int mnemonicIndex, Icon icon, Icon rolloverIcon) {
        class EasyButtonAction extends ButtonAction {

            public EasyButtonAction() {
                super();
            }

            public EasyButtonAction(String text, int mnemonicIndex, Icon icon, Icon rolloverIcon) {
                super(text, mnemonicIndex, icon, rolloverIcon);
            }

            public final void actionPerformed(ActionEvent e) {
                EasyAction.this.actionPerformed(e);
            }

            protected ButtonAction createCopy() {
                return new EasyButtonAction();
            }
        }
        return new EasyButtonAction(text, mnemonicIndex, icon, rolloverIcon);
    }
}
