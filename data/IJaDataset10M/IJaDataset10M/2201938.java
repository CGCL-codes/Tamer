package org.xactor.tm.recovery;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.Checksum;
import javax.transaction.xa.Xid;
import org.xactor.tm.TxUtils;

/**
 * Utility class with static methods to create transaction log records and to
 * extract information from transaction log records. It has static methods to
 * create the following kinds of log records:
 * <ul>
 * <li><code>TX_COMMITTED</code> records, which are used for locally-started 
 *     transactions that do not involve other transaction managers;</li>
 * <li><code>MULTI_TM_TX_COMMITTED</code> records, which are used for 
 *     locally-started transactions that involve other transaction 
 *     managers;</li>
 * <li><code>TX_PREPARED</code> records, which are used for foreign 
 *     transactions that entered this virtual machine in transaction contexts 
 *     propagated along with remote method invocations;</li>    
 * <li><code>JCA_TX_PREPARED</code> records, which are used for foreign 
 *     transactions that entered this virtual machine through JCA transaction 
 *     inflow;</li>
 * <li><code>TX_END</code> records, which are used for distributed 
 *     transactions and mark the end of the second phase of the 2PC subtree 
 *     coordinated by this transaction manager. They are paired with 
 *     <code>MULTI_TM_TX_COMMITTED</code>, <code>TX_PREPARED</code>, and 
 *     <code>JCA_TX_PREPARED</code> records. No <code>TX_END</code> record 
 *     is written out in the case of a locally-started transaction that 
 *     involves no external transaction managers. In other words, 
 *     <code>TX_END</code> records are not paired with 
 *     <code>TX_COMMITTED</code> records;</li>
 *<li><code>HEUR_STATUS</code> records, which are used to log the 
 *     heuristic status of a distributed transaction;</li>
 *<li><code>HEUR_FORGOTTEN</code> records, which are used to clear the 
 *     heuristic status of a distributed transaction.</li>
 * </ul>
 * Layout of <code>MULTI_TM_TX_COMMITTED</code> records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long) 
 *     - countOfDirEntries (N, a short)
 *     - varField 0    \
 *       ...            | <------------ variable-sized fields
 *     - varField N-1  /
 *     - offset of varField N-1  \      directory of varFields: N dir entries
 *     - length of varField N-1   |     (each entry contains the length of
 *       ...                      | <--  a varField and the offset of its 
 *     - offset of varField 0     |      first byte relative to the start
 *     - length of varField 0    /       of the record)
 *     - checksum (int)
 * </pre>
 * The varFields of a <code>MULTI_TM_TX_COMMITTED</code> record contain 
 * stringfied references for the remote <code>Resource</code>s enlisted in the 
 * transaction. Note that the dir entries are stored backwards at the end of 
 * the record, that is, the one that appears last (just before the checksum) 
 * refers to the first varField, and the one that appears first refers to the 
 * last varField.
 * <p> 
 * Layout of <code>TX_PREPARED</code> and <code>JCA_TX_PREPARED</code> 
 * records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long) 
 *     - countOfDirEntries (N, a short)
 *     - inboundFormatId (int)
 *     - gidLength (short)
 *     - globalTransactionId (an array of gidLength bytes)
 *     - varField 0    \
 *       ...            | <------------ variable-sized fields
 *     - varField N-1  /
 *     - offset of varField N-1  \      directory of varFields: N dir entries
 *     - length of varField N-1   |     (each entry contains the length of
 *       ...                      | <--  a varField and the offset of its 
 *     - offset of varField 0     |      first byte relative to the start
 *     - length of varField 0    /       of the record)
 *     - checksum (int)
 * </pre>
 * The inboundFormatId is the formatId of the foreign <code>Xid</code>.
 * In a <code>TX_PREPARED</code> record, the first varField (the one referred 
 * to by the dir entry that immediately precedes the checksum) contains a 
 * stringfied reference for the <code>RecoveryCoordinator</code> of the 
 * transaction. 
 * In a <code>JCA_TX_PREPARED</code> record, the first varField contains the 
 * inbound branch qualifier, which is the branch qualifier part of the foreign 
 * <code>Xid</code> passed to <code>XATerminator.prepare()</code>. 
 * The remaining varFields of a <code>TX_PREPARED</code> or 
 * <code>JCA_TX_PREPARED</code> records contain stringfied references for the
 * remote <code>Resource</code>s enlisted in the transaction.
 * </p> 
 * Layout of <code>TX_COMMITTED</code> records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long), 
 *     - checksum (int)
 * </pre>
 * <p> 
 * Layout of <code>TX_END</code> records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long), 
 *     - checksum (int)
 * </pre>
 * <p> 
 * Layout of <code>HEUR_STATUS</code> records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long)
 *     - countOfDirEntries (N, a short)
 *     - transactionStatus (byte)
 *     - heuristicStatusCode (byte)
 *     - locallyDetectedHeuristicHazardFlag (byte)
 *     - isForeignTx (byte)
 *     - formatId (int)
 *     - gidLength (short)
 *     - globalTransactionId (an array of gidLength bytes)
 *     - varField 0    \
 *       ...            | <------------ variable-sized fields
 *     - varField N-1  /
 *     - offset of varField N-1  \      directory of varFields: N dir entries
 *     - length of varField N-1   |     (each entry contains the length of
 *       ...                      | <--  a varField and the offset of its 
 *     - offset of varField 0     |      first byte relative to the start
 *     - length of varField 0    /       of the record)
 *     - checksum (int)
 * </pre>
 * 
 * In a <code>HEUR_STATUS</code> record, the first varField (the one referred 
 * to by the dir entry that immediately precedes the checksum) contains the
 * the inbound branch qualifier of a transaction that entered the server via 
 * JCA inflow. This varField is empty (i.e., it has zero length) in the case of
 * a transaction that did not enter the server via JCA inflow. The second 
 * varField contains a byte array with the heuristic status codes of the XA 
 * resources that reported heuristic decisions. This varField is empty if no
 * XA resource reported heuristic decisions. The remaining varFields are 
 * associated with remote resources that reported heuristic decisions. Each 
 * such varField contains a byte with the heuristic status code reported by the
 * remote resource, followed by a stringfied reference for the remote 
 * <code>Resource</code> instance that reported a heuristic decision.  
 * <p> 
 * Layout of <code>HEUR_FORGOTTEN</code> records:
 * <pre>
 *     - magicHeader (an array of HEADER_LEN bytes)
 *     - recordLength (short)
 *     - recordLengthCheck (short)
 *     - recordType (byte)
 *     - localTransactionId (long), 
 *     - checksum (int)
 * </pre>
 * <p> 
 * Note that <code>TX_COMMITTED</code>, <code>TX_END</code>, and 
 * <code>HEUR_FORGOTTEN</code> records have fixed size. The other types of 
 * records are variable-sized.
 * <p>
 * In all record types:
 * <ul>
 * <li>The recordLength is the number of bytes of the part of the record that
 *     follows the recordLengthCheck field. It counts the bytes from the 
 *     recordType field up to (and including) the checksum field.</li>
 * <li>The recordLengthCheck is the arithmetic negation of the recordLength
 *     value (i.e,, -recordLength).</li>
 * <li>The checksum is the Adler-32 checksum of the part of the record that
 *     starts at the recordType field(which is included in the checksum) and 
 *     ends at the checksum field (which is not included).</li>
 * </ul>
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37634 $ 
 */
public class LogRecord {

    /** Magic header placed in all log records.*/
    public static final byte[] HEADER = "Log".getBytes();

    /** Length of the magic header. */
    public static final int HEADER_LEN = HEADER.length;

    /** Null header that will be read when there are no more log records */
    private static final byte[] NULL_HEADER = { 0, 0, 0 };

    /** Size of a byte, in bytes. */
    private static final int SIZEOF_BYTE = 1;

    /** Size of a short, in bytes. */
    static final int SIZEOF_SHORT = 2;

    /** Size of a long, in bytes. */
    private static final int SIZEOF_LONG = 8;

    /** Length of the inbound format id. */
    private static final int FORMAT_ID_LEN = 4;

    /** Length of the checksum. */
    private static final int CHKSUM_LEN = 4;

    /** Total length of the magic header plus record length fields. */
    static final int FULL_HEADER_LEN = HEADER_LEN + 2 * SIZEOF_SHORT;

    /** Value of recordType field in a single-TM tx committed record */
    static final byte TX_COMMITTED = (byte) 'C';

    /** Value of recordType field in a multi-TM tx committed record */
    static final byte MULTI_TM_TX_COMMITTED = (byte) 'M';

    /** Value of recordType field in a tx prepared record */
    static final byte TX_PREPARED = (byte) 'P';

    /** Value of recordType field in a JCA tx prepared record */
    static final byte JCA_TX_PREPARED = (byte) 'R';

    /** Value of recordType field in a tx end record */
    static final byte TX_END = (byte) 'E';

    /** Value of recordType field in a heuristic status record */
    static final byte HEUR_STATUS = (byte) 'H';

    /** Value of recordType field in a heuristic status record */
    static final byte HEUR_FORGOTTEN = (byte) 'F';

    /** Size of varField directory entry */
    private static final int SIZEOF_DIR_ENTRY = SIZEOF_SHORT + SIZEOF_SHORT;

    /** 
    * Minimum length of a multi-TM tx committed 
    * (<code>MULTI_TM_TX_COMMITTED</code>) record. 
    */
    private static final int MIN_MULTI_TM_TX_COMMITTED_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + SIZEOF_SHORT + CHKSUM_LEN;

    /** 
    * Minimum length of a tx prepared (<code>TX_PREPARED</code>) 
    * or JCA tx prepared (<code>JCA_TX_PREPARED</code>) record.
    */
    private static final int MIN_TX_PREPARED_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + SIZEOF_SHORT + FORMAT_ID_LEN + SIZEOF_SHORT + CHKSUM_LEN;

    /**
    * Fixed length of a single-TM tx committed (<code>TX_COMMITTED</code>) 
    * record. 
    */
    private static final int TX_COMMITED_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + CHKSUM_LEN;

    /** 
    * Fixed length of a tx end (<code>TX_END</code>) record.
    */
    static final int TX_END_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + CHKSUM_LEN;

    /** 
    * Minimum length of a heuristic status (<code>HEUR_STATUS</code>) record. 
    */
    private static final int MIN_HEUR_STATUS_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_BYTE + SIZEOF_BYTE + SIZEOF_BYTE + FORMAT_ID_LEN + SIZEOF_SHORT + SIZEOF_DIR_ENTRY + SIZEOF_DIR_ENTRY + CHKSUM_LEN;

    /** 
    * Fixed length of a heuristic forgotten (HEUR_FORGOTTEN) record.
    */
    static final int HEUR_FORGOTTEN_LEN = HEADER_LEN + SIZEOF_SHORT + SIZEOF_SHORT + SIZEOF_BYTE + SIZEOF_LONG + CHKSUM_LEN;

    /**
    * Structure filled out by the method <code>LogRecord.getData()</code>.
    */
    public static class Data {

        public byte recordType;

        public long localTransactionId;

        public byte[] globalTransactionId;

        public int inboundFormatId;

        public short recCoorTrmiMechId;

        public String recoveryCoordinator;

        public byte[] inboundBranchQualifier;

        public short[] resTrmiMechIds;

        public String[] resources;
    }

    /**
    * Structure filled out by the method <code>LogRecord.getHeurData()</code>.
    */
    public static class HeurData {

        public byte recordType;

        public long localTransactionId;

        public boolean foreignTx;

        public int formatId;

        public byte[] globalTransactionId;

        public byte[] inboundBranchQualifier;

        public int transactionStatus;

        public int heuristicStatusCode;

        public boolean locallyDetectedHeuristicHazard;

        public int[] xaResourceHeuristics;

        public HeuristicStatus[] remoteResourceHeuristics;
    }

    /**
    * Private constructor to enforce non-instantiability.
    */
    private LogRecord() {
    }

    /**
    * Creates a tx committed record for a locally-started transaction that 
    * does not involve other transaction managers.
    *  
    * @param localTransactionId the local id of the transaction
    * @return a <code>ByteBuffer</code> containing the tx committed record. 
    *         The buffer position is set to zero and the buffer limit is set 
    *         to the number of bytes in the commit record.
    */
    static ByteBuffer createTxCommittedRecord(long localTransactionId) {
        ByteBuffer buffer = ByteBuffer.allocate(TX_COMMITED_LEN);
        buffer.put(HEADER).putShort((short) (TX_COMMITED_LEN - FULL_HEADER_LEN)).putShort((short) -(TX_COMMITED_LEN - FULL_HEADER_LEN)).put(TX_COMMITTED).putLong(localTransactionId);
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, SIZEOF_BYTE + SIZEOF_LONG);
        buffer.putInt(TX_COMMITED_LEN - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Creates a multi-TM tx committed record for a distributed transaction.
    * 
    * @param localTransactionId the local id of the transaction
    * @param resTrmiMechIds an array of TRMI mechnism IDs for the remote
    *                       resources (<code>org.xactor.remoting.Resource</code>
    *                       instances) enlisted in the transaction.
    * @param resources an array of stringfied references for the remote
    *                  resources (<code>org.xactor.remoting.Resource</code>
    *                  instances) enlisted in the transaction.
    * @return a <code>ByteBuffer</code> containing the tx committed record. 
    *         The buffer position is set to zero and the buffer limit is set 
    *         to the number of bytes in the record.
    */
    static ByteBuffer createTxCommittedRecord(long localTransactionId, short[] resTrmiMechIds, String[] resources) {
        int recordLen = MIN_MULTI_TM_TX_COMMITTED_LEN;
        int resourceCount = 0;
        if (resources != null && (resourceCount = resources.length) > 0) {
            for (int i = 0; i < resourceCount; i++) {
                recordLen += SIZEOF_DIR_ENTRY + SIZEOF_SHORT + resources[i].length();
            }
        } else throw new RuntimeException("No remote resources were specified");
        ByteBuffer buffer = ByteBuffer.allocate(recordLen);
        buffer.put(HEADER).putShort((short) (recordLen - FULL_HEADER_LEN)).putShort((short) -(recordLen - FULL_HEADER_LEN)).put(MULTI_TM_TX_COMMITTED).putLong(localTransactionId).putShort((short) resourceCount);
        for (int i = 0; i < resourceCount; i++) {
            int offset = buffer.position();
            int length = resources[i].length();
            byte[] resourceBytes = new byte[length];
            resources[i].getBytes(0, length, resourceBytes, 0);
            buffer.putShort(resTrmiMechIds[i]).put(resourceBytes).putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i, (short) (length + SIZEOF_SHORT)).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i, (short) offset);
        }
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, recordLen - FULL_HEADER_LEN - CHKSUM_LEN);
        buffer.putInt(recordLen - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Creates a tx prepared record or a JCA tx prepared record.
    * 
    * @param localTransactionId the local id of the transaction
    * @param inboundFormatId the format id of the foreign <code>Xid</code>
    * @param globalTransactionId the global id of the transaction
    * @param jcaInboundTransaction true if this method should create a JCA tx 
    *                              prepared record, false if this method should
    *                              create a tx prepared record
    * @param recoveryCoordOrInboundBranchQual an stringfied recovery 
    *                  coordinator converted to a byte array (if 
    *                  jcaInboundTransaction is false) or the inbound 
    *                  branch qualifier of a JCA inbound transaction (if 
    *                  jcaInboundTransaction is true)
    * @param resTrmiMechIds an array of TRMI mechnism IDs for the remote
    *                       resources (<code>org.xactor.remoting.Resource</code>
    *                       instances) enlisted in the transaction. 
    * @param resources an array of stringfied references for the remote
    *                  resources (<code>org.xactor.remoting.Resource</code>
    *                  instances) enlisted in the transaction.
    * @return a <code>ByteBuffer</code> containing the tx prepared record or
    *         JCA tx prepared record. The buffer position is set to zero and 
    *         the buffer limit is set to the number of bytes in the record.
    */
    private static ByteBuffer createTxPreparedRecord(long localTransactionId, int inboundFormatId, byte[] globalTransactionId, boolean jcaInboundTransaction, byte[] recoveryCoordOrInboundBranchQual, short[] resTrmiMechIds, String[] resources) {
        int recordLen = MIN_TX_PREPARED_LEN;
        int globalTxIdLen = 0;
        int resourceCount = 0;
        if (globalTransactionId != null && (globalTxIdLen = globalTransactionId.length) > 0) {
            recordLen += globalTxIdLen;
        } else throw new RuntimeException("The global transaction id " + "was not specified");
        if (resources != null && (resourceCount = resources.length) > 0) {
            for (int i = 0; i < resourceCount; i++) {
                recordLen += SIZEOF_DIR_ENTRY + SIZEOF_SHORT + resources[i].length();
            }
        }
        recordLen += SIZEOF_DIR_ENTRY + recoveryCoordOrInboundBranchQual.length;
        ByteBuffer buffer = ByteBuffer.allocate(recordLen);
        buffer.put(HEADER).putShort((short) (recordLen - FULL_HEADER_LEN)).putShort((short) -(recordLen - FULL_HEADER_LEN)).put(jcaInboundTransaction ? JCA_TX_PREPARED : TX_PREPARED).putLong(localTransactionId).putShort((short) (resourceCount + 1)).putInt(inboundFormatId).putShort((short) globalTxIdLen).put(globalTransactionId);
        int offset = buffer.position();
        int length = recoveryCoordOrInboundBranchQual.length;
        buffer.put(recoveryCoordOrInboundBranchQual).putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT, (short) length).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY, (short) offset);
        for (int i = 0; i < resourceCount; ) {
            offset = buffer.position();
            length = resources[i].length();
            byte[] byteArray = new byte[length];
            resources[i].getBytes(0, length, byteArray, 0);
            short trmiMechId = resTrmiMechIds[i];
            i++;
            buffer.putShort(trmiMechId).put(byteArray).putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i, (short) (length + SIZEOF_SHORT)).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i, (short) offset);
        }
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, recordLen - FULL_HEADER_LEN - CHKSUM_LEN);
        buffer.putInt(recordLen - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Creates a tx prepared record for a distributed transaction.
    * 
    * @param localTransactionId the local id of the transaction
    * @param inboundFormatId the format id of the foreign <code>Xid</code>
    * @param globalTransactionId the global id of the transaction
    * @param recCoorTrmiMechId the TRMI mechnism ID for the recovery
    *                          coordinator.
    * @param recoveryCoordinator a stringfied reference for the remote
    *                  coordinator, an 
    *                  <code>org.xactor.remoting.RecoveryCoordinator</code>
    *                  instance
    * @param resTrmiMechIds an array of TRMI mechnism IDs for the remote
    *                       resources (<code>org.xactor.remoting.Resource</code>
    *                       instances) enlisted in the transaction.
    * @param resources an array of stringfied references for the remote
    *                  resources (<code>org.xactor.remoting.Resource</code>
    *                  instances) enlisted in the transaction.
    * @return a <code>ByteBuffer</code> containing the tx prepared record. 
    *         The buffer position is set to zero and the buffer limit is set 
    *         to the number of bytes in the record.
    */
    static ByteBuffer createTxPreparedRecord(long localTransactionId, int inboundFormatId, byte[] globalTransactionId, short recCoorTrmiMechId, String recoveryCoordinator, short[] resTrmiMechIds, String[] resources) {
        int len = recoveryCoordinator.length();
        ByteBuffer coordinatorByteBuffer = ByteBuffer.allocate(len + SIZEOF_SHORT);
        coordinatorByteBuffer.putShort(recCoorTrmiMechId);
        recoveryCoordinator.getBytes(0, len, coordinatorByteBuffer.array(), SIZEOF_SHORT);
        return createTxPreparedRecord(localTransactionId, inboundFormatId, globalTransactionId, false, coordinatorByteBuffer.array(), resTrmiMechIds, resources);
    }

    /**
    * Creates a tx prepared record for a JCA inbound transaction.
    * 
    * @param localTransactionId the local id of the transaction
    * @param inboundXid a foreign <code>Xid</code> instance
    * @param resTrmiMechIds an array of TRMI mechnism IDs for the remote
    *                       resources (<code>org.xactor.remoting.Resource</code>
    *                       instances) enlisted in the transaction.
    * @param resources an array of stringfied references for the remote
    *                  resources (<code>org.xactor.remoting.Resource</code>
    *                  instances) enlisted in the transaction.
    * @return a <code>ByteBuffer</code> containing the JCA tx prepared record.
    *         The buffer position is set to zero and the buffer limit is set 
    *         to the number of bytes in the record.
    */
    static ByteBuffer createJcaTxPreparedRecord(long localTransactionId, Xid inboundXid, short[] resTrmiMechIds, String[] resources) {
        return createTxPreparedRecord(localTransactionId, inboundXid.getFormatId(), inboundXid.getGlobalTransactionId(), true, inboundXid.getBranchQualifier(), resTrmiMechIds, resources);
    }

    /**
    * Creates a tx end record for a distributed transaction.
    * 
    * @param localTransactionId the local id of the transaction.
    * @return a <code>ByteBuffer</code> containing the end record. The
    *         buffer position is set to zero and the buffer limit is set to 
    *         the number of bytes in the end record.
    */
    static ByteBuffer createTxEndRecord(long localTransactionId) {
        ByteBuffer buffer = ByteBuffer.allocate(TX_END_LEN);
        buffer.put(HEADER).putShort((short) (TX_END_LEN - FULL_HEADER_LEN)).putShort((short) -(TX_END_LEN - FULL_HEADER_LEN)).put(TX_END).putLong(localTransactionId);
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, SIZEOF_BYTE + SIZEOF_LONG);
        buffer.putInt(TX_END_LEN - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Creates a heuristic status record for a transaction.
    * 
    * @param localTransactionId the local id of the transaction
    * @param foreignTx true if the transaction is a foreign one, false otherwise
    * @param formatId the format id field of the transaction's <code>Xid</code>
    * @param globalTransactionId the global id field of the transaction's 
    *            <code>Xid</code>
    * @param inboundBranchQualifier the inbound branch qualifier, in the case 
    *            of a foreign transaction that has been imported via JCA 
    *            transaction inflow, or null otherwise   
    * @param transactionStatus the transaction status 
    *             (<code>javax.transaction.Status.STATUS_COMMITTING</code>, or
    *             <code>javax.transaction.Status.STATUS_COMMITTED</code>, or
    *             <code>javax.transaction.Status.STATUS_ROLLING_BACK</code>, or
    *             <code>javax.transaction.Status.STATUS_ROLLEDBACK</code>)
    * @param heurStatusCode the heuristic status code, which takes the same
    *             values as the <code>errorCode</code> field of 
    *             <code>javax.transaction.xa.XAException</code>
    * @param locallyDetectedHeuristicHazard true if a heuristic hazard was 
    *             detected locally and is still outstanding
    * @param xaResourceHeuristics array with the heuristic status codes of
    *                  the XA resources that reported heuristic decisions,
    *                  or null if no XA resources reported heuristic decisions
    * @param remoteResourceHeuristics array with the heuristic status of
    *                  the remote resources that reported heuristic decisions,
    *                  or null if no remote resources reported heuristic 
    *                  decisions
    * @return a <code>ByteBuffer</code> containing the heuristic status record.
    *         The buffer position is set to zero and the buffer limit is set 
    *         to the number of bytes in the record.
    */
    static ByteBuffer createHeurStatusRecord(long localTransactionId, boolean foreignTx, int formatId, byte[] globalTransactionId, byte[] inboundBranchQualifier, int transactionStatus, int heurStatusCode, boolean locallyDetectedHeuristicHazard, int[] xaResourceHeuristics, HeuristicStatus[] remoteResourceHeuristics) {
        int recordLen = MIN_HEUR_STATUS_LEN;
        int globalTxIdLen = 0;
        int remoteResourceHeuristicsCount = 0;
        if (globalTransactionId != null) recordLen += (globalTxIdLen = globalTransactionId.length);
        if (inboundBranchQualifier != null) recordLen += inboundBranchQualifier.length;
        if (xaResourceHeuristics != null) recordLen += xaResourceHeuristics.length;
        if (remoteResourceHeuristics != null) {
            remoteResourceHeuristicsCount = remoteResourceHeuristics.length;
            for (int i = 0; i < remoteResourceHeuristicsCount; i++) {
                recordLen += SIZEOF_DIR_ENTRY + SIZEOF_BYTE + SIZEOF_SHORT + remoteResourceHeuristics[i].resourceRef.length();
            }
        }
        ByteBuffer buffer = ByteBuffer.allocate(recordLen);
        buffer.put(HEADER).putShort((short) (recordLen - FULL_HEADER_LEN)).putShort((short) -(recordLen - FULL_HEADER_LEN)).put(HEUR_STATUS).putLong(localTransactionId).putShort((short) (remoteResourceHeuristicsCount + 2)).put((byte) transactionStatus).put((byte) heurStatusCode).put((locallyDetectedHeuristicHazard) ? (byte) 1 : (byte) 0).put((foreignTx) ? (byte) 1 : (byte) 0).putInt(formatId).putShort((short) globalTxIdLen).put(globalTransactionId);
        int offset, length;
        offset = buffer.position();
        length = (inboundBranchQualifier == null) ? 0 : inboundBranchQualifier.length;
        if (length > 0) buffer.put(inboundBranchQualifier);
        buffer.putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT, (short) length).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY, (short) offset);
        offset = buffer.position();
        length = (xaResourceHeuristics == null) ? 0 : xaResourceHeuristics.length;
        if (length > 0) {
            byte[] xaResHeurCodes = new byte[length];
            for (int i = 0; i < length; i++) xaResHeurCodes[i] = (byte) xaResourceHeuristics[i];
            buffer.put(xaResHeurCodes);
        }
        buffer.putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY, (short) length).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY, (short) offset);
        for (int i = 0; i < remoteResourceHeuristicsCount; i++) {
            String resourceRef = remoteResourceHeuristics[i].resourceRef;
            short resTrmiMechId = remoteResourceHeuristics[i].resTrmiMechId;
            offset = buffer.position();
            length = resourceRef.length();
            byte[] resource = new byte[length];
            resourceRef.getBytes(0, resourceRef.length(), resource, 0);
            buffer.put((byte) remoteResourceHeuristics[i].code).putShort(resTrmiMechId).put(resource).putShort(recordLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * (i + 2), (short) (length + 1 + SIZEOF_SHORT)).putShort(recordLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * (i + 2), (short) offset);
        }
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, recordLen - FULL_HEADER_LEN - CHKSUM_LEN);
        buffer.putInt(recordLen - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Creates a heuristic forgotten record for a transaction.
    * 
    * @param localTransactionId the local id of the transaction.
    * @return a <code>ByteBuffer</code> containing the heuristic forgotten 
    *         record. The buffer position is set to zero and the buffer limit 
    *         is set to the number of bytes in the heuristic forgotten record.
    */
    static ByteBuffer createHeurForgottenRecord(long localTransactionId) {
        ByteBuffer buffer = ByteBuffer.allocate(HEUR_FORGOTTEN_LEN);
        buffer.put(HEADER).putShort((short) (HEUR_FORGOTTEN_LEN - FULL_HEADER_LEN)).putShort((short) -(HEUR_FORGOTTEN_LEN - FULL_HEADER_LEN)).put(HEUR_FORGOTTEN).putLong(localTransactionId);
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), FULL_HEADER_LEN, SIZEOF_BYTE + SIZEOF_LONG);
        buffer.putInt(TX_END_LEN - CHKSUM_LEN, (int) checksum.getValue());
        return (ByteBuffer) buffer.position(0);
    }

    /**
    * Fills out a <code>Data</code> structure with the information taken from
    * the log record in a given <code>ByteBuffer</code>. The log record starts
    * at the beginning of the buffer. It cannot be a log record with heuristic
    * information (i.e., its type cannot be <code>HEUR_STATUS</code> or 
    * <code>HEUR_FORGOTTEN</code>). Its length is known in advance and may
    * be smaller than the number of bytes in the buffer. The magic header and
    * record length fields are not included in the <code>ByteBuffer</code>,
    * whose first byte is the record type of the log record.
    * 
    * @param buffer a <code>ByteBuffer</code> containing the part of a log 
    *               record that starts at the record type field (which is the
    *               first byte of the <code>ByteBuffer</code> and goes until
    *               the checksum field (which is included in the 
    *               <code>ByteBuffer</code> 
    * @param recLen the length of the log record in the buffer
    * @param data a <code>Data</code> structure to be filled out with the
    *               information extracted from the log record.
    */
    static void getData(ByteBuffer buffer, int recLen, Data data) {
        short gidLength;
        short countOfDirEntries;
        if (recLen > buffer.limit()) return;
        int checksumField = buffer.getInt(recLen - CHKSUM_LEN);
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), 0, recLen - CHKSUM_LEN);
        if ((int) checksum.getValue() != checksumField) {
            throw new CorruptedLogRecordException("Wrong checksum.");
        }
        data.recordType = buffer.get();
        switch(data.recordType) {
            case MULTI_TM_TX_COMMITTED:
                data.localTransactionId = buffer.getLong();
                countOfDirEntries = buffer.getShort();
                data.resTrmiMechIds = new short[countOfDirEntries];
                data.resources = new String[countOfDirEntries];
                for (int i = 0; i < countOfDirEntries; i++) {
                    short length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                    short offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                    offset -= FULL_HEADER_LEN;
                    data.resTrmiMechIds[i] = buffer.getShort(offset);
                    data.resources[i] = new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT);
                }
                data.globalTransactionId = null;
                data.inboundFormatId = -1;
                data.recoveryCoordinator = null;
                data.inboundBranchQualifier = null;
                break;
            case TX_PREPARED:
            case JCA_TX_PREPARED:
                data.localTransactionId = buffer.getLong();
                countOfDirEntries = buffer.getShort();
                data.inboundFormatId = buffer.getInt();
                gidLength = buffer.getShort();
                data.globalTransactionId = new byte[gidLength];
                buffer.get(data.globalTransactionId);
                short length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT);
                short offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY);
                offset -= FULL_HEADER_LEN;
                if (data.recordType == TX_PREPARED) {
                    data.recCoorTrmiMechId = buffer.getShort(offset);
                    data.recoveryCoordinator = new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT);
                    data.inboundBranchQualifier = null;
                } else {
                    data.recoveryCoordinator = null;
                    data.inboundBranchQualifier = new byte[length];
                    buffer.get(data.inboundBranchQualifier);
                }
                data.resTrmiMechIds = new short[countOfDirEntries - 1];
                data.resources = new String[countOfDirEntries - 1];
                for (int i = 1; i < countOfDirEntries; i++) {
                    length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                    offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                    offset -= FULL_HEADER_LEN;
                    data.resTrmiMechIds[i - 1] = buffer.getShort(offset);
                    data.resources[i - 1] = new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT);
                }
                break;
            case TX_COMMITTED:
            case TX_END:
                data.localTransactionId = buffer.getLong();
                data.globalTransactionId = null;
                data.inboundFormatId = -1;
                data.recoveryCoordinator = null;
                data.inboundBranchQualifier = null;
                data.resources = null;
                break;
            case HEUR_STATUS:
            case HEUR_FORGOTTEN:
                throw new RuntimeException("Log record with unexpected type");
            default:
                throw new RuntimeException("Log record with invalid type");
        }
    }

    /**
    * Fills out a <code>HeurData</code> structure with the information taken 
    * from the <code>HEUR_STATUS</code> or <code>HEUR_FORGOTTEN</code> log 
    * record in a given <code>ByteBuffer</code>. The log record starts at the 
    * beginning of the buffer. Its length is known in advance and may be 
    * smaller than the number of bytes in the buffer. The magic header and 
    * record length fields are not included in the <code>ByteBuffer</code>,
    * whose first byte is the record type of the log record.
    * 
    * @param buffer a <code>ByteBuffer</code> containing the part of a 
    *               <code>HEUR_STATUS</code> or <code>HEUR_FORGOTTEN</code> log 
    *               record that starts at the record type field (which is the
    *               first byte of the <code>ByteBuffer</code> and goes until
    *               the checksum field (which is included in the 
    *               <code>ByteBuffer</code> 
    * @param recLen the length of the log record in the buffer
    * @param data a <code>HeurData</code> structure to be filled out with the
    *               information extracted from the log record.
    */
    static void getHeurData(ByteBuffer buffer, int recLen, HeurData data) {
        if (recLen > buffer.limit()) return;
        int checksumField = buffer.getInt(recLen - CHKSUM_LEN);
        Checksum checksum = new Adler32();
        checksum.update(buffer.array(), 0, recLen - CHKSUM_LEN);
        if ((int) checksum.getValue() != checksumField) {
            throw new CorruptedLogRecordException("Wrong checksum.");
        }
        data.recordType = buffer.get();
        switch(data.recordType) {
            case HEUR_STATUS:
                data.localTransactionId = buffer.getLong();
                short countOfDirEntries = buffer.getShort();
                data.transactionStatus = buffer.get();
                data.heuristicStatusCode = buffer.get();
                data.locallyDetectedHeuristicHazard = (buffer.get() != 0);
                data.foreignTx = (buffer.get() != 0);
                data.formatId = buffer.getInt();
                int gidLength = buffer.getShort();
                data.globalTransactionId = new byte[gidLength];
                buffer.get(data.globalTransactionId);
                short length, offset;
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT);
                offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY);
                offset -= FULL_HEADER_LEN;
                if (length == 0) data.inboundBranchQualifier = null; else {
                    data.inboundBranchQualifier = new byte[length];
                    buffer.get(data.inboundBranchQualifier);
                }
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY);
                offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY);
                offset -= FULL_HEADER_LEN;
                if (length == 0) data.xaResourceHeuristics = null; else {
                    byte[] xaResHeurCodes = new byte[length];
                    buffer.position(offset);
                    buffer.get(xaResHeurCodes);
                    data.xaResourceHeuristics = new int[length];
                    for (int i = 0; i < length; i++) data.xaResourceHeuristics[i] = xaResHeurCodes[i];
                }
                if (countOfDirEntries > 2) data.remoteResourceHeuristics = new HeuristicStatus[countOfDirEntries - 2]; else data.remoteResourceHeuristics = null;
                for (int i = 2; i < countOfDirEntries; i++) {
                    length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                    offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                    offset -= FULL_HEADER_LEN;
                    byte remoteResourceHeurCode = buffer.get(offset);
                    short remoteResourceTrmiMechId = buffer.getShort(offset + 1);
                    String remoteResourceRef = new String(buffer.array(), 0, offset + 1 + SIZEOF_SHORT, length - 1 - SIZEOF_SHORT);
                    data.remoteResourceHeuristics[i - 2] = new HeuristicStatus(remoteResourceHeurCode, remoteResourceTrmiMechId, remoteResourceRef);
                }
                break;
            case HEUR_FORGOTTEN:
                data.localTransactionId = buffer.getLong();
                data.heuristicStatusCode = 0;
                data.xaResourceHeuristics = null;
                data.remoteResourceHeuristics = null;
                break;
            case MULTI_TM_TX_COMMITTED:
            case TX_PREPARED:
            case JCA_TX_PREPARED:
            case TX_COMMITTED:
            case TX_END:
                throw new RuntimeException("Log record with unexpected type");
            default:
                throw new RuntimeException("Log record with invalid type");
        }
    }

    /**
    * Gets the lenght of the log record that follows the one at the beginning
    * of a given buffer. This method assumes that the header, record lenght,
    * and record length check of the next record follows the current record 
    * in the buffer.
    *   
    * @param buffer a <code>ByteBuffer</code> containing the header, record
    *               lenght, and record length check of the next log record
    * @param currentRecordLen the buffer position at which the header starts.
    * @return the next record length, a short value read from the absolute
    *         buffer position <code>currentRecordLength + HEADER_LEN</code> 
    *                      
    */
    static int getNextRecordLength(ByteBuffer buffer, int currentRecordLength) {
        buffer.position(currentRecordLength);
        if (buffer.remaining() < FULL_HEADER_LEN) return 0; else {
            byte[] header = new byte[HEADER_LEN];
            buffer.get(header);
            if (!Arrays.equals(header, HEADER)) {
                if (Arrays.equals(header, NULL_HEADER)) return 0; else throw new CorruptedLogRecordException("Invalid header.");
            } else {
                short recLen = buffer.getShort();
                short recLenCheck = buffer.getShort();
                if (recLenCheck != -recLen) throw new CorruptedLogRecordException("Record lenght check failed.");
                return recLen;
            }
        }
    }

    /**
    * Receives a byte array containing the part of a log record that follows
    * the header and verifies if the record has a valid checksum.
    *  
    * @param buf a byte array containing the part of a log record that starts
    *            with the record type and ends with the checksum, which is 
    *            included in the array.
    * @return true if the checksum is valid, false otherwise.
    */
    static boolean hasValidChecksum(byte[] buf) {
        int bufLen = buf.length;
        int checksumField = ByteBuffer.wrap(buf).getInt(bufLen - CHKSUM_LEN);
        Checksum checksum = new Adler32();
        checksum.update(buf, 0, bufLen - CHKSUM_LEN);
        return ((int) checksum.getValue() == checksumField);
    }

    /**
    * Returs the string representation of the log record in a buffer. 
    * 
    * @param buffer a <code>ByteBuffer</code> containing a log record, 
    *               with the buffer position set to zero and the buffer limit
    *               set to the number of bytes in the commit record.
    * @return a string that describes the log record.
    */
    static String toString(ByteBuffer buffer) {
        short countOfDirEntries;
        short offset;
        short length;
        short gidLen;
        short trmiMechId;
        byte[] gid;
        int recLen = buffer.limit();
        buffer.position(FULL_HEADER_LEN);
        StringBuffer sb = new StringBuffer("Record Info:\n    Type: ");
        byte recordType = buffer.get();
        switch(recordType) {
            case MULTI_TM_TX_COMMITTED:
                sb.append("MULTI_TM_TX_COMMITTED\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                countOfDirEntries = buffer.getShort();
                if (countOfDirEntries > 0) {
                    sb.append("    Resources:\n");
                    for (int i = 0; i < countOfDirEntries; i++) {
                        length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                        offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                        trmiMechId = buffer.getShort(offset);
                        sb.append("        [TRMIMech=" + trmiMechId + "] ");
                        sb.append(new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT));
                        sb.append("\n");
                    }
                }
                break;
            case TX_PREPARED:
                sb.append("TX_PREPARED\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                countOfDirEntries = buffer.getShort();
                sb.append("    Inbound format id: ");
                sb.append(buffer.getInt());
                sb.append("\n");
                gidLen = buffer.getShort();
                sb.append("    Global transaction id: ");
                gid = new byte[gidLen];
                buffer.get(gid);
                sb.append(new String(gid, 0));
                sb.append("\n");
                sb.append("    Recovery coordinator: ");
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT);
                offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY);
                trmiMechId = buffer.getShort(offset);
                sb.append("[TRMIMech=" + trmiMechId + "] ");
                sb.append(new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT));
                sb.append("\n");
                if (countOfDirEntries > 1) {
                    sb.append("    Resources:\n");
                    for (int i = 1; i < countOfDirEntries; i++) {
                        length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                        offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                        trmiMechId = buffer.getShort(offset);
                        sb.append("        [TRMIMech=" + trmiMechId + "] ");
                        sb.append(new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT));
                    }
                }
                break;
            case JCA_TX_PREPARED:
                sb.append("JCA_TX_PREPARED\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                countOfDirEntries = buffer.getShort();
                sb.append("    Inbound format id: ");
                sb.append(buffer.getInt());
                sb.append("\n");
                gidLen = buffer.getShort();
                sb.append("    Global transaction id: ");
                gid = new byte[gidLen];
                buffer.get(gid);
                sb.append(new String(gid, 0));
                sb.append("\n");
                sb.append("    Inbound branch qualifier: ");
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT);
                offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY);
                sb.append(new String(buffer.array(), 0, offset, length));
                sb.append("\n");
                if (countOfDirEntries > 1) {
                    sb.append("    Resources:\n");
                    for (int i = 1; i < countOfDirEntries; i++) {
                        length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                        offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                        trmiMechId = buffer.getShort(offset);
                        sb.append("        [TRMIMech=" + trmiMechId + "] ");
                        sb.append(new String(buffer.array(), 0, offset + SIZEOF_SHORT, length - SIZEOF_SHORT));
                    }
                }
                break;
            case TX_COMMITTED:
                sb.append("TX_COMMITTED\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                break;
            case TX_END:
                sb.append("TX_END\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                break;
            case HEUR_STATUS:
                sb.append("HEUR_STATUS\n");
                sb.append("    Local transaction id: ");
                sb.append(buffer.getLong());
                sb.append("\n");
                countOfDirEntries = buffer.getShort();
                sb.append("    Transaction status: ");
                byte status = buffer.get();
                sb.append(TxUtils.getStatusAsString(status));
                sb.append("\n");
                sb.append("    Heuristic status: ");
                byte heurCode = buffer.get();
                if (heurCode != 0) sb.append(TxUtils.getXAErrorCodeAsString(heurCode)); else sb.append("NONE");
                sb.append("\n");
                sb.append("    Locally-detected heuristic hazard: ");
                sb.append((buffer.get() != 0) ? "yes" : "no");
                sb.append("\n");
                sb.append("    Foreign transaction: ");
                boolean foreignTransaction = (buffer.get() != 0);
                sb.append((foreignTransaction) ? "yes" : "no");
                sb.append("\n");
                sb.append((foreignTransaction) ? "    Inbound format id: " : "    Format id: ");
                sb.append(buffer.getInt());
                sb.append("\n");
                gidLen = buffer.getShort();
                sb.append("    Global transaction id: ");
                gid = new byte[gidLen];
                buffer.get(gid);
                sb.append(new String(gid, 0));
                sb.append("\n");
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT);
                if (length > 0) {
                    sb.append("    Inbound branch qualifier: ");
                    offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY);
                    sb.append(new String(buffer.array(), 0, offset, length));
                    sb.append("\n");
                }
                length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY);
                if (length > 0) {
                    offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY);
                    sb.append("    XAResource heuristics:\n");
                    for (int i = 0; i < length; i++) {
                        sb.append("        ");
                        heurCode = buffer.get(offset + i);
                        sb.append(TxUtils.getXAErrorCodeAsString(heurCode));
                        sb.append("\n");
                    }
                }
                if (countOfDirEntries > 2) {
                    sb.append("    Remote resource heuristics:\n");
                    for (int i = 2; i < countOfDirEntries; i++) {
                        length = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_SHORT - SIZEOF_DIR_ENTRY * i);
                        offset = buffer.getShort(recLen - CHKSUM_LEN - SIZEOF_DIR_ENTRY - SIZEOF_DIR_ENTRY * i);
                        heurCode = buffer.get(offset);
                        sb.append("        ");
                        sb.append(TxUtils.getXAErrorCodeAsString(heurCode));
                        sb.append(" - ");
                        trmiMechId = buffer.getShort(offset + 1);
                        sb.append("[TRMIMech=" + trmiMechId + "] ");
                        sb.append(new String(buffer.array(), 0, offset + 1 + SIZEOF_SHORT, length - 1 - SIZEOF_SHORT));
                        sb.append("\n");
                    }
                }
                break;
            case HEUR_FORGOTTEN:
                sb.append("HEUR_FORGOTTEN\n");
                sb.append("Local transaction id: ");
                sb.append(buffer.getLong(FULL_HEADER_LEN + SIZEOF_BYTE));
                sb.append("\n");
                break;
            default:
                sb.append("INVALID\n");
                break;
        }
        buffer.position(0);
        return sb.toString();
    }
}
