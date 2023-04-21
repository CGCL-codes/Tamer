package playground.dsantani.gpsProcessing.tripAlgorithms;

import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.dsantani.gpsProcessing.GPSStage;
import playground.dsantani.gpsProcessing.GPSTrip;
import playground.dsantani.gpsProcessing.GPSTrips;

public class GPSTripCheckForPuT extends GPSTripAlgorithm {

    private double gpsMinModeProbabilityYalcin;

    public GPSTripCheckForPuT(Config config, String CONFIG_MODULE) {
        super();
        this.gpsMinModeProbabilityYalcin = Double.parseDouble(config.findParam(CONFIG_MODULE, "gpsMinModeProbabilityYalcin"));
    }

    @Override
    public void run(GPSTrips trips) {
        Iterator<GPSTrip> it = trips.getTrips().iterator();
        while (it.hasNext()) {
            GPSTrip currentTrip = it.next();
            Iterator<GPSStage> itt = currentTrip.getStages().getStages().iterator();
            while (itt.hasNext()) {
                GPSStage currentStage = itt.next();
                if (currentStage.getFuzzyModeProbability("urbanPuT") > this.gpsMinModeProbabilityYalcin || currentStage.getFuzzyModeProbability("rail") > this.gpsMinModeProbabilityYalcin) currentTrip.setContainsPuT(true);
            }
        }
    }
}
