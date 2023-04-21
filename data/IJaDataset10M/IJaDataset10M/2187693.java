package uk.org.ogsadai.activity.dqp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.RequestStatusBuilder;
import uk.org.ogsadai.activity.authorization.ActivityAuthorizationException;
import uk.org.ogsadai.activity.dqp.preprocessor.PreProcessException;
import uk.org.ogsadai.activity.dqp.preprocessor.PreProcessor;
import uk.org.ogsadai.activity.event.ActivityListener;
import uk.org.ogsadai.activity.event.LoggingActivityListener;
import uk.org.ogsadai.activity.event.RequestStatusBuildingActivityListener;
import uk.org.ogsadai.activity.extension.RequestActivity;
import uk.org.ogsadai.activity.extension.RequestContextActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.extension.ResourceFactoryActivity;
import uk.org.ogsadai.activity.extension.ResourceManagerActivity;
import uk.org.ogsadai.activity.extension.SecureActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.BlockReader;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.pipeline.ActivityPipeline;
import uk.org.ogsadai.activity.request.OGSADAIChildRequestConfiguration;
import uk.org.ogsadai.activity.request.status.SimpleRequestStatusBuilder;
import uk.org.ogsadai.authorization.AuthorizingTaskFactory;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.client.toolkit.PipelineWorkflow;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.client.toolkit.exception.ActivityIllegalStateException;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.common.RequestDetails;
import uk.org.ogsadai.dqp.common.simple.SimpleRequestDetails;
import uk.org.ogsadai.dqp.execute.Coordinator;
import uk.org.ogsadai.dqp.execute.ExecutionException;
import uk.org.ogsadai.dqp.execute.LocalWorkflowProcessor;
import uk.org.ogsadai.dqp.execute.partition.PartitionEventListener;
import uk.org.ogsadai.dqp.execute.partition.SimplePartitionFactory;
import uk.org.ogsadai.dqp.execute.request.SubworkflowRequestBuilder;
import uk.org.ogsadai.exception.RequestProcessingException;
import uk.org.ogsadai.exception.RequestTerminatedException;
import uk.org.ogsadai.exception.RequestUserException;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.ResourceFactory;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceIDAlreadyAssignedException;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceUnknownException;
import uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor;
import uk.org.ogsadai.resource.dataresource.dqp.SimpleRequestDQPFederation;
import uk.org.ogsadai.resource.datasink.DataSinkResource;
import uk.org.ogsadai.resource.datasource.DataSourceResource;
import uk.org.ogsadai.resource.request.RequestResource;
import uk.org.ogsadai.util.concurrency.TaskProcessingService;

/**
 * An activity that executes an SQL query on a DQP resource and
 * produces a list of tuples containing the results of the query. 
 * This supports the same inputs as the relational SQLQuery activity
 * and so can be exposed with the same name as that activity so client
 * workflows can work transparently on relational or DQP resources.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>expression</code>. Type: {@link java.lang.String}. SQL query 
 * expression.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.  The tuples produced
 * by the query.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters:
 * </p>
 * <ul>
 * <li>
 * <code>dai.dqp.preprocessor</code>. Optional.
 * </li>
 * </ul>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: 
 * </p>
 * <ul>
 * <li>
 * <code>uk.org.ogsadai.activity.contract.SQLQuery</code>
 * </li>
 * </ul>
 * <p>
 * Target data resource:
 * <ul>
 * <li>
 * {@link uk.org.ogsadai.resource.dataresource.dqp.DQPResourceAccessor}
 * </li>
 * </ul>
 * </p>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * This activity accepts a sequence of SQL query expressions as input and is 
 * targeted at a relational data resource. In each iteration one input query is
 * processed by executing the query across the target data resource. The results
 * of each iteration is a OGSA-DAI list of tuples with a metadata header block. 
 * </li>
 * <li>
 * Partial data may be produced if an error occurs at any stage of processing. 
 * </li>
 * <li>
 * An <code>ActivitySQLUserException</code> is raised if there was an access 
 * error at the data resource for example syntax errors in the SQL  expression
 * or if the target table does not exist.  
 * </li>
 * </ul> 
 * <p>
 * Note that this activity cannot be used for updates.
 * </p>
 *
 * @author The OGSA-DAI Project Team.
 */
public class SQLQueryActivity extends MatchedIterativeActivity implements ResourceActivity, RequestContextActivity, ResourceFactoryActivity, ResourceManagerActivity, LocalWorkflowProcessor, SecureActivity, RequestActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008.";

    /** Logger. */
    private static final DAILogger LOGGER = DAILogger.getLogger(SQLQueryActivity.class);

    /** Expression input name. */
    private static final String INPUT_SQL = "expression";

    /** Data output name. */
    private static final String OUTPUT_DATA = "data";

    /** Dynamically created input streaming data from the subworkflow. */
    private static final String INPUT_SUBWORKFLOW_DATA = "data";

    /** Output block writer. */
    private BlockWriter mOutput;

    /** Resource accessor. */
    private DQPResourceAccessor mResource;

    /** Task factory for constructing activity pipeline tasks. */
    private AuthorizingTaskFactory mTaskFactory;

    /** Processes activity pipelines. */
    private TaskProcessingService mTaskProcessingService;

    /** Used to create data sources. */
    private ResourceFactory mResourceFactory;

    /** Subworkflow input reader. */
    private OutputInputConnector mConnector;

    /** Request status builder for the sub-workflow. */
    private SimpleRequestStatusBuilder mSubworkflowRequestStatusBuilder;

    /** Resource manager. */
    private ResourceManager mResourceManager;

    /** Preprocessor. */
    private PreProcessor mPreProcessor = null;

    /** The security context. */
    private SecurityContext mSecurityContext;

    /** Parent Request Resource. */
    private RequestResource mParentRequest;

    /** Details of parent request. */
    private RequestDetails mRequestDetails;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { new TypedActivityInput(INPUT_SQL, String.class) };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(mParentRequest.getResourceID().toString());
        }
        mRequestDetails = new SimpleRequestDetails(mResource.getResource().getResourceID(), mParentRequest, mSecurityContext, getActivityDescriptor().getInstanceName());
        mPreProcessor = mResource.getPreProcessor();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        Coordinator coordinator = new Coordinator();
        coordinator.setRequestFederation(new SimpleRequestDQPFederation(mResource.getFederation(), mRequestDetails));
        coordinator.setTaskProcessingService(mTaskProcessingService);
        coordinator.setCompilerConfiguration(mResource.getCompilerConfiguration());
        coordinator.setResourceAccessor(mResource);
        coordinator.setRequestResourceID(mParentRequest.getResourceID());
        coordinator.setPartitionFactory(new SimplePartitionFactory(this, mResource, getPartitionListeners()));
        String sql = (String) iterationData[0];
        if (mPreProcessor != null) {
            try {
                sql = mPreProcessor.preprocessQuery(sql, mResource.getResource().getResourceID(), mSecurityContext);
            } catch (PreProcessException e) {
                LOGGER.debug(e.getExceptionID().toString());
                LOGGER.debug(e.getMessage());
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                LOGGER.debug(sw.toString());
                throw new ActivityProcessingException(e);
            }
        }
        try {
            coordinator.execute(sql, mRequestDetails);
        } catch (Throwable e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            LOGGER.debug(sw.toString());
            throw new ActivityUserException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
    }

    /**
     * {@inheritDoc}
     * Notifies other threads when input INPUT_DATA becomes available.
     */
    public void addInput(final String name, final BlockReader input) {
        super.addInput(name, input);
        if (name.equals(INPUT_SUBWORKFLOW_DATA)) {
            mConnector.setInput(input);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public Class<DQPResourceAccessor> getTargetResourceAccessorClass() {
        return DQPResourceAccessor.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor) {
        mResource = (DQPResourceAccessor) resourceAccessor;
    }

    /**
     * {@inheritDoc}
     */
    public void setResourceManager(ResourceManager resourceManager) {
        mResourceManager = resourceManager;
    }

    /**
     * {@inheritDoc}
     */
    public void setRequestContext(OGSADAIChildRequestConfiguration context) {
        context.registerActivityListener(new LoggingActivityListener());
        mSubworkflowRequestStatusBuilder = new SimpleRequestStatusBuilder("internal");
        ActivityListener listener = new RequestStatusBuildingActivityListener(mSubworkflowRequestStatusBuilder);
        context.registerActivityListener(listener);
        mTaskFactory = context.getAuthorizingTaskFactory();
        mTaskProcessingService = context.getTaskProcessingService();
    }

    /**
     * {@inheritDoc}
     */
    public void setResourceFactory(ResourceFactory resourceFactory) {
        mResourceFactory = resourceFactory;
    }

    /**
     * {@inheritDoc}
     */
    public void createDataSources(List<String> dataSourceNames) throws ExecutionException {
        for (String dataSourceName : dataSourceNames) {
            try {
                DataSourceResource dataSource = mResourceFactory.createDataSourceResource();
                mResourceFactory.addResource(new ResourceID(dataSourceName), dataSource);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Created local data source: " + dataSourceName);
                }
            } catch (ResourceIDAlreadyAssignedException e) {
                throw new ExecutionException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void createDataSinks(List<String> dataSinkNames) throws ExecutionException {
        for (String dataSinkName : dataSinkNames) {
            try {
                DataSinkResource dataSink = mResourceFactory.createDataSinkResource();
                mResourceFactory.addResource(new ResourceID(dataSinkName), dataSink);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Created local data sink: " + dataSinkName);
                }
            } catch (ResourceIDAlreadyAssignedException e) {
                throw new ExecutionException(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void destroyDataSources(List<String> dataSourceNames) {
        for (String dataSourceName : dataSourceNames) {
            Resource resource;
            try {
                ResourceID id = new ResourceID(dataSourceName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Destroying local data source: " + id);
                }
                resource = mResourceManager.getResource(id);
                resource.destroy();
                mResourceManager.deleteResource(id);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Destroyed local data source " + id);
                }
            } catch (ResourceUnknownException e) {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void destroyDataSinks(List<String> dataSinkNames) {
        for (String dataSinkName : dataSinkNames) {
            Resource resource;
            try {
                ResourceID id = new ResourceID(dataSinkName);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Destroying local data sink: " + id);
                }
                resource = mResourceManager.getResource(id);
                resource.destroy();
                mResourceManager.deleteResource(id);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Destroyed local data sink " + id);
                }
            } catch (ResourceUnknownException e) {
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public void executePipeline(PipelineWorkflow workflow, SingleActivityOutput source) throws ExecutionException {
        if (source == null) {
            executeUnconnectedPipeline(workflow);
        } else {
            executeConnectedPipeline(workflow, source);
        }
    }

    /**
     * Executes a subworkflow that is not connected to this activity.
     * 
     * @param workflow
     *            subworkflow to execute
     * @throws ExecutionException
     *             if a problem occured during execution
     */
    private void executeUnconnectedPipeline(PipelineWorkflow workflow) throws ExecutionException {
        try {
            SubworkflowRequestBuilder requestBuilder = new SubworkflowRequestBuilder();
            workflow.buildRequest(requestBuilder);
            ActivityPipeline pipeline = requestBuilder.getActivityPipeline();
            final Callable subworkflow = mTaskFactory.createActivityPipelineProcessingTask(pipeline);
            Future future = mTaskProcessingService.submit(subworkflow);
            future.get();
        } catch (ActivityIllegalStateException e) {
            throw new ExecutionException(e);
        } catch (ActivityAuthorizationException e) {
            throw new ExecutionException(e);
        } catch (InterruptedException e) {
            throw new ExecutionException(new ActivityTerminatedException());
        } catch (java.util.concurrent.ExecutionException e) {
            throw new ExecutionException(e.getCause());
        }
    }

    /**
     * Executes a subworkflow whose output is connected to an input of this
     * activity.
     * 
     * @param workflow
     *            subworkflow to execute
     * @param source
     *            the output of the subworkflow that connect to this activity
     * @throws ExecutionException
     *             if a problem occured during execution
     */
    private void executeConnectedPipeline(PipelineWorkflow workflow, SingleActivityOutput source) throws ExecutionException {
        try {
            try {
                SubworkflowRequestBuilder requestBuilder = new SubworkflowRequestBuilder();
                requestBuilder.connectParent(this, INPUT_SUBWORKFLOW_DATA, source);
                workflow.buildRequest(requestBuilder);
                ActivityPipeline pipeline = requestBuilder.getActivityPipeline();
                final Callable subworkflow = mTaskFactory.createActivityPipelineProcessingTask(pipeline);
                final Set<Callable> tasks = new HashSet<Callable>();
                tasks.add(subworkflow);
                mConnector = new OutputInputConnector(mOutput);
                tasks.add(mConnector);
                mTaskProcessingService.processAll(tasks, true);
            } catch (RequestProcessingException e) {
                throw e.getCause();
            } catch (RequestUserException e) {
                throw e.getCause();
            } catch (RequestTerminatedException e) {
                throw new ActivityTerminatedException();
            }
        } catch (Throwable e) {
            removeSubworkflowInput();
            throw new ExecutionException(e);
        }
    }

    /**
     * Removes the subworkflow input from the activity descriptor.
     */
    @SuppressWarnings("unchecked")
    private void removeSubworkflowInput() {
        List inputs = getActivityDescriptor().getInputs();
        for (Object object : inputs) {
            uk.org.ogsadai.activity.pipeline.ActivityInput input = (uk.org.ogsadai.activity.pipeline.ActivityInput) object;
            if (input.getInputName().equals(INPUT_SUBWORKFLOW_DATA)) {
                getActivityDescriptor().removeInput(input);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public RequestStatusBuilder getRequestStatusBuilder() {
        return mSubworkflowRequestStatusBuilder;
    }

    /**
     * {@inheritDoc}
     */
    public EvaluationNode getLocalNode() {
        return mResource.getLocalNode();
    }

    /**
     * {@inheritDoc}
     */
    public void setSecurityContext(SecurityContext context) {
        mSecurityContext = context;
    }

    /**
     * {@inheritDoc}
     */
    public void setRequest(RequestResource request) {
        mParentRequest = request;
    }

    /**
     * Scans OGSADAIContext for registered PatritionEvenListerer objects and
     * returns a list of matches.
     * 
     * @return a list of partition event listeners
     */
    private List<PartitionEventListener> getPartitionListeners() {
        List<PartitionEventListener> result = new ArrayList<PartitionEventListener>();
        Set<Object> keys = OGSADAIContext.getInstance().getKeys();
        for (Object k : keys) {
            Object o = OGSADAIContext.getInstance().get(k);
            if (o instanceof PartitionEventListener) result.add((PartitionEventListener) o);
        }
        return result;
    }
}
