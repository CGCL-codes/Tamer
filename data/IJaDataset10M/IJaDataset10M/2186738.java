package fr.lig.sigma.astral.common.event;

import fr.lig.sigma.astral.common.Batch;
import fr.lig.sigma.astral.common.event.EventScheduler;
import java.io.Serializable;

/**
 * Descriptor of an independent task. This object is given by the EventScheduler service when a new
 * independent event is declared. This object can be serialized and be send through network, thanks to
 * the processorId.
 * @author Loic Petit
 */
public class IndependentTask implements Serializable, Comparable {

    private Batch batch;

    private int processorId;

    private long pushValue;

    /**
     * Creates the task description
     * @param batch the batch id
     * @param processorId processorId
     * @param scheduler scheduler 
     */
    public IndependentTask(Batch batch, int processorId, EventScheduler scheduler) {
        this.batch = batch;
        this.processorId = processorId;
    }

    /**
     * Get the id of the independent processor
     * @return the id
     */
    public int getProcessorId() {
        return processorId;
    }

    /**
     * Set the push value based on the scheduler for push transmission reasons
     * @param scheduler scheduler
     */
    public void updatePush(EventScheduler scheduler) {
    }

    /**
     * Get the push value
     * @return the value
     */
    public long getPush() {
        return pushValue;
    }

    /**
     * Verify the equality over timestamp and processorId
     * @param o An independant task
     * @return true if timestamp = o.timestamp & processorId = o.processorId 
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndependentTask)) return false;
        IndependentTask that = (IndependentTask) o;
        return processorId == that.processorId && !(batch != null ? !batch.equals(that.batch) : that.batch != null);
    }

    @Override
    public int hashCode() {
        int result = batch != null ? batch.hashCode() : 0;
        result = 31 * result + processorId;
        return result;
    }

    /**
     * Compares this description with another by timestamp
     * @param o An independant task
     * @return timestamp - o.timestamp
     */
    public int compareTo(Object o) {
        return batch.compareTo(((IndependentTask) o).getBatch());
    }

    @Override
    public String toString() {
        return "IndependentTask(" + batch + ", " + processorId + ", " + pushValue + ")";
    }

    /**
     * Get the batch id
     * @return the i
     */
    public Batch getBatch() {
        return batch;
    }
}
