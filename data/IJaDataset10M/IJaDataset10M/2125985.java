package org.kineticsystem.commons.threads;

/**
 * <p>Simple numeric semaphor without thread queue.</p>
 * <p>To avoid thread starvation use a semaphore with queue.</p>
 * @author Giovanni Remigi
 * $Revision: 24 $
 */
public class NumericSemaphore {

    /**
     * Maximum number of thread which can simultaneously aquire the semaphore.
     */
    private int count;

    /**
     * Main constructor.
     * @param count The maxumum number of thread which can simultaneously aquire
     *     the semaphore.
     */
    public NumericSemaphore(int count) {
        this.count = count;
    }

    /** Acquire the semaphore: it corresponds to a <i>wait</i> operation. */
    public synchronized void aquire() {
        while (count == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        count--;
    }

    /** Release the semaphore: it corresponds to a <i>signal</i> operation. */
    public synchronized void release() {
        count++;
        notifyAll();
    }
}
