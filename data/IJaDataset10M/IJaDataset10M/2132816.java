package org.jtools.io.serial;

import java.io.IOException;
import org.jpattern.io.Output;

public class DelegationOutput implements Output {

    private final Output delegate;

    public DelegationOutput(Output delegate) {
        this.delegate = delegate;
    }

    public void set(boolean value) throws IOException {
        delegate.set(value);
    }

    public void set(byte value) throws IOException {
        delegate.set(value);
    }

    public void set(byte[] dest, boolean fixSize) throws IOException {
        delegate.set(dest, fixSize);
    }

    public void set(char value) throws IOException {
        delegate.set(value);
    }

    public void set(double value) throws IOException {
        delegate.set(value);
    }

    public void set(float value) throws IOException {
        delegate.set(value);
    }

    public void set(int value) throws IOException {
        delegate.set(value);
    }

    public void set(long value) throws IOException {
        delegate.set(value);
    }

    public void set(short value) throws IOException {
        delegate.set(value);
    }

    public void set(String value) throws IOException {
        delegate.set(value);
    }

    public void flush() throws IOException {
        delegate.flush();
    }

    public void close() throws IOException {
        delegate.close();
    }
}
