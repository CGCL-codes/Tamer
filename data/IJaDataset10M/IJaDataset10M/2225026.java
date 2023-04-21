package simulatorKit.simulation.station;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Igaler
 * 
 * This class represent the base abstract station driven by ProximityJulia protocol
 */
public class ProximityJuliaStation extends JuliaStation {

    public static void setPeersVector() {
        if (ALL_PEER_STATIONS == null) ALL_PEER_STATIONS = new Vector<IUncompletedStation>(); else ALL_PEER_STATIONS.clear();
        Iterator stItr = SIMULATOR.getStations().iterator();
        ProximityJuliaStation currStation;
        while (stItr.hasNext()) {
            currStation = (ProximityJuliaStation) stItr.next();
            if (currStation instanceof IUncompletedStation) ALL_PEER_STATIONS.add((IUncompletedStation) currStation);
        }
    }

    /**
     * @param index
     * @throws Exception
     */
    public ProximityJuliaStation(short index) throws Exception {
        super(index);
    }

    protected boolean canDownloadFrom(JuliaStation other) {
        if (!this.knownNeighbors.contains(other)) return false;
        float partRatio = this.getPartsRatio();
        if (partRatio < 0.25) return true;
        float distanceRatio = ((float) 1.0 + (this.knownNeighbors.tailSet(other).size())) / (float) this.knownNeighbors.size();
        if ((partRatio < 0.5 && distanceRatio < 0.75) || (partRatio < 0.75 && distanceRatio < 0.5) || (distanceRatio < 0.25)) return true;
        return false;
    }
}
