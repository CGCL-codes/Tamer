package com.as.quickload.file.filemodule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator.CellValue;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.as.quickload.Configuration;
import com.as.quickload.LogHelper;
import com.as.quickload.file.FileReaderIfc;

/**
 * Excel File Reader.
 */
public class ExcelFileReaderImpl implements FileReaderIfc {

    private String fileName;

    private ArrayList<String> worksheets = new ArrayList<String>();

    private int noOfRows;

    private HSSFSheet curSheet;

    private int rowNum = -1;

    private HSSFRow curRow;

    private boolean readNumberAsText;

    private Configuration conf;

    private HSSFFormulaEvaluator evaluator;

    public ExcelFileReaderImpl() {
    }

    public boolean loadFile(final String fileName) throws IOException, FileNotFoundException {
        resetFile();
        this.fileName = fileName;
        File f = new File(fileName);
        if (!f.isFile() || !f.canRead()) {
            LogHelper.getInstance().printError(" Not a Readable File : " + fileName);
            return false;
        }
        InputStream is = new BufferedInputStream(new FileInputStream(fileName));
        POIFSFileSystem excel = null;
        try {
            excel = new POIFSFileSystem(is);
        } catch (Exception e) {
            LogHelper.getInstance().printError(" Not a Valid Excel File : " + fileName);
            return false;
        }
        HSSFWorkbook wkbook = new HSSFWorkbook(excel);
        int noOfWorksheets = wkbook.getNumberOfSheets();
        if (noOfWorksheets != 0) {
            for (int count = 0; count < noOfWorksheets; ++count) {
                worksheets.add(wkbook.getSheetName(count));
            }
        }
        is.close();
        applyConfiguration(Configuration.getInstance());
        return true;
    }

    private void resetFile() {
        fileName = null;
        worksheets = new ArrayList<String>();
        noOfRows = 0;
        curSheet = null;
        rowNum = -1;
        curRow = null;
    }

    public void loadWorksheet(int index) throws FileNotFoundException, IOException {
        resetSheet();
        InputStream is = new FileInputStream(fileName);
        POIFSFileSystem excel = new POIFSFileSystem(is);
        HSSFWorkbook wkbook = new HSSFWorkbook(excel);
        curSheet = wkbook.getSheetAt(index);
        noOfRows = curSheet.getLastRowNum();
        evaluator = new HSSFFormulaEvaluator(curSheet, wkbook);
    }

    private void resetSheet() {
        noOfRows = 0;
        curSheet = null;
        rowNum = -1;
        curRow = null;
        evaluator = null;
    }

    public Collection<String> getWorksheets() {
        return worksheets;
    }

    public boolean next() {
        if (rowNum + 1 > noOfRows) return false;
        HSSFRow record = curSheet.getRow(++rowNum);
        curRow = record;
        return true;
    }

    @SuppressWarnings("unchecked")
    public Vector<String> getRow() {
        Vector<String> objects = new Vector<String>();
        if (curRow != null && curRow.getLastCellNum() != -1) {
            objects.setSize(curRow.getLastCellNum());
            Iterator itr = curRow.cellIterator();
            while (itr.hasNext()) {
                HSSFCell cell = (HSSFCell) itr.next();
                evaluator.setCurrentRow(curRow);
                CellValue cellValue = evaluator.evaluate(cell);
                int type = cellValue.getCellType();
                if ("YES".equalsIgnoreCase(conf.read(Configuration.SKIP_HIDDEN_COLS)) && curSheet.isColumnHidden(cell.getCellNum())) {
                    objects.remove(cell.getCellNum());
                    objects.add(cell.getCellNum(), FileReaderIfc.HIDDEN);
                } else if (type == HSSFCell.CELL_TYPE_STRING) {
                    objects.remove(cell.getCellNum());
                    objects.add(cell.getCellNum(), cellValue.getRichTextStringValue().getString());
                } else if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat(conf.read(Configuration.DATE_FORMAT));
                    String dt = sdf.format(cell.getDateCellValue());
                    objects.remove(cell.getCellNum());
                    objects.add(cell.getCellNum(), dt);
                } else if (type == HSSFCell.CELL_TYPE_NUMERIC) {
                    objects.remove(cell.getCellNum());
                    String tmpValue = String.valueOf(cellValue.getNumberValue());
                    if (readNumberAsText) {
                        if ((int) cellValue.getNumberValue() == cellValue.getNumberValue()) {
                            tmpValue = String.valueOf((int) cellValue.getNumberValue());
                        }
                    }
                    objects.add(cell.getCellNum(), tmpValue);
                } else if (type == HSSFCell.CELL_TYPE_BLANK) {
                    objects.remove(cell.getCellNum());
                    objects.add(cell.getCellNum(), null);
                } else {
                    objects.remove(cell.getCellNum());
                    objects.add(cell.getCellNum(), FileReaderIfc.INDETERMINATE);
                }
            }
        }
        return objects;
    }

    private void applyConfiguration(Configuration aConf) {
        this.conf = aConf;
        readNumberAsText = "YES".equalsIgnoreCase(conf.read(Configuration.READ_NUM_AS_TEXT));
    }
}
