package com.simconomy.twitter.script.commons.client.exceptions;

import java.io.Serializable;

public class TwitterProcesException extends TwitterException implements Serializable {

    public TwitterProcesException(String message) {
        super(message);
    }

    public TwitterProcesException() {
        super();
    }
}
