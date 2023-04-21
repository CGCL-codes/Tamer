package lv.accenture.jbcamp.ex08;

public class Printer implements Runnable {

    Queue printQueue;

    boolean stateIsRunning;

    static Printer instance = new Printer();

    public static Printer getPrinter() {
        return instance;
    }

    private Printer() {
        super();
        this.stateIsRunning = true;
        printQueue = new CircularQueue(5);
    }

    public void halt() {
        stateIsRunning = false;
    }

    public synchronized boolean addJob(PrintJob job) {
        try {
            printQueue.addBack(job);
        } catch (FullQueueException e) {
            return false;
        }
        this.notify();
        return true;
    }

    private synchronized PrintJob getJob() throws EmptyQueueException {
        PrintJob result = null;
        while (result == null) {
            try {
                result = (PrintJob) printQueue.getFront();
                printQueue.removeFront();
            } catch (EmptyQueueException e) {
                System.err.println("     Printer is waiting for a job.");
                try {
                    this.wait();
                } catch (InterruptedException e1) {
                    System.err.println("Printer ERROR in WAIT");
                }
            }
        }
        return result;
    }

    @Override
    public void run() {
        PrintJob job;
        while (stateIsRunning || !printQueue.isEmpty()) {
            job = this.getJob();
            System.out.println("     Printing is starting   : " + job.getName());
            for (int i = 0; i < job.pages; ++i) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println("Printer ERROR in SLEEP");
                }
                System.out.println("     Printing page " + (i + 1) + " of " + job.pages);
            }
            System.out.println("     Printing has completed : " + job.getName());
        }
        System.out.println("\n\nPrinter is halted.");
    }
}
