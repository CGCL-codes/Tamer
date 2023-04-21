package com.google.clearsilver.jsilver.output;

/**
 * Implementation of OutputBufferProvider that creates a new StringBuilder
 */
public class InstanceOutputBufferProvider implements OutputBufferProvider {

    private final int bufferSize;

    public InstanceOutputBufferProvider(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public Appendable get() {
        return new StringBuilder(bufferSize);
    }

    @Override
    public void release(Appendable buffer) {
    }
}
