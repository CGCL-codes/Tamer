package com.idna.batchid.processor.batchjob;

import java.util.UUID;
import org.apache.log4j.Logger;
import com.idna.batchid.model.BatchOutput;
import com.idna.batchid.model.BatchProfile;
import com.idna.batchid.model.ProductResponse;
import com.idna.batchid.model.RecordOutput;
import com.idna.batchid.util.log.BatchIdLoggerFactoryImpl;
import com.idna.dm.domain.input.ExecutionRequestData;
import com.idna.dm.service.DecisionMatrixExecutionEntryPoint;

public class UnpluggableBatchFilesPostProcessorImpl implements UnpluggableBatchFilesPostProcessor {

    private DecisionMatrixExecutionEntryPoint decisionMatrixExecutionEntryPoint;

    private ProductResponseValidator productResponseValidator;

    protected final Logger logger = Logger.getLogger(this.getClass().getName(), new BatchIdLoggerFactoryImpl());

    @Override
    public void postProcessBatch(BatchProfile batchProfile, BatchOutput batchOutput) {
        if (batchOutput != null) {
            UUID loginId = UUID.fromString(batchProfile.getLoginId());
            logger.info(String.format("Found $d record outputs", batchOutput.getRecordOutputs().size()));
            for (RecordOutput recordOutput : batchOutput.getRecordOutputs()) {
                ExecutionRequestData executionRequestData = new ExecutionRequestData(loginId);
                executionRequestData.setDecisionCode(recordOutput.getRecordRequest().getDecisionCode());
                logger.info(String.format("Processing Record %s with DM", recordOutput.getRecordRequest().getId()));
                for (ProductResponse productResponse : recordOutput.getResponses()) {
                    if (productResponseValidator.isErrorResponse(productResponse)) continue;
                    String xmlResponse = productResponse.getXmlResponse();
                    logger.info(String.format("Pre-DM%n%s", xmlResponse));
                    String decisionMatrixXmlResponse = this.decisionMatrixExecutionEntryPoint.runDecisionMatrix(executionRequestData, xmlResponse);
                    logger.info(String.format("Decision Made For %s, %s: Overwriting XML Response", executionRequestData.getDecisionCode(), recordOutput.getRecordRequest().getId()));
                    logger.info(String.format("Post-DM%n%s", decisionMatrixXmlResponse));
                    productResponse.setXmlResponse(decisionMatrixXmlResponse);
                }
            }
        }
    }

    public void setDecisionMatrixExecutionEntryPoint(DecisionMatrixExecutionEntryPoint decisionMatrixExecutionEntryPoint) {
        this.decisionMatrixExecutionEntryPoint = decisionMatrixExecutionEntryPoint;
    }

    public void setProductResponseValidator(ProductResponseValidator productResponseValidator) {
        this.productResponseValidator = productResponseValidator;
    }
}
