package org.psepr.PsEPRServer;

/**
 * Sometimes the code here needs to send an event out.  This is a queue
 * and a processing thread that disconnects the sending thread from 
 * the processing of these messages.  This prevents thread tangling.
 * @author radams1
 */
public class InternalSender implements Inner, Outter {

    EventQueue internalQueue = null;

    Thread internalThread = null;

    /**
	 * 
	 */
    public InternalSender() {
        super();
        internalQueue = new EventQueue();
    }

    public void start() {
        internalThread = new Thread() {

            public void run() {
                processInboundQueue();
            }
        };
        internalThread.setName("InternalSender");
        internalThread.start();
    }

    /**
	 * Routine to process the inbound queue on the inside.  That is, as
	 * messages are received, they are put in the inboundQueue.  This
	 * routine takes events out of the queue and runs them through
	 * the router.  This is the thread that does all the work.
	 */
    private void processInboundQueue() {
        while (Global.keepWorking) {
            try {
                if (internalQueue == null) {
                    System.out.println("InternalSender: inboundQueue is null");
                }
                ServerEvent se = internalQueue.getEvent(1000);
                if (se != null) {
                    Global.router.routeEvent(se);
                }
            } catch (Exception e) {
                System.out.println("InternalSender threw:" + e.toString());
                try {
                    Thread.sleep(5);
                } catch (Exception ee) {
                }
            }
        }
        System.out.println("InternalSender exiting");
    }

    public EventQueue getInboundEventQueue() {
        return internalQueue;
    }

    public void send(ServerEvent pMessage) {
        internalQueue.putEvent(pMessage);
    }
}
