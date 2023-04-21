package gossipServices.gossipThread;

import gossipServices.basic.view.PartialView;

/**
 * Description:
 * The GossipThread class extends directly from the Thread one.
 * So it's allowed to start a thread. It's similar to a skeleton 
 * for the gossip protocol that implements features such as
 * the start and stop and creation operations. 
 * It contains a reference the view that will be used by subclasses,
 * and the cucleLength_msec variable which indicates the number of
 * milliseconds that a cycles is composed of.
 *
 */
public class GossipThread extends Thread {

    /**
	 * Number of milliseconds in a cycle.
	 */
    protected long cycleLenght_msec;

    /**
	 * A reference to the view of the current Node that will be
	 * accessed from the subclasses (Active and Passive threads).
	 */
    protected PartialView view;

    /**
	 * Variable used only during the stop of the thread after
	 * an InterruptedException cast to understand if the 
	 * current thread has to stop.
	 */
    protected boolean hasToStop = false;

    /**
	 * Reference to a class implementing the 
	 * GossipThreadInstructions that gathers all instructions
	 * performed by the thread during the gossip.
	 */
    protected GossipThreadsInstructions gossipProtocol;

    public GossipThread() {
    }

    public GossipThread(String name, long cycleLenghtMsec, PartialView view, GossipThreadsInstructions gossipProtocol) {
        super(name);
        cycleLenght_msec = cycleLenghtMsec;
        this.view = view;
        this.gossipProtocol = gossipProtocol;
    }

    public GossipThread(long cycleLenghtMsec, PartialView view, GossipThreadsInstructions gossipProtocol) {
        super();
        cycleLenght_msec = cycleLenghtMsec;
        this.view = view;
        this.gossipProtocol = gossipProtocol;
    }

    /**
	 * @param name
	 * @param cycleLenghtMsec
	 * @param view2
	 */
    protected GossipThread(String name, long cycleLenghtMsec, PartialView view2) {
        super(name);
        cycleLenght_msec = cycleLenghtMsec;
        this.view = view2;
    }

    public void run() {
        boolean running = true;
        System.out.println("Started a gossip thread: " + getName());
        while (running) {
            try {
                sleep(cycleLenght_msec);
                gossipProtocol.gossipThreadInstructions();
            } catch (InterruptedException e) {
                if (hasToStop) {
                    running = false;
                    System.out.println("stopped: " + getName());
                } else {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopGossipThread() {
        hasToStop = true;
        this.interrupt();
    }

    protected final void setGossipProtocol(GossipThreadsInstructions gossipProtocol) {
        this.gossipProtocol = gossipProtocol;
    }
}
