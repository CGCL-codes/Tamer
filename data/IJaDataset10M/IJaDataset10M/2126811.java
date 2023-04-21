package com.astrientlabs.util;

public interface ProgressListener {

    public void update(Object who, long position);

    public void completed(Object who);
}
