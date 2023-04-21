package fr.irisa.asap.debug.spy.capture;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import org.apache.log4j.Logger;
import fr.irisa.asap.debug.spy.IForwarding;
import fr.irisa.asap.debug.spy.MachineState;
import fr.irisa.asap.debug.spy.MsgDescriptor;
import fr.irisa.asap.debug.spy.NetworkMsg;
import fr.irisa.asap.debug.utility.Config;
import fr.irisa.asap.debug.utility.IPoolChild;
import fr.irisa.asap.debug.utility.Mattern;
import fr.irisa.asap.debug.utility.Pool;

/**
 * Process incoming connections on the <code>Proxy</code>.<br />
 * ProxyChild, created on the 27th of February, 2007.<br />
 * <br />
 * 
 * This class is used to process an incomed connexion from another debugger.
 * It deserialize the {@link NetworkMsg}, create an associated
 * {@link MsgDescriptor} and give it to the current {@link IForwarding}.<br />
 * 
 * To improve efficiency, this class can be associated with 
 *
 * @author DebugProject_anthony.loiseau@ens.insa-rennes.fr
 * @version $Id: ProxyChild.java 203 2007-04-19 14:37:07Z anthony.loiseau $
 *
 * @see Proxy
 * @see Pool
 */
public class ProxyChild extends Thread implements IPoolChild {

    /** Log4j logger for the current class */
    private static final Logger LOG4J = Logger.getLogger(ProxyChild.class);

    /**
     * Synchronize <code></code>processCapture()</code> and <code>run()</code>.
     * 
     * Its value is always in the range 0-2 (0 at the beginning).
     * Three main operation are done with it :
     * <ul>
     * <li>set to 2 by processCapture()</li>
     * <li>decremented at the beginning of the real capture process</li>
     * <li>decremented at the end of the real capture process</li>
     * </ul> 
     * 
     * like this, each state has a mean :
     * 
     * <ul>
     * <li><code>0</code> : free child.</li>
     * <li><code>2</code> : child has been given a task but has not yet begin
     *                      to process it.</li>
     * <li><code>1</code> : child busy, processing a task.</li>
     * </ul> 
     */
    private final Semaphore semSync;

    private Pool<IPoolChild> parentPool = null;

    private NetworkMsg netMsg;

    private Mattern recpTime;

    /**
     * Get a new <code>ProxyChild</code> not connected to a pool. 
     */
    public ProxyChild() {
        super();
        setName("ProxyChild_" + getName());
        semSync = new Semaphore(0);
        this.start();
    }

    /**
     * @return the parentPool
     */
    private Pool<IPoolChild> getParentPool() {
        return parentPool;
    }

    /**
     * 
     * @param parentPool the parentPool to set
     */
    @SuppressWarnings("unchecked")
    public void setPool(Pool<? extends IPoolChild> parentPool) {
        this.parentPool = (Pool<IPoolChild>) parentPool;
    }

    /**
     * @return the netMsg
     */
    private NetworkMsg getNetMsg() {
        return netMsg;
    }

    /**
     * @param sock the sock to set
     */
    private void setNetMsg(NetworkMsg nm) {
        this.netMsg = nm;
    }

    /**
     * @return the recpTime
     */
    private Mattern getRecpTime() {
        return recpTime;
    }

    /**
     * @param recpTime the recpTime to set
     */
    private void setRecpTime(Mattern recpTime) {
        this.recpTime = recpTime;
    }

    /**
     * Process incoming <code>NetworkMsg</code>.
     * 
     * This method only give the work to this child and return, the process
     * in fact is done by the thread.
     * If this child is used with a pool, it will set itself as free in this
     * spool.
     * 
     * @param sock the socket to get data from. It's given by the 
     * <code>Proxy</code>.
     * @param receptTime Logical time of the reception by the proxy.
     */
    public synchronized void processCapture(Socket sock, Mattern receptTime) {
        ObjectInputStream ois = null;
        NetworkMsg nm = null;
        setRecpTime(receptTime);
        try {
            ois = new ObjectInputStream(sock.getInputStream());
            nm = (NetworkMsg) ois.readObject();
            sock.close();
        } catch (IOException ex) {
            LOG4J.error("IOException while trying to read the NetworkMsg " + "throw the incomed connexion : " + ex + " >>>> skipping process");
            return;
        } catch (ClassNotFoundException ex) {
            LOG4J.error("ClassNotFoundException while trying to extract" + "the NetworkMsg from the incomed connexion." + ex + " >>>> skipping process");
            return;
        }
        setNetMsg(nm);
        MachineState.instance().getLogicTime().merge(nm.getTime());
        MachineState.instance().lockLogicTime.release();
        if (semSync.availablePermits() != 0) {
            LOG4J.error("semSync permits not 0 in wainting state : " + semSync.availablePermits());
            semSync.drainPermits();
        }
        semSync.release(2);
    }

    /**
     * infinite loop to process <code>processCapture</code> events.
     */
    @Override
    public void run() {
        MsgDescriptor msgDesc;
        while (true) {
            semSync.acquireUninterruptibly();
            if (LOG4J.isDebugEnabled()) {
                LOG4J.debug("processing NetworkMsg : " + getNetMsg());
            }
            msgDesc = new MsgDescriptor(getNetMsg(), MsgDescriptor.ECaptureInterface.RECEIVER, Config.getInetAddress("machines.host" + getNetMsg().getSender()));
            msgDesc.setReceptionTime(getRecpTime());
            MachineState.instance().fwd.processMsg(msgDesc);
            semSync.acquireUninterruptibly();
            releaseInPool();
        }
    }

    /**
     * Release the current PoolChild in the pool.
     * 
     * If no pool is set, do nothing.
     * @see #setPool(Pool)
     */
    private void releaseInPool() {
        if (getParentPool() != null) {
            getParentPool().releaseChild(this);
        }
    }
}
