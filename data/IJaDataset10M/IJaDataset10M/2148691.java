package phex.gui.actions;

import javax.swing.Icon;
import javax.swing.KeyStroke;

public abstract class FWToggleAction extends FWAction {

    public static final String IS_SELECTED = "IsSelected";

    /**
     * Defines an Action object with a default description string
     * and default icon.
     */
    public FWToggleAction() {
        super(null, null, null, null, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I> and a default icon.
     *
     * @see javax.swing.Action#NAME
     */
    public FWToggleAction(String name) {
        super(name, null, null, null, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I> and the specified <I>icon</I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon) {
        super(name, smallIcon, null, null, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I> and the specified
     * <I>toolTipText</I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, Icon mediumIcon) {
        super(name, smallIcon, mediumIcon, null, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I> and the specified
     * <I>toolTipText</I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, Icon mediumIcon, String toolTipText) {
        super(name, smallIcon, mediumIcon, toolTipText, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I> and the specified
     * <I>toolTipText</I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, String toolTipText) {
        super(name, smallIcon, null, toolTipText, null, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I>, the specified
     * <I>toolTipText</I> and the specified <I>menuShortCut<I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, String toolTipText, Integer mnemonic) {
        super(name, smallIcon, null, toolTipText, mnemonic, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I>, the specified
     * <I>toolTipText</I> and the specified <I>menuShortCut<I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, Icon largeIcon, String toolTipText, Integer mnemonic) {
        super(name, smallIcon, largeIcon, toolTipText, mnemonic, null);
    }

    /**
     * Defines an Action object with the specified description string
     * <I>name</I>, the specified <I>icon</I>, the specified
     * <I>toolTipText</I>, the specified <I>menuShortCut<I> and
     * the specified <I>accelerator<I>.
     *
     * @see javax.swing.Action#NAME
     * @see javax.swing.Action#SMALL_ICON
     */
    public FWToggleAction(String name, Icon smallIcon, Icon largeIcon, String toolTipText, Integer mnemonic, KeyStroke accelerator) {
        super(name, smallIcon, largeIcon, toolTipText, mnemonic, accelerator);
    }

    /**
     * This could be used by the action to indicate it likes to be displayed as
     * a toggle action.
     * @return If the action is a toggle action or not.
     */
    public boolean isToggleAction() {
        return true;
    }

    public void setSelected(boolean state) {
        putValue(IS_SELECTED, Boolean.valueOf(state));
    }

    public boolean isSelected() {
        Boolean state = (Boolean) getValue(IS_SELECTED);
        if (state == null) {
            return false;
        }
        return state.booleanValue();
    }
}
