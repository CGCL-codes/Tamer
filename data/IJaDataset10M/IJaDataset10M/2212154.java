package sippoint.framework.module.transport;

import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.channels.SelectionKey.OP_WRITE;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import sippoint.parser.core.Lexer;
import sippoint.parser.core.Lexer.AnalyzedSample;

/**
 * @author Martin Hynar
 * 
 */
public class TrafficChannel implements Runnable {

    protected UUID transactionKey;

    List<String> writeQ = new LinkedList<String>();

    private static String RESPONSE;

    {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("SIP/2.0 200 OK");
        messageBuilder.append("\r\n");
        messageBuilder.append("Via: SIP/2.0/UDP 10.0.0.139:1285;branch=z9hG4bK-d87543");
        messageBuilder.append("\r\n");
        messageBuilder.append("Supported: 100rel");
        messageBuilder.append("\r\n");
        messageBuilder.append("Call-ID: 1234abcd");
        messageBuilder.append("\r\n");
        messageBuilder.append("\r\n");
        RESPONSE = messageBuilder.toString();
    }

    private ByteBuffer readBuffer = ByteBuffer.allocate(100);

    private ByteBuffer writeBuffer;

    private SelectionKey key;

    /**
	 * 
	 */
    public TrafficChannel(SelectionKey key) {
        this.key = key;
    }

    /**
	 * {@inheritDoc}
	 */
    public synchronized void run() {
        if (this.key == null) {
            return;
        }
        try {
            key.interestOps(0);
            System.out.println("Traffic Channel: processing communication");
            if (key.isValid() && key.isReadable()) {
                readChannel();
                writeQ.add(RESPONSE);
                if (key.isValid()) {
                    key.interestOps(key.interestOps() | (OP_WRITE));
                }
            }
            if (key.isValid() && key.isWritable()) {
                writeChannel();
                if (key.isValid()) {
                    key.interestOps(key.interestOps() & (~OP_WRITE) | (OP_READ));
                }
            }
            this.key.selector().wakeup();
        } catch (IOException ex) {
            ex.printStackTrace();
            try {
                this.key.channel().close();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            key.selector().wakeup();
        }
    }

    /**
	 * @throws IOException
	 * 
	 */
    private void readChannel() throws IOException {
        System.out.println("Reading channel");
        SocketChannel channel = (SocketChannel) key.channel();
        int bytes = 0;
        int totalBytes = 0;
        StringBuilder receivedContent = new StringBuilder();
        Lexer lexer = new Lexer();
        readBuffer.clear();
        while ((bytes = channel.read(readBuffer)) > 0) {
            totalBytes += bytes;
            readBuffer.flip();
            String bunch = new String(readBuffer.array(), 0, bytes);
            AnalyzedSample as = lexer.getAS(bunch);
            receivedContent.append(bunch);
            readBuffer.clear();
        }
        System.err.println(receivedContent.toString());
        if (bytes < 0) {
            channel.close();
        }
    }

    private void writeChannel() throws IOException {
        if (writeQ.isEmpty()) {
            return;
        }
        System.out.println("Writting channel");
        SocketChannel channel = (SocketChannel) key.channel();
        writeBuffer = ByteBuffer.wrap(writeQ.remove(0).getBytes());
        while (writeBuffer.hasRemaining()) {
            channel.write(writeBuffer);
        }
    }
}
