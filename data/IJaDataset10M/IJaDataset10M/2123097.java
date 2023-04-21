package jsomap.algorithm;

import jsomap.map.Map;
import jsomap.data.Data;
import jsomap.algorithm.parameters.Parameters;

/** A skeletal implementation of the <CODE>ControlAlgorithm</CODE> interface.
 *
 * @author Zach Cox <zcox@iastate.edu>
 * @version $Revision: 1.2 $
 * @since JDK1.3
 */
public abstract class AbstractControlAlgorithm extends AbstractEventAlgorithm implements ControlAlgorithm {

    private volatile long _delay = 0;

    private volatile boolean _paused = false;

    private volatile boolean _stepping = false;

    /** Creates a new <CODE>AbstractControlAlgorithm</CODE> using the specified data, map, and parameters.
	 *
	 * @param data the data set.
	 * @param map the map.
	 * @param parameters the parameters.
	 * @throws NullPointerException if any of the parameters is <CODE>null</CODE>.
	 */
    public AbstractControlAlgorithm(Data data, Map map, Parameters parameters) {
        super(data, map, parameters);
    }

    /** Delays the current thread by the specified number of milliseconds.
	 *
	 * @param millis the number of milliseconds to delay.
	 */
    protected void doDelay(long millis) {
        try {
            Thread.currentThread().sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    /** Delays the current thread for 20 milliseconds.
	 *
	 */
    protected void doPause() {
        doDelay(20);
    }

    /** Returns the delay in milliseconds between iterations of this algorithm (optional operation).
	 *
	 * @return the delay in milliseconds between iterations of this algorithm.
	 * @see jsomap.algorithm.ControlAlgorithm#setDelay
	 */
    public long getDelay() {
        return _delay;
    }

    /** Returns <CODE>true</CODE> if this algorithm is currently paused.  Should
	 * generally return <CODE>true</CODE> after the pause() method is called, and
	 * <CODE>false</CODE> after the resume method is called.
	 *
	 * @return <CODE>true</CODE> if this algorithm is paused.
	 */
    public boolean isPaused() {
        return _paused;
    }

    /** Returns <CODE>true</CODE> if this algorithm is currently stepping through a single
	 * iteration.  When the algorithm is paused, calling <CODE>step</CODE> should cause this
	 * algorithm to return <CODE>true</CODE> while the iteration is being carried out.
	 * When the iteration is finished, the algorithm should call <CODE>pause</CODE> again, and this
	 * method should then return <CODE>false</CODE>.
	 *
	 * @return <CODE>true</CODE> if this algorithm is currently stepping.
	 */
    public boolean isStepping() {
        return _stepping;
    }

    /** Pauses the execution of this algorithm (optional operation).  Has no effect if
	 * the algorithm is already paused.  Should be implemented along with the resume()
	 * method.
	 * @see jsomap.algorithm.ControlAlgorithm#resume
	 */
    public void pause() {
        _paused = true;
        _stepping = false;
    }

    /** Resumes the execution of this algorithm (optional operation).  Has no effect if
	 * the algorithm is not paused.  Should be implemented along with the pause()
	 * method.
	 * @see jsomap.algorithm.ControlAlgorithm#pause
	 */
    public void resume() {
        _paused = false;
        _stepping = false;
    }

    /** Sets the delay in milliseconds between iterations of this algorithm (optional operation).
	 *
	 * @throws IllegalArgumentException if delay < 0.
	 * @param delay the delay in milliseconds between iterations of this algorithm.
	 * @see jsomap.algorithm.ControlAlgorithm#getDelay
	 */
    public void setDelay(long delay) {
        if (delay < 0) {
            throw new IllegalArgumentException("delay was " + delay + ", must be greater than 0");
        }
        _delay = delay;
    }

    /** Performs the next step, or iteration, of this algorithm (optional operation).
	 * Generally will have no effect if the algorithm is currently running, but this is
	 * an implementation-specific behavior.
	 *
	 */
    public void step() {
        if (isPaused()) {
            _stepping = true;
        }
    }
}
