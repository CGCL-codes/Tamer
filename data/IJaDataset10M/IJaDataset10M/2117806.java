package com.fujitsu.arcon.njs.streaming;

import java.util.HashMap;
import java.util.Map;
import org.unicore.AJOIdentifier;
import org.unicore.Identifier;
import org.unicore.Ulogin;
import org.unicore.UserAttributes;
import org.unicore.utility.PacketisedInputStream;
import org.unicore.utility.PacketisedOutputStream;
import com.fujitsu.arcon.common.Chunk;
import com.fujitsu.arcon.common.ChunkManager;
import com.fujitsu.arcon.common.FileTransferEngine;
import com.fujitsu.arcon.common.ReaderFactory;
import com.fujitsu.arcon.common.UPLCJReaderFactory;
import com.fujitsu.arcon.common.UPLROWriterFactory;
import com.fujitsu.arcon.common.UPLReader;
import com.fujitsu.arcon.common.UPLReadingChunkManager;
import com.fujitsu.arcon.common.UPLWriter;
import com.fujitsu.arcon.common.WriterFactory;
import com.fujitsu.arcon.njs.NJSGlobals;
import com.fujitsu.arcon.njs.interfaces.IncarnatedUser;
import com.fujitsu.arcon.njs.interfaces.NJSException;
import com.fujitsu.arcon.njs.interfaces.TSIUnavailableException;
import com.fujitsu.arcon.njs.logger.Logger;
import com.fujitsu.arcon.njs.logger.LoggerManager;
import com.fujitsu.arcon.njs.priest.OutcomeStore;
import com.fujitsu.arcon.njs.priest.UspaceManager;

public abstract class UPLStream implements FileTransferEngine.Requestor {

    /**
	 * Write the streamed data following a ConsignJob. 
	 * The target Uspace will be
	 * created if necessary. The method checks that the user can write (the
	 * User owns the Uspace and the xlogin/project is the same, or can be made the same).
	 * Synchronous, returns when transfer complete (exception thrown on error).
	 * 
	 * @param user The user, not endorser
	 * 
	 */
    public static boolean streamData(AJOIdentifier target, Ulogin consignor, UserAttributes user, IncarnatedUser iuser, PacketisedInputStream in) throws NJSException, TSIUnavailableException {
        UspaceManager.Uspace us = NJSGlobals.getUspaceManager().makePSUspace(target, user, consignor, iuser);
        UPLStream handler = getBundle(us, target, consignor);
        if (handler == null) handler = createBundle(us, null, target, consignor, true);
        return handler.processSynchronous(in);
    }

    /**
	 * Write the streamed data following a Transfer Idiomatic. 
	 * The target Uspace will be
	 * created if necessary. The method checks that the user can write (the
	 * User owns the Uspace and the xlogin/project is the same, or can be made the same).
	 * Synchronous, returns when transfer complete (exception thrown on error).
	 * 
	 * Note that the target Uspace is not the Uspace associated with the Transfer Idiomatic,
	 * a mapping is created from the Transfer Idiomatic to the target so that the transfer
	 * takes place directly.
	 * 
	 */
    public static boolean streamTIData(AJOIdentifier transfer_idiomatic, AJOIdentifier target, Ulogin consignor, UserAttributes user, IncarnatedUser iuser, PacketisedInputStream in) throws NJSException, TSIUnavailableException {
        UspaceManager.Uspace us = NJSGlobals.getUspaceManager().makePSUspace(target, user, consignor, iuser);
        UPLStream handler = UPLStream.getBundle(us, transfer_idiomatic, consignor);
        if (handler == null) handler = createBundle(us, null, target, consignor, true);
        return handler.processSynchronous(in);
    }

    /**
	 * Write streamed data to a Uspace from a concurrent UPL request (ConsignStreamedData).
	 * This will be accepted if the target Uspace exists and its AJO was consigned by
	 * the same Ulogin as this request. This request will either join an existing bundle
	 * or create a new one. It can be refused if the NJS is "too busy".
	 * 
	 */
    public static UPLReader streamData(AJOIdentifier target, Ulogin consignor, PacketisedInputStream in) throws NJSException, TSIUnavailableException {
        UspaceManager.Uspace us = NJSGlobals.getUspaceManager().testUspace(target, consignor);
        UPLStream handler = UPLStream.getBundle(us, target, consignor);
        if (handler == null) handler = createBundle(us, null, target, consignor, true);
        return (UPLReader) handler.addStream(in);
    }

    /**
	 * Return the status of any streamed file chunks in this Uspace (i.e. a listing of
	 * the contents of all still hidden portfolios).
	 * 
	 * @param target
	 * @param consignor
	 * @param in
	 * @return
	 * @throws NJSException
	 * @throws TSIUnavailableException
	 */
    public static Chunk[] streamDataStatus(AJOIdentifier target, Ulogin consignor, PacketisedInputStream in) throws NJSException, TSIUnavailableException {
        UspaceManager.Uspace us = NJSGlobals.getUspaceManager().testUspace(target, consignor);
        return DoStuff1.listUspace(us.getDirectory(), us.getIncarnatedUser());
    }

    /**
//UPLD	
//UPLD
//UPLD	
//UPLD	
	 * <p>
	 * Asynchronous, the Writer must be started for the transfer to take place.
	 * 
	 * @param target The AJO's whose Outcome is wanted
	 * @param consignor The client who wants the files
	 * @param out
	 * @return
	 * @throws NJSException
	 * @throws TSIUnavailableException
	 */
    public static UPLWriter streamOutcome(AJOIdentifier target, Ulogin consignor, PacketisedOutputStream out) throws NJSException, TSIUnavailableException {
        UPLStream bundle = getBundle(NJSGlobals.getUspaceManager().getUspace(target), target, consignor);
        if (bundle != null) {
            return (UPLWriter) bundle.addStream(out);
        } else {
            throw new NJSException("There is no existing transfer (from RetrieveOutcome or RestartRetrieveOutcome)");
        }
    }

    public static UPLWriter streamOutcome(Object dummy, AJOIdentifier target, Ulogin consignor, PacketisedOutputStream out) throws NJSException, TSIUnavailableException {
        NJSGlobals.getUspaceManager().couldStream(target, consignor);
        if (NJSGlobals.getUspaceManager().outcomeToStream(target)) {
            if (getBundle(NJSGlobals.getUspaceManager().getUspace(target), target, consignor) != null) throw new NJSException("There is already a transfer of this Outcome running.");
            UPLStream bundle = createBundle(NJSGlobals.getUspaceManager().getUspace(target), dummy, target, consignor, false);
            return (UPLWriter) bundle.addStream(out);
        } else {
            throw new NJSException("No data to stream from the Outcome");
        }
    }

    private static Map transfer_bundles = new HashMap();

    private static synchronized UPLStream getBundle(UspaceManager.Uspace uspace, AJOIdentifier map_id, Ulogin consignor) {
        UPLStream rtn = null;
        Map existing_transfers = (Map) transfer_bundles.get(consignor);
        if (existing_transfers != null) rtn = (UPLStream) existing_transfers.get(map_id);
        return rtn;
    }

    private static UPLStream createBundle(UspaceManager.Uspace uspace, Object dummy, AJOIdentifier job_id, Ulogin consignor, boolean incoming) throws NJSException {
        UPLStream rtn;
        if (incoming) {
            rtn = new UPLStream.Incoming(uspace, job_id, consignor);
        } else {
            rtn = new UPLStream.Outgoing(uspace, dummy, job_id, consignor);
        }
        Map existing_transfers = (Map) transfer_bundles.get(consignor);
        if (existing_transfers == null) {
            existing_transfers = new HashMap();
            transfer_bundles.put(consignor, existing_transfers);
        }
        existing_transfers.put(job_id, rtn);
        return rtn;
    }

    static synchronized void removeBundle(Identifier job_id, Ulogin consignor) {
        Map existing_transfers = (Map) transfer_bundles.get(consignor);
        if (existing_transfers != null) {
            existing_transfers.remove(job_id);
        }
    }

    protected Logger logger;

    FileTransferEngine fte;

    protected boolean started = false;

    protected Identifier map_id;

    protected Ulogin consignor;

    public abstract ReaderFactory getReaderFactory();

    public abstract WriterFactory getWriterFactory();

    public abstract Object addStream(Object stream) throws NJSException;

    public abstract boolean processSynchronous(Object stream) throws NJSException, TSIUnavailableException;

    /** 
	 * The FTE has done, notify all (any) synchronous waits.
	 * 
	 * @see com.fujitsu.arcon.common.FileTransferEngine.Requestor#transferDone()
	 */
    public synchronized void transferDone() {
        this.notifyAll();
        done = true;
    }

    private boolean done = false;

    public synchronized boolean done() {
        return done;
    }

    protected UPLStream(UspaceManager.Uspace uspace, Identifier map_id, Ulogin consignor) {
        this.logger = LoggerManager.get("Streaming");
        this.map_id = map_id;
        this.consignor = consignor;
    }

    public static class Incoming extends UPLStream {

        private UPLCJReaderFactory rf;

        private TSIFileWriterFactory wf;

        public ReaderFactory getReaderFactory() {
            return rf;
        }

        public WriterFactory getWriterFactory() {
            return wf;
        }

        public Incoming(UspaceManager.Uspace uspace, AJOIdentifier map_id, Ulogin consignor) {
            super(uspace, map_id, consignor);
            rf = new UPLCJReaderFactory(NJSGlobals.getMaxStreams(), logger);
            wf = new TSIFileWriterFactory.CJ(uspace, NJSGlobals.getMaxStreams(), logger);
            ChunkManager cm = new UPLReadingChunkManager(rf);
            fte = FileTransferEngine.create(rf, wf, cm, this, logger, NJSGlobals.getBufferSize());
            logger.logComment("Creating new incoming UPLStreamBundle for <" + uspace.getName() + ">");
        }

        /**
		 * Add a source of data that will be written to the Uspace.
		 * 
		 * @param in
		 * @return A Reader if the connection is accepted, null otherwise.
		 * @throws NJSException
		 */
        public Object addStream(Object stream) throws NJSException {
            if (!(stream instanceof PacketisedInputStream)) throw new NJSException("Can only add PacketisedInputStream");
            PacketisedInputStream in = (PacketisedInputStream) stream;
            UPLReader rtn = _addInputStream(in);
            if (rtn != null) {
                try {
                    if (started) {
                        fte.addReader();
                    } else {
                        fte.doTransfer();
                        started = true;
                        (new Thread((new Waiter(this)), "UPLStreamBundle-waiter")).start();
                    }
                } catch (RuntimeException e) {
                    fte.notifyError(null, e);
                    throw (e);
                }
            }
            return rtn;
        }

        private UPLReader _addInputStream(PacketisedInputStream in) throws NJSException {
            return rf.acceptConnection(new IncomingUPLConnection(in));
        }

        /**
		 * Read streamed data from the input stream and write it to Uspace. Return
		 * when the transfer is complete (all streams attached to this bundle have
		 * completed).
		 * 
		 * @param in
		 * @return true if the connection was accepted (and complete transfer), false if too busy
		 * @throws NJSException
		 */
        public boolean processSynchronous(Object stream) throws NJSException, TSIUnavailableException {
            if (!(stream instanceof PacketisedInputStream)) throw new NJSException("Can only add PacketisedInputStream");
            PacketisedInputStream in = (PacketisedInputStream) stream;
            UPLReader reader = _addInputStream(in);
            if (reader != null) {
                fte.doTransfer();
                started = true;
                (new Thread(reader, "UPLStreamBundle-incoming")).start();
                (new Waiter(this)).hold();
                return true;
            } else {
                return false;
            }
        }
    }

    public static class Outgoing extends UPLStream {

        private TSIFileReaderFactory rf;

        private UPLROWriterFactory wf;

        public ReaderFactory getReaderFactory() {
            return rf;
        }

        public WriterFactory getWriterFactory() {
            return wf;
        }

        public Outgoing(UspaceManager.Uspace uspace, Object dummy, AJOIdentifier map_id, Ulogin consignor) throws NJSException {
            super(uspace, map_id, consignor);
            TSIFileChunkManager cm = new TSIFileChunkManager(logger, NJSGlobals.getBufferSize());
            Chunk[] current = DoStuff1.listContents(OutcomeStore.getRootOutcomeStore(map_id).getDirectory(), uspace.getIncarnatedUser());
            for (int i = 0; i < current.length; i++) {
                TSIFileChunkManager.Allocation alloc = new TSIFileChunkManager.Allocation(current[i].getFileName(), current[i].getChunkLength(), current[i].getMode(), null);
                cm.addAllocation(alloc);
            }
            int streams = NJSGlobals.getMaxStreams();
            rf = new TSIFileReaderFactory(OutcomeStore.getRootOutcomeStore(map_id).getDirectory(), uspace.getIncarnatedUser(), streams, logger);
            wf = new UPLROWriterFactory(streams, logger);
            fte = FileTransferEngine.create(rf, wf, cm, this, logger, NJSGlobals.getBufferSize());
            logger.logComment("Creating new outgoing UPLStreamBundle for <" + OutcomeStore.getRootOutcomeStore(map_id) + ">");
        }

        /**
		 * Add a sink for data read from local Outcome.
		 * 
		 * @param in
		 * @return A Reader if the connection is accepted, null otherwise.
		 * @throws NJSException
		 */
        public Object addStream(Object stream) throws NJSException {
            if (!(stream instanceof PacketisedOutputStream)) throw new NJSException("Can only add PacketisedOutputStream");
            PacketisedOutputStream out = (PacketisedOutputStream) stream;
            UPLWriter rtn = _addOutputStream(out);
            if (rtn != null) {
                try {
                    if (started) {
                        fte.addWriter();
                    } else {
                        fte.doTransfer();
                        started = true;
                        (new Thread((new Waiter(this)), "UPLStreamBundle-waiter")).start();
                    }
                } catch (RuntimeException e) {
                    fte.notifyError(null, e);
                    throw (e);
                }
            }
            return rtn;
        }

        private UPLWriter _addOutputStream(PacketisedOutputStream out) throws NJSException {
            return wf.acceptConnection(new OutgoingUPLConnection(out));
        }

        public boolean processSynchronous(Object stream) throws NJSException, TSIUnavailableException {
            throw new NJSException("processSynchronous not implement for Outgoing");
        }
    }

    class Waiter implements Runnable {

        private UPLStream prnt;

        public Waiter(UPLStream prnt) {
            this.prnt = prnt;
        }

        public void run() {
            try {
                hold();
            } catch (TSIUnavailableException tex) {
            } catch (NJSException e) {
            }
        }

        public void hold() throws TSIUnavailableException, NJSException {
            if (prnt.fte.transferOK()) {
                synchronized (prnt) {
                    try {
                        if (!prnt.done()) prnt.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            UPLStream.removeBundle(prnt.map_id, prnt.consignor);
            if (!prnt.fte.transferOK()) {
                if (prnt.fte.getCause() instanceof TSIUnavailableException) {
                    throw (TSIUnavailableException) fte.getCause();
                } else {
                    throw new NJSException(prnt.fte.errorMessage());
                }
            }
        }
    }
}
