package org.antdepo.input;

import EDU.oswego.cs.dl.util.concurrent.Channel;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.Takable;
import org.antdepo.utils.Net;
import org.apache.log4j.Category;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * NetworkInputHandler is a daemon server thread which handles input queries from a client, and prompts the user for
 * input on the local machine using a provided InputHandler. <br> Use the method {@link
 * #beginServer(org.apache.tools.ant.input.InputHandler)} to obtain a running server using the given input handler, and
 * listening on an available port. Use the method {@link #beginServer(org.apache.tools.ant.input.InputHandler, int)}  to
 * specify a port to listen on.
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 384 $
 */
public class NetworkInputHandler {

    public static final Category logger = Category.getInstance(NetworkInputHandler.class);

    private InputHandler proxied;

    private PooledExecutor execPool = new PooledExecutor(new LinkedQueue(), 15);

    private Map requests = Collections.synchronizedMap(new HashMap());

    private Channel requestChannel = new LinkedQueue();

    private ServerSocket serv;

    private Thread consumer;

    private Thread mythread;

    private int port;

    private int tryPort = 0;

    private volatile boolean finished;

    /**
     * Start the input handler server using the given InputHandler layer and return the server instance. Listens on any
     * available port.
     *
     * @param layer an existing InputHandler to use
     * @return the NetworkInputHandler instance
     * @throws IOException if an error occurs
     */
    public static NetworkInputHandler beginServer(InputHandler layer) throws IOException {
        NetworkInputHandler netin = new NetworkInputHandler(layer);
        netin.beginThread();
        return netin;
    }

    /**
     * Start the input handler server using the given InputHandler layer and return the server instance. Listens on the
     * given port number.
     *
     * @param layer an existing InputHandler to use
     * @param port  the TCP port number greater than or equal to 1 to indicated the local port to listen on.  Other
     *              values will choose any available port.
     * @return the NetworkInputHandler instance
     * @throws IOException if an error occurs
     */
    public static NetworkInputHandler beginServer(InputHandler layer, int port) throws IOException {
        NetworkInputHandler netin = new NetworkInputHandler(layer, port);
        netin.beginThread();
        return netin;
    }

    /**
     * Create the input handler server, using the given InputHandler. Uses any available TCP port.
     *
     * @param layer an existing InputHandler
     */
    NetworkInputHandler(InputHandler layer) {
        this.proxied = layer;
        port = -1;
    }

    /**
     * Create the input handler server, using the given InputHandler.
     *
     * @param layer      an existing InputHandler
     * @param serverPort the TCP port to listen on
     */
    NetworkInputHandler(InputHandler layer, int serverPort) {
        this(layer);
        if (serverPort >= 0) {
            tryPort = serverPort;
        }
    }

    /**
     * Tells the server to shutdown.  Threads are stopped and currently connected clients
     * are disconnected.
     */
    public void finish() {
        finished = true;
        mythread.interrupt();
        try {
            mythread.join();
        } catch (InterruptedException e) {
        }
        execPool.shutdownAfterProcessingCurrentlyQueuedTasks();
        consumer.interrupt();
        try {
            consumer.join();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Begin the server threads and begin listening on the socket.
     *
     * @throws IOException
     */
    private void beginThread() throws IOException {
        serv = new ServerSocket(tryPort);
        serv.setSoTimeout(5000);
        setPort(serv.getLocalPort());
        consumer = new Thread(new Consumer(requestChannel));
        consumer.start();
        mythread = new Thread(new Runnable() {

            public void run() {
                NetworkInputHandler.this.run();
            }
        });
        mythread.start();
    }

    /**
     * This is the run method for the main server thread.  It accepts any new client connection for the socket, and
     * handles the connection
     */
    private void run() {
        while (!finished) {
            try {
                if (Thread.interrupted()) {
                    continue;
                }
                Socket client = serv.accept();
                logger.info("connection: " + client.getInetAddress());
                final SocketRequest sr = new SocketRequest(client);
                if (Thread.interrupted()) {
                    sr.close();
                } else if (sr.validConnection()) {
                    try {
                        execPool.execute(new Runnable() {

                            public void run() {
                                try {
                                    if (!sr.isDone() && null != sr.getInputRequest()) {
                                        requestChannel.put(sr);
                                        return;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                }
                                try {
                                    sr.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        sr.close();
                    }
                } else {
                    logger.warn("invalid connection, closing: " + client.getInetAddress());
                    sr.close();
                }
            } catch (SocketTimeoutException e) {
            } catch (IOException e) {
                logger.warn("Error accepting client connection: " + e.getMessage(), e);
            }
        }
        try {
            serv.close();
        } catch (IOException e) {
            logger.warn("IOException closing server socket: " + e.getMessage(), e);
        }
    }

    /**
     * Get the port the server is listening on.
     *
     * @return
     */
    public int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    /**
     * This class encapsulates a connected client socket and the logic for communicating with it.
     */
    private class SocketRequest {

        private Socket socket;

        InputRequest inreq = null;

        private boolean done;

        private boolean gotFinished = false;

        /**
         * Create the SocketRequest
         *
         * @param socket
         */
        SocketRequest(Socket socket) {
            this.socket = socket;
        }

        /**
         * Should be called as soon as the client is connected. Returns true if the client sends HELO and is ready.
         * Returns false if the client sends and unknown message or sends FINISHED.
         *
         * @return
         * @throws IOException
         */
        boolean validConnection() throws IOException {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            int read = Net.readInt(is);
            if (Protocol.HELO == read) {
                Net.writeInt(os, Protocol.HELO);
                return true;
            } else if (Protocol.FINISHED == read) {
                done = true;
                gotFinished = true;
                return false;
            } else {
                done = true;
                return false;
            }
        }

        /**
         * Returns true if the request is completed.
         *
         * @return
         */
        synchronized boolean isDone() {
            return done;
        }

        /**
         * Returns true if an input request has been read already.
         *
         * @return
         * @throws IOException
         */
        synchronized boolean hasInputRequest() throws IOException {
            if (inreq != null) {
                return true;
            } else {
                return getInputRequest() != null;
            }
        }

        /**
         * Returns the inputRequest if already read, otherwise it reads the input request from the client.
         *
         * @return The InputRequest, or null if it could not be read
         * @throws IOException
         */
        synchronized InputRequest getInputRequest() throws IOException {
            if (null == inreq) {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                Net.writeInt(os, Protocol.BEGIN);
                int read = Net.readInt(is);
                if (Protocol.PROMPT == read) {
                    String prompt = Net.readString(is);
                    inreq = new InputRequest(prompt);
                } else if (Protocol.FINISHED == read) {
                    done = true;
                    gotFinished = true;
                    return null;
                } else {
                    done = true;
                    return null;
                }
            }
            return inreq;
        }

        /**
         * Close the connection to the client.
         *
         * @throws IOException
         */
        synchronized void close() throws IOException {
            if (!gotFinished) {
                Net.writeInt(socket, Protocol.FINISHED);
            }
            socket.close();
            done = true;
        }

        /**
         * Send the reply set for the InputRequest, and then close the connection if the remote client validates. If
         * remote client prompts again, set done to false and read the new prompt.
         *
         * @throws IOException
         * @throws IllegalStateException if this method is called before a valid InputRequest has been read from the
         *                               client socket.
         */
        synchronized void sendReply() throws IOException {
            if (null != inreq) {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                String answer = inreq.getInput();
                Net.writeInt(os, Protocol.REPLY);
                Net.writeString(os, null == answer ? "" : answer);
                int read = Net.readInt(is);
                if (Protocol.PROMPT == read) {
                    String prompt = Net.readString(is);
                    inreq = new InputRequest(prompt);
                    done = false;
                    return;
                } else if (Protocol.FINISHED == read) {
                    done = true;
                    gotFinished = true;
                    inreq = null;
                    close();
                } else {
                    done = true;
                    inreq = null;
                    close();
                }
            } else {
                throw new IllegalStateException("sendReply called without a request");
            }
        }
    }

    /**
     * continuously gets the next input request and uses it to prompt the user
     */
    class Consumer implements Runnable {

        private Takable channel;

        Consumer(Takable channel) {
            this.channel = channel;
        }

        public void run() {
            while (!finished) {
                try {
                    consume(channel.take());
                } catch (InterruptedException ex) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                for (; ; ) {
                    Object item = channel.poll(0);
                    if (item != null) {
                        SocketRequest sockreq = (SocketRequest) item;
                        try {
                            sockreq.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        break;
                    }
                }
            } catch (InterruptedException ex) {
            }
        }

        void consume(Object o) throws IOException {
            logger.info("Handling request");
            SocketRequest sockreq = (SocketRequest) o;
            while (!sockreq.isDone()) {
                InputRequest req = sockreq.getInputRequest();
                if (Thread.interrupted()) {
                    sockreq.close();
                    return;
                }
                proxied.handleInput(req);
                if (Thread.interrupted()) {
                    sockreq.close();
                    return;
                }
                sockreq.sendReply();
                if (Thread.interrupted()) {
                    sockreq.close();
                    return;
                }
            }
        }
    }
}
