package com.idna.batchid.service.reporting.excel.workbook.utils;

import com.idna.batchid.model.BatchOutputWrapper;
import com.idna.batchid.model.database.Service;
import com.idna.batchid.service.reporting.excel.workbook.domain.BatchIdWorkbook;
import com.idna.batchid.service.reporting.matrix.ProcessedBatchOutputData;
import com.idna.batchid.service.reporting.matrix.ReportMatrix;

/**
 * 
 * Factory class for creating {@link BatchIdWorkbook}
 * 
 * @author gawain.hammond
 * 
 */
public class BatchIdWorkbookFactory {

    public static BatchIdWorkbook createBatchIdWorkBook(String filename, BatchOutputWrapper boWrapper, ProcessedBatchOutputData processedBatchOutput) {
        ReportMatrix customerRequestMasterMatrix = processedBatchOutput.getMasterMatrix();
        BatchIdWorkbook workbook = new BatchIdWorkbook(filename);
        for (Service service : boWrapper.getCustomerRequestedServices()) {
            workbook.addServiceSheet(service.getTaskName());
        }
        workbook.addAllRequestRecordsToMasterSheet(customerRequestMasterMatrix);
        return workbook;
    }
}
