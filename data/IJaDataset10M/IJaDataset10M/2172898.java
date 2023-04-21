package org.hypergraphdb.app.dataflow;

/**
 * <p>
 * An <code>OutputPort</code> connects a {@link Channel} to a {@link Processor} for writing.
 * It is written to by a <code>Processor</code> implementation.
 * </p>
 * 
 * @author muriloq
 * 
 * @param <V> The type of data going through this port.
 */
public class OutputPort<V> extends Port<V> {

    protected OutputPort() {
    }

    OutputPort(Channel<V> channel) {
        super(channel);
    }

    /**
	 * Return <code>true</code> if writing to this output was successful and
	 * <code>false</code> otherwise. An output port doesn't receive anything 
	 * if it's closed - i.e. this method will return <code>false</code> if the
	 * port is closed.
	 */
    public boolean put(V v) throws InterruptedException {
        if (!isOpen()) return false;
        channel.put(v);
        return true;
    }

    public void close() throws InterruptedException {
        synchronized (this) {
            if (!isOpen()) return;
            super.close();
        }
        channel.channelInputClosed();
    }

    public String toString() {
        return "OutputPort from " + channel;
    }
}
