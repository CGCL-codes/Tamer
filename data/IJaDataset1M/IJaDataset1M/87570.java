package org.makagiga.commons;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import org.makagiga.commons.script.ScriptEvent;
import org.makagiga.commons.script.ScriptExecutor;

/**
 * An application action.
 *
 * EXAMPLE:
 * @code
 * // Create File|Quit menu item
 * MMenuBar menuBar = new MMenuBar();
 * MMenu fileMenu = new MMenu("File");
 * fileMenu.add(new NewFileAction());
 * menuBar.add(fileMenu);
 * ...
 * private static final class NewFileAction extends AppAction {
 *   NewFileAction() {
 *     super(
 *       _("New"), // action name visible in user interface
 *       "ui/newfile", // icon name
 *       VK_N, CTRL_MASK // Ctrl+N shortcut
 *     );
 *     setHTMLHelp(_("Creates a new file."));
 *   }
 *   @Override
 *   public void onAction() {
 *     createNewFile();
 *   }
 * }
 * @endcode
 */
public class AppAction extends AbstractAction {

    public ScriptEvent onAction;

    private String script;

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 */
    public AppAction(final String text) {
        this(text, (Icon) null, 0, 0);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param icon An icon (@c null = no icon)
	 */
    public AppAction(final String text, final Icon icon) {
        this(text, icon, 0, 0);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param iconName An icon name (@c null = no icon)
	 */
    public AppAction(final String text, final String iconName) {
        this(text, iconName, 0, 0);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param iconName An icon name (@c null = no icon)
	 * @param keyStroke A key stroke
	 */
    public AppAction(final String text, final String iconName, final KeyStroke keyStroke) {
        this(text, iconName, keyStroke.getKeyCode(), keyStroke.getModifiers());
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param iconName An icon name (@c null = no icon)
	 * @param keyCode A key code (e.g. @c VK_X)
	 */
    public AppAction(final String text, final String iconName, final int keyCode) {
        this(text, iconName, keyCode, 0);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param icon An icon (@c null = no icon)
	 * @param keyCode A key code (e.g. @c VK_X)
	 * @param modifiers Modifiers keys (e.g. @c CTRL_MASK)
	 */
    public AppAction(final String text, final Icon icon, final int keyCode, final int modifiers) {
        setName(text);
        if (keyCode != 0) setAcceleratorKey(keyCode, modifiers);
        setIcon(icon);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param iconName An icon name (@c null = no icon)
	 * @param keyCode A key code (e.g. @c VK_X)
	 * @param modifiers Modifiers keys (e.g. @c CTRL_MASK)
	 */
    public AppAction(final String text, final String iconName, final int keyCode, final int modifiers) {
        this(text, FS.getIcon(iconName), keyCode, modifiers);
    }

    /**
	 * Constructs a new action.
	 * @param text A text to display
	 * @param keyCode A key code (e.g. @c VK_X)
	 * @param modifiers Modifiers keys (e.g. @c CTRL_MASK)
	 */
    public AppAction(final String text, final int keyCode, final int modifiers) {
        this(text, (Icon) null, keyCode, modifiers);
    }

    /**
	 * @param e An action event
	 * @see onAction
	 */
    public void actionPerformed(final ActionEvent e) {
        if ((script == null) || !ScriptExecutor.executeActionScript(this)) {
            if (onAction != null) onAction.event();
            onAction();
        }
    }

    /**
	 * @throws IllegalArgumentException If action name is @c null or empty
	 */
    public void connect(final JComponent component, final int condition) {
        MAction.connect(component, condition, this);
    }

    /**
	 * @throws IllegalArgumentException If action name is @c null or empty
	 */
    public void connect(final JComponent component, final int condition, final int keyCode) {
        MAction.connect(component, condition, keyCode, this);
    }

    /**
	 * @throws IllegalArgumentException If action name is @c null or empty
	 */
    public void connect(final JComponent component, final int condition, final int keyCode, final int modifiers) {
        MAction.connect(component, condition, keyCode, modifiers, this);
    }

    public void connect(final JComponent component, final String actionName) {
        MAction.connect(component, actionName, this);
    }

    public void fire() {
        fire(null);
    }

    public void fire(final JComponent source) {
        MAction.fire(this, source);
    }

    /**
	 * Returns the key stroke associated with this action.
	 */
    public KeyStroke getAcceleratorKey() {
        return getValue(ACCELERATOR_KEY, null);
    }

    /**
	 * Sets the keyboard shortcut.
	 * @param keyCode A key code (e.g. @c VK_X)
	 * @param modifiers Modifiers keys (e.g. @c CTRL_MASK)
	 */
    public void setAcceleratorKey(final int keyCode, final int modifiers) {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(keyCode, modifiers));
    }

    /**
	 * Sets the keyboard shortcut.
	 * @param keyStroke A key stroke
	 */
    public void setAcceleratorKey(final KeyStroke keyStroke) {
        putValue(ACCELERATOR_KEY, keyStroke);
    }

    /**
	 * Sets HTML help to @p value.
	 */
    public void setHTMLHelp(final String value) {
        setLongDescription(TK.isEmpty(value) ? null : UI.makeHTML(value));
    }

    /**
	 * Returns the icon associated with this action.
	 */
    public Icon getIcon() {
        return getValue(SMALL_ICON, null);
    }

    /**
	 * Sets icon to @p value.
	 */
    public void setIcon(final Icon value) {
        putValue(SMALL_ICON, value);
    }

    /**
	 * Sets icon to @p iconName (e.g. "ui/quit").
	 */
    public void setIcon(final String iconName) {
        putValue(SMALL_ICON, FS.getIcon(iconName));
    }

    /**
	 * Returns the long description, or @c null.
	 */
    public String getLongDescription() {
        return getValue(LONG_DESCRIPTION, null);
    }

    /**
	 * Sets long description to @p value.
	 */
    public void setLongDescription(final String value) {
        putValue(LONG_DESCRIPTION, value);
    }

    /**
	 * Returns the name or @c null.
	 */
    public String getName() {
        return getValue(NAME, null);
    }

    /**
	 * Sets name to @p value.
	 */
    public void setName(final String value) {
        putValue(NAME, TK.isEmpty(value) ? null : value);
    }

    public String getScript() {
        return script;
    }

    public void setScript(final String value) {
        script = value;
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(final String key, final T defaultValue) {
        Object value = getValue(key);
        return (value == null) ? defaultValue : (T) value;
    }

    /**
	 * Returns @c true if item associated with this action is selected (checked).
	 */
    public boolean isSelected() {
        return getValue(SELECTED_KEY, false);
    }

    /**
	 * Selects the item associated with this action.
	 * @param value @c true = select
	 */
    public void setSelected(final boolean value) {
        putValue(SELECTED_KEY, value);
    }

    /**
	 * Invoked when an action occurs.
	 */
    public void onAction() {
    }

    /**
	 * Sets description to @p value.
	 */
    public void setDescription(final String value) {
        putValue(SHORT_DESCRIPTION, value);
    }
}
