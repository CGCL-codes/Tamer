package org.openremote.controller.input;

import org.jboss.logging.Logger;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.FromContext;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.openremote.controller.core.Bootstrap;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Mock infrared receiver for incoming IR codes (from native layer) to Java runtime via socket
 * interface
 *
 * @author <a href = "mailto:juha@juhalindfors.com">Juha Lindfors</a>
 */
public class InfraredReceiver {

    /**
   * Default port this protocol handler binds to listen to incoming infrared commands: {@value}
   */
    public static final int DEFAULT_PORT = 7777;

    private static final Logger log = Logger.getLogger(Bootstrap.ROOT_LOG_CATEGORY + ".INFRARED RECEIVER");

    private int serverPort = DEFAULT_PORT;

    private boolean running = true;

    private KernelControllerContext serviceCtx;

    private int commandLength = 2;

    private Map<String, String> infraredToCommandMessageMap = new HashMap<String, String>();

    @Inject(fromContext = FromContext.CONTEXT)
    public void setServiceContext(KernelControllerContext ctx) {
        this.serviceCtx = ctx;
    }

    public void start() {
        log.info("Infrared Receiver Starting...");
        Thread thread = new Thread(new Runnable() {

            public void run() {
                ServerSocket serverSocket = null;
                try {
                    serverSocket = new ServerSocket(getPort());
                    while (running) {
                        final Socket socket = serverSocket.accept();
                        Thread socketThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    BufferedInputStream input = new BufferedInputStream(socket.getInputStream());
                                    byte[] irCommand = new byte[commandLength];
                                    int bytesRead = input.read(irCommand, 0, commandLength);
                                    if (bytesRead == -1) {
                                        return;
                                    }
                                    if (bytesRead != commandLength) {
                                        log.error("Unexpected IR command size. Was expecting " + commandLength * 8 + " bits.");
                                    }
                                    String controlMessage = createMessage(irCommand);
                                    try {
                                        serviceCtx.getKernel().getBus().invoke("ControlProtocol/Router", "route", new Object[] { controlMessage }, new String[] { String.class.getName() });
                                    } catch (Throwable t) {
                                        log.error("Error routing the command: " + t, t);
                                    }
                                } catch (IOException e) {
                                    log.error("Error reading the incoming command: " + e, e);
                                } finally {
                                    try {
                                        socket.close();
                                    } catch (IOException e) {
                                        log.debug("Failed to close socket: " + e, e);
                                    }
                                }
                            }
                        });
                        socketThread.start();
                    }
                } catch (IOException e) {
                    log.error("Error creating infrared receiver socket: " + e, e);
                } finally {
                    try {
                        if (serverSocket != null) serverSocket.close();
                    } catch (IOException ignored) {
                    }
                }
            }
        });
        thread.start();
        log.info("Infrared receiver started at port : " + getPort());
    }

    public void setPort(int port) {
        this.serverPort = port;
    }

    public int getPort() {
        return serverPort;
    }

    public int getInfraredCommandLength() {
        return commandLength;
    }

    public void setInfraredCommandLength(int sizeInBytes) {
        commandLength = sizeInBytes;
    }

    public Map<String, String> getInfraredCommandMap() {
        return infraredToCommandMessageMap;
    }

    public void setInfraredCommandMap(Map<String, String> map) {
        this.infraredToCommandMessageMap = map;
    }

    private String createMessage(byte[] infraredCommand) {
        StringBuilder builder = new StringBuilder(8);
        for (byte commandByte : infraredCommand) {
            int commandBits = commandByte & 0xFF;
            builder.append(Integer.toHexString(commandBits).toUpperCase());
        }
        log.info("Looking for message for IR command 0x" + builder.toString());
        String command = infraredToCommandMessageMap.get(builder.toString());
        log.info("Found message: " + command);
        return command;
    }
}
