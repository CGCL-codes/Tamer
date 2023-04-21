package catchnthrow;

import java.text.DecimalFormat;
import se.lth.control.realtime.AnalogIn;
import se.lth.control.realtime.AnalogOut;
import se.lth.control.realtime.DigitalIn;
import se.lth.control.realtime.DigitalOut;

/**
 * Fancy state machine
 * @author mb
 */
public class SM extends Thread {

    private static final int SMPriority = 5;

    private static final int BeamPriority = 7;

    private static final int BBPriority = 7;

    private static final int ThrowPriority = 6;

    private static final int PlotBeam = 1;

    private static final int PlotBall = 2;

    private static final int PlotNothing = 0;

    Beam beam;

    BallAndBeamRegul bb;

    Throw thr;

    private AnalogIn analogInAngle;

    private AnalogIn analogInPosition;

    private AnalogOut analogOut;

    private DigitalIn digitalIRSensor;

    private DigitalOut digitalSolenoid;

    Opcom opcom;

    private StateWrapper state;

    private int counter;

    private static final int AUTO = 0;

    private static final int STEP = 1;

    private static final boolean STOP = false;

    private static final boolean GO = true;

    private boolean doIt = true;

    StepMonitor stepMon;

    ModeMonitor modeMon;

    StartStopMonitor runMon;

    uPlotMonitor uMon;

    private double realTime = 0.0;

    private double reference = 0.0;

    private static final double refDeltaBeam = 0.05;

    private static final long samplingRateSM = 10;

    private static final double beamError = 0.03;

    private static final double ballError1 = 0.1;

    private static final double ballError2 = 0.2;

    private static final double BallPosition1 = -4;

    private static final double BallPosition2 = 0;

    private int ballSize;

    private static final int PUSH_WAIT_TIME = 100;

    DecimalFormat decFor = new DecimalFormat("0.00");

    /**
	 * Constructor
	 */
    public SM() {
        try {
            analogInPosition = new AnalogIn(2);
            analogInAngle = new AnalogIn(1);
            digitalIRSensor = new DigitalIn(0);
            digitalSolenoid = new DigitalOut(0);
            analogOut = new AnalogOut(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uMon = new uPlotMonitor();
        beam = new Beam(analogInAngle, analogOut, uMon, BeamPriority);
        bb = new BallAndBeamRegul(analogInPosition, analogInAngle, analogOut, uMon, BBPriority);
        thr = new Throw(beam, bb, ThrowPriority);
        thr.start();
        state = new StateWrapper();
        modeMon = new ModeMonitor();
        runMon = new StartStopMonitor();
        stepMon = new StepMonitor();
        modeMon.setMode(AUTO);
        runMon.setRunning(STOP);
        stepMon.setNextStep(GO);
    }

    /**
	 * Sets the Opcom GUI reference
	 * @param o the Opcom. Set by the main class.
	 */
    public void setOpcom(Opcom o) {
        opcom = o;
    }

    /**
	 * This class is a wrapper for the state classes
	 * @author mb
	 *
	 */
    class StateWrapper {

        private State m_current_state;

        public StateWrapper() {
            m_current_state = new StateInitial();
        }

        public void set_state(State s) {
            m_current_state = s;
        }

        public void nextState() {
            m_current_state.nextState(this);
        }

        public void firstExecution() {
            m_current_state.firstExecution();
        }

        public void Execute() {
            m_current_state.Execute();
        }

        public void pause() {
            m_current_state.pause();
        }

        public void resume() {
            m_current_state.resume();
        }

        public boolean condition() {
            return m_current_state.condition();
        }
    }

    /**
	 *  The interface for all the state classes
	 * @author mb
	 *
	 */
    interface State {

        void firstExecution();

        void Execute();

        void pause();

        void resume();

        boolean condition();

        void nextState(StateWrapper wrapper);
    }

    class StateInitial implements State {

        @Override
        public void Execute() {
            plotEverything(PlotNothing);
        }

        @Override
        public boolean condition() {
            return true;
        }

        @Override
        public void firstExecution() {
            reference = -1.2;
            ballSize = 0;
            try {
                digitalSolenoid.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            writeStateChangeToOpcom("StateInitial");
            if (!beam.isAlive()) {
                beam.start();
            }
            if (!bb.isAlive()) {
                bb.start();
            }
            bb.shutDown();
            beam.shutDown();
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            wrapper.set_state(new StateBeam());
        }

        @Override
        public void pause() {
            beam.shutDown();
            bb.shutDown();
        }

        @Override
        public void resume() {
            this.firstExecution();
        }
    }

    class StateBeam implements State {

        @Override
        public void Execute() {
            plotEverything(PlotBeam);
        }

        @Override
        public boolean condition() {
            return beam.hasReached(beamError);
        }

        @Override
        public void firstExecution() {
            beam.setRef(reference);
            if (!beam.isRunning()) {
                beam.restart();
            }
            reference -= refDeltaBeam;
            beam.setRef(reference);
            writeStateChangeToOpcom("StateBeam, ref: " + decFor.format(reference));
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            if (BeamInPosition()) {
                wrapper.set_state(new StateReleaseBall());
            } else {
                wrapper.set_state(new StateBeam());
            }
        }

        @Override
        public void pause() {
            beam.shutDown();
        }

        @Override
        public void resume() {
            beam.restart();
        }
    }

    class StateReleaseBall implements State {

        @Override
        public void Execute() {
            plotEverything(PlotNothing);
        }

        @Override
        public boolean condition() {
            if (counter >= PUSH_WAIT_TIME) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void firstExecution() {
            try {
                digitalSolenoid.set(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            writeStateChangeToOpcom("StateReleaseBall started");
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            try {
                digitalSolenoid.set(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            beam.shutDown();
            wrapper.set_state(new StateBallAndBeam());
        }

        @Override
        public void pause() {
            beam.shutDown();
        }

        @Override
        public void resume() {
            beam.restart();
            this.firstExecution();
        }
    }

    class StateBallAndBeam implements State {

        @Override
        public void Execute() {
            plotEverything(PlotBall);
        }

        @Override
        public boolean condition() {
            if (reference == BallPosition1) {
                return bb.hasReached(ballError1);
            } else {
                return bb.hasReached(ballError2);
            }
        }

        @Override
        public void firstExecution() {
            if (reference != BallPosition1) {
                reference = BallPosition1;
                bb.setRef(reference);
                writeStateChangeToOpcom("Ball and Beam started");
            } else {
                reference = BallPosition2;
                bb.setRef(reference);
                writeToOpcom("New reference: " + decFor.format(reference));
            }
            if (!bb.isRunning()) {
                bb.restart();
            }
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            if (reference == BallPosition2) {
                wrapper.set_state(new StateThrow());
                bb.shutDown();
            } else {
                wrapper.set_state(new StateHoldAndTrack());
            }
        }

        @Override
        public void pause() {
            bb.shutDown();
        }

        @Override
        public void resume() {
            bb.restart();
        }
    }

    class StateHoldAndTrack implements State {

        @Override
        public void Execute() {
            plotEverything(PlotBall);
        }

        @Override
        public boolean condition() {
            return (counter > 150);
        }

        @Override
        public void firstExecution() {
            writeStateChangeToOpcom("Trackin started");
            bb.trackBall(true);
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            bb.trackBall(false);
            ballSize = bb.ballSize();
            writeImportantToOpcom("Detected Ballsize: " + ballSize);
            if (ballSize == BallAndBeamRegul.unknownBall) {
                wrapper.set_state(new StateHoldAndTrack());
                writeWarningToOpcom("Unknown Ball, trying again");
            } else {
                wrapper.set_state(new StateBallAndBeam());
            }
        }

        @Override
        public void pause() {
            bb.shutDown();
        }

        @Override
        public void resume() {
            bb.restart();
        }
    }

    class StateThrow implements State {

        @Override
        public void Execute() {
            plotEverything(thr.getPlotter());
        }

        @Override
        public boolean condition() {
            return thr.done.isDone();
        }

        @Override
        public void firstExecution() {
            thr.setBallSize(ballSize);
            thr.restart();
            writeStateChangeToOpcom("StateThrow started");
        }

        @Override
        public void nextState(StateWrapper wrapper) {
            wrapper.set_state(new StateInitial());
            thr.stopThrow();
        }

        @Override
        public void pause() {
            thr.stopThrow();
        }

        @Override
        public void resume() {
            thr.restart();
        }
    }

    /**
	 * Monitor for the mode variable
	 */
    class ModeMonitor {

        private int mode;

        public synchronized int getMode() {
            return mode;
        }

        public synchronized void setMode(int mode) {
            this.mode = mode;
        }
    }

    /**
	 * Monitor for stopping/(re)starting the state machine
	 */
    class StartStopMonitor {

        private boolean running;

        public synchronized boolean isRunning() {
            return running;
        }

        public synchronized void setRunning(boolean running) {
            this.running = running;
        }
    }

    /**
	 * Monitor for the nextStep-Action
	 */
    class StepMonitor {

        private boolean nextStep;

        public synchronized boolean isNextStep() {
            return nextStep;
        }

        public synchronized void setNextStep(boolean nextStep) {
            this.nextStep = nextStep;
        }
    }

    /**
	 * Monitor for plotting u
	 */
    class uPlotMonitor {

        private double u = 0.0;

        public synchronized double getU() {
            return u;
        }

        public synchronized void setU(double u) {
            this.u = u;
        }
    }

    /**
	 * Holds/stops the state machine (No reset!)
	 */
    public void SM_pause() {
        state.pause();
        runMon.setRunning(STOP);
    }

    /**
	 * Resets the state machine to the initial state
	 */
    public void SM_reset() {
        state.set_state(new StateInitial());
    }

    /**
	 * Starts/resumes the state machine. Not to confuse with start();!
	 */
    public void SM_start() {
        state.resume();
        runMon.setRunning(GO);
    }

    /**
	 * Executes the next step if in step mode
	 */
    public void nextStep() {
        stepMon.setNextStep(GO);
    }

    /**
	 * Sets the state machine into auto-mode
	 */
    public void setAutoMode() {
        modeMon.setMode(AUTO);
        stepMon.setNextStep(GO);
    }

    /**
	 * Sets the state machine into step-mode
	 */
    public void setStepMode() {
        modeMon.setMode(STEP);
        stepMon.setNextStep(STOP);
    }

    /**
	 * Shuts the state machine down
	 */
    public void shutDown() {
        state.pause();
        bb.shutDown();
        beam.shutDown();
        thr.stopThrow();
        runMon.setRunning(STOP);
        doIt = false;
    }

    /**
	 * Write a message to the GUI
	 * @param message
	 */
    private void writeToOpcom(String message) {
        opcom.setOutput(message);
    }

    private void writeStateChangeToOpcom(String text) {
        opcom.setOutput(text, "#008000");
    }

    private void writeWarningToOpcom(String text) {
        opcom.setOutput(text, "#800000");
    }

    private void writeImportantToOpcom(String text) {
        opcom.setOutput(text, "#EFD900");
    }

    /**
	 * Plot in the Opcom
	 * @param plot 0: no ref/u, 1: Beam ref/u, 2:Ball ref/u 
	 */
    private void plotEverything(int plot) {
        double angle;
        double ballpos;
        PlotData angleData;
        PlotData ballData;
        try {
            angle = analogInAngle.get();
            ballpos = analogInPosition.get();
        } catch (Exception e) {
            e.printStackTrace();
            ballpos = 0.0;
            angle = 0.0;
        }
        switch(plot) {
            case PlotBall:
                angleData = new PlotData(realTime, 0.0, angle, 0);
                ballData = new PlotData(realTime, reference, ballpos, uMon.getU());
                break;
            case PlotBeam:
                angleData = new PlotData(realTime, reference, angle, uMon.getU());
                ballData = new PlotData(realTime, 0.0, ballpos, 0);
                break;
            default:
                angleData = new PlotData(realTime, 0.0, angle, 0);
                ballData = new PlotData(realTime, 0.0, ballpos, 0);
                break;
        }
        opcom.putAngleDataPoint(angleData);
        opcom.putBallDataPoint(ballData);
        realTime += ((double) samplingRateSM) / 1000.0;
    }

    /**
	 * Check if the Beam is in position
	 */
    private boolean BeamInPosition() {
        try {
            return !digitalIRSensor.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * Run method
	 */
    public void run() {
        setPriority(SMPriority);
        setName("StateMachine");
        long t = System.currentTimeMillis();
        long duration;
        counter = 0;
        while (doIt) {
            if (runMon.isRunning()) {
                counter++;
                if (counter == 1) {
                    state.firstExecution();
                } else {
                    state.Execute();
                }
                if (state.condition() && stepMon.isNextStep()) {
                    counter = 0;
                    state.nextState();
                    if (modeMon.getMode() == STEP) {
                        stepMon.setNextStep(STOP);
                    }
                }
            }
            t += samplingRateSM;
            duration = t - System.currentTimeMillis();
            if (duration > 0) {
                try {
                    sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
