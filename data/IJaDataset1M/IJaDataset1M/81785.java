package googlechartwrapper.data;

/**
 * Interface for charts which (usually) have only one data set, which can
 * be scaled.
 * @author steffan
 * @author martin
 *
 */
public interface ISingleDataScaleable {

    /**
	 * Sets the data scaling set for the data set.
	 * @param ds data scaling set
	 */
    public void setDataScaling(DataScalingSet ds);

    /**
	 * Returns the data scaling set.
	 * @return set
	 */
    public DataScalingSet getDataScaling();

    /**
	 * Removes the data scaling set.
	 */
    public void removeDataScaling();
}
