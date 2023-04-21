package com.jetigy.tach.generation;

public interface Metric {

    /**
	 * Dispatches the metric
	 */
    public void dispatch();

    /**
	 * Returns the name of this metric
	 * 
	 * @return String name
	 */
    public String getName();

    /**
	 * Gets the start time of the metric
	 * @return long
	 */
    public long getStartTime();

    /**
	 * Gets the stop time of the metric
	 * @return long
	 */
    public long getStopTime();

    /**
	 * Returns the weight of this metric
	 * 
	 * @return int weight
	 */
    public int getWeight();

    /**
	 * Resets the metric for reuse
	 */
    public void reset();

    /**
	 * Sets the weight for this metric
	 * 
	 * @param weight
	 */
    public void setWeight(int weight);

    /**
	 * Starts the Metric
	 */
    public void start();

    /**
	 * Stops the Metric
	 */
    public void stop();
}
