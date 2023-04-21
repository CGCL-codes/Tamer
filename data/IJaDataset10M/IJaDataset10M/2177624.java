package com.continuent.tungsten.replicator.thl.log;

import java.io.IOException;
import java.sql.Timestamp;
import org.apache.log4j.Logger;
import com.continuent.tungsten.replicator.ReplicatorException;
import com.continuent.tungsten.replicator.thl.THLEvent;
import com.continuent.tungsten.replicator.thl.THLException;
import com.continuent.tungsten.replicator.thl.serializer.Serializer;

/**
 * Implements client operations on the log. Each individual client of the log
 * must instantiate a separate connection. The client must be released after use
 * to avoid resource leaks.
 * 
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class LogConnection {

    private static Logger logger = Logger.getLogger(LogConnection.class);

    /**
     * Simple representing base seqno of uninitialized log.
     */
    public static long UNINITIALIZED = -1;

    /**
     * Symbol representing the first seqno in a new log.
     */
    public static long FIRST = 0;

    private final boolean readonly;

    private volatile boolean done = false;

    private DiskLog diskLog;

    private LogCursor cursor;

    private THLEvent pendingEvent;

    private long pendingSeqno;

    private long writeCount = 0;

    private long readCount = 0;

    private boolean doChecksum;

    private Serializer eventSerializer;

    private int logFileSize;

    private int timeoutMillis;

    private int logRotateMillis;

    private LogEventReadFilter readFilter;

    /**
     * Instantiates a client on a disk log.
     * 
     * @param disklog Disk log we are accessing
     * @param readonly If true, this client may not write
     */
    LogConnection(DiskLog diskLog, boolean readonly) {
        this.diskLog = diskLog;
        this.readonly = readonly;
        this.eventSerializer = diskLog.getEventSerializer();
        this.doChecksum = diskLog.isDoChecksum();
        this.timeoutMillis = diskLog.getTimeoutMillis();
        this.logRotateMillis = diskLog.getLogRotateMillis();
        if (!readonly) {
            this.logFileSize = diskLog.getLogFileSize();
        }
    }

    /**
     * Returns true if this is a read-only client.
     */
    public boolean isReadonly() {
        return readonly;
    }

    /**
     * Sets the read filter, which determines whether events are fully
     * deserialized on read. This implements query logic on scanned events.
     */
    public void setReadFilter(LogEventReadFilter readFilter) {
        this.readFilter = readFilter;
    }

    /**
     * Sets the timeout in milliseconds for blocking reads on this connection.
     * The value overrides the read timeout for the log as a whole.
     */
    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    /**
     * Sets the local connection value for reading a new file after a log
     * rotation.
     */
    public void setLogRotateMillis(int logRotateMillis) {
        this.logRotateMillis = logRotateMillis;
    }

    /**
     * Releases the client connection. This must be called to avoid resource
     * leaks.
     */
    public void release() {
        if (diskLog != null) diskLog.release(this);
    }

    /**
     * Releases the client connection. This must be called to avoid resource
     * leaks.
     */
    public synchronized void releaseInternal() {
        if (!done) {
            if (cursor != null) {
                cursor.release();
                cursor = null;
            }
            diskLog = null;
            done = true;
        }
    }

    /**
     * Returns true if connection is no longer in use.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Finds a specific THLEvent and position client cursor on the event. The
     * event in question may be past the end of the current log, in which case
     * we position at the end. It is not possible to seek on an event that is
     * before the beginning of the log.
     * 
     * @param seqno Desired sequence number
     * @param fragno Desired fragment
     * @return True if seek is successful and next() may be called; false if
     *         event does not exist, i.e., is before the beginning of the log
     * @throws ReplicatorException thrown if log cannot be read
     */
    public synchronized boolean seek(long seqno, short fragno) throws ReplicatorException, InterruptedException {
        assertNotDone();
        if (cursor != null) {
            cursor.release();
            cursor = null;
        }
        pendingEvent = null;
        pendingSeqno = UNINITIALIZED;
        LogFile logFile = diskLog.getLogFile(seqno);
        if (logFile == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Log is uninitialized and does not contain seqno: seqno=" + seqno);
            }
            return false;
        }
        logFile.openRead();
        cursor = new LogCursor(logFile, seqno);
        cursor.setRotateNext(true);
        if (logger.isDebugEnabled()) {
            logger.debug("Using log file " + logFile.getFile().getName() + " - seeking event " + seqno + "/" + fragno);
        }
        if (seqno == FIRST) {
            if (logger.isDebugEnabled()) {
                logger.debug("Seeking seqno in newly initialized log: seqno=" + seqno);
            }
            pendingSeqno = seqno;
            return true;
        }
        long lastSeqno = logFile.getBaseSeqno();
        while (true) {
            try {
                LogRecord logRecord = logFile.readRecord(0);
                if (logRecord.isEmpty()) {
                    if (lastSeqno == UNINITIALIZED && lastSeqno < 0) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Seeking seqno in newly initialized log: seqno=" + seqno);
                        }
                        pendingSeqno = seqno;
                        return true;
                    } else if (seqno > lastSeqno) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Seeking seqno past end of log: seqno=" + seqno + " end seqno=" + lastSeqno);
                        }
                        pendingSeqno = seqno;
                        return true;
                    } else break;
                }
                byte[] bytes = logRecord.getData();
                byte recordType = bytes[0];
                if (recordType == LogRecord.EVENT_REPL) {
                    LogEventReplReader eventReader = new LogEventReplReader(logRecord, eventSerializer, doChecksum);
                    if (eventReader.getSeqno() == seqno && eventReader.getFragno() == fragno) {
                        if (logger.isDebugEnabled()) logger.debug("Found requested event (" + seqno + "/" + fragno + ")");
                        pendingEvent = deserialize(logRecord);
                        break;
                    } else if (eventReader.getSeqno() > seqno || (eventReader.getSeqno() == seqno && eventReader.getFragno() > fragno)) {
                        if (logger.isDebugEnabled()) logger.debug("Requested event (" + seqno + "/" + fragno + ") not found. Found event " + eventReader.getSeqno() + "/" + eventReader.getFragno() + " instead");
                        break;
                    } else {
                        if (logger.isDebugEnabled()) logger.debug("Requested event (" + seqno + "/" + fragno + ") not reached. Current position " + eventReader.getSeqno() + "/" + eventReader.getFragno());
                        lastSeqno = eventReader.getSeqno();
                    }
                } else if (recordType == LogRecord.EVENT_ROTATE) {
                    break;
                } else {
                    throw new THLException("Unable to extract a valid record type; log appears to be corrupted: file=" + logFile.getFile().getName() + " offset=" + logRecord.getOffset() + " record type=" + recordType);
                }
            } catch (IOException e) {
                throw new THLException("Failed to extract event from log", e);
            }
        }
        return (pendingEvent != null);
    }

    private THLEvent deserialize(LogRecord logRecord) throws ReplicatorException {
        LogEventReplReader eventReader = new LogEventReplReader(logRecord, eventSerializer, doChecksum);
        THLEvent event;
        if (readFilter == null || readFilter.accept(eventReader)) {
            event = eventReader.deserializeEvent();
        } else {
            event = new THLEvent(eventReader.getSeqno(), eventReader.getFragno(), eventReader.isLastFrag(), eventReader.getSourceId(), THLEvent.REPL_DBMS_EVENT, eventReader.getEpochNumber(), new Timestamp(System.currentTimeMillis()), new Timestamp(eventReader.getSourceTStamp()), eventReader.getEventId(), eventReader.getShardId(), null);
        }
        eventReader.done();
        return event;
    }

    /**
     * Positions cursor on first fragment of a specific event.
     * 
     * @param seqno Desired sequence number
     * @return True if seek is successful and next() may be called; false if
     *         event does not exist
     * @throws ReplicatorException thrown if log cannot be read
     */
    public synchronized boolean seek(long seqno) throws ReplicatorException, InterruptedException {
        return seek(seqno, (short) 0);
    }

    /**
     * Opens a log file and positions client cursor on the event. Clients may
     * call next to read events.
     * 
     * @param name The short name of a current log file
     * @return True if seek is successful and next() may be called
     * @throws ReplicatorException Thrown if the log cannot be read
     * @throws IOException Thrown if file cannot be found
     * @throws InterruptedException
     */
    public synchronized boolean seek(String name) throws ReplicatorException, IOException, InterruptedException {
        assertNotDone();
        clearReadState();
        LogFile logFile = diskLog.getLogFile(name);
        if (logFile == null) return false; else {
            logFile.openRead();
            cursor = new LogCursor(logFile, logFile.getBaseSeqno());
            cursor.setRotateNext(false);
            if (logger.isDebugEnabled()) {
                logger.debug("Using log file for read: " + logFile.getFile().getName());
            }
            pendingSeqno = logFile.getBaseSeqno();
            return true;
        }
    }

    private void clearReadState() {
        if (cursor != null) {
            cursor.release();
            cursor = null;
        }
        if (pendingEvent != null) pendingEvent = null;
    }

    /**
     * Returns the next event in the log. If blocking is enabled, this will wait
     * for a new event to arrive. If disabled, this call returns immediately if
     * there is no next event. This method never returns an event with a seqno
     * earlier than the client requested. If clients call next() after seeking
     * past the end of the log, we therefore return the event corresponding to
     * the seek() call or nothing.
     * 
     * @param block If true, read blocks until next event is available
     * @return A THLEvent or null if we are non-blocking
     */
    public synchronized THLEvent next(boolean block) throws ReplicatorException, InterruptedException {
        assertNotDone();
        if (cursor == null) {
            throw new THLException("Must seek before attempting to read next event");
        }
        if (pendingEvent != null) {
            THLEvent event = pendingEvent;
            pendingEvent = null;
            readCount++;
            return event;
        }
        LogFile data = cursor.getLogFile();
        if (logger.isDebugEnabled()) {
            logger.debug("Using log file " + data.getFile().getName() + " - reading event");
        }
        int readTimeoutMillis = 0;
        if (block) readTimeoutMillis = timeoutMillis;
        THLEvent event = null;
        while (event == null) {
            try {
                LogRecord logRecord = data.readRecord(readTimeoutMillis);
                if (logRecord.isEmpty()) {
                    return null;
                }
                byte[] bytes = logRecord.getData();
                byte recordType = bytes[0];
                if (recordType == LogRecord.EVENT_REPL) {
                    event = deserialize(logRecord);
                    if (event.getSeqno() < this.pendingSeqno) {
                        event = null;
                        continue;
                    } else {
                        break;
                    }
                } else if (recordType == LogRecord.EVENT_ROTATE) {
                    if (logger.isDebugEnabled()) logger.debug("Found a rotate event: file=" + data.getFile().getName() + " offset=" + logRecord.getOffset());
                    if (!cursor.isRotateNext()) return null;
                    LogEventRotateReader rotateReader = new LogEventRotateReader(logRecord, doChecksum);
                    String newFileName = diskLog.getDataFileName(rotateReader.getIndex());
                    cursor.release();
                    int rotationTimeout = logRotateMillis;
                    while (rotationTimeout > 0) {
                        data = diskLog.getLogFileForReading(newFileName);
                        if (data != null) break;
                        if (data == null && !block) {
                            return null;
                        }
                        long startSleepMillis = System.currentTimeMillis();
                        Thread.sleep(50);
                        long sleepMillis = System.currentTimeMillis() - startSleepMillis;
                        rotationTimeout -= sleepMillis;
                        if (rotationTimeout <= 0) throw new LogTimeoutException("Read timed out while waiting for rotated log file; " + "this may indicate log corruption: missing file=" + newFileName);
                    }
                    cursor = new LogCursor(data, -1);
                    cursor.setRotateNext(true);
                } else {
                    throw new THLException("Unable to extract a valid record type; log appears to be corrupted: file=" + data.getFile().getName() + " offset=" + logRecord.getOffset() + " record type=" + recordType);
                }
            } catch (IOException e) {
                throw new THLException("Failed to extract event from log", e);
            }
        }
        if (pendingSeqno != UNINITIALIZED) {
            if (event.getSeqno() != pendingSeqno) throw new LogPositionException("Log seek failure: expected seqno=" + pendingSeqno + " found seqno=" + event.getSeqno());
            pendingSeqno = UNINITIALIZED;
        }
        readCount++;
        return event;
    }

    /**
     * Convenience method to return the next event with blocking enabled.
     * 
     * @return A THLEvent or null if we are non-blocking
     */
    public synchronized THLEvent next() throws ReplicatorException, InterruptedException {
        return next(true);
    }

    /**
     * Store a THL event at the end of the log.
     * 
     * @param event THLEvent to store
     * @param commit If true, flush to storage
     */
    public synchronized void store(THLEvent event, boolean commit) throws ReplicatorException, InterruptedException {
        assertWritable();
        if (this.cursor == null) {
            try {
                LogFile lastFile = diskLog.openLastFile(false);
                cursor = new LogCursor(lastFile, event.getSeqno());
                if (logger.isDebugEnabled()) {
                    logger.debug("Creating new log cursor: thread=" + Thread.currentThread() + " file=" + lastFile.getFile().getName() + " seqno=" + event.getSeqno());
                }
            } catch (ReplicatorException e) {
                throw new THLException("Failed to open log last log file", e);
            }
        }
        LogFile dataFile = cursor.getLogFile();
        if (logger.isDebugEnabled()) {
            logger.debug("Using log file for writing: " + dataFile.getFile().getName());
        }
        try {
            if (dataFile.getLength() > logFileSize && event.getFragno() == 0) {
                dataFile = diskLog.rotate(dataFile, event.getSeqno());
                cursor.release();
                cursor = new LogCursor(dataFile, event.getSeqno());
            }
            LogEventReplWriter eventWriter = new LogEventReplWriter(event, eventSerializer, doChecksum);
            LogRecord logRecord = eventWriter.write();
            dataFile.writeRecord(logRecord, logFileSize);
            diskLog.setMaxSeqno(event.getSeqno());
            writeCount++;
            if (commit) {
                dataFile.flush();
            }
        } catch (IOException e) {
            throw new THLException("Error while writing to log file: name=" + dataFile.getFile().getName(), e);
        }
    }

    /**
     * Commit transactions stored in the log.
     */
    public synchronized void commit() throws ReplicatorException, InterruptedException {
        assertWritable();
        if (cursor != null) {
            LogFile dataFile = cursor.getLogFile();
            try {
                dataFile.flush();
            } catch (IOException e) {
                throw new THLException("Commit failed on log: seqno=" + cursor.getLastSeqno() + " log file=" + dataFile.getFile().getName());
            }
            diskLog.checkLogSyncTask();
        }
    }

    /**
     * Rollback transactions stored in the log.
     */
    public synchronized void rollback() throws ReplicatorException {
        assertWritable();
    }

    /**
     * Delete a range of events from the log.
     */
    public synchronized void delete(Long low, Long high) throws ReplicatorException, InterruptedException {
        assertWritable();
        diskLog.delete(this, low, high);
    }

    private void assertWritable() throws ReplicatorException {
        assertNotDone();
        if (readonly) {
            throw new THLException("Attempt to write using read-only log connection");
        }
        if (!diskLog.isWritable()) {
            throw new THLException("Attempt to write using read-only log connection");
        }
    }

    private void assertNotDone() throws ReplicatorException {
        if (done) throw new THLException("Attempt to use released connection");
    }
}
