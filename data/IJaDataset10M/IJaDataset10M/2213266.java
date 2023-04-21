package es.atareao.queensboro.gui;

import java.awt.Color;
import java.awt.Component;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;
import es.atareao.queensboro.db.WrapperTable;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 *
 * @author Propietario
 */
public class JWrapperImageComboBoxTableCellRenderer extends JWrapperImageComboBox implements TableCellRenderer {

    private static final long serialVersionUID = 0L;

    private Color alternateColor = new Color(245, 245, 220);

    /**
     * Creates a new instance of JCheckBoxTableCellRenderer
     */
    public JWrapperImageComboBoxTableCellRenderer() {
        this(new Vector<WrapperTable>());
    }

    public JWrapperImageComboBoxTableCellRenderer(Vector<WrapperTable> elements) {
        super();
        this.setElements(elements);
        this.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                setPopupVisible(true);
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Border mEmptyBorder = BorderFactory.createEmptyBorder();
        Border mHighLightBorder = UIManager.getBorder("Table.focusCellHighlightBorder");
        Color mFocusCellForeground = table.getSelectionForeground();
        Color mFocusCellBackground = table.getSelectionBackground();
        Color mCellForeground = table.getForeground();
        Color mCellBackground = table.getBackground();
        if (table.isCellEditable(row, column)) {
            if (isSelected) {
                setForeground(mFocusCellForeground);
                setBackground(mFocusCellBackground);
            } else {
                setForeground(mCellForeground);
                setBackground((row % 2 == 0 ? alternateColor : mCellBackground));
            }
        } else {
            if (isSelected) {
                setForeground(Color.GRAY);
                setBackground(mFocusCellBackground);
            } else {
                setForeground(Color.GRAY);
                setBackground(mCellBackground);
            }
        }
        if (hasFocus) {
            setBorder(mHighLightBorder);
        } else {
            setBorder(mEmptyBorder);
        }
        this.setSelectedId((String) value);
        return this;
    }

    public void setSelectedItemModified(Object value) {
        if (value != null) {
            if (value instanceof String) {
                this.setSelectedId((String) value);
            }
        }
    }
}
