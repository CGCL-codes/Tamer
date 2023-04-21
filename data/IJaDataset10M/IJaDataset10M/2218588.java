package ru.adv.http;

import java.io.Serializable;

/**
 * The interface that all query values must adhere to.
 * @version $Revision: 1.8 $
 */
public abstract class QueryValue implements Serializable {

    private static final long serialVersionUID = 206950147942738L;

    private boolean _real;

    public QueryValue(boolean real) {
        _real = real;
    }

    /**
	 * Value is empty.
	 */
    public abstract boolean isEmpty();

    /**
	 * true for upload file values.
	 */
    public abstract boolean isFile();

    /**
	 * get string representation.
	 */
    public abstract String toString();

    public boolean isReal() {
        return _real;
    }

    public abstract QueryValue copy(boolean asReal);

    public abstract Object getValue();
}
