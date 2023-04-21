package netblend.master.event;

import netblend.master.slaves.SlaveHandler;

/**
 * An immutable event triggered when a progress changes.
 * 
 * @author Ian Thompson
 * 
 */
public class ProgressEvent {

    private SlaveHandler slave;

    private int progress;

    /**
	 * Creates a new progress event.
	 * 
	 * @param slave
	 *            the slave in question.
	 * @param progress
	 *            the new value of the progress.
	 */
    public ProgressEvent(SlaveHandler slave, int progress) {
        this.slave = slave;
        this.progress = progress;
    }

    /**
	 * Returns the slave in question.
	 * 
	 * @return the slave.
	 */
    public SlaveHandler getSlave() {
        return slave;
    }

    /**
	 * Return the value of the progress event.
	 * 
	 * @return the new progress.
	 */
    public int getProgress() {
        return progress;
    }
}
