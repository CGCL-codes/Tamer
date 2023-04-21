package org.jtools.protocol.server;

import java.io.IOException;

public interface Protocols {

    void handle(Job job) throws IOException;

    void addProtocol(short protocol, JobHandler handler);
}
