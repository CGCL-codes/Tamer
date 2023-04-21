package com.idna.batchid.service.reporting.impl;

import org.apache.log4j.Logger;
import com.idna.batchid.global.PackagingSettings;
import com.idna.batchid.model.BatchOutput;
import com.idna.batchid.model.BatchOutputWrapper;
import com.idna.batchid.model.database.CustomerProfile;
import com.idna.batchid.service.reporting.AbstractAccumulatingReportEmitter;
import com.idna.batchid.service.reporting.excel.ResultFileNamer;
import com.idna.batchid.service.reporting.excel.workbook.domain.BatchIdWorkbook;
import com.idna.batchid.service.reporting.excel.workbook.utils.BatchIdWorkbookBuilder;
import com.idna.batchid.util.io.DataSerializationManager;
import com.idna.batchid.util.log.BatchIdLoggerFactoryImpl;

public class FasterMatrixBasedReportEmitter extends AbstractAccumulatingReportEmitter {

    protected final Logger logger = Logger.getLogger(this.getClass().getName(), new BatchIdLoggerFactoryImpl());

    private BatchIdWorkbookBuilder builder;

    /**
	 * Serialises the BatchOutput object to a file for future use.  
	 * 
	 * @param profile
	 * @param originalFilename
	 * @param output
	 */
    private void saveParams(CustomerProfile profile, String originalFilename, BatchOutput output) {
        String boFile = DataSerializationManager.doSave(output);
        String profileFile = DataSerializationManager.doSave(profile);
        logger.debug("BatchOutput Saved: " + boFile);
        logger.debug("CustomerProfile Saved: " + profileFile);
    }

    public String outputResults(CustomerProfile profile, String originalFilename, BatchOutput output) throws Exception {
        logger.debug("***** FasterMatrixBasedReportEmitter Packaging Subsystem Starting...");
        if (PackagingSettings.isSerialize()) saveParams(profile, originalFilename, output);
        String resultsFilename = ResultFileNamer.buildResultsFilename(originalFilename);
        BatchIdWorkbook wb = buildWorkbook(profile, resultsFilename, output);
        wb.write();
        return resultsFilename;
    }

    private BatchIdWorkbook buildWorkbook(CustomerProfile profile, String filename, BatchOutput output) {
        BatchOutputWrapper boWrapper = new BatchOutputWrapper(output, profile);
        messageConverter.processBatchOutputMessages(boWrapper);
        BatchIdWorkbook wb = builder.buildWorkbook(boWrapper, filename);
        return wb;
    }

    public BatchIdWorkbookBuilder getWorkbookBuilder() {
        return builder;
    }

    public void setWorkbookBuilder(BatchIdWorkbookBuilder builder) {
        this.builder = builder;
    }
}
