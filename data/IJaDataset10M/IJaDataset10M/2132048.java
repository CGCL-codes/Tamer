package org.mypomodoro.gui.reports.export;

import au.com.bytecode.opencsv.CSVReader;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.mypomodoro.buttons.AbstractPomodoroButton;
import org.mypomodoro.model.Activity;
import org.mypomodoro.util.Labels;

/**
 * Panel to import reports
 * 
 * @author Phil Karoo
 */
public class ImportPanel extends JPanel {

    private static final long serialVersionUID = 20110814L;

    protected final ImportInputForm importInputForm = new ImportInputForm();

    private final GridBagConstraints gbc = new GridBagConstraints();

    private boolean importReports = true;

    public ImportPanel() {
        setLayout(new GridBagLayout());
        setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        addImportInputForm();
        addImportButton();
    }

    public ImportPanel(boolean importActivities) {
        this();
        this.importReports = !importActivities;
    }

    private void addImportButton() {
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.1;
        gbc.gridheight = 2;
        JButton importButton = new AbstractPomodoroButton(Labels.getString("ReportListPanel.Import"));
        importButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (importInputForm.getFileName().length() == 0) {
                    importInputForm.initFileName();
                }
                if (importInputForm.getEditableSeparatorText().length() == 0) {
                    importInputForm.initSeparatorComboBox();
                }
                importData();
            }
        });
        add(importButton, gbc);
    }

    private void addImportInputForm() {
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        add(new JScrollPane(importInputForm), gbc);
    }

    private void importData() {
        String filePath = importInputForm.getFileName();
        if (filePath != null && filePath.length() > 0) {
            try {
                if (importInputForm.isFileCSVFormat()) {
                    importCSV(filePath);
                } else if (importInputForm.isFileExcelFormat()) {
                    importExcel(filePath);
                }
                JFrame window = new JFrame();
                String title = Labels.getString("ReportListPanel.Import");
                String message = Labels.getString("ReportListPanel.Data imported", filePath);
                JOptionPane.showConfirmDialog(window, message, title, JOptionPane.DEFAULT_OPTION);
            } catch (Exception ex) {
                JFrame window = new JFrame();
                String title = Labels.getString("Common.Error");
                String message = Labels.getString("ReportListPanel.Import failed. See error log.");
                JOptionPane.showConfirmDialog(window, message, title, JOptionPane.DEFAULT_OPTION);
                writeErrorFile(ex.toString());
            }
        }
    }

    private void writeErrorFile(String error) {
        try {
            FileWriter fstream = new FileWriter("error.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            out.write(error);
            out.close();
        } catch (Exception e) {
        }
    }

    private void importCSV(String fileName) throws Exception {
        CSVReader reader = new CSVReader(new FileReader(fileName), importInputForm.getSeparator(), '\"', importInputForm.isHeaderSelected() ? 1 : 0);
        String[] line;
        while ((line = reader.readNext()) != null) {
            insertData(line);
        }
    }

    private void importExcel(String fileName) throws Exception {
        InputStream myxls = new FileInputStream(fileName);
        HSSFWorkbook book = new HSSFWorkbook(myxls);
        FormulaEvaluator eval = book.getCreationHelper().createFormulaEvaluator();
        HSSFSheet sheet = book.getSheetAt(0);
        if (importInputForm.isHeaderSelected()) {
            sheet.removeRow(sheet.getRow(0));
        }
        for (Row row : sheet) {
            String[] line = new String[16];
            int i = 0;
            for (Cell cell : row) {
                line[i] = getCellContent(cell, eval);
                i++;
            }
            insertData(line);
        }
        myxls.close();
    }

    private String getCellContent(Cell cell, FormulaEvaluator eval) {
        String value = "";
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_STRING:
                value = cell.getStringCellValue();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    CellValue cellValue = eval.evaluate(cell);
                    value = org.mypomodoro.util.DateUtil.getFormatedDate(DateUtil.getJavaDate(cellValue.getNumberValue(), true), importInputForm.getDatePattern());
                } else {
                    value = (int) cell.getNumericCellValue() + "";
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue() ? "1" : "0";
                break;
            default:
                value = "";
        }
        return value;
    }

    private void insertData(String[] line) throws Exception {
        Activity newReport = new Activity(line[13], line[12], line[3], line[14], line[11], Integer.parseInt(line[4]), org.mypomodoro.util.DateUtil.getDate(line[1] + " " + line[2], importInputForm.getDatePattern()), Integer.parseInt(line[5]), Integer.parseInt(line[6]), Integer.parseInt(line[9]), Integer.parseInt(line[10]), line[15], line[0].equals("0") ? false : true, importReports);
        newReport.databaseInsert();
    }
}
