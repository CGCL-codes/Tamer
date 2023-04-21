package playground.run;

import org.apache.log4j.Logger;

/**
 * This is a semi-stable entry point into Matsim4Urbansim.
 * 
 * @author nagel
 *
 */
public class Matsim4Urbansim {

    private static final Logger log = Logger.getLogger(Matsim4Urbansim.class);

    public static void main(String[] args) {
        log.info("Directing to playground tnicolai...");
        playground.tnicolai.matsim4opus.matsim4urbansim.MATSim4UrbanSimParcel.main(args);
    }
}
