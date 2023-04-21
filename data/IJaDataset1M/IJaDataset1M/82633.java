package simtools.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.ErrorManager;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LoggingEntryByteBuffer {

    static final int INITIAL_SIZE = 65536 * 4;

    static final int INITIAL_ENTRIES = 1024;

    private static final int LEVEL_OFFSET = 0 + 4;

    private static final int MILLIS_OFFSET = LEVEL_OFFSET + 2;

    private static final int NUMBER_OFFSET = MILLIS_OFFSET + 8;

    private static final int THREAD_OFFSET = NUMBER_OFFSET + 8;

    private static final int LOGGER_LENGTH_OFFSET = THREAD_OFFSET + 4;

    private static final int MESSAGE_LENGTH_OFFSET = LOGGER_LENGTH_OFFSET + 1;

    private static final int SOURCE_CLASS_LENGTH_OFFSET = MESSAGE_LENGTH_OFFSET + 2;

    private static final int SOURCE_METHOD_LENGTH_OFFSET = SOURCE_CLASS_LENGTH_OFFSET + 1;

    private static final int STRING_OFFSET = SOURCE_METHOD_LENGTH_OFFSET + 1;

    private static final int SOURCE_MAX_LENGTH = 255;

    private static final int MESSAGE_MAX_LENGTH = 65535;

    private static DocumentBuilderFactory docBuilderFactory = null;

    private int maxEntry;

    private ByteBuffer buffer;

    private int[] offsets;

    private int nbrEntry;

    private int start;

    /**(<b>LogSocketServer</b>) socketServer: the socket server that will listen for the logs.*/
    LogSocketServer socketServer;

    static int threadId = 0;

    public LoggingEntryByteBuffer() {
        this(-1);
    }

    public LoggingEntryByteBuffer(int maxEntry) {
        this.maxEntry = maxEntry;
        nbrEntry = 0;
        start = 0;
        if (maxEntry < 0) {
            offsets = new int[INITIAL_ENTRIES];
        } else {
            offsets = new int[maxEntry];
        }
        buffer = ByteBuffer.allocateDirect(INITIAL_SIZE);
    }

    public void clear() {
        buffer = ByteBuffer.allocateDirect(INITIAL_SIZE);
        buffer.position(0);
        buffer.limit(buffer.capacity());
        nbrEntry = 0;
        start = 0;
    }

    public void readXML(File f) throws ParserConfigurationException, SAXException, IOException {
        readXML(f, false);
    }

    public void readXML(File f, boolean append) throws ParserConfigurationException, SAXException, IOException {
        if (!append) {
            clear();
        }
        if (docBuilderFactory == null) {
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilderFactory.setValidating(false);
        }
        DocumentBuilder constructor = docBuilderFactory.newDocumentBuilder();
        Document doc = constructor.parse(f);
        String loggerName;
        String message;
        String sourceClassName;
        String sourceMethodName;
        int level;
        long millis;
        long sequenceNumber;
        int threadID;
        NodeList root = doc.getChildNodes();
        for (int i = 0; i < root.getLength(); i++) {
            Node n = root.item(i);
            if (n.getNodeName().equals("log")) {
                NodeList records = n.getChildNodes();
                for (int j = 0; j < records.getLength(); j++) {
                    Node record = records.item(j);
                    if (record.getNodeName().equals("record")) {
                        NodeList fields = record.getChildNodes();
                        loggerName = message = sourceClassName = sourceMethodName = "";
                        level = -1;
                        millis = -1;
                        sequenceNumber = -1;
                        threadID = -1;
                        if (fields.getLength() == 0) {
                            continue;
                        }
                        for (int k = 0; k < fields.getLength(); k++) {
                            Node field = fields.item(k);
                            Node child = field.getFirstChild();
                            if (child != null && child.getNodeType() == Node.TEXT_NODE) {
                                if (field.getNodeName().equals("logger")) {
                                    loggerName = child.getNodeValue();
                                } else if (field.getNodeName().equals("message")) {
                                    message = child.getNodeValue();
                                } else if (field.getNodeName().equals("class")) {
                                    sourceClassName = child.getNodeValue();
                                } else if (field.getNodeName().equals("method")) {
                                    sourceMethodName = child.getNodeValue();
                                } else if (field.getNodeName().equals("level")) {
                                    try {
                                        level = Level.parse(child.getNodeValue()).intValue();
                                    } catch (IllegalArgumentException ie) {
                                    }
                                } else if (field.getNodeName().equals("millis")) {
                                    try {
                                        millis = Long.parseLong(child.getNodeValue());
                                    } catch (NumberFormatException ne) {
                                    }
                                } else if (field.getNodeName().equals("sequence")) {
                                    try {
                                        sequenceNumber = Long.parseLong(child.getNodeValue());
                                    } catch (NumberFormatException ne) {
                                    }
                                } else if (field.getNodeName().equals("thread")) {
                                    try {
                                        threadID = Integer.parseInt(child.getNodeValue());
                                    } catch (NumberFormatException ne) {
                                    }
                                } else if (field.getNodeName().equals("exception")) {
                                    message = readException(field, message);
                                }
                            }
                        }
                        add(encode(loggerName, message, sourceClassName, sourceMethodName, level, millis, sequenceNumber, threadID));
                    }
                }
            }
        }
    }

    protected String readException(Node field, String message) {
        NodeList exfields = field.getChildNodes();
        for (int l = 0; l < exfields.getLength(); l++) {
            Node exfield = exfields.item(l);
            Node exchild = exfield.getFirstChild();
            if (exchild != null && exchild.getNodeType() == Node.TEXT_NODE) {
                System.err.println(exchild.getNodeValue());
                if (exfield.getNodeName().equals("message")) {
                    message += "\n" + exchild.getNodeValue();
                } else if (exfield.getNodeName().equals("frame")) {
                    NodeList frames = exfield.getChildNodes();
                    String className = "";
                    String methodName = "";
                    int lineNumber = -1;
                    for (int k = 0; k < frames.getLength(); k++) {
                        Node frame = frames.item(k);
                        Node child = frame.getFirstChild();
                        if (child != null && child.getNodeType() == Node.TEXT_NODE) {
                            if (frame.getNodeName().equals("class")) {
                                className = child.getNodeValue();
                            } else if (frame.getNodeName().equals("method")) {
                                methodName = child.getNodeValue();
                            } else if (frame.getNodeName().equals("line")) {
                                try {
                                    lineNumber = Integer.parseInt(child.getNodeValue());
                                } catch (NumberFormatException nfe) {
                                }
                            }
                        }
                    }
                    int index = className.lastIndexOf('.');
                    String fileName = className;
                    if (index >= 0 && index < className.length() - 1) {
                        fileName = className.substring(index + 1);
                    }
                    index = fileName.indexOf('$');
                    if (index > 0) {
                        fileName = fileName.substring(0, index);
                    }
                    message += "\n    at " + className + "." + methodName + "(" + fileName + ".java:" + lineNumber + ")";
                }
            }
        }
        return message;
    }

    /**
	 * Method read
	 * <br><b>Summary:</b><br>
	 * This method permits to read the given binary logfile, and put it in the current buffer.
	 * @param f            The file to open that contains binary logs.
	 * @throws IOException
	 */
    public void read(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        FileChannel fc = fis.getChannel();
        buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, f.length());
        int o = 0;
        do {
            int l = buffer.getInt(o);
            if (l <= STRING_OFFSET) {
                throw new IOException("Corrupted file : invalid packet length");
            }
            addOffset(o);
            o += l;
        } while (o < buffer.limit());
    }

    /**
	 * Method openServer
	 * <br><b>Summary:</b><br>
	 * Open the LogSocketserver on the given port.
	 * @param port         The port that the server will listen to.
	 * @throws IOException
	 */
    public void openServer(int port) throws IOException {
        if (socketServer != null) {
            closeServer();
        }
        clear();
        socketServer = new LogSocketServer(port);
        Thread t = new Thread(socketServer, socketServer.getClass().getName() + threadId);
        threadId++;
        t.start();
    }

    /**
	 * Method closeServer
	 * <br><b>Summary:</b><br>
	 * Close the logSocketServer.
	 * @throws IOException
	 */
    public void closeServer() throws IOException {
        if (socketServer != null) {
            socketServer.stop();
            socketServer = null;
        }
    }

    /**
     * Method isSocketServerOpen
     * <br><b>Summary:</b><br>
     * Return the port used by the socketServer or -1 if no server is listening.
     * @return <b>(int)</b>	The port used by the socketServer or -1 if no server is listening.
     */
    public int isSocketServerOpen() {
        int result = -1;
        if (socketServer != null) {
            result = socketServer.getPort();
        }
        return result;
    }

    /**
	 * Method save
	 * <br><b>Summary:</b><br>
	 * This method write the current buffer to the given file, in binary format.
	 * @param f                The file to write to.
	 * @throws IOException
	 */
    public void save(File f) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        FileChannel fc = fos.getChannel();
        buffer.flip();
        fc.write(buffer);
        buffer.limit(buffer.capacity());
        fc.close();
        fos.flush();
        fos.close();
    }

    /**
	 * Method checkBufferSize
	 * <br><b>Summary:</b><br>
	 * This method ensures that the buffer contains enought space to add length more bytes.
     * If the buffer is not enough big, it double its capacity.
	 * @param length       The number of bytes that the capacity could handle.
	 */
    protected void checkBufferSize(int length) {
        if ((buffer.position() + length) > buffer.capacity()) {
            ByteBuffer nb = ByteBuffer.allocateDirect(buffer.capacity() * 2);
            buffer.flip();
            nb.put(buffer);
            buffer = nb;
        }
    }

    /**
	 * Method add
	 * <br><b>Summary:</b><br>
	 * This method add the given byteBuffer to the buffer.
	 * @param b        The buffer to add to the logs.
	 */
    protected void add(ByteBuffer b) {
        addOffset(buffer.position());
        int l = b.remaining();
        checkBufferSize(l);
        buffer.put(b);
    }

    protected int addOffset(int offset) {
        if ((maxEntry < 0) && ((nbrEntry + start + 1) >= offsets.length)) {
            int[] n = new int[offsets.length * 2];
            System.arraycopy(offsets, 0, n, 0, offsets.length);
            offsets = n;
        }
        int index = (nbrEntry + start) % offsets.length;
        int previous = offsets[index];
        offsets[index] = offset;
        if (nbrEntry < offsets.length) {
            nbrEntry++;
            return -1;
        } else {
            start = (start + 1) % offsets.length;
            return previous;
        }
    }

    public int getLength() {
        return nbrEntry;
    }

    public void setSize(int maxEntry) {
        nbrEntry = 0;
        if (maxEntry < 0) {
            offsets = new int[INITIAL_ENTRIES];
        } else {
            offsets = new int[maxEntry];
        }
    }

    public int getSize() {
        return maxEntry;
    }

    public int getLevel(int index) {
        return buffer.getShort(offsets[index] + LEVEL_OFFSET);
    }

    public String getLoggerName(int index) {
        int length = ((int) buffer.get(offsets[index] + LOGGER_LENGTH_OFFSET)) & 0xff;
        final char[] res = new char[length];
        int offset = offsets[index] + STRING_OFFSET;
        for (int i = 0; i < length; i++) {
            res[i] = (char) buffer.get(offset++);
        }
        return new String(res);
    }

    public String getMessage(int index) {
        int length = ((int) buffer.getShort(offsets[index] + MESSAGE_LENGTH_OFFSET)) & 0xffff;
        final char[] res = new char[length];
        int offset = offsets[index] + STRING_OFFSET + (((int) buffer.get(offsets[index] + LOGGER_LENGTH_OFFSET)) & 0xff);
        for (int i = 0; i < length; i++) {
            res[i] = (char) buffer.get(offset++);
        }
        return new String(res);
    }

    public long getMillis(int index) {
        return buffer.getLong(offsets[index] + MILLIS_OFFSET);
    }

    public long getSequenceNumber(int index) {
        return buffer.getLong(offsets[index] + NUMBER_OFFSET);
    }

    public String getSourceClassName(int index) {
        int length = ((int) buffer.get(offsets[index] + SOURCE_CLASS_LENGTH_OFFSET)) & 0xff;
        final char[] res = new char[length];
        int offset = offsets[index] + STRING_OFFSET + (((int) buffer.get(offsets[index] + LOGGER_LENGTH_OFFSET)) & 0xff) + (((int) buffer.getShort(offsets[index] + MESSAGE_LENGTH_OFFSET)) & 0xffff);
        for (int i = 0; i < length; i++) {
            res[i] = (char) buffer.get(offset++);
        }
        return new String(res);
    }

    public String getSourceMethodName(int index) {
        int length = ((int) buffer.get(offsets[index] + SOURCE_METHOD_LENGTH_OFFSET)) & 0xff;
        final char[] res = new char[length];
        int offset = offsets[index] + STRING_OFFSET + (((int) buffer.get(offsets[index] + LOGGER_LENGTH_OFFSET)) & 0xff) + (((int) buffer.get(offsets[index] + SOURCE_CLASS_LENGTH_OFFSET)) & 0xff) + (((int) buffer.getShort(offsets[index] + MESSAGE_LENGTH_OFFSET)) & 0xffff);
        for (int i = 0; i < length; i++) {
            res[i] = (char) buffer.get(offset++);
        }
        return new String(res);
    }

    public int getThreadID(int index) {
        return buffer.getInt(offsets[index] + THREAD_OFFSET);
    }

    static ByteBuffer encode(String loggerName, String message, String sourceClassName, String sourceMethodName, int level, long millis, long sequenceNumber, int threadID) {
        int loggerLength = loggerName.length();
        if (loggerLength > SOURCE_MAX_LENGTH) {
            loggerLength = SOURCE_MAX_LENGTH;
        }
        int messageLength = message.length();
        if (messageLength > MESSAGE_MAX_LENGTH) {
            messageLength = MESSAGE_MAX_LENGTH;
        }
        int classLength = sourceClassName.length();
        if (classLength > SOURCE_MAX_LENGTH) {
            classLength = SOURCE_MAX_LENGTH;
        }
        int methodLength = sourceMethodName.length();
        if (methodLength > SOURCE_MAX_LENGTH) {
            methodLength = SOURCE_MAX_LENGTH;
        }
        byte[] b = new byte[STRING_OFFSET + loggerLength + messageLength + classLength + methodLength];
        ByteBuffer bb = ByteBuffer.wrap(b);
        bb.putInt(0, b.length);
        bb.putShort(LEVEL_OFFSET, (short) level);
        bb.putLong(MILLIS_OFFSET, millis);
        bb.putLong(NUMBER_OFFSET, sequenceNumber);
        bb.putInt(THREAD_OFFSET, threadID);
        bb.put(LOGGER_LENGTH_OFFSET, (byte) (loggerLength & 0xff));
        bb.putShort(MESSAGE_LENGTH_OFFSET, (short) (messageLength & 0xffff));
        bb.put(SOURCE_CLASS_LENGTH_OFFSET, (byte) (classLength & 0xff));
        bb.put(SOURCE_METHOD_LENGTH_OFFSET, (byte) (methodLength & 0xff));
        int ib = STRING_OFFSET;
        for (int i = 0; i < loggerLength; i++) {
            b[ib++] = (byte) loggerName.charAt(i);
        }
        for (int i = 0; i < messageLength; i++) {
            b[ib++] = (byte) message.charAt(i);
        }
        for (int i = 0; i < classLength; i++) {
            b[ib++] = (byte) sourceClassName.charAt(i);
        }
        for (int i = 0; i < methodLength; i++) {
            b[ib++] = (byte) sourceMethodName.charAt(i);
        }
        return bb;
    }

    public abstract static class Handler extends java.util.logging.Handler {

        public void publish(LogRecord record) {
            if (!isLoggable(record)) {
                return;
            }
            try {
                StringBuffer message = new StringBuffer(record.getMessage());
                if (record.getThrown() != null) {
                    message.append("\n");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    message.append(sw.toString());
                }
                write(encode(record.getLoggerName(), message.toString(), record.getSourceClassName(), record.getSourceMethodName(), record.getLevel().intValue(), record.getMillis(), record.getSequenceNumber(), record.getThreadID()));
            } catch (IOException e) {
                getErrorManager().error("Can not publish", e, ErrorManager.WRITE_FAILURE);
                close();
            }
        }

        protected abstract void write(ByteBuffer bb) throws IOException;
    }

    /**
     * Class LogSocketServer
     * This class permits to launch a LogSocketServer.
     * A LogSocketServer will listen on the given port to receive logs.
     * It can be launched, paused, re-launched, and stopped.
     * STATE_OPEN: server status open, means ready to accept client connection.
     * STATE_PAUSED: server status paused, means do not accept connection, but can be re-open.
     * STATE_STOPPED: server status stopped, means server is shutting down.
     */
    private class LogSocketServer implements Runnable {

        /**(<b>int</b>) state: the state of the server.*/
        private int state;

        /**(<b>ServerSocketChannel</b>) serverChannel: The socket server that is managed by the logSocketServer.*/
        private ServerSocketChannel serverChannel;

        /**(<b>int</b>) port: The port of the LogSocketServer.*/
        private int port;

        /**(<b>int</b>) STATE_OPEN: server status open, means ready to accept client connection.*/
        private static final int STATE_OPEN = 0;

        /**(<b>int</b>) STATE_STOPPED: server status stopped, means server is shutting down.*/
        private static final int STATE_STOPPED = 2;

        /**
         * Contructor LogSocketServer
         * <br><b>Summary:</b><br>
         * The constructor of the class LogSocketServer.
         * @param port The port to listen to.
         */
        public LogSocketServer(int port) throws IOException {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(port));
            this.port = port;
        }

        public void run() {
            try {
                while (state != STATE_STOPPED) {
                    SocketChannel channel = serverChannel.accept();
                    LogSocketConnectionHandler clientHandler = new LogSocketConnectionHandler(channel);
                    Thread t = new Thread(clientHandler, "LogSocketConnectionHandler" + threadId);
                    t.start();
                    threadId++;
                }
            } catch (IOException e) {
            }
        }

        /**
         * Method start
         * <br><b>Summary:</b><br>
         * Permits to set the server in open state.
         */
        public void start() {
            state = STATE_OPEN;
        }

        /**
         * Method stop
         * <br><b>Summary:</b><br>
         * Permits to set the server in stopped state.
         */
        public void stop() {
            state = STATE_STOPPED;
            try {
                serverChannel.close();
            } catch (IOException e) {
            }
        }

        /**
         * Method getPort
         * <br><b>Summary:</b><br>
         * Return the port used by the SocketCannel.
         * @return <b>(int)</b>	The port used by the SocketCannel.
         */
        public int getPort() {
            return port;
        }
    }

    /**
     * Class LogSocketConnectionHandler.
     * This method permits to handle a connection to the logSocketServer.
     */
    private class LogSocketConnectionHandler implements Runnable {

        /**(<b>SocketChannel</b>) channel: the channel to handle.*/
        private SocketChannel channel;

        /**
         * Contructor LogSocketConnectionHandler
         * <br><b>Summary:</b><br>
         * The constructor of the class LogSocketConnectionHandler.
         * @param channel   The channel to handle.
         */
        public LogSocketConnectionHandler(SocketChannel channel) {
            this.channel = channel;
        }

        public void run() {
            try {
                boolean continueReading = true;
                while (continueReading) {
                    int pos = buffer.position();
                    checkBufferSize(4);
                    buffer.limit(pos + 4);
                    int numBytesRead = channel.read(buffer);
                    if (numBytesRead != -1) {
                        int length = buffer.getInt(pos);
                        if (length <= STRING_OFFSET) {
                            throw new IOException("Corrupted stream : invalid packet length");
                        }
                        checkBufferSize(length - 4);
                        buffer.limit(pos + length);
                        channel.read(buffer);
                        addOffset(pos);
                    } else {
                        channel.close();
                        continueReading = false;
                    }
                }
            } catch (IOException e) {
            }
        }
    }
}
