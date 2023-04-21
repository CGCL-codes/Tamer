package net.sf.dropboxmq;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.naming.NamingException;
import net.sf.dropboxmq.dropboxsupport.TransactionData;
import net.sf.dropboxmq.messages.MessageImpl;
import org.easymock.EasyMock;
import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

/**
 * Created: 01 Sep 2006
 *
 * @author <a href="mailto:dwayne@schultz.net">Dwayne Schultz</a>
 * @version $Revision: 231 $, $Date: 2011-08-12 23:50:47 -0400 (Fri, 12 Aug 2011) $
 */
public class DropboxTransactionTest {

    private static final DropboxTransaction TRANSACTION_MOCK = createStrictMock(DropboxTransaction.class);

    private static final FileSystem FILE_SYSTEM_MOCK = createStrictMock(FileSystem.class);

    private static final BufferedWriter LOG_MOCK = createStrictMock(BufferedWriter.class);

    private static final BufferedReader READER_MOCK = createStrictMock(BufferedReader.class);

    private static final Configuration CONFIGURATION;

    private static final Configuration DELETE_COMPLETE_TRANSACTIONS_CONFIGURATION;

    private static final String TRANSACTION_ID = "trans-id";

    private DropboxTransaction transaction = null;

    static {
        final Map<String, String> environment = new HashMap<String, String>();
        environment.put(Configuration.ROOT_PROPERTY, ".");
        try {
            CONFIGURATION = new Configuration(environment) {

                @Override
                public boolean isDeleteCompleteTransactions() {
                    return false;
                }

                @Override
                public FileSystem getFileSystem() {
                    return FILE_SYSTEM_MOCK;
                }
            };
            DELETE_COMPLETE_TRANSACTIONS_CONFIGURATION = new Configuration(environment) {

                @Override
                public FileSystem getFileSystem() {
                    return FILE_SYSTEM_MOCK;
                }
            };
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() {
        reset(TRANSACTION_MOCK, FILE_SYSTEM_MOCK, LOG_MOCK, READER_MOCK);
    }

    private static void replay() {
        EasyMock.replay(TRANSACTION_MOCK, FILE_SYSTEM_MOCK, LOG_MOCK, READER_MOCK);
    }

    private static void verify() {
        EasyMock.verify(TRANSACTION_MOCK, FILE_SYSTEM_MOCK, LOG_MOCK, READER_MOCK);
    }

    @Test
    public void testIsAutoAcknowledge() {
        assertTrue(new DropboxTransaction(TRANSACTION_ID, false, Session.AUTO_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertTrue(new DropboxTransaction(TRANSACTION_ID, false, Session.DUPS_OK_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertFalse(new DropboxTransaction(TRANSACTION_ID, false, Session.CLIENT_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertFalse(new DropboxTransaction(TRANSACTION_ID, false, Session.SESSION_TRANSACTED, CONFIGURATION).isAutoAcknowledge());
        assertTrue(new DropboxTransaction(TRANSACTION_ID, true, Session.AUTO_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertTrue(new DropboxTransaction(TRANSACTION_ID, true, Session.DUPS_OK_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertFalse(new DropboxTransaction(TRANSACTION_ID, true, Session.CLIENT_ACKNOWLEDGE, CONFIGURATION).isAutoAcknowledge());
        assertFalse(new DropboxTransaction(TRANSACTION_ID, true, Session.SESSION_TRANSACTED, CONFIGURATION).isAutoAcknowledge());
    }

    private void setupStartLocal(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -19239, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            public void startPassively() throws TransactionException {
                TRANSACTION_MOCK.startPassively();
            }
        };
    }

    @Test
    public void testStartLocal_notTransacted() throws JMSException, DropboxTransaction.TransactionException, NamingException {
        setupStartLocal(false);
        replay();
        try {
            transaction.startLocal();
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("Session is not transacted", e.getMessage());
        }
        verify();
    }

    @Test
    public void testStartLocal_notInTrans() throws JMSException, DropboxTransaction.TransactionException, NamingException {
        setupStartLocal(true);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        TRANSACTION_MOCK.startPassively();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        TRANSACTION_MOCK.startPassively();
        replay();
        transaction.startLocal();
        transaction.startLocal();
        verify();
    }

    @Test
    public void testStartLocal_inTrans() throws JMSException, DropboxTransaction.TransactionException, NamingException {
        setupStartLocal(true);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        replay();
        try {
            transaction.startLocal();
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("Transaction already started", e.getMessage());
        }
        verify();
    }

    private void setupStartPassively(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -1939, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            public void start(final String newTransactionId, final boolean isDistributed) throws TransactionException {
                TRANSACTION_MOCK.start(newTransactionId, isDistributed);
            }
        };
    }

    @Test
    public void testStartPassively_alreadyStarted() throws DropboxTransaction.TransactionException, JMSException {
        setupStartPassively(true);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        replay();
        transaction.startPassively();
        verify();
    }

    @Test
    public void testStartPassively_notStarted() throws DropboxTransaction.TransactionException, JMSException {
        setupStartPassively(true);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        TRANSACTION_MOCK.start(TRANSACTION_ID + "-" + 0, false);
        replay();
        transaction.startPassively();
        verify();
    }

    private void setupStart() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -293428, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }
        };
    }

    @Test
    public void testStart_alreadyStarted() throws DropboxTransaction.TransactionException {
        setupStart();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        replay();
        try {
            transaction.start(TRANSACTION_ID, true);
            fail();
        } catch (DropboxTransaction.ProtocolException e) {
            assertEquals("Transaction log already initialized", e.getMessage());
        }
        verify();
    }

    @Test
    public void testStart_inLocalTransaction() throws DropboxTransaction.TransactionException, IOException {
        setupStart();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        replay();
        try {
            transaction.start(TRANSACTION_ID, true);
            fail();
        } catch (DropboxTransaction.InLocalTransactionException e) {
            assertEquals("Transaction log can't be initialized after messages have sent or received", e.getMessage());
        }
        verify();
    }

    @Test
    public void testStart_normal() throws DropboxTransaction.TransactionException, IOException {
        setupStart();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        replay();
        transaction.start(TRANSACTION_ID, false);
        verify();
    }

    private void setupEnd() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -18772, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            File writeTransactionLog() throws IOException, FileSystem.FileSystemException {
                return TRANSACTION_MOCK.writeTransactionLog();
            }
        };
    }

    @Test
    public void testEnd_notInTransaction() throws DropboxTransaction.TransactionException {
        setupEnd();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        replay();
        try {
            transaction.end(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.ProtocolException e) {
            assertEquals("Transaction log not initialized", e.getMessage());
        }
        verify();
    }

    @Test
    public void testEnd_inLocalTransaction() throws DropboxTransaction.TransactionException, IOException {
        setupEnd();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        replay();
        try {
            transaction.end(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.ProtocolException e) {
            assertEquals("Attempting to end distributed transaction while not in a distributed transaction", e.getMessage());
        }
        verify();
    }

    @Test
    public void testEnd_wrongId() throws DropboxTransaction.TransactionException, IOException {
        setupEnd();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        replay();
        transaction.start(TRANSACTION_ID + "1", true);
        try {
            transaction.end(TRANSACTION_ID + "2");
            fail();
        } catch (DropboxTransaction.ProtocolException e) {
            assertEquals("Attempting to end a transaction not matched with the current transaction", e.getMessage());
        }
        verify();
    }

    @Test
    public void testEnd_normal() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        setupEnd();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        expect(TRANSACTION_MOCK.writeTransactionLog()).andReturn(new File("trans-file"));
        FILE_SYSTEM_MOCK.move(new File("trans-file"), new File("./dropboxmq/transaction/unassociated/trans-file"));
        replay();
        transaction.start(TRANSACTION_ID, true);
        transaction.end(TRANSACTION_ID);
        verify();
    }

    @Test
    public void testEnd_ioException() throws IOException, DropboxTransaction.TransactionException, FileSystem.FileSystemException {
        doTestEnd_exception(new IOException("io"), "java.io.IOException: io");
    }

    @Test
    public void testEnd_fileSystemException() throws IOException, DropboxTransaction.TransactionException, FileSystem.FileSystemException {
        doTestEnd_exception(new FileSystem.FileSystemException("file-system"), "net.sf.dropboxmq.FileSystem$FileSystemException: file-system");
    }

    private void doTestEnd_exception(final Throwable throwable, final String expectedMessage) throws IOException, DropboxTransaction.TransactionException, FileSystem.FileSystemException {
        setupEnd();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        expect(TRANSACTION_MOCK.writeTransactionLog()).andThrow(throwable);
        replay();
        transaction.start(TRANSACTION_ID, true);
        try {
            transaction.end(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        verify();
    }

    private void setupWriteTransactionLog() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            void logMessage(final String type, final TransactionData data, final BufferedWriter transactionLog) throws IOException {
                TRANSACTION_MOCK.logMessage(type, data, transactionLog);
            }
        };
    }

    @Test
    public void testWriteTransactionLog_transaction() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        setupWriteTransactionLog();
        final DropboxTransaction.LogData data = transaction.getLogData();
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message1"), new File("./sent-commit1"), null, new File("./sent-rollback1")), data, 1, null);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message2"), new File("./sent-commit2"), null, new File("./sent-rollback2")), data, 2, null);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-commit1"), new File("./received-target1"), new File("./received-rollback1")), data, 3, null);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-commit2"), new File("./received-target2"), new File("./received-rollback2")), data, 4, null);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        expect(FILE_SYSTEM_MOCK.newBufferedWriter(new File("./dropboxmq/transaction/associated/xa-null"))).andReturn(LOG_MOCK);
        TRANSACTION_MOCK.logMessage("sent", new TransactionData(new File("./sent-message1"), new File("./sent-commit1"), null, new File("./sent-rollback1")), LOG_MOCK);
        TRANSACTION_MOCK.logMessage("sent", new TransactionData(new File("./sent-message2"), new File("./sent-commit2"), null, new File("./sent-rollback2")), LOG_MOCK);
        TRANSACTION_MOCK.logMessage("received", new TransactionData(new File("./received-message1"), new File("./received-commit1"), new File("./received-target1"), new File("./received-rollback1")), LOG_MOCK);
        TRANSACTION_MOCK.logMessage("received", new TransactionData(new File("./received-message2"), new File("./received-commit2"), new File("./received-target2"), new File("./received-rollback2")), LOG_MOCK);
        LOG_MOCK.close();
        replay();
        transaction.writeTransactionLog();
        verify();
    }

    @Test
    public void testWriteTransactionLog_ioException() throws IOException, FileSystem.FileSystemException {
        setupWriteTransactionLog();
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        expect(FILE_SYSTEM_MOCK.newBufferedWriter(new File("./dropboxmq/transaction/associated/xa-null"))).andReturn(LOG_MOCK);
        LOG_MOCK.close();
        expectLastCall().andThrow(new IOException("io-exception"));
        replay();
        transaction.writeTransactionLog();
        verify();
    }

    @Test
    public void testLogMessage_normal() throws DropboxTransaction.TransactionException, IOException, JMSException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -293428, CONFIGURATION);
        final String currentWorkingDir = new File(".").getAbsolutePath();
        LOG_MOCK.write("dropboxmq --creator-version 1.1 --type type1" + " --message-file " + currentWorkingDir + "/message-file1" + " --rollback-file " + currentWorkingDir + "/rollback-file ");
        LOG_MOCK.newLine();
        LOG_MOCK.write("dropboxmq --creator-version 1.1 --type type2" + " --message-file " + currentWorkingDir + "/message-file2" + " --original-file " + currentWorkingDir + "/target-file" + " --commit-file " + currentWorkingDir + "/commit-file");
        LOG_MOCK.newLine();
        replay();
        final TransactionData data1 = new TransactionData(new File("./message-file1"), null, null, new File("./rollback-file "));
        transaction.logMessage("type1", data1, LOG_MOCK);
        final TransactionData data2 = new TransactionData(new File("./message-file2"), new File("./target-file"), new File("./commit-file"), null);
        transaction.logMessage("type2", data2, LOG_MOCK);
        verify();
    }

    private void setupResume() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            void readTransactionLog(final File transactionFile, final LogData localLogData) throws IOException, TransactionException {
                TRANSACTION_MOCK.readTransactionLog(transactionFile, localLogData);
            }
        };
    }

    @Test
    public void testResume_alreadyStarted() throws DropboxTransaction.TransactionException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        replay();
        try {
            transaction.resume(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.ProtocolException e) {
            assertEquals("Transaction log already initialized", e.getMessage());
        }
        verify();
    }

    @Test
    public void testResume_inLocalTransaction() throws DropboxTransaction.TransactionException, IOException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        replay();
        try {
            transaction.resume(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.InLocalTransactionException e) {
            assertEquals("Transaction log can't be initialized after messages have sent or received", e.getMessage());
        }
        verify();
    }

    @Test
    public void testResume_normal() throws DropboxTransaction.TransactionException, IOException, FileSystem.FileSystemException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        FILE_SYSTEM_MOCK.move(new File("./dropboxmq/transaction/unassociated/xa-" + TRANSACTION_ID), new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID));
        TRANSACTION_MOCK.readTransactionLog(new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID), transaction.getLogData());
        replay();
        transaction.resume(TRANSACTION_ID);
        verify();
    }

    @Test
    public void testResume_sourceNotFound() throws DropboxTransaction.TransactionException, IOException, FileSystem.FileSystemException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        FILE_SYSTEM_MOCK.move(new File("./dropboxmq/transaction/unassociated/xa-" + TRANSACTION_ID), new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID));
        expectLastCall().andThrow(new FileSystem.SourceNotFoundException("src-not-found"));
        replay();
        try {
            transaction.resume(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.NotAnXIDException e) {
            assertEquals("Could not find unassocated transaction instanceId trans-id", e.getMessage());
        }
        verify();
    }

    @Test
    public void testResume_fileSystemException() throws DropboxTransaction.TransactionException, IOException, FileSystem.FileSystemException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        FILE_SYSTEM_MOCK.move(new File("./dropboxmq/transaction/unassociated/xa-" + TRANSACTION_ID), new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID));
        expectLastCall().andThrow(new FileSystem.FileSystemException("file-system"));
        replay();
        try {
            transaction.resume(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("net.sf.dropboxmq.FileSystem$FileSystemException: file-system", e.getMessage());
        }
        verify();
    }

    @Test
    public void testResume_ioException() throws DropboxTransaction.TransactionException, IOException, FileSystem.FileSystemException {
        setupResume();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        FILE_SYSTEM_MOCK.move(new File("./dropboxmq/transaction/unassociated/xa-" + TRANSACTION_ID), new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID));
        TRANSACTION_MOCK.readTransactionLog(new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID), transaction.getLogData());
        expectLastCall().andThrow(new IOException("io"));
        replay();
        try {
            transaction.resume(TRANSACTION_ID);
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("java.io.IOException: io", e.getMessage());
        }
        verify();
    }

    @Test
    public void testReadTransactionLog_normal() throws IOException, DropboxTransaction.TransactionException {
        doTestReadTransactionLog(false);
    }

    @Test
    public void testReadTransactionLog_ioExcption() throws IOException, DropboxTransaction.TransactionException {
        doTestReadTransactionLog(true);
    }

    private void doTestReadTransactionLog(final boolean ioExceptionOnClose) throws IOException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            void parseLine(final String line, final LogData localLogData, final int lineNo, final File transactionFile) throws TransactionException {
                TRANSACTION_MOCK.parseLine(line, localLogData, lineNo, transactionFile);
            }
        };
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        final File logFile = new File("./dropboxmq/transaction/associated/xa-" + TRANSACTION_ID);
        expect(FILE_SYSTEM_MOCK.newBufferedReader(logFile)).andReturn(READER_MOCK);
        expect(READER_MOCK.readLine()).andReturn("line-1");
        TRANSACTION_MOCK.parseLine("line-1", transaction.getLogData(), 1, logFile);
        expect(READER_MOCK.readLine()).andReturn("line-2");
        TRANSACTION_MOCK.parseLine("line-2", transaction.getLogData(), 2, logFile);
        expect(READER_MOCK.readLine()).andReturn(null);
        READER_MOCK.close();
        if (ioExceptionOnClose) {
            expectLastCall().andThrow(new IOException("io"));
        }
        replay();
        transaction.start(TRANSACTION_ID, true);
        transaction.readTransactionLog(logFile, transaction.getLogData());
        verify();
    }

    private void setupParseLine() {
        setUp();
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            void addParsedLine(final String type, final TransactionData data, final LogData localLogData, final int lineNo, final File transactionFile) throws TransactionException {
                TRANSACTION_MOCK.addParsedLine(type, data, localLogData, lineNo, transactionFile);
            }
        };
    }

    @Test
    public void testParseLine_normal() throws DropboxTransaction.TransactionException {
        doTestParseLine("aksk --creator-version 1.1 --message-file xxx", 434, null, "xxx", null, null, null);
        doTestParseLine("asdfghjkl --type x y z --creator-version 1.1 --message-file xxx", -1, "x y z", "xxx", null, null, null);
        doTestParseLine("qwertyuiop --message-file 234546 ghfgg --creator-version 1.1", 1, null, "234546 ghfgg", null, null, null);
        doTestParseLine("zxcvbnm --creator-version 1.1 --message-file xxx --commit-file m 8n6 cv 3x", 1, null, "xxx", null, "m 8n6 cv 3x", null);
        doTestParseLine("zxcvbnm --creator-version 1.1 --message-file xxx --original-file kksksdjs asksa", 1, null, "xxx", "kksksdjs asksa", null, null);
        doTestParseLine("zxcvbnm --rollback-file /xyz/123/8765 --creator-version 1.1 --message-file xxx", 1, null, "xxx", null, null, "/xyz/123/8765");
        doTestParseLine("asdfghjkl --type x y z --message-file 234546 ghfgg --commit-file m 8n6 cv 3x" + " --original-file jsj adsjjkas jajs --rollback-file /xyz/123/8765 --creator-version 1.1", 345566566, "x y z", "234546 ghfgg", "jsj adsjjkas jajs", "m 8n6 cv 3x", "/xyz/123/8765");
        doTestParseLine("asdfghjkl --original-file 8283 28292 2919 199 --creator-version 1.1" + " --rollback-file /xyz/123/8765 --commit-file m 8n6 cv 3x \t--message-file 234546 ghfgg --type x y z", 345566566, "x y z", "234546 ghfgg", "8283 28292 2919 199", "m 8n6 cv 3x", "/xyz/123/8765");
    }

    private void doTestParseLine(final String line, final int lineNo, final String type, final String messageFileString, final String originalFileString, final String commitFileString, final String rollbackFileString) throws DropboxTransaction.TransactionException {
        setupParseLine();
        final File messageFile = messageFileString == null ? null : new File(messageFileString);
        final File originalFile = originalFileString == null ? null : new File(originalFileString);
        final File commitFile = commitFileString == null ? null : new File(commitFileString);
        final File rollbackFile = rollbackFileString == null ? null : new File(rollbackFileString);
        TRANSACTION_MOCK.addParsedLine(type, new TransactionData(messageFile, originalFile, commitFile, rollbackFile), transaction.getLogData(), lineNo, new File("transaction-file"));
        replay();
        transaction.parseLine(line, transaction.getLogData(), lineNo, new File("transaction-file"));
        verify();
    }

    @Test
    public void testParseLine_badOption() {
        setupParseLine();
        replay();
        try {
            transaction.parseLine("zxcvbnm --garbage-dir m 8n6 cv 3x", transaction.getLogData(), 828, new File("transaction-file"));
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("Invalid option on line 828 of transaction-file", e.getMessage());
        }
        verify();
    }

    @Test
    public void testAddParsedLine_nullMessageFile() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        replay();
        try {
            transaction.addParsedLine("sent", new TransactionData(null, new File("commit-file"), new File("target-file"), new File("rollback-file")), transaction.getLogData(), 35454, new File("transaction-file"));
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("Undefined message file on line 35454 of transaction-file", e.getMessage());
        }
        verify();
    }

    @Test
    public void testAddParsedLine_normal() throws DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        replay();
        transaction.addParsedLine("sent", new TransactionData(new File("message-file1"), new File("commit-file1"), new File("target-file1"), new File("rollback-file1")), transaction.getLogData(), 35454, new File("transaction-file"));
        transaction.addParsedLine("received", new TransactionData(new File("message-file2"), new File("commit-file2"), new File("target-file2"), new File("rollback-file2")), transaction.getLogData(), 35455, new File("transaction-file"));
        verify();
    }

    @Test
    public void testAddParsedLine_badType() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        replay();
        try {
            transaction.addParsedLine("bad-type", new TransactionData(new File("message-file"), new File("commit-file"), new File("target-file"), new File("rollback-file")), transaction.getLogData(), 35454, new File("transaction-file"));
            fail();
        } catch (DropboxTransaction.TransactionException e) {
            assertEquals("Invalid or undefined type on line 35454 of transaction-file", e.getMessage());
        }
        verify();
    }

    private void setupCommit(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -182371, CONFIGURATION) {

            @Override
            void checkCommitOrRollback(final boolean distributedInvocation) throws JMSException {
                TRANSACTION_MOCK.checkCommitOrRollback(distributedInvocation);
            }

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            File writeTransactionLog() throws IOException, FileSystem.FileSystemException {
                return TRANSACTION_MOCK.writeTransactionLog();
            }

            @Override
            LogData newLogData() {
                return TRANSACTION_MOCK.newLogData();
            }

            @Override
            void readTransactionLog(final File transactionFile, final LogData localLogData) throws IOException, TransactionException {
                TRANSACTION_MOCK.readTransactionLog(transactionFile, localLogData);
            }

            @Override
            void doCommit(final File preCommittingFile, final LogData localLogData) throws FileSystem.FileSystemException {
                TRANSACTION_MOCK.doCommit(preCommittingFile, localLogData);
            }
        };
    }

    @Test
    public void testCommit_normal() throws JMSException, IOException, FileSystem.FileSystemException {
        setupCommit(true);
        TRANSACTION_MOCK.checkCommitOrRollback(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        final File preCommittingFile = new File("pre-committing-file");
        expect(TRANSACTION_MOCK.writeTransactionLog()).andReturn(preCommittingFile);
        TRANSACTION_MOCK.doCommit(preCommittingFile, transaction.getLogData());
        replay();
        transaction.commit();
        verify();
    }

    @Test
    public void testCommit_distributedOnePhase() throws JMSException, FileSystem.FileSystemException, DropboxTransaction.TransactionException, IOException {
        setupCommit(true);
        TRANSACTION_MOCK.checkCommitOrRollback(true);
        final DropboxTransaction.LogData data = new DropboxTransaction.LogData();
        expect(TRANSACTION_MOCK.newLogData()).andReturn(data);
        final File transactionFile = new File("./dropboxmq/transaction/unassociated/xa-trans-id293923");
        TRANSACTION_MOCK.readTransactionLog(transactionFile, data);
        TRANSACTION_MOCK.doCommit(transactionFile, data);
        replay();
        transaction.commit("trans-id293923", true);
        verify();
    }

    @Test
    public void testCommit_distributedTwoPhase() throws JMSException, IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        setupCommit(true);
        TRANSACTION_MOCK.checkCommitOrRollback(true);
        final DropboxTransaction.LogData data = new DropboxTransaction.LogData();
        expect(TRANSACTION_MOCK.newLogData()).andReturn(data);
        final File transactionFile = new File("./dropboxmq/transaction/prepared/xa-trans-id2838383");
        TRANSACTION_MOCK.readTransactionLog(transactionFile, data);
        TRANSACTION_MOCK.doCommit(transactionFile, data);
        replay();
        transaction.commit("trans-id2838383", false);
        verify();
    }

    @Test
    public void testCommit_fileSystemException() throws JMSException, FileSystem.FileSystemException, IOException {
        setupCommit(true);
        TRANSACTION_MOCK.checkCommitOrRollback(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        final File transactionFile = new File("trans-file");
        expect(TRANSACTION_MOCK.writeTransactionLog()).andReturn(transactionFile);
        TRANSACTION_MOCK.doCommit(transactionFile, transaction.getLogData());
        expectLastCall().andThrow(new FileSystem.FileSystemException("file-system"));
        replay();
        try {
            transaction.commit();
            fail();
        } catch (DropboxMQJMSException e) {
            assertEquals("file-system", e.getMessage());
        }
        verify();
    }

    private void setupCheckCommitOrRollback(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -182371, CONFIGURATION) {

            @Override
            void checkClosed() throws IllegalStateException {
                TRANSACTION_MOCK.checkClosed();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }
        };
    }

    @Test
    public void testCheckCommitOrRollback_notTransacted() throws JMSException {
        setupCheckCommitOrRollback(false);
        TRANSACTION_MOCK.checkClosed();
        replay();
        try {
            transaction.checkCommitOrRollback(true);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Attempt to call commit() or rollback() on a non-transacted session", e.getMessage());
        }
        verify();
    }

    @Test
    public void testCheckCommitOrRollback_notInTransaction() throws JMSException {
        setupCheckCommitOrRollback(true);
        TRANSACTION_MOCK.checkClosed();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(false);
        replay();
        transaction.checkCommitOrRollback(false);
        verify();
    }

    @Test
    public void testCheckCommitOrRollback_notLocal() throws IllegalStateException {
        setupCheckCommitOrRollback(true);
        TRANSACTION_MOCK.checkClosed();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        replay();
        try {
            transaction.checkCommitOrRollback(false);
            fail();
        } catch (JMSException e) {
            assertEquals("Attempting to locally commit or rollback a distributed transaction", e.getMessage());
        }
        verify();
    }

    @Test
    public void testCheckCommitOrRollback_normal() throws JMSException {
        setupCheckCommitOrRollback(true);
        TRANSACTION_MOCK.checkClosed();
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        replay();
        transaction.checkCommitOrRollback(false);
        verify();
    }

    @Test
    public void testCheckClosed_notClosed() throws IllegalStateException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        transaction.checkClosed();
    }

    @Test
    public void testCheckClosed_closed() throws JMSException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        transaction.close();
        try {
            transaction.checkClosed();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Connection is currently in a closed state", e.getMessage());
        }
    }

    @Test
    public void testDoCommit_normal() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, true, -182371, CONFIGURATION);
        final File preCommittingFile = new File("./pre-committing-dir/trans-file");
        final File committingFile = new File("./dropboxmq/transaction/committing/trans-file");
        FILE_SYSTEM_MOCK.move(preCommittingFile, committingFile);
        final DropboxTransaction.LogData data = transaction.getLogData();
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message1"), new File("./sent-target1"), new File("./sent-commit1"), new File("./sent-rollback1")), data, 1, committingFile);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message2"), new File("./sent-target2"), new File("./sent-commit2"), new File("./sent-rollback2")), data, 2, committingFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-target1"), new File("./received-commit1"), new File("./received-rollback1")), data, 3, committingFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target2"), new File("./received-commit2"), new File("./received-rollback2")), data, 4, committingFile);
        FILE_SYSTEM_MOCK.move(new File("./sent-message1"), new File("./sent-commit1"));
        FILE_SYSTEM_MOCK.move(new File("./sent-message2"), new File("./sent-commit2"));
        FILE_SYSTEM_MOCK.move(new File("./received-message1"), new File("./received-commit1"));
        FILE_SYSTEM_MOCK.move(new File("./received-message2"), new File("./received-commit2"));
        FILE_SYSTEM_MOCK.move(committingFile, new File("./dropboxmq/transaction/committed/trans-file"));
        replay();
        transaction.doCommit(preCommittingFile, data);
        verify();
    }

    @Test
    public void testDoCommit_deleteTransactionFile() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, true, -182371, DELETE_COMPLETE_TRANSACTIONS_CONFIGURATION);
        final File preCommittingFile = new File("./pre-committing-dir/trans-file");
        FILE_SYSTEM_MOCK.move(preCommittingFile, new File("./dropboxmq/transaction/committing/trans-file"));
        FILE_SYSTEM_MOCK.delete(new File("./dropboxmq/transaction/committing/trans-file"));
        replay();
        transaction.doCommit(preCommittingFile, transaction.getLogData());
        verify();
    }

    private void setupRollback(final boolean transacted) throws IOException, FileSystem.FileSystemException, JMSException {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -182371, CONFIGURATION) {

            @Override
            void checkCommitOrRollback(final boolean distributedInvocation) throws JMSException {
                TRANSACTION_MOCK.checkCommitOrRollback(distributedInvocation);
            }

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            File writeTransactionLog() throws IOException, FileSystem.FileSystemException {
                return TRANSACTION_MOCK.writeTransactionLog();
            }

            @Override
            void doRollback(final File preRollbackFile, final LogData localLogData) throws IOException, FileSystem.FileSystemException {
                TRANSACTION_MOCK.doRollback(preRollbackFile, localLogData);
            }
        };
        TRANSACTION_MOCK.checkCommitOrRollback(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        expect(TRANSACTION_MOCK.writeTransactionLog()).andReturn(new File("trans-file"));
    }

    @Test
    public void testRollback_normal() throws JMSException, IOException, FileSystem.FileSystemException {
        setupRollback(true);
        TRANSACTION_MOCK.doRollback(new File("trans-file"), transaction.getLogData());
        replay();
        transaction.rollback();
        verify();
    }

    @Test
    public void testRollback_ioException() throws JMSException, IOException, FileSystem.FileSystemException {
        doTestRollback_doRollbackException(new IOException("io"), "io");
    }

    @Test
    public void testRollback_fileSystemException() throws JMSException, IOException, FileSystem.FileSystemException {
        doTestRollback_doRollbackException(new FileSystem.FileSystemException("file-system"), "file-system");
    }

    private void doTestRollback_doRollbackException(final Throwable throwable, final String expectedMessage) throws JMSException, IOException, FileSystem.FileSystemException {
        setupRollback(true);
        TRANSACTION_MOCK.doRollback(new File("trans-file"), transaction.getLogData());
        expectLastCall().andThrow(throwable);
        replay();
        try {
            transaction.rollback();
            fail();
        } catch (DropboxMQJMSException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        verify();
    }

    @Test
    public void testDoRollback_transacted() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            void doRecover(final File rollingBackFile, final LogData localLogData) throws FileSystem.FileSystemException {
                TRANSACTION_MOCK.doRecover(rollingBackFile, localLogData);
            }
        };
        final File preRollbackFile = new File("./pre-rolling-back-dir/trans-file");
        final File rollingBackFile = new File("./dropboxmq/transaction/rolling-back/trans-file");
        FILE_SYSTEM_MOCK.move(preRollbackFile, rollingBackFile);
        final DropboxTransaction.LogData data = transaction.getLogData();
        final File transactionFile = new File("transaction-file");
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message1"), new File("./sent-target1"), new File("./sent-commit1"), new File("./sent-rollback1")), data, 1, transactionFile);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message2"), new File("./sent-target2"), new File("./sent-commit2"), new File("./sent-rollback2")), data, 2, transactionFile);
        FILE_SYSTEM_MOCK.delete(new File("./sent-message1"));
        FILE_SYSTEM_MOCK.delete(new File("./sent-message2"));
        TRANSACTION_MOCK.doRecover(rollingBackFile, data);
        replay();
        transaction.doRollback(preRollbackFile, data);
        verify();
    }

    private void setupRecover(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -182371, CONFIGURATION) {

            @Override
            File writeTransactionLog() throws IOException, FileSystem.FileSystemException {
                return TRANSACTION_MOCK.writeTransactionLog();
            }

            @Override
            void doRecover(final File rollingBackFile, final LogData localLogData) throws FileSystem.FileSystemException {
                TRANSACTION_MOCK.doRecover(rollingBackFile, localLogData);
            }
        };
    }

    @Test
    public void testRecover_transacted() throws JMSException {
        setupRecover(true);
        replay();
        try {
            transaction.recover();
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Attempt to call recover() on a transacted session", e.getMessage());
        }
        verify();
    }

    @Test
    public void testRecover_normal() throws FileSystem.FileSystemException, JMSException, IOException, DropboxTransaction.TransactionException {
        setupRecover(false);
        final DropboxTransaction.LogData data = transaction.getLogData();
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target-dir2"), new File("./received-commit2"), new File("./received-rollback2")), data, 4, new File("trans-file"));
        expect(TRANSACTION_MOCK.writeTransactionLog()).andReturn(new File("trans-file"));
        final File rollingBackFile = new File("./dropboxmq/transaction/rolling-back/trans-file");
        FILE_SYSTEM_MOCK.move(new File("trans-file"), rollingBackFile);
        TRANSACTION_MOCK.doRecover(rollingBackFile, transaction.getLogData());
        replay();
        transaction.recover();
        verify();
    }

    @Test
    public void testRecover_fileSystemException() throws JMSException, FileSystem.FileSystemException, IOException, DropboxTransaction.TransactionException {
        doTestRecover_writeTransactionLogException(new FileSystem.FileSystemException("file-system"), "file-system");
    }

    @Test
    public void testRecover_ioException() throws JMSException, FileSystem.FileSystemException, IOException, DropboxTransaction.TransactionException {
        doTestRecover_writeTransactionLogException(new IOException("io-system"), "io-system");
    }

    private void doTestRecover_writeTransactionLogException(final Throwable throwable, final String expectedMessage) throws JMSException, FileSystem.FileSystemException, IOException, DropboxTransaction.TransactionException {
        setupRecover(false);
        final DropboxTransaction.LogData data = transaction.getLogData();
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-commit2"), new File("./received-target-dir2"), new File("./received-rollback2")), data, 4, new File("trans-file"));
        expect(TRANSACTION_MOCK.writeTransactionLog()).andThrow(throwable);
        replay();
        try {
            transaction.recover();
            fail();
        } catch (DropboxMQJMSException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
        verify();
    }

    @Test
    public void testDoRecover_transacted() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, true, -182371, CONFIGURATION);
        final DropboxTransaction.LogData data = transaction.getLogData();
        final File transactionFile = new File("transaction-file");
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-target1"), new File("./received-commit1"), new File("./received-rollback1-dir/received-message1")), data, 1, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target2"), new File("./received-commit2"), new File("./received-rollback2-dir/received-message2")), data, 2, transactionFile);
        FILE_SYSTEM_MOCK.move(new File("./received-message1"), new File("./received-rollback1-dir/received-message1"));
        FILE_SYSTEM_MOCK.move(new File("./received-message2"), new File("./received-rollback2-dir/received-message2"));
        FILE_SYSTEM_MOCK.move(new File("rolling-back-file"), new File("./dropboxmq/transaction/rolled-back/rolling-back-file"));
        replay();
        transaction.doRecover(new File("rolling-back-file"), data);
        verify();
    }

    @Test
    public void testDoRecover_deleteTransacted() throws IOException, FileSystem.FileSystemException, DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, true, -182371, DELETE_COMPLETE_TRANSACTIONS_CONFIGURATION);
        FILE_SYSTEM_MOCK.delete(new File("rolling-back-file"));
        replay();
        transaction.doRecover(new File("rolling-back-file"), transaction.getLogData());
        verify();
    }

    private void setupAcknowledge(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -182371, CONFIGURATION) {

            @Override
            boolean isAutoAcknowledge() {
                return TRANSACTION_MOCK.isAutoAcknowledge();
            }
        };
    }

    @Test
    public void testAcknowledge_autoAcknowledge() throws DropboxTransaction.TransactionException, JMSException {
        setupAcknowledge(false);
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(true);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message"), new File("./received-commit"), new File("./received-target"), new File("./received-rollback-dir")), transaction.getLogData(), 1, new File("transaction-file"));
        replay();
        transaction.acknowledge(new File("./received-message"));
        verify();
    }

    @Test
    public void testAcknowledge_transacted() throws DropboxTransaction.TransactionException, JMSException {
        setupAcknowledge(true);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message"), new File("./received-commit"), new File("./received-target"), new File("./received-rollback-dir")), transaction.getLogData(), 1, new File("transaction-file"));
        replay();
        transaction.acknowledge(new File("./received-message"));
        verify();
    }

    @Test
    public void testAcknowledge_unknownMessage() throws DropboxTransaction.TransactionException, JMSException {
        setupAcknowledge(false);
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(false);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message"), new File("./received-commit"), new File("./received-target"), new File("./received-rollback-dir")), transaction.getLogData(), 1, new File("transaction-file"));
        replay();
        transaction.acknowledge(new File("./unreceived-message"));
        verify();
    }

    @Test
    public void testAcknowledge_normal() throws DropboxTransaction.TransactionException, JMSException, FileSystem.FileSystemException {
        setupAcknowledge(false);
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(false);
        final DropboxTransaction.LogData data = transaction.getLogData();
        final File transactionFile = new File("transaction-file");
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-target1"), new File("./received-commit1"), new File("./received-rollback-dir1")), data, 1, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target2"), new File("./received-commit2"), new File("./received-rollback-dir2")), data, 2, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message3"), new File("./received-target3"), new File("./received-commit3"), new File("./received-rollback-dir3")), data, 3, transactionFile);
        FILE_SYSTEM_MOCK.move(new File("./received-message1"), new File("./received-commit1"));
        FILE_SYSTEM_MOCK.move(new File("./received-message2"), new File("./received-commit2"));
        replay();
        transaction.acknowledge(new File("./received-message2"));
        verify();
    }

    @Test
    public void testAcknowledge_fileSystemException() throws DropboxTransaction.TransactionException, JMSException, FileSystem.FileSystemException {
        setupAcknowledge(false);
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(false);
        final DropboxTransaction.LogData data = transaction.getLogData();
        final File transactionFile = new File("transaction-file");
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-target1"), new File("./received-commit1"), new File("./received-rollback-dir1")), data, 1, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target2"), new File("./received-commit2"), new File("./received-rollback-dir2")), data, 2, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message3"), new File("./received-target3"), new File("./received-commit3"), new File("./received-rollback-dir3")), data, 3, transactionFile);
        FILE_SYSTEM_MOCK.move(new File("./received-message1"), new File("./received-commit1"));
        FILE_SYSTEM_MOCK.move(new File("./received-message2"), new File("./received-commit2"));
        expectLastCall().andThrow(new FileSystem.FileSystemException("file-system-exception"));
        replay();
        try {
            transaction.acknowledge(new File("./received-message2"));
            fail();
        } catch (JMSException e) {
            assertEquals("file-system-exception", e.getMessage());
        }
        verify();
    }

    private void setupClose() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION) {

            @Override
            boolean isInTransaction() {
                return TRANSACTION_MOCK.isInTransaction();
            }

            @Override
            boolean isDistributed() {
                return TRANSACTION_MOCK.isDistributed();
            }

            @Override
            public void rollback() throws JMSException {
                TRANSACTION_MOCK.rollback();
            }

            @Override
            public void recover() throws JMSException {
                TRANSACTION_MOCK.recover();
            }
        };
    }

    @Test
    public void testClose_local() throws IOException, FileSystem.FileSystemException, JMSException {
        setupClose();
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        TRANSACTION_MOCK.rollback();
        replay();
        transaction.close();
        verify();
    }

    @Test
    public void testClose_distributed() throws IOException, FileSystem.FileSystemException, JMSException {
        setupClose();
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(true);
        replay();
        transaction.close();
        verify();
    }

    @Test
    public void testClose_jmsException() throws JMSException {
        setupClose();
        expect(TRANSACTION_MOCK.isDistributed()).andReturn(false);
        expect(TRANSACTION_MOCK.isInTransaction()).andReturn(true);
        TRANSACTION_MOCK.rollback();
        expectLastCall().andThrow(new JMSException("xyz"));
        replay();
        try {
            transaction.close();
            fail();
        } catch (JMSException e) {
            assertEquals("xyz", e.getMessage());
        }
        verify();
    }

    private void setupAddMessage(final boolean transacted) {
        transaction = new DropboxTransaction(TRANSACTION_ID, transacted, -1929, CONFIGURATION) {

            @Override
            void checkClosed() throws IllegalStateException {
                TRANSACTION_MOCK.checkClosed();
            }

            @Override
            boolean isAutoAcknowledge() {
                return TRANSACTION_MOCK.isAutoAcknowledge();
            }

            @Override
            void startPassively() throws TransactionException {
                TRANSACTION_MOCK.startPassively();
            }

            @Override
            void logMessage(final String type, final TransactionData data, final BufferedWriter transactionLog) throws IOException {
                TRANSACTION_MOCK.logMessage(type, data, transactionLog);
            }
        };
    }

    @Test
    public void testAddSentMessage_alreadyAddedSent() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddSentMessage_alreadyAdded("sent");
    }

    @Test
    public void testAddSentMessage_alreadyAddedReceived() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddSentMessage_alreadyAdded("received");
    }

    private void doTestAddSentMessage_alreadyAdded(final String type) throws JMSException, DropboxTransaction.TransactionException {
        setupAddMessage(true);
        TRANSACTION_MOCK.checkClosed();
        transaction.addParsedLine(type, new TransactionData(new File("./sent-message"), new File("./sent-commit"), null, null), transaction.getLogData(), 1, new File("transaction-file"));
        replay();
        try {
            transaction.addSentMessage(new File("./sent-message"), new File("./commit-file"));
            fail();
        } catch (JMSException e) {
            assertEquals("Message file added to transaction twice, messageFile = ./sent-message", e.getMessage());
        }
        verify();
    }

    @Test
    public void testAddSentMessage_normalTransacted() throws JMSException, DropboxTransaction.TransactionException, IOException {
        setupAddMessage(true);
        TRANSACTION_MOCK.checkClosed();
        TRANSACTION_MOCK.startPassively();
        replay();
        transaction.addSentMessage(new File("./sent-message"), new File("./commit-file"));
        verify();
    }

    @Test
    public void testAddSentMessage_normalNotTransacted() throws JMSException, DropboxTransaction.TransactionException, FileSystem.FileSystemException {
        setupAddMessage(false);
        TRANSACTION_MOCK.checkClosed();
        FILE_SYSTEM_MOCK.move(new File("./sent-message"), new File("./commit-file"));
        replay();
        transaction.addSentMessage(new File("./sent-message"), new File("./commit-file"));
        verify();
    }

    @Test
    public void testAddSentMessage_fileSystemException() throws JMSException, DropboxTransaction.TransactionException, FileSystem.FileSystemException {
        setupAddMessage(false);
        TRANSACTION_MOCK.checkClosed();
        FILE_SYSTEM_MOCK.move(new File("./sent-message"), new File("./commit-file"));
        expectLastCall().andThrow(new FileSystem.FileSystemException("file-system"));
        replay();
        try {
            transaction.addSentMessage(new File("./sent-message"), new File("./commit-file"));
            fail();
        } catch (DropboxMQJMSException e) {
            assertEquals("file-system", e.getMessage());
        }
        verify();
    }

    @Test
    public void testAddReceivedMessage_alreadyAddedSent() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddReceivedMessage_alreadyAdded("sent");
    }

    @Test
    public void testAddReceivedMessage_alreadyAddedReceived() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddReceivedMessage_alreadyAdded("received");
    }

    private void doTestAddReceivedMessage_alreadyAdded(final String type) throws JMSException, DropboxTransaction.TransactionException {
        setupAddMessage(true);
        TRANSACTION_MOCK.checkClosed();
        transaction.addParsedLine(type, new TransactionData(new File("./received-message"), new File("./received-commit"), new File("./received-target"), new File("./received-rollback-dir")), transaction.getLogData(), 1, new File("transaction-file"));
        replay();
        try {
            transaction.addReceivedMessage(new File("./received-message"), null, new File("./commit-file"), null);
            fail();
        } catch (JMSException e) {
            assertEquals("Message file added to transaction twice ./received-message", e.getMessage());
        }
        verify();
    }

    @Test
    public void testAddReceivedMessage_normalTransacted() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddReceivedMessage_normal(true);
    }

    @Test
    public void testAddReceivedMessage_normalNotAutoAcknowledge() throws JMSException, DropboxTransaction.TransactionException {
        doTestAddReceivedMessage_normal(false);
    }

    private void doTestAddReceivedMessage_normal(final boolean transacted) throws JMSException, DropboxTransaction.TransactionException {
        setupAddMessage(transacted);
        TRANSACTION_MOCK.checkClosed();
        if (!transacted) {
            expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(false);
        }
        TRANSACTION_MOCK.startPassively();
        replay();
        transaction.addReceivedMessage(new File("./received-message"), new File("./target-file"), new File("./commit-file"), new File("./rollback-file"));
        verify();
    }

    @Test
    public void testAddReceivedMessage_normalMove() throws JMSException, FileSystem.FileSystemException {
        setupAddMessage(false);
        TRANSACTION_MOCK.checkClosed();
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(true);
        FILE_SYSTEM_MOCK.move(new File("./message-file"), new File("./commit-file"));
        replay();
        transaction.addReceivedMessage(new File("./message-file"), null, new File("./commit-file"), null);
        verify();
    }

    @Test
    public void testAddReceivedMessage_fileSystemException() throws JMSException, FileSystem.FileSystemException {
        setupAddMessage(false);
        TRANSACTION_MOCK.checkClosed();
        expect(TRANSACTION_MOCK.isAutoAcknowledge()).andReturn(true);
        FILE_SYSTEM_MOCK.move(new File("./message-file"), new File("./commit-file"));
        expectLastCall().andThrow(new FileSystem.FileSystemException("file-system"));
        replay();
        try {
            transaction.addReceivedMessage(new File("./message-file"), null, new File("./commit-file"), null);
            fail();
        } catch (DropboxMQJMSException e) {
            assertEquals("file-system", e.getMessage());
        }
        verify();
    }

    @Test
    public void testDeliverMessage_noSessionListener() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        final MessageListener receiversListenerMock = createStrictMock(MessageListener.class);
        final Message message = new MessageImpl(null);
        receiversListenerMock.onMessage(message);
        replay();
        EasyMock.replay(receiversListenerMock);
        transaction.deliverMessage(receiversListenerMock, message);
        verify();
        EasyMock.verify(receiversListenerMock);
    }

    @Test
    public void testDeliverMessage_sessionListener() {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        final MessageListener sessionListenerMock = createStrictMock(MessageListener.class);
        transaction.setMessageListener(sessionListenerMock);
        final MessageListener receiversListenerMock = createStrictMock(MessageListener.class);
        final Message message = new MessageImpl(null);
        sessionListenerMock.onMessage(message);
        replay();
        EasyMock.replay(sessionListenerMock, receiversListenerMock);
        transaction.deliverMessage(receiversListenerMock, message);
        verify();
        EasyMock.verify(sessionListenerMock, receiversListenerMock);
    }

    private void setupCheckUnsubscribe() throws DropboxTransaction.TransactionException {
        transaction = new DropboxTransaction(TRANSACTION_ID, false, -182371, CONFIGURATION);
        final DropboxTransaction.LogData data = transaction.getLogData();
        final File transactionFile = new File("transaction-file");
        transaction.addParsedLine("received", new TransactionData(new File("./received-message1"), new File("./received-target-file1"), new File("./commit1/sub1/file1"), new File("./received-rollback-file1")), data, 1, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message2"), new File("./received-target-file2"), new File("./commit2/sub2/file2"), new File("./received-rollback-file2")), data, 2, transactionFile);
        transaction.addParsedLine("received", new TransactionData(new File("./received-message3"), new File("./received-target-file3"), new File("./commit3/sub3/file3"), new File("./received-rollback-file3")), data, 3, transactionFile);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message4"), new File("./sent-target-file1"), new File("./commit4/sub4/file4"), new File("./sent-rollback-file1")), data, 4, transactionFile);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message5"), new File("./sent-target-file2"), new File("./commit5/sub5/file5"), new File("./sent-rollback-file2")), data, 5, transactionFile);
        transaction.addParsedLine("sent", new TransactionData(new File("./sent-message6"), new File("./sent-target-file3"), new File("./commit6/sub6/file6"), new File("./sent-rollback-file")), data, 6, transactionFile);
    }

    @Test
    public void testCheckUnsubscribe_normal() throws DropboxTransaction.TransactionException, JMSException {
        setupCheckUnsubscribe();
        transaction.checkUnsubscribe(new File("./other-commit"));
    }

    @Test
    public void testCheckUnsubscribe_received() throws DropboxTransaction.TransactionException, JMSException {
        setupCheckUnsubscribe();
        try {
            transaction.checkUnsubscribe(new File("./commit2"));
            fail();
        } catch (JMSException e) {
            assertEquals("Attempting to unsubscribe while active message consumers exist for subscription ./commit2", e.getMessage());
        }
    }

    @Test
    public void testCheckUnsubscribe_sent() throws DropboxTransaction.TransactionException, JMSException {
        setupCheckUnsubscribe();
        try {
            transaction.checkUnsubscribe(new File("./commit6"));
            fail();
        } catch (JMSException e) {
            assertEquals("Attempting to unsubscribe while active message consumers exist for subscription ./commit6", e.getMessage());
        }
    }
}
