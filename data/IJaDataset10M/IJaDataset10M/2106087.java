package iwork.gamecontroller;

import java.util.*;
import iwork.eheap2.*;
import iwork.eheap2.EventRegistration;
import iwork.eheap2.EventCallback;

public class SimpleEventHandler {

    public static final int SERVER_TIMEOUT = 10000;

    public static final int SEND_INTERVAL = 50;

    private EventHeap theEventHeap;

    private EventRegistration theRegistration;

    private String appName;

    private boolean validEventHeap;

    private String heapMachine;

    private int heapPort;

    private Timer timer = new Timer();

    private int numEventsSent = 0;

    private long latencySum = 0;

    private int numEventsProcessed = 0;

    public SimpleEventHandler(String appName, String heapMachine, int heapPort) {
        this.appName = appName;
        this.heapMachine = heapMachine;
        this.heapPort = heapPort;
        this.validEventHeap = false;
        this.theEventHeap = null;
        EventHeapInitializer initializer = new EventHeapInitializer(heapMachine, heapPort);
        Thread initThread = new Thread(initializer);
        initThread.start();
        try {
            initThread.join(SERVER_TIMEOUT);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (initThread.isAlive()) {
            initThread.interrupt();
            System.out.println("No valid server found. Server initialization interrupted.");
            validEventHeap = false;
        } else {
            validEventHeap = initializer.validEventHeap();
            if (validEventHeap) {
                this.theEventHeap = initializer.theEventHeap;
                System.out.println("Connection established.");
            }
        }
    }

    public boolean validEventHeap() {
        return validEventHeap;
    }

    public boolean registerForAll(EventCallback callback) {
        if (!validEventHeap) return false;
        try {
            theRegistration = theEventHeap.registerForAll(callback);
        } catch (EventHeapException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    private class EventHeapInitializer implements Runnable {

        public EventHeap theEventHeap;

        public String heapMachine;

        public int heapPort;

        public EventHeapInitializer(String heapMachine, int heapPort) {
            theEventHeap = null;
            this.heapMachine = heapMachine;
            this.heapPort = heapPort;
        }

        public void run() {
            System.out.println("Attempting to connect to Event Heap:\n" + "\tEHeap Host Machine:\t" + heapMachine + "\n" + "\tEHeap Port:\t\t" + heapPort + "\n" + "\tOur Application Name:\t\t'" + appName + "'");
            theEventHeap = new EventHeap(appName, heapMachine, heapPort, null, null, appName);
            if (theEventHeap == null) System.out.println("Error: EventHeap object could not be created.");
            System.out.println("\tMy source name:\t\t" + theEventHeap.getSourceName());
        }

        public boolean validEventHeap() {
            return (theEventHeap != null);
        }
    }

    public void sendEventNow(Event e) {
        try {
            if (theEventHeap != null) {
                theEventHeap.putEvent(e);
                numEventsSent++;
                if (GameController.BENCHMARK) {
                    double latency = GameController.hiResTimer.endTiming(GameController.triggerTime);
                    latencySum += latency;
                    System.out.print("\r" + numEventsSent + " events sent (" + latency + " ms).");
                    if (numEventsSent % 10 == 0) {
                        System.out.println(" \tAverage latency: " + latencySum / numEventsSent + " ms");
                    }
                }
            }
        } catch (EventHeapException ex) {
            ex.printStackTrace();
        }
    }

    public static final int TIMEOUT = 5000;

    public void sendEventAndWait(Event e) {
        try {
            if (theEventHeap != null) {
                Event[] templateEvents = { e };
                sendEventNow(e);
                theEventHeap.registerForEvents(templateEvents, new EventWaitCallback(Thread.currentThread()));
                boolean eventReceived = false;
                try {
                    Thread.sleep(TIMEOUT);
                } catch (InterruptedException ex) {
                    eventReceived = true;
                }
                if (!eventReceived) {
                    System.out.println("Error: event dropped.");
                } else {
                    System.out.println("Event sent successfully.");
                }
            }
        } catch (EventHeapException ex) {
            ex.printStackTrace();
        }
    }

    public class EventWaitCallback implements EventCallback {

        Thread t;

        public EventWaitCallback(Thread t) {
            this.t = t;
        }

        public boolean returnEvent(Event[] retEvents) {
            t.interrupt();
            return true;
        }
    }

    public void deregister() {
        if (theRegistration != null) theRegistration.deregister();
    }

    public String getMyName() {
        return appName;
    }

    public int getServerVersion() {
        if (theEventHeap != null) return theEventHeap.getVersion(); else return 0;
    }

    public String getMachine() {
        if (theEventHeap != null) return theEventHeap.getMachine(); else return "N/A";
    }

    public String getHeapMachine() {
        return heapMachine;
    }

    public int getHeapPort() {
        return heapPort;
    }
}
