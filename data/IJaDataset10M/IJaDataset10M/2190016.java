package playground.mrieser.core.mobsim.features;

import org.apache.log4j.Logger;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.utils.misc.Time;

public class StatusFeature implements MobsimFeature {

    private static final Logger log = Logger.getLogger(StatusFeature.class);

    private double nextTime = 0.0;

    private boolean isFirst = true;

    private double firstTime = Double.NaN;

    private long firstRealTime = 0;

    @Override
    public void beforeMobSim() {
    }

    @Override
    public void doSimStep(final double time) {
        if (this.isFirst) {
            this.firstTime = time;
            this.isFirst = false;
            this.firstRealTime = System.currentTimeMillis();
        }
        if (time >= this.nextTime) {
            log.info("Simulation time: " + Time.writeTime(time) + " speed-up: " + ((time - this.firstTime) / ((System.currentTimeMillis() - this.firstRealTime) / 1000.0)));
            Gbl.printMemoryUsage();
        }
        while (this.nextTime <= time) {
            this.nextTime += 3600.0;
        }
    }

    @Override
    public void afterMobSim() {
    }
}
