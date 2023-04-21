package org.apache.http.contrib.logging;

import org.apache.http.impl.nio.DefaultServerIOEventDispatch;
import org.apache.http.nio.NHttpServerIOTarget;
import org.apache.http.nio.NHttpServiceHandler;
import org.apache.http.nio.reactor.IOSession;
import org.apache.http.params.HttpParams;

public class LoggingServerIOEventDispatch extends DefaultServerIOEventDispatch {

    public LoggingServerIOEventDispatch(final NHttpServiceHandler handler, final HttpParams params) {
        super(new LoggingNHttpServiceHandler(handler), params);
    }

    @Override
    protected NHttpServerIOTarget createConnection(final IOSession session) {
        return new LoggingNHttpServerConnection(new LoggingIOSession(session, "server"), createHttpRequestFactory(), this.allocator, this.params);
    }
}
