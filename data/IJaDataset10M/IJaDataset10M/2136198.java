package hu.usz.inf.netspotter.agentlogic;

public class TimeChecker extends Thread {

    private Timers timers;

    private boolean run = true;

    public TimeChecker() {
        super();
    }

    public void halt() {
        run = false;
    }

    public void run() {
        while (run) {
            synchronized (timers) {
                timers.check();
            }
            try {
                sleep(100);
            } catch (Exception e) {
                System.out.println("Exception");
            }
        }
        System.out.println("Finished thread");
    }

    public Timers getTimers() {
        return timers;
    }

    public void setTimers(Timers timers) {
        this.timers = timers;
    }
}
