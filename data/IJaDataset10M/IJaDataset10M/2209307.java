package homura.hde.scenegraph.scenegraph;

/**
 * HDE_Scene - LWJGLContext
 * @version 1.0
 * @author <a href="mailto:Dessimat0r@ntlworld.com">Chris Dennett</a>
 */
public class LWJGLContext {

    public static final int MAX_DELAYED_RUNNABLES = 5;

    private DelayedRunnable[] delayedRunnables = new DelayedRunnable[MAX_DELAYED_RUNNABLES];

    private int noOfDelayedRunnables = 0;

    /**
	 * 
	 */
    public LWJGLContext() {
    }

    public boolean addDelayedRunnable(DelayedRunnable dr) {
        assert (dr != null);
        if (noOfDelayedRunnables > MAX_DELAYED_RUNNABLES) return false;
        delayedRunnables[noOfDelayedRunnables++] = dr;
        return true;
    }
}
