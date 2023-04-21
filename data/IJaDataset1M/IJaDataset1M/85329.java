package jpicedt.ui.dialog;

import jpicedt.JPicEdt;
import jpicedt.Localizer;
import jpicedt.graphic.toolkit.AbstractCustomizer;
import jpicedt.ui.MDIManager;
import jpicedt.graphic.toolkit.ActionLocalizer;
import jpicedt.graphic.PEToolKit;
import java.lang.reflect.Field;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import static jpicedt.Log.*;
import static jpicedt.Localizer.*;

/**
 * A panel for editing keyboard shortcuts (i.e. Swing's ACCELERATORS).
 * @author Sylvain Reynal
 * @since jPicEdt 1.4
 * @version $Id: ShortcutsCustomizer.java,v 1.9 2012/03/11 17:38:12 vincentb1 Exp $
 */
public class ShortcutsCustomizer extends AbstractCustomizer implements ActionListener {

    private JTable table;

    private ShortcutsTableModel model;

    private Properties preferences;

    private JMenuItem[] miArray;

    private String[] shortCuts;

    /**
	 * Construct a new panel for editing shortcuts.
	 */
    public ShortcutsCustomizer(Properties preferences) {
        this.preferences = preferences;
        load();
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), localize("preferences.Shortcuts")));
        table = new ShortcutsJTable();
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.NORTH);
    }

    /**
	 * Load widgets display content with default values.
	 */
    public void loadDefault() {
        this.shortCuts = new String[miArray.length];
        ActionLocalizer localizer = jpicedt.Localizer.currentLocalizer().getActionLocalizer();
        for (int i = 0; i < miArray.length; i++) {
            Action a = miArray[i].getAction();
            KeyStroke ks = null;
            if (a != null && a.getValue(Action.ACTION_COMMAND_KEY) != null) ks = localizer.getActionAccelerator((String) (a.getValue(Action.ACTION_COMMAND_KEY)));
            shortCuts[i] = keyStrokeToString(ks);
        }
        model.fireTableDataChanged();
    }

    /**
	 * Load widgets value from the <code>Properties</code> object.
	 */
    public void load() {
        this.miArray = JPicEdt.getMDIManager().getMenuItems();
        this.shortCuts = new String[miArray.length];
        for (int i = 0; i < miArray.length; i++) {
            shortCuts[i] = keyStrokeToString(miArray[i].getAccelerator());
        }
    }

    /**
	 * Store current widgets value, presumably to the <code>Properties</code> object given in the constructor.
	 */
    public void store() {
        ActionLocalizer localizer = jpicedt.Localizer.currentLocalizer().getActionLocalizer();
        for (int i = 0; i < miArray.length; i++) {
            JMenuItem mi = miArray[i];
            Action a = miArray[i].getAction();
            if (a == null) continue;
            String actionName = (String) a.getValue(Action.ACTION_COMMAND_KEY);
            if (actionName == null) continue;
            if (!(shortCuts[i].equals(""))) {
                KeyStroke defaultKs = localizer.getActionAccelerator(actionName);
                KeyStroke ks = KeyStroke.getKeyStroke(shortCuts[i]);
                if (defaultKs != null && defaultKs.equals(ks)) preferences.remove(actionName); else preferences.setProperty(actionName, shortCuts[i]);
                mi.setAccelerator(ks);
            } else {
                KeyStroke defaultKs = localizer.getActionAccelerator(actionName);
                if (defaultKs == null) preferences.remove(actionName); else preferences.setProperty(actionName, "none");
                mi.setAccelerator(null);
            }
        }
    }

    /**
	 * Returns the panel title, used e.g. for Border or Tabpane title.
	 */
    public String getTitle() {
        return localize("preferences.Shortcuts");
    }

    /**
	 * @return the Icon associated with this panel, used e.g. for TabbedPane decoration
	 */
    public Icon getIcon() {
        return null;
    }

    /**
	 * @return the tooltip string associated with this panel
	 */
    public String getTooltip() {
        return localize("preferences.Shortcuts.tooltip");
    }

    /**
	 * Open a directory browser when an action occurs on the "Browse" button.
	 */
    public void actionPerformed(ActionEvent e) {
    }

    private String keyStrokeToString(KeyStroke ks) {
        if (ks == null) return "";
        int keyCode = ks.getKeyCode();
        int modifier = ks.getModifiers();
        String str = "";
        str += ((modifier & InputEvent.CTRL_MASK) != 0 ? "control " : "");
        str += ((modifier & InputEvent.ALT_MASK) != 0 ? "alt " : "");
        str += ((modifier & InputEvent.META_MASK) != 0 ? "meta " : "");
        str += ((modifier & InputEvent.SHIFT_MASK) != 0 ? "shift " : "");
        str += getSymbolicName(keyCode);
        return str;
    }

    private String getModifierString(InputEvent evt) {
        StringBuffer buf = new StringBuffer();
        if (evt.isControlDown()) buf.append("control ");
        if (evt.isAltDown()) buf.append("alt ");
        if (evt.isMetaDown()) buf.append("meta ");
        if (evt.isShiftDown()) buf.append("shift ");
        return buf.toString();
    }

    /**
	 * Adapted from jEdit 4.1
	 * @author Slava Pastov
	 */
    private String getSymbolicName(int keyCode) {
        if (keyCode == KeyEvent.VK_UNDEFINED) return null;
        if (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) return String.valueOf(Character.toUpperCase((char) keyCode));
        try {
            Field[] fields = KeyEvent.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                String name = field.getName();
                if (name.startsWith("VK_") && field.getInt(null) == keyCode) {
                    return name.substring(3);
                }
            }
        } catch (Exception e) {
            error(e.toString());
        }
        return null;
    }

    /** ibid. */
    class InputPane extends JTextField {

        /**
		 * Makes the tab key work in Java 1.4.
		 * @since jEdit 3.2pre4
		 */
        public boolean getFocusTraversalKeysEnabled() {
            return false;
        }

        protected void processKeyEvent(KeyEvent evt) {
            evt.consume();
            if (evt.getID() != KeyEvent.KEY_PRESSED) return;
            KeyStroke ks = KeyStroke.getKeyStrokeForEvent(evt);
            String s = keyStrokeToString(ks);
            setText(s);
        }

        protected void _processKeyEvent(KeyEvent evt) {
            evt.consume();
            StringBuffer keyString = new StringBuffer(getText());
            if (getDocument().getLength() != 0) keyString.append(' ');
            if (evt.getID() == KeyEvent.KEY_TYPED) {
                if (!Character.isLetterOrDigit(evt.getKeyChar()) && !Character.isUpperCase(evt.getKeyChar())) return;
                keyString.append(evt.getKeyChar());
            } else if (evt.getID() == KeyEvent.KEY_PRESSED) {
                String modifiers = getModifierString(evt);
                if (modifiers.length() != 0) {
                    keyString.append(modifiers);
                    keyString.append(' ');
                }
                int keyCode = evt.getKeyCode();
                if (((keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z) || (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9)) && modifiers.length() == 0) {
                    return;
                }
                String symbolicName = getSymbolicName(keyCode);
                if (symbolicName == null) return;
                keyString.append(symbolicName);
            } else if (evt.getID() == KeyEvent.KEY_RELEASED) return;
            setText(keyString.toString());
        }
    }

    class ShortcutsJTable extends JTable {

        private final ShortcutsCellEditor cellEditor = new ShortcutsCellEditor();

        ShortcutsJTable() {
            super();
            setModel(model = new ShortcutsTableModel());
        }

        public TableCellEditor getCellEditor(int row, int col) {
            if (col == 1) return cellEditor; else return super.getCellEditor(row, col);
        }
    }

    /**
	 * a class that specifies the methods the JTable will use to interrogate a tabular data model.
	 */
    class ShortcutsTableModel extends AbstractTableModel {

        /**
		 * Returns the name of the column at columnIndex.
		 */
        public String getColumnName(int colIndex) {
            switch(colIndex) {
                case 0:
                    return "Command";
                case 1:
                    return "Shortcut";
                default:
                    return "";
            }
        }

        /**
		 * Returns the number of columns in the model.
		 */
        public int getColumnCount() {
            return 2;
        }

        /**
		 * Returns the number of rows in the model.
		 */
        public int getRowCount() {
            return miArray.length;
        }

        /**
		 * Returns true if the cell at rowIndex and columnIndex is editable.
		 */
        public boolean isCellEditable(int rowIndex, int colIndex) {
            if (colIndex == 0) return false; else return true;
        }

        /**
		 * Called when the associated JTable wants to know what to display at
		 * columnIndex and rowIndex.
		 */
        public Object getValueAt(int rowIndex, int colIndex) {
            switch(colIndex) {
                case 0:
                    Action a = miArray[rowIndex].getAction();
                    if (a != null && a.getValue(Action.ACTION_COMMAND_KEY) != null) return a.getValue(Action.NAME); else return "";
                case 1:
                    return shortCuts[rowIndex];
                default:
                    return null;
            }
        }

        /**
		 * Called when a user entered a new value in the cell at columnIndex and
		 * rowIndex to value.
		 */
        public void setValueAt(Object value, int rowIndex, int colIndex) {
            switch(colIndex) {
                case 1:
                    shortCuts[rowIndex] = (String) value;
                    break;
                default:
                    return;
            }
        }
    }

    /**
	 * ShortCut's cell editor is based on InputPane
	 */
    class ShortcutsCellEditor_ extends DefaultCellEditor {

        private InputPane shortcut;

        ShortcutsCellEditor_(InputPane shortcut) {
            super(shortcut);
            this.shortcut = shortcut;
        }

        public Object getCellEditorValue() {
            return shortcut.getText();
        }
    }

    /**
	 *
	 */
    class ShortcutsCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        protected InputPane textField;

        protected JButton clearButton, okButton;

        protected JPanel pane;

        /**
		 * Constructs a <code>DefaultCellEditor</code> that uses a text field.
		 *
		 * @param textField  a <code>JTextField</code> object
		 */
        public ShortcutsCellEditor() {
            textField = new InputPane();
            textField.setColumns(15);
            clearButton = new JButton(PEToolKit.createImageIcon("EditTextClear"));
            clearButton.setMargin(new Insets(0, 0, 0, 0));
            clearButton.setVerticalTextPosition(SwingConstants.TOP);
            clearButton.addActionListener(this);
            okButton = new JButton(PEToolKit.createImageIcon("EditTextValidate"));
            okButton.setMargin(new Insets(0, 0, 0, 0));
            okButton.addActionListener(this);
            pane = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pane.add(textField);
            pane.add(clearButton);
            pane.add(okButton);
        }

        /** Implements the <code>TableCellEditor</code> interface. */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            textField.setText(value.toString());
            return pane;
        }

        /**
		 * Returns the value of this cell.
		 */
        public Object getCellEditorValue() {
            return textField.getText();
        }

        /**
		 * Sets the value of this cell.
		 */
        public void setValue(Object value) {
            textField.setText(value.toString());
        }

        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= 2;
            }
            return true;
        }

        /**
		 * Returns true to indicate that the editing cell may
		 * be selected.
		 */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        /**
		 * Returns true to indicate that editing has begun.
		 */
        public boolean startCellEditing(EventObject anEvent) {
            return true;
        }

        /**
		 * Stops editing and
		 * returns true to indicate that editing has stopped.
		 * This method calls <code>fireEditingStopped</code>.
		 */
        public boolean stopCellEditing() {
            fireEditingStopped();
            return true;
        }

        /**
		 * Cancels editing.  This method calls <code>fireEditingCanceled</code>.
		 */
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        /**
		 * When an action is performed, editing is ended.
		 */
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == okButton) stopCellEditing(); else if (e.getSource() == clearButton) textField.setText("");
        }
    }
}
