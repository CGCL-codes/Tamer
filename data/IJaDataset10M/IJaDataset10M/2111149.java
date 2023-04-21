package coda.io;

import coda.DataFrame;
import coda.gui.CoDaPackConf;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;

/**
 * ExcelAdapter enables Copy-Paste Clipboard functionality on JTables.
 * The clipboard data format used by the adapter is compatible with
 * the clipboard format used by Excel. This provides for clipboard
 * interoperability between enabled JTables and Excel.
 */
public class ExcelAdapter implements ActionListener {

    private String rowstring, value;

    private Clipboard system;

    private StringSelection stsel;

    private JTable jTable1;

    private DataFrame dataFrame;

    /**
    * The Excel Adapter is constructed with a
    * JTable on which it enables Copy-Paste and acts
    * as a Clipboard listener.
    */
    public ExcelAdapter(JTable myJTable, DataFrame df) {
        jTable1 = myJTable;
        dataFrame = df;
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
        KeyStroke copy_mac = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.META_MASK, false);
        KeyStroke paste = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK, false);
        jTable1.registerKeyboardAction(this, "Copy", copy, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Copy_Mac", copy_mac, JComponent.WHEN_FOCUSED);
        jTable1.registerKeyboardAction(this, "Paste", paste, JComponent.WHEN_FOCUSED);
        system = Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    /**
    * Public Accessor methods for the Table on which this adapter acts.
    */
    public JTable getJTable() {
        return jTable1;
    }

    public void setJTable(JTable jTable1) {
        this.jTable1 = jTable1;
    }

    /**
    * This method is activated on the Keystrokes we are listening to
    * in this implementation. Here it listens for Copy and Paste ActionCommands.
    * Selections comprising non-adjacent cells result in invalid selection and
    * then copy action cannot be performed.
    * Paste is done by aligning the upper left corner of the selection with the
    * 1st element in the current selection of the JTable.
    */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().compareTo("Copy") == 0 || e.getActionCommand().compareTo("Copy_Mac") == 0) {
            StringBuilder sbf = new StringBuilder();
            int numcols = jTable1.getSelectedColumnCount();
            int nrows = jTable1.getRowCount();
            int numrows = jTable1.getSelectedRowCount();
            int[] rowsselected = jTable1.getSelectedRows();
            int[] colsselected = jTable1.getSelectedColumns();
            String names[] = new String[numcols];
            for (int j = 0; j < numcols; j++) {
                names[j] = jTable1.getColumnName(colsselected[j]);
                sbf.append(names[j]);
                if (j < numcols - 1) sbf.append("\t");
            }
            sbf.append("\n");
            for (int i = 0; i < nrows; i++) {
                for (int j = 0; j < numcols; j++) {
                    if (dataFrame.get(names[j]).get(i) != null) {
                        if (dataFrame.get(names[j]).isNumeric()) {
                            sbf.append(CoDaPackConf.getDecimalExportFormat().format(dataFrame.getNumericalData(names[j])[i]));
                        } else {
                            sbf.append(dataFrame.getCategoricalData(names[j])[i]);
                        }
                    } else {
                        sbf.append("");
                    }
                    if (j < numcols - 1) sbf.append("\t");
                }
                sbf.append("\n");
            }
            stsel = new StringSelection(sbf.toString());
            system = Toolkit.getDefaultToolkit().getSystemClipboard();
            system.setContents(stsel, stsel);
        }
    }
}
