package pitt.search.semanticvectors;

import java.lang.Exception;

public class ZeroVectorException extends Exception {

    public ZeroVectorException() {
        super();
    }

    public ZeroVectorException(String message) {
        super(message);
    }
}
