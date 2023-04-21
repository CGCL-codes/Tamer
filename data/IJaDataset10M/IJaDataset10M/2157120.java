package org.job.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.job.JobException;
import org.job.JobIterator;

/**
 * This class defines an abstraction layer for JOB stream instances used in JOB
 * sessions. It is subclassed by server streams and client streams. JOB streams
 * are created by JOB connections and are sessions factories.
 * @author Michael Watzek
 */
abstract class JobAbstractConnectionStream extends JobObjectStream {

    static final int protocolId = 36612;

    static final int success = 0;

    static final int warning = 1;

    static final int error = 2;

    static final int exception = 3;

    static final int sessionTimeout = 4;

    static final int initClientConnection = 100;

    static final int shutdown = 101;

    static final int createSession = 110;

    static final int deleteSession = 111;

    static final int reconnectSession = 112;

    static final int getSessionsInfo = 113;

    static final int beginTransaction = 120;

    static final int endTransaction = 121;

    static final int abortTransaction = 122;

    static final int newJobSpace = 130;

    static final int deleteJobSpace = 131;

    static final int getJobSpaceName = 132;

    static final int getJobSpaceId = 133;

    static final int getDefaultJobSpaceId = 134;

    static final int createJobMap = 150;

    static final int deleteJobMap = 151;

    static final int putIntoJobMap = 152;

    static final int removeFromJobMap = 153;

    static final int getFromJobMap = 154;

    static final int getFirstFromJobMap = 155;

    static final int getLastFromJobMap = 156;

    static final int getNextFromJobMap = 157;

    static final int getPrevFromJobMap = 158;

    static final int getSizeOfJobMap = 159;

    static final int getInitialCapacityOfJobMap = 160;

    static final int getExtentCapacityOfJobMap = 161;

    static final int getNameOfJobMap = 162;

    static final int getFirstBunchFromJobMap = 163;

    static final int getNextBunchFromJobMap = 164;

    static final int newInJobMap = 165;

    static final int getLastBunchFromJobMap = 166;

    static final int getPrevBunchFromJobMap = 167;

    static final int getFirstFromJobSortedMap = 168;

    static final int getLastFromJobSortedMap = 169;

    static final int putIntoJobSortedMap = 180;

    static final int removeFromJobSortedMap = 181;

    static final int getFromJobSortedMap = 182;

    static final int getNextFromJobSortedMap = 183;

    static final int getPrevFromJobSortedMap = 184;

    static final int getSizeOfJobSortedMap = 185;

    static final int getNextBunchFromJobSortedMap = 186;

    static final int newInJobSortedMap = 187;

    static final int getPrevBunchFromJobSortedMap = 188;

    static final int getFirstBunchFromJobSortedMap = 189;

    static final int getLastBunchFromJobSortedMap = 190;

    static final int createJobKeyMap = 200;

    static final int deleteJobKeyMap = 201;

    static final int newInJobKeyMap = 202;

    static final int putIntoJobKeyMap = 203;

    static final int removeFromJobKeyMap = 204;

    static final int getFromJobKeyMap = 205;

    static final int getFirstFromJobKeyMap = 206;

    static final int getLastFromJobKeyMap = 207;

    static final int getNextFromJobKeyMap = 208;

    static final int getPrevFromJobKeyMap = 209;

    static final int getSizeOfJobKeyMap = 210;

    static final int getInitialCapacityOfJobKeyMap = 211;

    static final int getExtentCapacityOfJobKeyMap = 212;

    static final int getNameOfJobKeyMap = 213;

    static final int getSizeOfSlot = 214;

    static final int getJobKeyMapId = 215;

    static final int getFirstBunchFromJobKeyMap = 216;

    static final int getNextBunchFromJobKeyMap = 217;

    static final int getLastBunchFromJobKeyMap = 218;

    static final int getPrevBunchFromJobKeyMap = 219;

    static final int createJobLOB = 230;

    static final int deleteJobLOB = 231;

    static final int putIntoJobLOB = 232;

    static final int getFromJobLOB = 233;

    static final int getSizeOfJobLOB = 234;

    static final int getInitialCapacityOfJobLOB = 235;

    static final int getExtentCapacityOfJobLOB = 236;

    static final int getNameOfJobLOB = 237;

    private static final Map opCodes = new HashMap();

    static {
        opCodes.put(new Integer(success), "success");
        opCodes.put(new Integer(warning), "warning");
        opCodes.put(new Integer(error), "error");
        opCodes.put(new Integer(exception), "exception");
        opCodes.put(new Integer(sessionTimeout), "sessionTimeout");
        opCodes.put(new Integer(initClientConnection), "initClientConnection");
        opCodes.put(new Integer(shutdown), "shutdown");
        opCodes.put(new Integer(createSession), "createSession");
        opCodes.put(new Integer(deleteSession), "deleteSession");
        opCodes.put(new Integer(reconnectSession), "reconnectSessio");
        opCodes.put(new Integer(getSessionsInfo), "getSessionInfo");
        opCodes.put(new Integer(beginTransaction), "beginTransaction");
        opCodes.put(new Integer(endTransaction), "endTransaction");
        opCodes.put(new Integer(abortTransaction), "abortTransaction");
        opCodes.put(new Integer(newJobSpace), "newJobSpace");
        opCodes.put(new Integer(deleteJobSpace), "deleteJobSpace");
        opCodes.put(new Integer(getJobSpaceName), "getJobSpaceName");
        opCodes.put(new Integer(getJobSpaceId), "getJobSpaceId");
        opCodes.put(new Integer(getDefaultJobSpaceId), "getDefaultJobSpace");
        opCodes.put(new Integer(createJobMap), "createJobMap");
        opCodes.put(new Integer(deleteJobMap), "deleteJobMap");
        opCodes.put(new Integer(putIntoJobMap), "putIntoJobMap");
        opCodes.put(new Integer(removeFromJobMap), "removeFromJobMap");
        opCodes.put(new Integer(getFromJobMap), "getFromJobMap");
        opCodes.put(new Integer(getFirstFromJobMap), "getFirstFromJobMap");
        opCodes.put(new Integer(getLastFromJobMap), "getLastFromJobMap");
        opCodes.put(new Integer(getNextFromJobMap), "getNextFromJobMap");
        opCodes.put(new Integer(getPrevFromJobMap), "getPrevFromJobMap");
        opCodes.put(new Integer(getSizeOfJobMap), "getSizeOfJobMap");
        opCodes.put(new Integer(getInitialCapacityOfJobMap), "getInitialCapacityOfJobMap");
        opCodes.put(new Integer(getExtentCapacityOfJobMap), "getExtentCapacityOfJobMap");
        opCodes.put(new Integer(getNameOfJobMap), "getNameOfJobMap");
        opCodes.put(new Integer(getFirstBunchFromJobMap), "getFirstBunchFromJobMap");
        opCodes.put(new Integer(getNextBunchFromJobMap), "getNextBunchFromJobMap");
        opCodes.put(new Integer(newInJobMap), "newInJobMap");
        opCodes.put(new Integer(getLastBunchFromJobMap), "getLastBunchFromJobMap");
        opCodes.put(new Integer(getPrevBunchFromJobMap), "getPrevBunchFromJobMap");
        opCodes.put(new Integer(getFirstFromJobSortedMap), "getFirstFromJobSortedMap");
        opCodes.put(new Integer(getLastFromJobSortedMap), "getLastFromJobSortedMap");
        opCodes.put(new Integer(putIntoJobSortedMap), "putIntoJobSortedMap");
        opCodes.put(new Integer(removeFromJobSortedMap), "removeFromJobSortedMap");
        opCodes.put(new Integer(getFromJobSortedMap), "getFromJobSortedMap");
        opCodes.put(new Integer(getNextFromJobSortedMap), "getNextFromJobSortedMap");
        opCodes.put(new Integer(getPrevFromJobSortedMap), "getPrevFromJobSortedMap");
        opCodes.put(new Integer(getSizeOfJobSortedMap), "getSizeOfJobSortedMap");
        opCodes.put(new Integer(getNextBunchFromJobSortedMap), "getNextBunchFromJobSortedMap");
        opCodes.put(new Integer(newInJobSortedMap), "newInJobSortedMap");
        opCodes.put(new Integer(getPrevBunchFromJobSortedMap), "getPrevBunchFromJobSortedMap");
        opCodes.put(new Integer(getFirstBunchFromJobSortedMap), "getFirstBunchFromJobSortedMap");
        opCodes.put(new Integer(getLastBunchFromJobSortedMap), "getLastBunchFromJobSortedMap");
        opCodes.put(new Integer(createJobKeyMap), "createJobKeyMap");
        opCodes.put(new Integer(deleteJobKeyMap), "deleteJobKeyMap");
        opCodes.put(new Integer(newInJobKeyMap), "newInJobKeyMap");
        opCodes.put(new Integer(putIntoJobKeyMap), "putIntoJobKeyMap");
        opCodes.put(new Integer(removeFromJobKeyMap), "removeFromJobKeyMap");
        opCodes.put(new Integer(getFromJobKeyMap), "getFromJobKeyMap");
        opCodes.put(new Integer(getFirstFromJobKeyMap), "getFirstFromJobKeyMap");
        opCodes.put(new Integer(getLastFromJobKeyMap), "getLastFromJobKeyMap");
        opCodes.put(new Integer(getNextFromJobKeyMap), "getNextFromJobKeyMap");
        opCodes.put(new Integer(getPrevFromJobKeyMap), "getPrevFromJobKeyMap");
        opCodes.put(new Integer(getSizeOfJobKeyMap), "getSizeOfJobKeyMap");
        opCodes.put(new Integer(getInitialCapacityOfJobKeyMap), "getInitialCapacityOfJobKeyMap");
        opCodes.put(new Integer(getExtentCapacityOfJobKeyMap), "getExtentCapacityOfJobKeyMap");
        opCodes.put(new Integer(getNameOfJobKeyMap), "getNameOfJobKeyMap");
        opCodes.put(new Integer(getSizeOfSlot), "getSizeOfSlot");
        opCodes.put(new Integer(getJobKeyMapId), "getJobKeyMapId");
        opCodes.put(new Integer(getFirstBunchFromJobKeyMap), "getFirstBunchFromJobKeyMap");
        opCodes.put(new Integer(getNextBunchFromJobKeyMap), "getNextBunchFromJobKeyMap");
        opCodes.put(new Integer(getLastBunchFromJobKeyMap), "getLastBunchFromJobKeyMap");
        opCodes.put(new Integer(getPrevBunchFromJobKeyMap), "getPrevBunchFromJobKeyMap");
        opCodes.put(new Integer(createJobLOB), "createJobLOB");
        opCodes.put(new Integer(deleteJobLOB), "deleteJobLOB");
        opCodes.put(new Integer(putIntoJobLOB), "putIntoJobLOB");
        opCodes.put(new Integer(getFromJobLOB), "getFromJobLOB");
        opCodes.put(new Integer(getSizeOfJobLOB), "getSizeOfJobLOB");
        opCodes.put(new Integer(getInitialCapacityOfJobLOB), "getInitialCapacityOfJobLOB");
        opCodes.put(new Integer(getExtentCapacityOfJobLOB), "getExtentCapacityOfJobLOB");
        opCodes.put(new Integer(getNameOfJobLOB), "getNameOfJobLOB");
    }

    static String opCodeToString(int opCode) {
        return (String) opCodes.get(new Integer(opCode));
    }

    JobAbstractConnection connection;

    /**
     * Creates a JOB abstract stream for given connection.
     * @param connection the connection
     */
    JobAbstractConnectionStream(final JobAbstractConnection connection) {
        super(connection.classLoader, connection.streamBufferSize);
        this.connection = connection;
    }

    /**
     * Closes this stream.
     * @throws IOException
     */
    public void close() throws IOException {
        removeSession();
        super.close();
    }

    void handleException(final Exception e) throws JobException {
        handleException(e, null);
    }

    void handleException(final Exception e, Object[] arguments) throws JobException {
        handleException(e, arguments, false);
    }

    void handleException(final Exception e, final boolean resetStream) throws JobException {
        handleException(e, null, resetStream);
    }

    void handleException(final Exception e, Object[] arguments, final boolean resetStream) throws JobException {
        if (resetStream) {
            reset();
        }
        JobException jobException;
        if (e instanceof JobDuplicateKeyException) {
            String message = e.getMessage();
            String newMessage = JobMessageFactory.newMessage(message, arguments);
            jobException = new org.job.JobDuplicateKeyException(newMessage, e);
        } else if (e instanceof JobKernelException) {
            JobKernelException jobKernelException = (JobKernelException) e;
            jobKernelException.setMessageArguments(arguments);
            jobException = jobKernelException;
        } else {
            String message = e.getMessage();
            String newMessage = JobMessageFactory.newMessage(message, arguments);
            jobException = new JobException(newMessage, e);
        }
        throw jobException;
    }

    /**
     * Puts back the session of this stream into the session pool.
     */
    void poolSession() {
        this.connection.poolSession(this.session);
    }

    /**
     * Invalidates all pooled sessions. Subclasses must additionally shut down
     * JOB server in two tier mode and factory in one tier mode.
     */
    void shutdown() {
        this.connection.invalidateSessionPool();
    }

    /**
     * Creates a session instance for the specified properties.
     * @param user the name of a valid user
     * @param password the password associated with user
     * @param isolationLevel the default isolation level for the session, one of
     *            <LI><code>JobSession.SERIALIZABLE</code>
     *            <LI><code>JobSession.REPEATABLE_READ</code>
     *            <LI><code>JobSession.COMMITED_READ</code>
     *            <LI><code>JobSession.OVERWRITE_COMMITED_CHANGES</code>
     *            <LI><code>JobSession.DIRTY_READ</code>
     * @param optimistic if <code>true</code> the default transaction mode for
     *            the session is optimistic
     * @param encoding the string encoding
     * @param multiThreaded if <code>true</code> this session instance may be
     *            processed concurrently by several threads
     * @param isTransactional if <code>true</code> JOB maps instantiated by
     *            this session have transactional caches
     * @param nonTransactionalCache the default non transactional cache type for
     *            JOB maps instantiated by this session
     * @param useJavaUtilMapSemantics if <code>true</code> then this session
     *            uses JOB maps implementing <code>java.util.Map</code>
     *            semantics for methods <code>put</code> and
     *            <code>remove</code>
     * @param sessionPoolType the sessionPoolType
     * @return the session instance, or <code>null</code> if user cannot be
     *         found
     * @exception JobException if this operation is not successful
     */
    abstract JobSession newSession(String user, byte[] password, int isolationLevel, boolean optimistic, String encoding, boolean multiThreaded, boolean isTransactional, String nonTransactionalCache, boolean useJavaUtilMapSemantics, String sessionPoolType);

    /**
     * Releases resources for this stream's session.
     */
    abstract boolean removeSession();

    /**
     * Returns a new JOB space for the specified session id, JOB space id, name,
     * and size.
     * @param sessionId the session id
     * @param jobSpaceId the JOB space id
     * @param name the JOB space name
     * @param size the JOB space size
     * @return the JOB space id
     */
    abstract int newJobSpace(int sessionId, int jobSpaceId, String name, int size);

    /**
     * Deletes the JOB space having the specified name.
     * @param sessionId the session id
     * @param name the JOB space name
     * @return <code>true</code> if the JOB space has been deleted
     */
    abstract boolean deleteJobSpace(int sessionId, String name);

    /**
     * Returns the name for the specified JOB space id.
     * @param sessionId the session id
     * @param jobSpaceId the JOB space id
     * @return the JOB space name
     */
    abstract String getJobSpaceName(int sessionId, int jobSpaceId);

    /**
     * Returns the JOB space id for the specified name.
     * @param sessionId the session id
     * @param name the JOB space name
     * @return the JOB space id
     */
    abstract int getJobSpaceId(int sessionId, String name);

    /**
     * Returns the id of the default JOB space.
     * @return the JOB space id
     */
    abstract int getDefaultJobSpaceId();

    /**
     * Begins a transaction for the specified session.
     * @param sessionId the session id
     * @param isolationLevel the isolationLevel
     */
    abstract void beginTransaction(int sessionId, int isolationLevel);

    /**
     * Ends a transaction for the specified session.
     * @param sessionId the session id
     */
    abstract void endTransaction(int sessionId);

    /**
     * Aborts a transaction for the specified session.
     * @param sessionId the session id
     */
    abstract void abortTransaction(int sessionId);

    /**
     * Returns an object for argument <code>key</code> from yout JOB instance.
     * That object is meant to be put into argument <code>map</code>. If
     * argument <code>value</code> does not equal <code>null</code>, then
     * argument <code>value</code> may be returned. The rationale is to
     * refresh argument <code>value</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @return the value for argument <code>key</code>
     */
    abstract Object get(JobMap map, int sessionId, int id, Object key, Object value);

    /**
     * Returns an object for argument <code>key</code> from JOB. That object
     * is meant to be put into argument <code>map</code>. If argument
     * <code>value</code> does not equal <code>null</code>, then argument
     * <code>value</code> may be returned. The rationale is to refresh
     * argument <code>value</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @return the value for argument <code>key</code>
     */
    abstract Object get(JobSortedMap map, int sessionId, int id, Object key, Object value);

    /**
     * Returns an object for argument <code>key</code> from JOB. That object
     * is meant to be put into argument <code>map</code>. If argument
     * <code>value</code> does not equal <code>null</code>, then argument
     * <code>value</code> may be returned. The rationale is to refresh
     * argument <code>value</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @return the value for argument <code>key</code>
     */
    abstract Object get(JobKeyMap map, int sessionId, int id, JobKey key, Object value);

    /**
     * Writes bytes fetched from JOB into the array of the given
     * <code>lob</code>. Bytes are written beginning at position
     * <code>offset</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     * @param offset the offset where to start writing bytes
     * @return the number of written bytes
     */
    abstract int get(JobLOB lob, int sessionId, int id, long offset);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair. If there is an entry
     * for the given key, then an instance of <code>JobMessageFactory</code>
     * is thrown.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param flush if <code>true</code> then parameters are flushed
     */
    abstract void newEntry(JobMap map, int sessionId, int id, Object key, Object value, boolean flush);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair. If there is an entry
     * for the given key, then an instance of <code>JobMessageFactory</code>
     * is thrown.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param flush if <code>true</code> then parameters are flushed
     */
    abstract void newEntry(JobSortedMap map, int sessionId, int id, Object key, Object value, boolean flush);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair. If there is an entry
     * for the given key, then an instance of <code>JobMessageFactory</code>
     * is thrown.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param flush if <code>true</code> then parameters are flushed
     */
    abstract void newEntry(JobKeyMap map, int sessionId, int id, JobKey key, Object value, boolean flush);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param replaced the instance to be replaced by argument
     *            <code>value</code>
     * @param flush if <code>true</code> then parameters are flushed
     * @return the replaced value
     */
    abstract Object put(JobMap map, int sessionId, int id, Object key, Object value, Object replaced, boolean flush);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param replaced the instance to be replaced by argument
     *            <code>value</code>
     * @param flush if <code>true</code> then parameters are flushed
     * @return the replaced value
     */
    abstract Object put(JobSortedMap map, int sessionId, int id, Object key, Object value, Object replaced, boolean flush);

    /**
     * Puts the pair of arguments <code>key</code>, <code>value</code> into
     * JOB. Argument <code>map</code> holds this pair.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param value the value for argument <code>key</code>
     * @param replaced the instance to be replaced by argument
     *            <code>value</code>
     * @param flush if <code>true</code> then parameters are flushed
     * @return the replaced value
     */
    abstract Object put(JobKeyMap map, int sessionId, int id, JobKey key, Object value, Object replaced, boolean flush);

    /**
     * Puts the array of argument <code>lob</code> into JOB. The array is
     * written starting at position <code>offset</code>. This method writes
     * bytes until an <code>EOFException</code> is thrown. The exception is
     * caught and the number of written bytes is returned.
     * @param lob the lob
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param offset the offset to the first byte written
     * @return the number of written bytes
     */
    abstract int put(JobLOB lob, int sessionId, int id, long offset);

    /**
     * Removes a value from JOB associated with argument <code>key</code>.
     * Argument <code>map</code> holds argument <code>key</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param flush if <code>true</code> then parameters are flushed
     * @return the removed value
     */
    abstract Object remove(JobMap map, int sessionId, int id, Object key, boolean flush);

    /**
     * Removes a value from JOB associated with argument <code>key</code>.
     * Argument <code>map</code> holds argument <code>key</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param flush if <code>true</code> then parameters are flushed
     * @return the removed value
     */
    abstract Object remove(JobSortedMap map, int sessionId, int id, Object key, boolean flush);

    /**
     * Removes a value from JOB associated with argument <code>key</code>.
     * Argument <code>map</code> holds argument <code>key</code>.
     * @param map the map
     * @param sessionId the sesion id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key for the returned value
     * @param flush if <code>true</code> then parameters are flushed
     * @return the removed value
     */
    abstract Object remove(JobKeyMap map, int sessionId, int id, JobKey key, boolean flush);

    /**
     * Returns the first entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first entry
     */
    abstract JobMap.JobMapEntry getFirst(JobMap map, int sessionId, int id);

    /**
     * Returns the first entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first entry
     */
    abstract JobMap.JobMapEntry getFirst(JobSortedMap map, int sessionId, int id);

    /**
     * Returns the first entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first entry
     */
    abstract JobMap.JobMapEntry getFirst(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the last entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last entry
     */
    abstract JobMap.JobMapEntry getLast(JobMap map, int sessionId, int id);

    /**
     * Returns the last entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last entry
     */
    abstract JobMap.JobMapEntry getLast(JobSortedMap map, int sessionId, int id);

    /**
     * Returns the last entry of argument <code>map</code> from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last entry
     */
    abstract JobMap.JobMapEntry getLast(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the next entry of argument <code>map</code> from JOB. The next
     * entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the previous entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the next entry
     */
    abstract JobMap.JobMapEntry getNext(JobMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the next entry of argument <code>map</code> from JOB. The next
     * entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the previous entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the next entry
     */
    abstract JobMap.JobMapEntry getNext(JobSortedMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the next entry of argument <code>map</code> from JOB. The next
     * entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the previous entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the next entry
     */
    abstract JobMap.JobMapEntry getNext(JobKeyMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the previous entry of argument <code>map</code> from JOB. The
     * previous entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the next entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the previous entry
     */
    abstract JobMap.JobMapEntry getPrev(JobMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the previous entry of argument <code>map</code> from JOB. The
     * previous entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the next entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the previous entry
     */
    abstract JobMap.JobMapEntry getPrev(JobSortedMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the previous entry of argument <code>map</code> from JOB. The
     * previous entry depends on argument <code>entry</code>. Argument
     * <code>createNewEntry</code> determines, if a new entry is returned, or
     * if argument <code>entry</code> is returned.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param entry the next entry related to the returned entry
     * @param createNewEntry determines, if a new entry is returned, or if
     *            argument <code>entry</code> is returned.
     * @return the previous entry
     */
    abstract JobMap.JobMapEntry getPrev(JobKeyMap map, int sessionId, int id, JobMap.JobMapEntry entry, boolean createNewEntry);

    /**
     * Returns the size for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the size
     */
    abstract int size(JobMap map, int sessionId, int id);

    /**
     * Returns the size for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the size
     */
    abstract int size(JobSortedMap map, int sessionId, int id);

    /**
     * Returns the size for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the size
     */
    abstract int size(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the size for argument <code>id</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     * @return the size
     */
    abstract long size(JobLOB lob, int sessionId, int id);

    /**
     * Returns the initial capacity for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the initial capacity
     */
    abstract int getInitialCapacity(JobMap map, int sessionId, int id);

    /**
     * Returns the initial capacity for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the initial capacity
     */
    abstract int getInitialCapacity(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the initial capacity for argument <code>id</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     * @return the initial capacity
     */
    abstract int getInitialCapacity(JobLOB lob, int sessionId, int id);

    /**
     * Returns the extent capacity for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the extent capacity
     */
    abstract int getExtentCapacity(JobMap map, int sessionId, int id);

    /**
     * Returns the extent capacity for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the extent capacity
     */
    abstract int getExtentCapacity(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the extent capacity for argument <code>id</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     * @return the extent capacity
     */
    abstract int getExtentCapacity(JobLOB lob, int sessionId, int id);

    /**
     * Returns the name for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the name
     */
    abstract String getName(JobMap map, int sessionId, int id);

    /**
     * Returns the name for argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the name
     */
    abstract String getName(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the name for argument <code>id</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     * @return the name
     */
    abstract String getName(JobLOB lob, int sessionId, int id);

    /**
     * Creates a new id for argument <code>map</code>. This id is created in
     * space <code>spaceId</code>. It is assigned properties specified by
     * arguments <code>initialCapacity</code>, <code>extentCapacitiy</code>,
     * and <code>name</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param spaceId the space id for argument <code>map</code>
     * @param initialCapacity the initial capacity for argument <code>map</code>
     * @param extentCapacity the extent capacity for argument <code>map</code>
     * @param name the name for argument <code>map</code>
     * @return id the id
     */
    abstract int create(JobMap map, int sessionId, int spaceId, int initialCapacity, int extentCapacity, String name);

    /**
     * Creates a new id for argument <code>map</code>. This id is created in
     * space <code>spaceId</code>. It is assigned properties specified by
     * arguments <code>initialCapacity</code>, <code>extentCapacitiy</code>,
     * and <code>name</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param spaceId the space id for argument <code>map</code>
     * @param initialCapacity the initial capacity for argument <code>map</code>
     * @param extentCapacity the extent capacity for argument <code>map</code>
     * @param name the name for argument <code>map</code>
     * @return id the id
     */
    abstract int create(JobKeyMap map, int sessionId, int spaceId, int initialCapacity, int extentCapacity, String name);

    /**
     * Creates a new id for argument <code>lob</code>. This id is created in
     * space <code>spaceId</code>. It is assigned properties specified by
     * arguments <code>initialCapacity</code>, <code>extentCapacitiy</code>,
     * and <code>name</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param spaceId the space id for argument <code>lob</code>
     * @param initialCapacity the initial capacity for argument <code>lob</code>
     * @param extentCapacity the extent capacity for argument <code>lob</code>
     * @param name the name for argument <code>lob</code>
     * @return id the id
     */
    abstract int create(JobLOB lob, int sessionId, int spaceId, int initialCapacity, int extentCapacity, String name);

    /**
     * Deletes the map specified by argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     */
    abstract void delete(JobMap map, int sessionId, int id);

    /**
     * Deletes the map specified by argument <code>id</code>.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     */
    abstract void delete(JobKeyMap map, int sessionId, int id);

    /**
     * Deletes the lob specified by argument <code>id</code>.
     * @param lob the lob
     * @param sessionId the session id of argument <code>lob</code>
     * @param id the id of argument <code>lob</code>
     */
    abstract void delete(JobLOB lob, int sessionId, int id);

    /**
     * Returns the JOB key map id for the specified JOB key.
     * @param sessionId the session id
     * @param key the key
     * @return the map id
     */
    abstract int getJobKeyMapId(int sessionId, JobKey key);

    /**
     * Returns a JOB iterator containing JOB map entries for argument
     * <code>map</code>.
     * @param map the map
     * @return the iterator
     */
    abstract JobIterator entrySetIterator(JobMap map);

    /**
     * Returns a JOB iterator containing JOB map entries for argument
     * <code>map</code>. The returned iterator is initialized using the given
     * <code>key</code>. This means that iteration starts relatively to the
     * given <code>key</code>.
     * @param map the map
     * @param key the key
     * @return the iterator
     */
    abstract JobIterator entrySetIterator(JobMap map, Object key);

    /**
     * Returns a JOB iterator containing keys for argument <code>map</code>.
     * @param map the map
     * @return the iterator
     */
    abstract JobIterator keySetIterator(JobMap map);

    /**
     * Returns a JOB iterator containing values for argument <code>map</code>.
     * @param map the map
     * @return the iterator
     */
    abstract JobIterator valueSetIterator(JobMap map);

    /**
     * Returns the first bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first bunch
     */
    abstract List getFirstBunch(JobMap map, int sessionId, int id);

    /**
     * Returns the last bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last bunch
     */
    abstract List getLastBunch(JobMap map, int sessionId, int id);

    /**
     * Returns the next bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the next bunch
     */
    abstract List getNextBunch(JobMap map, int sessionId, int id, Object key);

    /**
     * Returns the previous bunch of entries for argument <code>map</code>
     * from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the previous bunch
     */
    abstract List getPrevBunch(JobMap map, int sessionId, int id, Object key);

    /**
     * Returns the first bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first bunch
     */
    abstract List getFirstBunch(JobSortedMap map, int sessionId, int id);

    /**
     * Returns the last bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last bunch
     */
    abstract List getLastBunch(JobSortedMap map, int sessionId, int id);

    /**
     * Returns the next bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the next bunch
     */
    abstract List getNextBunch(JobSortedMap map, int sessionId, int id, Object key);

    /**
     * Returns the previous bunch of entries for argument <code>map</code>
     * from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the previous bunch
     */
    abstract List getPrevBunch(JobSortedMap map, int sessionId, int id, Object key);

    /**
     * Returns the first bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the first bunch
     */
    abstract List getFirstBunch(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the last bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @return the last bunch
     */
    abstract List getLastBunch(JobKeyMap map, int sessionId, int id);

    /**
     * Returns the next bunch of entries for argument <code>map</code> from
     * JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the first bunch
     */
    abstract List getNextBunch(JobKeyMap map, int sessionId, int id, Object key);

    /**
     * Returns the previous bunch of entries for argument <code>map</code>
     * from JOB.
     * @param map the map
     * @param sessionId the session id of argument <code>map</code>
     * @param id the id of argument <code>map</code>
     * @param key the key
     * @return the previous bunch
     */
    abstract List getPrevBunch(JobKeyMap map, int sessionId, int id, Object key);
}
