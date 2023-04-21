package com.astrientlabs.net;

public interface Cancellable {

    public boolean shouldCancel();

    public void cancel();
}
