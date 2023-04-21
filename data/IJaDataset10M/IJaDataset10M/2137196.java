package repastcity3.agent;

import java.util.logging.Level;
import java.util.logging.Logger;
import repastcity3.main.ContextManager;

/**
 * This class can be used to step agents in different threads simulataneously.
 * If the <code>ContextManager</code> determines that this is a good idea (e.g.
 * if there will be no inter-agent communication) then, rather than using Repast
 * to schedule each agent's step() method directly, it will schedule the
 * agentStep() method (below) instead. This method is then responsible for
 * making the agents step by delegating the work do different threads depending
 * on how many CPU cores are free. As you can imagine, this leads to massive
 * decreases in computation time on multi-core computers.
 * 
 * <p>
 * It is important to note that there will be other side-effects from using
 * multiple threads, particularly agents simultaneously trying to access
 * Building methods or trying to write output data. So care needs to be taken
 * with the rest of the model to prevent problems. The (fairly) naive way that
 * I've tackled this is basically with the liberal use of
 * <code>synchronized</code>
 * </p>
 * 
 * @author Nick Malleson
 * @see ContextManager
 * @see ThreadController
 * @see BurglarThread
 */
public class ThreadedAgentScheduler {

    private static Logger LOGGER = Logger.getLogger(ThreadedAgentScheduler.class.getName());

    private boolean burglarsFinishedStepping;

    /**
	 * This is called once per iteration and goes through each burglar calling
	 * their step method. This is done (instead of using Repast scheduler) to
	 * allow multi-threading (each step method can be executed on a free core).
	 * This method actually just starts a ThreadController thread (which handles
	 * spawning threads to step burglars) and waits for it to finish
	 */
    public synchronized void agentStep() {
        this.burglarsFinishedStepping = false;
        (new Thread(new ThreadController(this))).start();
        while (!this.burglarsFinishedStepping) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "", e);
                ContextManager.stopSim(e, ThreadedAgentScheduler.class);
            }
        }
    }

    /**
	 * Used to tell the ContextCreator that all burglars have finished their
	 * step methods and it can continue doing whatever it was doing (it will be
	 * waiting while burglars are stepping).
	 */
    public synchronized void setBurglarsFinishedStepping() {
        this.burglarsFinishedStepping = true;
        this.notifyAll();
    }
}

/** Controls the allocation of <code>BurglarThread</code>s to free CPUs */
class ThreadController implements Runnable {

    private static Logger LOGGER = Logger.getLogger(ThreadController.class.getName());

    private ThreadedAgentScheduler cc;

    private int numCPUs;

    private boolean[] cpuStatus;

    public ThreadController(ThreadedAgentScheduler cc) {
        this.cc = cc;
        this.numCPUs = Runtime.getRuntime().availableProcessors();
        this.cpuStatus = new boolean[this.numCPUs];
        for (int i = 0; i < this.numCPUs; i++) {
            this.cpuStatus[i] = true;
        }
    }

    /**
	 * Start the ThreadController. Iterate over all burglars, starting
	 * <code>BurglarThread</code>s on free CPUs. If no free CPUs then wait for a
	 * BurglarThread to finish.
	 */
    public void run() {
        for (IAgent b : ContextManager.getAllAgents()) {
            boolean foundFreeCPU = false;
            while (!foundFreeCPU) {
                synchronized (this) {
                    cpus: for (int i = 0; i < this.numCPUs; i++) {
                        if (this.cpuStatus[i]) {
                            foundFreeCPU = true;
                            this.cpuStatus[i] = false;
                            (new Thread(new BurglarThread(this, i, b))).start();
                            break cpus;
                        }
                    }
                    if (!foundFreeCPU) {
                        this.waitForBurglarThread();
                    }
                }
            }
        }
        boolean allFinished = false;
        while (!allFinished) {
            allFinished = true;
            synchronized (this) {
                cpus: for (int i = 0; i < this.cpuStatus.length; i++) {
                    if (!this.cpuStatus[i]) {
                        allFinished = false;
                        break cpus;
                    }
                }
                if (!allFinished) {
                    this.waitForBurglarThread();
                }
            }
        }
        this.cc.setBurglarsFinishedStepping();
    }

    /**
	 * Causes the ThreadController to wait for a BurglarThred to notify it that
	 * it has finished and a CPU has become free.
	 */
    private synchronized void waitForBurglarThread() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "", e);
            ContextManager.stopSim(e, ThreadedAgentScheduler.class);
        }
    }

    /**
	 * Tell this <code>ThreadController</code> that one of the CPUs is no free
	 * and it can stop waiting
	 * 
	 * @param cpuNumber
	 *            The CPU which is now free
	 */
    public synchronized void setCPUFree(int cpuNumber) {
        this.cpuStatus[cpuNumber] = true;
        this.notifyAll();
    }
}

/** Single thread to call a Burglar's step method */
class BurglarThread implements Runnable {

    private static Logger LOGGER = Logger.getLogger(BurglarThread.class.getName());

    private IAgent theburglar;

    private ThreadController tc;

    private int cpuNumber;

    public BurglarThread(ThreadController tc, int cpuNumber, IAgent b) {
        this.tc = tc;
        this.cpuNumber = cpuNumber;
        this.theburglar = b;
    }

    public void run() {
        try {
            this.theburglar.step();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "ThreadedAgentScheduler caught an error, telling model to stop", ex);
            ContextManager.stopSim(ex, this.getClass());
        }
        tc.setCPUFree(this.cpuNumber);
    }
}
