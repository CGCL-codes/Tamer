package playground.scnadine.gpsCompareWithFlammData;

import java.util.ArrayList;
import playground.scnadine.gpsProcessing.GPSCoord;
import playground.scnadine.gpsProcessing.GPSCoordFactory;

public class FlammTPAs {

    private ArrayList<FlammTPA> tpas;

    private int tpaIdCounter;

    public FlammTPAs() {
        this.tpas = new ArrayList<FlammTPA>();
        this.tpaIdCounter = 0;
    }

    public void addTPA(FlammTPA tpa) {
        this.tpas.add(tpa);
    }

    public void clearTPAs() {
        this.tpas.clear();
    }

    public FlammTPA createTPA(int tpaId, GPSCoord[] coords) {
        FlammTPA tpa = new FlammTPA(tpaId, coords);
        return tpa;
    }

    public void createTPAs(GPSCoordFactory coords) {
        ArrayList<GPSCoord> tpaCoords = new ArrayList<GPSCoord>();
        for (GPSCoord coord : coords.getCoords()) {
            if (coord.getPlaceTypeFlamm().contains("TPA")) {
                tpaCoords.add(coord);
            } else if (!tpaCoords.isEmpty()) {
                GPSCoord[] tpaCoordsArray = new GPSCoord[tpaCoords.size()];
                tpaCoordsArray = tpaCoords.toArray(tpaCoordsArray);
                FlammTPA tpa = createTPA(this.tpaIdCounter, tpaCoordsArray);
                addTPA(tpa);
                this.tpaIdCounter++;
                tpaCoords.clear();
            }
        }
        if (!tpaCoords.isEmpty()) {
            GPSCoord[] tpaCoordsArray = new GPSCoord[tpaCoords.size()];
            tpaCoordsArray = tpaCoords.toArray(tpaCoordsArray);
            FlammTPA tpa = createTPA(this.tpaIdCounter, tpaCoordsArray);
            addTPA(tpa);
            this.tpaIdCounter++;
            tpaCoords.clear();
        }
    }

    public FlammTPA getTPA(int tpaId) {
        return this.tpas.get(tpaId);
    }

    public ArrayList<FlammTPA> getTPAs() {
        return this.tpas;
    }
}
