package org.gvsig.exceptions;

import java.util.Iterator;

class BaseExceptionIterator implements Iterator {

    Exception exception;

    BaseExceptionIterator(BaseException exception) {
        this.exception = exception;
    }

    /** 
	 *  @return true if the iteration has more elements.
	 */
    public boolean hasNext() {
        return this.exception != null;
    }

    /** 
	 *  @return The next element in the iteration.
	 */
    public Object next() {
        Exception exception;
        exception = this.exception;
        this.exception = (Exception) exception.getCause();
        return exception;
    }

    /** 
	 *  @throws "UnsupportedOperationException" because
	 *  the remove operation will not be supported
	 *  by this Iterator.
	 */
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
