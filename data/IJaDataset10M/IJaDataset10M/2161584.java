package rails.algorithms;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import rails.algorithms.NetworkVertex.StationType;
import rails.algorithms.NetworkVertex.VertexType;
import rails.common.LocalText;
import rails.ui.swing.hexmap.HexMap;

/**
 * Links the results from the revenue calculator to the rails program
 * Each object defines the run of one train
 *
 */
public class RevenueTrainRun implements Comparable<RevenueTrainRun> {

    private static final int PRETTY_PRINT_LENGTH = 100;

    private static final int PRETTY_PRINT_INDENT = 10;

    protected static Logger log = Logger.getLogger(RevenueTrainRun.class.getPackage().getName());

    private RevenueAdapter revenueAdapter;

    private NetworkTrain train;

    private List<NetworkVertex> vertices;

    private List<NetworkEdge> edges;

    RevenueTrainRun(RevenueAdapter revenueAdapter, NetworkTrain train) {
        this.revenueAdapter = revenueAdapter;
        this.train = train;
        vertices = new ArrayList<NetworkVertex>();
        edges = new ArrayList<NetworkEdge>();
    }

    public List<NetworkVertex> getRunVertices() {
        return vertices;
    }

    /** 
     * returns true if train has a valid run (at least two vertices)
     */
    public boolean hasAValidRun() {
        return vertices.size() >= 2;
    }

    /**
     * returns the first vertex of a train run
     */
    public NetworkVertex getFirstVertex() {
        NetworkVertex startVertex = null;
        NetworkVertex firstVertex = null;
        for (NetworkVertex vertex : vertices) {
            if (startVertex == vertex) return firstVertex;
            if (startVertex == null) startVertex = vertex;
            firstVertex = vertex;
        }
        return startVertex;
    }

    /**
     * returns the last vertex of a train run
     */
    public NetworkVertex getLastVertex() {
        return vertices.get(vertices.size() - 1);
    }

    public Set<NetworkVertex> getUniqueVertices() {
        return new HashSet<NetworkVertex>(vertices);
    }

    public NetworkTrain getTrain() {
        return train;
    }

    public int getRunValue() {
        int value = 0;
        NetworkVertex startVertex = null;
        for (NetworkVertex vertex : vertices) {
            if (startVertex == vertex) continue;
            if (startVertex == null) startVertex = vertex;
            value += revenueAdapter.getVertexValue(vertex, train, revenueAdapter.getPhase());
        }
        for (RevenueBonus bonus : revenueAdapter.getRevenueBonuses()) {
            if (bonus.checkComplexBonus(vertices, train.getRailsTrain(), revenueAdapter.getPhase())) {
                value += bonus.getValue();
            }
        }
        return value;
    }

    boolean hasButtomRun() {
        boolean buttomRun = false;
        NetworkVertex startVertex = null;
        for (NetworkVertex vertex : vertices) {
            if (startVertex == vertex) buttomRun = true;
            if (startVertex == null) startVertex = vertex;
        }
        return buttomRun;
    }

    void addVertex(NetworkVertex vertex) {
        vertices.add(vertex);
    }

    void addEdge(NetworkEdge edge) {
        edges.add(edge);
    }

    /** defines the vertices from the list of edges */
    void convertEdgesToVertices() {
        vertices = new ArrayList<NetworkVertex>();
        if (edges.size() == 0) {
            return;
        } else if (edges.size() == 1) {
            vertices.add(edges.get(0).getSource());
            vertices.add(edges.get(0).getTarget());
            return;
        }
        NetworkEdge previousEdge = null;
        NetworkVertex startVertex = null;
        for (NetworkEdge edge : edges) {
            log.debug("Processing edge " + edge.toFullInfoString());
            if (previousEdge == null) {
                previousEdge = edge;
                continue;
            }
            NetworkVertex commonVertex = edge.getCommonVertex(previousEdge);
            if (startVertex == null) {
                if (commonVertex != null) {
                    log.debug("Head Run");
                    startVertex = previousEdge.getOtherVertex(commonVertex);
                    vertices.add(startVertex);
                    vertices.add(commonVertex);
                } else {
                    log.error("Error in revenue train run: cannot identify startVertex");
                }
            } else {
                if (commonVertex != null) {
                    log.debug("Added common vertex");
                    vertices.add(commonVertex);
                } else {
                    log.debug("Bottom Run");
                    vertices.add(previousEdge.getOtherVertex(vertices.get(vertices.size() - 1)));
                    vertices.add(startVertex);
                }
            }
            previousEdge = edge;
        }
        vertices.add(previousEdge.getOtherVertex(vertices.get(vertices.size() - 1)));
        log.debug("Converted edges to vertices " + vertices);
    }

    /** defines the edges from the list of vertices */
    void convertVerticesToEdges() {
        edges = new ArrayList<NetworkEdge>();
        if (vertices.size() <= 1) {
            return;
        }
        NetworkVertex startVertex = null;
        NetworkVertex previousVertex = null;
        for (NetworkVertex vertex : vertices) {
            if (startVertex == null) {
                startVertex = vertex;
                previousVertex = vertex;
                continue;
            }
            if (vertex != startVertex) {
                NetworkEdge edge = revenueAdapter.getRCGraph().getEdge(previousVertex, vertex);
                if (edge != null) {
                    edges.add(edge);
                } else {
                    log.error("Error in revenue train run: cannot find according edge");
                }
            }
            previousVertex = vertex;
        }
    }

    private String prettyPrintHexName(NetworkVertex vertex) {
        if (vertex.isVirtual()) {
            return vertex.getIdentifier();
        } else {
            return vertex.getHex().getName();
        }
    }

    private int prettyPrintNewLine(StringBuffer runPrettyPrint, int multiple, int initLength) {
        int length = runPrettyPrint.length() - initLength;
        if (length / PRETTY_PRINT_LENGTH != multiple) {
            multiple = length / PRETTY_PRINT_LENGTH;
            runPrettyPrint.append("\n");
            for (int i = 0; i < PRETTY_PRINT_INDENT; i++) runPrettyPrint.append(" ");
        }
        return multiple;
    }

    String prettyPrint(boolean includeDetails) {
        StringBuffer runPrettyPrint = new StringBuffer();
        runPrettyPrint.append(LocalText.getText("N_Train", train.toString()));
        runPrettyPrint.append(" = " + getRunValue());
        if (includeDetails) {
            Set<NetworkVertex> uniqueVertices = getUniqueVertices();
            int majors = NetworkVertex.numberOfVertexType(uniqueVertices, VertexType.STATION, StationType.MAJOR);
            int minors = NetworkVertex.numberOfVertexType(uniqueVertices, VertexType.STATION, StationType.MINOR);
            if (train.ignoresMinors() || minors == 0) {
                runPrettyPrint.append(LocalText.getText("RevenueStationsIgnoreMinors", majors));
            } else {
                runPrettyPrint.append(LocalText.getText("RevenueStations", majors, minors));
            }
            int initLength = runPrettyPrint.length();
            int multiple = prettyPrintNewLine(runPrettyPrint, -1, initLength);
            String currentHexName = null;
            NetworkVertex startVertex = null;
            for (NetworkVertex vertex : vertices) {
                if (startVertex == null) {
                    currentHexName = prettyPrintHexName(vertex);
                    startVertex = vertex;
                    runPrettyPrint.append(prettyPrintHexName(vertex) + "(");
                } else if (startVertex == vertex) {
                    currentHexName = prettyPrintHexName(vertex);
                    runPrettyPrint.append(") / ");
                    multiple = prettyPrintNewLine(runPrettyPrint, multiple, initLength);
                    runPrettyPrint.append(prettyPrintHexName(vertex) + "(0");
                    continue;
                } else if (!currentHexName.equals(prettyPrintHexName(vertex))) {
                    currentHexName = prettyPrintHexName(vertex);
                    runPrettyPrint.append("), ");
                    multiple = prettyPrintNewLine(runPrettyPrint, multiple, initLength);
                    runPrettyPrint.append(prettyPrintHexName(vertex) + "(");
                } else {
                    runPrettyPrint.append(",");
                }
                if (vertex.isStation()) {
                    runPrettyPrint.append(revenueAdapter.getVertexValueAsString(vertex, train, revenueAdapter.getPhase()));
                } else {
                    runPrettyPrint.append(vertex.getHex().getOrientationName(vertex.getSide()));
                }
            }
            if (currentHexName != null) {
                runPrettyPrint.append(")");
            }
            List<RevenueBonus> activeBonuses = new ArrayList<RevenueBonus>();
            for (RevenueBonus bonus : revenueAdapter.getRevenueBonuses()) {
                if (bonus.checkComplexBonus(vertices, train.getRailsTrain(), revenueAdapter.getPhase())) {
                    activeBonuses.add(bonus);
                }
            }
            Map<String, Integer> printBonuses = RevenueBonus.combineBonuses(activeBonuses);
            for (String bonusName : printBonuses.keySet()) {
                runPrettyPrint.append(" + ");
                runPrettyPrint.append(bonusName + "(" + printBonuses.get(bonusName) + ")");
                multiple = prettyPrintNewLine(runPrettyPrint, multiple, initLength);
            }
            runPrettyPrint.append("\n");
        }
        return runPrettyPrint.toString();
    }

    GeneralPath getAsPath(HexMap map) {
        GeneralPath path = new GeneralPath();
        for (NetworkEdge edge : edges) {
            List<NetworkVertex> edgeVertices = edge.getVertexPath();
            boolean initPath = false;
            for (NetworkVertex edgeVertex : edgeVertices) {
                Point2D edgePoint = NetworkVertex.getVertexPoint2D(map, edgeVertex);
                if (edgePoint == null) continue;
                if (!initPath) {
                    path.moveTo((float) edgePoint.getX(), (float) edgePoint.getY());
                    initPath = true;
                } else {
                    path.lineTo((float) edgePoint.getX(), (float) edgePoint.getY());
                }
            }
        }
        return path;
    }

    public int compareTo(RevenueTrainRun other) {
        return ((Integer) this.getRunValue()).compareTo(other.getRunValue());
    }
}
