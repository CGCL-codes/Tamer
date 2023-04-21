package net.sourceforge.circuitsmith.objects.panels;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import net.sourceforge.circuitsmith.gui.EdaFontDialog;
import net.sourceforge.circuitsmith.objects.EdaAttributeText;
import net.sourceforge.circuitsmith.parts.EdaAttribute;
import net.sourceforge.circuitsmith.parts.EdaStringAttribute;

public class EdaAttributeTextTableModel extends AbstractTableModel {

    private final class FontCellEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

        private Font font;

        private final Component parent;

        FontCellEditor(final Component component) {
            parent = component;
        }

        public Component getTableCellEditorComponent(final JTable table, final Object value, final boolean isSelected, final int row, final int column) {
            final EdaAttribute attribute = m_attributes.get(row);
            font = m_attributeMap.get(attribute).getFont();
            final JButton button = new JButton(EdaFontDialog.getFontText(font));
            button.addActionListener(this);
            return button;
        }

        public Object getCellEditorValue() {
            return font;
        }

        public void actionPerformed(final ActionEvent e) {
            font = EdaFontDialog.showDialog(parent, font);
            fireEditingStopped();
        }
    }

    enum Column {

        NAME("Name", String.class), VALUE("Value", String.class), VISIBLE("Visible", Boolean.class), STYLE("Style", String.class), FONT("Font", String.class);

        final String name;

        final Class columnClass;

        Column(final String columnName, final Class clazz) {
            name = columnName;
            columnClass = clazz;
        }
    }

    private final List<EdaAttribute> m_attributes = new ArrayList<EdaAttribute>();

    private final Map<EdaAttribute, EdaAttributeText> m_attributeMap = new HashMap<EdaAttribute, EdaAttributeText>();

    public EdaAttributeTextTableModel() {
    }

    void update() {
        m_attributes.clear();
        for (final EdaAttribute attribute : m_attributeMap.keySet()) {
            m_attributes.add(attribute);
        }
        Collections.sort(m_attributes, new Comparator<EdaAttribute>() {

            public int compare(EdaAttribute o1, EdaAttribute o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        fireTableDataChanged();
    }

    void setUp(final JTable table) {
        MouseListener listener = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if ((mouseEvent.getButton() & MouseEvent.BUTTON2) != 0) {
                    final int row = table.rowAtPoint(mouseEvent.getPoint());
                    table.changeSelection(row, -1, false, false);
                    final EdaAttribute attribute = m_attributes.get(row);
                    final EdaAttributeText text = m_attributeMap.get(attribute);
                    final boolean hasAttribute = text != null;
                    final JPopupMenu popup = new JPopupMenu();
                    final Action visibleAction = new AbstractAction("Make Visible") {

                        public void actionPerformed(ActionEvent e) {
                            setValueAt(Boolean.TRUE, row, Column.VISIBLE.ordinal());
                        }
                    };
                    visibleAction.putValue(Action.MNEMONIC_KEY, new Integer('N'));
                    final Action hideAction = new AbstractAction("Hide") {

                        public void actionPerformed(ActionEvent e) {
                            setValueAt(Boolean.FALSE, row, Column.VISIBLE.ordinal());
                        }
                    };
                    hideAction.putValue(Action.MNEMONIC_KEY, new Integer('D'));
                    if (hasAttribute) {
                        visibleAction.setEnabled(false);
                        hideAction.setEnabled(true);
                    } else {
                        visibleAction.setEnabled(true);
                        hideAction.setEnabled(false);
                    }
                    popup.add(visibleAction);
                    popup.add(hideAction);
                    popup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());
                }
            }
        };
        table.addMouseListener(listener);
        table.getColumn(Column.FONT.name).setCellEditor(new FontCellEditor(table));
        final TableColumn column = table.getColumnModel().getColumn(Column.STYLE.ordinal());
        final JComboBox styleCombo = new JComboBox();
        for (final EdaAttributeText.Style style : EdaAttributeText.Style.values()) {
            styleCombo.addItem(style.getDescription());
        }
        column.setCellEditor(new DefaultCellEditor(styleCombo));
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return Column.values()[column].columnClass;
    }

    public int getRowCount() {
        return m_attributes.size();
    }

    public int getColumnCount() {
        return Column.values().length;
    }

    public Object getValueAt(final int row, final int columnNumber) {
        final Column column = Column.values()[columnNumber];
        final EdaAttribute attribute = m_attributes.get(row);
        final EdaAttributeText attributeText = m_attributeMap.get(attribute);
        switch(column) {
            case NAME:
                return attribute.getName();
            case VALUE:
                return attribute.getValue();
            case VISIBLE:
                if (attributeText == null) {
                    return Boolean.FALSE;
                }
                return Boolean.TRUE;
            case STYLE:
                if (attributeText == null) {
                    return "N/A";
                }
                return attributeText.getStyle().getDescription();
            case FONT:
                if (attributeText == null) {
                    return "N/A";
                }
                final Font font = attributeText.getFont();
                return font.getName() + ", " + font.getSize();
            default:
                throw new RuntimeException("column does not exist: " + column);
        }
    }

    private void replace(final EdaAttribute attribute, final EdaAttribute newAttribute) {
        final String name = attribute.getName();
        for (int i = 0; i < m_attributes.size(); i++) {
            if (m_attributes.get(i).getName().equals(name)) {
                m_attributes.remove(i);
                m_attributes.add(i, newAttribute);
                return;
            }
        }
    }

    @Override
    public void setValueAt(final Object value, final int row, final int columnNumber) {
        final Column column = Column.values()[columnNumber];
        final EdaAttribute attribute = m_attributes.get(row);
        final EdaAttributeText textAttribute = m_attributeMap.get(attribute);
        switch(column) {
            case NAME:
                break;
            case VALUE:
                final String attributeName = attribute.getName();
                final String oldValue = attribute.getValue();
                if (!oldValue.equals(value)) {
                    final EdaAttribute newAttribute = new EdaStringAttribute(attributeName, (String) value);
                    replace(attribute, newAttribute);
                    m_attributeMap.remove(attribute);
                    m_attributeMap.put(newAttribute, textAttribute);
                }
                break;
            case VISIBLE:
                if (textAttribute == null) {
                    final EdaAttributeText newAttributeText = new EdaAttributeText();
                    newAttributeText.setAttributeName(attribute.getName());
                    m_attributeMap.put(attribute, newAttributeText);
                } else {
                    m_attributeMap.put(attribute, null);
                }
                fireTableCellUpdated(row, Column.STYLE.ordinal());
                fireTableCellUpdated(row, Column.FONT.ordinal());
                break;
            case STYLE:
                for (final EdaAttributeText.Style style : EdaAttributeText.Style.values()) {
                    if (value.equals(style.getDescription())) {
                        textAttribute.setStyle(style);
                    }
                }
                break;
            case FONT:
                textAttribute.setFont((Font) value);
                break;
            default:
                throw new RuntimeException("column does not exist: " + column);
        }
        fireTableCellUpdated(row, columnNumber);
    }

    @Override
    public String getColumnName(int column) {
        return Column.values()[column].name;
    }

    @Override
    public boolean isCellEditable(int row, int columnNumber) {
        final Column column = Column.values()[columnNumber];
        switch(column) {
            case NAME:
                return false;
            case VALUE:
                return true;
            case VISIBLE:
                return true;
            case STYLE:
            case FONT:
                final EdaAttribute attribute = m_attributes.get(row);
                if (m_attributeMap.get(attribute) == null) {
                    return false;
                }
                return true;
            default:
                throw new RuntimeException("column does not exist: " + column);
        }
    }

    final Map<EdaAttribute, EdaAttributeText> getAttributeMap() {
        return m_attributeMap;
    }
}
