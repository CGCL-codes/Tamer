package playground.scnadine.GPSDaten.coordAlgorithms;

import playground.scnadine.GPSDaten.GPSCoordFactory;

public abstract class GPSCoordAlgorithm {

    public GPSCoordAlgorithm() {
    }

    public abstract void run(GPSCoordFactory gpsFactory);
}
