package com.myeye.gameserver.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import com.myeye.gameserver.core.domain.GameMessage;
import com.myeye.gameserver.core.service.GameMessageService;

/**
 * Handles the game messages requests.
 * 
 * @author Marcello de Sales (marcello.desales@gmail.com).
 *
 */
public class GameMessageRequestHandler implements Runnable {

    /**
     * The client socket connection.
     */
    private Socket clientSocket;

    /**
     * The queue responsible for logging in the message.
     */
    private BlockingQueue<GameMessage> loggerQueue;

    public GameMessageRequestHandler(Socket clientSocket, BlockingQueue<GameMessage> loggerQueue) {
        this.loggerQueue = loggerQueue;
        this.clientSocket = clientSocket;
    }

    public static String makeThreadName(Socket client) {
        String clientIp = client.getRemoteSocketAddress().toString();
        return "Client [" + clientIp + "]";
    }

    @Override
    public void run() {
        final String clientName = makeThreadName(clientSocket);
        Thread.currentThread().setName(clientName);
        long initial = System.currentTimeMillis();
        System.out.println("Handling client request " + clientName);
        byte[] bynaryMessage = receiveBinaryMessage(clientSocket);
        GameMessage incomingMessage = GameMessageService.unmarshall(bynaryMessage);
        if (incomingMessage != null) {
            this.loggerQueue.offer(incomingMessage);
        }
        long end = System.currentTimeMillis();
        System.out.println("Logged request of " + clientName + " in " + (end - initial) + " ms");
    }

    /**
     * Hands the client connection, handling the Request and Response.
     * 
     * @param clientSocket
     * @throws HttpErrorException
     * @throws HttpRequestInterpreterException
     */
    private static byte[] receiveBinaryMessage(Socket clientSocket) {
        InputStream is;
        byte[] binaryMessage = null;
        try {
            is = clientSocket.getInputStream();
            ByteBuffer binaryMessageBuffer = ByteBuffer.allocate(1048576);
            byte[] byteBufferRead = new byte[1024];
            @SuppressWarnings("unused") int byteCount = -1;
            while ((byteCount = is.read(byteBufferRead)) >= 0) {
                binaryMessageBuffer.put(byteBufferRead);
            }
            if (binaryMessageBuffer.capacity() < 1) {
                return null;
            }
            binaryMessage = binaryMessageBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return binaryMessage;
    }
}
