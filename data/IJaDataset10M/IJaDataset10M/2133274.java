package org.matsim.contrib.evacuation.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.evacuation.config.EvacuationConfigGroup;
import org.matsim.contrib.evacuation.config.EvacuationConfigGroup.EvacuationScenario;
import org.matsim.contrib.evacuation.flooding.FloodingInfo;
import org.matsim.contrib.evacuation.flooding.FloodingReader;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkFactoryImpl;
import org.matsim.core.network.NetworkChangeEvent.ChangeType;
import org.matsim.core.network.NetworkChangeEvent.ChangeValue;
import org.matsim.core.utils.collections.QuadTree;
import org.matsim.core.utils.geometry.geotools.MGC;
import com.vividsolutions.jts.geom.Envelope;

public class NetworkChangeEventsFromNetcdf {

    private static final double MAX_DIST = 10;

    private static final double EARLIEST_FLOODING_TIME_STEP = 20;

    private final Envelope envelope = new Envelope(0, 0, 0, 0);

    private final List<FloodingReader> readers;

    private QuadTree<FloodingInfo> quadTree;

    private final Network network;

    private final double startTime;

    public NetworkChangeEventsFromNetcdf(List<FloodingReader> netcdfReaders, Scenario sc) {
        this.readers = netcdfReaders;
        this.network = sc.getNetwork();
        EvacuationScenario esc = ((EvacuationConfigGroup) sc.getConfig().getModule("evacuation")).getEvacuationScanrio();
        if (esc == EvacuationScenario.night) {
            this.startTime = 3 * 3600.;
        } else if (esc == EvacuationScenario.day) {
            this.startTime = 12 * 3600.;
        } else if (esc == EvacuationScenario.afternoon) {
            this.startTime = 16 * 3600.;
        } else {
            throw new RuntimeException("Unknown scenario: " + esc);
        }
    }

    public List<NetworkChangeEvent> createChangeEvents() {
        buildFiQuadTree();
        Map<Double, NetworkChangeEvent> events = new HashMap<Double, NetworkChangeEvent>();
        for (Link link : this.network.getLinks().values()) {
            double fTime = getFTime(link.getFromNode().getCoord());
            fTime = Math.min(fTime, getFTime(link.getToNode().getCoord()));
            if (fTime < Double.POSITIVE_INFINITY) {
                fTime = this.startTime + 60 * fTime;
                fTime = Math.max(fTime, this.startTime + 60 * EARLIEST_FLOODING_TIME_STEP);
                NetworkChangeEvent e = events.get(fTime);
                if (e == null) {
                    e = ((NetworkFactoryImpl) network.getFactory()).createNetworkChangeEvent(fTime);
                    events.put(fTime, e);
                    ChangeValue freespeedChange = new ChangeValue(ChangeType.ABSOLUTE, 0.);
                    e.setFreespeedChange(freespeedChange);
                }
                e.addLink(link);
            }
        }
        List<NetworkChangeEvent> ret = new ArrayList<NetworkChangeEvent>(events.values());
        Comparator<NetworkChangeEvent> c = new ChangeEventsComparator();
        Collections.sort(ret, c);
        return ret;
    }

    private static class ChangeEventsComparator implements Comparator<NetworkChangeEvent>, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public int compare(NetworkChangeEvent arg0, NetworkChangeEvent arg1) {
            if (arg0.getStartTime() < arg1.getStartTime()) {
                return -1;
            } else if (arg0.getStartTime() == arg1.getStartTime()) {
                throw new RuntimeException("this should not happen!!");
            }
            return 1;
        }
    }

    private double getFTime(Coord c) {
        FloodingInfo fi = this.quadTree.get(c.getX(), c.getY());
        if (fi.getCoordinate().distance(MGC.coord2Coordinate(c)) < MAX_DIST) {
            return fi.getFloodingTime();
        }
        return Double.POSITIVE_INFINITY;
    }

    private void buildFiQuadTree() {
        for (FloodingReader fr : this.readers) {
            this.envelope.expandToInclude(fr.getEnvelope());
        }
        this.quadTree = new QuadTree<FloodingInfo>(this.envelope.getMinX(), this.envelope.getMinY(), this.envelope.getMaxX(), this.envelope.getMaxY());
        for (FloodingReader fr : this.readers) {
            for (FloodingInfo fi : fr.getFloodingInfos()) {
                this.quadTree.put(fi.getCoordinate().x, fi.getCoordinate().y, fi);
            }
        }
    }
}
