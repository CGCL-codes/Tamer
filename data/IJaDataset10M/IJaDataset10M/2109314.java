package ch.epfl.lsr.adhoc.services.neighboring;

import java.util.*;
import ch.epfl.lsr.adhoc.runtime.Message;
import ch.epfl.lsr.adhoc.runtime.MessagePool;
import ch.epfl.lsr.adhoc.runtime.Service;
import ch.epfl.lsr.adhoc.runtime.Dispatcher;
import ch.epfl.lsr.adhoc.runtime.FrancRuntime;
import ch.epfl.lsr.adhoc.runtime.FrancThread;
import ch.epfl.lsr.adhoc.runtime.Parameters;
import ch.epfl.lsr.adhoc.runtime.ParamDoesNotExistException;
import ch.epfl.lsr.adhoc.runtime.Buffer;

/**
 * This class defines the NeighborService which collects information about all
 * active neighbors running this service.
 * <p>
 * This means this service collects information about nodes inside
 * the range of communication. The service is independant of the asynchronous
 * communication stack of adhoc.*
 * <p>
 * The service, contrary to the layers of the mode, does not search the messages
 * in a buffer located at layer n-1, but receives the messages from the
 * dispatcher by use of the delivery method.
 * <p>
 * The collected information is stored in a NeighborTable.
 *
 * @see Service
 * @see HelloMsg
 * @see Hello
 * @see HelloMsgFactory
 * @see NeighborTable
 * @author Reto Krummenacher
 */
public class NeighborService extends FrancThread implements Service {

    private Parameters params;

    protected Buffer buf;

    protected Dispatcher disp;

    protected NeighborTable neighbors;

    protected MessagePool mp;

    protected int entryExp;

    protected int interval;

    protected char msgType;

    protected FrancThread sender;

    /**
     * Creates a new instance of the neighbor service.
     * The service runs by use of to threads. The first one is sending hello
     * messages via broadcast by use of Hello.sendHello; this is done every
     * HELLO_INTERVALL seconds. The second thread is responsible for the
     * neighbor table, i.e. makes updates and deletion of entries.
     */
    public NeighborService(Parameters params) {
        this.params = params;
    }

    public NeighborTable getTable() {
        return neighbors;
    }

    public void initialize(FrancRuntime runtime) {
        disp = runtime.getDispatcher();
        mp = runtime.getMessagePool();
        neighbors = new NeighborTable();
        buf = new Buffer();
        try {
            msgType = mp.getMessageType(params.getString("msgType"));
            interval = params.getInt("HelloInterval");
            entryExp = params.getInt("entryExp");
        } catch (ParamDoesNotExistException pdnee) {
            pdnee.printStackTrace();
        }
        sender = new FrancThread(new Hello(runtime, msgType, interval));
        sender.setDaemon(true);
        setDaemon(true);
    }

    public void startup() {
        sender.start();
        start();
    }

    /**
     * This method is run by the second thread to keep the neighbor table up to
     * date. The thread reads messages put into the buffer by the dispatcher and
     * adds, updates or deletes entries in the table.
     * <p>
     * The thread waits until a message is put in the buffer or an entry will
     * expire.
     */
    public void run() {
        long dream = Long.MAX_VALUE;
        HelloMsg newMsg;
        while (true) {
            try {
                synchronized (this) {
                    wait(dream);
                }
                Date date = new Date();
                long currentTime = date.getTime();
                while (!buf.isEmpty()) {
                    newMsg = (HelloMsg) buf.remove();
                    updateNeighborTable(new NeighborTableEntry(newMsg.getSrcNode(), currentTime, entryExp));
                    mp.freeMessage(newMsg);
                }
                dream = checkNeighborTable();
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * This method adds or updates entries in the table. Thus it adds
     * neighbor table entries to the table. The latest entry is alway put to the
     * end of the list, in that way it is easy to find the next expiring entry.
     *
     * @param entry The NeighborTableEntry to add or replace in the table.
     */
    protected void updateNeighborTable(NeighborTableEntry entry) {
        for (int i = 0; i < neighbors.size(); i++) {
            if (neighbors.getNeighbor(i).equals(entry)) {
                neighbors.removeNeighbor(i);
                break;
            }
        }
        neighbors.addNeighbor(entry);
    }

    /**
     * This method checks the neighbor table for expiring entries, deletes those
     * and gets by the use of the method getWaitingTime the time at which the
     * next entry will expire.
     *
     * @return The time at which the thread will have to wake at latest to check
     * the table.
     */
    protected long checkNeighborTable() {
        Date date = new Date();
        long currentTime = date.getTime();
        NeighborTableEntry nte = null;
        int i;
        for (i = 0; i < neighbors.size(); i++) {
            nte = neighbors.getNeighbor(i);
            if (nte.getLifetime() - currentTime < 0) {
            } else {
                break;
            }
        }
        for (int j = 0; j < i; j++) {
            neighbors.removeNeighbor(0);
        }
        return getWaitingTime(currentTime);
    }

    /**
     * This methods gets the time at which the next entry will expire and
     * determines the time which the thread has to wait.
     *
     * @param currentTime The current system time.
     * @return The time at which the thread will have to wake at latest to check
     * the table.
     */
    protected long getWaitingTime(long currentTime) {
        if (neighbors.size() > 0) {
            NeighborTableEntry nte = neighbors.getNeighbor(0);
            return (nte.getLifetime() - currentTime);
        } else {
            return Long.MAX_VALUE;
        }
    }

    /**
     * This method is called by the dispatcher at delivery of a message. It
     * puts the message in the local buffer and wakes the thread up as to read
     * the message.
     *
     * @param msg The message delivered to the service
     */
    public void deliverMessage(Message msg) {
        buf.add(msg);
        synchronized (this) {
            notify();
        }
    }

    /**
    * This method permit to change the expiration of an entry dynamically
    */
    public void setEntryExp(int entryExp) {
        this.entryExp = entryExp;
    }

    /**
    * This method permit to get the expiration of an entry
    */
    public int getEntryExp() {
        return (entryExp);
    }
}
