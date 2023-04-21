package org.in4ama.editor.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import net.xoetrope.xui.XAttributedComponent;
import net.xoetrope.xui.XModelHolder;
import net.xoetrope.xui.XProjectManager;
import net.xoetrope.xui.data.XModel;
import net.xoetrope.xui.data.XRowSelector;
import net.xoetrope.xui.events.XListenerHelper;
import net.xoetrope.xui.helper.XTranslator;
import net.xoetrope.xui.style.XStyle;
import net.xoetrope.xui.style.XStyleComponent;
import net.xoetrope.xui.style.XStyleManager;
import org.in4ama.editor.project.ComponentInfo;
import org.in4ama.editor.project.pdfdocument.FontAndSize;
import org.in4ama.editor.project.pdfdocument.PdfFontInfo;
import org.in4ama.editor.ui.XFontChooserDialog;

public class AcroFieldPropertiesTable extends JTable implements XAttributedComponent, XModelHolder, XStyleComponent, XRowSelector, XListenerHelper, ListSelectionListener, PropertyChangeListener {

    private static final String[] COLUMN_NAMES = { XProjectManager.getCurrentProject().getTranslator().translate("ST_NAME"), XProjectManager.getCurrentProject().getTranslator().translate("ST_VALUE") };

    private static final int COL_NAME = 0;

    private static final int COL_VALUE = 1;

    private JComboBox comboCellEditorComponent;

    private TableCellEditor multiEditor;

    private JCheckBox checkBoxCellEditorComponent;

    private TableCellEditor booleanEditor;

    private ColorEditor colorEditor;

    private FontEditor fontEditor;

    private ColorRenderer colorRenderer;

    private FontRenderer fontRenderer;

    private BooleanRenderer booleanRenderer;

    private final AcroFieldPropertiesTableModel model = new AcroFieldPropertiesTableModel();

    private XTranslator translator;

    public AcroFieldPropertiesTable() {
        super();
        setModel(model);
        initCellEditors();
        initRenderers();
        super.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        translator = XProjectManager.getCurrentProject().getTranslator();
    }

    private void initCellEditors() {
        comboCellEditorComponent = new JComboBox();
        comboCellEditorComponent.setLightWeightPopupEnabled(false);
        multiEditor = new DefaultCellEditor(comboCellEditorComponent);
        booleanEditor = new DefaultCellEditor(checkBoxCellEditorComponent = new JCheckBox());
        colorEditor = new ColorEditor();
        fontEditor = new FontEditor();
    }

    private void initRenderers() {
        colorRenderer = new ColorRenderer(true);
        fontRenderer = new FontRenderer();
        booleanRenderer = new BooleanRenderer();
    }

    public void setComponentInfo(ComponentInfo fieldInfo) {
        model.setAcroFieldInfo(fieldInfo);
        update();
        if (fieldInfo != null) {
            fieldInfo.addPropertyChangeListener(this);
        }
    }

    public void setStyle(String style) {
        XStyleManager styleMgr = XProjectManager.getCurrentProject().getStyleManager();
        XStyle xstyle = styleMgr.getStyle(style);
        setForeground(xstyle.getStyleAsColor(XStyle.COLOR_FORE));
        setBackground(xstyle.getStyleAsColor(XStyle.COLOR_BACK));
        setFont(styleMgr.getFont(xstyle));
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int col) {
        TableCellRenderer cellRenderer = null;
        if (col == COL_VALUE) cellRenderer = getPropertyCellRenderer(row);
        return (cellRenderer == null) ? super.getCellRenderer(row, col) : cellRenderer;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int col) {
        TableCellEditor cellEditor = null;
        if (col == COL_VALUE) cellEditor = getPropertyCellEditor(row);
        return (cellEditor == null) ? super.getCellEditor(row, col) : cellEditor;
    }

    private TableCellEditor getPropertyCellEditor(int row) {
        ComponentInfo fieldInfo = (ComponentInfo) model.getComponentInfo();
        String propertyName = fieldInfo.getPropertyNames()[row];
        Object propertyValue = fieldInfo.getProperty(propertyName);
        int propertyType = fieldInfo.getPropertyType(propertyName);
        TableCellEditor cellEditor = null;
        switch(propertyType) {
            case ComponentInfo.PROPERTY_MULTI:
                Object[] allowedValues = fieldInfo.getPropertyAllowedValues(propertyName);
                comboCellEditorComponent.setModel(new DefaultComboBoxModel(allowedValues));
                comboCellEditorComponent.setSelectedItem(propertyValue);
                comboCellEditorComponent.revalidate();
                comboCellEditorComponent.updateUI();
                cellEditor = multiEditor;
                break;
            case ComponentInfo.PROPERTY_BOOLEAN:
                checkBoxCellEditorComponent.setSelected((Boolean) propertyValue);
                checkBoxCellEditorComponent.revalidate();
                checkBoxCellEditorComponent.updateUI();
                cellEditor = booleanEditor;
                break;
            case ComponentInfo.PROPERTY_COLOR:
                cellEditor = colorEditor;
                break;
            case ComponentInfo.PROPERTY_FONT:
                cellEditor = fontEditor;
                break;
            default:
                cellEditor = null;
        }
        return cellEditor;
    }

    private TableCellRenderer getPropertyCellRenderer(int row) {
        ComponentInfo fieldInfo = (ComponentInfo) model.getComponentInfo();
        String propertyName = fieldInfo.getPropertyNames()[row];
        int propertyType = fieldInfo.getPropertyType(propertyName);
        TableCellRenderer cellRenderer = null;
        switch(propertyType) {
            case ComponentInfo.PROPERTY_COLOR:
                cellRenderer = colorRenderer;
                break;
            case ComponentInfo.PROPERTY_FONT:
                cellRenderer = fontRenderer;
                break;
            case ComponentInfo.PROPERTY_BOOLEAN:
                cellRenderer = booleanRenderer;
                break;
            default:
                cellRenderer = null;
        }
        return cellRenderer;
    }

    /** ******************************************************** */
    public int setAttribute(String arg0, Object arg1) {
        return 0;
    }

    public void setModel(XModel arg0) {
    }

    public void setSelectedRow(int arg0) {
    }

    public void addHandler(Object arg0, String arg1, String arg2) throws NoSuchMethodException {
    }

    /** ******************************************************** */
    class AcroFieldPropertiesTableModel extends AbstractTableModel {

        private ComponentInfo fieldInfo;

        public void setAcroFieldInfo(ComponentInfo fieldInfo) {
            this.fieldInfo = fieldInfo;
        }

        public ComponentInfo getComponentInfo() {
            return fieldInfo;
        }

        public int getColumnCount() {
            return COLUMN_NAMES.length;
        }

        public String getColumnName(int col) {
            return COLUMN_NAMES[col];
        }

        public int getRowCount() {
            return (fieldInfo != null) ? fieldInfo.getNumProperties() : 0;
        }

        public Object getValueAt(int row, int col) {
            String propertyName = getPropertyName(row);
            if (col == COL_NAME) return propertyName; else if (col == COL_VALUE) return fieldInfo.getProperty(propertyName); else return null;
        }

        public void setValueAt(Object value, int row, int col) {
            if (col != COL_VALUE) return;
            String propertyName = getPropertyName(row);
            try {
                fieldInfo.setProperty(propertyName, value);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(AcroFieldPropertiesTable.this, ex.getMessage(), translator.translate("ST_ERROR"), JOptionPane.ERROR_MESSAGE);
            }
        }

        public boolean isCellEditable(int row, int col) {
            if (col != COL_VALUE) return false;
            String propertyName = getPropertyName(row);
            return fieldInfo.isPropertyEditable(propertyName);
        }

        private String getPropertyName(int idx) {
            return fieldInfo.getPropertyNames()[idx];
        }
    }

    public void update() {
        updateUI();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (model.getComponentInfo() == evt.getSource()) {
            revalidate();
            repaint();
        }
    }

    /**
	   * Force the scroll pane to set the component size if there is no data - otherwise the
	   * table would have no height
	   */
    public boolean getScrollableTracksViewportHeight() {
        if (getParent() instanceof JViewport) {
            getParent().setBackground(Color.WHITE);
        }
        return super.getScrollableTracksViewportHeight();
    }
}

class BooleanRenderer extends JCheckBox implements TableCellRenderer {

    public BooleanRenderer() {
        super();
        setBackground(Color.WHITE);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Boolean selected = (Boolean) value;
        setSelected(selected);
        return this;
    }
}

class ColorRenderer extends JLabel implements TableCellRenderer {

    Border unselectedBorder = null;

    Border selectedBorder = null;

    boolean isBordered = true;

    public ColorRenderer(boolean isBordered) {
        this.isBordered = isBordered;
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object color, boolean isSelected, boolean hasFocus, int row, int column) {
        Color newColor = (Color) color;
        setBackground(newColor);
        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                }
                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                }
                setBorder(unselectedBorder);
            }
        }
        setToolTipText("RGB value: " + newColor.getRed() + ", " + newColor.getGreen() + ", " + newColor.getBlue());
        return this;
    }
}

class FontRenderer extends JLabel implements TableCellRenderer {

    public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        FontAndSize fontAndSize = (FontAndSize) arg1;
        String name = fontAndSize.getName();
        String size = (fontAndSize.getSize() > 0) ? String.valueOf(fontAndSize.getSize()) : "scaled";
        setText(name + "(" + size + ")");
        setToolTipText("Font: " + name + "\nSize: " + size);
        return this;
    }
}

class FontEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    FontAndSize currentFontAndSize = new FontAndSize();

    JButton button;

    XFontChooserDialog dialog;

    protected static final String EDIT = "edit";

    public FontEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        dialog = new XFontChooserDialog(null, "choose font", "in4ama studio");
        dialog.setSize(400, 350);
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentFontAndSize = (FontAndSize) value;
        return button;
    }

    public Object getCellEditorValue() {
        return currentFontAndSize;
    }

    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            button.setText("choose a font");
            dialog.setSelectedBaseFont(currentFontAndSize.getBaseFont());
            dialog.setSelectedSize(currentFontAndSize.getSize());
            PdfFontInfo fontInfo = dialog.showDialog();
            if (fontInfo != null) {
                currentFontAndSize = new FontAndSize();
                currentFontAndSize.setBaseFont(fontInfo.getFont());
                currentFontAndSize.setSize(dialog.getSelectedSize());
                currentFontAndSize.setName(fontInfo.getName());
                fireEditingStopped();
            }
        }
    }
}

class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    Color currentColor;

    JButton button;

    JColorChooser colorChooser;

    JDialog dialog;

    protected static final String EDIT = "edit";

    public ColorEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(button, "Pick a Color", true, colorChooser, this, null);
    }

    /**
	 * Handles events from the editor button and from the dialog's OK button.
	 */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);
            fireEditingStopped();
        } else {
            currentColor = colorChooser.getColor();
        }
    }

    public Object getCellEditorValue() {
        return currentColor;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentColor = (Color) value;
        return button;
    }
}
