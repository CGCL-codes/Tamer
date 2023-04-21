package coda.io;

import coda.Variable;
import coda.DataFrame;
import coda.Factor;
import coda.FactorVar;
import coda.Numeric;
import coda.NumericVar;
import coda.Zero;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author marc
 */
public class ImportDataXLS implements ImportData {

    /**
     *
     */
    public static String NON_AVAILABLE = "NA";

    /**
     *
     */
    public static String NON_DETECTED = "<";

    boolean headers = true;

    private int firstRow = 0;

    /**
     *
     * @param fname containing the path to the Excel file
     * 
     * @return A DataFrame class containing the gathered data
     */
    public DataFrame importData(String fname) {
        DataFrame df = new DataFrame();
        InputStream inp = null;
        try {
            inp = new FileInputStream(fname);
        } catch (FileNotFoundException e) {
            System.out.println("File not found in the specified path.");
            return null;
        }
        Workbook wb = null;
        if (fname.endsWith(".xls")) {
            try {
                wb = new HSSFWorkbook(inp);
            } catch (IOException ex) {
                Logger.getLogger(ImportDataXLS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            System.out.println("Format not recognized.");
            return null;
        }
        int nsheet = 0;
        if (wb.getNumberOfSheets() > 1) {
            String name_sheet[] = new String[wb.getNumberOfSheets()];
            for (int i = 0; i < wb.getNumberOfSheets(); i++) {
                name_sheet[i] = wb.getSheetName(i);
            }
            final JDialog dialog = new JDialog();
            final JPanel panel = new JPanel();
            final JList sheet_selection = new JList(name_sheet);
            final JScrollPane scroll = new JScrollPane();
            final JButton accept = new JButton("Accept");
            accept.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    dialog.dispose();
                }
            });
            sheet_selection.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        dialog.dispose();
                    }
                }
            });
            scroll.setViewportView(sheet_selection);
            panel.setLayout(new BorderLayout());
            panel.add(scroll, BorderLayout.CENTER);
            panel.add(accept, BorderLayout.SOUTH);
            dialog.setTitle("Choose one sheet");
            dialog.setModal(true);
            dialog.setSize(200, 200);
            dialog.getContentPane().add(panel);
            dialog.setLocationRelativeTo(null);
            dialog.setVisible(true);
            nsheet = sheet_selection.getSelectedIndex();
        }
        Sheet sheet = wb.getSheetAt(nsheet);
        ArrayList<Integer> columns = new ArrayList<Integer>();
        ArrayList<Variable> variables = new ArrayList<Variable>();
        if (headers) {
            int position = firstRow;
            Row row = sheet.getRow(firstRow);
            Iterator<Cell> cells = row.cellIterator();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String name = null;
                if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    name = cell.getRichStringCellValue().getString().trim();
                }
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    name = Double.valueOf(cell.getNumericCellValue()).toString();
                }
                if (name != null) {
                    columns.add(cell.getColumnIndex());
                    variables.add(new NumericVar(name));
                    position++;
                }
            }
        }
        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        Iterator<Integer> it = columns.iterator();
        int k = 0;
        while (it.hasNext()) {
            int ncol = it.next();
            Variable var = variables.get(k);
            int nrow = (headers ? firstRow + 1 : firstRow + 0);
            boolean cont = true;
            String str;
            while (cont) {
                Row row = sheet.getRow(nrow);
                Cell cell;
                CellValue cellValue;
                try {
                    cell = row.getCell(ncol);
                    cellValue = evaluator.evaluate(cell);
                    if (var.isFactor()) {
                        FactorVar facVar = (FactorVar) var;
                        if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
                            facVar.add(new Factor(cellValue.getStringValue().trim(), facVar));
                        } else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            facVar.add(new Factor(Double.valueOf(cellValue.getNumberValue()).toString(), facVar));
                        }
                    } else {
                        NumericVar numVar = (NumericVar) var;
                        if (cellValue.getCellType() == Cell.CELL_TYPE_STRING) {
                            str = cellValue.getStringValue().trim();
                            if (str.compareTo(NON_AVAILABLE) == 0) {
                                numVar.add(new Numeric(Double.NaN));
                            } else if (str.startsWith(NON_DETECTED)) {
                                numVar.add(new Zero(Double.parseDouble(str.substring(1, str.length()).replace(",", "."))));
                            } else {
                                var = numVar.factorize();
                                variables.remove(k);
                                variables.add(k, var);
                                var.add(new Factor(str, (FactorVar) var));
                            }
                        } else if (cellValue.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            double val = Double.valueOf(cellValue.getNumberValue());
                            if (val == 0) {
                                numVar.add(new Zero(Double.NaN));
                            } else {
                                numVar.add(new Numeric(val));
                            }
                        }
                    }
                } catch (NullPointerException ex) {
                    cont = false;
                }
                nrow++;
            }
            df.put(var.getName(), var);
            k++;
        }
        return df;
    }

    public void exportData(String fname, DataFrame dataframe) {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("data");
        Row row = sheet.createRow((short) 0);
        short col = 0;
        for (int i = 0; i < dataframe.size(); i++) {
            row.createCell(col++).setCellValue(dataframe.get(i).getName());
        }
        Variable vars[] = new Variable[dataframe.size()];
        int nrows = 0;
        int nrow[] = new int[dataframe.size()];
        for (int i = 0; i < dataframe.size(); i++) {
            vars[i] = dataframe.get(dataframe.get(i).getName());
            nrow[i] = vars[i].size();
            nrows = Math.max(nrows, nrow[i]);
        }
        short r = 1;
        for (int i = 0; i < nrows; i++) {
            row = sheet.createRow(r++);
            col = 0;
            for (int j = 0; j < dataframe.size(); j++) {
                Cell cell = row.createCell(col++);
                if (r <= nrow[j] + 1) {
                    if (vars[j].isFactor()) {
                        cell.setCellValue((String) vars[j].get(i));
                    } else {
                        Object obj = vars[j].get(i);
                        double d = (Double) obj;
                        if (Double.isNaN(d)) {
                            cell.setCellValue("NA");
                        } else {
                            cell.setCellValue((Double) obj);
                        }
                    }
                }
            }
        }
        if (!fname.endsWith(".xls")) fname = fname.concat(".xls");
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(fname);
            try {
                wb.write(fileOut);
                fileOut.close();
            } catch (IOException ex) {
                Logger.getLogger(ImportDataXLS.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ImportDataXLS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
