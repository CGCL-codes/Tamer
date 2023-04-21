package com.ek.mitapp.excel.sheet;

import java.util.*;
import jxl.Cell;
import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.Sheet;
import jxl.format.*;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.*;
import org.apache.log4j.Logger;
import com.ek.mitapp.*;
import com.ek.mitapp.data.IntersectionRegistry;
import com.ek.mitapp.data.MitigationProject;
import com.ek.mitapp.excel.ExcelCell;
import com.ek.mitapp.excel.ExcelUtils;

/**
 * TODO: Class description.
 * <br>
 * Id: $Id: $
 *
 * @author dirwin
 */
public class MitigationListSheet implements IExcelSheet {

    /**
     * Define the root logger.
     */
    private static Logger logger = Logger.getLogger(MitigationListSheet.class.getName());

    /**
     * Sheet name.
     */
    private static final String SHEET_NAME = "3. Enter Mitigation List";

    /**
     * Define cell offsets.
     */
    public static enum Offsets {

        /**
         * ID to intersection cell offset
         */
        ID_TO_INTER(1, 0), /**
         * Intersection to ID cell offset
         */
        INTER_TO_ID(-1, 0), /**
         * ID to previous ID cell offset
         */
        ID_TO_PREV_ID(0, -2), /**
         * ID to increase storage cell offset
         */
        ID_TO_INC(2, 0), /**
         * ID to other mitigation type cell offset
         */
        ID_TO_OTHER(3, 0), /**
         * ID to mitigation description cell offset
         */
        ID_TO_DESC(4, 0), /**
         * ID to mitigation approach cell offset
         */
        ID_TO_APPR(5, 0), /**
         * ID to citizen request cell offset.
         */
        ID_TO_CITREQ(6, 0);

        /**
         * Column and row offsets.
         */
        private int columnOffset, rowOffset;

        /**
         * Default constructor.
         * 
         * @param columnOffset
         * @param rowOffset
         */
        private Offsets(int columnOffset, int rowOffset) {
            this.columnOffset = columnOffset;
            this.rowOffset = rowOffset;
        }

        /**
         * Get the column offset.
         * 
         * @return The column offset.
         */
        public int getColumnOffset() {
            return columnOffset;
        }

        /**
         * Get the row offset.
         * 
         * @return The row offset.
         */
        public int getRowOffset() {
            return rowOffset;
        }
    }

    /**
     * Offset between rows.
     */
    private static final int ROW_OFFSET = 2;

    /**
     * Define the starting ID cell location.
     * <br>
     * The ExcelCell starts at location (0, 0) which corresponds to Excel cell (A, 1). For example, 
     * defining an ExcelCell(0, 6) corresponds to Excel cell (A,7). 
     */
    private static final ExcelCell STARTING_ID_CELL = new ExcelCell(2, 8);

    /**
     * Define the starting intersection cell location.
     * <br>
     * The ExcelCell starts at location (0, 0) which corresponds to Excel cell (A, 1). For example, 
     * defining an ExcelCell(0, 6) corresponds to Excel cell (A,7). 
     */
    private static final ExcelCell STARTING_INTER_CELL = new ExcelCell(3, 8);

    /**
     * Default constructor.
     */
    public MitigationListSheet() {
    }

    /**
     * @see com.ek.mitapp.excel.sheet.IExcelSheet#getSheetName()
     */
    public String getSheetName() {
        return SHEET_NAME;
    }

    /** 
     * @see com.ek.mitapp.excel.sheet.IExcelSheet#getRowOffset()
     */
    public Integer getRowOffset() {
        return ROW_OFFSET;
    }

    /**
     * Write the mitigation list information to file.
     * 
     * @param mitigationProject
     * @param intersectionRegistry
     * @param writableSheet
     */
    public final void writeMitigationProject(final MitigationProject mitigationProject, final IntersectionRegistry intersectionRegistry, final WritableSheet writableSheet) {
        if (mitigationProject == null) throw new IllegalArgumentException("Mitigation project cannot be null");
        if (intersectionRegistry == null) throw new IllegalArgumentException("Intersection registry cannot be null");
        if (writableSheet == null) throw new IllegalArgumentException("Sheet cannot be null");
        logger.debug("Writing mitigation project to file");
        String searchingForInterName = mitigationProject.getIntersectionName();
        ProjectId projectId = retrieveCurrentProjectId(searchingForInterName, intersectionRegistry, STARTING_ID_CELL, writableSheet);
        if (projectId == null) {
            for (String key : intersectionRegistry.getKeyset()) {
                if (intersectionRegistry.get(key).getIntersectionName().equals(searchingForInterName)) {
                    projectId = new ProjectId(key + ".0");
                    break;
                }
            }
        }
        projectId.incrementMinor();
        write(mitigationProject, writableSheet, projectId);
    }

    /**
     * Retrieve the current project Id given the intersection name or description.
     *
     * @param searchInterName
     * @param intersectionRegistry
     * @param startingIdCell
     * @param sheet
     * @return The project Id
     */
    private ProjectId retrieveCurrentProjectId(final String searchInterName, IntersectionRegistry intersectionRegistry, final ExcelCell startingIdCell, WritableSheet sheet) {
        ExcelCell currentIdExcelCell = startingIdCell;
        Cell lastMatchingCell = null;
        String interName = null;
        Cell currentIdCell = sheet.getCell(currentIdExcelCell.getColumn(), currentIdExcelCell.getRow());
        CellType currentIdCellType = currentIdCell.getType();
        String currentIdContents = currentIdCell.getContents().trim();
        while (currentIdCellType != CellType.EMPTY) {
            logger.debug("Current Id cell (" + CellReferenceHelper.getCellReference(currentIdCell.getColumn(), currentIdCell.getRow()) + ") not empty: " + currentIdContents);
            interName = intersectionRegistry.get(ProjectId.getMajorValue(currentIdContents)).getIntersectionName();
            if (interName.equals(searchInterName)) lastMatchingCell = currentIdCell;
            currentIdExcelCell = currentIdExcelCell.getRowShiftedCell(ROW_OFFSET);
            currentIdCell = sheet.getCell(currentIdExcelCell.getColumn(), currentIdExcelCell.getRow());
            currentIdCellType = currentIdCell.getType();
            currentIdContents = currentIdCell.getContents().trim();
        }
        if (lastMatchingCell == null) {
            logger.debug("No match found.");
            return null;
        }
        logger.debug("Match found!");
        return new ProjectId(lastMatchingCell.getContents());
    }

    /**
     * Actually write the mitigation project to file.
     *
     * @param mitigationProject
     * @param writableSheet
     * @param projectId
     */
    private void write(final MitigationProject mitigationProject, WritableSheet writableSheet, ProjectId projectId) {
        logger.debug("Writing mitigation project: " + mitigationProject.getIntersectionName() + ", " + projectId.getId());
        ExcelCell idExcelCell = ExcelUtils.findNextEmptyRow(STARTING_ID_CELL, writableSheet, ROW_OFFSET);
        ExcelCell descExcelCell = idExcelCell.getOffsetCell(Offsets.ID_TO_DESC.getColumnOffset(), Offsets.ID_TO_DESC.getRowOffset());
        ExcelCell mitTypeExcelCell = null;
        if (mitigationProject.getMitigationMeasure().isIncreaseStorageType()) mitTypeExcelCell = idExcelCell.getOffsetCell(Offsets.ID_TO_INC.getColumnOffset(), Offsets.ID_TO_INC.getRowOffset()); else mitTypeExcelCell = idExcelCell.getOffsetCell(Offsets.ID_TO_OTHER.getColumnOffset(), Offsets.ID_TO_OTHER.getRowOffset());
        ExcelCell apprExcelCell = idExcelCell.getOffsetCell(Offsets.ID_TO_APPR.getColumnOffset(), Offsets.ID_TO_APPR.getRowOffset());
        ExcelCell citReqExcelCell = idExcelCell.getOffsetCell(Offsets.ID_TO_CITREQ.getColumnOffset(), Offsets.ID_TO_CITREQ.getRowOffset());
        WritableCell idCell = writableSheet.getWritableCell(idExcelCell.getColumn(), idExcelCell.getRow());
        WritableCell mitTypeCell = writableSheet.getWritableCell(mitTypeExcelCell.getColumn(), mitTypeExcelCell.getRow());
        WritableCell descCell = writableSheet.getWritableCell(descExcelCell.getColumn(), descExcelCell.getRow());
        WritableCell apprCell = writableSheet.getWritableCell(apprExcelCell.getColumn(), apprExcelCell.getRow());
        WritableCell citReqCell = writableSheet.getWritableCell(citReqExcelCell.getColumn(), citReqExcelCell.getRow());
        try {
            WritableCellFormat wcf = new WritableCellFormat();
            wcf.setBorder(Border.ALL, BorderLineStyle.MEDIUM);
            wcf.setAlignment(Alignment.CENTRE);
            wcf.setWrap(true);
            wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
            wcf.setLocked(false);
            Label idLabel = new Label(idCell.getColumn(), idCell.getRow(), projectId.getId(), wcf);
            ExcelUtils.writeCell(writableSheet, idLabel, wcf);
            Label mitTypeLabel = new Label(mitTypeCell.getColumn(), mitTypeCell.getRow(), "X", wcf);
            ExcelUtils.writeCell(writableSheet, mitTypeLabel, wcf);
            Label descLabel = new Label(descCell.getColumn(), descCell.getRow(), mitigationProject.getMitigationMeasure().toString(), wcf);
            ExcelUtils.writeCell(writableSheet, descLabel, wcf);
            Label apprLabel = new Label(apprCell.getColumn(), apprCell.getRow(), mitigationProject.getMitigationApproach().toString(), wcf);
            ExcelUtils.writeCell(writableSheet, apprLabel, wcf);
            if (mitigationProject.isCitizenRequest()) {
                Label citReqLabel = new Label(citReqCell.getColumn(), citReqCell.getRow(), "X", wcf);
                ExcelUtils.writeCell(writableSheet, citReqLabel, wcf);
            }
        } catch (RowsExceededException ree) {
            logger.error("Rows exceeded exception: " + ree.getMessage());
        } catch (WriteException we) {
            logger.error("Write exception: " + we.getMessage());
        }
    }

    /**
     * 
     * @param startingIdCell
     * @param sheet
     * @return
     */
    private final ProjectId findHighestProjectId(final ExcelCell startingIdCell, Sheet sheet) {
        logger.debug("Searching for hightest existing project Id (starting at " + CellReferenceHelper.getCellReference(startingIdCell.getColumn(), startingIdCell.getRow()) + ")");
        ExcelCell currentExcelIdCell = startingIdCell;
        ProjectId highestProjectId = null;
        Cell currentIdCell = sheet.getCell(currentExcelIdCell.getColumn(), currentExcelIdCell.getRow());
        String currentIdContents = currentIdCell.getContents().trim();
        while (currentIdCell.getType() != CellType.EMPTY) {
            if (highestProjectId == null) {
                highestProjectId = new ProjectId(currentIdCell.getContents());
            } else {
                if (!highestProjectId.isGreaterThan(currentIdCell.getContents())) highestProjectId = new ProjectId(currentIdCell.getContents());
            }
            currentExcelIdCell = currentExcelIdCell.getRowShiftedCell(ROW_OFFSET);
            currentIdCell = sheet.getCell(currentExcelIdCell.getColumn(), currentExcelIdCell.getRow());
            currentIdContents = currentIdCell.getContents().trim();
        }
        if (highestProjectId == null) logger.debug("No project Id found!"); else logger.debug("Highest project Id found: " + highestProjectId.getId());
        return highestProjectId;
    }
}

/**
 * Class used to keep track of the project Id value.
 */
final class ProjectId {

    private int major = -1;

    private int minor = -1;

    /**
     * Default constructor.
     * 
     * @param fullString
     */
    ProjectId(String fullString) {
        StringTokenizer st = new StringTokenizer(fullString, ".");
        major = Integer.parseInt(st.nextToken());
        minor = Integer.parseInt(st.nextToken());
    }

    final String getId() {
        return major + "." + minor;
    }

    final void incrementMinor() {
        minor++;
    }

    final void incrementMajor() {
        major++;
    }

    final void resetMinor() {
        minor = 0;
    }

    final void incrementMajorResetMinor() {
        incrementMajor();
        resetMinor();
    }

    final boolean isGreaterThan(String id) {
        StringTokenizer st = new StringTokenizer(id, ".");
        int major = Integer.parseInt(st.nextToken());
        int minor = Integer.parseInt(st.nextToken());
        if (this.major > major) return true; else return false;
    }

    public static final String getMajorValue(String fullId) {
        StringTokenizer st = new StringTokenizer(fullId, ".");
        return st.nextToken();
    }
}
