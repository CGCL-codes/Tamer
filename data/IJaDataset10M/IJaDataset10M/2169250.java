package barsuift.simLife.process;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import barsuift.simLife.condition.BoundConditionState;
import barsuift.simLife.condition.CyclicConditionState;
import barsuift.simLife.message.PublisherTestHelper;
import static org.fest.assertions.Assertions.assertThat;

public class BasicMainSynchronizerTest {

    private BasicMainSynchronizer synchro;

    private MainSynchronizerState state;

    private MockSplitConditionalTask taskFast;

    private MockConditionalTask taskSlow;

    @BeforeMethod
    protected void setUp() {
        Speed speed = Speed.VERY_FAST;
        setUpFromSpeed(speed);
    }

    private void setUpFromSpeed(Speed speed) {
        int stepSize = speed.getSpeed();
        ConditionalTaskState conditionalTaskState = new ConditionalTaskState(new CyclicConditionState(1, 0), new BoundConditionState(4, 0));
        taskSlow = new MockConditionalTask(conditionalTaskState);
        ConditionalTaskState conditionalTask3DState = new ConditionalTaskState(new CyclicConditionState(1, 0), new BoundConditionState(60, 0));
        taskFast = new MockSplitConditionalTask(new SplitConditionalTaskState(conditionalTask3DState, stepSize));
        SynchronizerSlowState synchronizerSlowState = new SynchronizerSlowState(speed);
        SynchronizerFastState synchronizerFastState = new SynchronizerFastState(stepSize);
        state = new MainSynchronizerState(synchronizerSlowState, synchronizerFastState);
        synchro = new BasicMainSynchronizer(state);
        synchro.scheduleSlow(taskSlow);
        synchro.scheduleFast(taskFast);
    }

    @AfterMethod
    protected void tearDown() {
        state = null;
        synchro = null;
        taskFast = null;
        taskSlow = null;
    }

    @Test
    public void setSpeed() {
        assertThat(synchro.getSpeed()).isEqualTo(Speed.VERY_FAST);
        assertThat(synchro.getSynchronizerSlow().getSpeed()).isEqualTo(Speed.VERY_FAST);
        assertThat(synchro.getSynchronizerFast().getStepSize()).isEqualTo(Speed.VERY_FAST.getSpeed());
        synchro.setSpeed(Speed.FAST);
        assertThat(synchro.getSpeed()).isEqualTo(Speed.FAST);
        assertThat(synchro.getSynchronizerSlow().getSpeed()).isEqualTo(Speed.FAST);
        assertThat(synchro.getSynchronizerFast().getStepSize()).isEqualTo(Speed.FAST.getSpeed());
        synchro.setSpeed(Speed.NORMAL);
        assertThat(synchro.getSpeed()).isEqualTo(Speed.NORMAL);
        assertThat(synchro.getSynchronizerSlow().getSpeed()).isEqualTo(Speed.NORMAL);
        assertThat(synchro.getSynchronizerFast().getStepSize()).isEqualTo(Speed.NORMAL.getSpeed());
    }

    @Test
    public void oneStepNormal() throws InterruptedException {
        internalTestOneStep(Speed.NORMAL);
    }

    @Test
    public void oneStepFast() throws InterruptedException {
        internalTestOneStep(Speed.FAST);
    }

    @Test
    public void oneStepVeryFast() throws InterruptedException {
        internalTestOneStep(Speed.VERY_FAST);
    }

    private void internalTestOneStep(Speed speed) throws InterruptedException {
        setUpFromSpeed(speed);
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(1);
        assertThat(synchro.getNbStops()).isEqualTo(1);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(1);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(1);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(1);
        assertThat(taskFast.getNbExecuted()).isEqualTo(20 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(20);
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(2);
        assertThat(synchro.getNbStops()).isEqualTo(2);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(2);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(2);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(2);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(2);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(2);
        assertThat(taskFast.getNbExecuted()).isEqualTo(40 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(40);
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(3);
        assertThat(synchro.getNbStops()).isEqualTo(3);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(3);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(3);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(3);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(3);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(3);
        assertThat(taskFast.getNbExecuted()).isEqualTo(60 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(60);
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(4);
        assertThat(synchro.getNbStops()).isEqualTo(4);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(4);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(4);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(4);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(4);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(4);
        assertThat(taskFast.getNbExecuted()).isEqualTo(60 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(60);
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(5);
        assertThat(synchro.getNbStops()).isEqualTo(5);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(5);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(5);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(4);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(5);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(5);
        assertThat(taskFast.getNbExecuted()).isEqualTo(60 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(60);
    }

    @Test
    public void startNormal() throws InterruptedException {
        internalTestStart(Speed.NORMAL);
    }

    @Test
    public void startFast() throws InterruptedException {
        internalTestStart(Speed.FAST);
    }

    @Test
    public void startVeryFast() throws InterruptedException {
        internalTestStart(Speed.VERY_FAST);
    }

    private void internalTestStart(Speed speed) throws InterruptedException {
        setUpFromSpeed(speed);
        assertThat(synchro.isRunning()).isFalse();
        synchro.start();
        Thread.sleep(6 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isTrue();
        assertThat(synchro.getNbStarts()).isEqualTo(1);
        assertThat(synchro.getNbStops()).isEqualTo(0);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(0);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(4);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(0);
        assertThat(taskFast.getNbExecuted()).isEqualTo(60 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(60);
        synchro.stop();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(synchro.isRunning()).isFalse();
        assertThat(synchro.getNbStarts()).isEqualTo(1);
        assertThat(synchro.getNbStops()).isEqualTo(1);
        assertThat(synchro.getSynchronizerSlow().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerSlow().getNbStops()).isEqualTo(1);
        assertThat(taskSlow.getNbExecuted()).isEqualTo(4);
        assertThat(synchro.getSynchronizerFast().getNbStarts()).isEqualTo(1);
        assertThat(synchro.getSynchronizerFast().getNbStops()).isEqualTo(1);
        assertThat(taskFast.getNbExecuted()).isEqualTo(60 / synchro.getSpeed().getSpeed());
        assertThat(taskFast.getNbIncrementExecuted()).isEqualTo(60);
    }

    @Test
    public void publisher() throws Exception {
        PublisherTestHelper publisherHelper = new PublisherTestHelper();
        publisherHelper.addSubscriberTo(synchro);
        synchro.start();
        assertThat(publisherHelper.nbUpdated()).isEqualTo(1);
        assertThat(publisherHelper.getUpdateObjects().get(0)).isNull();
        publisherHelper.reset();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        synchro.stop();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(publisherHelper.nbUpdated()).isEqualTo(1);
        assertThat(publisherHelper.getUpdateObjects().get(0)).isNull();
        publisherHelper.reset();
        synchro.oneStep();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        assertThat(publisherHelper.nbUpdated()).isEqualTo(2);
        assertThat(publisherHelper.getUpdateObjects().get(0)).isNull();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void illegalStateException_onStop() {
        synchro.stop();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void illegalStateException_onAlreadyStarted() throws InterruptedException {
        synchro.start();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        synchro.start();
    }

    @Test(expectedExceptions = { IllegalStateException.class })
    public void illegalStateException_oneStep_onAlreadyStarted() throws InterruptedException {
        synchro.start();
        Thread.sleep(3 * BasicSynchronizerSlow.CYCLE_LENGTH_SLOW_MS / synchro.getSpeed().getSpeed() + 100);
        synchro.oneStep();
    }

    @Test
    public void getState() {
        assertThat(synchro.getState()).isEqualTo(state);
        assertThat(synchro.getState()).isSameAs(state);
        assertThat(synchro.getState().getSynchronizerFastState().getStepSize()).isEqualTo(Speed.VERY_FAST.getSpeed());
        assertThat(synchro.getState().getSynchronizerSlowState().getSpeed()).isEqualTo(Speed.VERY_FAST);
        synchro.setSpeed(Speed.NORMAL);
        assertThat(synchro.getState()).isEqualTo(state);
        assertThat(synchro.getState()).isSameAs(state);
        assertThat(synchro.getState().getSynchronizerFastState().getStepSize()).isEqualTo(Speed.NORMAL.getSpeed());
        assertThat(synchro.getState().getSynchronizerSlowState().getSpeed()).isEqualTo(Speed.NORMAL);
    }
}
