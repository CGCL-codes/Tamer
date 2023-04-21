package org.jpos.util;

public interface LogProducer {

    public void addListener(LogListener l);

    public void removeListener(LogListener l);

    public void removeAllListeners();
}
