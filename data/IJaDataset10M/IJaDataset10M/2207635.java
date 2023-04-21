package org.makagiga.commons;

import static org.makagiga.commons.UI._;
import static java.awt.event.KeyEvent.*;
import java.io.Serializable;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.KeyStroke;
import org.makagiga.commons.annotation.Important;

/**
 * @mg.threadSafe
 *
 * @since 3.0
 */
public final class MActionInfo implements Serializable {

    private static final String CLEAR_TEXT = _("Clear");

    private static final String CLOSE_TEXT = _("Close");

    private static final String DELETE_TEXT = _("Delete");

    public static final MActionInfo ABOUT = new MActionInfo(_("About"), "ui/info");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo ADD_STAR = new MActionInfo(_("Add Star"), "ui/star");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo ADVANCED = new MActionInfo(_("Advanced..."), "ui/misc");

    /**
	 * @since 3.8.8
	 */
    public static final MActionInfo BACK = new MActionInfo(_("Back"), "ui/previous");

    public static final MActionInfo CANCEL = new MActionInfo(_("Cancel"), "ui/cancel");

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo CLEAR = new MActionInfo(CLEAR_TEXT, "ui/delete");

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo CLEAR_HISTORY = new MActionInfo(_("Clear History"), "ui/delete");

    public static final MActionInfo CLEAR_LEFT = new MActionInfo(CLEAR_TEXT, "ui/clearleft");

    public static final MActionInfo CLEAR_RIGHT = new MActionInfo(CLEAR_TEXT, "ui/clearright");

    public static final MActionInfo CLOSE = new MActionInfo(CLOSE_TEXT, "ui/close", VK_W, MAction.getMenuMask());

    /**
	 * @since 3.8
	 *
	 * @deprecated As of 4.2, replaced by {@link #CLOSE} and {@link #noKeyStroke()}
	 */
    @Deprecated
    public static final MActionInfo CLOSE_NO_KEY_STROKE = new MActionInfo(CLOSE_TEXT, "ui/close");

    public static final MActionInfo CONTINUE = new MActionInfo(_("Continue"), "ui/next");

    public static final MActionInfo COPY = new MActionInfo(_("Copy"), "ui/copy", VK_C, MAction.getMenuMask());

    /**
	 * @since 3.8
	 */
    public static final MActionInfo COPY_AS_HTML = new MActionInfo(_("Copy as HTML"), "ui/copy");

    public static final MActionInfo CURRENT_DATE_AND_TIME = new MActionInfo(_("Current Date and Time"), "ui/star");

    public static final MActionInfo CUT = new MActionInfo(_("Cut"), "ui/cut", VK_X, MAction.getMenuMask());

    public static final MActionInfo DELETE = new MActionInfo(DELETE_TEXT, "ui/delete", VK_DELETE);

    /**
	 * @deprecated As of 4.2, replaced by {@link #DELETE} and {@link #noKeyStroke()}
	 */
    @Deprecated
    public static final MActionInfo DELETE_NO_KEY_STROKE = new MActionInfo(DELETE_TEXT, "ui/delete");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo DOWNLOAD = new MActionInfo(_("Download"), "ui/download");

    /**
	 * @since 3.8.8
	 */
    public static final MActionInfo FORWARD = new MActionInfo(_("Forward"), "ui/next");

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo FIND = new MActionInfo(_("Find..."), "ui/find", VK_F, MAction.getMenuMask());

    /**
	 * @since 4.0
	 */
    public static final MActionInfo FIND_NEXT = new MActionInfo(_("Find Next"), "ui/down", MAction.getFindNextKeyStroke());

    /**
	 * @since 3.8.2
	 */
    public static final MActionInfo HELP = new MActionInfo(_("Help"), "ui/help", VK_F1);

    public static final MActionInfo IMPORT = new MActionInfo(_("Import"), "ui/ok");

    public static final MActionInfo INSTALL = new MActionInfo(_("Install"), "ui/save");

    /**
	 * @since 3.8.6
	 */
    public static final MActionInfo LOCK = new MActionInfo(_("Lock"), "ui/locked");

    public static final MActionInfo LOGIN = new MActionInfo(_("Login"), "ui/ok");

    public static final MActionInfo NEW = new MActionInfo(_("New"), "ui/newfile", VK_N, MAction.getMenuMask());

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo NEW_FOLDER = new MActionInfo(_("New Folder..."), "ui/newfolder", VK_N, MAction.getMenuMask() | SHIFT_MASK);

    /**
	 * @since 3.8.8
	 */
    public static final MActionInfo NEXT = new MActionInfo(_("Next"), "ui/next");

    public static final MActionInfo NO_DATE_TIME = new MActionInfo(_("No Date/Time"), "ui/clearright");

    public static final MActionInfo OK = new MActionInfo(_("OK"), "ui/ok");

    public static final MActionInfo OPEN = new MActionInfo(_("Open..."), "ui/open", VK_O, MAction.getMenuMask());

    public static final MActionInfo OPEN_URI = new MActionInfo(_("Open"), "ui/misc", VK_L, MAction.getMenuMask());

    public static final MActionInfo OVERWRITE = new MActionInfo(_("Overwrite"), "ui/ok");

    public static final MActionInfo PASTE = new MActionInfo(_("Paste"), "ui/paste", VK_V, MAction.getMenuMask());

    /**
	 * @since 3.8.8
	 */
    public static final MActionInfo PREVIOUS = new MActionInfo(_("Previous"), "ui/previous");

    public static final MActionInfo PRINT = new MActionInfo(_("Print..."), "ui/print", VK_P, MAction.getMenuMask());

    public static final MActionInfo PROPERTIES = new MActionInfo(_("Properties"), "ui/properties", VK_ENTER, ALT_MASK);

    public static final MActionInfo QUIT = new MActionInfo(_("Quit"), "ui/quit", VK_Q, MAction.getMenuMask() | SHIFT_MASK);

    /**
	 * @since 3.2
	 */
    public static final MActionInfo REFRESH = new MActionInfo(_("Refresh"), "ui/refresh");

    /**
	 * @since 4.4
	 */
    public static final MActionInfo REMOVE = new MActionInfo(_("Remove"), "ui/delete", VK_DELETE);

    /**
	 * @since 3.4
	 */
    public static final MActionInfo REMOVE_STAR = new MActionInfo(_("Remove Star"), "ui/star");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo RESTORE_DEFAULT_VALUES = new MActionInfo(_("Restore Default Values"), "ui/revert");

    public static final MActionInfo REVERT = new MActionInfo(_("Revert"), "ui/revert");

    /**
	 * @since 4.2
	 */
    public static final MActionInfo ROTATE_LEFT = new MActionInfo(_("Rotate Left"), "ui/rotateleft", VK_OPEN_BRACKET, CTRL_MASK);

    /**
	 * @since 4.2
	 */
    public static final MActionInfo ROTATE_RIGHT = new MActionInfo(_("Rotate Right"), "ui/rotate", VK_CLOSE_BRACKET, CTRL_MASK);

    public static final MActionInfo SAVE = new MActionInfo(_("Save"), "ui/save", VK_S, MAction.getMenuMask());

    /**
	 * @since 3.8.8
	 */
    public static final MActionInfo SAVE_AS = new MActionInfo(_("Save As..."), "ui/save");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo SAVE_AS_DEFAULT = new MActionInfo(_("Save As Default"), "ui/save");

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo SEARCH = new MActionInfo(_("Search..."), "ui/find");

    public static final MActionInfo SELECT_ALL = new MActionInfo(_("Select All"), null, VK_A, MAction.getMenuMask());

    /**
	 * @since 4.2
	 */
    public static final MActionInfo SET_COLOR = new MActionInfo(_("Set Color..."), "ui/color");

    /**
	 * @since 3.8.1
	 */
    public static final MActionInfo SET_DATE_TIME = new MActionInfo(_("Set Date/Time"), "ui/calendar");

    public static final MActionInfo SETTINGS = new MActionInfo(_("Settings"), "ui/configure");

    /**
	 * @since 3.8.3
	 */
    public static final MActionInfo SHOW_GRID = new MActionInfo(_("Show Grid"), "ui/grid");

    /**
	 * @since 3.4
	 */
    public static final MActionInfo SHOW_STARRED = new MActionInfo(_("Show Starred"), "ui/star");

    public static final MActionInfo UNINSTALL = new MActionInfo(_("Uninstall"), "ui/delete");

    /**
	 * @since 3.8.6
	 */
    public static final MActionInfo UNLOCK = new MActionInfo(_("Unlock"), "ui/unlocked");

    public static final MActionInfo UNSELECT_ALL = new MActionInfo(_("Unselect All"), null, VK_A, MAction.getMenuMask() | SHIFT_MASK);

    /**
	 * @since 4.2
	 */
    public static final MActionInfo UPDATE = new MActionInfo(_("Update"), "ui/refresh");

    /**
	 * @since 4.2
	 */
    public static final MActionInfo WHATS_THIS = new MActionInfo(_("What's This?"), null, VK_F1, SHIFT_MASK);

    private int hash;

    private int keyCode;

    private int modifiers;

    private String iconName;

    private String text;

    /**
	 * @since 4.2
	 */
    public MActionInfo(final String text) {
        this(text, null);
    }

    public MActionInfo(final String text, final String iconName) {
        this(text, iconName, 0, 0);
    }

    /**
	 * @since 4.2
	 */
    public MActionInfo(final String text, final String iconName, final int keyCode) {
        this(text, iconName, keyCode, 0);
    }

    public MActionInfo(final String text, final String iconName, final int keyCode, final int modifiers) {
        this.text = text;
        this.iconName = iconName;
        this.keyCode = keyCode;
        this.modifiers = modifiers;
    }

    /**
	 * @since 4.0
	 */
    public MActionInfo(final String text, final String iconName, final KeyStroke keyStroke) {
        this(text, iconName, keyStroke.getKeyCode(), keyStroke.getModifiers());
    }

    @Override
    public boolean equals(final Object o) {
        switch(MObject.equalsFinal(this, o)) {
            case YES:
                return true;
            case NO:
                return false;
            default:
                MActionInfo other = (MActionInfo) o;
                return (this.keyCode == other.keyCode) && (this.modifiers == other.modifiers) && Objects.equals(this.iconName, other.iconName) && Objects.equals(this.text, other.text);
        }
    }

    /**
	 * Returns a text suitable for dialog titles.
	 * The current implementation returns {@link #getText()}
	 * without leading and trailing <code>"..."</code>.
	 *
	 * @since 3.8.1
	 */
    public String getDialogTitle() {
        return getDialogTitle(getText());
    }

    public Icon getIcon() {
        return MIcon.stock(iconName);
    }

    public String getIconName() {
        return iconName;
    }

    public int getKeyCode() {
        return keyCode;
    }

    /**
	 * @since 3.8.7
	 */
    public KeyStroke getKeyStroke() {
        if (keyCode == 0) return null;
        return KeyStroke.getKeyStroke(keyCode, modifiers);
    }

    /**
	 * @since 3.8.10
	 */
    public Icon getMediumIcon() {
        return MIcon.medium(iconName);
    }

    public int getModifiers() {
        return modifiers;
    }

    public Icon getSmallIcon() {
        return MIcon.small(iconName);
    }

    public String getText() {
        return text;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            int h = MObject.hashCode(iconName, text);
            h = 31 * h + keyCode;
            h = 31 * h + modifiers;
            hash = h;
        }
        return hash;
    }

    /**
	 * Returns a copy of this object without icon info.
	 *
	 * @since 4.4
	 */
    public MActionInfo noIcon() {
        if (iconName == null) return this;
        return new MActionInfo(text, null, keyCode, modifiers);
    }

    /**
	 * Returns a copy of this object without key stroke info.
	 *
	 * @since 4.2
	 */
    public MActionInfo noKeyStroke() {
        if (keyCode == 0) return this;
        return new MActionInfo(text, iconName);
    }

    @Important
    @Override
    public String toString() {
        return Objects.toString(text, "");
    }

    /**
	 * Returns a copy of this object with a specified key stroke info.
	 *
	 * @since 4.2
	 */
    public MActionInfo withKeyStroke(final int keyCode) {
        return new MActionInfo(text, iconName, keyCode, 0);
    }

    /**
	 * Returns a copy of this object with a specified key stroke info.
	 *
	 * @since 4.2
	 */
    public MActionInfo withKeyStroke(final int keyCode, final int modifiers) {
        return new MActionInfo(text, iconName, keyCode, modifiers);
    }

    /**
	 * Returns a copy of this object with a specified key stroke info.
	 *
	 * @since 4.2
	 */
    public MActionInfo withKeyStroke(final KeyStroke keyStroke) {
        return new MActionInfo(text, iconName, keyStroke);
    }

    static String getDialogTitle(final String text) {
        String result = text;
        if (TK.isEmpty(result)) return result;
        if (result.startsWith("...")) result = result.substring("...".length());
        int i = result.lastIndexOf("...");
        if (i != -1) result = result.substring(0, i);
        return result.trim();
    }
}
