package nu.lazy8.ledger.port;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.Ostermiller.util.CSVPrint;
import com.Ostermiller.util.CSVPrinter;
import com.Ostermiller.util.ExcelCSVPrinter;
import nu.lazy8.ledger.forms.DataExchangeForm;
import nu.lazy8.ledger.jdbc.DataConnection;
import nu.lazy8.ledger.main.Lazy8Ledger;
import nu.lazy8.util.gen.StringBinaryConverter;
import nu.lazy8.util.gen.Translator;
import nu.lazy8.util.gen.WorkingDialog;
import nu.lazy8.util.help.HelpedButton;
import nu.lazy8.util.help.HelpedLabel;
import org.gjt.sp.util.Log;

/**
 *  Description of the Class
 *
 * @author     Lazy Eight Data HB, Thomas Dilts
 * @created    den 2 juli 2004
 */
public class CsvExportPane extends DataExchangeForm {

    protected int CompId;

    private final boolean[] defaultState;

    private char delimiter;

    private CsvFormatNumPane formatNumPane;

    public String fromDate;

    private boolean isExcel;

    private final JList list;

    private CsvOptionsPane options;

    private File outputFile;

    private String[] saveFields;

    protected String tableName;

    public String toDate;

    private final JFrame view;

    /**
   *Constructor for the CsvExportPane object
   *
   * @param  views          Description of the Parameter
   * @param  frameName      Description of the Parameter
   * @param  saveFields     Description of the Parameter
   * @param  defaultStates  Description of the Parameter
   * @param  tableName      Description of the Parameter
   * @param  showDates      Description of the Parameter
   * @param  showWholeNums  Description of the Parameter
   */
    public CsvExportPane(JFrame views, String frameName, String[] saveFields, boolean[] defaultStates, String tableName, boolean showDates, boolean showWholeNums) {
        super("account", views, frameName);
        Log.log(Log.DEBUG, this, "CsvExportPane Start=" + frameName);
        this.view = views;
        this.tableName = tableName;
        this.saveFields = saveFields;
        this.defaultState = defaultStates;
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setDialogTitle("Select the csv file");
        if (fileDialog.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    Exit();
                }
            });
            list = new JList(createData(saveFields));
            return;
        }
        outputFile = fileDialog.getSelectedFile();
        setLayout(new BorderLayout());
        JTabbedPane tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new HelpedLabel(Translator.getTranslation("Select Fields to export"), "fields", "export", view), BorderLayout.NORTH);
        list = new JList(createData(saveFields));
        list.setCellRenderer(new CheckListRenderer());
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                CheckableItem item = (CheckableItem) list.getModel().getElementAt(index);
                item.setSelected(!item.isSelected());
                Rectangle rect = list.getCellBounds(index, index);
                list.repaint(rect);
            }

            public void mouseEntered(MouseEvent e) {
                Lazy8Ledger.ShowContextHelp(view, "export", "fields");
            }
        });
        JScrollPane sp = new JScrollPane(list);
        centerPanel.add(sp, BorderLayout.CENTER);
        JPanel selectingButtonsPanel = new JPanel();
        selectingButtonsPanel.setLayout(new GridLayout(1, 3));
        JButton butDefault = new HelpedButton(Translator.getTranslation("Default selection"), "default", "export", view);
        butDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDefaultList();
            }
        });
        selectingButtonsPanel.add(butDefault);
        JButton butSelectAll = new HelpedButton(Translator.getTranslation("Select all"), "selectall", "export", view);
        butSelectAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int i = 0; i < list.getModel().getSize(); i++) {
                    CheckableItem item = (CheckableItem) list.getModel().getElementAt(i);
                    item.setSelected(true);
                }
                Rectangle rect = list.getCellBounds(0, list.getModel().getSize() - 1);
                list.repaint(rect);
            }
        });
        selectingButtonsPanel.add(butSelectAll);
        JButton butUnselect = new HelpedButton(Translator.getTranslation("Unselect all"), "unselectall", "export", view);
        butUnselect.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                for (int i = 0; i < list.getModel().getSize(); i++) {
                    CheckableItem item = (CheckableItem) list.getModel().getElementAt(i);
                    item.setSelected(false);
                }
                Rectangle rect = list.getCellBounds(0, list.getModel().getSize() - 1);
                list.repaint(rect);
            }
        });
        selectingButtonsPanel.add(butUnselect);
        centerPanel.add(selectingButtonsPanel, BorderLayout.SOUTH);
        tabbedPane.addTab(Translator.getTranslation("Select Fields"), centerPanel);
        options = new CsvOptionsPane(view, showDates);
        tabbedPane.addTab(Translator.getTranslation("Options"), options);
        formatNumPane = new CsvFormatNumPane(view, showDates, showWholeNums);
        tabbedPane.addTab(Translator.getTranslation("Field formats"), formatNumPane);
        JPanel southVisualPanel = new JPanel();
        southVisualPanel.setLayout(new GridLayout(2, 1));
        southVisualPanel.add(new JPanel());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3));
        JButton butHelp = new HelpedButton(Translator.getTranslation("Help"), "help", "export", view);
        butHelp.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Lazy8Ledger.ShowHelp(view, "export", "");
            }
        });
        buttonPanel.add(butHelp);
        JButton butExport = new HelpedButton(Translator.getTranslation("Export"), "exportbutton", "export", view);
        butExport.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boolean[] selectedFields;
                selectedFields = new boolean[list.getModel().getSize()];
                for (int i = 0; i < list.getModel().getSize(); i++) {
                    CheckableItem item = (CheckableItem) list.getModel().getElementAt(i);
                    selectedFields[i] = item.isSelected();
                }
                isExcel = options.fileTypeButton[1].isSelected();
                if (options.delimiterButton[0].isSelected()) {
                    delimiter = ',';
                } else {
                    delimiter = ';';
                }
                CompId = ((Integer) options.cc.comboBox.getSelectedItemsKey()).intValue();
                fromDate = options.fromDateTextField.getSQLDateValue();
                toDate = options.toDateTextField.getSQLDateValue();
                DoExport(selectedFields);
            }
        });
        buttonPanel.add(butExport);
        JButton butExit = new HelpedButton(Translator.getTranslation("Exit"), "exit", "export", view);
        butExit.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Exit();
            }
        });
        buttonPanel.add(butExit);
        southVisualPanel.add(buttonPanel);
        add(southVisualPanel, BorderLayout.SOUTH);
        Log.log(Log.DEBUG, this, "CsvExportPane End=" + frameName);
        setDefaultList();
    }

    /**
   *  Description of the Method
   *
   * @param  selectedFields  Description of the Parameter
   */
    private void DoExport(boolean[] selectedFields) {
        CSVPrint printer;
        OutputStream out;
        String sSelectString = getSelectString(selectedFields, fromDate, toDate);
        WorkingDialog workDialog = new WorkingDialog(null);
        workDialog.setVisible(true);
        workDialog.SetProgress(0);
        try {
            out = new FileOutputStream(outputFile);
            if (isExcel) {
                printer = new ExcelCSVPrinter(out);
            } else {
                printer = new CSVPrinter(out);
            }
            printer.changeDelimiter(delimiter);
            DataConnection dc = DataConnection.getInstance(view);
            if (dc == null || !dc.bIsConnectionMade) {
                return;
            }
            Statement st = dc.con.createStatement();
            ResultSet rs = st.executeQuery(dc.filterSQL(sSelectString));
            ResultSetMetaData rsmd = rs.getMetaData();
            if (rsmd == null) {
                return;
            }
            int iNumColumns = rsmd.getColumnCount();
            String[] outputStrings = new String[iNumColumns];
            int numRows = 0;
            if (rs.next()) {
                rs.last();
                numRows = rs.getRow();
            }
            rs.beforeFirst();
            int rowCountShowProgress = 0;
            int rowNum = 0;
            while (rs.next()) {
                rowNum++;
                rowCountShowProgress++;
                if (rowCountShowProgress >= 10) {
                    workDialog.SetProgress(rowNum * 100 / numRows);
                    rowCountShowProgress = 0;
                }
                for (int i = 1; i <= iNumColumns; i++) {
                    switch(rsmd.getColumnType(i)) {
                        case Types.VARCHAR:
                            outputStrings[i - 1] = rs.getString(i);
                            if (outputStrings[i - 1] == null) {
                                outputStrings[i - 1] = "";
                            }
                            if (rsmd.getColumnName(i).equals("TypeName")) {
                                outputStrings[i - 1] = Translator.getTranslation(outputStrings[i - 1]);
                            }
                            break;
                        case Types.INTEGER:
                            outputStrings[i - 1] = formatNumPane.numPanels[formatNumPane.SHOW_INTEGER].format(new Integer(rs.getInt(i)));
                            break;
                        case Types.NUMERIC:
                        case Types.REAL:
                        case Types.FLOAT:
                        case Types.DECIMAL:
                        case Types.DOUBLE:
                            outputStrings[i - 1] = formatNumPane.numPanels[formatNumPane.SHOW_WHOLE_NUMBER].format(new Double(rs.getDouble(i)));
                            break;
                        case Types.DATE:
                            outputStrings[i - 1] = formatNumPane.numPanels[formatNumPane.SHOW_DATE].format(rs.getDate(i));
                            break;
                        default:
                            outputStrings[i - 1] = StringBinaryConverter.BinaryToString(rs.getBytes(i));
                            break;
                    }
                    Log.log(Log.DEBUG, this, "outputStrings[i-1]=" + outputStrings[i - 1]);
                }
                printer.println(outputStrings);
            }
            out.close();
        } catch (Exception e) {
            Log.log(Log.ERROR, this, "Error printing CSV file=" + e);
        }
        workDialog.dispose();
    }

    /**
   *  Description of the Method
   *
   * @param  strs  Description of the Parameter
   * @return       Description of the Return Value
   */
    private CheckableItem[] createData(String[] strs) {
        int n = strs.length;
        CheckableItem[] items = new CheckableItem[n];
        for (int i = 0; i < n; i++) {
            items[i] = new CheckableItem(strs[i]);
        }
        return items;
    }

    /**
   *  Gets the selectString attribute of the CsvExportPane object
   *
   * @param  selectedFields  Description of the Parameter
   * @param  fromDate        Description of the Parameter
   * @param  toDate          Description of the Parameter
   * @return                 The selectString value
   */
    public String getSelectString(boolean[] selectedFields, String fromDate, String toDate) {
        String sSelectString;
        sSelectString = "SELECT ";
        String fieldList = "";
        for (int i = 0; i < selectedFields.length; i++) {
            if (selectedFields[i]) {
                if (fieldList.length() != 0) {
                    fieldList = fieldList + ",";
                }
                fieldList = fieldList + tableName + "." + saveFields[i] + " ";
            }
        }
        sSelectString = sSelectString + fieldList + " FROM " + tableName + " WHERE " + tableName + ".CompId=" + CompId + " ORDER BY " + fieldList;
        Log.log(Log.DEBUG, this, "sSelectString=" + sSelectString);
        return sSelectString;
    }

    /**
   *  Sets the defaultList attribute of the CsvExportPane object
   */
    private void setDefaultList() {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            CheckableItem item = (CheckableItem) list.getModel().getElementAt(i);
            item.setSelected(defaultState[i]);
        }
        Rectangle rect = list.getCellBounds(0, list.getModel().getSize() - 1);
        list.repaint(rect);
    }

    class CheckListRenderer extends JCheckBox implements ListCellRenderer {

        /**
     *Constructor for the CheckListRenderer object
     */
        public CheckListRenderer() {
            setBackground(UIManager.getColor("List.textBackground"));
            setForeground(UIManager.getColor("List.textForeground"));
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(((CheckableItem) value).isSelected());
            setFont(list.getFont());
            setText(value.toString());
            return this;
        }
    }

    class CheckableItem {

        private boolean isSelected;

        private String str;

        /**
     *Constructor for the CheckableItem object
     *
     * @param  str  Description of the Parameter
     */
        public CheckableItem(String str) {
            this.str = str;
            isSelected = false;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean b) {
            isSelected = b;
        }

        public String toString() {
            return str;
        }
    }
}
