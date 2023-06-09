package com.nexusbpm.services.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;

/**
 * .
 *
 * @author catch23
 */
public class ExcelPOIObject extends ExcelObject {

    private static final transient Log LOG = LogFactory.getLog(ExcelPOIObject.class);

    private HSSFWorkbook book = null;

    private HSSFSheet sheet = null;

    /**
     * .
     */
    protected HSSFRow currentRow;

    public ExcelPOIObject(String file_name, String sheet_name, String template_name) throws Exception {
        super(file_name, sheet_name, template_name);
        FileInputStream fis = new FileInputStream(file_name);
        if (null == fis) {
            throw new Exception("Couldn't load POI file - file=" + file_name + ";sheet=" + sheet_name + ";template=" + template_name);
        }
        POIFSFileSystem fs = new POIFSFileSystem(fis);
        if (null == fs) {
            throw new Exception("Couldn't load POI file - file=" + file_name + ";fis=" + fis.available() + ";sheet=" + sheet_name + ";template=" + template_name);
        }
        try {
            book = new HSSFWorkbook(fs);
        } catch (NullPointerException e) {
            throw new Exception("Couldn't load POI file - file=" + file_name + ";fis=" + fis.available() + ";shortDesc=" + fs.getShortDescription() + ";sheet=" + sheet_name + ";template=" + template_name, e);
        }
        sheet = book.getSheet(getSheetName());
        if (sheet == null) {
            if (null != template_name && template_name.length() > 0) {
                int templateIndex = book.getSheetIndex(template_name);
                sheet = book.cloneSheet(templateIndex);
                LOG.info("Creating Template in ExcelPOIObject:" + templateIndex);
                int newSheetIndex = book.getSheetIndex(template_name + "(2)");
                book.setSheetName(newSheetIndex, sheet_name);
            } else {
                LOG.info("Creating Sheet in ExcelPOIObject:" + getSheetName());
                sheet = book.createSheet(getSheetName());
            }
        }
    }

    /**
     * .
     *
     * @param columnOffset
     * @param columnLimit
     * @param rowValues
     * @throws Exception
     */
    public void insertRow(short columnOffset, int columnLimit, List rowValues) throws Exception {
        int size = rowValues.size();
        if (checkOutOfBound(columnOffset, size)) {
            throw new ExcelOutOfBoundException(columnOffset + size);
        }
        Iterator iter = rowValues.iterator();
        short index = columnOffset;
        int i = 0;
        while (iter.hasNext()) {
            Object obj = iter.next();
            HSSFCell cell = currentRow.getCell((short) (index));
            if (cell == null) {
                cell = currentRow.createCell((short) (index));
            }
            if (obj == null) {
                cell.setCellValue("null");
            } else if (Number.class.isAssignableFrom(obj.getClass())) {
                cell.setCellValue(((Number) obj).doubleValue());
            } else {
                cell.setCellValue(obj.toString());
            }
            index++;
            i++;
            if (i >= columnLimit) {
                break;
            }
        }
    }

    /**
     * .
     *
     * @param start
     * @param len
     * @return boolean
     */
    public boolean checkOutOfBound(int start, int len) {
        boolean ret = false;
        if (start + len - 1 > 256) {
            ret = true;
        }
        return ret;
    }

    /**
     * .
     *
     * @throws Exception
     */
    public void save() throws Exception {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(getFileName());
            book.write(fileOut);
        } finally {
        }
    }

    /**
     * .
     *
     * @param table
     * @param anchorString
     * @param rowLimit
     * @param colLimit
     * @throws Exception
     */
    public void insertTableAtAnchor(String table, String anchorString, int rowLimit, int colLimit) throws Exception {
        LOG.info("POI anchor=" + anchorString + ";rowLimit=" + rowLimit + ";colLimit=" + colLimit);
        int rowOffset = 0;
        short columnOffset = 0;
        this.setColLimit(colLimit);
        Anchor anchor = new Anchor(anchorString);
        rowOffset = anchor.getRowIndex() - 1;
        columnOffset = (short) (anchor.getColIndex() - 1);
        int maxRows = rowLimit;
        for (int i = 0; i < maxRows; i++) {
        }
    }
}
