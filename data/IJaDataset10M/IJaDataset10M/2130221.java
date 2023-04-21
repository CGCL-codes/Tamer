package uk.org.ogsadai.activity.workflow;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import junit.framework.TestCase;
import org.easymock.MockControl;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityFrameworkConfiguration;
import uk.org.ogsadai.activity.ActivityID;
import uk.org.ogsadai.activity.MockTaskProcessingService;
import uk.org.ogsadai.activity.SimpleActivityFrameworkConfiguration;
import uk.org.ogsadai.activity.SimpleActivityManager;
import uk.org.ogsadai.activity.SupportedActivities;
import uk.org.ogsadai.activity.UnsupportedActivityException;
import uk.org.ogsadai.activity.concurrency.WorkflowProcessingTask;
import uk.org.ogsadai.activity.event.ActivityListener;
import uk.org.ogsadai.activity.event.EventDetails;
import uk.org.ogsadai.activity.event.Warning;
import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.ActivityInputLiteral;
import uk.org.ogsadai.activity.pipeline.ActivityPipeline;
import uk.org.ogsadai.activity.pipeline.SimpleActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.SimpleActivityPipeline;
import uk.org.ogsadai.activity.request.RequestConfiguration;
import uk.org.ogsadai.activity.request.SimpleRequestConfiguration;
import uk.org.ogsadai.authorization.NullWorkflowAuthorizer;
import uk.org.ogsadai.authorization.SecurityContext;
import uk.org.ogsadai.authorization.NullSecurityContext;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.RequestProcessingException;
import uk.org.ogsadai.exception.RequestTerminatedException;
import uk.org.ogsadai.exception.RequestUserException;
import uk.org.ogsadai.exception.VisitorException;
import uk.org.ogsadai.persistence.resource.ResourceStateDAO;
import uk.org.ogsadai.resource.Resource;
import uk.org.ogsadai.resource.ResourceID;
import uk.org.ogsadai.resource.ResourceManager;
import uk.org.ogsadai.resource.ResourceState;
import uk.org.ogsadai.resource.ResourceType;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.resource.datasink.DataSinkResource;
import uk.org.ogsadai.resource.datasource.DataSourceResource;
import uk.org.ogsadai.resource.drer.DRER;
import uk.org.ogsadai.resource.event.ResourceManagerEventListener;
import uk.org.ogsadai.resource.request.RequestResource;
import uk.org.ogsadai.resource.session.SessionResource;

/**
 * Test class for <code>SimpleWorkflowProcessor</code>.
 *
 * @author The OGSA-DAI Project Team
 */
public class SimpleWorkflowProcessorTest extends TestCase {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** A sequence workflow. */
    private SequenceWorkflow mSequence;

    /** Control for mock workflow object. */
    private MockControl mControlWorkflow;

    /** Mock workflow object. */
    private Workflow mMockWorkflow;

    /** Control for mock workflow object. */
    private MockControl mControlWorkflow2;

    /** Mock workflow object. */
    private Workflow mMockWorkflow2;

    /** Control for mock request configuration. */
    private MockControl mControlRequestConfiguration;

    /** Mock request configuration. */
    private RequestConfiguration mMockRequestConfiguration;

    /** Mock task processing service. */
    private MockTaskProcessingService mMockTaskProcessingService;

    /** Activity framework configuration. */
    private ActivityFrameworkConfiguration mConfiguration;

    /** Request context for use by tests. */
    private SimpleRequestConfiguration mRequestConfig;

    /** Workflow processor to test. */
    private WorkflowProcessor mWorkflowProcessor;

    /**
     * Runs the test.
     * 
     * @param args
     *     Unused.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(SimpleWorkflowProcessorTest.class);
    }

    /**
     * Creates a test case.
     * 
     * @param name
     *     Name of test case.
     */
    public SimpleWorkflowProcessorTest(String name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    protected void setUp() throws Exception {
        super.setUp();
        mControlWorkflow = MockControl.createControl(Workflow.class);
        mMockWorkflow = (Workflow) mControlWorkflow.getMock();
        mControlWorkflow2 = MockControl.createControl(Workflow.class);
        mMockWorkflow2 = (Workflow) mControlWorkflow2.getMock();
        mControlRequestConfiguration = MockControl.createControl(RequestConfiguration.class);
        mMockRequestConfiguration = (RequestConfiguration) mControlRequestConfiguration.getMock();
        mMockTaskProcessingService = new MockTaskProcessingService();
        SupportedActivities supportedActivities = new SupportedActivities() {

            public Class getActivityClass(ActivityDescriptor descriptor) throws UnsupportedActivityException {
                Class activityClass = null;
                if (descriptor.getActivityName().equals(new ActivityID("x.head"))) {
                    activityClass = HeadActivity.class;
                } else if (descriptor.getActivityName().equals(new ActivityID("x.body"))) {
                    activityClass = BodyActivity.class;
                } else if (descriptor.getActivityName().equals(new ActivityID("x.tail"))) {
                    activityClass = TailActivity.class;
                }
                return activityClass;
            }

            public Properties getActivityProperties(ActivityDescriptor descriptor) throws UnsupportedActivityException {
                return new Properties();
            }
        };
        ResourceManager manager = new ResourceManager() {

            public ResourceID createUniqueID() {
                return null;
            }

            public ResourceID createUniqueID(String namespace) {
                return null;
            }

            public void deleteResource(ResourceID resourceID) {
            }

            public void deleteResourceStateTemplate(ResourceID resourceID) {
            }

            public Resource getResource(ResourceID resourceID) {
                return null;
            }

            public Resource getPublicResource(ResourceID resourceID) {
                return null;
            }

            public Resource getResource(ResourceID resourceID, ResourceType resourceType) {
                return null;
            }

            public Resource getPublicResource(ResourceID resourceID, ResourceType resourceType) {
                return null;
            }

            public ResourceState getResourceStateTemplate(ResourceID resourceID) {
                return null;
            }

            public ResourceState getResourceStateTemplate(ResourceID resourceID, ResourceType resourceType) {
                return null;
            }

            public void insertResource(Resource resource, SecurityContext sc) {
            }

            public void insertResourceStateTemplate(ResourceState resourceStateTemplate) {
            }

            public void setResourceStateDAO(ResourceStateDAO resoureStateDAO) {
            }

            public boolean isResourceKnown(ResourceID resourceID) {
                return false;
            }

            public boolean isResourceStateTemplateKnown(ResourceID resourceID) {
                return false;
            }

            public List listResources() {
                return null;
            }

            public List listPublicResources() {
                return null;
            }

            public List listResources(ResourceType resourceType) {
                return null;
            }

            public List listPublicResources(ResourceType resourceType) {
                return null;
            }

            public List listResourceStateTemplates() {
                return null;
            }

            public void insertResource(DRER resource) {
            }

            public void insertResource(DataResource resource) {
            }

            public void insertResource(DataSinkResource resource) {
            }

            public void insertResource(DataSourceResource resource) {
            }

            public void insertResource(RequestResource resource) {
            }

            public void insertResource(SessionResource resource) {
            }

            public void flush() {
            }

            public void addResourceManagerEventListener(ResourceManagerEventListener listener) {
            }

            public void removeResourceManagerEventListener(ResourceManagerEventListener listener) {
            }
        };
        OGSADAIContext.getInstance().put(OGSADAIConstants.RESOURCE_MANAGER, manager);
        OGSADAIContext.getInstance().put(OGSADAIConstants.ACTIVITY_MANAGER, new SimpleActivityManager());
        mConfiguration = new SimpleActivityFrameworkConfiguration(supportedActivities, manager);
        mRequestConfig = new SimpleRequestConfiguration(mConfiguration, new NullRequestStatusBuilder(), new NullWorkflowAuthorizer(), new NullSecurityContext());
        mWorkflowProcessor = new SimpleWorkflowProcessor(mMockRequestConfiguration);
    }

    /**
     * {@inheritDoc}
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        mConfiguration.getTaskProcessingService().shutdownNow();
    }

    /**
     * Test processing a sequence workflow.
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testSequenceProcess() throws Exception {
        Workflow workflow = new SequenceWorkflow("test");
        workflow.addChild(mMockWorkflow);
        workflow.addChild(mMockWorkflow2);
        mMockWorkflow.accept(mWorkflowProcessor);
        mMockWorkflow2.accept(mWorkflowProcessor);
        mControlWorkflow.replay();
        mControlWorkflow2.replay();
        workflow.accept(mWorkflowProcessor);
        mControlWorkflow.verify();
        mControlWorkflow2.verify();
    }

    /**
     * Test processing a sequence workflow when an exception occurs in
     * a child workflow.
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testSequenceProcessingException() throws Exception {
        Workflow workflow = new SequenceWorkflow("test");
        workflow.addChild(mMockWorkflow);
        mMockWorkflow.accept(mWorkflowProcessor);
        mControlWorkflow.setThrowable(new VisitorException(new RequestProcessingException()));
        mControlWorkflow.replay();
        try {
            try {
                workflow.accept(mWorkflowProcessor);
                fail("VisitorException was expected.");
            } catch (VisitorException visitorException) {
                mWorkflowProcessor.throwExceptions(visitorException);
                fail("RequestProcessingException was expected.");
            }
        } catch (RequestProcessingException e) {
        }
        mControlWorkflow.verify();
    }

    /**
     * Test processing a parallel workflow.
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testParallelProcess() throws Exception {
        Workflow workflow = new ParallelWorkflow("test");
        workflow.addChild(mMockWorkflow);
        workflow.addChild(mMockWorkflow2);
        mMockWorkflow.accept(mWorkflowProcessor);
        mMockWorkflow2.accept(mWorkflowProcessor);
        mControlWorkflow.replay();
        mControlWorkflow2.replay();
        mMockRequestConfiguration.getTaskProcessingService();
        mControlRequestConfiguration.setReturnValue(mMockTaskProcessingService);
        mControlRequestConfiguration.replay();
        workflow.accept(mWorkflowProcessor);
        Set tasks = mMockTaskProcessingService.getProcessedTasks();
        assertEquals("The wrong number of tasks were processed.", 2, tasks.size());
        Set expectedComponents = new HashSet();
        expectedComponents.add(mMockWorkflow);
        expectedComponents.add(mMockWorkflow2);
        Set actualComponents = new HashSet();
        for (Iterator i = tasks.iterator(); i.hasNext(); ) {
            WorkflowProcessingTask task = (WorkflowProcessingTask) i.next();
            actualComponents.add(task.getProcessingComponent());
        }
        assertTrue("The expected child request components were not processed.", actualComponents.containsAll(expectedComponents));
    }

    /**
     * Tests processing a parallel workflow when the task processing
     * service raises a <code>RequestProcessingException</code> during
     * processing. 
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testProcessWithRequestProcessingException() throws Exception {
        Workflow workflow = new ParallelWorkflow("test");
        workflow.addChild(mMockWorkflow);
        workflow.addChild(mMockWorkflow2);
        mMockRequestConfiguration.getTaskProcessingService();
        mControlRequestConfiguration.setReturnValue(mMockTaskProcessingService);
        mControlRequestConfiguration.replay();
        mMockTaskProcessingService.throwRequestProcessingException();
        try {
            try {
                workflow.accept(mWorkflowProcessor);
                fail("VisitorException was expected.");
            } catch (VisitorException visitorException) {
                mWorkflowProcessor.throwExceptions(visitorException);
                fail("RequestProcessingException was expected");
            }
        } catch (RequestProcessingException e) {
        }
    }

    /**
     * Tests processing a parallel workflow when the task processing
     * service raises a <code>RequestTerminatedException</code> during
     * processing. 
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testProcessWithRequestTerminatedException() throws Exception {
        Workflow workflow = new ParallelWorkflow("test");
        workflow.addChild(mMockWorkflow);
        workflow.addChild(mMockWorkflow2);
        mMockRequestConfiguration.getTaskProcessingService();
        mControlRequestConfiguration.setReturnValue(mMockTaskProcessingService);
        mControlRequestConfiguration.replay();
        mMockTaskProcessingService.throwRequestTerminatedException();
        try {
            try {
                workflow.accept(mWorkflowProcessor);
                fail("VisitorException was expected.");
            } catch (VisitorException visitorException) {
                mWorkflowProcessor.throwExceptions(visitorException);
                fail("RequestTerminatedException was expected");
            }
        } catch (RequestTerminatedException e) {
        }
    }

    /**
     * Tests processing a parallel workflow when the task processing
     * service raises a <code>RequestUserException</code> during
     * processing. 
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testProcessWithRequestUserException() throws Exception {
        Workflow workflow = new ParallelWorkflow("test");
        workflow.addChild(mMockWorkflow);
        workflow.addChild(mMockWorkflow2);
        mMockRequestConfiguration.getTaskProcessingService();
        mControlRequestConfiguration.setReturnValue(mMockTaskProcessingService);
        mControlRequestConfiguration.replay();
        mMockTaskProcessingService.throwRequestUserException();
        try {
            try {
                workflow.accept(mWorkflowProcessor);
                fail("VisitorException was expected.");
            } catch (VisitorException visitorException) {
                mWorkflowProcessor.throwExceptions(visitorException);
                fail("RequestUserException was expected");
            }
        } catch (RequestUserException e) {
        }
    }

    /**
     * Tests the case where many connected activities are processed
     * and thousands of large data blocks are streamed along the
     * pipeline. 
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testPipelineWithManyConcurrentActivities() throws Exception {
        WorkflowProcessor processor = new SimpleWorkflowProcessor(mRequestConfig);
        ActivityPipeline activityGraph = createActivityGraph(100, 100, 100);
        ActivityPipelineWorkflow component = new ActivityPipelineWorkflow(activityGraph);
        component.accept(processor);
    }

    /**
     * Tests the case where an activity produces so much data that an
     * <code>OutOfMemoryError</code> occurs. This should be wrapped in
     * an <code>ActivityProcessingException</code>.
     * 
     * @throws Exception 
     *     If an error occurs.
     */
    public void testPipelineOutOfMemoryCondition() throws Exception {
        ActivityErrorListener listener = new ActivityErrorListener();
        mRequestConfig.registerActivityListener(listener);
        WorkflowProcessor processor = new SimpleWorkflowProcessor(mRequestConfig);
        ActivityPipeline activityGraph = createActivityGraph(3, 10, 1000000000);
        ActivityPipelineWorkflow component = new ActivityPipelineWorkflow(activityGraph);
        component.accept(processor);
        assertNotNull("An exception was expected", listener.getException());
        assertNotNull("A chained exception was expected", listener.getException().getCause());
        assertEquals("The exception has an unexpected cause.", OutOfMemoryError.class, listener.getException().getCause().getClass());
    }

    /**
     * Creates an activity graph.
     * 
     * @param numberOfActivities
     *     Number of activities to connect into the pipeline.
     * @param blocks
     *     Number of blocks of data to produce and consume.
     * @param blockSize
     *     Length of each block in characters.
     * @return the activity graph.
     */
    private ActivityPipeline createActivityGraph(final int numberOfActivities, final int blocks, final int blockSize) {
        final ActivityPipeline activityGraph = new SimpleActivityPipeline();
        final ActivityDescriptor[] activities = new ActivityDescriptor[numberOfActivities];
        activities[0] = new SimpleActivityDescriptor("x.head");
        activities[0].addInput(new ActivityInputLiteral("blocks", String.valueOf(blocks)));
        activities[0].addInput(new ActivityInputLiteral("blockSize", String.valueOf(blockSize)));
        for (int i = 1; i < (activities.length - 1); i++) {
            activities[i] = new SimpleActivityDescriptor("x.body");
        }
        activities[numberOfActivities - 1] = new SimpleActivityDescriptor("x.tail");
        activities[numberOfActivities - 1].addInput(new ActivityInputLiteral("blockSize", String.valueOf(blockSize)));
        activityGraph.addActivity(activities[0]);
        for (int i = 1; i < activities.length; i++) {
            activityGraph.addActivity(activities[i]);
            activityGraph.connect(activities[i - 1], "output", activities[i], "input");
        }
        return activityGraph;
    }

    /**
     * Simple activity listener to detect and record exception.
     *
     * @author The OGSA-DAI Project Team
     */
    private class ActivityErrorListener implements ActivityListener {

        /** Copyright statement. */
        private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

        /** Exception detected. */
        private DAIException mException;

        /**
         * Returns the last exception that was emitted.
         * 
         * @return an exception
         */
        public DAIException getException() {
            return mException;
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void completed(Activity source) {
        }

        /**
         * {@inheritDoc}
         */
        public void error(Activity source, DAIException cause) {
            if (mException == null) {
                mException = cause;
            }
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void otherEvent(Activity source, EventDetails details) {
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void pending(Activity source) {
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void processing(Activity source) {
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void terminated(Activity source) {
        }

        /**
         * {@inheritDoc}
         *
         * No-op.
         */
        public void warning(Activity source, Warning warning) {
        }
    }
}
