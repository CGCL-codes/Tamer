package org.hydracache.cluster;

public interface Node {

    String toString();

    void send(Object msg);
}
