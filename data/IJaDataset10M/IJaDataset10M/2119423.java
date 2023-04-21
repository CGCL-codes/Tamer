package org.dataminx.dts.wn;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.dataminx.dts.batch.DtsJobLauncher;
import org.dataminx.schemas.dts.x2009.x07.messages.CustomFaultDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.CustomFaultType;
import org.dataminx.schemas.dts.x2009.x07.messages.InvalidJobDefinitionFaultDocument;
import org.proposal.dmi.schemas.dts.x2010.dmiCommon.DataCopyActivityDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.Message;
import org.springframework.integration.core.MessageHeaders;
import org.springframework.integration.message.MessageBuilder;

/**
 * A {@link ServiceActivator} class that launches jobs.
 *
 * @author David Meredith
 */
public class SubmitRequestHandler {

    /** Internal application logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SubmitRequestHandler.class);

    /** A reference to the DTS specific job launcher */
    private DtsJobLauncher mDtsJobLauncher;

    /** A reference to the DTS Worker Node information service. */
    private DtsWorkerNodeInformationService mDtsWorkerNodeInformationService;

    /**
     * Handles Spring Integration messages that either wrap a valid {@link JobDefinitionDocument}
     * or a {@link InvalidJobDefinitionFaultDocument}. If a valid job is
     * recieved, the job is launched. If an invalid fault doc is recieved, then
     * the {@link InvalidJobDefinitionFaultDocument} is returned. If any problem
     * occurs when submitting the job, then a {@link CustomFaultDocument} is
     * returned.
     *
     * @param message
     * @return a Spring Integration Message used to wrap the returned document type. 
     */
    @ServiceActivator
    public Message<?> handleJobSubmitRequest(Message<?> message) {
        final Object submitRequest = message.getPayload();
        final MessageHeaders headers = message.getHeaders();
        String jobID;
        if (headers.getCorrelationId() == null) {
            jobID = UUID.randomUUID().toString();
        } else {
            jobID = (String) headers.getCorrelationId();
        }
        if (submitRequest instanceof DataCopyActivityDocument) {
            LOG.debug("Submit job for: " + jobID);
            try {
                final Map<String, Object> headersAsBatchJobParameters = new HashMap<String, Object>();
                Iterator<String> it = headers.keySet().iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    Object h = headers.get(key);
                    if (h instanceof String || h instanceof Date || h instanceof Double || h instanceof Long) {
                        headersAsBatchJobParameters.put(key, h);
                    } else {
                        if (h != null) {
                            try {
                                headersAsBatchJobParameters.put(key, h.toString());
                            } catch (Exception ex) {
                                LOG.warn("Unable to add header key [" + key + "] to JobParameters map");
                            }
                        }
                    }
                }
                mDtsJobLauncher.run(jobID, (DataCopyActivityDocument) submitRequest, headersAsBatchJobParameters);
                return null;
            } catch (Exception ex) {
                return this.buildAnErrorMessage(headers, ex.getMessage());
            }
        } else if (submitRequest instanceof InvalidJobDefinitionFaultDocument) {
            LOG.debug("Invalid job request for " + jobID);
            return message;
        }
        return this.buildAnErrorMessage(headers, "Unsupported Message Type");
    }

    /**
     * Create a new Spring Integration Message that wraps a {@link CustomFaultDocument}.
     *
     * @param messageHeaders
     * @param error
     * @return
     */
    private Message buildAnErrorMessage(MessageHeaders messageHeaders, String error) {
        final CustomFaultDocument document = CustomFaultDocument.Factory.newInstance();
        final CustomFaultType GetJobStatusRequestReply = document.addNewCustomFault();
        Map<String, Object> SIMsgHeaders = new LinkedHashMap<String, Object>();
        Iterator<String> iterator = messageHeaders.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            SIMsgHeaders.put(key, messageHeaders.get(key).toString());
        }
        SIMsgHeaders.put(mDtsWorkerNodeInformationService.getWorkerNodeIDMessageHeaderName(), mDtsWorkerNodeInformationService.getInstanceId());
        GetJobStatusRequestReply.setMessage(error);
        MessageBuilder<CustomFaultDocument> msgbuilder = MessageBuilder.withPayload(document).copyHeaders(SIMsgHeaders);
        Message<CustomFaultDocument> msg = msgbuilder.build();
        return msg;
    }

    /**
     * Inject the dts job launcher in order to submit jobs.
     *
     * @param jobLauncher
     */
    public void setDtsJobLauncher(final DtsJobLauncher jobLauncher) {
        this.mDtsJobLauncher = jobLauncher;
    }

    /**
     * Set the DtsWorkerNodeInformationService.
     *
     * @param dtsWorkerNodeInformationService
     */
    public void setDtsWorkerNodeInformationService(final DtsWorkerNodeInformationService dtsWorkerNodeInformationService) {
        mDtsWorkerNodeInformationService = dtsWorkerNodeInformationService;
    }
}
