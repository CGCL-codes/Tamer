package org.encog.engine.data;

/**
 * Specifies that a data set can be accessed in random order via an index. This
 * property is required for multi-threaded training. 
 */
public interface EngineIndexableSet extends EngineDataSet {

    /**
	 * Determine the total number of records in the set.
	 * @return The total number of records in the set.
	 */
    long getRecordCount();

    /**
	 * Read an individual record, specified by index, in random order.
	 * @param index The index to read.
	 * @param pair The pair that the record will be copied into.
	 */
    void getRecord(long index, EngineData pair);

    /**
	 * Opens an additional instance of this dataset.  
	 * @return The new instance.
	 */
    EngineIndexableSet openAdditional();
}
