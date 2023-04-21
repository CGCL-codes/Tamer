package org.agile.dfs.rpc.server;

import java.io.IOException;
import org.agile.dfs.rpc.endpoint.Endpointable;
import org.agile.dfs.rpc.util.MulValueLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcRunnable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RpcRunnable.class);

    private static final MulValueLocal local = MulValueLocal.newInstance();

    private RpcHandler handler;

    private Endpointable endpoint;

    public RpcRunnable(Endpointable endpoint, RpcHandler handler) {
        this.endpoint = endpoint;
        this.handler = handler;
    }

    public void run() {
        logger.info(" >>> Request from {} ", endpoint);
        while (true) {
            try {
                local.set("dfs.endpoint", endpoint);
                handler.handle(endpoint);
            } catch (IOException e) {
                if (endpoint.isClose()) {
                    logger.error("Endpoint is closed! " + endpoint);
                } else {
                    logger.error("IOException when handle " + endpoint, e);
                }
            } catch (Throwable t) {
                logger.error("Handle request error!", t);
                break;
            } finally {
                local.clear("dfs.endpoint");
            }
            if (endpoint.isClose()) {
                break;
            }
        }
        endpoint.close();
        logger.info("Close session {}", endpoint);
    }
}
