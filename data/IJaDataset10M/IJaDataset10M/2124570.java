package ch.unifr.nio.framework.examples;

import ch.unifr.nio.framework.AbstractClientSocketChannelHandler;
import ch.unifr.nio.framework.Dispatcher;
import ch.unifr.nio.framework.transform.AbstractForwarder;
import ch.unifr.nio.framework.transform.ByteBufferToStringTransformer;
import ch.unifr.nio.framework.transform.StringToByteBufferTransformer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An echo client application that demonstrates how to connect to a server
 * in a non-blocking way.
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class NonBlockingEchoClient extends AbstractClientSocketChannelHandler {

    private static final Logger LOGGER = Logger.getLogger(NonBlockingEchoClient.class.getName());

    private final String host;

    private final Lock lock = new ReentrantLock();

    private final Condition inputArrived = lock.newCondition();

    /** Creates a new instance of NonBlockingEchoClient
     * @param host the host name of the server
     * @param port the port of the server
     */
    public NonBlockingEchoClient(String host, int port) {
        this.host = host;
        ByteBufferToStringTransformer byteBufferToStringTransformer = new ByteBufferToStringTransformer();
        byteBufferToStringTransformer.setNextForwarder(new NonBlockingEchoClientTransformer());
        channelReader.setNextForwarder(byteBufferToStringTransformer);
        try {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.start();
            dispatcher.registerClientSocketChannelHandler(host, port, this);
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        }
    }

    /**
     * starts the NonBlockingEchoClient
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: NonBlockingEchoClient <server host> <server port>");
            System.exit(1);
        }
        new NonBlockingEchoClient(args[0], Integer.parseInt(args[1]));
    }

    @Override
    public void inputClosed() {
        System.out.println("EchoServer closed the connection");
        System.exit(1);
    }

    @Override
    public void channelException(Exception exception) {
        System.out.println("Connection error " + exception);
        System.exit(1);
    }

    @Override
    public void resolveFailed() {
        System.out.println("Could not resolve \"" + host + "\"");
        System.exit(1);
    }

    @Override
    public void connectSucceeded() {
        new OutputHandler().start();
    }

    @Override
    public void connectFailed(IOException exception) {
        LOGGER.log(Level.WARNING, null, exception);
        System.exit(1);
    }

    private class OutputHandler extends Thread {

        private final StringToByteBufferTransformer transformer;

        public OutputHandler() {
            setDaemon(false);
            transformer = new StringToByteBufferTransformer();
            transformer.setNextForwarder(channelWriter);
        }

        @Override
        public void run() {
            System.out.println("NonBlockingEchoClient is running...");
            try {
                InputStreamReader streamReader = new InputStreamReader(System.in);
                BufferedReader stdIn = new BufferedReader(streamReader);
                while (true) {
                    System.out.print("Your input: ");
                    String userInput = stdIn.readLine();
                    if (userInput.length() == 0) {
                        continue;
                    }
                    System.out.println("sending \"" + userInput + "\"");
                    transformer.forward(userInput);
                    lock.lock();
                    try {
                        inputArrived.await();
                    } catch (InterruptedException ex) {
                        LOGGER.log(Level.WARNING, null, ex);
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (IOException ex) {
                LOGGER.log(Level.WARNING, null, ex);
            }
        }
    }

    private class NonBlockingEchoClientTransformer extends AbstractForwarder<String, Void> {

        @Override
        public void forward(String input) throws IOException {
            System.out.println("received \"" + input + "\"");
            lock.lock();
            try {
                inputArrived.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
