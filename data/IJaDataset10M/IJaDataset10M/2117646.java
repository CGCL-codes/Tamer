package ucalgary.ebe.ci.gestures.input.table;

import java.util.Timer;
import java.util.TimerTask;
import ucalgary.ebe.ci.gestures.input.AbstractGestureInput;
import ucalgary.ebe.ci.gestures.input.GestureInput;
import ucalgary.ebe.ci.pen.ICIPenProvider;
import ucalgary.ebe.ci.pen.events.ICIPenEvent;
import ucalgary.ebe.ci.pen.events.ICIPenListener;
import ucalgary.ebe.ci.pen.events.ICIPenMoveListener;

/**
 * this class sends only a specific id to the gesture inlut listener that events
 * will be sent the pen must not move for suspensionTime ms after pmoving down
 * to table
 * 
 * @author hkolenda
 * 
 */
public class SuspendedSinglePenInput extends AbstractGestureInput implements GestureInput {

    private class ExpiredTimer extends TimerTask {

        @Override
        public void run() {
            fireStartGesture();
            System.out.println("tmer done" + penID);
        }
    }

    private class InputHandler implements ICIPenListener, ICIPenMoveListener {

        public void PenDown(ICIPenEvent e) {
            if (e.getID() == penID) {
                startTimer();
            }
        }

        public void PenMove(ICIPenEvent e) {
            if (e.getID() == penID) {
                if (isGestureRunning()) {
                    fireCoordInput(e.getX(), e.getY());
                } else {
                    if (!isInTolerance(e)) {
                        cancelTimer();
                        fireCancelGesture();
                    }
                }
            }
        }

        public void PenUp(ICIPenEvent e) {
            if (e.getID() == penID) {
                fireStopGesture();
            }
        }
    }

    private int movedTolerance = 10;

    private ICIPenEvent oldValue = null;

    private int penID = Integer.MIN_VALUE;

    private long suspensionTime = 0;

    private Timer suspensionTimer = null;

    private TimerTask suspensionTimerTask = null;

    public SuspendedSinglePenInput(ICIPenProvider penProvider, int penID) {
        InputHandler inputHandler = new InputHandler();
        penProvider.addICIPenListener(inputHandler);
        penProvider.addICIPenMoveListener(inputHandler);
        this.penID = penID;
        suspensionTimer = new Timer();
    }

    private void cancelTimer() {
        if (suspensionTimerTask != null) {
            suspensionTimerTask.cancel();
        }
    }

    private boolean isInTolerance(ICIPenEvent newValue) {
        boolean result = false;
        if ((oldValue == null) || ((Math.abs(oldValue.getX() - newValue.getX()) <= movedTolerance) && (Math.abs(oldValue.getY() - newValue.getY()) <= movedTolerance))) {
            result = true;
        }
        oldValue = newValue;
        return result;
    }

    /**
	 * @param suspensionTime
	 *            the suspensionTime to set
	 */
    public void setSuspensionTime(long suspensionTime) {
        this.suspensionTime = suspensionTime;
    }

    private void startTimer() {
        if (suspensionTime > 0) {
            cancelTimer();
            suspensionTimerTask = new ExpiredTimer();
            suspensionTimer.schedule(suspensionTimerTask, suspensionTime);
        } else {
            fireStartGesture();
        }
    }

    @Override
    protected void fireStopGesture() {
        super.fireStopGesture();
        cancelTimer();
        oldValue = null;
    }
}
