package org.dataminx.dts.ws.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dataminx.dts.common.jms.JobQueueSender;
import org.dataminx.dts.common.model.JobStatus;
import org.dataminx.dts.common.util.SchemaUtils;
import org.dataminx.dts.common.ws.InvalidJobDefinitionException;
import org.dataminx.dts.common.ws.JobStatusUpdateException;
import org.dataminx.dts.common.ws.NonExistentJobException;
import org.dataminx.dts.ws.model.Job;
import org.dataminx.dts.ws.repo.JobDao;
import org.dataminx.schemas.dts.x2009.x07.messages.CancelJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.GetJobDetailsRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.GetJobStatusRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.ResumeJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.SubmitJobRequestDocument;
import org.dataminx.schemas.dts.x2009.x07.messages.SuspendJobRequestDocument;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.oxm.xmlbeans.XmlBeansMarshaller;
import org.springframework.util.Assert;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;
import org.springframework.xml.transform.StringResult;

/**
 * The Data Transfer Service Implementation. This class interacts with the DTS
 * Job repository and hands over the submitted jobs to the DTS Messaging System
 * which then forwards them to the DTS Worker Nodes.
 *
 * @author Gerson Galang
 */
public class DataTransferServiceImpl implements DataTransferService, InitializingBean {

    /** The logger. */
    private static final Log LOGGER = LogFactory.getLog(DataTransferServiceImpl.class);

    /**
     * the constant variable to be used in finding the value of the common name.
     */
    private static final String CN_EQUALS = "CN=";

    /** The submit job message sender. */
    private JobQueueSender mJobSubmitMessageSender;

    private JobQueueSender mJobControlMessageSender;

    /** The job repository for this DTS implementation. */
    private JobDao mJobRepository;

    /** The xmlbeans marshaller. */
    private XmlBeansMarshaller mMarshaller;

    private String mWebserviceIDMessageHeaderName;

    private String mWebserviceID;

    /**
     * {@inheritDoc}
     */
    public String submitJob(final SubmitJobRequestDocument submitJobRequest) {
        String subjectName = "NEW_USER";
        final TransportContext txContext = TransportContextHolder.getTransportContext();
        final HttpServletConnection connection = (HttpServletConnection) txContext.getConnection();
        final HttpServletRequest request = connection.getHttpServletRequest();
        final Subject subject = (Subject) request.getSession().getAttribute("subject");
        if (subject != null) {
            final String distinguishedName = subject.getPrincipals().toArray()[0].toString();
            subjectName = distinguishedName.substring(distinguishedName.indexOf(CN_EQUALS) + CN_EQUALS.length());
        }
        LOGGER.debug("DataTransferServiceImpl submitJob()");
        final Job newJob = new Job();
        final String newJobResourceKey = UUID.randomUUID().toString();
        newJob.setName(newJobResourceKey);
        newJob.setResourceKey(newJobResourceKey);
        newJob.setStatus(JobStatus.CREATED);
        newJob.setSubjectName(subjectName);
        newJob.setCreationTime(new Date());
        try {
            newJob.setExecutionHost(InetAddress.getLocalHost().getCanonicalHostName());
        } catch (final UnknownHostException ex) {
            LOGGER.error(ex.getMessage());
        }
        mJobRepository.saveOrUpdate(newJob);
        final Result result = new StringResult();
        try {
            mMarshaller.marshal(submitJobRequest, result);
        } catch (final IOException e) {
            throw new InvalidJobDefinitionException(e.fillInStackTrace());
        }
        final String auditableRequest = SchemaUtils.getAuditableString(submitJobRequest);
        LOGGER.debug(auditableRequest);
        final Map<String, Object> jmsParameterMap = new HashMap<String, Object>();
        jmsParameterMap.put("department", "'chem'");
        jmsParameterMap.put(mWebserviceIDMessageHeaderName, mWebserviceID);
        String deliveryMode = "NON_PERSISTENT";
        if (deliveryMode.equals("PERSISTENT")) {
            mJobSubmitMessageSender.doSend(newJobResourceKey, jmsParameterMap, result.toString(), false);
        } else {
            mJobSubmitMessageSender.doSend(newJobResourceKey, jmsParameterMap, result.toString(), true);
        }
        newJob.setStatus(JobStatus.SCHEDULED);
        newJob.setQueuedTime(new Date());
        mJobRepository.saveOrUpdate(newJob);
        return newJobResourceKey;
    }

    /**
     * {@inheritDoc}
     */
    public void cancelJob(final CancelJobRequestDocument cancelJobRequest) {
        final String jobResourceKey = cancelJobRequest.getCancelJobRequest().getJobResourceKey();
        LOGGER.debug("DataTransferServiceImpl cancelJob()");
        LOGGER.debug("Cancelling job: " + jobResourceKey);
        Job foundJob = null;
        foundJob = mJobRepository.findByResourceKey(jobResourceKey);
        if (foundJob == null) {
            throw new NonExistentJobException("Job doesn't exist.");
        }
        if (!(foundJob.getStatus().equals(JobStatus.CREATED) || foundJob.getStatus().equals(JobStatus.SCHEDULED) || foundJob.getStatus().equals(JobStatus.SUSPENDED) || foundJob.getStatus().equals(JobStatus.TRANSFERRING))) {
            throw new JobStatusUpdateException("Job " + jobResourceKey + " cannot be cancelled as it has already completed, failed, or cancelled.");
        }
        foundJob.setStatus(JobStatus.DONE);
        mJobRepository.saveOrUpdate(foundJob);
        final Result result = new StringResult();
        try {
            mMarshaller.marshal(cancelJobRequest, result);
        } catch (final IOException e) {
            throw new InvalidJobDefinitionException(e.fillInStackTrace());
        }
        mJobControlMessageSender.doSend(jobResourceKey, result.toString());
    }

    /**
     * {@inheritDoc}
     */
    public void suspendJob(final SuspendJobRequestDocument suspendJobRequest) {
        final String jobResourceKey = suspendJobRequest.getSuspendJobRequest().getJobResourceKey();
        LOGGER.debug("DataTransferServiceImpl suspendJob()");
        LOGGER.debug("Suspending job: " + jobResourceKey);
        Job foundJob = null;
        foundJob = mJobRepository.findByResourceKey(jobResourceKey);
        if (foundJob == null) {
            throw new NonExistentJobException("Job doesn't exist.");
        }
        if (foundJob.getStatus().equals(JobStatus.SUSPENDED)) {
            throw new JobStatusUpdateException("Job " + jobResourceKey + " cannot be suspended as it is already in the suspended state.");
        } else if (!(foundJob.getStatus().equals(JobStatus.CREATED) || foundJob.getStatus().equals(JobStatus.SCHEDULED) || foundJob.getStatus().equals(JobStatus.TRANSFERRING))) {
            throw new JobStatusUpdateException("Job " + jobResourceKey + " cannot be suspended as it is has already completed, failed, or cancelled.");
        }
        foundJob.setStatus(JobStatus.SUSPENDED);
        mJobRepository.saveOrUpdate(foundJob);
        final Result result = new StringResult();
        try {
            mMarshaller.marshal(suspendJobRequest, result);
        } catch (final IOException e) {
            throw new InvalidJobDefinitionException(e.fillInStackTrace());
        }
        mJobControlMessageSender.doSend(jobResourceKey, result.toString());
    }

    /**
     * {@inheritDoc}
     */
    public void resumeJob(final ResumeJobRequestDocument resumeJobRequest) {
        final String jobResourceKey = resumeJobRequest.getResumeJobRequest().getJobResourceKey();
        LOGGER.debug("DataTransferServiceImpl resumeJob()");
        LOGGER.debug("Resuming job " + jobResourceKey);
        Job foundJob = null;
        foundJob = mJobRepository.findByResourceKey(jobResourceKey);
        if (foundJob == null) {
            throw new NonExistentJobException("Job doesn't exist.");
        }
        if (!foundJob.getStatus().equals(JobStatus.SUSPENDED)) {
            throw new JobStatusUpdateException("Job " + jobResourceKey + " cannot be resumed as it has not been suspended.");
        }
        foundJob.setStatus(JobStatus.TRANSFERRING);
        mJobRepository.saveOrUpdate(foundJob);
        final Result result = new StringResult();
        try {
            mMarshaller.marshal(resumeJobRequest, result);
        } catch (final IOException e) {
            throw new InvalidJobDefinitionException(e.fillInStackTrace());
        }
        mJobControlMessageSender.doSend(jobResourceKey, result.toString());
    }

    /**
     * {@inheritDoc}
     */
    public String getJobStatus(final GetJobStatusRequestDocument getJobStatusRequest) {
        final String jobResourceKey = getJobStatusRequest.getGetJobStatusRequest().getJobResourceKey();
        LOGGER.debug("DataTransferServiceImpl getJobStatus()");
        LOGGER.debug("Getting job status of job " + jobResourceKey);
        Job foundJob = null;
        foundJob = mJobRepository.findByResourceKey(jobResourceKey);
        if (foundJob == null) {
            throw new NonExistentJobException("Job doesn't exist.");
        }
        return foundJob.getStatus().getStringValue();
    }

    /**
     * {@inheritDoc}
     */
    public Job getJobDetails(final GetJobDetailsRequestDocument getJobDetailsRequest) {
        final String jobResourceKey = getJobDetailsRequest.getGetJobDetailsRequest().getJobResourceKey();
        LOGGER.debug("DataTransferServiceImpl getJobDetails()");
        LOGGER.debug("Getting job details of job " + jobResourceKey);
        Job foundJob = null;
        foundJob = mJobRepository.findByResourceKey(jobResourceKey);
        if (foundJob == null) {
            throw new NonExistentJobException("Job doesn't exist.");
        }
        return foundJob;
    }

    public void setJobSubmitQueueSender(final JobQueueSender jobSubmitQueueSender) {
        mJobSubmitMessageSender = jobSubmitQueueSender;
    }

    public void setJobControlQueueSender(final JobQueueSender jobControlQueueSender) {
        mJobControlMessageSender = jobControlQueueSender;
    }

    public void setJobRepository(final JobDao jobRepository) {
        mJobRepository = jobRepository;
    }

    public void setMarshaller(final XmlBeansMarshaller marshaller) {
        mMarshaller = marshaller;
    }

    public void setWebserviceIDMessageHeaderName(final String webserviceIDMessageHeaderName) {
        mWebserviceIDMessageHeaderName = webserviceIDMessageHeaderName;
    }

    public void setWebserviceID(final String webserviceID) {
        mWebserviceID = webserviceID;
    }

    /**
     * {@inheritDoc}
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(mJobSubmitMessageSender, "A JobSubmitQueueSender needs to be configured for the DataTransferService.");
        Assert.notNull(mJobControlMessageSender, "A JobControlMessageSender needs to be configured for the DataTransferService.");
        Assert.notNull(mJobRepository, "A JobDao needs to be configured for the DataTransferService.");
        Assert.notNull(mMarshaller, "A JaxbMarshaller needs to be configured for the DataTransferService");
    }
}
