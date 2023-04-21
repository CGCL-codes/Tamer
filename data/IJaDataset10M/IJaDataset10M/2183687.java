package org.nakedobjects.distribution.duplex;

import java.io.Serializable;

final class Response {

    private Object response;

    private boolean hasResponse;

    synchronized Serializable awaitResponse() {
        while (!hasResponse) {
            try {
                wait();
            } catch (InterruptedException ignore) {
            }
        }
        hasResponse = false;
        notify();
        return (Serializable) response;
    }

    synchronized void setResponse(Object object) {
        while (hasResponse) {
            try {
                wait();
            } catch (InterruptedException ignore) {
            }
        }
        response = object;
        hasResponse = true;
        notify();
    }
}
