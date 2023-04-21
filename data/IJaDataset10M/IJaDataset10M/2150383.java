package com.fujitsu.arcon.common;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import org.unicore.AJOIdentifier;
import org.unicore.outcome.AbstractJob_Outcome;
import org.unicore.upl.Reply;
import org.unicore.upl.RetrieveOutcome;
import org.unicore.upl.RetrieveOutcomeReply;
import org.unicore.upl.ServerRequest;
import org.unicore.upl.UnicoreResponse;

/**
//UPLD 
//UPLD 
//UPLD  
 * 
 * @author Sven van den Berghe, Fujitsu Laboratories of Europe
 * 
 * Copyright Fujitsu Laboratories of Europe 2003
 *  
 * Created Jul 30, 2003
 */
public class RSDReaderFactory implements ReaderFactory {

    private FileTransferEngine fte;

    private int limit;

    private AJOIdentifier ajo_id;

    ConnectionFactory cf;

    Logger logger;

    private Set all_readers = new HashSet();

    public RSDReaderFactory(AJOIdentifier ajo_id, int limit, ConnectionFactory cf, Logger logger) {
        this.limit = 1;
        this.ajo_id = ajo_id;
        this.cf = cf;
        this.logger = logger;
    }

    private static int counter = 0;

    private AbstractJob_Outcome ajoo = null;

    public AbstractJob_Outcome getOutcome() {
        return ajoo;
    }

    /**
//UPLD 	 
//UPLD 	
//UPLD 	 
//UPLD 	 
	 */
    public boolean makeReader() {
        ServerRequest sr;
        sr = new RetrieveOutcome();
        sr.setTarget(ajo_id);
        try {
            Connection connection = cf.connect();
            Reply from_server = connection.sendServerRequest(sr);
            UnicoreResponse[] ur = from_server.getTrace();
            if (ur[ur.length - 1].getReturnCode() < 0) {
                notifyTerminated((Reader) null);
                logger.logError("UPLReaderFactory shutting down because Server refused first connection request.");
            } else if (ur[ur.length - 1].getReturnCode() > 0) {
                logger.logError("UPLReaderFactory got an error: " + ur[ur.length - 1].getReturnCode() + ":" + ur[ur.length - 1].getComment());
                notifyTerminated((Reader) null);
                logger.logError("UPLReaderFactory shutting down because Server returned error on first connection request.");
            } else {
                try {
                    RetrieveOutcomeReply ror = (RetrieveOutcomeReply) connection.readReply();
                    ajoo = (AbstractJob_Outcome) (new ObjectInputStream(new ByteArrayInputStream(ror.getOutcome()))).readObject();
                    if (!ror.hasStreamed()) {
                        logger.logComment("UPLReaderFactory found no streamed data in Outcome, shutting down.");
                        notifyTerminated((Reader) null);
                        return false;
                    }
                } catch (java.lang.Exception ex) {
                    logger.logError("UPLReaderFactory got an error reading Outcome, shutting down: " + ex.getMessage());
                    notifyTerminated((Reader) null);
                    return false;
                }
                UPLReader fr = new UPLReader(fte, connection, this, logger);
                all_readers.add(fr);
                (new Thread(fr, "ConcurrentFTUPLReader-" + counter++)).start();
                logger.logComment("Creating new UPL reader" + counter);
                return true;
            }
        } catch (Exception e) {
            logger.logError("UPLReaderFactory got an error: ", e);
            if (all_readers.isEmpty()) {
                notifyTerminated((Reader) null);
                logger.logError("UPLReaderFactory shutting down because Server returned exception on first connection request.");
            }
        }
        return false;
    }

    public void notifyTerminated(Reader r) {
        all_readers.remove(r);
        if (all_readers.isEmpty()) {
            all_terminated = true;
            fte.allReadersTerminated();
            fte = null;
        }
    }

    private boolean all_terminated = false;

    public boolean allTerminated() {
        return all_terminated;
    }

    public void setFileTransferEngine(FileTransferEngine fte) {
        this.fte = fte;
    }

    public boolean canMakeWriter() {
        return all_readers.size() != 1;
    }
}
