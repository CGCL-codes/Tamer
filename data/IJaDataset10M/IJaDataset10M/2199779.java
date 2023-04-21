package fr.itris.glips.rtdaeditor.anim.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import fr.itris.glips.library.widgets.*;
import fr.itris.glips.rtdaeditor.anim.*;
import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.resources.*;
import fr.itris.glips.rtda.action.svg.*;
import javax.swing.table.*;

/**
 * the wideget of an event chooser
 * @author ITRIS, Jordi SUC
 */
public class EventChooser extends Widget {

    /**
	 * the array of the keys
	 */
    public static Integer[] keysArray = { KeyEvent.VK_0, KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6, KeyEvent.VK_7, KeyEvent.VK_8, KeyEvent.VK_9, KeyEvent.VK_A, KeyEvent.VK_ALL_CANDIDATES, KeyEvent.VK_ALPHANUMERIC, KeyEvent.VK_ALT, KeyEvent.VK_ALT_GRAPH, KeyEvent.VK_AMPERSAND, KeyEvent.VK_ASTERISK, KeyEvent.VK_AT, KeyEvent.VK_B, KeyEvent.VK_BACK_QUOTE, KeyEvent.VK_BACK_SLASH, KeyEvent.VK_BRACELEFT, KeyEvent.VK_BRACERIGHT, KeyEvent.VK_C, KeyEvent.VK_CAPS_LOCK, KeyEvent.VK_CIRCUMFLEX, KeyEvent.VK_CLEAR, KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_COLON, KeyEvent.VK_COMMA, KeyEvent.VK_CONTEXT_MENU, KeyEvent.VK_CONTROL, KeyEvent.VK_D, KeyEvent.VK_DECIMAL, KeyEvent.VK_DELETE, KeyEvent.VK_DIVIDE, KeyEvent.VK_DOLLAR, KeyEvent.VK_DOWN, KeyEvent.VK_E, KeyEvent.VK_END, KeyEvent.VK_ENTER, KeyEvent.VK_EQUALS, KeyEvent.VK_ESCAPE, KeyEvent.VK_EURO_SIGN, KeyEvent.VK_EXCLAMATION_MARK, KeyEvent.VK_F, KeyEvent.VK_F1, KeyEvent.VK_F10, KeyEvent.VK_F11, KeyEvent.VK_F12, KeyEvent.VK_F13, KeyEvent.VK_F14, KeyEvent.VK_F15, KeyEvent.VK_F16, KeyEvent.VK_F17, KeyEvent.VK_F18, KeyEvent.VK_F19, KeyEvent.VK_F2, KeyEvent.VK_F20, KeyEvent.VK_F21, KeyEvent.VK_F22, KeyEvent.VK_F23, KeyEvent.VK_F24, KeyEvent.VK_F3, KeyEvent.VK_F4, KeyEvent.VK_F5, KeyEvent.VK_F6, KeyEvent.VK_F7, KeyEvent.VK_F8, KeyEvent.VK_F9, KeyEvent.VK_G, KeyEvent.VK_GREATER, KeyEvent.VK_H, KeyEvent.VK_HOME, KeyEvent.VK_I, KeyEvent.VK_INSERT, KeyEvent.VK_J, KeyEvent.VK_K, KeyEvent.VK_KP_DOWN, KeyEvent.VK_KP_LEFT, KeyEvent.VK_KP_RIGHT, KeyEvent.VK_KP_UP, KeyEvent.VK_L, KeyEvent.VK_LEFT, KeyEvent.VK_LEFT_PARENTHESIS, KeyEvent.VK_LESS, KeyEvent.VK_M, KeyEvent.VK_META, KeyEvent.VK_MINUS, KeyEvent.VK_MULTIPLY, KeyEvent.VK_N, KeyEvent.VK_NUM_LOCK, KeyEvent.VK_NUMBER_SIGN, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3, KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9, KeyEvent.VK_O, KeyEvent.VK_OPEN_BRACKET, KeyEvent.VK_P, KeyEvent.VK_PAGE_DOWN, KeyEvent.VK_PAGE_UP, KeyEvent.VK_PAUSE, KeyEvent.VK_PERIOD, KeyEvent.VK_PLUS, KeyEvent.VK_PRINTSCREEN, KeyEvent.VK_Q, KeyEvent.VK_QUOTE, KeyEvent.VK_QUOTEDBL, KeyEvent.VK_R, KeyEvent.VK_RIGHT, KeyEvent.VK_RIGHT_PARENTHESIS, KeyEvent.VK_ROMAN_CHARACTERS, KeyEvent.VK_S, KeyEvent.VK_SCROLL_LOCK, KeyEvent.VK_SEMICOLON, KeyEvent.VK_SEPARATOR, KeyEvent.VK_SHIFT, KeyEvent.VK_SLASH, KeyEvent.VK_SPACE, KeyEvent.VK_SUBTRACT, KeyEvent.VK_T, KeyEvent.VK_TAB, KeyEvent.VK_U, KeyEvent.VK_UNDERSCORE, KeyEvent.VK_UP, KeyEvent.VK_V, KeyEvent.VK_W, KeyEvent.VK_WINDOWS, KeyEvent.VK_X, KeyEvent.VK_Y, KeyEvent.VK_Z };

    /**
	 * the array of the modifiers
	 */
    public static Integer[] modifiersArray = { InputEvent.BUTTON1_DOWN_MASK, InputEvent.BUTTON2_DOWN_MASK, InputEvent.BUTTON3_DOWN_MASK, InputEvent.ALT_GRAPH_DOWN_MASK, InputEvent.ALT_DOWN_MASK, InputEvent.CTRL_DOWN_MASK, InputEvent.META_DOWN_MASK, InputEvent.SHIFT_DOWN_MASK };

    /**
	 * handling the array of the keys and of the modifiers
	 */
    static {
        Comparator<Integer> comparator = new Comparator<Integer>() {

            public int compare(Integer integer0, Integer integer1) {
                if (integer0 != null && integer1 != null) {
                    return KeyEvent.getKeyText(integer0).compareTo(KeyEvent.getKeyText(integer1));
                }
                return 0;
            }
        };
        Arrays.sort(keysArray, comparator);
        comparator = new Comparator<Integer>() {

            public int compare(Integer integer0, Integer integer1) {
                if (integer0 != null && integer1 != null) {
                    return KeyEvent.getKeyModifiersText(integer0).compareTo(KeyEvent.getKeyModifiersText(integer1));
                }
                return 0;
            }
        };
        Arrays.sort(modifiersArray, comparator);
    }

    /**
	 * the list of the event items
	 */
    private LinkedHashMap<Integer, EventItem> eventItems = new LinkedHashMap<Integer, EventItem>();

    /**
	 * the map associating an event type to the label of this event
	 */
    private HashMap<Integer, String> events = new HashMap<Integer, String>();

    /**
	 * the text field
	 */
    private JTextField textField;

    /**
	 * the more button
	 */
    private JButton moreButton;

    /**
	 * the event chooser dialog
	 */
    private EventChooserDialog eventChooserDialog;

    /**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
    protected EventChooser(boolean isEditor) {
        super(isEditor);
        eventItems.put(SVGEventsManager.MOUSE_DOWN, new EventItem(SVGEventsManager.MOUSE_DOWN));
        eventItems.put(SVGEventsManager.MOUSE_UP, new EventItem(SVGEventsManager.MOUSE_UP));
        eventItems.put(SVGEventsManager.CLICK, new EventItem(SVGEventsManager.CLICK));
        eventItems.put(SVGEventsManager.MOUSE_OVER, new EventItem(SVGEventsManager.MOUSE_OVER));
        eventItems.put(SVGEventsManager.MOUSE_OUT, new EventItem(SVGEventsManager.MOUSE_OUT));
        eventItems.put(SVGEventsManager.MOUSE_MOVE, new EventItem(SVGEventsManager.MOUSE_MOVE));
        eventItems.put(SVGEventsManager.KEY_UP, new EventItem(SVGEventsManager.KEY_UP));
        eventItems.put(SVGEventsManager.KEY_DOWN, new EventItem(SVGEventsManager.KEY_DOWN));
        eventItems.put(SVGEventsManager.KEY_TYPED, new EventItem(SVGEventsManager.KEY_TYPED));
        try {
            ResourceBundle bundle = ResourcesManager.bundle;
            events.put(SVGEventsManager.MOUSE_DOWN, bundle.getString("item_mouseDown"));
            events.put(SVGEventsManager.MOUSE_UP, bundle.getString("item_mouseUp"));
            events.put(SVGEventsManager.CLICK, bundle.getString("item_click"));
            events.put(SVGEventsManager.MOUSE_OVER, bundle.getString("item_mouseOver"));
            events.put(SVGEventsManager.MOUSE_OUT, bundle.getString("item_mouseOut"));
            events.put(SVGEventsManager.MOUSE_MOVE, bundle.getString("item_mouseMove"));
            events.put(SVGEventsManager.KEY_UP, bundle.getString("item_keyUp"));
            events.put(SVGEventsManager.KEY_DOWN, bundle.getString("item_keyDown"));
            events.put(SVGEventsManager.KEY_TYPED, bundle.getString("item_keyTyped"));
        } catch (Exception ex) {
        }
        buildWidget();
    }

    /**
	 * builds the widget
	 */
    protected void buildWidget() {
        textField = new JTextField();
        textField.setOpaque(false);
        textField.setBorder(new EmptyBorder(0, 0, 0, 0));
        textField.setEditable(false);
        moreButton = new JButton();
        String moreChooserLabel = "";
        ResourceBundle bundle = ResourcesManager.bundle;
        try {
            moreChooserLabel = bundle.getString("rtdaanim_tagchooserbutton");
        } catch (Exception ex) {
        }
        moreButton.setText(moreChooserLabel);
        moreButton.setMargin(new Insets(1, 1, 1, 1));
        setLayout(new BorderLayout(1, 0));
        add(textField, BorderLayout.CENTER);
        add(moreButton, BorderLayout.EAST);
        if (isEditor) {
            moreButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    eventChooserDialog.showDialog();
                    getItem().setValue(getEventItemsValue());
                    if (validateRunnable != null) {
                        validateRunnable.run();
                    }
                }
            });
            if (Editor.getParent() instanceof Frame) {
                eventChooserDialog = new EventChooserDialog((Frame) Editor.getParent());
            } else if (Editor.getParent() instanceof JDialog) {
                eventChooserDialog = new EventChooserDialog((JDialog) Editor.getParent());
            }
        }
    }

    @Override
    protected void setItem(EditableItem item, Runnable validateRunnable) {
        super.setItem(item, validateRunnable);
        handleEventItems(item.getValue());
        String label = "";
        for (EventItem eventItem : eventItems.values()) {
            if (eventItem.isUsed()) {
                label += eventItem.toLabel() + ", ";
            }
        }
        if (label.endsWith(", ")) {
            label = label.substring(0, label.length() - 2);
        }
        textField.setText(label);
    }

    /**
	 * handle the event items according the new attribute value
	 * @param value a value 
	 */
    protected void handleEventItems(String value) {
        if (value == null) {
            value = "";
        }
        for (EventItem item : eventItems.values()) {
            item.reset();
        }
        if (!value.equals("")) {
            String[] splitValue = value.split(EventItem.itemsSeparator);
            int event = -1, modifier = 0, key = -1;
            String[] innerItemSplitValue = null;
            LinkedList<String> elements = new LinkedList<String>();
            for (String itemValue : splitValue) {
                if (itemValue != null) {
                    elements.clear();
                    innerItemSplitValue = itemValue.split(EventItem.elementSeparator);
                    for (String val : innerItemSplitValue) {
                        if (!val.equals("")) {
                            elements.add(val);
                        }
                    }
                    if (elements.size() >= 3) {
                        event = -1;
                        modifier = 0;
                        key = -1;
                        try {
                            event = Integer.parseInt(elements.getFirst());
                            modifier = Integer.parseInt(elements.get(1));
                            key = Integer.parseInt(elements.get(2));
                        } catch (Exception ex) {
                        }
                        if (event != -1) {
                            EventItem item = eventItems.get(event);
                            if (item != null) {
                                item.setModifiers(modifier);
                                item.setKey(key);
                                item.setUsed(true);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
	 * @return the string representation of the event items value
	 */
    protected String getEventItemsValue() {
        String rep = "";
        for (EventItem eventItem : eventItems.values()) {
            if (eventItem.isUsed()) {
                rep += eventItem.toString() + EventItem.itemsSeparator;
            }
        }
        return rep;
    }

    /**
	 * the class of the dialog used to choose a tag or a function
	 * @author ITRIS, Jordi SUC
	 */
    protected class EventChooserDialog extends TitledDialog {

        /**
		 * the ok constant
		 */
        public static final int OK = 0;

        /**
		 * the cancel constant
		 */
        public static final int CANCEL = 1;

        /**
		 * the event chooser table
		 */
        private EventChooserTable table;

        /**
		 * the result
		 */
        private int currentResult = OK;

        /**
		 * the constructor of the class
		 * @param parent the parent container
		 */
        protected EventChooserDialog(Frame parent) {
            super(parent, true, true);
        }

        /**
		 * the constructor of the class
		 * @param parent the parent container
		 */
        protected EventChooserDialog(JDialog parent) {
            super(parent, true);
        }

        @Override
        protected JPanel buildContentPanel() {
            JPanel thePanel = new JPanel();
            ResourceBundle bundle = ResourcesManager.bundle;
            String titleLabel = bundle.getString("rtdaanim_eventchooser_title");
            String messageLabel = bundle.getString("rtdaanim_eventchooser_explainLabel");
            setTitleMessage(titleLabel);
            setMessage(messageLabel, TitledDialog.INFORMATION_TYPE);
            table = new EventChooserTable();
            ActionListener buttonsListener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (evt.getSource().equals(okButton)) {
                        currentResult = OK;
                    } else {
                        currentResult = CANCEL;
                    }
                    setVisible(false);
                }
            };
            okButtonListener = buttonsListener;
            cancelButtonListener = buttonsListener;
            okButton.addActionListener(buttonsListener);
            cancelButton.addActionListener(buttonsListener);
            thePanel.setLayout(new BorderLayout(0, 0));
            thePanel.add(table.getTableHeader(), BorderLayout.NORTH);
            thePanel.add(table, BorderLayout.CENTER);
            return thePanel;
        }

        /**
		 * shows the dialog 
		 * @return whether the user has clicked 
		 */
        public int showDialog() {
            table.resizeColumns();
            pack();
            showDialog(moreButton);
            return currentResult;
        }
    }

    /**
	 * the class of the event chooser
	 * @author ITRIS, Jordi SUC
	 */
    protected class EventChooserTable extends JTable {

        /**
		 * the checkbox widgets
		 */
        protected EventChooserWidget checkBoxTableWidget = new EventChooserCheckBox(), checkBoxTableWidgetEditor = new EventChooserCheckBox();

        /**
		 * the label widget
		 */
        protected EventChooserWidget jLabelWidget = new EventChooserJLabel();

        /**
		 * the modifiers list widget
		 */
        protected EventChooserWidget modifiersListWidget = new EventChooserModifiersList(), modifiersListWidgetEditor = new EventChooserModifiersList();

        /**
		 * the key chooser widget
		 */
        protected EventChooserWidget keyChooserWidget = new EventChooserKeyCombo(), keyChooserWidgetEditor = new EventChooserKeyCombo();

        /**
		 * the table model
		 */
        protected TableModel tableModel = null;

        /**
		 * the table cell renderer
		 */
        protected TableCellRenderer tableRenderer = null;

        /**
		 * the table cell editor
		 */
        protected TableCellEditor tableEditor = null;

        /**
		 * the cell height
		 */
        public int cellHeight = 32;

        /**
		 * the constructor of the class
		 */
        public EventChooserTable() {
            tableModel = new EventChooserTableModel();
            tableRenderer = new EventChooserTableRenderer();
            tableEditor = new EventChooserTableEditor();
            setModel(tableModel);
            setAutoCreateColumnsFromModel(true);
            setDefaultEditor(Object.class, tableEditor);
            setIntercellSpacing(new Dimension(1, 1));
            getTableHeader().setReorderingAllowed(false);
            setRowHeight(cellHeight);
        }

        /**
		 * resize all the columns
		 * @return the total width of the table
		 */
        public int resizeColumns() {
            int allWidth = 0;
            getColumnModel().getColumn(0).setPreferredWidth(checkBoxTableWidget.getPreferredWidth());
            getColumnModel().getColumn(1).setPreferredWidth(jLabelWidget.getPreferredWidth());
            getColumnModel().getColumn(2).setPreferredWidth(modifiersListWidget.getPreferredWidth());
            getColumnModel().getColumn(3).setPreferredWidth(keyChooserWidget.getPreferredWidth());
            allWidth += checkBoxTableWidget.getPreferredWidth();
            allWidth += jLabelWidget.getPreferredWidth();
            allWidth += modifiersListWidget.getPreferredWidth();
            allWidth += keyChooserWidget.getPreferredWidth();
            return allWidth;
        }

        /**
		 * @return the cellHeight
		 */
        public int getCellHeight() {
            return cellHeight;
        }

        @Override
        public TableCellRenderer getCellRenderer(int row, int col) {
            if (tableRenderer != null && row < getRowCount() && col < getColumnCount()) {
                return tableRenderer;
            }
            return super.getCellRenderer(row, col);
        }

        /**
		 * the class of the table model
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserTableModel extends AbstractTableModel {

            /**
			 * the column labels
			 */
            protected String usedlabel = "", eventColumnLabel = "", modifiersColumnLabel = "", keyColumnLabel = "";

            /**
			 * the event chooser table model
			 */
            protected EventChooserTableModel() {
                try {
                    usedlabel = ResourcesManager.bundle.getString("rtdaanim_eventchooser_usedLabel");
                    eventColumnLabel = ResourcesManager.bundle.getString("rtdaanim_eventchooser_eventLabel");
                    modifiersColumnLabel = ResourcesManager.bundle.getString("rtdaanim_eventchooser_modifiersLabel");
                    keyColumnLabel = ResourcesManager.bundle.getString("rtdaanim_eventchooser_keysLabel");
                } catch (Exception ex) {
                }
            }

            /**
			 * @see javax.swing.table.TableModel#getColumnCount()
			 */
            public int getColumnCount() {
                return 4;
            }

            /**
			 * @see javax.swing.table.TableModel#getRowCount()
			 */
            public int getRowCount() {
                return eventItems.size();
            }

            /**
			 * @see javax.swing.table.TableModel#getValueAt(int, int)
			 */
            public Object getValueAt(int row, int col) {
                return eventItems.values().toArray(new EventItem[] {})[row];
            }

            @Override
            public String getColumnName(int col) {
                switch(col) {
                    case 0:
                        return usedlabel;
                    case 1:
                        return eventColumnLabel;
                    case 2:
                        return modifiersColumnLabel;
                    case 3:
                        return keyColumnLabel;
                }
                return "";
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                EventItem eventItem = (EventItem) getValueAt(row, col);
                if (eventItem != null) {
                    switch(col) {
                        case 0:
                            return true;
                        case 1:
                            return false;
                        case 2:
                            return eventItem.isUsed();
                        case 3:
                            return eventItem.isUsed() && eventItem.isKeyEvent();
                    }
                }
                return false;
            }

            @Override
            public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
                fireTableDataChanged();
            }
        }

        /**
		 * the class of the table renderer
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserTableRenderer implements TableCellRenderer {

            /**
			 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
			 */
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                EventItem eventItem = (EventItem) value;
                if (eventItem != null) {
                    switch(column) {
                        case 0:
                            checkBoxTableWidget.setEventItemForRendering(eventItem);
                            return checkBoxTableWidget;
                        case 1:
                            jLabelWidget.setEventItemForRendering(eventItem);
                            return jLabelWidget;
                        case 2:
                            modifiersListWidget.setEventItemForRendering(eventItem);
                            return modifiersListWidget;
                        case 3:
                            if (eventItem.isKeyEvent()) {
                                keyChooserWidget.setEventItemForRendering(eventItem);
                                return keyChooserWidget;
                            }
                            return null;
                    }
                }
                return null;
            }
        }

        /**
		 * the class of the table renderer
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserTableEditor extends AbstractCellEditor implements TableCellEditor {

            /**
			 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
			 */
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                EventItem eventItem = (EventItem) value;
                if (eventItem != null) {
                    switch(column) {
                        case 0:
                            checkBoxTableWidgetEditor.setEventItemForEditing(eventItem, this);
                            return checkBoxTableWidgetEditor;
                        case 2:
                            modifiersListWidgetEditor.setEventItemForEditing(eventItem, this);
                            return modifiersListWidgetEditor;
                        case 3:
                            keyChooserWidgetEditor.setEventItemForEditing(eventItem, this);
                            return keyChooserWidgetEditor;
                    }
                }
                return null;
            }

            /**
			 * @see javax.swing.CellEditor#getCellEditorValue()
			 */
            public Object getCellEditorValue() {
                return null;
            }

            @Override
            public void fireEditingStopped() {
                super.fireEditingStopped();
            }
        }

        /**
		 * the super class of the widgets of the table
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserWidget extends JComponent {

            /**
			 * the current event item
			 */
            protected EventItem currentEventItem = null;

            /**
			 * the table cell editor
			 */
            protected EventChooserTableEditor tableCellEditor = null;

            /**
			 * the constructor of the class
			 */
            public EventChooserWidget() {
                setOpaque(false);
                setBorder(new EmptyBorder(0, 0, 0, 0));
            }

            /**
			 * sets the new event item for rendering
			 * @param item an item
			 */
            protected void setEventItemForRendering(EventItem item) {
                this.currentEventItem = item;
            }

            /**
			 * sets the new event item for editing
			 * @param item an item
			 * @param tableCellEditor the table cell editor
			 */
            protected void setEventItemForEditing(EventItem item, EventChooserTableEditor tableCellEditor) {
                this.tableCellEditor = tableCellEditor;
                setEventItemForRendering(item);
            }

            /**
			 * @return the width
			 */
            public int getPreferredWidth() {
                return 0;
            }
        }

        /**
		 * the class of the check box
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserCheckBox extends EventChooserWidget {

            /**
			 * the check box
			 */
            private JCheckBox checkBox = new JCheckBox();

            /**
			 * the listener to the check box
			 */
            private ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    checkBox.removeActionListener(this);
                    if (currentEventItem != null && tableCellEditor != null) {
                        currentEventItem.setUsed(checkBox.isSelected());
                    }
                    tableCellEditor.fireEditingStopped();
                }
            };

            @Override
            public int getPreferredWidth() {
                return 27;
            }

            /**
			 * the constructor of the class
			 */
            public EventChooserCheckBox() {
                checkBox.setOpaque(false);
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(checkBox);
            }

            @Override
            protected void setEventItemForRendering(EventItem item) {
                super.setEventItemForRendering(item);
                checkBox.setSelected(item.isUsed());
            }

            @Override
            protected void setEventItemForEditing(EventItem item, EventChooserTableEditor tableCellEditor) {
                super.setEventItemForEditing(item, tableCellEditor);
                checkBox.addActionListener(actionListener);
            }
        }

        /**
		 * the class of the check box
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserJLabel extends EventChooserWidget {

            /**
			 * the jlabel for rendering the event type
			 */
            protected JLabel eventLbl = new JLabel();

            /**
			 * the constructor of the class
			 */
            public EventChooserJLabel() {
                setLayout(new FlowLayout(FlowLayout.LEFT));
                add(eventLbl);
            }

            @Override
            protected void setEventItemForRendering(EventItem item) {
                super.setEventItemForRendering(item);
                eventLbl.setEnabled(item.isUsed());
                eventLbl.setText(item.getEventLabel());
            }

            @Override
            public int getPreferredWidth() {
                return 120;
            }

            @Override
            protected void setEventItemForEditing(EventItem item, EventChooserTableEditor tableCellEditor) {
            }
        }

        /**
		 * the class of the jlist used to choose modifiers
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserModifiersList extends EventChooserWidget {

            /**
			 * the jlist used to chooser a modifier
			 */
            protected JList modifiersList = null;

            /**
			 * the scrollpane
			 */
            protected JScrollPane scrollpane = null;

            /**
			 * the listener to the changes on the list selections
			 */
            protected ListSelectionListener listSelectionListener = new ListSelectionListener() {

                public void valueChanged(ListSelectionEvent evt) {
                    modifiersList.removeListSelectionListener(listSelectionListener);
                    if (currentEventItem != null && tableCellEditor != null) {
                        Object[] selectedValues = modifiersList.getSelectedValues();
                        int modifier = 0, value = 0;
                        if (selectedValues != null) {
                            ComboListItem listItem = null;
                            for (int i = 0; i < selectedValues.length; i++) {
                                listItem = (ComboListItem) selectedValues[i];
                                if (listItem != null) {
                                    try {
                                        value = (Integer) listItem.getValue();
                                    } catch (Exception ex) {
                                        value = 0;
                                    }
                                    modifier = modifier | value;
                                }
                            }
                        }
                        currentEventItem.setModifiers(modifier);
                    }
                    tableCellEditor.fireEditingStopped();
                }
            };

            @Override
            public int getPreferredWidth() {
                return 250;
            }

            /**
			 * the constructor of the class
			 */
            public EventChooserModifiersList() {
                ComboListItem listItem = null;
                String label = "";
                ComboListItem[] items = new ComboListItem[modifiersArray.length];
                int mod = 0;
                for (int i = 0; i < modifiersArray.length; i++) {
                    mod = modifiersArray[i];
                    label = InputEvent.getModifiersExText(mod);
                    listItem = new ComboListItem(mod, label + " ");
                    items[i] = listItem;
                }
                modifiersList = new JList(items);
                modifiersList.setBorder(new EmptyBorder(0, 0, 0, 0));
                modifiersList.setBorder(new EmptyBorder(0, 2, 0, 0));
                modifiersList.setAlignmentX(SwingConstants.CENTER);
                modifiersList.setLayoutOrientation(JList.VERTICAL_WRAP);
                modifiersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
                modifiersList.setVisibleRowCount(2);
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(modifiersList);
            }

            @Override
            protected void setEventItemForRendering(EventItem item) {
                super.setEventItemForRendering(item);
                modifiersList.setEnabled(item.isUsed());
                if (item.isUsed()) {
                    modifiersList.getSelectionModel().clearSelection();
                    int mod = item.getModifiers(), cmod = 0;
                    ComboListItem listItem = null;
                    for (int i = 0; i < modifiersList.getModel().getSize(); i++) {
                        listItem = (ComboListItem) modifiersList.getModel().getElementAt(i);
                        cmod = (Integer) listItem.getValue();
                        if ((mod & cmod) == cmod) {
                            modifiersList.addSelectionInterval(i, i);
                        }
                    }
                } else {
                    modifiersList.getSelectionModel().clearSelection();
                }
            }

            @Override
            protected void setEventItemForEditing(EventItem item, EventChooserTableEditor tableCellEditor) {
                super.setEventItemForEditing(item, tableCellEditor);
                modifiersList.addListSelectionListener(listSelectionListener);
            }
        }

        /**
		 * the class of the key combo box
		 * @author ITRIS, Jordi SUC
		 */
        protected class EventChooserKeyCombo extends EventChooserWidget {

            /**
			 * the combo box used to choose a key
			 */
            protected JComboBox keyCombo = new JComboBox();

            /**
			 * the listener to the check box
			 */
            private ActionListener actionListener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    keyCombo.removeActionListener(this);
                    if (currentEventItem != null && tableCellEditor != null) {
                        ComboListItem selectedItem = (ComboListItem) keyCombo.getSelectedItem();
                        if (selectedItem != null) {
                            currentEventItem.setKey((Integer) selectedItem.getValue());
                        }
                    }
                    tableCellEditor.fireEditingStopped();
                }
            };

            @Override
            public int getPreferredWidth() {
                return 230;
            }

            /**
			 * the constructor of the class
			 */
            public EventChooserKeyCombo() {
                ComboListItem comboItem = null;
                for (int key : keysArray) {
                    comboItem = new ComboListItem(key, KeyEvent.getKeyText(key));
                    keyCombo.addItem(comboItem);
                }
                setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
                add(keyCombo);
            }

            @Override
            protected void setEventItemForRendering(EventItem item) {
                super.setEventItemForRendering(item);
                boolean isEnabled = item.isUsed() && item.isKeyEvent();
                keyCombo.setEnabled(isEnabled);
                if (isEnabled) {
                    int index = -1, key = item.getKey();
                    for (int i = 0; i < keysArray.length; i++) {
                        if (keysArray[i] == key) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        keyCombo.setSelectedIndex(index);
                    } else {
                        keyCombo.setSelectedIndex(0);
                    }
                } else {
                    keyCombo.setSelectedIndex(0);
                }
            }

            @Override
            protected void setEventItemForEditing(EventItem item, EventChooserTableEditor tableCellEditor) {
                super.setEventItemForEditing(item, tableCellEditor);
                keyCombo.addActionListener(actionListener);
            }
        }
    }

    /**
	 * the class of an event item
	 * @author ITRIS, Jordi SUC
	 */
    protected class EventItem {

        /**
		 * the separators
		 */
        protected static final String elementSeparator = ":", itemsSeparator = ";";

        /**
		 * the type of the event and the key id
		 */
        protected int eventType = -1, key = -1;

        /**
		 * the set of the modifiers
		 */
        protected HashSet<Integer> modifiers = new HashSet<Integer>();

        /**
		 * whether this event item is used
		 */
        protected boolean used = false;

        /**
		 * whether the event consists in pressing, releasing or clicking a mouse button
		 */
        protected boolean isMouseButtonEvent = false;

        /**
		 * the constructor of the class
		 * @param eventType the type of the event
		 */
        protected EventItem(int eventType) {
            this.eventType = eventType;
            isMouseButtonEvent = (eventType == SVGEventsManager.CLICK || eventType == SVGEventsManager.MOUSE_DOWN || eventType == SVGEventsManager.MOUSE_UP);
        }

        /**
		 * resetting the event items
		 */
        public void reset() {
            modifiers.clear();
            if (isMouseButtonEvent) {
                modifiers.add(InputEvent.BUTTON1_DOWN_MASK);
            }
            key = -1;
            used = false;
        }

        /**
		 * @return the key
		 */
        public int getKey() {
            return key;
        }

        /**
		 * @param key the key to set
		 */
        public void setKey(int key) {
            this.key = key;
        }

        /**
		 * @return the use
		 */
        public boolean isUsed() {
            return used;
        }

        /**
		 * @param used the use to set
		 */
        public void setUsed(boolean used) {
            this.used = used;
        }

        /**
		 * @return the eventType
		 */
        public int getEventType() {
            return eventType;
        }

        /**
		 * @return whether the event of this object is a key event or not
		 */
        public boolean isKeyEvent() {
            return (eventType >= 7 && eventType <= 9);
        }

        /**
		 * @return the modifier
		 */
        public int getModifiers() {
            int result = 0;
            for (int modifier : modifiers) {
                result = result | modifier;
            }
            return result;
        }

        /**
		 * @param modifier the modifier to set
		 */
        public void setModifiers(int modifier) {
            modifiers.clear();
            if (modifier != -1 && modifier != 0) {
                for (int mod : modifiersArray) {
                    if ((mod & modifier) == mod) {
                        modifiers.add(mod);
                    }
                }
            }
            checkModifiers();
        }

        @Override
        public String toString() {
            return eventType + elementSeparator + getModifiers() + elementSeparator + key;
        }

        /**
		 * @return the displayable representation of this event item
		 */
        public String toLabel() {
            String label = events.get(eventType);
            if (getModifiers() != 0 && getModifiers() != -1) {
                label += "+" + InputEvent.getModifiersExText(getModifiers());
            }
            if (key != -1) {
                label += "+" + KeyEvent.getKeyText(key);
            }
            return label;
        }

        /**
		 * checks the consistency of the modifiers
		 */
        protected void checkModifiers() {
            if (isMouseButtonEvent && !modifiers.contains(InputEvent.BUTTON1_DOWN_MASK) && !modifiers.contains(InputEvent.BUTTON2_DOWN_MASK) && !modifiers.contains(InputEvent.BUTTON3_DOWN_MASK)) {
                modifiers.add(InputEvent.BUTTON1_DOWN_MASK);
            }
        }

        /**
		 * @return the event label
		 */
        public String getEventLabel() {
            return events.get(eventType);
        }

        /**
		 * @param modifier the modifier
		 * @return the event label
		 */
        public String getModifierLabel(int modifier) {
            return InputEvent.getModifiersExText(modifier);
        }

        /**
		 * @return the key label
		 */
        public String getKeyLabel() {
            return KeyEvent.getKeyText(key);
        }
    }
}
