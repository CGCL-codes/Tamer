package jhomenet.commons.utils;

/**
 * TODO: Class description.
 * 
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public interface DataQueue {

    /**
     * Push data into the queue.
     * 
     * @param data
     */
    public void enQueue(String data);

    /**
     * Pop data from the queue.
     * 
     * @return
     */
    public String deQueue();

    /**
     * Get all the data from the queue.
     * 
     * @return
     */
    public Object[] getAll();
}
