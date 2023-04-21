package itSIMPLE;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 * Implements a cell editor that uses a formatted text field
 * to edit Integer values.
 */
public class IntegerEditor extends DefaultCellEditor {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4171624498370771595L;

    JFormattedTextField ftf;

    NumberFormat integerFormat;

    private Integer minimum, maximum;

    private boolean DEBUG = false;

    public IntegerEditor(int min, int max) {
        super(new JFormattedTextField());
        ftf = (JFormattedTextField) getComponent();
        minimum = new Integer(min);
        maximum = new Integer(max);
        integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter intFormatter = new NumberFormatter(integerFormat);
        intFormatter.setFormat(integerFormat);
        intFormatter.setMinimum(minimum);
        intFormatter.setMaximum(maximum);
        ftf.setFormatterFactory(new DefaultFormatterFactory(intFormatter));
        ftf.setValue(minimum);
        ftf.setHorizontalAlignment(JTextField.TRAILING);
        ftf.setFocusLostBehavior(JFormattedTextField.PERSIST);
        ftf.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "check");
        ftf.getActionMap().put("check", new AbstractAction() {

            /**
			 * 
			 */
            private static final long serialVersionUID = 4763225792443612979L;

            public void actionPerformed(ActionEvent e) {
                if (!ftf.isEditValid()) {
                    if (userSaysRevert()) {
                        ftf.postActionEvent();
                    }
                } else try {
                    ftf.commitEdit();
                    ftf.postActionEvent();
                } catch (java.text.ParseException exc) {
                }
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        JFormattedTextField ftf = (JFormattedTextField) super.getTableCellEditorComponent(table, value, isSelected, row, column);
        ftf.setValue(value);
        return ftf;
    }

    public Object getCellEditorValue() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        Object o = ftf.getValue();
        if (o instanceof Integer) {
            return o;
        } else if (o instanceof Number) {
            return new Integer(((Number) o).intValue());
        } else {
            if (DEBUG) {
                System.out.println("getCellEditorValue: o isn't a Number");
            }
            try {
                return integerFormat.parseObject(o.toString());
            } catch (ParseException exc) {
                System.err.println("getCellEditorValue: can't parse o: " + o);
                return null;
            }
        }
    }

    public boolean stopCellEditing() {
        JFormattedTextField ftf = (JFormattedTextField) getComponent();
        if (ftf.isEditValid()) {
            try {
                ftf.commitEdit();
            } catch (java.text.ParseException exc) {
            }
        } else {
            if (!userSaysRevert()) {
                return false;
            }
        }
        return super.stopCellEditing();
    }

    /** 
     * Lets the user know that the text they entered is 
     * bad. Returns true if the user elects to revert to
     * the last good value.  Otherwise, returns false, 
     * indicating that the user wants to continue editing.
     */
    protected boolean userSaysRevert() {
        Toolkit.getDefaultToolkit().beep();
        ftf.selectAll();
        Object[] options = { "Edit", "Revert" };
        int answer = JOptionPane.showOptionDialog(SwingUtilities.getWindowAncestor(ftf), "The value must be an integer between " + minimum + " and " + maximum + ".\n" + "You can either continue editing " + "or revert to the last valid value.", "Invalid Text Entered", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[1]);
        if (answer == 1) {
            ftf.setValue(ftf.getValue());
            return true;
        }
        return false;
    }
}
