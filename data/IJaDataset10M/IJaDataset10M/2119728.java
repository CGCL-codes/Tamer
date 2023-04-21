package playground.marcel.osm2matsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import org.matsim.counts.Count;
import org.matsim.network.NetworkLayer;
import org.matsim.utils.geometry.CoordI;
import org.matsim.utils.geometry.shared.Coord;
import org.matsim.utils.geometry.transformations.WGS84toCH1903LV03;
import org.matsim.utils.io.MatsimXmlParser;
import org.xml.sax.Attributes;

public class OSMReader extends MatsimXmlParser {

    private final Map<String, OSMNode> nodes = new HashMap<String, OSMNode>();

    private final Map<String, OSMWay> ways = new HashMap<String, OSMWay>();

    private final List<String> ignored = new ArrayList<String>();

    private OSMWay currentWay = null;

    private long id = 0;

    public OSMReader() {
        super();
        this.setValidating(false);
        this.ignored.add("footway");
        this.ignored.add("pedestrian");
        this.ignored.add("cycleway");
        this.ignored.add("service");
        this.ignored.add("steps");
        this.ignored.add("track");
        this.ignored.add("bridleway");
        this.ignored.add("footway; cycleway");
        this.ignored.add("footway; unclassified");
        this.ignored.add("trackj");
        this.ignored.add("pedestrian ");
        this.ignored.add("footway;steps");
        this.ignored.add("pedestrian; residential");
        this.ignored.add(" track");
        this.ignored.add("truck");
        this.ignored.add("unsurfaced");
    }

    @Override
    public void startTag(final String name, final Attributes atts, final Stack<String> context) {
        if ("node".equals(name)) {
            this.nodes.put(atts.getValue("id"), new OSMNode(Long.parseLong(atts.getValue("id")), Double.parseDouble(atts.getValue("lat")), Double.parseDouble(atts.getValue("lon"))));
        } else if ("way".equals(name)) {
            this.currentWay = new OSMWay(Long.parseLong(atts.getValue("id")));
            this.ways.put(atts.getValue("id"), this.currentWay);
        } else if ("nd".equals(name)) {
            if (this.currentWay != null) {
                this.currentWay.nodes.add(atts.getValue("ref"));
            }
        } else if ("tag".equals(name)) {
            if (this.currentWay != null) {
                this.currentWay.tags.put(atts.getValue("k"), atts.getValue("v"));
            }
        }
    }

    @Override
    public void endTag(final String name, final String content, final Stack<String> context) {
        if ("way".equals(name)) {
            this.currentWay = null;
        }
    }

    public NetworkLayer convert() {
        NetworkLayer network = new NetworkLayer();
        network.setCapacityPeriod(3600);
        for (OSMWay way : this.ways.values()) {
            String highway = way.tags.get("highway");
            if (highway != null) {
                if (!this.ignored.contains(highway)) {
                    this.nodes.get(way.nodes.get(0)).ways++;
                    this.nodes.get(way.nodes.get(way.nodes.size() - 1)).ways++;
                    for (String id : way.nodes) {
                        OSMNode node = this.nodes.get(id);
                        node.used = true;
                        node.ways++;
                    }
                }
            }
        }
        for (OSMNode node : this.nodes.values()) {
            if (node.ways == 1) {
                node.used = false;
            }
            if (node.used) {
                network.createNode(Long.toString(node.id), Double.toString(node.coord.getX()), Double.toString(node.coord.getY()), null);
            }
        }
        this.id = 1;
        for (OSMWay way : this.ways.values()) {
            String highway = way.tags.get("highway");
            if (highway != null) {
                if (!this.ignored.contains(highway)) {
                    boolean oneway = way.tags.containsKey("oneway") && "yes".equals(way.tags.get("oneway"));
                    OSMNode fromNode = this.nodes.get(way.nodes.get(0));
                    double length = 0.0;
                    OSMNode lastToNode = fromNode;
                    if (fromNode.used) {
                        for (int i = 1, n = way.nodes.size(); i < n; i++) {
                            OSMNode toNode = this.nodes.get(way.nodes.get(i));
                            length += lastToNode.coord.calcDistance(toNode.coord);
                            if (toNode.used) {
                                createLink(network, way, fromNode, toNode, oneway, length);
                                fromNode = toNode;
                                length = 0.0;
                            }
                            lastToNode = toNode;
                        }
                    }
                }
            }
        }
        return network;
    }

    private void createLink(final NetworkLayer network, final OSMWay way, final OSMNode fromNode, final OSMNode toNode, final boolean oneway, final double length) {
        String highway = way.tags.get("highway");
        String fromId = Long.toString(fromNode.id);
        String toId = Long.toString(toNode.id);
        String len = Double.toString(length);
        String freespeed = "13.3";
        String capacity = "600";
        String nofLanes = "1";
        String origId = Long.toString(way.id);
        if ("motorway".equals(highway)) {
            nofLanes = "2";
            capacity = "4000";
            freespeed = Double.toString(120.0 / 3.6);
        } else if ("motorway_link".equals(highway)) {
            capacity = "1500";
            freespeed = Double.toString(80.0 / 3.6);
        } else if ("trunk".equals(highway)) {
            capacity = "2000";
            freespeed = Double.toString(80.0 / 3.6);
        } else if ("trunk_link".equals(highway)) {
            capacity = "1500";
            freespeed = Double.toString(60.0 / 3.6);
        } else if ("primary".equals(highway)) {
            capacity = "1500";
            freespeed = Double.toString(80.0 / 3.6);
        } else if ("primary_link".equals(highway)) {
            capacity = "1500";
            freespeed = Double.toString(60.0 / 3.6);
        } else if ("secondary".equals(highway)) {
            capacity = "1000";
            freespeed = Double.toString(60.0 / 3.6);
        } else if ("tertiary".equals(highway) || "minor".equals(highway)) {
            capacity = "600";
            freespeed = Double.toString(45.0 / 3.6);
        } else if ("unclassified".equals(highway)) {
            capacity = "600";
            freespeed = Double.toString(45.0 / 3.6);
        } else if ("residential".equals(highway)) {
            capacity = "600";
            freespeed = Double.toString(35.0 / 3.6);
        } else if ("living_street".equals(highway)) {
            capacity = "300";
            freespeed = Double.toString(20.0 / 3.6);
        } else {
            System.out.println("unknown kind of highway: " + highway);
            return;
        }
        network.createLink(Long.toString(this.id), fromId, toId, len, freespeed, capacity, nofLanes, origId, null);
        this.id++;
        if (!oneway) {
            network.createLink(Long.toString(this.id), toId, fromId, len, freespeed, capacity, nofLanes, origId, null);
            this.id++;
        }
    }

    private static class OSMNode {

        public final long id;

        public final double lat;

        public final double lon;

        public boolean used = false;

        public int ways = 0;

        public final CoordI coord;

        public static final WGS84toCH1903LV03 transform = new WGS84toCH1903LV03();

        public OSMNode(final long id, final double lat, final double lon) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.coord = transform.transform(new Coord(lon, lat));
        }
    }

    private static class OSMWay {

        public final long id;

        public final List<String> nodes = new ArrayList<String>();

        public final Map<String, String> tags = new HashMap<String, String>();

        public Count count = null;

        public OSMNode countFromNode = null;

        public OSMWay(final long id) {
            this.id = id;
        }
    }
}
