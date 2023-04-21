package com.idna.batchid.service.batch.batchprocessing.recordservices;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import org.apache.log4j.Logger;
import com.idna.batchid.global.PackagingSettings;
import com.idna.batchid.model.BatchProfile;
import com.idna.batchid.model.ProductResponse;
import com.idna.batchid.model.RecordOutput;
import com.idna.batchid.model.RecordRequest;
import com.idna.batchid.service.record.RecordProcessingManager;
import com.idna.batchid.util.io.DataSerializationManager;
import com.idna.batchid.util.log.BatchIdLoggerFactoryImpl;

/**
 * 
 * @author Matthew Cosgrove
 */
public class RecordProcessingTask implements Callable<RecordOutput> {

    private final RecordRequest record;

    private final BatchProfile configuration;

    private final RecordProcessingManager recordManager;

    protected final Logger logger = Logger.getLogger(this.getClass().getName(), new BatchIdLoggerFactoryImpl());

    public RecordProcessingTask(final RecordRequest record, final BatchProfile configuration, RecordProcessingManager recordManager) {
        if (logger.isDebugEnabled()) {
            logger.debug("POPULATING TASK WITH RECORD REQUEST: " + record.toString());
        }
        this.record = record;
        this.configuration = configuration;
        this.recordManager = recordManager;
    }

    public RecordOutput call() {
        RecordOutput recordOutput = null;
        String threadName = Thread.currentThread().getName();
        Thread.currentThread().setName(threadName + record.getId());
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("\nTHREAD WILL PROCESS: %s \n%s", record.getId(), configuration.toString()));
        }
        try {
            recordOutput = recordManager.processRecord(record, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (PackagingSettings.isSerialize()) {
            if (recordOutput != null) {
            }
        }
        return recordOutput;
    }

    private synchronized void saveParams(RecordOutput output, RecordRequest input) {
        RecordRequest outputRequest = output.getRecordRequest();
        String id = null;
        String inputId = null;
        if (outputRequest != null) {
            id = "Task" + outputRequest.getId();
            inputId = outputRequest.getId() + "Orig" + input.getId();
        } else {
            id = "TaskHasNullOutputRequest";
            inputId = "NullOutputRequestOrig" + input.getId();
        }
        DataSerializationManager dsm = new DataSerializationManager();
        String responseFile = dsm.doSave(output, id);
        logger.info("RecordOutput Saved: " + responseFile);
        String inputFile = dsm.doSave(input, inputId);
        logger.info("RecordRequest Input Saved: " + inputFile);
    }

    public RecordOutput getFailureResponse(Throwable t) {
        logger.error("getting failure response.");
        RecordOutput recordOutput = new RecordOutput();
        recordOutput.setRecordRequest(record);
        recordOutput.setResponses(new ArrayList<ProductResponse>(0));
        if (record != null) {
            logger.error("Returning an empty response list as a Failure response occured for the following record request " + record.toString());
        } else logger.error("Returning an empty response list as a Failure response occured for a null record request");
        logger.error(t.getCause(), t);
        return recordOutput;
    }

    public RecordOutput getTimeoutResponse(CancellationException e) {
        return null;
    }
}
