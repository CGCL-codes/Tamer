package com.thoughtworks.turtlemock.internal;

public abstract class AbstractRollbackableInvokeLog implements RollbackableInvokeLog {

    public void twice() {
        times(2);
    }
}
