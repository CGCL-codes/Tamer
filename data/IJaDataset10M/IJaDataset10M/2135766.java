package radius.server;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class DispatchStrategy {

    private boolean singelThread;

    private boolean autoConfig;

    private int minThread;

    private int maxThread;

    private int increment;

    /**
	 * @return Returns the autoConfig.
	 */
    public boolean isAutoConfig() {
        return autoConfig;
    }

    /**
	 * @param autoConfig
	 *            The autoConfig to set.
	 */
    public void setAutoConfig(boolean autoConfig) {
        this.autoConfig = autoConfig;
    }

    /**
	 * @return Returns the increment.
	 */
    public int getIncrement() {
        return increment;
    }

    /**
	 * @param increment
	 *            The increment to set.
	 */
    public void setIncrement(int increment) {
        this.increment = increment;
    }

    /**
	 * @return Returns the maxThread.
	 */
    public int getMaxThread() {
        return maxThread;
    }

    /**
	 * @param maxThread
	 *            The maxThread to set.
	 */
    public void setMaxThread(int maxThread) {
        this.maxThread = maxThread;
    }

    /**
	 * @return Returns the minThread.
	 */
    public int getMinThread() {
        return minThread;
    }

    /**
	 * @param minThread
	 *            The minThread to set.
	 */
    public void setMinThread(int minThread) {
        this.minThread = minThread;
    }

    /**
	 * @return Returns the singelThread.
	 */
    public boolean isSingelThread() {
        return singelThread;
    }

    /**
	 * @param singelThread
	 *            The singelThread to set.
	 */
    public void setSingelThread(boolean singelThread) {
        this.singelThread = singelThread;
    }
}
