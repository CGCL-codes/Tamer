package org.creativor.rayson.demo.main;

import java.io.IOException;
import org.creativor.rayson.demo.server.DemoServiceImpl;
import org.creativor.rayson.demo.server.DemoTransferService;
import org.creativor.rayson.exception.IllegalServiceException;
import org.creativor.rayson.server.RpcServer;
import org.creativor.rayson.transport.api.ServiceAlreadyExistedException;
import org.creativor.rayson.transport.server.ServerConfig;

/**
 * 
 * @author Nick Zhang
 */
public class DemoServer {

    public static void main(String[] args) {
        int portNumber = ServerConfig.DEFAULT_PORT_NUMBER;
        try {
            portNumber = Integer.parseInt(args[0]);
        } catch (Exception e) {
        }
        try {
            RpcServer rpcServer = new RpcServer(portNumber);
            rpcServer.start();
            rpcServer.registerService("demo", "Demo service", new DemoServiceImpl());
            rpcServer.registerService(new DemoTransferService());
        } catch (ServiceAlreadyExistedException e) {
            e.printStackTrace();
        } catch (IllegalServiceException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
