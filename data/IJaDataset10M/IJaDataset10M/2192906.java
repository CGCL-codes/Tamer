package net.sf.asyncobjects.io;

import net.sf.asyncobjects.Promise;

/**
 * A batched data interface. This interface contains operations that are common
 * to {@link BinaryData} and {@link TextData} classes. It is used to provide a
 * common IO code.
 * 
 * @author const
 * @param <D>
 *            an actual type
 * 
 */
public interface BatchedData<D extends BatchedData<D>> extends Comparable<D> {

    /**
	 * Concatenage two items
	 * 
	 * @param data
	 *            a second element
	 * @return this data concatenatd with provided data.
	 */
    D concat(D data);

    /**
	 * Get new data with first n elements dropped
	 * 
	 * @param n
	 *            cut point
	 * @return data after position
	 */
    D drop(int n);

    /**
	 * Get data with first n elements
	 * 
	 * @param n
	 *            amount to cut from beginning of the data
	 * @return data with first n bytes of the data
	 */
    D head(int n);

    /**
	 * Get subrange of data
	 * 
	 * @param start
	 *            a start posiion
	 * @param end
	 *            a limit
	 * @return a data that represents subrange
	 */
    D subrange(int start, int end);

    /**
	 * @return true if data is empty
	 */
    boolean isEmpty();

    /**
	 * @return length of data
	 */
    int length();

    /**
	 * Return a promise that wraps this value
	 * 
	 * @return a promise
	 */
    Promise<D> promise();
}
