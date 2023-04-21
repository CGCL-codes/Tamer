package playground.scnadine.gpsProcessingV2.coordAlgorithms;

import java.util.Iterator;
import org.matsim.core.config.Config;
import playground.scnadine.gpsProcessingV2.GPSCoord;
import playground.scnadine.gpsProcessingV2.GPSCoordFactory;

public class GPSCoordFilterSatellites extends GPSCoordAlgorithm {

    private int minSatellites;

    public GPSCoordFilterSatellites(Config config, String CONFIG_MODULE) {
        super();
        this.minSatellites = Integer.parseInt(config.findParam(CONFIG_MODULE, "minSatellites"));
    }

    @Override
    public void run(GPSCoordFactory gpsFactory) {
        Iterator<GPSCoord> it = gpsFactory.getCoords().iterator();
        while (it.hasNext()) {
            GPSCoord coord = it.next();
            if (coord.getSatellites() < this.minSatellites) {
                it.remove();
            }
        }
        Iterator<GPSCoord> itt = gpsFactory.getCoords().iterator();
        int coordId = 0;
        while (itt.hasNext()) {
            GPSCoord coord = itt.next();
            coord.setId(coordId);
            coordId++;
        }
    }
}
