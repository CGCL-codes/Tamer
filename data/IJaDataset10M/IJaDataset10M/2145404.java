package net.sf.gateway.mef.batchjobs;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.Iterator;
import net.sf.gateway.mef.axis2.Axis2Configurator;
import net.sf.gateway.mef.axis2.ExceptionHandler;
import net.sf.gateway.mef.batches.Batch;
import net.sf.gateway.mef.configuration.ClientConfigurator;
import net.sf.gateway.mef.databases.tables.MefSubmission;
import net.sf.gateway.mef.types.MessageIdType;
import net.sf.gateway.mef.utilities.Axis2Utils;
import net.sf.gateway.mef.webservices.GetSubmissionStub;
import net.sf.gateway.mef.webservices.MeFFaultMessage;
import net.sf.gateway.mef.webservices.GetSubmissionStub.GetSubmissionRequestArgs;
import net.sf.gateway.mef.webservices.GetSubmissionStub.GetSubmissionRequestType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.GetSubmissionResponse;
import net.sf.gateway.mef.webservices.GetSubmissionStub.HeaderMessageIdType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.IRSDataForStateSubmissionType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.MeF;
import net.sf.gateway.mef.webservices.GetSubmissionStub.MeFHeaderType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.SessionIndicatorType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.SubmissionIdType;
import net.sf.gateway.mef.webservices.GetSubmissionStub.TestIndicatorType;
import net.sf.gateway.mef.workunits.WorkUnit;
import org.apache.axis2.AxisFault;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 * Get a single Submission.
 */
public class GetSubmission extends BatchJob<URL> implements WebServiceBatchJob {

    /**
     * Logging.
     */
    private static final Log LOG = LogFactory.getLog(GetSubmission.class);

    /**
     * Constructor. Calls the super constructor and sets the priority.
     */
    public GetSubmission() {
        super();
        this.setPriority(ClientConfigurator.getBatchJobs().getGetSubmission().getPriority());
    }

    /**
     * Checks the configuration to determine if the BatchJob should be invoked.
     * 
     * @return true if enabled, otherwise false.
     */
    public boolean isEnabled() {
        return ClientConfigurator.getBatchJobs().getGetSubmission().getEnabled();
    }

    /**
     * Get a Batch of WorkUnits to process. Our work units here are web service
     * URLs.
     * 
     * @return batch size or -1 if there was an error fetching the batch.
     */
    public long fetchBatch() {
        Batch<URL> batch = new Batch<URL>();
        WorkUnit<URL> wu = new WorkUnit<URL>();
        try {
            wu.setWork(new URL(ClientConfigurator.getA2aUrl() + this.getWebServicePath()));
        } catch (MalformedURLException e) {
            LOG.error("Bad logout service URL: " + ClientConfigurator.getA2aUrl() + this.getWebServicePath(), e);
            this.setBatch(null);
            return -1;
        }
        batch.add(wu);
        this.setBatch(batch);
        return this.getWorkUnitsFetched();
    }

    @Override
    public long processBatch() {
        Batch<URL> batch = this.getBatch();
        Iterator<WorkUnit<URL>> itr = batch.iterator();
        while (itr.hasNext()) {
            WorkUnit<URL> unit = itr.next();
            LOG.info("<" + this.getJobId() + "> Invoking web service: " + unit.getWork().toString());
            GetSubmissionRequestArgs request = null;
            GetSubmissionResponse response = null;
            GetSubmissionStub stub = null;
            try {
                HeaderMessageIdType msgId = new HeaderMessageIdType();
                msgId.setHeaderMessageIdType(MessageIdType.fromGenerator().toString());
                MeFHeaderType header = new MeFHeaderType();
                header.setMessageID(msgId);
                header.setAction(this.getWebServiceAction());
                header.setTimestamp(new GregorianCalendar());
                header.setETIN(ClientConfigurator.getCredentials().getETIN());
                header.setSessionIndicator(SessionIndicatorType.Y);
                header.setTestIndicator(TestIndicatorType.T);
                header.setAppSysID(ClientConfigurator.getCredentials().getUsername());
                MeF mef = new MeF();
                mef.setMeF(header);
                SubmissionIdType submissionId = new SubmissionIdType();
                submissionId.setSubmissionIdType(ClientConfigurator.getBatchJobs().getGetSubmission().getSubmissionId());
                GetSubmissionRequestType body = new GetSubmissionRequestType();
                body.setSubmissionId(submissionId);
                request = new GetSubmissionRequestArgs();
                request.setGetSubmissionRequestArgs(body);
                stub = new GetSubmissionStub(Axis2Configurator.getConfigurationContext(this), null);
                ServiceClient client = stub._getServiceClient();
                Axis2Configurator.configureOptions(client.getOptions(), unit);
                response = stub.getSubmission(request, mef);
                MessageContext responseMessageContext = client.getLastOperationContext().getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
                Axis2Utils.saveZipAttachments(responseMessageContext, ClientConfigurator.getBatchJobs().getGetSubmission().getAttachmentsDirectory(), this.getJobId(), "submissions");
                IRSDataForStateSubmissionType submission = response.getGetSubmissionResponse().getIRSData();
                if (submission != null) {
                    LOG.info("<" + this.getJobId() + "> Web service returned " + submission.getSubmissionId() + " Submissions");
                    SessionFactory sessionFactory = ClientConfigurator.getSessionFactory();
                    Session session = sessionFactory.getCurrentSession();
                    Transaction tx = null;
                    try {
                        tx = session.beginTransaction();
                        LOG.info("<" + this.getJobId() + "> Adding database record for SubmissionId " + submission.getSubmissionId().getSubmissionIdType());
                        MefSubmission mefSubmission = new MefSubmission();
                        mefSubmission.setSubmissionId(submission.getSubmissionId().getSubmissionIdType());
                        mefSubmission.setReceivedTimestamp(submission.getReceivedTimestamp().getTime());
                        mefSubmission.setEtin(submission.getETIN());
                        if (submission.getFederalEINStatus() != null) {
                            mefSubmission.setFederalEINStatus(submission.getFederalEINStatus().getValue());
                        }
                        if (submission.getElectronicPostmark() != null) {
                            mefSubmission.setElectronicPostmark(submission.getElectronicPostmark().getTime());
                        }
                        if (submission.getSourceIRSSubmissionId() != null) {
                            mefSubmission.setSourceIRSSubmissionId(submission.getSourceIRSSubmissionId().getSubmissionIdType());
                        }
                        if (submission.getSSNValidationStatus() != null) {
                            mefSubmission.setPriSsnValStat(submission.getSSNValidationStatus().getPrimarySSNValidationStatus().getValue());
                            if (submission.getSSNValidationStatus().getSecondarySSNValidationStatus() != null) {
                                mefSubmission.setSecSsnValStat(submission.getSSNValidationStatus().getSecondarySSNValidationStatus().getValue());
                            }
                        }
                        if (submission.getITINMismatchCd() != null) {
                            mefSubmission.setItinMismatchCd(submission.getITINMismatchCd());
                        }
                        if (submission.getImperfectReturnIndicator() != null) {
                            mefSubmission.setImperfectReturnIndicator(submission.getImperfectReturnIndicator());
                        }
                        mefSubmission.setClientProcessingState("RDY4ZIPVAL");
                        session.save(mefSubmission);
                        tx.commit();
                        LOG.info("<" + this.getJobId() + "> Transaction Committed");
                    } catch (HibernateException e) {
                        LOG.error("Transaction Failed: Hibernate could not update MefSubmission Records", e);
                        if (tx != null && tx.isActive()) {
                            tx.rollback();
                        }
                        unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                        continue;
                    }
                } else {
                    LOG.info("<" + this.getJobId() + "> Web service returned 0 Submissions");
                }
            } catch (AxisFault a) {
                ExceptionHandler.handleAxisFault(this, a);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            } catch (MeFFaultMessage m) {
                ExceptionHandler.handleMeFFaultMessage(this, m);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            } catch (Exception e) {
                ExceptionHandler.handleException(this, e);
                unit.setProcessingStatus(WorkUnit.ProcessingStatus.FAILURE);
                continue;
            }
            unit.setProcessingStatus(WorkUnit.ProcessingStatus.SUCCESS);
        }
        return this.getWorkUnitsProcessed();
    }

    public String getWebServiceAction() {
        return "GetSubmission";
    }

    public String getWebServicePath() {
        if ("http://localhost:8080/axis2/services/".equals(ClientConfigurator.getA2aUrl()) || "http://localhost:8888/axis2/services/".equals(ClientConfigurator.getA2aUrl())) {
            return "GetSubmission";
        } else {
            return "mime/GetSubmission";
        }
    }
}
