package edu.ucdavis.genomics.metabolomics.util.xls;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import edu.ucdavis.genomics.metabolomics.util.transform.crosstable.object.FormatObject;

/**
 * @author wohlgemuth
 * @version Aug 18, 2003
 * <br>
 * BinBaseDatabase
 * @description Speicht die lines in einer excel tabelle ab
 */
public class SplitToSheets implements Splitter {

    /**
     * DOCUMENT ME!
     */
    private static final int MAX_COLUMN = 256;

    /**
     * DOCUMENT ME!
     *
     * @uml.property name="workbook"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private HSSFWorkbook workbook = new HSSFWorkbook();

    /**
     * DOCUMENT ME!
     *
     * @uml.property name="sheets"
     * @uml.associationEnd multiplicity="(0 -1)"
     */
    private HSSFSheet[] sheets = null;

    /**
     * DOCUMENT ME!
     */
    private boolean firstrun = true;

    /**
     * DOCUMENT ME!
     */
    private int size = 0;

    /**
     * DOCUMENT ME!
     */
    private short rowCounter;

    /**
     * @version Aug 21, 2003
     * @author wohlgemuth
     * <br>
     * @see edu.ucdavis.genomics.metabolomics.binbase.utils.xls.Splitter#getBook()
     */
    public HSSFWorkbook getBook() {
        return this.workbook;
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.utils.xls.Splitter#setHeader(boolean)
     */
    public void setHeader(boolean value) {
    }

    /**
     * @see edu.ucdavis.genomics.metabolomics.binbase.utils.xls.Splitter#isHeader()
     */
    public boolean isHeader() {
        return false;
    }

    /**
     * f?gt eine neue linie in das workbook ein
     * @version Aug 18, 2003
     * @author wohlgemuth
     * <br>
     * @param line
     */
    public void addLine(List line) {
        if (line == null) {
            return;
        }
        if (firstrun == true) {
            size = line.size();
            double count = ((float) size) / (MAX_COLUMN - 1);
            count = Math.ceil(count) + 1;
            sheets = new HSSFSheet[(int) count];
            for (int i = 0; i < sheets.length; i++) {
                sheets[i] = workbook.createSheet();
            }
            firstrun = false;
        }
        for (short i = 0; i < sheets.length; i++) {
            HSSFRow row = sheets[i].createRow(rowCounter);
            short x = 0;
            Iterator it = line.iterator();
            boolean next = it.hasNext();
            while (next == true) {
                if (x < MAX_COLUMN) {
                    try {
                        Object nexto = it.next();
                        String data = null;
                        if (nexto instanceof FormatObject) {
                            data = ((FormatObject) nexto).getValue().toString();
                        } else {
                            data = nexto.toString();
                        }
                        it.remove();
                        HSSFCell cell = row.createCell(x);
                        cell.setCellValue(data);
                        x++;
                    } catch (NoSuchElementException e) {
                        next = false;
                    }
                } else {
                    next = false;
                }
            }
        }
        rowCounter++;
    }

    /**
     * Speichert die Datei als excel workbook und beendet die m?glichkeit des anf?gen von Linie
     * @version Aug 18, 2003
     * @author wohlgemuth
     * <br>
     * @param file
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveBook(File file) throws FileNotFoundException, IOException {
        workbook.write(new FileOutputStream(file));
    }

    /**
     * @version Aug 30, 2003
     * @author wohlgemuth
     * <br>
     * @see edu.ucdavis.genomics.metabolomics.binbase.utils.xls.Splitter#saveBook(java.io.OutputStream)
     */
    public void saveBook(OutputStream stream) throws IOException {
        workbook.write(stream);
    }
}
