package javax.swing.text;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ActionMapUIResource;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.plaf.TextUI;

public abstract class JTextComponent extends JComponent implements Scrollable, Accessible {

    /**
   * This class implements accessibility support for the JTextComponent class. 
   * It provides an implementation of the Java Accessibility API appropriate 
   * to menu user-interface elements.
   */
    public class AccessibleJTextComponent extends AccessibleJComponent implements AccessibleText, CaretListener, DocumentListener, AccessibleAction, AccessibleEditableText {

        private static final long serialVersionUID = 7664188944091413696L;

        /** The caret's offset. */
        int dot = 0;

        /** The current JTextComponent. */
        JTextComponent textComp = JTextComponent.this;

        /**
     * Constructs an AccessibleJTextComponent. 
     * Adds a listener to track caret change.
     */
        public AccessibleJTextComponent() {
            super();
            textComp.addCaretListener(this);
        }

        /**
     * Returns the zero-based offset of the caret. Note: The character 
     * to the right of the caret will have the same index value as the 
     * offset (the caret is between two characters).
     * 
     * @return offset of caret
     */
        public int getCaretPosition() {
            dot = textComp.getCaretPosition();
            return dot;
        }

        /**
     * Returns the portion of the text that is selected.
     * 
     * @return null if no text is selected.
     */
        public String getSelectedText() {
            return textComp.getSelectedText();
        }

        /**
     * Returns the start offset within the selected text. If there is no 
     * selection, but there is a caret, the start and end offsets will be 
     * the same. Return 0 if the text is empty, or the caret position if no selection.
     * 
     * @return index of the start of the text >= 0.
     */
        public int getSelectionStart() {
            if (getSelectedText() == null || (textComp.getText().equals(""))) return 0;
            return textComp.getSelectionStart();
        }

        /**
     * Returns the end offset within the selected text. If there is no 
     * selection, but there is a caret, the start and end offsets will 
     * be the same. Return 0 if the text is empty, or the caret position
     * if no selection.
     * 
     * @return index of the end of the text >= 0.
     */
        public int getSelectionEnd() {
            if (getSelectedText() == null || (textComp.getText().equals(""))) return 0;
            return textComp.getSelectionEnd();
        }

        /**
     * Handles caret updates (fire appropriate property change event, which are 
     * AccessibleContext.ACCESSIBLE_CARET_PROPERTY and 
     * AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY). This keeps track of 
     * the dot position internally. When the caret moves, the internal position 
     * is updated after firing the event.
     * 
     * @param e - caret event
     */
        public void caretUpdate(CaretEvent e) {
            dot = e.getDot();
        }

        /**
     * Returns the accessible state set of this component.
     *
     * @return the accessible state set of this component
     */
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet state = super.getAccessibleStateSet();
            return state;
        }

        /**
     * Returns the accessible role of this component.
     *
     * @return the accessible role of this component
     *
     * @see AccessibleRole
     */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TEXT;
        }

        /**
     * Returns the AccessibleEditableText interface for this text component.
     * 
     * @return this
     */
        public AccessibleEditableText getAccessibleEditableText() {
            return this;
        }

        /**
     * Get the AccessibleText associated with this object. In the implementation 
     * of the Java Accessibility API for this class, return this object, 
     * which is responsible for implementing the AccessibleText interface on 
     * behalf of itself.
     *
     * @return this
     *
     * @see AccessibleText
     */
        public AccessibleText getAccessibleText() {
            return this;
        }

        /**
     * Insert update. Fire appropriate property change event which 
     * is AccessibleContext.ACCESSIBLE_TEXT_PROPERTY.
     * 
     * @param e - document event
     */
        public void insertUpdate(DocumentEvent e) {
        }

        /**
     * Remove update. Fire appropriate property change event which 
     * is AccessibleContext.ACCESSIBLE_TEXT_PROPERTY.
     * 
     * @param e - document event
     */
        public void removeUpdate(DocumentEvent e) {
        }

        /**
     * Changed update. Fire appropriate property change event which 
     * is AccessibleContext.ACCESSIBLE_TEXT_PROPERTY.
     * 
     * @param e - document event
     */
        public void changedUpdate(DocumentEvent e) {
        }

        /**
     * Given a point in the coordinate system of this object, return the
     * 0-based index of the character at that point, or -1 if there is none.
     *
     * @param p the point to look at
     * @return the character index, or -1
     */
        public int getIndexAtPoint(Point p) {
            return 0;
        }

        /**
     * Determines the bounding box of the indexed character. Returns an empty
     * rectangle if the index is out of bounds.  The bounds are returned in local coordinates. 
     * If the index is invalid a null rectangle is returned. The screen coordinates returned are 
     * "unscrolled coordinates" if the JTextComponent is contained in a JScrollPane in which 
     * case the resulting rectangle should be composed with the parent coordinates. 
     * Note: the JTextComponent must have a valid size (e.g. have been added to a parent 
     * container whose ancestor container is a valid top-level window) for this method to 
     * be able to return a meaningful (non-null) value.
     *
     * @param index the 0-based character index
     * @return the bounding box, may be empty or null.
     */
        public Rectangle getCharacterBounds(int index) {
            return null;
        }

        /**
     * Return the number of characters.
     *
     * @return the character count
     */
        public int getCharCount() {
            return textComp.getText().length();
        }

        /** 
    * Returns the attributes of a character at an index, or null if the index
    * is out of bounds.
    *
    * @param index the 0-based character index
    * @return the character's attributes
    */
        public AttributeSet getCharacterAttribute(int index) {
            return null;
        }

        /**
     * Returns the section of text at the index, or null if the index or part
     * is invalid.
     *
     * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
     * @param index the 0-based character index
     * @return the selection of text at that index, or null
     */
        public String getAtIndex(int part, int index) {
            return null;
        }

        /**
     * Returns the section of text after the index, or null if the index or part
     * is invalid.
     *
     * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
     * @param index the 0-based character index
     * @return the selection of text after that index, or null
     */
        public String getAfterIndex(int part, int index) {
            return null;
        }

        /**
     * Returns the section of text before the index, or null if the index or part
     * is invalid.
     *
     * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
     * @param index the 0-based character index
     * @return the selection of text before that index, or null
     */
        public String getBeforeIndex(int part, int index) {
            return null;
        }

        /**
     * Get the number possible actions for this object, with the zeroth
     * representing the default action.
     * 
     * @return the 0-based number of actions
     */
        public int getAccessibleActionCount() {
            return 0;
        }

        /**
     * Get a description for the specified action. Returns null if out of
     * bounds.
     * 
     * @param i
     *          the action to describe, 0-based
     * @return description of the action
     */
        public String getAccessibleActionDescription(int i) {
            return super.getAccessibleDescription();
        }

        /**
     * Perform the specified action. Does nothing if out of bounds.
     *
     * @param i the action to perform, 0-based
     * @return true if the action was performed
     */
        public boolean doAccessibleAction(int i) {
            return false;
        }

        /**
     * Set the text contents to the given string.
     *
     * @param s the new text
     */
        public void setTextContents(String s) {
        }

        /**
     * Inserts the given string at the specified location.
     *
     * @param index the index for insertion
     * @param s the new text
     */
        public void insertTextAtIndex(int index, String s) {
            replaceText(index, index, s);
        }

        /**
     * Return the text between two points.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     */
        public String getTextRange(int start, int end) {
            try {
                return textComp.getText(start, end - start);
            } catch (BadLocationException ble) {
                return "";
            }
        }

        /**
     * Delete the text between two points.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     */
        public void delete(int start, int end) {
            replaceText(start, end, "");
        }

        /**
     * Cut the text between two points to the system clipboard.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     */
        public void cut(int start, int end) {
            textComp.select(start, end);
            textComp.cut();
        }

        /**
     * Paste the text from the system clipboard at the given index.
     *
     * @param start the start position
     */
        public void paste(int start) {
            textComp.setCaretPosition(start);
            textComp.paste();
        }

        /**
     * Replace the text between two points with the given string.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     * @param s the string to paste
     */
        public void replaceText(int start, int end, String s) {
            textComp.select(start, end);
            textComp.replaceSelection(s);
        }

        /**
     * Select the text between two points.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     */
        public void selectText(int start, int end) {
            textComp.select(start, end);
        }

        /**
     * Set the attributes of text between two points.
     *
     * @param start the start position, inclusive
     * @param end the end position, exclusive
     * @param s the new attribute set for the range
     */
        public void setAttributes(int start, int end, AttributeSet s) {
        }
    }

    public static class KeyBinding {

        public KeyStroke key;

        public String actionName;

        /**
     * Creates a new <code>KeyBinding</code> instance.
     *
     * @param key a <code>KeyStroke</code> value
     * @param actionName a <code>String</code> value
     */
        public KeyBinding(KeyStroke key, String actionName) {
            this.key = key;
            this.actionName = actionName;
        }
    }

    /**
   * According to <a
   * href="http://java.sun.com/products/jfc/tsc/special_report/kestrel/keybindings.html">this
   * report</a>, a pair of private classes wraps a {@link
   * javax.swing.text.Keymap} in the new {@link InputMap} / {@link
   * ActionMap} interfaces, such that old Keymap-using code can make use of
   * the new framework.
   *
   * <p>A little bit of experimentation with these classes reveals the following
   * structure:
   *
   * <ul>
   *
   * <li>KeymapWrapper extends {@link InputMap} and holds a reference to
   * the underlying {@link Keymap}.</li>
   *
   * <li>KeymapWrapper maps {@link KeyStroke} objects to {@link Action}
   * objects, by delegation to the underlying {@link Keymap}.</li>
   *
   * <li>KeymapActionMap extends {@link ActionMap} also holds a reference to
   * the underlying {@link Keymap} but only appears to use it for listing 
   * its keys. </li>
   *
   * <li>KeymapActionMap maps all {@link Action} objects to
   * <em>themselves</em>, whether they exist in the underlying {@link
   * Keymap} or not, and passes other objects to the parent {@link
   * ActionMap} for resolving.
   *
   * </ul>
   */
    private class KeymapWrapper extends InputMap {

        Keymap map;

        public KeymapWrapper(Keymap k) {
            map = k;
        }

        public int size() {
            return map.getBoundKeyStrokes().length + super.size();
        }

        public Object get(KeyStroke ks) {
            Action mapped = null;
            Keymap m = map;
            while (mapped == null && m != null) {
                mapped = m.getAction(ks);
                if (mapped == null && ks.getKeyEventType() == KeyEvent.KEY_TYPED) mapped = m.getDefaultAction();
                if (mapped == null) m = m.getResolveParent();
            }
            if (mapped == null) return super.get(ks); else return mapped;
        }

        public KeyStroke[] keys() {
            KeyStroke[] superKeys = super.keys();
            KeyStroke[] mapKeys = map.getBoundKeyStrokes();
            KeyStroke[] bothKeys = new KeyStroke[superKeys.length + mapKeys.length];
            for (int i = 0; i < superKeys.length; ++i) bothKeys[i] = superKeys[i];
            for (int i = 0; i < mapKeys.length; ++i) bothKeys[i + superKeys.length] = mapKeys[i];
            return bothKeys;
        }

        public KeyStroke[] allKeys() {
            KeyStroke[] superKeys = super.allKeys();
            KeyStroke[] mapKeys = map.getBoundKeyStrokes();
            int skl = 0;
            int mkl = 0;
            if (superKeys != null) skl = superKeys.length;
            if (mapKeys != null) mkl = mapKeys.length;
            KeyStroke[] bothKeys = new KeyStroke[skl + mkl];
            for (int i = 0; i < skl; ++i) bothKeys[i] = superKeys[i];
            for (int i = 0; i < mkl; ++i) bothKeys[i + skl] = mapKeys[i];
            return bothKeys;
        }
    }

    private class KeymapActionMap extends ActionMap {

        Keymap map;

        public KeymapActionMap(Keymap k) {
            map = k;
        }

        public Action get(Object cmd) {
            if (cmd instanceof Action) return (Action) cmd; else return super.get(cmd);
        }

        public int size() {
            return map.getBoundKeyStrokes().length + super.size();
        }

        public Object[] keys() {
            Object[] superKeys = super.keys();
            Object[] mapKeys = map.getBoundKeyStrokes();
            Object[] bothKeys = new Object[superKeys.length + mapKeys.length];
            for (int i = 0; i < superKeys.length; ++i) bothKeys[i] = superKeys[i];
            for (int i = 0; i < mapKeys.length; ++i) bothKeys[i + superKeys.length] = mapKeys[i];
            return bothKeys;
        }

        public Object[] allKeys() {
            Object[] superKeys = super.allKeys();
            Object[] mapKeys = map.getBoundKeyStrokes();
            Object[] bothKeys = new Object[superKeys.length + mapKeys.length];
            for (int i = 0; i < superKeys.length; ++i) bothKeys[i] = superKeys[i];
            for (int i = 0; i < mapKeys.length; ++i) bothKeys[i + superKeys.length] = mapKeys[i];
            return bothKeys;
        }
    }

    static class DefaultKeymap implements Keymap {

        String name;

        Keymap parent;

        Hashtable map;

        Action defaultAction;

        public DefaultKeymap(String name) {
            this.name = name;
            this.map = new Hashtable();
        }

        public void addActionForKeyStroke(KeyStroke key, Action a) {
            map.put(key, a);
        }

        /**
     * Looks up a KeyStroke either in the current map or the parent Keymap;
     * does <em>not</em> return the default action if lookup fails.
     *
     * @param key The KeyStroke to look up an Action for.
     *
     * @return The mapping for <code>key</code>, or <code>null</code>
     * if no mapping exists in this Keymap or any of its parents.
     */
        public Action getAction(KeyStroke key) {
            if (map.containsKey(key)) return (Action) map.get(key); else if (parent != null) return parent.getAction(key); else return null;
        }

        public Action[] getBoundActions() {
            Action[] ret = new Action[map.size()];
            Enumeration e = map.elements();
            int i = 0;
            while (e.hasMoreElements()) {
                ret[i++] = (Action) e.nextElement();
            }
            return ret;
        }

        public KeyStroke[] getBoundKeyStrokes() {
            KeyStroke[] ret = new KeyStroke[map.size()];
            Enumeration e = map.keys();
            int i = 0;
            while (e.hasMoreElements()) {
                ret[i++] = (KeyStroke) e.nextElement();
            }
            return ret;
        }

        public Action getDefaultAction() {
            return defaultAction;
        }

        public KeyStroke[] getKeyStrokesForAction(Action a) {
            int i = 0;
            Enumeration e = map.keys();
            while (e.hasMoreElements()) {
                if (map.get(e.nextElement()).equals(a)) ++i;
            }
            KeyStroke[] ret = new KeyStroke[i];
            i = 0;
            e = map.keys();
            while (e.hasMoreElements()) {
                KeyStroke k = (KeyStroke) e.nextElement();
                if (map.get(k).equals(a)) ret[i++] = k;
            }
            return ret;
        }

        public String getName() {
            return name;
        }

        public Keymap getResolveParent() {
            return parent;
        }

        public boolean isLocallyDefined(KeyStroke key) {
            return map.containsKey(key);
        }

        public void removeBindings() {
            map.clear();
        }

        public void removeKeyStrokeBinding(KeyStroke key) {
            map.remove(key);
        }

        public void setDefaultAction(Action a) {
            defaultAction = a;
        }

        public void setResolveParent(Keymap p) {
            parent = p;
        }
    }

    class DefaultTransferHandler extends TransferHandler {

        public boolean canImport(JComponent component, DataFlavor[] flavors) {
            JTextComponent textComponent = (JTextComponent) component;
            if (!(textComponent.isEnabled() && textComponent.isEditable() && flavors != null)) return false;
            for (int i = 0; i < flavors.length; ++i) if (flavors[i].equals(DataFlavor.stringFlavor)) return true;
            return false;
        }

        public void exportToClipboard(JComponent component, Clipboard clipboard, int action) {
            JTextComponent textComponent = (JTextComponent) component;
            int start = textComponent.getSelectionStart();
            int end = textComponent.getSelectionEnd();
            if (start == end) return;
            try {
                String data = textComponent.getDocument().getText(start, end);
                StringSelection selection = new StringSelection(data);
                clipboard.setContents(selection, null);
                if (action == MOVE) doc.remove(start, end - start);
            } catch (BadLocationException e) {
            }
        }

        public int getSourceActions() {
            return NONE;
        }

        public boolean importData(JComponent component, Transferable transferable) {
            DataFlavor flavor = null;
            DataFlavor[] flavors = transferable.getTransferDataFlavors();
            if (flavors == null) return false;
            for (int i = 0; i < flavors.length; ++i) if (flavors[i].equals(DataFlavor.stringFlavor)) flavor = flavors[i];
            if (flavor == null) return false;
            try {
                JTextComponent textComponent = (JTextComponent) component;
                String data = (String) transferable.getTransferData(flavor);
                textComponent.replaceSelection(data);
                return true;
            } catch (IOException e) {
            } catch (UnsupportedFlavorException e) {
            }
            return false;
        }
    }

    private static final long serialVersionUID = -8796518220218978795L;

    public static final String DEFAULT_KEYMAP = "default";

    public static final String FOCUS_ACCELERATOR_KEY = "focusAcceleratorKey";

    private static DefaultTransferHandler defaultTransferHandler;

    private static Hashtable keymaps = new Hashtable();

    private Keymap keymap;

    private char focusAccelerator = '\0';

    private NavigationFilter navigationFilter;

    /**
   * Get a Keymap from the global keymap table, by name.
   *
   * @param n The name of the Keymap to look up
   *
   * @return A Keymap associated with the provided name, or
   * <code>null</code> if no such Keymap exists
   *
   * @see #addKeymap
   * @see #removeKeymap
   * @see #keymaps
   */
    public static Keymap getKeymap(String n) {
        return (Keymap) keymaps.get(n);
    }

    /**
   * Remove a Keymap from the global Keymap table, by name.
   *
   * @param n The name of the Keymap to remove
   *
   * @return The keymap removed from the global table
   *
   * @see #addKeymap
   * @see #getKeymap()
   * @see #keymaps
   */
    public static Keymap removeKeymap(String n) {
        Keymap km = (Keymap) keymaps.get(n);
        keymaps.remove(n);
        return km;
    }

    /**
   * Create a new Keymap with a specific name and parent, and add the new
   * Keymap to the global keymap table. The name may be <code>null</code>,
   * in which case the new Keymap will <em>not</em> be added to the global
   * Keymap table. The parent may also be <code>null</code>, which is
   * harmless.
   * 
   * @param n The name of the new Keymap, or <code>null</code>
   * @param parent The parent of the new Keymap, or <code>null</code>
   *
   * @return The newly created Keymap
   *
   * @see #removeKeymap
   * @see #getKeymap()
   * @see #keymaps
   */
    public static Keymap addKeymap(String n, Keymap parent) {
        Keymap k = new DefaultKeymap(n);
        k.setResolveParent(parent);
        if (n != null) keymaps.put(n, k);
        return k;
    }

    /**
   * Get the current Keymap of this component.
   *
   * @return The component's current Keymap
   *
   * @see #setKeymap
   * @see #keymap
   */
    public Keymap getKeymap() {
        return keymap;
    }

    /**
   * Set the current Keymap of this component, installing appropriate
   * {@link KeymapWrapper} and {@link KeymapActionMap} objects in the
   * {@link InputMap} and {@link ActionMap} parent chains, respectively,
   * and fire a property change event with name <code>"keymap"</code>.
   *
   * @see #getKeymap()
   * @see #keymap
   */
    public void setKeymap(Keymap k) {
        KeymapWrapper kw = (k == null ? null : new KeymapWrapper(k));
        InputMap childInputMap = getInputMap(JComponent.WHEN_FOCUSED);
        if (childInputMap == null) setInputMap(JComponent.WHEN_FOCUSED, kw); else {
            while (childInputMap.getParent() != null && !(childInputMap.getParent() instanceof KeymapWrapper) && !(childInputMap.getParent() instanceof InputMapUIResource)) childInputMap = childInputMap.getParent();
            if (childInputMap.getParent() == null) childInputMap.setParent(kw); else if (childInputMap.getParent() instanceof KeymapWrapper) {
                if (kw == null) childInputMap.setParent(childInputMap.getParent().getParent()); else {
                    kw.setParent(childInputMap.getParent().getParent());
                    childInputMap.setParent(kw);
                }
            } else if (childInputMap.getParent() instanceof InputMapUIResource) {
                if (kw != null) {
                    kw.setParent(childInputMap.getParent());
                    childInputMap.setParent(kw);
                }
            }
        }
        KeymapActionMap kam = (k == null ? null : new KeymapActionMap(k));
        ActionMap childActionMap = getActionMap();
        if (childActionMap == null) setActionMap(kam); else {
            while (childActionMap.getParent() != null && !(childActionMap.getParent() instanceof KeymapActionMap) && !(childActionMap.getParent() instanceof ActionMapUIResource)) childActionMap = childActionMap.getParent();
            if (childActionMap.getParent() == null) childActionMap.setParent(kam); else if (childActionMap.getParent() instanceof KeymapActionMap) {
                if (kam == null) childActionMap.setParent(childActionMap.getParent().getParent()); else {
                    kam.setParent(childActionMap.getParent().getParent());
                    childActionMap.setParent(kam);
                }
            } else if (childActionMap.getParent() instanceof ActionMapUIResource) {
                if (kam != null) {
                    kam.setParent(childActionMap.getParent());
                    childActionMap.setParent(kam);
                }
            }
        }
        Keymap old = keymap;
        keymap = k;
        firePropertyChange("keymap", old, k);
    }

    /**
   * Resolves a set of bindings against a set of actions and inserts the
   * results into a {@link Keymap}. Specifically, for each provided binding
   * <code>b</code>, if there exists a provided action <code>a</code> such
   * that <code>a.getValue(Action.NAME) == b.ActionName</code> then an
   * entry is added to the Keymap mapping <code>b</code> to
   * <code>a</code>.
   *
   * @param map The Keymap to add new mappings to
   * @param bindings The set of bindings to add to the Keymap
   * @param actions The set of actions to resolve binding names against
   *
   * @see Action#NAME
   * @see Action#getValue
   * @see KeyBinding#actionName
   */
    public static void loadKeymap(Keymap map, JTextComponent.KeyBinding[] bindings, Action[] actions) {
        Hashtable acts = new Hashtable(actions.length);
        for (int i = 0; i < actions.length; ++i) acts.put(actions[i].getValue(Action.NAME), actions[i]);
        for (int i = 0; i < bindings.length; ++i) if (acts.containsKey(bindings[i].actionName)) map.addActionForKeyStroke(bindings[i].key, (Action) acts.get(bindings[i].actionName));
    }

    /**
   * Returns the set of available Actions this component's associated
   * editor can run.  Equivalent to calling
   * <code>getUI().getEditorKit().getActions()</code>. This set of Actions
   * is a reasonable value to provide as a parameter to {@link
   * #loadKeymap}, when resolving a set of {@link KeyBinding} objects
   * against this component.
   *
   * @return The set of available Actions on this component's {@link EditorKit}
   *
   * @see TextUI#getEditorKit
   * @see EditorKit#getActions()
   */
    public Action[] getActions() {
        return getUI().getEditorKit(this).getActions();
    }

    Document doc;

    Caret caret;

    boolean editable;

    private Highlighter highlighter;

    private Color caretColor;

    private Color disabledTextColor;

    private Color selectedTextColor;

    private Color selectionColor;

    private Insets margin;

    private boolean dragEnabled;

    /**
   * Creates a new <code>JTextComponent</code> instance.
   */
    public JTextComponent() {
        Keymap defkeymap = getKeymap(DEFAULT_KEYMAP);
        if (defkeymap == null) {
            defkeymap = addKeymap(DEFAULT_KEYMAP, null);
            defkeymap.setDefaultAction(new DefaultEditorKit.DefaultKeyTypedAction());
        }
        setFocusable(true);
        setEditable(true);
        enableEvents(AWTEvent.KEY_EVENT_MASK);
        updateUI();
    }

    public void setDocument(Document newDoc) {
        Document oldDoc = doc;
        doc = newDoc;
        firePropertyChange("document", oldDoc, newDoc);
        revalidate();
        repaint();
    }

    public Document getDocument() {
        return doc;
    }

    /**
   * Get the <code>AccessibleContext</code> of this object.
   *
   * @return an <code>AccessibleContext</code> object
   */
    public AccessibleContext getAccessibleContext() {
        return new AccessibleJTextComponent();
    }

    public void setMargin(Insets m) {
        margin = m;
    }

    public Insets getMargin() {
        return margin;
    }

    public void setText(String text) {
        try {
            if (doc instanceof AbstractDocument) ((AbstractDocument) doc).replace(0, doc.getLength(), text, null); else {
                doc.remove(0, doc.getLength());
                doc.insertString(0, text, null);
            }
        } catch (BadLocationException e) {
        }
    }

    /**
   * Retrieves the current text in this text document.
   *
   * @return the text
   *
   * @exception NullPointerException if the underlaying document is null
   */
    public String getText() {
        if (doc == null) return null;
        try {
            return doc.getText(0, doc.getLength());
        } catch (BadLocationException e) {
            return "";
        }
    }

    /**
   * Retrieves a part of the current text in this document.
   *
   * @param offset the postion of the first character
   * @param length the length of the text to retrieve
   *
   * @return the text
   *
   * @exception BadLocationException if arguments do not hold pre-conditions
   */
    public String getText(int offset, int length) throws BadLocationException {
        return getDocument().getText(offset, length);
    }

    /**
   * Retrieves the currently selected text in this text document.
   *
   * @return the selected text
   *
   * @exception NullPointerException if the underlaying document is null
   */
    public String getSelectedText() {
        try {
            return doc.getText(getSelectionStart(), getSelectionEnd());
        } catch (BadLocationException e) {
            return null;
        }
    }

    /**
   * Returns a string that specifies the name of the Look and Feel class
   * that renders this component.
   *
   * @return the string "TextComponentUI"
   */
    public String getUIClassID() {
        return "TextComponentUI";
    }

    /**
   * Returns a string representation of this JTextComponent.
   */
    protected String paramString() {
        return super.paramString();
    }

    /**
   * This method returns the label's UI delegate.
   *
   * @return The label's UI delegate.
   */
    public TextUI getUI() {
        return (TextUI) ui;
    }

    /**
   * This method sets the label's UI delegate.
   *
   * @param newUI The label's UI delegate.
   */
    public void setUI(TextUI newUI) {
        super.setUI(newUI);
    }

    /**
   * This method resets the label's UI delegate to the default UI for the
   * current look and feel.
   */
    public void updateUI() {
        setUI((TextUI) UIManager.getUI(this));
    }

    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    public int getScrollableUnitIncrement(Rectangle visible, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) return visible.width / 10; else if (orientation == SwingConstants.VERTICAL) return visible.height / 10; else throw new IllegalArgumentException("orientation must be either " + "javax.swing.SwingConstants.VERTICAL " + "or " + "javax.swing.SwingConstants.HORIZONTAL");
    }

    public int getScrollableBlockIncrement(Rectangle visible, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) return visible.width; else if (orientation == SwingConstants.VERTICAL) return visible.height; else throw new IllegalArgumentException("orientation must be either " + "javax.swing.SwingConstants.VERTICAL " + "or " + "javax.swing.SwingConstants.HORIZONTAL");
    }

    /**
   * Checks whether this text component it editable.
   *
   * @return true if editable, false otherwise
   */
    public boolean isEditable() {
        return editable;
    }

    /**
   * Enables/disabled this text component's editability.
   *
   * @param newValue true to make it editable, false otherwise.
   */
    public void setEditable(boolean newValue) {
        if (editable == newValue) return;
        boolean oldValue = editable;
        editable = newValue;
        firePropertyChange("editable", oldValue, newValue);
    }

    /**
   * The <code>Caret</code> object used in this text component.
   *
   * @return the caret object
   */
    public Caret getCaret() {
        return caret;
    }

    /**
   * Sets a new <code>Caret</code> for this text component.
   *
   * @param newCaret the new <code>Caret</code> to set
   */
    public void setCaret(Caret newCaret) {
        if (caret != null) caret.deinstall(this);
        Caret oldCaret = caret;
        caret = newCaret;
        if (caret != null) caret.install(this);
        firePropertyChange("caret", oldCaret, newCaret);
    }

    public Color getCaretColor() {
        return caretColor;
    }

    public void setCaretColor(Color newColor) {
        Color oldCaretColor = caretColor;
        caretColor = newColor;
        firePropertyChange("caretColor", oldCaretColor, newColor);
    }

    public Color getDisabledTextColor() {
        return disabledTextColor;
    }

    public void setDisabledTextColor(Color newColor) {
        Color oldColor = disabledTextColor;
        disabledTextColor = newColor;
        firePropertyChange("disabledTextColor", oldColor, newColor);
    }

    public Color getSelectedTextColor() {
        return selectedTextColor;
    }

    public void setSelectedTextColor(Color newColor) {
        Color oldColor = selectedTextColor;
        selectedTextColor = newColor;
        firePropertyChange("selectedTextColor", oldColor, newColor);
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color newColor) {
        Color oldColor = selectionColor;
        selectionColor = newColor;
        firePropertyChange("selectionColor", oldColor, newColor);
    }

    /**
   * Retrisves the current caret position.
   *
   * @return the current position
   */
    public int getCaretPosition() {
        return caret.getDot();
    }

    /**
   * Sets the caret to a new position.
   *
   * @param position the new position
   */
    public void setCaretPosition(int position) {
        if (doc == null) return;
        if (position < 0 || position > doc.getLength()) throw new IllegalArgumentException();
        caret.setDot(position);
    }

    /**
   * Moves the caret to a given position. This selects the text between
   * the old and the new position of the caret.
   */
    public void moveCaretPosition(int position) {
        if (doc == null) return;
        if (position < 0 || position > doc.getLength()) throw new IllegalArgumentException();
        caret.moveDot(position);
    }

    public Highlighter getHighlighter() {
        return highlighter;
    }

    public void setHighlighter(Highlighter newHighlighter) {
        if (highlighter != null) highlighter.deinstall(this);
        Highlighter oldHighlighter = highlighter;
        highlighter = newHighlighter;
        if (highlighter != null) highlighter.install(this);
        firePropertyChange("highlighter", oldHighlighter, newHighlighter);
    }

    /**
   * Returns the start postion of the currently selected text.
   *
   * @return the start postion
   */
    public int getSelectionStart() {
        return Math.min(caret.getDot(), caret.getMark());
    }

    /**
   * Selects the text from the given postion to the selection end position.
   *
   * @param start the start positon of the selected text.
   */
    public void setSelectionStart(int start) {
        select(start, getSelectionEnd());
    }

    /**
   * Returns the end postion of the currently selected text.
   *
   * @return the end postion
   */
    public int getSelectionEnd() {
        return Math.max(caret.getDot(), caret.getMark());
    }

    /**
   * Selects the text from the selection start postion to the given position.
   *
   * @param end the end positon of the selected text.
   */
    public void setSelectionEnd(int end) {
        select(getSelectionStart(), end);
    }

    /**
   * Selects a part of the content of the text component.
   *
   * @param start the start position of the selected text
   * @param end the end position of the selected text
   */
    public void select(int start, int end) {
        int length = doc.getLength();
        start = Math.max(start, 0);
        start = Math.min(start, length);
        end = Math.max(end, start);
        end = Math.min(end, length);
        setCaretPosition(start);
        moveCaretPosition(end);
    }

    /**
   * Selects the whole content of the text component.
   */
    public void selectAll() {
        select(0, doc.getLength());
    }

    public synchronized void replaceSelection(String content) {
        int dot = caret.getDot();
        int mark = caret.getMark();
        if (content == null) {
            caret.setDot(dot);
            return;
        }
        try {
            int start = getSelectionStart();
            int end = getSelectionEnd();
            if (dot != mark) doc.remove(start, end - start);
            doc.insertString(start, content, null);
            setCaretPosition(start + content.length());
        } catch (BadLocationException e) {
        }
    }

    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) return ((JViewport) getParent()).getHeight() > getPreferredSize().height;
        return false;
    }

    public boolean getScrollableTracksViewportWidth() {
        if (getParent() instanceof JViewport) return ((JViewport) getParent()).getWidth() > getPreferredSize().width;
        return false;
    }

    /**
   * Adds a <code>CaretListener</code> object to this text component.
   *
   * @param listener the listener to add
   */
    public void addCaretListener(CaretListener listener) {
        listenerList.add(CaretListener.class, listener);
    }

    /**
   * Removed a <code>CaretListener</code> object from this text component.
   *
   * @param listener the listener to remove
   */
    public void removeCaretListener(CaretListener listener) {
        listenerList.remove(CaretListener.class, listener);
    }

    /**
   * Returns all added <code>CaretListener</code> objects.
   *
   * @return an array of listeners
   */
    public CaretListener[] getCaretListeners() {
        return (CaretListener[]) getListeners(CaretListener.class);
    }

    /**
   * Notifies all registered <code>CaretListener</code> objects that the caret
   * was updated.
   *
   * @param event the event to send
   */
    protected void fireCaretUpdate(CaretEvent event) {
        CaretListener[] listeners = getCaretListeners();
        for (int index = 0; index < listeners.length; ++index) listeners[index].caretUpdate(event);
    }

    /**
   * Adds an <code>InputListener</code> object to this text component.
   *
   * @param listener the listener to add
   */
    public void addInputMethodListener(InputMethodListener listener) {
        listenerList.add(InputMethodListener.class, listener);
    }

    /**
   * Removes an <code>InputListener</code> object from this text component.
   *
   * @param listener the listener to remove
   */
    public void removeInputMethodListener(InputMethodListener listener) {
        listenerList.remove(InputMethodListener.class, listener);
    }

    /**
   * Returns all added <code>InputMethodListener</code> objects.
   *
   * @return an array of listeners
   */
    public InputMethodListener[] getInputMethodListeners() {
        return (InputMethodListener[]) getListeners(InputMethodListener.class);
    }

    public Rectangle modelToView(int position) throws BadLocationException {
        return getUI().modelToView(this, position);
    }

    public boolean getDragEnabled() {
        return dragEnabled;
    }

    public void setDragEnabled(boolean enabled) {
        dragEnabled = enabled;
    }

    public int viewToModel(Point pt) {
        return getUI().viewToModel(this, pt);
    }

    public void copy() {
        doTransferAction("copy", TransferHandler.getCopyAction());
    }

    public void cut() {
        doTransferAction("cut", TransferHandler.getCutAction());
    }

    public void paste() {
        doTransferAction("paste", TransferHandler.getPasteAction());
    }

    private void doTransferAction(String name, Action action) {
        if (getTransferHandler() == null) {
            if (defaultTransferHandler == null) defaultTransferHandler = new DefaultTransferHandler();
            setTransferHandler(defaultTransferHandler);
        }
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action.getValue(Action.NAME).toString());
        action.actionPerformed(event);
    }

    public void setFocusAccelerator(char newKey) {
        if (focusAccelerator == newKey) return;
        char oldKey = focusAccelerator;
        focusAccelerator = newKey;
        firePropertyChange(FOCUS_ACCELERATOR_KEY, oldKey, newKey);
    }

    public char getFocusAccelerator() {
        return focusAccelerator;
    }

    /**
   * @since 1.4
   */
    public NavigationFilter getNavigationFilter() {
        return navigationFilter;
    }

    /**
   * @since 1.4
   */
    public void setNavigationFilter(NavigationFilter filter) {
        navigationFilter = filter;
    }

    /**
   * Read and set the content this component. If not overridden, the
   * method reads the component content as a plain text.
   *
   * The second parameter of this method describes the input stream. It can
   * be String, URL, File and so on. If not null, this object is added to
   * the properties of the associated document under the key
   * {@link Document#StreamDescriptionProperty}.
   *
   * @param input an input stream to read from.
   * @param streamDescription an object, describing the stream.
   *
   * @throws IOException if the reader throws it.
   *
   * @see #getDocument()
   * @see Document#getProperty(Object)
   */
    public void read(Reader input, Object streamDescription) throws IOException {
        if (streamDescription != null) {
            Document d = getDocument();
            if (d != null) d.putProperty(Document.StreamDescriptionProperty, streamDescription);
        }
        StringBuffer b = new StringBuffer();
        int c;
        while ((c = input.read()) >= 0) b.append((char) c);
        setText(b.toString());
    }

    /**
   * Write the content of this component to the given stream. If not
   * overridden, the method writes the component content as a plain text.
   *
   * @param output the writer to write into.
   *
   * @throws IOException if the writer throws it.
   */
    public void write(Writer output) throws IOException {
        output.write(getText());
    }

    /**
   * Returns the tooltip text for this text component for the given mouse
   * event. This forwards the call to
   * {@link TextUI#getToolTipText(JTextComponent, Point)}.
   *
   * @param ev the mouse event
   *
   * @return the tooltip text for this text component for the given mouse
   *         event
   */
    public String getToolTipText(MouseEvent ev) {
        return getUI().getToolTipText(this, ev.getPoint());
    }
}
