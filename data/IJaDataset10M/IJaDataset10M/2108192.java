package avrora.monitors;

import avrora.sim.Simulator;
import avrora.sim.util.MemTimer;

/**
 * The <code>TimerMonitor</code> gives apps access to a simple
 * start/stop timer interface
 *
 * @author John Regehr
 */
public class TimerMonitor extends MonitorFactory {

    public class Monitor implements avrora.monitors.Monitor {

        public final MemTimer memprofile;

        private static final int BASE = 2999;

        Monitor(Simulator s) {
            memprofile = new MemTimer(BASE);
            s.insertWatch(memprofile, BASE);
        }

        public void report() {
        }
    }

    public TimerMonitor() {
        super("The \"timer\" monitor watches a dedicated SRAM location for instructions " + "from the simulated program telling it to start or stop a timer.  Be sure to " + "set BASE to an address not otherwise used by your program.");
    }

    public avrora.monitors.Monitor newMonitor(Simulator s) {
        return new Monitor(s);
    }
}
