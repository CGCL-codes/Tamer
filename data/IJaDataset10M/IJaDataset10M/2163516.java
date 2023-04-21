package edu.washington.mysms.client.sms;

import java.nio.ByteBuffer;
import edu.washington.mysms.Message;
import edu.washington.mysms.security.Login;
import edu.washington.mysms.security.LoginEncoder;
import edu.washington.mysms.server.InboundQueryMessage;
import edu.washington.mysms.client.MySMS_ClientService;
import edu.washington.mysms.client.Properties;
import edu.washington.mysms.client.notifyMySMS;
import edu.washington.mysms.coding.QueryEncoder;
import edu.washington.mysms.coding.ResultDecoder;
import edu.washington.mysms.coding.ResultEncoder;
import edu.washington.mysms.coding.ResultTable;
import edu.washington.mysms.coding.SerializableByteBufferWrapper;
import edu.washington.mysms.coding.SerializingResultCoder;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;
import android.os.SystemClock;

public class MySMS_SMSService extends Service {

    private static final boolean DEBUG = true;

    private static final String LOGGING_TAG = "MySMS_SMSService";

    private static final Boolean EMULATOR_SEND_OUT = true;

    private static final Boolean FRAG_TEXT_TEST = false;

    private static final int HEADER_SIZE = 3;

    private static final int MAX_SMS_SIZE = 140;

    private static final int MAX_FRAGMENTS = 128;

    private static final int STREAM_MAX = 64;

    protected static SQLiteDatabase receiverDB;

    private static final String DB_NUMBER_TABLE = "PhoneNumbersTable";

    private static final String DB_FRAG_TABLE = "FragmentsTable";

    private static final String DB_MSG_TABLE = "MessagesTable";

    private static final String DB_RESULT_TABLE = "ResultsTable";

    private static final String DB_STREAM_TABLE = "StreamTable";

    private static final String DB_PROCESSING_TABLE = "ProcessingTable";

    private static final String DB_NUMBER_COL = "number";

    private static final String DB_STREAM_COL = "streamNumber";

    private static final String DB_MSG_COL = "message";

    private static final String DB_IN_TIMESTAMP_COL = "inTimeStamp";

    private static final String DB_OUT_TIMESTAMP_COL = "outTimeStamp";

    private static final String DB_MSG_NUMBER_COL = "messageNumber";

    private static final String DB_FRAG_NUMBER_COL = "fragmentNumber";

    private static final String DB_LAST_FRAG_COL = "lastFragment";

    private static final String DB_ID_COL = "SSID";

    private static final String DB_LINKID_COL = "SSIDLink";

    private static final String DB_ACK_RECEIVE_TIMEOUT_INIT_COL = "receiveTimerInit";

    private static final String DB_ACK_RECEIVE_TIMEOUT_COL = "receiveTimerCur";

    private static final String DB_RETRIES_REMAINING_COL = "retriesRemaining";

    private static final String DB_DECODER_CLASS_COL = "decoderClass";

    private static final String DB_APP_HANDLE_COL = "appHandle";

    private static final String DB_MSG_INITIATED_COL = "msgInOut";

    private static final String DB_RESULT_TYPE_COL = "resultType";

    private static final int DB_VERSION = 1;

    private static final String DB_NAME = "/data/data/edu.washington.mysms.client/MySMS_SMSService.db";

    private static final String DB_ERROR = "MySMS Receiver unable to open database.\n MySMS will not function and all applications depending on the MySMS framework will be unable to communicate with their remote databases.";

    private static final String DECODER_CLASS = "edu.washington.mysms.coding.FieldsOnlyResultCoder";

    private static final String ENCODER_CLASS = "edu.washington.mysms.coding.SimpleQueryCoder";

    private static final long ACK_RECEIVE_TIMEOUT_INIT = 60000;

    private static ReliabilityHandler RELIABILITY_THREAD;

    private static final long NOTIFICATION_TOO_OLD = 86400000;

    private static notifyMySMS[] notifyService = new notifyMySMS[STREAM_MAX];

    /**
	 * This is the binder that implements the methods available to
	 * other processes.
	 */
    private final MySMS_ClientService.Stub serviceBinder = new MySMS_ClientService.Stub() {

        /**
		 * Use this to unregister a stream number.
		 * @param applicationName the application holding the stream number
		 * @param stream stream number to unregister; if -1, unregister all streams
		 * @return true on success
		 */
        public boolean unregisterStream(String applicationName, int stream) {
            if (stream < -1 || stream >= STREAM_MAX) {
                return false;
            }
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    return false;
                }
            }
            String[] selectArgs = { applicationName };
            if (stream != -1) {
                return (receiverDB.delete(DB_STREAM_TABLE, DB_APP_HANDLE_COL + "=?" + " AND " + DB_STREAM_COL + "=" + stream, selectArgs) > 0);
            } else {
                return (receiverDB.delete(DB_STREAM_TABLE, DB_APP_HANDLE_COL + "=?", selectArgs) > 0);
            }
        }

        /**
		 * Use to register for a stream.  If an application is already registered for a stream
		 * number then that stream number will be returned
		 * @param applicationName the name of the application registering; must be unique
		 * @param decoderClass the class to use when decoding messages; if null, will be set to default
		 * @param initiate true if this stream is to be used for initiated messages; false for uninitiated messages 
		 * @returns the new stream number or -1 if unable register or -2 no more available locations
		 */
        public int registerStream(String applicationName, String decoderClass, boolean initiate) {
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Starting Stream Registration (" + applicationName + ")");
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Database malfunction");
                    return -1;
                }
            }
            if (decoderClass == null) decoderClass = DECODER_CLASS;
            String[] columns = { DB_STREAM_COL };
            String[] selectArgs = { applicationName };
            Cursor getStream = receiverDB.query(DB_STREAM_TABLE, columns, DB_APP_HANDLE_COL + "=?" + " AND " + DB_MSG_INITIATED_COL + "=" + ((initiate) ? 1 : 0), selectArgs, null, null, null);
            if (getStream == null || !getStream.first()) {
                if (getStream != null) getStream.close();
                getStream = receiverDB.query(DB_STREAM_TABLE, columns, null, null, null, null, DB_STREAM_COL);
                int stream = 0;
                if (getStream == null || !getStream.first()) {
                    if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Empty List");
                } else {
                    if (getStream.count() == STREAM_MAX) {
                        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Streams Maxed Out");
                        return -2;
                    }
                    while (!getStream.isAfterLast()) {
                        if (getStream.getInt(0) != stream) break;
                        stream++;
                        if (!getStream.next()) break;
                    }
                }
                if (getStream != null) getStream.close();
                ContentValues newStream = new ContentValues(2);
                newStream.put(DB_MSG_NUMBER_COL, 0);
                newStream.put(DB_STREAM_COL, stream);
                newStream.put(DB_MSG_INITIATED_COL, (initiate) ? 1 : 0);
                newStream.put(DB_DECODER_CLASS_COL, decoderClass);
                newStream.put(DB_APP_HANDLE_COL, applicationName);
                long returnValue = receiverDB.insert(DB_STREAM_TABLE, null, newStream);
                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Stream Registration (" + applicationName + "," + stream + ")");
                return (returnValue > 0) ? stream : -1;
            } else {
                int stream = getStream.getInt(0);
                getStream.close();
                return stream;
            }
        }

        /**
		 * Register a specific stream number for uninitiated messages
		 * @param applicationName the name of the application registering; must be unique
		 * @param decoderClass the class to use when decoding messages; if null, will be set to default
		 * @param stream the stream to manually register for
		 * @return true if stream successfully registered
		 */
        public boolean registerUninitiatedStream(String applicationName, String decoderClass, int stream) {
            if (stream < 0 || stream >= STREAM_MAX) {
                return false;
            }
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    return false;
                }
            }
            if (decoderClass == null) decoderClass = DECODER_CLASS;
            SQLiteStatement getStream = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_STREAM_TABLE + " WHERE " + DB_STREAM_COL + "=" + stream);
            if (getStream.simpleQueryForLong() > 0) return false;
            ContentValues newStream = new ContentValues(2);
            newStream.put(DB_MSG_NUMBER_COL, 0);
            newStream.put(DB_STREAM_COL, stream);
            newStream.put(DB_MSG_INITIATED_COL, 0);
            newStream.put(DB_DECODER_CLASS_COL, decoderClass);
            newStream.put(DB_APP_HANDLE_COL, applicationName);
            long returnValue = receiverDB.insert(DB_STREAM_TABLE, null, newStream);
            return (returnValue > 0);
        }

        /**
	     * A client application can use this method to query the status of a message.  If a message is
	     * still awaiting results and another older message with the same message number is still in the
	     * results, this method will favor the more current message and report "Still Processing"
	     * 3 = Have a stored error result
	     * 2 = Have a stored ResultTable result
	     * 1 = Still Processing
	     * 0 = Unknown Message Number
	     * -1 = Bad Argument
	     * -2 = Error Processing Request
	     */
        public int queryMessageStatus(int stream, int msgNumber) {
            if (stream < 0 || stream >= STREAM_MAX || msgNumber < 0 || msgNumber >= 128) {
                return -1;
            }
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    return -2;
                }
            }
            SQLiteStatement checkProcessing = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_PROCESSING_TABLE + " WHERE " + DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber);
            if (checkProcessing.simpleQueryForLong() > 0) return 1;
            String[] checkResultsColumns = { DB_RESULT_TYPE_COL };
            Cursor checkResults = receiverDB.query(DB_RESULT_TABLE, checkResultsColumns, DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber, null, null, null, null);
            if (checkResults != null) {
                if (checkResults.first()) {
                    int type = checkResults.getInt(0);
                    if (type == Message.Type.ERROR.ordinal()) type = 3; else if (type == Message.Type.RESULTTABLE.ordinal()) type = 2; else type = 0;
                    checkResults.close();
                    return type;
                }
                checkResults.close();
            }
            return 0;
        }

        /**
		 * Sends the given query to the given address.  The caller
		 * should also provide a stream id, unique to each query.
		 *
		 * The Properties object passed should contain MySMS keys for
		 * this particular client application.  Later on this properties
		 * object should also contain encryption information.
		 * 
		 * The Properties object MUST contain the following information:
		 * *"query.encoder" - the class to use when encoding the query; must extend QueryEncoder.
		 * 
		 * The Properties object MAY contain the following information:
		 * *query.retries - The number of retry attempts for the message.  Default is 0.
		 * *query.timeout - The length in milliseconds before attempting to retry.
		 * *query.messageType - The type of message to be sent. Default is InboundQueryMessage.Type.UNSECURE_QUERY.
		 * See InboundMessage.java for more information.
		 * *query.responseType - The type of response desired from the server. Default is   
		 * InboundQueryMessage.ResponseType.DEFAULT. See InboundMessage.java for more information.
		 * *query.usernameEncoder - The LoginEncoder class to use when query.messageType is 
		 * InboundQueryMessage.Type.USERNAME_QUERY.
		 * *query.username - The username to use when query.messageType is InboundQueryMessage.Type.USERNAME_QUERY.
		 * 
		 * @param properties The MySMS properties for the client application.
		 * @param stream An integer unique to this query provided by the client.
		 * @param address The address of the server to query.
		 * @param query A SQL query to pass to the server.
		 * @return the messageNumber if successful, negative if error
		 *  -1 = bad parameter; -2 = unable to encode; -3 = unable to send
		 */
        @SuppressWarnings("unchecked")
        public int executeQuery(Properties properties, int stream, String number, String query) {
            if (DEBUG) Log.d(LOGGING_TAG, "Call: executeQuery(\"" + number + "\", \"" + query + "\")");
            if (query == null || number == null || properties == null || stream < 0 || stream >= STREAM_MAX) {
                Log.i(LOGGING_TAG, "[MySMS SMSService] invalid execute query arguments");
                return -1;
            }
            if (DEBUG) Log.d(LOGGING_TAG, "Encoder = " + properties.getProperty("query.encoder", ENCODER_CLASS));
            ByteBuffer encodedQuery;
            Class<? extends QueryEncoder> encoder_class;
            try {
                encoder_class = (Class<? extends QueryEncoder>) Class.forName(properties.getProperty("query.encoder", ENCODER_CLASS));
            } catch (Exception e) {
                Log.i(LOGGING_TAG, "Could not find specified encoder: " + properties.getProperty("query.encoder", ENCODER_CLASS));
                return -1;
            }
            encodedQuery = encodeQuery(query, encoder_class);
            if (encodedQuery == null) {
                Log.i(LOGGING_TAG, "[MySMS SMSService] execute query: unable to encode properly");
                return -2;
            }
            int retries = Integer.parseInt(properties.getProperty("query.retries", "" + 0));
            long ackTimeout = Long.parseLong(properties.getProperty("query.timeout", "" + ACK_RECEIVE_TIMEOUT_INIT));
            byte messageType = (byte) Integer.parseInt(properties.getProperty("query.messageType", "" + InboundQueryMessage.Type.UNSECURE_QUERY.ordinal()));
            byte responseType = (byte) Integer.parseInt(properties.getProperty("query.responseType", "" + InboundQueryMessage.ResponseType.DEFAULT.ordinal()));
            if (messageType == (byte) InboundQueryMessage.Type.USERNAME_QUERY.ordinal()) {
                Class<? extends LoginEncoder> login_encoder_class;
                if (properties.getProperty("query.usernameEncoder") == null || properties.getProperty("query.username") == null) {
                    Log.i(LOGGING_TAG, "Attempt to encode with login, but critical properties are missing");
                    return -1;
                }
                try {
                    login_encoder_class = (Class<? extends LoginEncoder>) Class.forName(properties.getProperty("query.usernameEncoder"));
                } catch (Exception e) {
                    Log.i(LOGGING_TAG, "Could not find specified encoder: " + properties.getProperty("query.usernameEncoder"));
                    return -1;
                }
                LoginEncoder login_encoder;
                try {
                    login_encoder = login_encoder_class.newInstance();
                } catch (Exception e) {
                    Log.i(LOGGING_TAG, "Could not create instances of encoder: " + properties.getProperty("query.usernameEncoder"));
                    return -1;
                }
                Login username = new Login(properties.getProperty("query.username"));
                try {
                    encodedQuery = ByteBuffer.wrap(login_encoder.encode(username, encodedQuery.array()));
                } catch (Exception e) {
                    Log.i(LOGGING_TAG, "Could not encode sql statement with username.");
                    return -2;
                }
            }
            ByteBuffer wrappedMessage = ByteBuffer.allocate(encodedQuery.capacity() + 1);
            byte header = messageType;
            header |= responseType;
            wrappedMessage.put(header);
            wrappedMessage.put(encodedQuery);
            int result = sendMessage(number, wrappedMessage, stream, retries, ackTimeout);
            return ((result >= 0) ? result : 3);
        }

        /**
		 * Retrieve the error message from the service.  The service will
		 * utilize the given callback to notify the application that a response
		 * has come from the server.  If the return type was not an error,
		 * null will be returned.
		 * 
		 * @param properties The MySMS properties for the client application.
		 * @param stream An integer unique to this query provided by the client.
		 * @param msgNumber the message for which to retrieve results
		 * @return the error message; null if not an error
		 */
        public String getError(Properties properties, int stream, int msgNumber) {
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    return null;
                }
            }
            String[] dataColumns = { DB_MSG_COL, DB_RESULT_TYPE_COL };
            Cursor getData = receiverDB.query(DB_RESULT_TABLE, dataColumns, DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber, null, null, null, null);
            if (getData == null || !getData.first()) {
                if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] getResults : no Data.");
                return null;
            }
            if (getData.getInt(1) != Message.Type.ERROR.ordinal()) {
                getData.close();
                return null;
            }
            String errorMessage = getData.getString(0);
            getData.close();
            return errorMessage;
        }

        /**
		 * Retrieve the results from the service.  The service will
		 * utilize the given callback to notify the application that a response
		 * has come from the server.  If the return type was not a result table,
		 * null will be returned.
		 * 
		 * @param properties The MySMS properties for the client application.
		 * @param stream An integer unique to this query provided by the client.
		 * @param msgNumber the message for which to retrieve results
		 * @return the byte array to be decoded using SimpleResultCoder
		 */
        public byte[] getResults(Properties properties, int stream, int msgNumber) {
            if (receiverDB == null) {
                if (!openDatabase(null)) {
                    return null;
                }
            }
            String[] dataColumns = { DB_MSG_COL, DB_RESULT_TYPE_COL };
            Cursor getData = receiverDB.query(DB_RESULT_TABLE, dataColumns, DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber, null, null, null, null);
            if (getData == null || !getData.first()) {
                if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] getResults : no Data.");
                if (getData != null) getData.close();
                return null;
            }
            if (getData.getInt(1) != Message.Type.RESULTTABLE.ordinal()) {
                getData.close();
                return null;
            }
            String rs_table_S = getData.getString(0);
            getData.close();
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] string hash:" + rs_table_S.hashCode());
            receiverDB.delete(DB_RESULT_TABLE, DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber, null);
            if (rs_table_S == null) return null;
            return rs_table_S.getBytes();
        }

        /**
		 * The client application should use this before sending a query.
		 * This allows MySMS to notify the application that a new message has come 
		 * @see edu.washington.mysms.client.MySMS_ClientService#startNotify(edu.washington.mysms.client.notifyMySMS)
		 * @param callback the notifyMySMS callback to use for this application
		 * @param stream the stream number to which this callback should be registered
		 * @return true is successfully registered; false if parameter error or if there is already a callback registered
		 * for this stream
		 */
        public boolean startNotify(final notifyMySMS callback, int stream) {
            if (stream >= 0 && stream < STREAM_MAX && callback != null) if (notifyService[stream] == null) {
                notifyService[stream] = callback;
                return true;
            }
            return false;
        }
    };

    /**
	 * Returns an IBinder by which to call this service.
	 * @return An IBinder for this service.
	 */
    public IBinder getBinder() {
        return this.serviceBinder;
    }

    /**
	 * Returns an IBinder by which to call this service.
	 * @return An IBinder for this service
	 */
    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    /**
	 * This service should only be started by the MySMS_SMSReceiver.  Upon starting, it will process
	 * the new incoming SMS message.
	 * @param arguments A bundle of data: message, message number, fragmentation information
	 */
    @Override
    public void onStart(int startID, Bundle arguments) {
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] started: " + startID);
        if (!arguments.containsKey("MySMS_message") || !arguments.containsKey("MySMS_phoneNumber") || !arguments.containsKey("MySMS_timestamp")) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] improper usage of MySMS_Service. Exiting.");
            return;
        }
        SerializableByteBufferWrapper fullMessage = (SerializableByteBufferWrapper) arguments.getSerializable("MySMS_message");
        if (fullMessage == null) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] Message == null");
            return;
        }
        byte[] rawMessage = fullMessage.getByteBuffer().array();
        if (rawMessage.length > MAX_SMS_SIZE || rawMessage.length < HEADER_SIZE) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] Message wrong size");
            return;
        }
        String phoneNumber = arguments.getString("MySMS_phoneNumber");
        if (phoneNumber == null) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] No number");
            return;
        }
        long messageTimestamp = arguments.getLong("MySMS_timestamp");
        if (messageTimestamp < 0) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] Timestamp = " + messageTimestamp);
            return;
        }
        int msgNumber = rawMessage[1];
        int stream = rawMessage[0] & 0x3f;
        int fragNumber = rawMessage[2] & 0x7f;
        int lastFrag = (((rawMessage[2] & 0x80) > 0) ? 1 : 0);
        if (receiverDB == null) {
            if (!openDatabase(this)) {
                return;
            }
        }
        int size = rawMessage.length - HEADER_SIZE;
        byte[] fragMessage = new byte[size];
        for (int i = 0; i < size; i++) {
            fragMessage[i] = rawMessage[i + HEADER_SIZE];
        }
        if (fragNumber == 0 && lastFrag == 1) {
            int messageType = fragMessage[HEADER_SIZE] & 0x0F;
            if (messageType == Message.Type.RESENDREQUEST.ordinal()) {
                return;
            }
        }
        String[] streamColumns = { DB_MSG_INITIATED_COL };
        Cursor checkValidStream = receiverDB.query(DB_STREAM_TABLE, streamColumns, DB_STREAM_COL + "=" + stream, null, null, null, null);
        if (checkValidStream == null || !checkValidStream.first()) {
            Toast.makeText(this, "[MySMS SMSService] MySMS has received a message for an unregistered stream. " + "Please register for stream " + stream + " or contact " + phoneNumber + " to have your number removed.", Toast.LENGTH_LONG);
            if (checkValidStream != null) checkValidStream.close();
            return;
        }
        int initiated = checkValidStream.getInt(0);
        checkValidStream.close();
        if (initiated == 0) if (System.currentTimeMillis() - NOTIFICATION_TOO_OLD > messageTimestamp) return;
        String[] getIDColumns = { DB_ID_COL, DB_ACK_RECEIVE_TIMEOUT_INIT_COL };
        Cursor getData = receiverDB.query(DB_PROCESSING_TABLE, getIDColumns, DB_MSG_NUMBER_COL + "=" + msgNumber + " AND " + DB_STREAM_COL + "=" + stream, null, null, null, null);
        if (initiated == 1 && (getData == null || !getData.first())) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] No a requested message. Rejecting");
            return;
        }
        if (fragNumber == 0 && lastFrag == 1) {
            long processSSID = -1;
            if (initiated == 1) {
                processSSID = getData.getLong(0);
                ContentValues dataToInsert = new ContentValues(1);
                dataToInsert.put(DB_IN_TIMESTAMP_COL, SystemClock.elapsedRealtime() + getData.getLong(1));
                receiverDB.update(DB_PROCESSING_TABLE, dataToInsert, DB_ID_COL + "=" + processSSID, null);
            }
            if (getData != null) getData.close();
            processIncomingMessage(fragMessage, stream, msgNumber, phoneNumber, processSSID);
            return;
        }
        long processSSID;
        if (initiated == 0 && (getData == null || !getData.first())) {
            ContentValues dataToInsert = new ContentValues();
            dataToInsert.put(DB_NUMBER_COL, phoneNumber);
            dataToInsert.put(DB_STREAM_COL, stream);
            dataToInsert.put(DB_MSG_NUMBER_COL, msgNumber);
            dataToInsert.put(DB_IN_TIMESTAMP_COL, SystemClock.elapsedRealtime() + ACK_RECEIVE_TIMEOUT_INIT);
            dataToInsert.put(DB_ACK_RECEIVE_TIMEOUT_INIT_COL, ACK_RECEIVE_TIMEOUT_INIT);
            dataToInsert.put(DB_ACK_RECEIVE_TIMEOUT_COL, ACK_RECEIVE_TIMEOUT_INIT);
            dataToInsert.put(DB_RETRIES_REMAINING_COL, 0);
            processSSID = receiverDB.insert(DB_PROCESSING_TABLE, null, dataToInsert);
            synchronized (receiverDB) {
                if (RELIABILITY_THREAD == null || RELIABILITY_THREAD.state < 0) {
                    RELIABILITY_THREAD = new ReliabilityHandler();
                    RELIABILITY_THREAD.start();
                }
            }
        } else processSSID = getData.getLong(0);
        long ackWaitTime = getData.getLong(1);
        getData.close();
        SQLiteStatement getFragCount = receiverDB.compileStatement("Select COUNT(*) FROM " + DB_FRAG_TABLE + " WHERE " + DB_LINKID_COL + "=" + processSSID + " AND " + DB_FRAG_NUMBER_COL + "=" + fragNumber);
        if (getFragCount.simpleQueryForLong() != 0) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] duplicate: frag = " + fragNumber);
            return;
        }
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put(DB_LINKID_COL, processSSID);
        dataToInsert.put(DB_FRAG_NUMBER_COL, fragNumber);
        dataToInsert.put(DB_LAST_FRAG_COL, lastFrag);
        dataToInsert.put(DB_MSG_COL, new String(fragMessage));
        receiverDB.insert(DB_FRAG_TABLE, null, dataToInsert);
        dataToInsert.clear();
        dataToInsert.put(DB_IN_TIMESTAMP_COL, SystemClock.elapsedRealtime() + ackWaitTime);
        receiverDB.update(DB_PROCESSING_TABLE, dataToInsert, DB_ID_COL + "=" + processSSID, null);
        String[] fragColumns = { DB_FRAG_NUMBER_COL, DB_MSG_COL, DB_LAST_FRAG_COL };
        Cursor getFrags = receiverDB.query(DB_FRAG_TABLE, fragColumns, DB_LINKID_COL + "=" + processSSID, null, null, null, DB_FRAG_NUMBER_COL);
        if (getFrags.last()) {
            if (getFrags.getInt(2) == 0 || (getFrags.getInt(0) + 1 != getFrags.count())) {
                getFrags.close();
                return;
            }
        } else {
            getFrags.close();
            return;
        }
        int numberOfFrags = getFrags.getInt(0);
        byte[] fragData = getFrags.getString(1).getBytes();
        int displacement = (numberOfFrags) * (MAX_SMS_SIZE - HEADER_SIZE);
        size = fragData.length + displacement;
        byte[] message = new byte[size];
        if (DEBUG) {
            Log.i(LOGGING_TAG, "[MySMS SMSService] message size = " + size);
            Log.d(LOGGING_TAG, "[MySMS SMSService] added = " + getFrags.getString(1));
        }
        if (!getFrags.first()) {
            getFrags.close();
            return;
        }
        int resultNumber = 0;
        while (!getFrags.isAfterLast()) {
            fragData = getFrags.getString(1).getBytes();
            for (int i = fragData.length - 1; i >= 0; i--) message[i + resultNumber * (MAX_SMS_SIZE - HEADER_SIZE)] = fragData[i];
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] added = " + getFrags.getString(1));
            resultNumber++;
            if (!getFrags.next()) {
                getFrags.close();
                break;
            }
        }
        getFrags.close();
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] new message header = (" + message[0] + "," + message[1] + "," + message[2] + ")");
        if (FRAG_TEXT_TEST && message.length == 256) {
            Log.d(LOGGING_TAG, "[MySMS SMSService] starting ByteTest");
            for (int i = 0; i < 256; i++) if (message[i] != (byte) i) Log.d(LOGGING_TAG, "[MySMS SMSService] failure at (" + i + ", " + message[i] + ")");
            Log.d(LOGGING_TAG, "[MySMS SMSService] ByteTest complete");
        }
        processIncomingMessage(message, stream, msgNumber, phoneNumber, processSSID);
    }

    /**
	 * This method processes a completed incoming message
	 * @param message	the message to be processed
	 * @param stream	the stream it relates to
	 * @param msgNumber the message number
	 * @param phoneNumber	the originating phone number
	 * @param SSID		the database ID number in the processing table (-1 if this a single fragment notification) 
	 */
    @SuppressWarnings("unchecked")
    private void processIncomingMessage(byte[] message, int stream, int msgNumber, String phoneNumber, long processSSID) {
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS SMSService] ProcessSSID = " + processSSID);
        if (receiverDB == null) {
            if (!openDatabase(this)) {
                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Unable to open database");
                return;
            }
        }
        int messageHeader = message[0] & 0x0F;
        boolean notify = true;
        String messageData;
        if (messageHeader == Message.Type.ACKNOWLEDGMENT.ordinal()) {
            messageData = null;
        } else if (messageHeader == Message.Type.ERROR.ordinal()) {
            messageData = new String(message, 1, message.length - 1);
        } else if (messageHeader == Message.Type.RESULTTABLE.ordinal()) {
            ByteBuffer toDecode = ByteBuffer.allocate(message.length - 1);
            toDecode.put(message, 1, message.length - 1);
            ResultTable rs_table;
            SQLiteStatement getDecoderClass = receiverDB.compileStatement("Select " + DB_DECODER_CLASS_COL + " FROM " + DB_STREAM_TABLE + " WHERE " + DB_STREAM_COL + "=" + stream);
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS EncodeDecode] Decoding with = " + getDecoderClass.simpleQueryForString());
            Class<? extends ResultDecoder> decoder_class;
            try {
                decoder_class = (Class<? extends ResultDecoder>) Class.forName(getDecoderClass.simpleQueryForString());
            } catch (Exception e) {
                Log.i(LOGGING_TAG, "Could not find specified encoder: " + getDecoderClass.simpleQueryForString());
                return;
            }
            rs_table = decodeResults(toDecode, decoder_class);
            if (rs_table == null) {
                if (processSSID != -1) {
                    receiverDB.delete(DB_FRAG_TABLE, DB_LINKID_COL + "=" + processSSID, null);
                    SQLiteStatement checkForOutgoing = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_MSG_TABLE + " WHERE " + DB_LINKID_COL + "=" + processSSID);
                    if (checkForOutgoing.simpleQueryForLong() == 0) {
                        receiverDB.delete(DB_PROCESSING_TABLE, DB_ID_COL + "=" + processSSID, null);
                    }
                }
                if (DEBUG) Log.i(LOGGING_TAG, "[MySMS EncodeDecode] ERROR");
                return;
            }
            ResultEncoder encoder = new SerializingResultCoder();
            ByteBuffer resultBuffer;
            try {
                resultBuffer = encoder.encode(null, rs_table);
                messageData = new String(resultBuffer.array());
            } catch (Exception e) {
                Log.i(LOGGING_TAG, "Could not encode result table");
                notify = false;
                messageData = null;
            }
        } else {
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Invalid Message Type");
            notify = false;
            messageData = null;
        }
        if (processSSID != -1) {
            receiverDB.delete(DB_FRAG_TABLE, DB_LINKID_COL + "=" + processSSID, null);
            receiverDB.delete(DB_PROCESSING_TABLE, DB_ID_COL + "=" + processSSID, null);
            receiverDB.delete(DB_MSG_TABLE, DB_LINKID_COL + "=" + processSSID, null);
        }
        if (FRAG_TEXT_TEST) {
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Frag_Text_Test. Exiting");
            return;
        }
        if (messageData != null) {
            receiverDB.delete(DB_RESULT_TABLE, DB_MSG_NUMBER_COL + "=" + msgNumber + " AND " + DB_STREAM_COL + "=" + stream, null);
            ContentValues insertValues = new ContentValues(2);
            insertValues.put(DB_STREAM_COL, stream);
            insertValues.put(DB_MSG_COL, messageData);
            insertValues.put(DB_MSG_NUMBER_COL, msgNumber);
            insertValues.put(DB_IN_TIMESTAMP_COL, System.currentTimeMillis());
            insertValues.put(DB_RETRIES_REMAINING_COL, 0);
            insertValues.put(DB_RESULT_TYPE_COL, messageHeader);
            receiverDB.insert(DB_RESULT_TABLE, null, insertValues);
        }
        if (notify && notifyService[stream] != null) {
            IBinder connectionToClient = notifyService[stream].asBinder();
            if (connectionToClient != null && connectionToClient.pingBinder()) {
                Parcel myResults = Parcel.obtain();
                Parcel myNotification = Parcel.obtain();
                myNotification.writeByte((byte) msgNumber);
                try {
                    connectionToClient.transact(IBinder.FIRST_CALL_TRANSACTION, myNotification, null, 0);
                } catch (DeadObjectException e) {
                    notifyService[stream] = null;
                }
                myNotification.recycle();
                myResults.recycle();
            } else notifyService[stream] = null;
        }
        if (notify && notifyService[stream] == null) {
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS EncodeDecode] No one to notify");
            ;
            Toast.makeText(this, "[MySMS SMSService] MySMS has received a message for " + "However this application is not connected.", Toast.LENGTH_LONG);
        }
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS EncodeDecode] intent launched");
    }

    /**
	 * Use this function to send the completed message (encrypted, encoded etc)
	 * to a destination address.  This function only splits the message and sends it
	 *  Returns the following values
	 *  	 0 = Message Sent without apparent errors
	 *  	-1 = Illegal Argument(s)
	 *  	-2 = Message larger than max
	 *  
	 *  @param	phoneNumber	the destination number
	 *  @param	message		the message to send
	 *  @param	stream		the stream number (must be from 0 to 63)
	 *  @param	msg_number	the number id of the message (must be from 0 to 127)
	 *  @return	status of send attempt	
	 * 		 0 = no errors
	 * 		-1 = IllegalArgumentException
	 * 		-2 = Message too large
	 */
    private static int sendMessageStatic(String phoneNumber, ByteBuffer message, int stream, int msgNumber) {
        byte[] myBuffer = message.array();
        int numFrags = 1 + myBuffer.length / (MAX_SMS_SIZE - HEADER_SIZE);
        if (numFrags >= MAX_FRAGMENTS) {
            return -2;
        }
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] processing long message: " + numFrags);
        for (int i = 0; i < numFrags; i++) {
            int displacement = i * (MAX_SMS_SIZE - HEADER_SIZE);
            int size = (i + 1 < numFrags) ? MAX_SMS_SIZE : (myBuffer.length - displacement + HEADER_SIZE);
            byte[] smsMessage = new byte[size];
            smsMessage[0] = (byte) stream;
            smsMessage[1] = (byte) msgNumber;
            smsMessage[2] = (byte) i;
            if (i + 1 == numFrags) smsMessage[2] = (byte) (smsMessage[2] | 0x80);
            for (int j = HEADER_SIZE; j < size; j++) smsMessage[j] = myBuffer[j - HEADER_SIZE + displacement];
            try {
            } catch (IllegalArgumentException iae) {
                return -1;
            }
            if (EMULATOR_SEND_OUT) MySMS_SMS2Emulator.sendSMS(smsMessage, phoneNumber);
            if (DEBUG) {
                Log.i(LOGGING_TAG, "[MySMS SMSService] frag " + i + " setup");
                Log.i(LOGGING_TAG, "[MySMS SMSService] header =  (" + smsMessage[0] + ", " + smsMessage[1] + ", " + smsMessage[2] + ")");
                Log.i(LOGGING_TAG, "[MySMS SMSService] frag " + i + "emulator sent");
            }
        }
        return 0;
    }

    /**
	 * Use this function to send the message to a destination address.
	 * This message will deal with encoding and reliability.  This method will
	 * invoke sendMessageStatic   
	 *  Returns the following values
	 *  	 0 = Message Sent with apparent errors
	 *  	-1 = Illegal Argument(s)
	 *  	-2 = Message larger than max
	 *  
	 *  @param	phoneNumber	the destination number
	 *  @param	message		the message to send
	 *  @param	stream		the stream number (must be from 0 to 63)
	 *  @param 	retries		the number of times to attempt re-sending a message
	 *  @param  ackReceiveTimeout	acknowledge timeout value to use for reliability
	 *  @return	message number; or a negative number if there was an error
	 * 		 0 or greater = no errors
	 * 		-1 = IllegalArgumentException
	 * 		-2 = Message too large
	 * 		-3 = Database problem
	 * 		-4 = Too many messages outstanding
	 */
    private int sendMessage(String phoneNumber, ByteBuffer message, int stream, int retries, long ackReceiveTimeout) {
        if (receiverDB == null) {
            if (!openDatabase(null)) {
                return -3;
            }
        }
        int msgNumber;
        String[] messageColumn = { DB_MSG_NUMBER_COL };
        Cursor messageNumber = receiverDB.query(DB_STREAM_TABLE, messageColumn, DB_STREAM_COL + "=" + stream, null, null, null, null);
        if (messageNumber != null && messageNumber.first()) {
            msgNumber = messageNumber.getInt(0) + 1;
            SQLiteStatement checkForOverflow = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_PROCESSING_TABLE + " WHERE " + DB_STREAM_COL + "=" + stream + " AND " + DB_MSG_NUMBER_COL + "=" + msgNumber);
            if (checkForOverflow.simpleQueryForLong() > 0) {
                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] message number in use");
                messageNumber.close();
                return -4;
            }
            ContentValues newNumber = new ContentValues(1);
            newNumber.put(DB_MSG_NUMBER_COL, msgNumber);
            receiverDB.update(DB_STREAM_TABLE, newNumber, DB_STREAM_COL + "=" + stream, null);
        } else {
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] Not a registered stream");
            return -1;
        }
        sendMessageStatic(phoneNumber, message, stream, msgNumber);
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] message sent");
        if (messageNumber != null) messageNumber.close();
        ContentValues messageData = new ContentValues(8);
        messageData.put(DB_NUMBER_COL, phoneNumber);
        messageData.put(DB_MSG_NUMBER_COL, msgNumber);
        messageData.put(DB_STREAM_COL, stream);
        messageData.put(DB_ACK_RECEIVE_TIMEOUT_INIT_COL, ackReceiveTimeout);
        messageData.put(DB_ACK_RECEIVE_TIMEOUT_COL, ackReceiveTimeout);
        messageData.put(DB_RETRIES_REMAINING_COL, retries);
        messageData.put(DB_OUT_TIMESTAMP_COL, SystemClock.elapsedRealtime() + ackReceiveTimeout);
        long processSQLID = receiverDB.insert(DB_PROCESSING_TABLE, null, messageData);
        messageData.clear();
        messageData.put(DB_LINKID_COL, processSQLID);
        messageData.put(DB_MSG_COL, new String(message.array()));
        long messageSQLID = receiverDB.insert(DB_MSG_TABLE, null, messageData);
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] ProcessSQLID,MessageSQLID=(" + messageSQLID + "," + processSQLID + ")");
        MySMS_SMSService.addListenNumber(phoneNumber, this);
        synchronized (receiverDB) {
            if (RELIABILITY_THREAD == null || RELIABILITY_THREAD.state < 0) {
                RELIABILITY_THREAD = new ReliabilityHandler();
                RELIABILITY_THREAD.start();
            } else {
                receiverDB.notify();
            }
        }
        return msgNumber;
    }

    /**
	 * This function is used to encode a SQL query
	 * @param query			The query to be encoded
	 * @param encoder_class The class to use to encode the query
	 * @param results		The ByteBuffer to store the results in
	 * @return	The results in a ByteBuffer; null if there was a problem
	 */
    private ByteBuffer encodeQuery(String query, Class<? extends QueryEncoder> encoder_class) {
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS EncodeDecode] start encoding: ");
        QueryEncoder encoder;
        try {
            encoder = encoder_class.newInstance();
        } catch (Exception e) {
            Log.i(LOGGING_TAG, "Could not create instance of encoder:" + encoder_class.getName());
            return null;
        }
        ByteBuffer buffer;
        try {
            buffer = encoder.encode(null, query);
        } catch (Exception e) {
            Log.i(LOGGING_TAG, "Could not encode sql statement \"" + query + "\" with the \"" + encoder_class.getName() + "\" encoder");
            return null;
        }
        return buffer;
    }

    /**
	 * Use this function to decode the results
	 * @param rawMessage	the message to be decoded
	 * @param decoder_class the class to use to decode the message
	 * @return the ResultTable resulting from the decoding
	 */
    private ResultTable decodeResults(ByteBuffer rawMessage, Class<? extends ResultDecoder> decoder_class) {
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS EncodeDecode] decoding: ");
        ResultDecoder decoder;
        try {
            decoder = decoder_class.newInstance();
        } catch (Exception e) {
            Log.i(LOGGING_TAG, "Could not create instance of decoder:" + decoder_class.getName());
            return null;
        }
        ResultTable re_results;
        try {
            re_results = decoder.decode(null, rawMessage);
        } catch (Exception e) {
            Log.i(LOGGING_TAG, "Error decoding message into a ResultTable with " + "the \"" + decoder_class.getName() + "\" decoder");
            return null;
        }
        return re_results;
    }

    /**
	 * Function to open the database to house the numbers
	 * and information required for reliable SMS fragmentation
	 * This database also houses all necessary information or the SMSService to run
	 * @param context Context in which to announce a failure; may be null
	 * 
	 * @return true if the database was successfully opened 
	 */
    private static boolean openDatabase(Context context) {
        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS SMSService] opening database: ");
        if (receiverDB == null) {
            receiverDB = SQLiteDatabase.open(DB_NAME, null);
            if (receiverDB == null) {
                if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Service] opening database failed. creating...: ");
                receiverDB = SQLiteDatabase.create(DB_NAME, DB_VERSION, null);
                if (receiverDB != null) {
                    try {
                        receiverDB.execSQL("CREATE TABLE " + DB_NUMBER_TABLE + " (" + DB_NUMBER_COL + " TEXT PRIMARY KEY);");
                        receiverDB.execSQL("CREATE TABLE " + DB_FRAG_TABLE + " (" + DB_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_LINKID_COL + " INTEGER NOT NULL, " + DB_MSG_COL + " TEXT NOT NULL, " + DB_FRAG_NUMBER_COL + " INTEGER NOT NULL, " + DB_LAST_FRAG_COL + " INTEGER NOT NULL, " + "FOREIGN KEY(" + DB_LINKID_COL + ") REFERENCES " + DB_PROCESSING_TABLE + " (" + DB_ID_COL + "));");
                        receiverDB.execSQL("CREATE TABLE " + DB_MSG_TABLE + " (" + DB_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_LINKID_COL + " INTEGER NOT NULL, " + DB_MSG_COL + " TEXT NOT NULL, " + "FOREIGN KEY(" + DB_LINKID_COL + ") REFERENCES " + DB_PROCESSING_TABLE + " (" + DB_ID_COL + "));");
                        receiverDB.execSQL("CREATE TABLE " + DB_PROCESSING_TABLE + " (" + DB_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " + DB_NUMBER_COL + " TEXT NOT NULL, " + DB_STREAM_COL + " INTEGER NOT NULL, " + DB_MSG_NUMBER_COL + " INTEGER NOT NULL, " + DB_RETRIES_REMAINING_COL + " INTEGER NOT NULL, " + DB_ACK_RECEIVE_TIMEOUT_INIT_COL + " INTEGER NOT NULL, " + DB_ACK_RECEIVE_TIMEOUT_COL + " INTEGER NOT NULL, " + DB_OUT_TIMESTAMP_COL + " INTEGER, " + DB_IN_TIMESTAMP_COL + " INTEGER, " + "UNIQUE(" + DB_STREAM_COL + ", " + DB_MSG_NUMBER_COL + "));");
                        receiverDB.execSQL("CREATE TABLE " + DB_RESULT_TABLE + "(" + DB_STREAM_COL + " INTEGER NOT NULL, " + DB_MSG_NUMBER_COL + " INTEGER NOT NULL, " + DB_IN_TIMESTAMP_COL + " INTEGER NOT NULL, " + DB_RETRIES_REMAINING_COL + " INTEGER NOT NULL, " + DB_RESULT_TYPE_COL + " INTEGER NOT NULL, " + DB_MSG_COL + " TEXT NOT NULL, " + "UNIQUE(" + DB_STREAM_COL + ", " + DB_MSG_NUMBER_COL + "));");
                        receiverDB.execSQL("CREATE TABLE " + DB_STREAM_TABLE + "(" + DB_STREAM_COL + " INTEGER PRIMARY KEY, " + DB_MSG_NUMBER_COL + " INTEGER NOT NULL, " + DB_MSG_INITIATED_COL + " INTEGER NOT NULL, " + DB_DECODER_CLASS_COL + " TEXT NOT NULL, " + DB_APP_HANDLE_COL + " TEXT NOT NULL, " + "UNIQUE(" + DB_STREAM_COL + ", " + DB_MSG_NUMBER_COL + "));");
                    } catch (SQLException sqlE) {
                        receiverDB.close();
                        if (context != null) Toast.makeText(context, DB_ERROR, Toast.LENGTH_LONG).show();
                        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Service] creation failed. Unable to populate tables.");
                        return false;
                    }
                } else {
                    if (context != null) Toast.makeText(context, DB_ERROR, Toast.LENGTH_LONG).show();
                    if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Service] creation failed.");
                    return false;
                }
            }
        }
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Service] database open.");
        return true;
    }

    /**
	 * Use this method to clear all stored processing information
	 * This is like a soft reset.
	 * Does not clear the phone number list.
	 */
    public static void clearStat() {
        if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Service] clearing database state.");
        if (receiverDB != null) {
            receiverDB = SQLiteDatabase.open(DB_NAME, null);
            if (receiverDB != null) {
                receiverDB.delete(DB_FRAG_TABLE, "1", null);
                receiverDB.delete(DB_MSG_TABLE, "1", null);
                receiverDB.delete(DB_PROCESSING_TABLE, "1", null);
                receiverDB.delete(DB_RESULT_TABLE, "1", null);
                receiverDB.delete(DB_STREAM_TABLE, "1", null);
            }
        }
    }

    /**
	 *  Use this function to check if a number is a MySMS registered phone number
	 *   
	 *  @param 	phoneNumber phone number to check; currently assumed to be a safe sql value
	 *  @param	context		the context in which to display any error messages; may be null
	 *  @return 			true if the given phone number is in the listen for set
	 */
    public static boolean isListenNumber(String phoneNumber, Context context) {
        if (phoneNumber == null) return false;
        if (receiverDB == null) {
            if (!openDatabase(context)) {
                return false;
            }
        }
        SQLiteStatement checkPhoneNumber = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_NUMBER_TABLE + " WHERE " + DB_NUMBER_COL + "=?");
        checkPhoneNumber.bindString(1, phoneNumber);
        long matchingNumber = checkPhoneNumber.simpleQueryForLong();
        return (matchingNumber > 0) ? true : false;
    }

    /**
	 * Use this function to add a number to the set of phone numbers to listen
	 *  Returns the following values
	 *  	 0 = Number Successfully Added
	 *  	-1 = Invalidly Formatted Number
	 *  	-2 = Number already existed
	 *  	-3 = Error with Database
	 *  
	 *  @param 	phoneNumber phone number to add; currently assumed to be a safe sql value
	 *  @param	context		the context in which to display any error messages; may be null
	 *  @return  			status of add
	 */
    public static int addListenNumber(String phoneNumber, Context context) {
        if (phoneNumber == null) return -1;
        if (receiverDB == null) {
            if (!openDatabase(context)) {
                return -3;
            }
        }
        SQLiteStatement checkPhoneNumber = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_NUMBER_TABLE + " WHERE " + DB_NUMBER_COL + "=?");
        checkPhoneNumber.bindString(1, phoneNumber);
        if (checkPhoneNumber.simpleQueryForLong() == 0) {
            try {
                ContentValues insertValues = new ContentValues(1);
                insertValues.put(DB_NUMBER_COL, phoneNumber);
                return (receiverDB.insert(DB_NUMBER_TABLE, null, insertValues) >= 0) ? 0 : -3;
            } catch (SQLException sqlexc) {
                return -1;
            }
        }
        return -2;
    }

    /**
	 * Use this function to delete a number from within
	 *  the set of phone numbers to listen
	 *
	 * Returns the following values
	 *  	 1+ = Number of entries successfully deleted
	 *  	 0	= Number doesn't exist
	 *  	-1  = Invalid Arguments
	 *  	-2  = Unused
	 *  	-3  = Error with Database
	 * 
	 * @param	phoneNumber phone number to delete; currently assumed to be a safe sql value
	 * @param	context		the context in which to display any error messages; may be null
	 * @return 				true if the number was found to remove
	 */
    public static int deleteListenNumber(String phoneNumber, Context context) {
        if (phoneNumber == null || context == null) return -1;
        if (receiverDB == null) {
            if (!openDatabase(context)) {
                return -3;
            }
        }
        String[] whereArgs = { phoneNumber };
        return (receiverDB.delete(DB_NUMBER_TABLE, DB_NUMBER_COL + "=?", whereArgs));
    }

    /**
	 * Use this method to display the numbers in the number database
	 * @param	context		the context in which to display any error messages; may be null
	 * @return	string representation of the phone numbers in the listen database in the form
	 * 			[number1,number2,...]
	 */
    public static String listListenNumber(Context context) {
        if (receiverDB == null) {
            if (!openDatabase(context)) {
                return null;
            }
        }
        StringBuilder listenNumbers = new StringBuilder();
        listenNumbers.append("[");
        Cursor result = receiverDB.query(DB_NUMBER_TABLE, null, null, null, null, null, null);
        if (result.first()) {
            int numberCol = result.getColumnIndex(DB_NUMBER_COL);
            while (!result.isLast()) {
                listenNumbers.append(result.getString(numberCol));
                listenNumbers.append(",");
                result.next();
            }
            listenNumbers.append(result.getString(numberCol));
        }
        listenNumbers.append("]");
        result.close();
        return listenNumbers.toString();
    }

    /**
	 * Use this method to display the numbers in the number database
	 * @param	context		the context in which to display any error messages; may be null
	 * @return	string representation of the phone numbers in the listen database in the form
	 * 			[number:app,number:app]
	 */
    public static String listRegisteredStreams(Context context) {
        if (receiverDB == null) {
            if (!openDatabase(context)) {
                return null;
            }
        }
        StringBuilder listenNumbers = new StringBuilder();
        listenNumbers.append("[");
        String[] columns = { DB_STREAM_COL, DB_APP_HANDLE_COL };
        Cursor result = receiverDB.query(DB_STREAM_TABLE, columns, null, null, null, null, null);
        if (result.first()) {
            while (!result.isLast()) {
                listenNumbers.append(result.getString(0));
                listenNumbers.append(":");
                listenNumbers.append(result.getString(1));
                listenNumbers.append(",");
                result.next();
            }
            listenNumbers.append(result.getString(0));
            listenNumbers.append(":");
            listenNumbers.append(result.getString(1));
        }
        listenNumbers.append("]");
        result.close();
        return listenNumbers.toString();
    }

    /**
	 * This class is a thread that manages message resends
	 * state =  0 on creation
	 * state =  1 on running
	 * state = -1 on closed
	 * @author david
	 */
    private class ReliabilityHandler extends Thread {

        public int state = 0;

        public void run() {
            synchronized (receiverDB) {
                state = 1;
            }
            SQLiteStatement getNumberOfTasks = receiverDB.compileStatement("SELECT COUNT(*) FROM " + DB_PROCESSING_TABLE + " WHERE " + DB_OUT_TIMESTAMP_COL + " IS NOT NULL");
            SQLiteStatement getWaitTime = receiverDB.compileStatement("SELECT MIN(" + DB_OUT_TIMESTAMP_COL + ") FROM " + DB_PROCESSING_TABLE + " WHERE " + DB_OUT_TIMESTAMP_COL + " IS NOT NULL");
            if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Starting..." + getNumberOfTasks.simpleQueryForLong());
            boolean arg = false;
            while (state == 1) {
                long currentTime = SystemClock.elapsedRealtime();
                if (getNumberOfTasks.simpleQueryForLong() != 0) {
                    String[] columns = { DB_ID_COL, DB_RETRIES_REMAINING_COL, DB_ACK_RECEIVE_TIMEOUT_COL, DB_NUMBER_COL, DB_MSG_NUMBER_COL, DB_STREAM_COL, DB_OUT_TIMESTAMP_COL };
                    Cursor overdue = receiverDB.query(DB_PROCESSING_TABLE, columns, DB_OUT_TIMESTAMP_COL + " IS NOT NULL AND (" + "(" + DB_OUT_TIMESTAMP_COL + "<" + currentTime + " AND " + "(" + DB_IN_TIMESTAMP_COL + " IS NULL OR " + DB_IN_TIMESTAMP_COL + "<" + DB_OUT_TIMESTAMP_COL + ")" + ")" + " OR (" + DB_IN_TIMESTAMP_COL + " IS NOT NULL AND " + DB_IN_TIMESTAMP_COL + ">" + DB_OUT_TIMESTAMP_COL + " AND " + DB_IN_TIMESTAMP_COL + "<" + currentTime + "))", null, null, null, null);
                    if (overdue != null && overdue.first()) {
                        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Processing Message Retries...");
                        boolean deleteTasks = false;
                        StringBuilder tasksToDelete = new StringBuilder();
                        StringBuilder messagesToDelete = new StringBuilder();
                        while (!overdue.isAfterLast()) {
                            int numRetries = overdue.getInt(1);
                            String SSID = overdue.getString(0);
                            if (numRetries <= 0) {
                                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Deleting...");
                                if (deleteTasks) {
                                    tasksToDelete.append(" OR ");
                                    messagesToDelete.append(" OR ");
                                }
                                tasksToDelete.append(DB_ID_COL + "=" + SSID);
                                messagesToDelete.append(DB_LINKID_COL + "=" + SSID);
                                int streamToNotify = overdue.getInt(5);
                                if (notifyService[streamToNotify] != null) {
                                    IBinder connectionToClient = notifyService[streamToNotify].asBinder();
                                    if (connectionToClient != null && connectionToClient.pingBinder()) {
                                        Parcel myResults = Parcel.obtain();
                                        Parcel myNotification = Parcel.obtain();
                                        myNotification.writeByte((byte) (-1 + overdue.getInt(4) * -1));
                                        try {
                                            connectionToClient.transact(IBinder.FIRST_CALL_TRANSACTION, myNotification, null, 0);
                                        } catch (DeadObjectException e) {
                                            notifyService[streamToNotify] = null;
                                        }
                                        myNotification.recycle();
                                        myResults.recycle();
                                    } else notifyService[streamToNotify] = null;
                                }
                                deleteTasks = true;
                            } else {
                                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Resending...");
                                String[] msgColumns = { DB_MSG_COL };
                                Cursor messageToResend = receiverDB.query(DB_MSG_TABLE, msgColumns, DB_ID_COL + "=" + overdue.getString(0), null, null, null, null);
                                if (messageToResend.first()) {
                                    sendMessageStatic(overdue.getString(3), ByteBuffer.wrap(messageToResend.getString(0).getBytes()), overdue.getInt(5), overdue.getInt(4));
                                }
                                messageToResend.close();
                                long waitTime = overdue.getLong(2) * 2;
                                ContentValues dataToUpdate = new ContentValues();
                                dataToUpdate.put(DB_OUT_TIMESTAMP_COL, waitTime + currentTime);
                                dataToUpdate.put(DB_ACK_RECEIVE_TIMEOUT_COL, waitTime);
                                dataToUpdate.put(DB_RETRIES_REMAINING_COL, numRetries - 1);
                                receiverDB.update(DB_PROCESSING_TABLE, dataToUpdate, DB_ID_COL + "=" + SSID, null);
                            }
                            if (!overdue.next()) break;
                        }
                        overdue.close();
                        if (deleteTasks) {
                            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Serv] Reliability Handler has " + getNumberOfTasks.simpleQueryForLong());
                            int deletedMessage = receiverDB.delete(DB_MSG_TABLE, messagesToDelete.toString(), null);
                            int deletedTask = receiverDB.delete(DB_PROCESSING_TABLE, tasksToDelete.toString(), null);
                            if (DEBUG) {
                                Log.i(LOGGING_TAG, "[MySMS Serv] Reliability Handler Deleted " + deletedMessage + "," + deletedTask);
                                Log.i(LOGGING_TAG, "[MySMS Serv] Reliability Handler Deleted " + messagesToDelete.toString() + "," + tasksToDelete.toString());
                                Log.i(LOGGING_TAG, "[MySMS Serv] Reliability Handler Remaining " + getNumberOfTasks.simpleQueryForLong());
                            }
                        }
                    }
                } else {
                    if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Breaking..." + getNumberOfTasks.simpleQueryForLong());
                    break;
                }
                String[] columns = { DB_ID_COL };
                StringBuilder tasksToDelete = new StringBuilder();
                StringBuilder fragsToDelete = new StringBuilder();
                Cursor overdueFragMes = receiverDB.query(DB_PROCESSING_TABLE, columns, DB_OUT_TIMESTAMP_COL + " IS NULL AND " + DB_IN_TIMESTAMP_COL + "<" + currentTime, null, null, null, null);
                boolean deleteTasks = false;
                if (overdueFragMes != null && overdueFragMes.first()) {
                    long SSID = overdueFragMes.getLong(0);
                    if (deleteTasks) {
                        tasksToDelete.append(" OR ");
                        fragsToDelete.append(" OR ");
                    }
                    tasksToDelete.append(DB_ID_COL + "=" + SSID);
                    fragsToDelete.append(DB_LINKID_COL + "=" + SSID);
                    deleteTasks = true;
                }
                if (overdueFragMes != null) overdueFragMes.close();
                if (deleteTasks) {
                    receiverDB.delete(DB_FRAG_TABLE, fragsToDelete.toString(), null);
                    receiverDB.delete(DB_PROCESSING_TABLE, tasksToDelete.toString(), null);
                }
                try {
                    long waitTime = getWaitTime.simpleQueryForLong();
                    if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Sleeping for " + (waitTime - currentTime));
                    if (waitTime - currentTime > 0) synchronized (receiverDB) {
                        receiverDB.wait(getWaitTime.simpleQueryForLong() - currentTime);
                    } else {
                        if (arg) state -= 1;
                        if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler exiting abnormal...");
                        arg = true;
                    }
                } catch (InterruptedException e) {
                    ;
                }
                if (DEBUG) Log.d(LOGGING_TAG, "[MySMS Serv] Reliability Handler Awake...");
            }
            synchronized (receiverDB) {
                state = -1;
            }
            if (DEBUG) Log.i(LOGGING_TAG, "[MySMS Serv] Reliability Handler Closing..." + getNumberOfTasks.simpleQueryForLong());
        }
    }
}
