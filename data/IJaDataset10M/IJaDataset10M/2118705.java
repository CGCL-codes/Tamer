package net.sourceforge.processdash.ui.lib;

import java.awt.Dimension;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import javax.swing.ButtonModel;
import javax.swing.JCheckBox;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * General-purpose class for saving and restoring the user-preferred state of
 * common user interface components.
 */
public class GuiPrefs {

    private Preferences prefsBase;

    private Map<String, RegisteredItem> registeredItems;

    /**
     * Create a new object to read and write user-preferred component state from
     * a particular location within the user preferences.
     * 
     * @param preferencesPath
     *            a list of objects directing where data should be stored. Each
     *            object in this path can be one of:
     *            <ul>
     * 
     *            <li>A {@link Preferences} object, which should be used as a
     *            starting point for storage. If no {@link Preferences} object
     *            is included in the preferencesPath, the search will begin at
     *            the "user root"</li>
     * 
     *            <li>A {@link Class}: the fully-qualified name of the class
     *            will be used to select a node underneath the current
     *            {@link Preferences} node</li>
     * 
     *            <li>Any other object: the toString method will be called and
     *            the resulting name will be used to select a node underneath
     *            the current {@link Preferences} node</li>
     * 
     *            </ul>
     */
    public GuiPrefs(Object... preferencesPath) {
        Preferences prefsBase = Preferences.userRoot();
        for (Object pathElem : preferencesPath) {
            if (pathElem instanceof Preferences) {
                prefsBase = (Preferences) pathElem;
            } else {
                if (pathElem instanceof Class) pathElem = ((Class) pathElem).getName().replace('.', '/');
                if (pathElem != null) prefsBase = prefsBase.node(pathElem.toString());
            }
        }
        this.prefsBase = prefsBase;
        this.registeredItems = new HashMap<String, RegisteredItem>();
    }

    /**
     * Save the current state of all user interface controls that were
     * previously loaded by this object.
     */
    public void saveAll() {
        for (RegisteredItem item : registeredItems.values()) item.save();
    }

    /**
     * Reset a group of user interface controls, removing any end-user
     * customizations and returning them to the program-default state that they
     * had before the load() method was called.
     * 
     * @param ids
     *            a list of ids that were previously passed to <code>load</code>
     *            methods in this object.
     */
    public void reset(String... ids) {
        for (String id : ids) {
            RegisteredItem item = registeredItems.get(id);
            if (item != null) item.reset();
        }
    }

    /**
     * Register a window so its width and height will be saved when the
     * {@link #saveAll()} method is called, and restore any width and height
     * settings that were saved for this window in the past.
     * 
     * @param windowId
     *            a unique ID for this window; if a {@link GuiPrefs} object is
     *            managing state for several windows, each one should have a
     *            different id.
     * @param window
     *            the window to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(String windowId, Window window) {
        return load(new RegisteredWindow(windowId, window));
    }

    /**
     * Register a window so its width and height will be saved when the
     * {@link #saveAll()} method is called, and restore any width and height
     * settings that were saved for this window in the past.
     * 
     * This variant can be used if a particular {@link GuiPrefs} object is only
     * managing state for one window.  The window will be registered with the
     * generic id "window".
     * 
     * @param window the window to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(Window window) {
        return load("window", window);
    }

    /**
     * Register a table so the column sizes and order will be saved when the
     * {@link #saveAll()} method is called, and restore any column state that
     * was saved for this table in the past.
     * 
     * If a {@link JTableColumnVisibilityButton} or
     * {@link JTableColumnVisibilityAction} has <b>already</b> been created for
     * this table, this method will also save and restore the visibility of the
     * various columns. Therefore, if this behavior is desired, it is important
     * to construct the visibility helper before calling this method.
     * 
     * @param tableId
     *            a unique ID for this table; if a {@link GuiPrefs} object is
     *            managing state for several tables, each one should have a
     *            different id.
     * @param table
     *            the table to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(String tableId, JTable table) {
        return load(new RegisteredTable(tableId, table));
    }

    /**
     * Register a table so the column sizes and order will be saved when the
     * {@link #saveAll()} method is called, and restore any column state that
     * was saved for this table in the past.
     * 
     * If a {@link JTableColumnVisibilityButton} or
     * {@link JTableColumnVisibilityAction} has <b>already</b> been created for
     * this table, this method will also save and restore the visibility of the
     * various columns. Therefore, if this behavior is desired, it is important
     * to construct the visibility helper before calling this method.
     * 
     * This variant can be used if a particular {@link GuiPrefs} object is only
     * managing state for one table. The table will be registered with the
     * generic id "table".
     * 
     * @param table
     *            the table to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(JTable table) {
        return load("table", table);
    }

    /**
     * Register a {@link ButtonModel} so its "selected" state will be saved when
     * the {@link #saveAll()} method is called, and restore any state that was
     * saved for this button model in the past.
     * 
     * @param buttonId
     *            a unique ID for this button model; if a {@link GuiPrefs}
     *            object is managing state for several buttons, each one should
     *            have a different id.
     * @param buttonModel
     *            the button model to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(String buttonId, ButtonModel buttonModel) {
        return load(new RegisteredButtonModel(buttonId, buttonModel));
    }

    /**
     * Register a {@link JCheckBox} so its "checked" state will be saved when
     * the {@link #saveAll()} method is called, and restore any state that was
     * saved for this check box in the past.
     * 
     * @param checkBoxId
     *            a unique ID for this check box; if a {@link GuiPrefs} object
     *            is managing state for several check boxes, each one should
     *            have a different id.
     * @param checkBox
     *            the check box to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(String checkBoxId, JCheckBox checkBox) {
        return load(checkBoxId, checkBox.getModel());
    }

    /**
     * Register a {@link JCheckBox} so its "checked" state will be saved when
     * the {@link #saveAll()} method is called, and restore any state that was
     * saved for this check box in the past.
     * 
     * This variant can be used if a particular {@link GuiPrefs} object is only
     * managing state for one check box. The check box will be registered with
     * the generic id "checkbox".
     * 
     * @param checkBox
     *            the check box to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(JCheckBox checkBox) {
        return load("checkbox", checkBox);
    }

    /**
     * Register a {@link JSplitPane} so its location will be saved when the
     * {@link #saveAll()} method is called, and restore any location that was
     * saved for this split pane in the past.
     * 
     * @param splitPaneId
     *            a unique ID for this split pane; if a {@link GuiPrefs} object
     *            is managing state for several split panes, each one should
     *            have a different id.
     * @param splitPane
     *            the split pane to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(String splitPaneId, JSplitPane splitPane) {
        return load(new RegisteredSplitPane(splitPaneId, splitPane));
    }

    /**
     * Register a {@link JSplitPane} so its location will be saved when the
     * {@link #saveAll()} method is called, and restore any location that was
     * saved for this split pane in the past.
     * 
     * This variant can be used if a particular {@link GuiPrefs} object is only
     * managing state for one split pane. The split pane will be registered with
     * the generic id "splitpane".
     * 
     * @param splitPane
     *            the split pane to register
     * @return true if any user customizations were loaded, false if none were
     *         found
     */
    public boolean load(JSplitPane splitPane) {
        return load("splitpane", splitPane);
    }

    private boolean load(RegisteredItem item) {
        try {
            return item.load();
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    private abstract class RegisteredItem {

        Preferences prefs;

        public RegisteredItem(String id) {
            this.prefs = prefsBase.node(id);
            registeredItems.put(id, this);
        }

        abstract boolean load();

        abstract void reset();

        abstract void save();

        int getInt(String attr) {
            return prefs.getInt(attr, -1);
        }

        void putInt(String attr, int val) {
            prefs.putInt(attr, val);
        }
    }

    private class RegisteredWindow extends RegisteredItem {

        Window w;

        Dimension orig;

        public RegisteredWindow(String id, Window w) {
            super(id);
            this.w = w;
            this.orig = w.getSize();
        }

        @Override
        boolean load() {
            int width = getInt("width");
            int height = getInt("height");
            if (width > 0 && height > 0) {
                w.setSize(width, height);
                return true;
            } else {
                return false;
            }
        }

        @Override
        void reset() {
            w.setSize(orig);
        }

        @Override
        void save() {
            putInt("width", w.getWidth());
            putInt("height", w.getHeight());
        }
    }

    private class RegisteredTable extends RegisteredItem {

        TableColumnModel cols;

        TableColumnModel orig;

        boolean reorder;

        JTableColumnVisibilityAction visibility;

        public RegisteredTable(String id, JTable table) {
            super(id);
            this.cols = table.getColumnModel();
            this.orig = TableUtils.cloneTableColumnModel(this.cols);
            this.reorder = table.getTableHeader().getReorderingAllowed();
            this.visibility = JTableColumnVisibilityAction.getForTable(table);
        }

        @Override
        boolean load() {
            List<ColPos> columnPositions = new ArrayList<ColPos>();
            int lastColumnPos = 0;
            boolean foundInfo = false;
            if (visibility != null) visibility.loadColumnVisibility(prefs);
            for (int i = 0; i < cols.getColumnCount(); i++) {
                TableColumn column = cols.getColumn(i);
                Object columnId = column.getIdentifier();
                int width = getInt(columnId + ".width");
                if (width > 0) {
                    column.setPreferredWidth(width);
                    foundInfo = true;
                }
                int colPos = getInt(columnId + ".pos");
                if (colPos == -1) colPos = lastColumnPos;
                columnPositions.add(new ColPos(columnId, colPos));
                lastColumnPos = colPos;
            }
            if (reorder) {
                Collections.sort(columnPositions);
                for (int pos = 0; pos < columnPositions.size(); pos++) {
                    Object id = columnPositions.get(pos).id;
                    int currentIdx = cols.getColumnIndex(id);
                    if (currentIdx != pos) cols.moveColumn(currentIdx, pos);
                }
            }
            return foundInfo;
        }

        @Override
        void reset() {
            for (int i = cols.getColumnCount(); i-- > 0; ) cols.removeColumn(cols.getColumn(i));
            for (int i = 0; i < orig.getColumnCount(); i++) cols.addColumn(TableUtils.cloneTableColumn(orig.getColumn(i)));
        }

        @Override
        void save() {
            for (int i = 0; i < cols.getColumnCount(); i++) {
                TableColumn column = cols.getColumn(i);
                Object columnId = column.getIdentifier();
                if (reorder) putInt(columnId + ".pos", i);
                if (column.getWidth() > 0) putInt(columnId + ".width", column.getWidth());
            }
            if (visibility != null) visibility.saveColumnVisibility(prefs, orig);
        }

        private class ColPos implements Comparable<ColPos> {

            Object id;

            int pos;

            public ColPos(Object id, int pos) {
                this.id = id;
                this.pos = pos;
            }

            public int compareTo(ColPos that) {
                return this.pos - that.pos;
            }
        }
    }

    private class RegisteredButtonModel extends RegisteredItem {

        ButtonModel buttonModel;

        boolean orig;

        public RegisteredButtonModel(String id, ButtonModel buttonModel) {
            super(id);
            this.buttonModel = buttonModel;
            this.orig = buttonModel.isSelected();
        }

        @Override
        boolean load() {
            int pref = getInt("selected");
            if (pref == -1) {
                return false;
            } else {
                buttonModel.setSelected(pref == 1);
                return true;
            }
        }

        @Override
        void reset() {
            buttonModel.setSelected(orig);
        }

        @Override
        void save() {
            putInt("selected", buttonModel.isSelected() ? 1 : 0);
        }
    }

    private class RegisteredSplitPane extends RegisteredItem {

        JSplitPane splitPane;

        int orig;

        public RegisteredSplitPane(String id, JSplitPane splitPane) {
            super(id);
            this.splitPane = splitPane;
            this.orig = splitPane.getDividerLocation();
        }

        @Override
        boolean load() {
            int location = getInt("position");
            if (location > 0) {
                splitPane.setDividerLocation(location);
                return true;
            } else {
                return false;
            }
        }

        @Override
        void reset() {
            splitPane.setDividerLocation(orig);
        }

        @Override
        void save() {
            putInt("position", splitPane.getDividerLocation());
        }
    }
}
