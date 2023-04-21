package obs;

import alice.cartago.*;

/**
 * A simple clock artifact.
 * 
 * @author aricci
 *
 */
public class Clock extends Artifact {

    private boolean stopped;

    @OPERATION
    void init() {
        defineObsProperty("nticks", 0);
        stopped = false;
    }

    /**
	 * Start the clock.
	 * 
	 * <p>Events generated:
	 * <ul>
	 * <li>tick - a clock tick, every 100 ms</li>
	 * </ul>
	 * </p>
	 */
    @OPERATION
    void start() {
        try {
            stopped = false;
            nextStep("tick");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Stop the clock.
	 */
    @OPERATION
    void stop() {
        stopped = true;
        log("STOPPED.");
    }

    @OPSTEP(tguard = 100)
    void tick() {
        if (!stopped) {
            int nticks = getObsProperty("nticks").intValue();
            updateObsProperty("nticks", nticks + 1);
            log("TICK " + nticks);
            try {
                nextStep("tick");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
