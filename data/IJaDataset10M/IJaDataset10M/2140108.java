package sf.controller;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.util.regex.PatternSyntaxException;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import org.jdesktop.xswingx.JXSearchField;
import sf.model.SQLTableModel;
import sf.view.StandardForm;

public class Controller {

    private StandardForm parentForm;

    public Controller(StandardForm parentForm) {
        this.parentForm = parentForm;
    }

    public void processRowSelectionChanged(ListSelectionEvent e) {
        SQLTableModel model = (SQLTableModel) parentForm.getBrowser().getTable().getModel();
        int selectedRow = parentForm.getBrowser().getSelectedRow();
        int columnCount = model.getColumnCount();
        if (selectedRow == -1) {
            parentForm.getDetailsPane().clearAllFields();
        } else {
            for (int i = 0; i < columnCount; i++) {
                String columnName = model.getColumnName(i);
                Object value = model.getValueAt(selectedRow, i);
                parentForm.getDetailsPane().setFieldValue(columnName, value);
            }
        }
    }

    public void processTableClick(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
            JTable table = (JTable) e.getSource();
            int red = table.getSelectedRow();
            System.out.println("Dupli klik na red: " + red);
        }
    }

    public ActionListener getOpenImageListener() {
        ActionListener actionListener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        };
        return actionListener;
    }

    private FocusListener fieldFocusListener = new FocusListener() {

        private Border oldBorder;

        public void focusGained(FocusEvent e) {
            JComponent comp = (JComponent) e.getSource();
            oldBorder = comp.getBorder();
            Insets insets = oldBorder.getBorderInsets(comp);
            comp.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(insets.top - 1, insets.left - 1, insets.bottom - 1, insets.right - 1)));
        }

        public void focusLost(FocusEvent e) {
            JComponent comp = (JComponent) e.getSource();
            comp.setBorder(oldBorder);
        }
    };

    public FocusListener getFieldFocusListener() {
        return fieldFocusListener;
    }

    private ActionListener quickFilterListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            String text = ((JXSearchField) e.getSource()).getText();
            if (text.length() == 0) {
                parentForm.getBrowser().getTableRowSorter().setRowFilter(null);
            } else {
                try {
                    parentForm.getBrowser().getTableRowSorter().setRowFilter(RowFilter.regexFilter(text));
                } catch (PatternSyntaxException pse) {
                    System.err.println("Bad regex pattern.");
                }
            }
        }
    };

    public ActionListener getQuickFilterListener() {
        return quickFilterListener;
    }
}
