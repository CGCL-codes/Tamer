package uk.org.ogsadai.dqp.execute.partition;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import uk.org.ogsadai.client.toolkit.DataRequestExecutionResource;
import uk.org.ogsadai.client.toolkit.DataSinkResource;
import uk.org.ogsadai.client.toolkit.DataSourceResource;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.RequestExecutionType;
import uk.org.ogsadai.client.toolkit.RequestResource;
import uk.org.ogsadai.client.toolkit.Workflow;
import uk.org.ogsadai.client.toolkit.activities.generic.GenericActivity;
import uk.org.ogsadai.client.toolkit.exception.ClientException;
import uk.org.ogsadai.client.toolkit.exception.RequestCompletedWithErrorException;
import uk.org.ogsadai.client.toolkit.exception.RequestErrorException;
import uk.org.ogsadai.client.toolkit.exception.RequestException;
import uk.org.ogsadai.client.toolkit.exception.RequestExecutionException;
import uk.org.ogsadai.client.toolkit.exception.RequestTerminatedException;
import uk.org.ogsadai.client.toolkit.exception.ResourceUnknownException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.ExecutionException;
import uk.org.ogsadai.dqp.execute.workflow.PipelineWorkflowBuilder;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.request.RequestExecutionStatus;
import uk.org.ogsadai.resource.request.RequestStatus;

/**
 * An implementation of a partition that is executed on a remote node.
 *
 * @author The OGSA-DAI Project Team.
 */
public class RemotePartition implements Partition {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(RemotePartition.class);

    /** Evaluation node of the partition */
    private final EvaluationNode mNode;

    /** Details relating to the parent request. */
    private final RequestDetails mRequestDetails;

    /** Main pipeline to execute */
    private PipelineWorkflow mPipeline;

    /** List of setup workflows */
    private final List<Workflow> mSetup;

    /** List of data sources */
    private final List<ResourceID> mDataSources;

    /** List of data sinks */
    private final List<ResourceID> mDataSinks;

    /** Root operator of this partition */
    private Operator mRoot;

    /** DRER corresponding to the evaluation node */
    private DataRequestExecutionResource mDRER;

    /** Request status of the main workflow (if available) */
    private RequestStatus mRequestStatus;

    /** Request statuses of the setup workflows */
    private List<RequestStatus> mSetupStatus;

    /** Partition listeners list. */
    List<PartitionEventListener> mPartitionListeners;

    /** 
     * Poll interval (in milliseconds) between checks to see if a request
     * has completed.
     */
    private final int mPollInterval;

    /**
     * Creates a new remote partition.
     * 
     * @param node
     *            the evaluation node that executes the partition
     * @param requestDetails 
     *            details relating to the parent request
     * @param pollInterval
     *            interval in milliseconds between checks to see if a
     *            request has completed.
     */
    public RemotePartition(EvaluationNode node, RequestDetails requestDetails, int pollInterval) {
        mNode = node;
        mRequestDetails = requestDetails;
        mSetup = new ArrayList<Workflow>();
        mDataSources = new ArrayList<ResourceID>();
        mDataSinks = new ArrayList<ResourceID>();
        mSetupStatus = new ArrayList<RequestStatus>();
        mPollInterval = pollInterval;
    }

    /**
     * {@inheritDoc}
     */
    public void buildWorkflow(PipelineWorkflowBuilder builder) throws ActivityConstructionException {
        builder.build(mRoot);
        mPipeline = builder.getPipelineWorkflow();
        if (LOG.isDebugEnabled()) {
            LOG.debug(mPipeline.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    public EvaluationNode getEvaluationNode() {
        return mNode;
    }

    /**
     * Adds a data source to the list to be created.
     * 
     * @param dataSource
     *            data source ID
     */
    public void addDataSourceToSetup(String dataSource) {
        PipelineWorkflow workflow = new PipelineWorkflow();
        GenericActivity create = new GenericActivity("uk.org.ogsadai.CreateDataSource");
        create.createInput("resourceId");
        create.createOutput("result");
        ResourceID dsID = new ResourceID(dataSource);
        mDataSources.add(dsID);
        create.addInput("resourceId", dsID.toString());
        workflow.add(create);
        GenericActivity deliverToRequestStatus = new GenericActivity("uk.org.ogsadai.DeliverToRequestStatus");
        deliverToRequestStatus.createInput("input");
        deliverToRequestStatus.connectInput("input", create.getOutput("result"));
        workflow.add(deliverToRequestStatus);
        mSetup.add(workflow);
    }

    /**
     * Adds a data sink to the list to be created.
     * 
     * @param dataSink
     *            data sinkID
     */
    public void addDataSinkToSetup(String dataSink) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding data sink " + dataSink + " to partition for : " + mNode);
        }
        PipelineWorkflow workflow = new PipelineWorkflow();
        GenericActivity create = new GenericActivity("uk.org.ogsadai.CreateDataSink");
        create.createInput("resourceId");
        create.createOutput("result");
        ResourceID dsID = new ResourceID(dataSink);
        mDataSinks.add(dsID);
        create.addInput("resourceId", dsID.toString());
        workflow.add(create);
        GenericActivity deliverToRequestStatus = new GenericActivity("uk.org.ogsadai.DeliverToRequestStatus");
        deliverToRequestStatus.createInput("input");
        deliverToRequestStatus.connectInput("input", create.getOutput("result"));
        workflow.add(deliverToRequestStatus);
        mSetup.add(workflow);
    }

    /**
     * {@inheritDoc}
     */
    public void execute() throws ExecutionException {
        RequestResource request = null;
        try {
            if (mDRER == null) {
                loadDRER();
            }
            request = mDRER.execute(mPipeline, RequestExecutionType.ASYNCHRONOUS);
            if (mPartitionListeners != null) {
                for (PartitionEventListener pl : mPartitionListeners) {
                    pl.partitionExecuteEvent(mRequestDetails, request.getResourceID(), mNode, mPipeline);
                }
            }
            pollUntilRequestCompleted(request, mPollInterval);
            mRequestStatus = request.getRequestStatus();
        } catch (RequestExecutionException e) {
            mRequestStatus = e.getRequestResource().getLocalRequestStatus();
            throw new ExecutionException(e);
        } catch (InterruptedException e) {
            try {
                request.destroy();
                request = null;
            } catch (Throwable t) {
                LOG.error(new ExecutionException(e));
            }
            Thread.currentThread().interrupt();
        } catch (Throwable e) {
            throw new ExecutionException(e);
        } finally {
            try {
                if (request != null) {
                    request.destroy();
                }
            } catch (Throwable t) {
                LOG.error(new ExecutionException(t));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executeSetup() throws ExecutionException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Remote partition " + mNode + ": ENTERING SETUP.");
        }
        try {
            if (!mSetup.isEmpty() && mDRER == null) {
                loadDRER();
            }
            for (Workflow workflow : mSetup) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Creating data source/sink for : " + mNode);
                }
                RequestResource request = mDRER.execute(workflow, RequestExecutionType.SYNCHRONOUS);
                mSetupStatus.add(request.getRequestStatus());
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Created data source/sink for : " + mNode);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Remote partion " + mNode + ": SETUP COMPLETED.");
            }
        } catch (RequestExecutionException e) {
            RequestStatus status = e.getRequestResource().getLocalRequestStatus();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Got an error during setup of " + mNode);
            }
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOG.debug(sw.toString());
            mSetupStatus.add(status);
            throw new ExecutionException(e);
        } catch (Throwable e) {
            throw new ExecutionException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void cleanup() {
        LOG.debug("Remote partition " + mNode + ": ENTERING CLEANUP.");
        for (ResourceID id : mDataSources) {
            DataSourceResource dataSource = mNode.getDataSourceResource(id, mRequestDetails);
            try {
                dataSource.destroy();
                LOG.debug("Remote partition " + mNode + ": data source " + id + " destroyed.");
            } catch (Throwable e) {
                LOG.warn(e);
            }
        }
        for (ResourceID id : mDataSinks) {
            DataSinkResource dataSink = mNode.getDataSinkResource(id, mRequestDetails);
            try {
                dataSink.destroy();
                LOG.debug("Remote partition " + mNode + ": data sink " + id + " destroyed.");
            } catch (Throwable e) {
                LOG.warn(e);
            }
        }
        LOG.debug("Remote partition " + mNode + ": CLEANUP COMPLETED.");
    }

    /** 
     * {@inheritDoc}
     */
    public void setRoot(Operator root) {
        mRoot = root;
    }

    /**
     * {@inheritDoc}
     */
    public Operator getRoot() {
        return mRoot;
    }

    /**
     * {@inheritDoc}
     */
    public RequestStatus getRequestStatus() {
        return mRequestStatus;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return "REMOTE PARTITION (" + mNode + ")" + mRoot;
    }

    /**
     * Loads the data request execution node proxy used to talk to the remote
     * partition.
     */
    private void loadDRER() {
        mDRER = mNode.getDRER(mRequestDetails);
    }

    /**
     * Polls until a request is completed. This implementation differs slightly
     * from the client toolkit version as it can be interrupted.
     * 
     * @param request
     *            request to wait for
     * @param pollInterval
     *            interval between polls
     * @throws RequestException
     *             if a request exception occurs while retrieving the request
     *             execution status
     * @throws ResourceUnknownException
     *             if the request resource is unknown
     * @throws ClientException
     *             a client exception
     * @throws InterruptedException
     *             if the current thread was interrupted
     */
    private void pollUntilRequestCompleted(RequestResource request, int pollInterval) throws RequestException, ResourceUnknownException, ClientException, InterruptedException {
        boolean completed = false;
        while (!completed) {
            RequestExecutionStatus status = request.getRequestExecutionStatus();
            if (status == RequestExecutionStatus.COMPLETED) {
                completed = true;
            } else if (status == RequestExecutionStatus.ERROR) {
                RequestStatus requestStatus = getRequestStatus();
                throw new RequestErrorException(request, requestStatus);
            } else if (status == RequestExecutionStatus.TERMINATED) {
                throw new RequestTerminatedException(request);
            } else if (status == RequestExecutionStatus.COMPLETED_WITH_ERROR) {
                throw new RequestCompletedWithErrorException(request);
            } else {
                Thread.sleep(pollInterval);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void registerPartitionEventListeners(List<PartitionEventListener> partitionListeners) {
        mPartitionListeners = partitionListeners;
    }
}
