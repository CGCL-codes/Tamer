package org.hypergraphdb.app.dataflow;

public interface ChannelListener {

    <T> boolean beforePut(Channel<T> ch, T datum);

    <T> void afterPut(Channel<T> ch, T datum);
}
