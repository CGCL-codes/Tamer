package org.javahispano.dbmt.migrations;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import static java.util.logging.Level.*;
import java.util.logging.*;
import org.medfoster.sqljep.*;
import org.javahispano.dbmt.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Source for reading tables from XLS files.
 *
 * <p>
 * <code>MigrationXLSSource</code> has three parameters
 * <table border=1  cellSpacing=0 summary="MigrationXLSSource parameters">
 * <tr>
 * <th>Name</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>trim</td>
 * <td>If <CODE>true</CODE> then removes leading and trailing whitespace; do nothing otherwise</td>
 *</tr>
 * <tr>
 * <td>file</td>
 * <td>If this parametes is specified then the file with name 'URL+file' for all steps will be used. 
 * Migration opens file in the first step and closes in the last step.
 * If this parameter is omited then each step will use own file with name equals to step's name</td>
 * </tr>
 * </table>
 * <p>
 * Parameters are passed by {@link MigrationXLSSource#setSource(String driver, String url)}.
 *
 * @author <a href="mailto:alexey.gaidukov@gmail.com">Alexey Gaidukov</a>
 * @see Step
 */
public class MigrationXLSSource implements MigrationSource {

    private static final Logger logger = Logger.getLogger("org.javahispano.dbmt");

    private static final String STR_TRIM = "trim=";

    private static final String PARAM_FILE = "file=";

    private String url;

    private boolean globalWorkbook = false;

    private HSSFWorkbook workbook = null;

    private Comparable[] record = null;

    private boolean trim = false;

    private boolean readEmptyFile = false;

    private boolean stepWithoutWorkbook = false;

    private HashMap<String, Integer> columnMapping = new HashMap<String, Integer>();

    private DbmtJEP where = null;

    private Step step = null;

    private ArrayList<XLSMigrationArea> fromAreas = new ArrayList<XLSMigrationArea>();

    private HSSFSheet[] sheets = null;

    private boolean limitedArea;

    private boolean continueReading = true;

    /**
	 *
	 * @param driver MigrationXLSSourse parameters.
	 * Parameters delimeter is a space symbol.
	 * @param url of the XLS file.
	 * Full path is <CODE>url+Step.getSourceTable()</CODE>.
	 */
    public void setSource(String driver, String url, String username, String password) throws MigrationException {
        this.url = (url != null) ? url : "";
        String[] param = driver.split("\\s+");
        String fileName = null;
        for (String p : param) {
            if (STR_TRIM.regionMatches(true, 0, p, 0, STR_TRIM.length())) {
                trim = Boolean.valueOf(p.substring(STR_TRIM.length()));
            } else if (PARAM_FILE.regionMatches(true, 0, p, 0, PARAM_FILE.length())) {
                fileName = p.substring(PARAM_FILE.length());
            }
        }
        if (fileName != null && workbook == null) {
            openWorkbook(url + fileName);
            globalWorkbook = true;
        }
    }

    public void setInput(HSSFWorkbook workbook) {
        this.workbook = workbook;
        globalWorkbook = true;
    }

    public void initSource(String source, Step step) throws MigrationException {
        this.step = step;
        columnMapping.clear();
        if (globalWorkbook || source != null && source.length() > 0) {
            if (workbook == null) {
                openWorkbook(url + source);
                globalWorkbook = false;
            }
            fromAreas.clear();
            limitedArea = false;
            for (Field field : step.getFields()) {
                try {
                    XLSMigrationArea area = new XLSMigrationArea(field.getFrom());
                    String alias = field.getFromAlias();
                    int ind = fromAreas.size();
                    field.setColumnIndex(ind);
                    if (alias != null) {
                        columnMapping.put(alias, ind);
                    }
                    if (area.isLimited()) {
                        limitedArea = true;
                    }
                    fromAreas.add(area);
                } catch (IllegalArgumentException e) {
                    field.setJEP(DbmtJEP.DUMMY_JEP);
                }
            }
            record = new Comparable[fromAreas.size()];
            sheets = new HSSFSheet[fromAreas.size()];
            continueReading = true;
            for (int i = 0; i < fromAreas.size(); i++) {
                String sheetName = fromAreas.get(i).getSheetName();
                if (sheetName != null) {
                    sheets[i] = workbook.getSheet(sheetName);
                    if (sheets[i] == null) {
                        throw new MigrationException("Can't find sheet '" + sheetName + "'");
                    }
                } else {
                    sheets[i] = workbook.getSheetAt(0);
                }
            }
            stepWithoutWorkbook = true;
            for (Field field : step.getFields()) {
                if (field.getJEP() == DbmtJEP.DUMMY_JEP) {
                    String expr = field.getFrom();
                    DbmtJEP jep = new DbmtJEP(expr, step);
                    try {
                        jep.parseExpression(columnMapping);
                    } catch (ParseException e) {
                        throw new MigrationException(e);
                    }
                    jep.setRow(record);
                    field.setJEP(jep);
                } else {
                    stepWithoutWorkbook = false;
                }
            }
        } else {
            readEmptyFile = false;
            stepWithoutWorkbook = true;
            for (Field field : step.getFields()) {
                DbmtJEP jep = new DbmtJEP(field.getFrom(), step);
                try {
                    jep.parseExpression(columnMapping);
                } catch (ParseException e) {
                    throw new MigrationException(e);
                }
                field.setJEP(jep);
            }
        }
    }

    private void openWorkbook(String filename) throws MigrationException {
        logger.fine("Open XLS:" + filename);
        File file = new File(filename);
        try {
            InputStream inputStream = new FileInputStream(file);
            POIFSFileSystem fs = new POIFSFileSystem(inputStream);
            workbook = new HSSFWorkbook(fs);
        } catch (IOException e) {
            throw new MigrationException(file.getAbsolutePath(), e);
        }
    }

    public DbmtJEP compileWhere(String whereCondition) throws MigrationException {
        if (whereCondition != null) {
            where = new DbmtJEP(whereCondition, step);
            try {
                where.parseExpression(columnMapping);
            } catch (ParseException e) {
                throw new MigrationException(e);
            }
            where.setRow(record);
        } else {
            where = null;
        }
        return where;
    }

    public boolean next() throws MigrationException {
        if (stepWithoutWorkbook) {
            if (!readEmptyFile) {
                readEmptyFile = true;
                return true;
            } else {
                return false;
            }
        }
        if (!continueReading) {
            return false;
        }
        boolean hasValue = false;
        for (int i = 0; i < fromAreas.size(); i++) {
            XLSMigrationArea area = fromAreas.get(i);
            int r = area.getRow();
            short c = area.getColumn();
            HSSFRow row = sheets[i].getRow(r);
            HSSFCell cell = (row != null) ? row.getCell(c) : null;
            if (cell != null) {
                switch(cell.getCellType()) {
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        record[i] = cell.getNumericCellValue();
                        hasValue = true;
                        break;
                    case HSSFCell.CELL_TYPE_STRING:
                        record[i] = cell.getStringCellValue();
                        if (trim && record[i] instanceof String) {
                            record[i] = ((String) record[i]).trim();
                        }
                        hasValue = true;
                        break;
                    case HSSFCell.CELL_TYPE_FORMULA:
                        double val = cell.getNumericCellValue();
                        if (val == Double.NaN) {
                            record[i] = cell.getStringCellValue();
                        } else {
                            record[i] = val;
                        }
                        hasValue = true;
                        break;
                    case HSSFCell.CELL_TYPE_BOOLEAN:
                        record[i] = cell.getBooleanCellValue();
                        hasValue = true;
                        break;
                    case HSSFCell.CELL_TYPE_ERROR:
                    case HSSFCell.CELL_TYPE_BLANK:
                        record[i] = null;
                        break;
                    default:
                        throw new MigrationException("Undefined cell type");
                }
            } else {
                record[i] = null;
            }
            boolean nextExist = area.next();
            if (limitedArea && !nextExist) {
                continueReading = false;
            }
        }
        return limitedArea ? true : hasValue;
    }

    public Comparable getColumnObject(Field field) throws MigrationException {
        try {
            int column = field.getColumnIndex();
            return record[column];
        } catch (IndexOutOfBoundsException e) {
            throw new MigrationException(e.getMessage());
        }
    }

    public void close() throws MigrationException {
        if (!globalWorkbook) {
            workbook = null;
        }
        record = null;
    }

    public void shutdown() throws MigrationException {
        sheets = null;
        workbook = null;
    }
}
