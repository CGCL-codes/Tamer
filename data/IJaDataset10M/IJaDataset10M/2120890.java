package iwork.eventlogger;

import iwork.seheap2.*;
import javax.swing.*;

public class SimpleEventHandler extends JFrame {

    public static final int SERVER_TIMEOUT = 20000;

    private SecureEventHeap theEventHeap;

    private EventRegistration theRegistration;

    private String appName;

    private boolean validEventHeap;

    private String heapMachine;

    private int heapPort;

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

        public SecureEventHeap theEventHeap;

        public String heapMachine;

        public int heapPort;

        public EventHeapInitializer(String heapMachine, int heapPort) {
            theEventHeap = null;
            this.heapMachine = heapMachine;
            this.heapPort = heapPort;
        }

        public void run() {
            System.out.println("Attempting to connect to Event Heap:\n" + "\tEHeap Host Machine:\t" + heapMachine + "\n" + "\tEHeap Port:\t\t" + heapPort + "\n" + "\tOur Application Name:\t\t'" + appName + "'");
            theEventHeap = new SecureEventHeap(heapMachine, -1);
            if (theEventHeap == null) System.out.println("Error: EventHeap object could not be created.");
            System.out.println("\tMy source name:\t\t" + theEventHeap.getSourceName());
        }

        public boolean validEventHeap() {
            return (theEventHeap != null);
        }
    }

    public void sendEvent(SecureEvent e) {
        sendEvent(e, false);
    }

    public void sendEvent(SecureEvent e, boolean printEvent) {
        try {
            theEventHeap.putEvent(e);
            if (printEvent) {
                System.out.println("Event sent:");
                System.out.println(e.toString());
            }
        } catch (EventHeapException ex) {
            ex.printStackTrace();
            return;
        }
    }

    public void sendEvent(Tuple t) {
        sendEvent(t, false);
    }

    public void sendEvent(Tuple t, boolean printEvent) {
        try {
            SecureEvent e = new SecureEvent(t);
            sendEvent(e, printEvent);
        } catch (Exception ee) {
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
