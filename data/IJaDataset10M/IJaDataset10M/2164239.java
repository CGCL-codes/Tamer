package uk.org.ogsadai.activity.concurrency;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.easymock.MockControl;
import uk.org.ogsadai.activity.Activity;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.activity.ActivityName;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.io.Pipe;
import uk.org.ogsadai.activity.io.PipeManager;
import uk.org.ogsadai.activity.pipeline.ActivityDescriptor;
import uk.org.ogsadai.activity.pipeline.ActivityInputStream;
import uk.org.ogsadai.activity.pipeline.ActivityOutputStream;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;

/**
 * Tests the activity processing task.
 *
 * @author The OGSA-DAI Team.
 */
public class ActivityProcessingTaskTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Control for mock Activity object. */
    private MockControl mControlActivity;

    /** Mock Activity object. */
    private Activity mMockActivity;

    /** Control for mock ActivityDescriptor object. */
    private MockControl mControlActivityDescriptor;

    /** Mock ActivityDescriptor object. */
    private ActivityDescriptor mMockActivityDescriptor;

    /** Control for mock PipeManager object. */
    private MockControl mControlPipeManager;

    /** Mock PipeManager object. */
    private PipeManager mMockPipeManager;

    /** Control for mock Pipe object. */
    private MockControl mControlPipe;

    /** Mock Pipe object. */
    private Pipe mMockPipe;

    /** Processing task to test */
    private ActivityProcessingTask mTask;

    /**
     * Constructor
     * 
     * @param arg0
     */
    public ActivityProcessingTaskTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mControlActivity = MockControl.createControl(Activity.class);
        mMockActivity = (Activity) mControlActivity.getMock();
        mControlActivityDescriptor = MockControl.createControl(ActivityDescriptor.class);
        mMockActivityDescriptor = (ActivityDescriptor) mControlActivityDescriptor.getMock();
        mControlPipeManager = MockControl.createControl(PipeManager.class);
        mMockPipeManager = (PipeManager) mControlPipeManager.getMock();
        mControlPipe = MockControl.createControl(Pipe.class);
        mMockPipe = (Pipe) mControlPipe.getMock();
        mTask = new ActivityProcessingTask(mMockActivity, mMockPipeManager);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests calling an activity processing task which completes normally.
     * 
     * @throws Exception
     *             if an unexpected exception occurs
     */
    public void testCall() throws Exception {
        mMockActivity.process();
        mControlActivity.expectAndReturn(mMockActivity.getActivityDescriptor(), mMockActivityDescriptor);
        String inputPipe = "pipe1";
        List inputs = new ArrayList();
        inputs.add(new ActivityInputStream("input1", inputPipe));
        String outputPipe = "pipe2";
        List outputs = new ArrayList();
        outputs.add(new ActivityOutputStream("output1", outputPipe));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInputs(), inputs);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getOutputs(), outputs);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(inputPipe), mMockPipe);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(outputPipe), mMockPipe);
        mMockPipe.closeForReading();
        mMockPipe.closeForWriting();
        mControlActivity.replay();
        mControlPipeManager.replay();
        mControlActivityDescriptor.replay();
        mControlPipe.replay();
        mTask.call();
        mControlActivity.verify();
        mControlPipeManager.verify();
        mControlActivityDescriptor.verify();
        mControlPipe.verify();
    }

    /**
     * Tests calling an activity task with a processing error.
     * 
     * @throws Exception
     *             if an unexpected exception occurs
     */
    public void testCallWithProcessingException() throws Exception {
        mMockActivity.process();
        mControlActivity.setThrowable(new ActivityProcessingException(new ErrorID("test")));
        mControlActivity.expectAndReturn(mMockActivity.getActivityDescriptor(), mMockActivityDescriptor, 2);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getActivityName(), new ActivityName("activity"));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInstanceName(), new ActivityInstanceName("activity"));
        String inputPipe = "pipe1";
        List inputs = new ArrayList();
        inputs.add(new ActivityInputStream("input1", inputPipe));
        String outputPipe = "pipe2";
        List outputs = new ArrayList();
        outputs.add(new ActivityOutputStream("output1", outputPipe));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInputs(), inputs);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getOutputs(), outputs);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(inputPipe), mMockPipe);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(outputPipe), mMockPipe);
        mMockPipe.closeForReading();
        mMockPipe.closeForWritingDueToError();
        mControlActivity.replay();
        mControlPipeManager.replay();
        mControlActivityDescriptor.replay();
        mControlPipe.replay();
        try {
            mTask.call();
            fail("ActivityTaskProcessingException expected");
        } catch (ActivityTaskProcessingException e) {
            assertNotNull(e.getCause());
            assertEquals(ActivityProcessingException.class, e.getCause().getClass());
        }
        mControlActivity.verify();
        mControlPipeManager.verify();
        mControlActivityDescriptor.verify();
        mControlPipe.verify();
    }

    /**
     * Tests calling an activity task with a user error.
     * 
     * @throws Exception
     *             if an unexpected exception occurs
     */
    public void testCallWithUserException() throws Exception {
        mMockActivity.process();
        ErrorID errorID = new ErrorID("test");
        mControlActivity.setThrowable(new ActivityUserException(errorID));
        mControlActivity.expectAndReturn(mMockActivity.getActivityDescriptor(), mMockActivityDescriptor, 2);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getActivityName(), new ActivityName("activity"));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInstanceName(), new ActivityInstanceName("activity"));
        String inputPipe = "pipe1";
        List inputs = new ArrayList();
        inputs.add(new ActivityInputStream("input1", inputPipe));
        String outputPipe = "pipe2";
        List outputs = new ArrayList();
        outputs.add(new ActivityOutputStream("output1", outputPipe));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInputs(), inputs);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getOutputs(), outputs);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(inputPipe), mMockPipe);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(outputPipe), mMockPipe);
        mMockPipe.closeForReading();
        mMockPipe.closeForWritingDueToError();
        mControlActivity.replay();
        mControlPipeManager.replay();
        mControlActivityDescriptor.replay();
        mControlPipe.replay();
        try {
            mTask.call();
            fail("ActivityTaskUserException expected");
        } catch (ActivityTaskUserException e) {
            assertNotNull(e.getCause());
            assertEquals(ActivityUserException.class, e.getCause().getClass());
            assertEquals(errorID, ((DAIException) e.getCause()).getErrorID());
        }
        mControlActivity.verify();
        mControlPipeManager.verify();
        mControlActivityDescriptor.verify();
        mControlPipe.verify();
    }

    /**
     * Tests calling an activity task that throws a runtime exception. All pipes
     * must be closed when a runtime exception occurs - output pipes closed due
     * to an exception and input pipes closed for reading.
     * 
     * @throws Exception
     *             if an unexpected exception occurs
     */
    public void testCallWithRuntimeException() throws Exception {
        mMockActivity.process();
        mControlActivity.setThrowable(new RuntimeException());
        mControlActivity.expectAndReturn(mMockActivity.getActivityDescriptor(), mMockActivityDescriptor);
        String inputPipe = "pipe1";
        List inputs = new ArrayList();
        inputs.add(new ActivityInputStream("input1", inputPipe));
        String outputPipe = "pipe2";
        List outputs = new ArrayList();
        outputs.add(new ActivityOutputStream("output1", outputPipe));
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getInputs(), inputs);
        mControlActivityDescriptor.expectAndReturn(mMockActivityDescriptor.getOutputs(), outputs);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(inputPipe), mMockPipe);
        mControlPipeManager.expectAndReturn(mMockPipeManager.getPipe(outputPipe), mMockPipe);
        mMockPipe.closeForReading();
        mMockPipe.closeForWritingDueToError();
        mControlActivity.replay();
        mControlPipeManager.replay();
        mControlActivityDescriptor.replay();
        mControlPipe.replay();
        try {
            mTask.call();
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
        }
        mControlActivity.verify();
        mControlPipeManager.verify();
        mControlActivityDescriptor.verify();
        mControlPipe.verify();
    }
}
