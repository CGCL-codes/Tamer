package org.asyncj.examples;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.asyncj.Async;
import org.asyncj.handlers.BufferedHandler;
import org.asyncj.handlers.Handler;
import org.asyncj.handlers.ServerHandler;

public class TestServer extends ServerHandler {

    public static void main(String[] args) throws IOException {
        Async router = Async.startup();
        log.info("started on 8089");
        router.register(new TestServer(8089));
        router.run();
        log.info("fin");
    }

    public TestServer(int port) throws IOException {
        super(port);
    }

    public Handler getServiceHandler(Async router, SelectableChannel servingChannel) throws IOException {
        return new BufferedHandler(servingChannel) {

            public void write(byte[] bytes) {
                log.info("WRITING:" + new String(bytes));
                super.write(bytes);
            }

            public byte[] read(byte[] bytes) {
                log.info("READ:" + new String(bytes));
                write(bytes);
                return bytes;
            }
        };
    }

    static final Log log = LogFactory.getLog(TestServer.class);
}
