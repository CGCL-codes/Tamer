package com.bbn.wild.server.component;

public interface SynchronousRequestor extends Requestor {

    void registerResponder(SynchronousResponder srs);
}
