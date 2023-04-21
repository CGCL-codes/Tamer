package fr.inria.zuist.app.atc;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Vector;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import infovis.graph.DefaultGraph;
import infovis.graph.io.GraphReaderFactory;
import infovis.utils.RowIterator;
import infovis.column.NumberColumn;
import infovis.column.StringColumn;
import infovis.column.IntColumn;
import infovis.column.DoubleColumn;
import infovis.DynamicTable;
import com.xerox.VTM.glyphs.VCircleST;
import net.claribole.zvtm.glyphs.DPathST;
import fr.inria.zuist.engine.Region;

class GraphManager {

    static final double CC = 21600 * 2 / 180.0;

    static int MAX_WEIGHT = 400000;

    static final int AIRPORT_NODE_SIZE = 10;

    static final Color SHAPE_FILL_COLOR = Color.YELLOW;

    static final Color SHAPE_STROKE_COLOR = Color.BLACK;

    static final Color LABEL_STROKE_COLOR = Color.BLACK;

    static final float DEFAULT_ARC_ALPHA = 0.7f;

    static final String[] transitions = { Region.APPEAR_STR, Region.APPEAR_STR, Region.DISAPPEAR_STR, Region.DISAPPEAR_STR };

    ATCExplorer application;

    String GML_FILE_PATH = "data/airtraffic_2004.gml";

    String GEO_FILE_PATH = "data/airports.csv";

    static final String INPUT_CSV_SEP = ";";

    HashMap iata2airport;

    int shapeID = 0;

    int labelID = 0;

    LNode[] allNodes;

    LEdge[] allArcs;

    GraphManager(ATCExplorer app) {
        this.application = app;
        loadAirportLocalisations();
    }

    void loadAirportLocalisations() {
        iata2airport = new HashMap();
        try {
            FileInputStream fis = new FileInputStream(new File(GEO_FILE_PATH));
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line = br.readLine();
            while (line != null) {
                if (line.length() > 0) {
                    String[] data = line.split(INPUT_CSV_SEP);
                    if (iata2airport.containsKey(data[0])) {
                        System.err.println("Warning: airport " + data[0] + " defined multiple times: " + data[1]);
                    } else {
                        iata2airport.put(data[0], new Airport(data));
                    }
                }
                line = br.readLine();
            }
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Loaded " + iata2airport.size() + " airport localizations");
    }

    double QUAD_ANGLE = Math.PI / 6.0;

    void loadAirTraffic() {
        Region region = application.sm.createRegion(0, 0, 84600, 43200, 0, 4, "TR0", "Traffic", 1, transitions, Region.ORDERING_DISTANCE_STR, false, null, null);
        DefaultGraph g = new DefaultGraph();
        DynamicTable vtable = g.getVertexTable();
        DynamicTable etable = g.getEdgeTable();
        vtable.addColumn(new DoubleColumn("x"));
        vtable.addColumn(new DoubleColumn("y"));
        etable.addColumn(new IntColumn("weight"));
        GraphReaderFactory.readGraph(GML_FILE_PATH, g);
        NumberColumn col = (NumberColumn) etable.getColumn("weight");
        RowIterator ri = g.edgeIterator();
        int edge;
        NumberColumn xcol = (NumberColumn) vtable.getColumn("x");
        NumberColumn ycol = (NumberColumn) vtable.getColumn("y");
        StringColumn accol = (StringColumn) vtable.getColumn("airport_code");
        StringColumn cncol = (StringColumn) vtable.getColumn("city_name");
        HashMap int2node = new HashMap();
        int count = 0;
        int weight;
        Vector aes = new Vector();
        while (ri.hasNext()) {
            edge = ri.nextRow();
            weight = (int) Math.round(col.getDoubleAt(edge));
            if (weight > MAX_WEIGHT) {
                int v1 = g.getFirstVertex(edge);
                int v2 = g.getSecondVertex(edge);
                LNode tail = getNode(region, v1, accol, cncol, xcol, ycol, int2node);
                LNode head = getNode(region, v2, accol, cncol, xcol, ycol, int2node);
                if (tail != null && head != null) {
                    double alpha = Math.atan2(head.getShape().vy - tail.getShape().vy, head.getShape().vx - tail.getShape().vx);
                    double ds = Math.sqrt(Math.pow((head.getShape().vx - tail.getShape().vx), 2) + Math.pow((head.getShape().vy - tail.getShape().vy), 2)) / 2.0;
                    double rho = ds / Math.cos(QUAD_ANGLE);
                    long cx = Math.round(tail.getShape().vx + rho * Math.cos(alpha + QUAD_ANGLE));
                    long cy = Math.round(tail.getShape().vy + rho * Math.sin(alpha + QUAD_ANGLE));
                    DPathST p = new DPathST(tail.getShape().vx, tail.getShape().vy, 0, SHAPE_FILL_COLOR, DEFAULT_ARC_ALPHA);
                    p.addQdCurve(head.getShape().vx, head.getShape().vy, cx, cy, true);
                    application.vsm.addGlyph(p, application.bSpace);
                    application.bSpace.atBottom(p);
                    LEdge e = new LEdge(weight, p);
                    e.setDirected(true);
                    e.setTail(tail);
                    e.setHead(head);
                    count++;
                    aes.add(e);
                }
            }
        }
        allArcs = (LEdge[]) aes.toArray(new LEdge[aes.size()]);
        Vector ans = new Vector(int2node.values());
        allNodes = (LNode[]) ans.toArray(new LNode[ans.size()]);
        System.out.println("Constructing " + allNodes.length + " airports");
        System.out.println("Constructing " + allArcs.length + " connections");
    }

    LNode getNode(Region region, int nodeIndex, StringColumn airport_code, StringColumn city_name, NumberColumn xc, NumberColumn yc, HashMap i2n) {
        Integer kv = new Integer(nodeIndex);
        if (i2n.containsKey(kv)) {
            return (LNode) i2n.get(kv);
        } else {
            String iataCode = airport_code.get(nodeIndex);
            if (iata2airport.containsKey(iataCode)) {
                Airport ap = (Airport) iata2airport.get(iataCode);
                long x = Math.round(ap.lng * GraphManager.CC);
                long y = Math.round(ap.lat * GraphManager.CC);
                VCircleST shape = new VCircleST(x, y, 1, AIRPORT_NODE_SIZE, SHAPE_FILL_COLOR, SHAPE_STROKE_COLOR, 1.0f);
                BText label = new BText(x, y - 3, 1, LABEL_STROKE_COLOR, ap.iataCode, BText.TEXT_ANCHOR_MIDDLE, 1.0f, 1);
                application.vsm.addGlyph(shape, application.bSpace);
                application.vsm.addGlyph(label, application.bSpace);
                LNode res = new LNode(iataCode, ap.name, shape, label);
                i2n.put(kv, res);
                return res;
            }
        }
        return null;
    }
}

class Airport {

    String iataCode;

    String name;

    double lat;

    double lng;

    Airport(String[] data) {
        iataCode = data[0];
        name = data[1];
        lat = Double.parseDouble(data[2]);
        lng = Double.parseDouble(data[3]);
    }
}
