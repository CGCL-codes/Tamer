package com.pl.itsense.ftsm.common;

public interface ICumulator extends IParametrizable {

    /**
	 * 
	 * @return
	 */
    int getHistoryLength();

    /**
	 * 
	 * @return
	 */
    boolean isFull();

    /**
	 * 
	 * @param value
	 * @param timeStamp
	 */
    void add(ISignal signal);

    void append(ISignal signal);

    /**
	 * it may return null if there is no enough samples yet.
	 * @return
	 */
    Double getValue(IIndicator indicator);

    /**
	 * 
	 */
    void clear();
}
