package com.vangent.hieos.services.atna.arr.serviceimpl;

import com.vangent.hieos.xutil.socket.ServerProperties;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author Adeola Odunlami
 */
public class ATNAServer {

    private static final Logger log = Logger.getLogger(ATNAServer.class);

    private static final String POOL_SIZE = "pool_size";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException, Exception {
        if (args.length < 2) {
            ATNAServer.printUsage();
            System.exit(1);
        }
        if (!args[0].equalsIgnoreCase("-p")) {
            ATNAServer.printUsage();
            System.exit(1);
        }
        String propertyFilename = args[1];
        ServerProperties props = new ServerProperties(propertyFilename);
        int poolSize = props.getIntegerProperty(POOL_SIZE);
        log.info("Initializing Server Environment");
        TCPServer tcpServer = new TCPServer(poolSize, props);
        UDPServer udpServer = new UDPServer(poolSize, props);
        udpServer.start();
        log.info("Started UDP Server");
        tcpServer.start();
        log.info("Started TCP Server");
        udpServer.listen();
        log.info("UDP Server Waiting");
        tcpServer.listen();
        log.info("TCP Server Waiting");
    }

    /**
     *
     */
    private static void printUsage() {
        System.out.println("Usage: ATNAServer -p <properties_file>");
    }
}
