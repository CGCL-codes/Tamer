package imtek.optsuite.acquisition.routine.sequence;

import imtek.optsuite.acquisition.routine.MeasurementRoutineStep;

/**
 * @author Alexander Bieber <fleque@users.sourceforge.net>
 *
 */
public interface MeasurementRoutineTransitionStep extends MeasurementRoutineStep {

    /**
	 * Called before the first and between all other runs of
	 * the MeasurementRoutineSteps of the MeasurementRoutineSequence
	 * this transition step is assigned to.
	 * Return <code>true</code> in order to enable an new iteration
	 * and execution of the MeasurementRoutineSteps, <code>false</code>
	 * if you want the sequence to stop.
	 */
    public boolean doTransition();

    /**
	 * Called before a sequence with this transition step starts
	 * on the given thread. Allows the transitions step do do
	 * initialisation for the sequence.
	 */
    public void sequenceStart(MeasurementRoutineSequence sequence, Thread thread);

    /**
	 * Called when the sequence is done or (cancelled/failed) on the given 
	 * thread.
	 */
    public void sequenceDone(MeasurementRoutineSequence sequence, Thread thread);

    public String getStateDescription();
}
