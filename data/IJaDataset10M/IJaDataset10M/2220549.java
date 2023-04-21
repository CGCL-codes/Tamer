package com.raelity.jvi.swing;

import java.awt.EventQueue;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.KeyStroke;
import com.raelity.jvi.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.TextAction;
import static java.awt.event.InputEvent.SHIFT_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;
import static com.raelity.jvi.KeyDefs.*;
import static com.raelity.jvi.Constants.*;

public class KeyBinding {

    private static Logger LOG = Logger.getLogger(KeyBinding.class.getName());

    public static final String KEY_BINDINGS = "KeyBinding";

    private static Preferences prefs = ViManager.getViFactory().getPreferences().node(ViManager.PREFS_KEYS);

    private static KeyBinding INSTANCE;

    private static KeyBinding getInstance() {
        if (INSTANCE == null) INSTANCE = new KeyBinding();
        return INSTANCE;
    }

    private KeyBinding() {
    }

    private static BooleanOption keyDebugOption;

    public static final boolean isKeyDebug() {
        if (keyDebugOption == null) {
            keyDebugOption = (BooleanOption) Options.getOption(Options.dbgKeyStrokes);
        }
        if (keyDebugOption == null) {
            return false;
        } else {
            return keyDebugOption.getBoolean();
        }
    }

    public static boolean notImpDebug = false;

    public static final int MOD_MASK = SHIFT_MASK | CTRL_MASK | InputEvent.META_MASK | InputEvent.ALT_MASK;

    static final String enqueKeyAction = "enque-key";

    public static void init() {
        createSubKeymaps();
        prefs.addPreferenceChangeListener(new PreferenceChangeListener() {

            public void preferenceChange(PreferenceChangeEvent evt) {
                if (EventQueue.isDispatchThread()) updateKeymap.run(); else EventQueue.invokeLater(updateKeymap);
            }
        });
        getBindingsListInternal();
    }

    private static Runnable updateKeymap = new Runnable() {

        public void run() {
            bindingList = null;
            firePropertyChange(KEY_BINDINGS, null, null);
        }
    };

    /**
   * Return a keymap for a standard swing text component.
   * Also, if not already existing, construct insert and normal mode
   * keymaps only used for user
   * defined mappings.
   */
    public static Keymap getKeymap() {
        Keymap keymap = JTextComponent.addKeymap(null, null);
        keymap.setDefaultAction(ViManager.getViFactory().createCharAction(enqueKeyAction));
        JTextComponent.loadKeymap(keymap, getBindings(), getActions());
        createSubKeymaps();
        return keymap;
    }

    static void createSubKeymaps() {
        Keymap insertModeKeymap = JTextComponent.addKeymap(null, null);
        JTextComponent.KeyBinding[] bindings = getInsertModeBindings();
        Action[] actions = getInsertModeActions();
        JTextComponent.loadKeymap(insertModeKeymap, bindings, actions);
        ViManager.setInsertModeKeymap(insertModeKeymap);
    }

    public static Action getDefaultAction() {
        return ViManager.getViFactory().createCharAction(enqueKeyAction);
    }

    /** Modify the keymap <code>km00</code> by removing any keystrokes
   * specified in the keymap <code>km01</code>. This is typically used
   * to remove keys that vi would normally be looking at, e.g. function
   * keys, but are otherwise bound to the environment in which vi is
   * operating.
   */
    public static void removeBindings(Keymap km00, Keymap km01) {
        int i;
        KeyStroke[] k01 = km01.getBoundKeyStrokes();
        for (i = 0; i < k01.length; i++) {
            km00.removeKeyStrokeBinding(k01[i]);
        }
    }

    /**
   * Bind the keys to actions of their own name. This is simply a
   * way to grab all the keys. May want to use key events
   * directly at some point.
   */
    public static JTextComponent.KeyBinding[] getBindings() {
        List<JTextComponent.KeyBinding> l = getBindingsListInternal();
        return l.toArray(new JTextComponent.KeyBinding[l.size()]);
    }

    private static List<JTextComponent.KeyBinding> bindingList;

    /**
   * Return an ArrayList of bindings. This can be modified without
   * affecting the backing list.
   */
    public static List<JTextComponent.KeyBinding> getBindingsList() {
        return Collections.unmodifiableList(getBindingsListInternal());
    }

    private static synchronized List<JTextComponent.KeyBinding> getBindingsListInternal() {
        if (bindingList != null) {
            return bindingList;
        }
        bindingList = new ArrayList<JTextComponent.KeyBinding>();
        List<JTextComponent.KeyBinding> bl = bindingList;
        for (char c = 'A'; c <= 'Z'; c++) {
            String name = "Ctrl-" + String.valueOf(c);
            checkUseKey(bl, name, name, c, CTRL_MASK);
        }
        for (String key : keypadNameMap.keySet()) {
            checkUseKey(bl, key, keypadNameMap.get(key), 0);
            checkUseKey(bl, key, keypadNameMap.get(key), CTRL_MASK);
            checkUseKey(bl, key, keypadNameMap.get(key), SHIFT_MASK);
        }
        checkUseKey(bl, "Ctrl-[", "Escape", KeyEvent.VK_OPEN_BRACKET, CTRL_MASK);
        checkUseKey(bl, "Ctrl-]", "CloseBracket", KeyEvent.VK_CLOSE_BRACKET, CTRL_MASK);
        checkUseKey(bl, "Ctrl->", "PeriodCloseAngle", '.', CTRL_MASK);
        checkUseKey(bl, "Ctrl-<", "CommaOpenAngle", ',', CTRL_MASK);
        checkUseKey(bl, "Ctrl-@", "Ctrl-@", KeyEvent.VK_2, CTRL_MASK);
        checkUseKey(bl, "Ctrl-@", "Ctrl-@", KeyEvent.VK_2, CTRL_MASK | SHIFT_MASK);
        return bl;
    }

    private static void checkUseKey(List<JTextComponent.KeyBinding> bl, String key, int code, int mod) {
        String modTag = "";
        switch(mod) {
            case 0:
                modTag = "";
                break;
            case CTRL_MASK:
                modTag = "Ctrl-";
                break;
            case SHIFT_MASK:
                modTag = "Shift-";
                break;
            default:
                assert (false) : "mod = " + mod + ", not jVi modifier.";
        }
        String prefName = modTag + key;
        checkUseKey(bl, prefName, key, code, mod);
    }

    private static void checkUseKey(List<JTextComponent.KeyBinding> bl, String prefName, String actionName, int code, int mod) {
        keyBindingPrefs.addKnownKey(prefName);
        if (prefs.getBoolean(prefName, getCatchKeyDefault(prefName))) {
            bl.add(createKeyBinding(code, mod, "Vi" + actionName + "Key"));
        }
    }

    public static List getExtraBindingsList() {
        List<JTextComponent.KeyBinding> bl = new ArrayList<JTextComponent.KeyBinding>();
        bl.add(createKeyBinding(KeyEvent.VK_SPACE, SHIFT_MASK, "ViSpaceKey"));
        bl.add(createKeyBinding(KeyEvent.VK_BACK_SPACE, SHIFT_MASK, "ViBack_spaceKey"));
        bl.add(createKeyBinding(KeyEvent.VK_BACK_SLASH, CTRL_MASK, "ViCtrl-BackslashKey"));
        bl.add(createKeyBinding(KeyEvent.VK_TAB, SHIFT_MASK, "ViTabKey"));
        bl.add(createKeyBinding(KeyEvent.VK_ENTER, SHIFT_MASK, "ViEnterKey"));
        return bl;
    }

    public static List<JTextComponent.KeyBinding> getFunctionKeyBindingsList() {
        List<JTextComponent.KeyBinding> bl = new ArrayList<JTextComponent.KeyBinding>();
        bl.add(createKeyBinding(KeyEvent.VK_F1, 0, "ViF1Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F2, 0, "ViF2Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F3, 0, "ViF3Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F4, 0, "ViF4Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F5, 0, "ViF5Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F6, 0, "ViF6Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F7, 0, "ViF7Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F8, 0, "ViF8Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F9, 0, "ViF9Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F10, 0, "ViF10Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F11, 0, "ViF11Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F12, 0, "ViF12Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F1, SHIFT_MASK, "ViF1Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F2, SHIFT_MASK, "ViF2Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F3, SHIFT_MASK, "ViF3Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F4, SHIFT_MASK, "ViF4Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F5, SHIFT_MASK, "ViF5Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F6, SHIFT_MASK, "ViF6Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F7, SHIFT_MASK, "ViF7Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F8, SHIFT_MASK, "ViF8Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F9, SHIFT_MASK, "ViF9Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F10, SHIFT_MASK, "ViF10Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F11, SHIFT_MASK, "ViF11Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F12, SHIFT_MASK, "ViF12Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F1, CTRL_MASK, "ViF1Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F2, CTRL_MASK, "ViF2Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F3, CTRL_MASK, "ViF3Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F4, CTRL_MASK, "ViF4Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F5, CTRL_MASK, "ViF5Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F6, CTRL_MASK, "ViF6Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F7, CTRL_MASK, "ViF7Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F8, CTRL_MASK, "ViF8Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F9, CTRL_MASK, "ViF9Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F10, CTRL_MASK, "ViF10Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F11, CTRL_MASK, "ViF11Key"));
        bl.add(createKeyBinding(KeyEvent.VK_F12, CTRL_MASK, "ViF12Key"));
        return bl;
    }

    /**
     * Return the available actions.
     * 
     * NEEDSWORK: only need actionsMap, use values to get all the actions.
     */
    public static Action[] getActions() {
        List<Action> l = getActionsListInternal();
        return l.toArray(new Action[l.size()]);
    }

    private static List<Action> actionList;

    private static Map<String, Action> actionsMap;

    public static Action getAction(String key) {
        if (actionsMap == null) getActionsListInternal();
        return actionsMap.get(key);
    }

    public static List<Action> getActionsList() {
        return Collections.unmodifiableList(getActionsListInternal());
    }

    private static synchronized List<Action> getActionsListInternal() {
        if (actionList == null) actionList = createActionList();
        return actionList;
    }

    private static List<Action> createActionList() {
        List<Action> actionsList = new ArrayList<Action>();
        try {
            ViFactory factory = ViManager.getViFactory();
            actionsList.add(factory.createKeyAction("ViUpKey", K_UP));
            actionsList.add(factory.createKeyAction("ViDownKey", K_DOWN));
            actionsList.add(factory.createKeyAction("ViLeftKey", K_LEFT));
            actionsList.add(factory.createKeyAction("ViRightKey", K_RIGHT));
            actionsList.add(factory.createKeyAction("ViInsertKey", K_INS));
            actionsList.add(factory.createKeyAction("ViDeleteKey", K_DEL));
            actionsList.add(factory.createKeyAction("ViTabKey", K_TAB));
            actionsList.add(factory.createKeyAction("ViHomeKey", K_HOME));
            actionsList.add(factory.createKeyAction("ViEndKey", K_END));
            actionsList.add(factory.createKeyAction("ViHelpKey", K_HELP));
            actionsList.add(factory.createKeyAction("ViUndoKey", K_UNDO));
            actionsList.add(factory.createKeyAction("ViBack_spaceKey", (char) KeyEvent.VK_BACK_SPACE));
            actionsList.add(factory.createKeyAction("ViPageUpKey", K_PAGEUP));
            actionsList.add(factory.createKeyAction("ViPageDownKey", K_PAGEDOWN));
            actionsList.add(factory.createKeyAction("ViPlusKey", K_KPLUS));
            actionsList.add(factory.createKeyAction("ViMinusKey", K_KMINUS));
            actionsList.add(factory.createKeyAction("ViDivideKey", K_KDIVIDE));
            actionsList.add(factory.createKeyAction("ViMultiplyKey", K_KMULTIPLY));
            actionsList.add(factory.createKeyAction("ViEnterKey", K_KENTER));
            actionsList.add(factory.createKeyAction("ViPeriodCloseAngleKey", K_X_PERIOD));
            actionsList.add(factory.createKeyAction("ViCommaOpenAngleKey", K_X_COMMA));
            actionsList.add(factory.createKeyAction("ViCtrl-@Key", (char) 0));
            actionsList.add(factory.createKeyAction("ViCtrl-AKey", (char) 1));
            actionsList.add(factory.createKeyAction("ViCtrl-BKey", (char) 2));
            actionsList.add(factory.createKeyAction("ViCtrl-CKey", (char) 3));
            actionsList.add(factory.createKeyAction("ViCtrl-DKey", (char) 4));
            actionsList.add(factory.createKeyAction("ViCtrl-EKey", (char) 5));
            actionsList.add(factory.createKeyAction("ViCtrl-FKey", (char) 6));
            actionsList.add(factory.createKeyAction("ViCtrl-GKey", (char) 7));
            actionsList.add(factory.createKeyAction("ViCtrl-HKey", (char) 8));
            actionsList.add(factory.createKeyAction("ViCtrl-IKey", (char) 9));
            actionsList.add(factory.createKeyAction("ViCtrl-JKey", (char) 10));
            actionsList.add(factory.createKeyAction("ViCtrl-KKey", (char) 11));
            actionsList.add(factory.createKeyAction("ViCtrl-LKey", (char) 12));
            actionsList.add(factory.createKeyAction("ViCtrl-MKey", (char) 13));
            actionsList.add(factory.createKeyAction("ViCtrl-NKey", (char) 14));
            actionsList.add(factory.createKeyAction("ViCtrl-OKey", (char) 15));
            actionsList.add(factory.createKeyAction("ViCtrl-PKey", (char) 16));
            actionsList.add(factory.createKeyAction("ViCtrl-QKey", (char) 17));
            actionsList.add(factory.createKeyAction("ViCtrl-RKey", (char) 18));
            actionsList.add(factory.createKeyAction("ViCtrl-SKey", (char) 19));
            actionsList.add(factory.createKeyAction("ViCtrl-TKey", (char) 20));
            actionsList.add(factory.createKeyAction("ViCtrl-UKey", (char) 21));
            actionsList.add(factory.createKeyAction("ViCtrl-VKey", (char) 22));
            actionsList.add(factory.createKeyAction("ViCtrl-WKey", (char) 23));
            actionsList.add(factory.createKeyAction("ViCtrl-XKey", (char) 24));
            actionsList.add(factory.createKeyAction("ViCtrl-YKey", (char) 25));
            actionsList.add(factory.createKeyAction("ViCtrl-ZKey", (char) 26));
            actionsList.add(factory.createKeyAction("ViEscapeKey", (char) KeyEvent.VK_ESCAPE));
            actionsList.add(factory.createKeyAction("ViCtrl-BackslashKey", (char) 28));
            actionsList.add(factory.createKeyAction("ViCloseBracketKey", (char) 29));
            actionsList.add(factory.createKeyAction("ViSpaceKey", (char) KeyEvent.VK_SPACE));
            actionsList.add(factory.createKeyAction("ViF1Key", K_F1));
            actionsList.add(factory.createKeyAction("ViF2Key", K_F2));
            actionsList.add(factory.createKeyAction("ViF3Key", K_F3));
            actionsList.add(factory.createKeyAction("ViF4Key", K_F4));
            actionsList.add(factory.createKeyAction("ViF5Key", K_F5));
            actionsList.add(factory.createKeyAction("ViF6Key", K_F6));
            actionsList.add(factory.createKeyAction("ViF7Key", K_F7));
            actionsList.add(factory.createKeyAction("ViF8Key", K_F8));
            actionsList.add(factory.createKeyAction("ViF9Key", K_F9));
            actionsList.add(factory.createKeyAction("ViF10Key", K_F10));
            actionsList.add(factory.createKeyAction("ViF11Key", K_F11));
            actionsList.add(factory.createKeyAction("ViF12Key", K_F12));
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, null, e);
        }
        actionsMap = new HashMap<String, Action>();
        for (Action a : actionsList) {
            TextAction ta = (TextAction) a;
            actionsMap.put((String) ta.getValue(Action.NAME), a);
        }
        return actionsList;
    }

    public static JTextComponent.KeyBinding[] getInsertModeBindings() {
        List<JTextComponent.KeyBinding> l = getInsertModeBindingsList();
        return l.toArray(new JTextComponent.KeyBinding[l.size()]);
    }

    public static List<JTextComponent.KeyBinding> getInsertModeBindingsList() {
        List<JTextComponent.KeyBinding> bl = new ArrayList<JTextComponent.KeyBinding>();
        bl.add(createKeyBinding(KeyEvent.VK_PERIOD, CTRL_MASK, "ViInsert_indentNextParen"));
        bl.add(createKeyBinding(KeyEvent.VK_COMMA, CTRL_MASK, "ViInsert_indentPrevParen"));
        bl.add(createKeyBinding(KeyEvent.VK_T, CTRL_MASK, "ViInsert_shiftRight"));
        bl.add(createKeyBinding(KeyEvent.VK_D, CTRL_MASK, "ViInsert_shiftLeft"));
        bl.add(createKeyBinding(KeyEvent.VK_INSERT, 0, "ViInsert_insertReplace"));
        return bl;
    }

    public static Action[] getInsertModeActions() {
        Action[] localActions = null;
        try {
            ViFactory factory = ViManager.getViFactory();
            localActions = new Action[] { factory.createInsertModeKeyAction("ViInsert_shiftRight", IM_SHIFT_RIGHT, "Insert one shiftwidth of indent at the" + " start of the current line." + " Only key press events are valid."), factory.createInsertModeKeyAction("ViInsert_shiftLeft", IM_SHIFT_LEFT, "Delete one shiftwidth of indent at the" + " start of the current line." + " Only key press events are valid."), factory.createInsertModeKeyAction("ViInsert_indentNextParen", IM_SHIFT_RIGHT_TO_PAREN, "Indent current line to start under next" + " parenthesis on previous line." + " Only key press events are valid."), factory.createInsertModeKeyAction("ViInsert_indentPrevParen", IM_SHIFT_LEFT_TO_PAREN, "Indent current line to start under previous" + " parenthesis on previous line." + " Only key press events are valid."), factory.createInsertModeKeyAction("ViInsert_insertReplace", IM_INS_REP, "Toggle between insert and replace mode") };
        } catch (Throwable e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return localActions;
    }

    public static int[] initJavaKeyMap() {
        int[] jk = new int[KeyDefs.MAX_JAVA_KEY_MAP + 1];
        for (int i = 0; i < jk.length; i++) {
            jk[i] = -1;
        }
        jk[MAP_K_UP] = KeyEvent.VK_UP;
        jk[MAP_K_DOWN] = KeyEvent.VK_DOWN;
        jk[MAP_K_LEFT] = KeyEvent.VK_LEFT;
        jk[MAP_K_RIGHT] = KeyEvent.VK_RIGHT;
        jk[MAP_K_TAB] = KeyEvent.VK_TAB;
        jk[MAP_K_HOME] = KeyEvent.VK_HOME;
        jk[MAP_K_END] = KeyEvent.VK_END;
        jk[MAP_K_F1] = KeyEvent.VK_F1;
        jk[MAP_K_F2] = KeyEvent.VK_F2;
        jk[MAP_K_F3] = KeyEvent.VK_F3;
        jk[MAP_K_F4] = KeyEvent.VK_F4;
        jk[MAP_K_F5] = KeyEvent.VK_F5;
        jk[MAP_K_F6] = KeyEvent.VK_F6;
        jk[MAP_K_F7] = KeyEvent.VK_F7;
        jk[MAP_K_F8] = KeyEvent.VK_F8;
        jk[MAP_K_F9] = KeyEvent.VK_F9;
        jk[MAP_K_F10] = KeyEvent.VK_F10;
        jk[MAP_K_F11] = KeyEvent.VK_F11;
        jk[MAP_K_F12] = KeyEvent.VK_F12;
        jk[MAP_K_F13] = KeyEvent.VK_F13;
        jk[MAP_K_F14] = KeyEvent.VK_F14;
        jk[MAP_K_F15] = KeyEvent.VK_F15;
        jk[MAP_K_F16] = KeyEvent.VK_F16;
        jk[MAP_K_F17] = KeyEvent.VK_F17;
        jk[MAP_K_F18] = KeyEvent.VK_F18;
        jk[MAP_K_F19] = KeyEvent.VK_F19;
        jk[MAP_K_F20] = KeyEvent.VK_F20;
        jk[MAP_K_F21] = KeyEvent.VK_F21;
        jk[MAP_K_F22] = KeyEvent.VK_F22;
        jk[MAP_K_F23] = KeyEvent.VK_F23;
        jk[MAP_K_F24] = KeyEvent.VK_F24;
        jk[MAP_K_HELP] = KeyEvent.VK_HELP;
        jk[MAP_K_UNDO] = KeyEvent.VK_UNDO;
        jk[MAP_K_BS] = KeyEvent.VK_BACK_SPACE;
        jk[MAP_K_INS] = KeyEvent.VK_INSERT;
        jk[MAP_K_DEL] = KeyEvent.VK_DELETE;
        jk[MAP_K_PAGEUP] = KeyEvent.VK_PAGE_UP;
        jk[MAP_K_PAGEDOWN] = KeyEvent.VK_PAGE_DOWN;
        jk[MAP_K_KPLUS] = KeyEvent.VK_PLUS;
        jk[MAP_K_KMINUS] = KeyEvent.VK_MINUS;
        jk[MAP_K_KDIVIDE] = KeyEvent.VK_DIVIDE;
        jk[MAP_K_KMULTIPLY] = KeyEvent.VK_MULTIPLY;
        jk[MAP_K_KENTER] = KeyEvent.VK_ENTER;
        jk[MAP_K_X_PERIOD] = KeyEvent.VK_PERIOD;
        jk[MAP_K_X_COMMA] = KeyEvent.VK_COMMA;
        return jk;
    }

    private static JTextComponent.KeyBinding createKeyBinding(int c, int modifiers, String bindKeyName) {
        JTextComponent.KeyBinding kb = new JTextComponent.KeyBinding(KeyStroke.getKeyStroke(c, modifiers), bindKeyName);
        return kb;
    }

    public static boolean getCatchKeyDefault(String prefName) {
        return keyBindingPrefs.getCatchKeyDefault(prefName);
    }

    public static boolean isKnownKey(String prefName) {
        return keyBindingPrefs.isKnownKey(prefName);
    }

    public static Set<String> getKeypadNames() {
        return Collections.unmodifiableSet(keypadNameMap.keySet());
    }

    private static HashMap<String, Integer> keypadNameMap = new HashMap<String, Integer>();

    private static KeyBindingPrefs keyBindingPrefs = new KeyBindingPrefs();

    private static class KeyBindingPrefs {

        private Set<String> defaultKeysFalse = new HashSet<String>();

        private Set<String> knownKeys = new HashSet<String>();

        KeyBindingPrefs() {
            defaultKeysFalse.add("Ctrl-[");
            defaultKeysFalse.add("Ctrl-@");
            defaultKeysFalse.add("Ctrl-A");
            defaultKeysFalse.add("Ctrl-C");
            defaultKeysFalse.add("Ctrl-I");
            defaultKeysFalse.add("Ctrl-J");
            defaultKeysFalse.add("Ctrl-K");
            defaultKeysFalse.add("Ctrl-Q");
            defaultKeysFalse.add("Ctrl-V");
            defaultKeysFalse.add("Ctrl-X");
            defaultKeysFalse.add("Ctrl-Z");
            defaultKeysFalse.add("Shift-Enter");
            defaultKeysFalse.add("Ctrl-Enter");
            defaultKeysFalse.add("Shift-Escape");
            defaultKeysFalse.add("Ctrl-Escape");
            defaultKeysFalse.add("Shift-Back_space");
            defaultKeysFalse.add("Ctrl-Back_space");
            defaultKeysFalse.add("Shift-Tab");
            defaultKeysFalse.add("Ctrl-Tab");
            defaultKeysFalse.add("Shift-Undo");
            defaultKeysFalse.add("Ctrl-Undo");
            defaultKeysFalse.add("Shift-Insert");
            defaultKeysFalse.add("Ctrl-Insert");
            defaultKeysFalse.add("Shift-Delete");
            defaultKeysFalse.add("Ctrl-Delete");
            keypadNameMap.put("Enter", KeyEvent.VK_ENTER);
            keypadNameMap.put("Escape", KeyEvent.VK_ESCAPE);
            keypadNameMap.put("Back_space", KeyEvent.VK_BACK_SPACE);
            keypadNameMap.put("Tab", KeyEvent.VK_TAB);
            keypadNameMap.put("Up", KeyEvent.VK_UP);
            keypadNameMap.put("Down", KeyEvent.VK_DOWN);
            keypadNameMap.put("Left", KeyEvent.VK_LEFT);
            keypadNameMap.put("Right", KeyEvent.VK_RIGHT);
            keypadNameMap.put("Insert", KeyEvent.VK_INSERT);
            keypadNameMap.put("Delete", KeyEvent.VK_DELETE);
            keypadNameMap.put("Home", KeyEvent.VK_HOME);
            keypadNameMap.put("End", KeyEvent.VK_END);
            keypadNameMap.put("Undo", KeyEvent.VK_UNDO);
            keypadNameMap.put("PageUp", KeyEvent.VK_PAGE_UP);
            keypadNameMap.put("PageDown", KeyEvent.VK_PAGE_DOWN);
        }

        public void addKnownKey(String key) {
            knownKeys.add(key);
        }

        public Boolean isKnownKey(String key) {
            return knownKeys.contains(key);
        }

        public Boolean getCatchKeyDefault(String keyName) {
            return !defaultKeysFalse.contains(keyName);
        }
    }

    private static PropertyChangeSupport pcs = new PropertyChangeSupport(getInstance());

    public static void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public static void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }

    public static void addPropertyChangeListener(String p, PropertyChangeListener l) {
        pcs.addPropertyChangeListener(p, l);
    }

    public static void removePropertyChangeListener(String p, PropertyChangeListener l) {
        pcs.removePropertyChangeListener(p, l);
    }

    /** This should only be used from Option and its subclasses */
    private static void firePropertyChange(String name, Object oldValue, Object newValue) {
        pcs.firePropertyChange(name, oldValue, newValue);
    }
}
