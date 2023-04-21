package mipt.gui.controls;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.KeyStroke;
import javax.swing.Icon;

public abstract class ButtonAction extends AbstractAction implements ButtonActionOwner {

    public static final String ROLLOVER_SMALL_ICON = "RolloverSmallIcon";

    /**
	 *
	 */
    public ButtonAction() {
        super();
    }

    /**
	 *
	 * @param text java.lang.String
	 */
    public ButtonAction(String text) {
        super(text);
    }

    /**
	 * 
	 * @param text java.lang.String
	 * @param mnemonicIndex int
	 */
    public ButtonAction(String text, int mnemonicIndex) {
        super(text);
        setMnemonicIndex(mnemonicIndex);
    }

    /**
	 * 
	 * @param text java.lang.String
	 * @param mnemonicIndex int
	 * @param icon javax.swing.Icon
	 */
    public ButtonAction(String text, int mnemonicIndex, Icon icon) {
        super(text, icon);
        setMnemonicIndex(mnemonicIndex);
    }

    /**
	 * Use ordinary AbstractAction if you need only two first arguments or less
	 * @param text java.lang.String
	 * @param mnemonicIndex int
	 * @param icon javax.swing.Icon
	 * @param rolloverIcon javax.swing.Icon
	 * @see setAccelerator(), setActionCommand(), setShortDesription()
	 */
    public ButtonAction(String text, int mnemonicIndex, Icon icon, Icon rolloverIcon) {
        this(text, mnemonicIndex, icon);
        if (rolloverIcon != null) setRolloverIcon(rolloverIcon);
    }

    /**
	 * Returns the index`th char in the given text
	 * @return char
	 * @param text java.lang.String
	 * @param mnemonicIndex int
	 */
    public static char getMnemonic(String text, int mnemonicIndex) {
        if ((text == null) || (mnemonicIndex < 0) || (mnemonicIndex >= text.length())) return (char) Character.UNASSIGNED;
        return text.charAt(mnemonicIndex);
    }

    /**
	 * Returns the index of the (mnemonic) char in the given text
	 * @return int
	 * @param text java.lang.String
	 * @param mnemonic int
	 */
    public static int getMnemonicIndex(String text, int mnemonic) {
        if ((text == null) || (mnemonic == Character.UNASSIGNED)) return -1;
        return text.indexOf((char) mnemonic);
    }

    /**
	 * If action can be performed, imitates action event with this as source
	 */
    public void performAction() {
        if (!isEnabled()) return;
        actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, (String) getValue(ACTION_COMMAND_KEY)));
    }

    /**
	 * Sets default (with CTRL_MASK) accelerator value for menu items
	 * @param keyCode int : one of KeyEvent.VK_* constants
	 */
    public void setAccelerator(int keyCode) {
        setAccelerator(keyCode, java.awt.Event.CTRL_MASK);
    }

    /**
	 * Sets accelerator value for menu items
	 * @param keyCode int : one of KeyEvent.VK_* constants
	 * @param modifiers int : combination of Event.*_MASK constants or 0
	 */
    public void setAccelerator(int keyCode, int modifiers) {
        setAccelerator(KeyStroke.getKeyStroke(keyCode, modifiers));
    }

    public void setAccelerator(KeyStroke keyStroke) {
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    public final KeyStroke getAccelerator() {
        return (KeyStroke) getValue(ACCELERATOR_KEY);
    }

    /**
	 * Sets action command value
	 * Note : it will alse be component name (not text!) only in EasyButtonAction
	 * @param command java.lang.String
	 */
    public void setActionCommand(String command) {
        putValue(ACTION_COMMAND_KEY, command);
    }

    public final String getActionCommand() {
        return (String) getValue(ACTION_COMMAND_KEY);
    }

    /**
	 * Sets mnemonic char value
	 * @param mnemonic char : cann`t be (char)Character.UNASSIGNED
	 */
    public void setMnemonic(char mnemonic) {
        putValue(MNEMONIC_KEY, new Integer(mnemonic));
    }

    public final int getMnemonic() {
        Object mnemonic = getValue(MNEMONIC_KEY);
        if (mnemonic == null) return Character.UNASSIGNED;
        return ((Integer) mnemonic).intValue();
    }

    /**
	 * Sets mnemonic char as the index`th char of this`s name (component text)
	 * @param mnemonicIndex int
	 */
    public void setMnemonicIndex(int mnemonicIndex) {
        String name = getName();
        if (name == null) return;
        setMnemonic(getMnemonic(name, mnemonicIndex));
    }

    public final int getMnemonicIndex() {
        return getMnemonicIndex(getName(), getMnemonic());
    }

    /**
	 * Sets text value
	 * @param description java.lang.String
	 */
    public void setName(String name) {
        putValue(NAME, name);
    }

    public final String getName() {
        return (String) getValue(NAME);
    }

    /**
	 * Sets rollover icon value
	 * @param rolloverIcon javax.swing.Icon
	 */
    public void setIcon(Icon icon) {
        putValue(SMALL_ICON, icon);
    }

    public final Icon getIcon() {
        return (Icon) getValue(SMALL_ICON);
    }

    /**
	 * Sets rollover icon value
	 * @param rolloverIcon javax.swing.Icon
	 */
    public void setRolloverIcon(Icon rolloverIcon) {
        putValue(ROLLOVER_SMALL_ICON, rolloverIcon);
    }

    public final Icon getRolloverIcon() {
        return (Icon) getValue(ROLLOVER_SMALL_ICON);
    }

    /**
	 * Sets tool tip text value
	 * @param description java.lang.String
	 */
    public void setShortDescription(String description) {
        putValue(SHORT_DESCRIPTION, description);
    }

    public final String getShortDescription() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    /**
	 * Updates properties of the given control according to ROLLOVER_SMALL_ICON
	 *   and ACTION_COMMAND values of this object
	 * NAME(text), SMALL_ICON(icon), MNEMONIC_VALUE and SHORT_DESCRIPTION
	 *   (toolTipText) values is updated by AbstactButton in setAction(this),
	 *   ACCELERATOR - in JMenuItem
	 * Note : calling this not needed if contol is from mipt.gui.controls.*
	 * @param control javax.swing.AbstractButton
	 */
    public void updateControl(AbstractButton control) {
        Object value;
        value = getValue(ROLLOVER_SMALL_ICON);
        if (value != null) control.setRolloverIcon((Icon) value);
        value = getValue(ACTION_COMMAND_KEY);
        if (value != null) control.setActionCommand((String) value);
    }

    /**
	 * @see mipt.gui.controls.ButtonActionOwner#getAction()
	 */
    public final ButtonAction getAction() {
        return this;
    }

    /**
	 * Returns the clone
	 */
    public ButtonAction copy() {
        ButtonAction copy = createCopy();
        copy.setAccelerator(getAccelerator());
        copy.setActionCommand(getActionCommand());
        copy.setMnemonic((char) getMnemonic());
        copy.setName(getName());
        copy.setIcon(getIcon());
        copy.setRolloverIcon(getRolloverIcon());
        copy.setShortDescription(getShortDescription());
        return copy;
    }

    /**
	 * Create instance without standard fields (only with specifiv ones)
	 */
    protected abstract ButtonAction createCopy();
}
