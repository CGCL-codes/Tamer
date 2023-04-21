package lejos.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.EmptyQueueException;
import lejos.nxt.comm.NXTConnection;
import lejos.util.Delay;

/**
 * Logger class for the NXT that supports real time and deferred (cached) data logging of the primitive datatypes. 
 * <p>
 * This class communicates with 
 * <code>lejos.pc.charting.DataLogger</code> via Bluetooth or USB which is used by the NXT Charting Logger tool.
 * <p>When instantiated, the <code>NXTDataLogger</code> starts out in cached mode (<code>{@link #startCachingLog}</code>) as default.
 * Cache mode uses a growable ring buffer that consumes (if needed) up to all available memory during a logging run.
 * <p>Hints for real-time logging efficiency:
 * <ul>
 * <li>Try to keep your datatypes the same across <code>writeLog()</code> method calls to avoid the protocol
 * overhead that is incurred when 
 * switching datatypes. For instance, every time you change between <code>writeLog(int)</code> and <code>writeLog(long)</code>, a 
 * synchronization message must be sent to change the datatype on the receiver (<code>lejos.pc.charting.DataLogger</code>).
 * <li>Use the the <code>writeLog()</code> method with the smallest datatype that fits your data. Less data means better 
 * throughput overall. Note that this implementation always use 4 bytes for datatypes <code>boolean</code>, <code>short</code>, 
 * <code>int</code>, and <code>float</code> and 8 bytes for <code>long</code> and <code>double</code>.
 * </ul>
 * @author Kirk P. Thompson
 */
public class NXTDataLogger implements Logger {

    private static final byte ATTENTION1 = (byte) 0xff;

    private static final byte ATTENTION2 = (byte) 0xab;

    private static final byte COMMAND_DATATYPE = 1;

    private static final byte DT_BOOLEAN = 0;

    private static final byte DT_BYTE = 1;

    private static final byte DT_SHORT = 2;

    private static final byte DT_INTEGER = 3;

    private static final byte DT_LONG = 4;

    private static final byte DT_FLOAT = 5;

    private static final byte DT_DOUBLE = 6;

    private static final byte COMMAND_SETHEADERS = 2;

    private static final byte COMMAND_FLUSH = 3;

    private static final byte COMMAND_COMMENT = 4;

    private static final byte COMMAND_PASSTHROUGH = 5;

    private static final int XORMASK = 0xff;

    private static final int FLUSH_THRESHOLD_MS = 200;

    private static final int BYTEBUF_CAPCITY_INCREMENT = 256;

    private DataOutputStream dos = null;

    private DataInputStream dis = null;

    private byte currentDataType = DT_INTEGER;

    private byte itemsPerLine = -1;

    private int currColumnPosition = 1;

    private boolean initialFlush = false;

    private LogColumn[] columnDefs = null;

    private ByteRingQueue byteQueue = new ByteRingQueue();

    private byte[] attentionRandoms = new byte[5];

    private int chksumSeedIndex = -1;

    private static final int LMSTATE_UNINIT = 0;

    private static final int LMSTATE_CACHE = 1;

    private static final int LMSTATE_REAL = 2;

    private int logmodeState = LMSTATE_UNINIT;

    private boolean disableWriteState = false;

    private int sessionBeginTime;

    private int setColumnsCount = 0;

    private int lineBytes;

    private NXTConnection passedNXTConnection = null;

    private String commentText = new String("");

    private int currentTimeStamp = 0;

    private long finishLineTS = 0;

    /**
     * Default constructor establishes a data logger instance in cache mode.
     * @see #startCachingLog
     * @see #startRealtimeLog(NXTConnection)
     */
    public NXTDataLogger() {
        for (int i = 0; i < attentionRandoms.length; i++) {
            attentionRandoms[i] = (byte) ((int) (Math.random() * 255) & 0xff);
        }
        startCachingLog();
    }

    /** 
     * caching dos. uses write(int) override to grab all encoded bytes from DataOutputStream which we cache to "play"
     * to receiver. Signaling protocol bytes are not saved here as they don't need to be since we know the row structure
     * (i.e. datatypes and their position in the row) from setColumns()
     */
    class CacheOutputStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            byteQueue.add((byte) (b & 0xff));
        }
    }

    private class ByteRingQueue {

        private int addIndex = 0;

        private int removeIndex = 0;

        private byte[] barr;

        private boolean reachedQueueEnd = false;

        private int lineByteCounter = 0;

        private boolean ringMode = false;

        ByteRingQueue() {
            barr = new byte[1024];
        }

        /** 
         * Get the next FIFO value from the queue. If it is the last element in the ring buffer, the next call to this method
         * will throw an EmptyQueueException to signal we have reached the end (beginning?). Rinse, repeat.
         * @return the next FIFO value
         * @throws EmptyQueueException when end of ringbuffer is reached. 
         */
        byte remove() throws EmptyQueueException {
            if (reachedQueueEnd) {
                reachedQueueEnd = false;
                throw new EmptyQueueException();
            }
            byte retval = barr[removeIndex++];
            if (removeIndex >= barr.length) removeIndex = 0;
            if (removeIndex == addIndex) reachedQueueEnd = true;
            return retval;
        }

        void add(byte b) {
            barr[addIndex++] = b;
            this.lineByteCounter++;
            if (!ringMode && !ensureCapacity(BYTEBUF_CAPCITY_INCREMENT)) {
                ringMode = true;
                wrapReadpointerToBONL();
            }
            if (this.lineByteCounter > NXTDataLogger.this.lineBytes - 1) {
                this.lineByteCounter = 0;
                if (ringMode) wrapReadpointerToBONL();
            }
            if (addIndex >= barr.length) addIndex = 0;
            return;
        }

        /** 
         * expand array by capacity bytes. if OutOfMemoryError, return  false
         * @param capacity expand by how many bytes?
         * @return true for success, false if OutOfMemoryError
         */
        boolean ensureCapacity(int capacity) {
            if (this.addIndex >= this.barr.length - 1) {
                try {
                    byte[] tb = new byte[this.barr.length + capacity];
                    System.arraycopy(this.barr, this.removeIndex, tb, 0, this.addIndex - this.removeIndex);
                    this.addIndex -= this.removeIndex;
                    this.removeIndex = 0;
                    this.barr = tb;
                    tb = null;
                } catch (OutOfMemoryError e) {
                    return false;
                }
            }
            return true;
        }

        void wrapReadpointerToBONL() {
            this.removeIndex += NXTDataLogger.this.lineBytes;
            if (this.removeIndex >= this.barr.length) this.removeIndex = 0;
        }

        int byteCount() {
            int size = addIndex - removeIndex;
            if (size < 0) size = barr.length + size;
            return size;
        }
    }

    private byte getChksumRandVal() {
        if (chksumSeedIndex >= attentionRandoms.length - 1) chksumSeedIndex = -1;
        chksumSeedIndex++;
        return attentionRandoms[chksumSeedIndex];
    }

    /** 
     * Start a realtime logging session using passed data streams.
     * The <code>setColumns()</code>
     * method must be called after this method is called and before the first
     * <code>writeLog()</code> method is called. 
     * <p>
     * The use of this method is mutually exclusive with <code>startCachingLog()</code> and will reset internal state 
     * to realtime mode.
     * @param out A valid <code>DataOutputStream</code>
     * @param in A valid <code>DataInputStream</code>
     * @throws IOException if the data streams are not valid
     * @see #stopLogging
     * @see #startRealtimeLog(NXTConnection)
     * @see #setColumns
     */
    public void startRealtimeLog(DataOutputStream out, DataInputStream in) throws IOException {
        if (out == null) throw new IOException("DataOutputStream is null");
        if (in == null) throw new IOException("DataInputStream is null");
        logmodeState = LMSTATE_REAL;
        this.dos = out;
        this.dis = in;
        sessionBeginTime = (int) System.currentTimeMillis();
        setColumnsCount = 0;
    }

    /** 
     * Start a realtime logging session using passed <code>NXTConnection</code> to retrieve the data streams. The
     * connection must already be established.
     * The <code>setColumns()</code>
     * method must be called after this method is called and before the first
     * <code>writeLog()</code> method is called. 
     * <p>
     * The use of this method is mutually exclusive with <code>startCachingLog()</code> and will reset internal state 
     * to realtime mode.
     * @param connection A connected <code>NXTConnection</code> instance
     * @throws IOException if the data streams are not valid
     * @see #stopLogging
     * @see #startRealtimeLog(DataOutputStream, DataInputStream)
     * @see #setColumns
     */
    public void startRealtimeLog(NXTConnection connection) throws IOException {
        this.passedNXTConnection = connection;
        startRealtimeLog(connection.openDataOutputStream(), connection.openDataInputStream());
    }

    /** 
     * Stop the logging session and close down the connection and data streams. After this method is called, you must call
     * one of the logging mode start methods to begin a new logging session.
     * @see #startRealtimeLog(NXTConnection)
     * @see #startCachingLog
     */
    public void stopLogging() {
        cleanConnection();
    }

    /** 
     * Sets caching (deferred) logging. This is the default mode at instantiation. 
     * The <code>setColumns()</code>
     * method must be called after this method is called and before the first
     * <code>writeLog()</code> method is called. 
     * <p>
     * The use of this method is mutually exclusive with the <code>startRealtimeLog()</code> methods and will reset internal state 
     * to caching mode.
     * @see #stopLogging
     * @see #sendCache(NXTConnection)
     * @see #startRealtimeLog(NXTConnection)
     * 
     */
    public void startCachingLog() {
        logmodeState = LMSTATE_CACHE;
        byteQueue = new ByteRingQueue();
        this.dos = new DataOutputStream(new CacheOutputStream());
        sessionBeginTime = (int) System.currentTimeMillis();
        setColumnsCount = 0;
    }

    /** 
     * Sends the log cache. Valid only for caching (deferred) logging using startCachingLog(). 
     * @param out A valid <code>DataOutputStream</code>
     * @param in A valid <code>DataInputStream</code>
     * @throws IOException if the data streams are not valid
     * @throws IllegalStateException if <code>startCachingLog()</code> has not been called
     */
    public void sendCache(DataOutputStream out, DataInputStream in) throws IOException {
        if (logmodeState != LMSTATE_CACHE) throw new IllegalStateException("wrong mode");
        if (out == null) throw new IOException("DataOutputStream is null");
        if (in == null) throw new IOException("DataInputStream is null");
        this.dos = out;
        this.dis = in;
        logmodeState = LMSTATE_REAL;
        sendHeaders();
        setDataType(columnDefs[0].getDatatype());
        byte[] tempBytes;
        boolean doExit = false;
        outer: while (!doExit) {
            for (int i = 0; i < columnDefs.length; i++) {
                setDataType(columnDefs[i].getDatatype());
                try {
                    tempBytes = getBytesFromCache(columnDefs[i].getSize());
                } catch (EmptyQueueException e) {
                    signalFlush();
                    doExit = true;
                    break outer;
                }
                dos.write(tempBytes);
            }
        }
        logmodeState = LMSTATE_CACHE;
    }

    /** 
     * Sends the log cache using passed <code>NXTConnection</code> to retrieve the data streams. The
     * connection must already be established.  Valid only for caching (deferred) logging using <code>startCachingLog()</code>.
     * @param connection A connected <code>NXTConnection</code> instance
     * @throws IOException if the data streams are not valid
     * @throws IllegalStateException if <code>startCachingLog()</code> has not been called
     */
    public void sendCache(NXTConnection connection) throws IOException {
        this.passedNXTConnection = connection;
        sendCache(connection.openDataOutputStream(), connection.openDataInputStream());
    }

    private byte[] getBytesFromCache(int count) throws EmptyQueueException {
        byte[] temp = new byte[count];
        for (int i = 0; i < count; i++) {
            temp[i] = byteQueue.remove();
        }
        return temp;
    }

    private void signalFlush() {
        sendATTN();
        byte[] command = { COMMAND_FLUSH, -1 };
        sendCommand(command);
    }

    /** 
     * Close the current open connection. The data stream is flushed and closed. After calling this method, 
     *   another connection must be established or data streams re-created.
     */
    private void cleanConnection() {
        signalFlush();
        try {
            this.dos.flush();
            Delay.msDelay(100);
            if (this.passedNXTConnection != null) {
                passedNXTConnection.close();
            }
            if (this.dis != null) this.dis.close();
            if (this.dos != null) this.dos.close();
        } catch (IOException e) {
            ;
        } catch (Exception e) {
            ;
        }
        this.dis = null;
        this.dos = null;
    }

    /** 
     * lower level data sending method
     * @param command the bytes[] to send
     */
    private final synchronized void sendCommand(byte[] command) {
        if (this.dos == null) return;
        try {
            this.dos.write(command);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    private void checkWriteState(int datatype) {
        if (this.disableWriteState) return;
        if (setColumnsCount == 0) throw new IllegalStateException("cols not set ");
        if (this.currColumnPosition >= ((this.itemsPerLine) & 0xff)) throw new IllegalStateException("too many cols ");
        this.disableWriteState = true;
        if (this.currColumnPosition == 1) {
            setDataType(DT_INTEGER);
            this.currentTimeStamp = (int) System.currentTimeMillis() - sessionBeginTime;
            writeLog(currentTimeStamp);
            if (!initialFlush) {
                initialFlush = true;
                try {
                    this.dos.flush();
                } catch (IOException e) {
                    cleanConnection();
                }
            }
        }
        if (datatype != columnDefs[currColumnPosition].getDatatype()) throw new IllegalStateException("datatyp mismatch ");
        setDataType(datatype);
        this.currColumnPosition++;
        this.disableWriteState = false;
    }

    /** 
     * Finish the row and start a new one. 
     * <p>
     * The Column count is set by calling
     * <code>setColumns()</code> and you must ensure that you call the appropriate <code>writeLog()</code> method the same number of 
     * times as that column count before this method is called.
     * 
     * @throws IllegalStateException if all the columns defined with <code>setColumns()</code> per row have not been logged. 
     * @see #setColumns
     */
    public void finishLine() {
        if (this.currColumnPosition != ((this.itemsPerLine) & 0xff)) throw new IllegalStateException("too few cols ");
        currColumnPosition = 1;
        if (currentTimeStamp == 0) {
            return;
        }
        if (this.finishLineTS == 0) this.finishLineTS = System.currentTimeMillis();
        if ((System.currentTimeMillis() - this.finishLineTS) > FLUSH_THRESHOLD_MS) {
            try {
                dos.flush();
            } catch (IOException e) {
                ;
            }
        }
        this.finishLineTS = System.currentTimeMillis();
        if (this.commentText.equals("")) return;
        sendATTN();
        byte[] command = { COMMAND_COMMENT, -1 };
        sendCommand(command);
        try {
            this.dos.writeInt(this.currentTimeStamp);
        } catch (IOException e) {
            cleanConnection();
        }
        writeStringData(this.commentText);
        this.commentText = "";
    }

    /** 
     * send the command to set the active datatype
     * @param datatype
     */
    private void setDataType(int datatype) {
        if (this.currentDataType == (datatype & 0xff)) return;
        if (logmodeState != LMSTATE_REAL) return;
        byte[] command = { COMMAND_DATATYPE, -1 };
        this.currentDataType = (byte) (datatype & 0xff);
        command[1] = this.currentDataType;
        sendATTN();
        sendCommand(command);
    }

    /** 
     * Send an ATTENTION request. Commands usually follow. There is no response/handshake mechanism.
     */
    private void sendATTN() {
        byte[] command = { ATTENTION1, ATTENTION2, 0, 0 };
        int total = 0;
        command[2] = getChksumRandVal();
        for (int i = 0; i < 3; i++) total += command[i];
        command[3] = (byte) ((total ^ XORMASK) & 0xff);
        sendCommand(command);
    }

    /** 
    * Write a <code>boolean</code> value as an <code>int</code> 1 (<code>true</code>) or 0 (<code>false</code>) to the log. 
    * In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>boolean</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @throws OutOfMemoryError if in cache mode and memory is exhausted.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(boolean datapoint) {
        writeInt(datapoint ? 1 : 0, DT_BOOLEAN);
    }

    /** 
    * Write a <code>byte</code> value to the log. 
    * In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>byte</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @throws OutOfMemoryError if in cache mode and memory is exhausted.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(byte datapoint) {
        writeInt(datapoint, DT_BYTE);
    }

    /** 
    * Write a <code>short</code> value to the log. 
    * In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>short</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @throws OutOfMemoryError if in cache mode and memory is exhausted.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(short datapoint) {
        writeInt(datapoint, DT_SHORT);
    }

    /** 
      * Write an <code>int</code> to the log. In realtime logging mode, if an <code>IOException</code> occurs, the connection
      * and data streams are silently closed down and no exception is thrown from this method.
      * 
      * @param datapoint The <code>int</code> value to log.
      * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
      * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
      * <code>finishLine()</code> was not called after last column logged), or the column
      * definitions have not been set with <code>setColumns()</code>.
      * @see #setColumns
      * @see #finishLine
      */
    public void writeLog(int datapoint) {
        writeInt(datapoint, DT_INTEGER);
    }

    private void writeInt(int datapoint, byte datatype) {
        checkWriteState(datatype);
        try {
            this.dos.writeInt(datapoint);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    /** 
    * Write an <code>long</code> to the log. In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>long</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(long datapoint) {
        checkWriteState(DT_LONG);
        try {
            this.dos.writeLong(datapoint);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    /** 
    * Write an <code>float</code> to the log. In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>float</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(float datapoint) {
        checkWriteState(DT_FLOAT);
        try {
            this.dos.writeFloat(datapoint);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    /** 
    * Write an <code>double</code> to the log. In realtime logging mode, if an <code>IOException</code> occurs, the connection
    * and data streams are silently closed down and no exception is thrown from this method.
    * 
    * @param datapoint The <code>double</code> value to log.
    * @throws IllegalStateException if the column datatype for the column position this method was called for does not match
    * the datatype that was set in <code>setColumns()</code>, the column position exceeds the total column count (i.e.
    * <code>finishLine()</code> was not called after last column logged), or the column
    * definitions have not been set with <code>setColumns()</code>.
    * @see #setColumns
    * @see #finishLine
    */
    public void writeLog(double datapoint) {
        checkWriteState(DT_DOUBLE);
        try {
            this.dos.writeDouble(datapoint);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    /** 
     * Log a text comment. Displayed as event marker on domain axis of NXJChartingLogger chart and after the current line in the log. 
     * Ignored in cache mode.
     * Only one comment per line. (i.e. before <code>finishLine()</code> is called)
    * @param comment The comment
    */
    public void writeComment(String comment) {
        if (logmodeState != LMSTATE_REAL) return;
        this.commentText = comment;
    }

    private void writeStringData(String strData) {
        byte[] strBytes;
        int residual;
        int arrayLength;
        strBytes = strData.getBytes("");
        residual = (strBytes.length + 1) % 4;
        arrayLength = residual > 0 ? strBytes.length + 1 + (4 - residual) : strBytes.length + 1;
        byte[] tempBytes = new byte[arrayLength];
        System.arraycopy(strBytes, 0, tempBytes, 0, strBytes.length);
        try {
            this.dos.write(tempBytes);
        } catch (IOException e) {
            cleanConnection();
        }
    }

    public void setColumns(LogColumn[] columnDefs) {
        if (columnDefs.length == 0) return;
        if (columnDefs.length > 255) return;
        if (this.setColumnsCount > 1 && logmodeState == LMSTATE_CACHE) throw new UnsupportedOperationException("already called");
        LogColumn[] tempColumnDefs = new LogColumn[columnDefs.length + 1];
        tempColumnDefs[0] = new LogColumn("milliseconds", LogColumn.DT_INTEGER, 1);
        System.arraycopy(columnDefs, 0, tempColumnDefs, 1, columnDefs.length);
        this.columnDefs = tempColumnDefs;
        this.setColumnsCount++;
        this.itemsPerLine = (byte) (this.columnDefs.length & 0xff);
        if (logmodeState == LMSTATE_REAL) sendHeaders();
        lineBytes = 0;
        for (int i = 0; i < this.columnDefs.length; i++) {
            lineBytes += this.columnDefs[i].getSize();
        }
    }

    private void sendHeaders() {
        this.currColumnPosition = 1;
        byte[] command = { COMMAND_SETHEADERS, 0 };
        command[1] = (byte) (this.columnDefs.length & 0xff);
        sendATTN();
        sendCommand(command);
        for (LogColumn item : columnDefs) {
            writeStringData(item.getName().replace('!', ' ') + "!" + (item.isChartable() ? "y" : "n") + "!" + item.getRangeAxisID());
        }
    }
}
