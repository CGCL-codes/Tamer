package org.amlfilter.dao;

import java.util.List;
import org.amlfilter.model.ExcelFileProcessingStatus;
import org.springframework.dao.DataAccessException;

/**
 * The DAOExcelFileProcessingStatus interface
 *
 * @author  Harish Seshadri
 * @version $Id: DAOExcelFileProcessingStatusInterface.java,v 1.2 2007/06/25 03:04:42 hseshadr Exp $
 */
public interface DAOExcelFileProcessingStatusInterface {

    public ExcelFileProcessingStatus getExcelFileProcessingStatus(String pProcessingFileName) throws DataAccessException;

    public ExcelFileProcessingStatus loadExcelFileProcessingStatus(Long pExcelFileProcessingStatusId) throws DataAccessException;

    public void storeExcelFileProcessingStatus(ExcelFileProcessingStatus pExcelFileProcessingStatus) throws DataAccessException;

    /**
     * Get all the excel file processing status items
     * @return A collection of all the excel file processing status items
     */
    public List getAllExcelFileProcessingStatusItems();

    /**
     * Get all the excel file processing status items
     * @param pMaxResults The max results
     * @return A collection of all the excel file processing status items
     */
    public List getAllExcelFileProcessingStatusItems(int pMaxResults);

    /**
     * Get the incoming excel file processing status items
     * @return The incoming excel file processing status items
     */
    public List getIncomingExcelFileProcessingStatusItems();
}
