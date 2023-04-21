package com.tikal.util;

/**
 * @author <a href="mailto:adi@tikalk.com">Adi Baron</a>
 */
public interface ClassLoadersIterationPolicy {

    /**
	 * Indicates whether or not there is a next class-loader to iterate.
	 * @return true if there's another class-loader to iterate; false otherwise.
	 */
    boolean hasNext();

    /**
	 * The next class-loader in the iteration.
	 * @return the next class-loader in the iteration.
	 * @exception java.util.NoSuchElementException for no more elements.
	 */
    ClassLoader next();

    /**
	 * Resets the iterator to start from the beginning.
	 */
    void reset();
}
