package fcp;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
import fcp.sessions.*;

/**
 * <p>A class exposing a high-level interface to the FCP client protocol.  This
 * class hides the details of FCP from the user and presents an asynchronous,
 * persistent connection to a single Freenet node.</p>
 *
 * <p>FCPManager is primariliy useful as a repository of unfinished FCP
 * sessions.  Since requests over FCP can take a long time to complete, this
 * class allows the user to submit requests which the manager will make
 * repeatedly until they complete.  Additionally, the manager can be
 * serialized and restored so that requests are not lost in the event of the
 * caller exiting.</p>
 *
 * @author Adam Thomason
 **/
public class FCPManager implements Runnable {

    /**
     * Creates a new FCP manager for the specified host and parameters.
     *
     * @param host the location of the Freenet host to connect to.
     * @param port the listening FCP port on the Freenet host.
     * @param requestHtl the hops-to-live value for requesting data.
     * @param insertHtl the hops-to-live value for inserting data.
     * @param slotOvercheck the number of succesively missing slots to
     *     observe before using the first slot in the missing range.  This
     *     parameter is useful for allowing for missing Freenet documents.
     *     This should never be less than 1.
     * @param socketTimeout the number of milliseconds to wait on idle sockets.
     *
     * @exception UnknownHostException if the named host is invalid.
     **/
    public FCPManager(InetAddress host, int port, int requestHtl, int insertHtl, int slotOvercheck, int socketTimeout) {
        this.host = host;
        this.port = port;
        this.requestHtl = requestHtl;
        this.insertHtl = insertHtl;
        this.slotOvercheck = slotOvercheck;
        this.socketTimeout = socketTimeout;
        log = Logger.getLogger(this.getClass().getName());
        LogManager.getLogManager().addLogger(log);
        sessions = Collections.synchronizedList(new ArrayList());
        cancellations = Collections.synchronizedList(new LinkedList());
    }

    /**
     * Requests a document from Freenet.  This call blocks until the request
     * completes.
     *
     * @param document the document to put the result into.
     *
     * @return the request result.
     **/
    public FCPRequester request(FCPDocument document) {
        FCPRequester request = new FCPRequester(host, port, socketTimeout, requestHtl, document);
        request.run();
        return request;
    }

    /**
     * Requests a document from Freenet.  This call does not block.
     *
     * @param document the document to put the result into.
     **/
    public void requestAsync(FCPDocument document) {
        FCPRequester request = new FCPRequester(host, port, socketTimeout, requestHtl, document);
        request.setListener(listener);
        request.setPassback(passback);
        request.start();
        sessions.add(request);
        log.info("requestAsync added to manager");
    }

    /**
     * Inserts a document into Freenet.  This call blocks until the request
     * completes.  The request is not repeated upon failure, serialized along
     * with asynchronous requests, or otherwise managed.
     *
     * @param document the document to publish.
     *
     * @return the insert result.
     **/
    public FCPInserter insert(FCPDocument document) {
        FCPInserter insert = new FCPInserter(host, port, socketTimeout, insertHtl, document);
        insert.run();
        return insert;
    }

    /**
     * Inserts a document into Freenet.  This call does not block.
     *
     * @param document the document to publish.
     **/
    public void insertAsync(FCPDocument document) {
        FCPInserter insert = new FCPInserter(host, port, socketTimeout, insertHtl, document);
        insert.setListener(listener);
        insert.setPassback(passback);
        insert.start();
        sessions.add(insert);
        log.info("insertAsync added to manager");
    }

    /**
     * Inserts a document into the first available slot in a KSK subspace.  This
     * call blocks until the request completes.  The request is not repeated
     * upon failure, serialized along with asynchronous requests, or otherwise
     * managed.
     *
     * @param slotAddressPrefix the subkey to search and insert under.
     * @param startSlot the first slot to attempt to insert under.
     * @param document the document to insert.
     *
     * @return the slot inserter.
     **/
    public SlotInserter slotInsert(String slotAddressPrefix, int startSlot, FCPDocument document) {
        SlotInserter inserter = new SlotInserter(this, slotAddressPrefix, startSlot, slotOvercheck, document);
        inserter.run();
        return inserter;
    }

    /**
     * Inserts a document into the first available slot in a KSK subspace.  This
     * call does not block.
     *
     * @param slotAddressPrefix the subkey to search and insert under.
     * @param startSlot the first slot to attempt to insert under.
     * @param document the document to insert.
     **/
    public void slotInsertAsync(String slotAddressPrefix, int startSlot, FCPDocument document) {
        SlotInserter inserter = new SlotInserter(this, slotAddressPrefix, startSlot, slotOvercheck, document);
        inserter.setListener(listener);
        inserter.setPassback(passback);
        inserter.start();
        sessions.add(inserter);
        log.info("slotInsertAsync added to manager");
    }

    /**
     * Retrieves documents from slots in a KSK subspace.  This call blocks
     * until the request completes.  The request is not repeated upon failure,
     * serialized along with asynchronous requests, or otherwise managed.
     *
     * @param slotAddressPrefix the subkey to search and insert under.
     * @param startSlot the first slot to attempt to insert under.
     * @param saveDocuments set this parameter to true if you want the
     *      retriever to store the slot documents for retrieval with
     *      <code>getDocuments</code>.
     *
     * @return the slot retriever.
     **/
    public SlotRetriever slotRetrieve(String slotAddressPrefix, int startSlot, boolean saveDocuments) {
        SlotRetriever retriever = new SlotRetriever(this, slotAddressPrefix, startSlot, slotOvercheck, saveDocuments);
        retriever.run();
        return retriever;
    }

    /**
     * Retrieves documents from slots in a KSK subspace.  This call does not
     * block.
     *
     * @param slotAddressPrefix the subkey to search and insert under.
     * @param startSlot the first slot to attempt to insert under.
     * @param saveDocuments set this parameter to true if you want the
     *      retriever to store the slot documents for retrieval with
     *      <code>getDocuments</code>.
     **/
    public void slotRetrieveAsync(String slotAddressPrefix, int startSlot, boolean saveDocuments) {
        SlotRetriever retriever = new SlotRetriever(this, slotAddressPrefix, startSlot, slotOvercheck, saveDocuments);
        retriever.setListener(listener);
        retriever.setPassback(passback);
        retriever.start();
        sessions.add(retriever);
        log.info("slotRetrieveAsync added to manager");
    }

    /**
     * Requests an SSK keypair from the node.  This call blocks until the
     * request completes.
     *
     * @return the session object, filled with a keypair if successful.
     **/
    public FCPSSKGenerator generateSSKPair() {
        FCPSSKGenerator generator = new FCPSSKGenerator(host, port, socketTimeout);
        generator.run();
        return generator;
    }

    /**
     * Requests an SSK keypair from the node.  This call does not block.
     **/
    public void generateSSKPairAsync() {
        FCPSSKGenerator generator = new FCPSSKGenerator(host, port, socketTimeout);
        generator.setListener(listener);
        generator.setPassback(passback);
        generator.start();
        sessions.add(generator);
        log.info("slotRetrieveAsync added to manager");
    }

    /**
     * Sets a listener for this manager.  The listener will be notify'ed when
     * any request completes.  Any existing sessions will use this listener
     * when next run.
     *
     * @param listener the new listener.
     **/
    public void setListener(FCPListener listener) {
        this.listener = listener;
        Iterator sessionIter = sessions.iterator();
        while (sessionIter.hasNext()) {
            FCPSession session = (FCPSession) sessionIter.next();
            session.setListener(listener);
        }
    }

    /**
     * Sets a passback for the listener on this session.  When the listener is
     * notify'ed, this passback object will be included.  Any existing
     * sessions will use this passback when next run.
     *
     * @param passback the new passback.
     **/
    public void setPassback(Object passback) {
        this.passback = passback;
        Iterator sessionIter = sessions.iterator();
        while (sessionIter.hasNext()) {
            FCPSession session = (FCPSession) sessionIter.next();
            session.setPassback(passback);
        }
    }

    /**
     * Cancels a managed session.  The session will be removed during
     * the next management update.  The running session will not be killed,
     * so it is possible for the session to complete after this method is
     * called.
     *
     * @param session the session to cancel.
     **/
    public void cancelSession(FCPSession session) {
        cancellations.add(session);
    }

    /**
     * Checks if a session has been cancelled.  If so, the session is removed
     * from the cancellation list under the assumption that the caller will
     * handle the cancellation.
     *
     * @param session the session to check.
     *
     * @return true if <code>session</code> is in the cancellation list.
     **/
    protected boolean isCancelled(FCPSession session) {
        Iterator iter = cancellations.iterator();
        while (iter.hasNext()) {
            if (session == iter.next()) {
                iter.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * Starts the manager loop.  The manager starts a monitors a thread for
     * each pending session.  If a session fails due to a Freenet error, the
     * session is started again.
     **/
    public void run() {
        enabled = true;
        while (enabled) {
            Iterator sessionIter = sessions.iterator();
            while (sessionIter.hasNext()) {
                FCPSession session = (FCPSession) sessionIter.next();
                if (isCancelled(session)) {
                    sessionIter.remove();
                    continue;
                }
                if (!session.isAlive()) {
                    if (session.getSuccess()) {
                        sessionIter.remove();
                    } else {
                        session.start();
                    }
                }
            }
            try {
                Thread.sleep(monitorPeriod);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * @return the time in milliseconds this manager waits between checking
     *     on session threads.
     **/
    public int getMonitorPeriod() {
        return monitorPeriod;
    }

    /**
     * Set the monitor waiting period.
     *
     * @param monitorPeriod the new period.
     **/
    public void setMonitorPeriod(int monitorPeriod) {
        this.monitorPeriod = monitorPeriod;
    }

    /**
     * @return a list of the pending sessions that are being managed.
     **/
    public List getSessions() {
        return sessions;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getRequestHtl() {
        return requestHtl;
    }

    public void setRequestHtl(int requestHtl) {
        this.requestHtl = requestHtl;
    }

    public int getInsertHtl() {
        return insertHtl;
    }

    public void setInsertHtl(int insertHtl) {
        this.insertHtl = insertHtl;
    }

    public int getSlotOvercheck() {
        return slotOvercheck;
    }

    public void setSlotOvercheck(int slotOvercheck) {
        this.slotOvercheck = slotOvercheck;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    protected InetAddress host;

    protected int port;

    protected int requestHtl;

    protected int insertHtl;

    protected int slotOvercheck;

    protected int socketTimeout;

    protected Logger log;

    protected boolean enabled;

    protected FCPListener listener;

    protected Object passback;

    protected List sessions;

    protected List cancellations;

    /**
     * Time in milliseconds to wait between checking on session threads.
     **/
    protected int monitorPeriod = 3000;
}
