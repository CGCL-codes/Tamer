package com.jchy.graph.core;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import com.jchy.graph.Edge;
import org.apache.log4j.Logger;
import com.jchy.graph.Graph;
import com.jchy.graph.Vertex;

/**
 * GraphItData - data object for graphs, (x,y) positions of vertices,
 *               colors of vertices, which vertices are selected,
 *               and persistence (open, save and save as)
 * @author chyjm
 *
 */
public class GraphItData {

    private Graph aGraph;

    private HashMap<Vertex, Pixel> map;

    private ArrayList<Vertex> selected;

    private HashMap<Vertex, Color> colors;

    private static Logger log = Logger.getLogger(GraphItData.class);

    private String theGraphItFile;

    private Color currentColor;

    private static int staticLeaf;

    private static int staticLeafIncr;

    public GraphItData() {
        aGraph = new Graph();
        map = new HashMap<Vertex, Pixel>();
        selected = new ArrayList<Vertex>();
        colors = new HashMap<Vertex, Color>();
        theGraphItFile = "";
    }

    public void createSampleGraph() {
        Long aLong = new Long(1L);
        Long bLong = new Long(2L);
        Long cLong = new Long(3L);
        Pixel aPixel = new Pixel(30, 30);
        Pixel bPixel = new Pixel(70, 30);
        Pixel cPixel = new Pixel(70, 80);
        aGraph.setBidirectional(Graph.BI_DIRECTIONAL);
        Vertex aVertex = new Vertex(aLong);
        Vertex bVertex = new Vertex(bLong);
        Vertex cVertex = new Vertex(cLong);
        map.put(aVertex, aPixel);
        map.put(bVertex, bPixel);
        map.put(cVertex, cPixel);
        Edge abEdge = new Edge(aVertex, bVertex);
        aGraph.addEdge(abEdge);
        Edge bcEdge = new Edge(bVertex, cVertex);
        aGraph.addEdge(bcEdge);
        Edge acEdge = new Edge(aVertex, cVertex);
        aGraph.addEdge(acEdge);
        Color aColor = new Color(255, 100, 100);
        Color bColor = new Color(100, 255, 100);
        Color cColor = new Color(100, 100, 255);
        colors.put(aVertex, aColor);
        colors.put(bVertex, bColor);
        colors.put(cVertex, cColor);
    }

    public void deSelectAll() {
        selected = new ArrayList<Vertex>();
    }

    public void colorSelectedVertices(Color inColor) {
        for (Vertex aVertex : selected) {
            colors.put(aVertex, inColor);
        }
    }

    public String getTheGraphItFile() {
        return theGraphItFile;
    }

    public Graph getAGraph() {
        return aGraph;
    }

    public void setAGraph(Graph graph) {
        aGraph = graph;
    }

    public HashMap<Vertex, Pixel> getMap() {
        return map;
    }

    public void setMap(HashMap<Vertex, Pixel> map) {
        this.map = map;
    }

    public ArrayList<Vertex> getSelected() {
        return selected;
    }

    public void setSelected(ArrayList<Vertex> selected) {
        this.selected = selected;
    }

    public void save() {
        FileOutputStream fw;
        PrintStream out;
        try {
            fw = new FileOutputStream(theGraphItFile);
            out = new PrintStream(fw);
            out.println("BIDIRECTIONAL " + String.valueOf(aGraph.isBidirectional()));
            ArrayList<Vertex> vertices = aGraph.getVertices();
            for (Vertex a : vertices) {
                out.println("VERTEX " + String.valueOf(a.getId()));
            }
            ArrayList<Edge> edges = aGraph.getEdges();
            for (Edge e : edges) {
                out.println("EDGE " + String.valueOf(e.getFrom().getId()) + " " + String.valueOf(e.getTo().getId()));
            }
            for (Vertex a : vertices) {
                Pixel aPixel = map.get(a);
                if (aPixel != null) {
                    out.println("LOCATION " + String.valueOf(a.getId()) + " " + String.valueOf(aPixel.getX()) + " " + String.valueOf(aPixel.getY()));
                }
            }
            for (Vertex a : vertices) {
                Color aColor = colors.get(a);
                if (aColor != null) {
                    out.println("COLOR " + String.valueOf(a.getId()) + " " + String.valueOf(aColor.getRed()) + " " + String.valueOf(aColor.getGreen()) + " " + String.valueOf(aColor.getBlue()));
                }
            }
            for (Vertex a : selected) {
                out.println("SELECTED " + String.valueOf(a.getId()));
            }
        } catch (IOException ioe) {
            log.error("I/O Exception: " + ioe);
        }
    }

    public void saveAs(String inGraphItFile) {
        theGraphItFile = inGraphItFile;
        save();
    }

    public void open(String inGraphItFile) {
        FileReader frs = null;
        BufferedReader in = null;
        String line = "";
        theGraphItFile = inGraphItFile;
        aGraph = new Graph();
        map = new HashMap<Vertex, Pixel>();
        selected = new ArrayList<Vertex>();
        try {
            frs = new FileReader(inGraphItFile);
            in = new BufferedReader(frs);
            line = in.readLine();
            while (line != null) {
                StringTokenizer st = new StringTokenizer(line);
                String dataType = st.nextToken();
                if (dataType.equals("BIDIRECTIONAL")) {
                    String value = st.nextToken();
                    if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes")) aGraph.setBidirectional(Graph.BI_DIRECTIONAL); else aGraph.setBidirectional(Graph.NOT_BI_DIRECTIONAL);
                } else if (dataType.equals("VERTEX")) {
                    String id = st.nextToken();
                    long longId = Long.parseLong(id);
                    Vertex aVertex = new Vertex(longId);
                    aGraph.addVertex(aVertex);
                } else if (dataType.equals("EDGE")) {
                    String fromId = st.nextToken();
                    String toId = st.nextToken();
                    long longFromId = Long.parseLong(fromId);
                    long longToId = Long.parseLong(toId);
                    Vertex vertexFrom = new Vertex(longFromId);
                    Vertex vertexTo = new Vertex(longToId);
                    Edge anEdge = new Edge(vertexFrom, vertexTo);
                    aGraph.addEdge(anEdge);
                } else if (dataType.equals("LOCATION")) {
                    String id = st.nextToken();
                    String x = st.nextToken();
                    String y = st.nextToken();
                    long longId = Long.parseLong(id);
                    Vertex aVertex = new Vertex(longId);
                    double intX = Double.parseDouble(x);
                    double intY = Double.parseDouble(y);
                    Pixel aPixel = new Pixel(intX, intY);
                    map.put(aVertex, aPixel);
                } else if (dataType.equals("COLOR")) {
                    String id = st.nextToken();
                    String red = st.nextToken();
                    String green = st.nextToken();
                    String blue = st.nextToken();
                    long longId = Long.parseLong(id);
                    Vertex aVertex = new Vertex(longId);
                    int intRed = Integer.parseInt(red);
                    int intGreen = Integer.parseInt(green);
                    int intBlue = Integer.parseInt(blue);
                    Color aColor = new Color(intRed, intGreen, intBlue);
                    colors.put(aVertex, aColor);
                } else if (dataType.equals("SELECTED")) {
                    String id = st.nextToken();
                    long longId = Long.parseLong(id);
                    Vertex aVertex = new Vertex(longId);
                    selected.add(aVertex);
                }
                line = in.readLine();
            }
        } catch (IOException ioe) {
            log.error("I/O Exception: " + ioe);
        }
        for (Vertex aVertex : aGraph.getVertices()) {
            if (colors.get(aVertex) == null) {
                colors.put(aVertex, Color.BLACK);
            }
        }
    }

    public void castCircle(int maxX, int maxY) {
        int count;
        ArrayList<Vertex> vertices;
        Pixel aPixel;
        map = new HashMap<Vertex, Pixel>();
        count = aGraph.getVertexCount();
        vertices = aGraph.getVertices();
        int i = 0;
        for (Vertex aVertex : vertices) {
            int x = (int) Math.round((maxX / 2) + (maxX * Math.cos((360 * i / count) * (Math.PI / 180)) / 2));
            int y = (int) Math.round((maxY / 2) + (maxY * Math.sin((360 * i / count) * (Math.PI / 180)) / 2));
            aPixel = new Pixel(x, y);
            map.put(aVertex, aPixel);
            i++;
        }
    }

    public Vertex getClosestVertex(int inX, int inY) {
        double min = -1;
        double curr = 0;
        Pixel pix;
        Vertex result = null;
        ArrayList<Vertex> verts = aGraph.getVertices();
        for (Vertex aVertex : verts) {
            pix = map.get(aVertex);
            curr = (pix.getX() - inX) * (pix.getX() - inX) + (pix.getY() - inY) * (pix.getY() - inY);
            if ((min == -1) || (curr < min)) {
                min = curr;
                result = aVertex;
            }
        }
        return result;
    }

    /** 
	 * 
	 */
    public void moveVerticesConnectedToClosest(int inFromX, int inFromY, int inToX, int inToY) {
        Vertex minVertex = null;
        ArrayList<Vertex> compConnected;
        Pixel oldPix, newPix;
        minVertex = getClosestVertex(inFromX, inFromY);
        if (minVertex != null) {
            compConnected = aGraph.getComponentVertices(minVertex);
            for (Vertex aVertex : compConnected) {
                oldPix = map.get(aVertex);
                newPix = new Pixel(oldPix.getX() + inToX - inFromX, oldPix.getY() + inToY - inFromY);
                map.put(aVertex, newPix);
            }
        }
    }

    public void moveVerticesConnectedToClosestSameColor(int inFromX, int inFromY, int inToX, int inToY) {
        Vertex minVertex = null;
        ArrayList<Vertex> compConnected;
        Pixel oldPix, newPix;
        Color minColor;
        minVertex = getClosestVertex(inFromX, inFromY);
        if (minVertex != null) {
            minColor = colors.get(minVertex);
            compConnected = aGraph.getComponentVertices(minVertex);
            for (Vertex aVertex : compConnected) {
                if (colors.get(aVertex).equals(minColor)) {
                    oldPix = map.get(aVertex);
                    newPix = new Pixel(oldPix.getX() + inToX - inFromX, oldPix.getY() + inToY - inFromY);
                    map.put(aVertex, newPix);
                }
            }
        }
    }

    /**
	 * @return the colors
	 */
    public HashMap<Vertex, Color> getColors() {
        return colors;
    }

    /**
	 * @param colors the colors to set
	 */
    public void setColors(HashMap<Vertex, Color> colors) {
        this.colors = colors;
    }

    public void setCurrentColor(Color inColor) {
        currentColor = inColor;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public void colorClosestVertex(int x, int y) {
        Vertex minVertex = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            getColors().put(minVertex, currentColor);
        }
    }

    public void moveVertexTo(Vertex inVertex, Pixel to) {
        if (inVertex != null) map.put(inVertex, to);
    }

    public void moveVertexDelta(Vertex inVertex, Pixel delta) {
        if (inVertex != null) {
            Pixel oldPix = map.get(inVertex);
            Pixel newPix = new Pixel(oldPix.getX() + delta.getX(), oldPix.getY() + delta.getY());
            map.put(inVertex, newPix);
        }
    }

    public void moveVerticesDelta(List<Vertex> inVertexList, Pixel delta) {
        for (Vertex aVertex : inVertexList) {
            moveVertexDelta(aVertex, delta);
        }
    }

    public void moveClosestVertex(int startX, int startY, int endX, int endY) {
        Vertex minVertex = null;
        Pixel delta;
        minVertex = getClosestVertex(startX, startY);
        if (minVertex != null) {
            delta = new Pixel(endX - startX, endY - startY);
            moveVertexDelta(minVertex, delta);
        }
    }

    public void removeClosestVertex(int x, int y) {
        Vertex minVertex = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            aGraph.removeVertex(minVertex);
        }
    }

    public void addEdgesByClosest(int x, int y) {
        Vertex minVertex = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            for (Vertex aVertex : selected) {
                Edge newEdge = new Edge(minVertex, aVertex);
                aGraph.addEdge(newEdge);
            }
        }
    }

    public void removeEdgesByClosest(int x, int y) {
        Vertex minVertex = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            for (Vertex aVertex : selected) {
                Edge delEdge = new Edge(minVertex, aVertex);
                aGraph.removeEdge(delEdge);
            }
        }
    }

    public Vertex addVertex(double x, double y) {
        long maxLong = -1;
        for (Vertex aVertex : aGraph.getVertices()) {
            if ((maxLong == -1) || (aVertex.getId() > maxLong)) maxLong = aVertex.getId();
        }
        maxLong = maxLong + 1L;
        Vertex newVertex = new Vertex(maxLong);
        Pixel newPixel = new Pixel(x, y);
        aGraph.addVertex(newVertex);
        map.put(newVertex, newPixel);
        selected.add(newVertex);
        return newVertex;
    }

    public void selectVerticesByNearest(int x, int y) {
        Vertex minVertex = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            if (selected.contains(minVertex)) selected.remove(minVertex); else selected.add(minVertex);
        }
    }

    public void selectVerticesConnectedToNearest(int x, int y) {
        Vertex minVertex = null;
        ArrayList<Vertex> compConnected;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            compConnected = aGraph.getComponentVertices(minVertex);
            if (selected.contains(minVertex)) {
                selected.removeAll(compConnected);
            } else {
                selected.addAll(compConnected);
            }
        }
    }

    public void selectComponentsWithSameSizeAsSelected(int x, int y) {
        ArrayList<Graph> compList;
        ArrayList<Vertex> verts;
        int selSize = 0;
        Vertex minVertex = null;
        Graph minVertexConnected = null;
        minVertex = getClosestVertex(x, y);
        if (minVertex != null) {
            minVertexConnected = aGraph.getComponentSubGraph(minVertex);
            selSize = minVertexConnected.getVertexCount();
        }
        compList = aGraph.getComponents();
        for (Graph compGraph : compList) {
            verts = compGraph.getVertices();
            if (verts.size() == selSize) {
                verts = compGraph.getVertices();
                verts.removeAll(selected);
                selected.addAll(verts);
            }
        }
    }

    public void selectVerticesByBox(int startX, int startY, int endX, int endY) {
        int minX, minY, maxX, maxY;
        minX = Math.min(startX, endX);
        minY = Math.min(startY, endY);
        maxX = Math.max(startX, endX);
        maxY = Math.max(startY, endY);
        ArrayList<Vertex> verts = aGraph.getVertices();
        for (Vertex aVertex : verts) {
            Pixel pix = map.get(aVertex);
            if (pix.getX() >= minX && pix.getX() <= maxX && pix.getY() >= minY && pix.getY() <= maxY) if (selected.contains(aVertex)) selected.remove(aVertex); else selected.add(aVertex);
        }
    }

    public void moveSelectedVertices(int startX, int startY, int endX, int endY) {
        for (Vertex aVertex : selected) {
            Pixel oldPix = map.get(aVertex);
            Pixel newPix = new Pixel(oldPix.getX() + endX - startX, oldPix.getY() + endY - startY);
            map.put(aVertex, newPix);
        }
    }

    public void sortVerticesByClosest(int vertX, int vertY) {
        Vertex start;
        ArrayList<Vertex> compConnect, nextConnected, currConnected, vertConnected;
        int count, depth;
        Pixel pix;
        Graph sub;
        depth = 50;
        staticLeaf = 50;
        staticLeafIncr = 20;
        sub = new Graph();
        sub.setBidirectional(Graph.NOT_BI_DIRECTIONAL);
        start = getClosestVertex(vertX, vertY);
        pix = map.get(start);
        pix.setX(depth);
        currConnected = new ArrayList<Vertex>();
        currConnected.add(start);
        compConnect = new ArrayList<Vertex>();
        compConnect.add(start);
        sub.addVertex(start);
        do {
            depth += 60;
            nextConnected = new ArrayList<Vertex>();
            for (Vertex cVertex : currConnected) {
                vertConnected = aGraph.getConnectedVertices(cVertex);
                vertConnected.removeAll(compConnect);
                vertConnected.removeAll(currConnected);
                for (Vertex deep : vertConnected) {
                    pix = map.get(deep);
                    pix.setX(depth);
                    sub.addEdge(new Edge(cVertex, deep));
                }
                nextConnected.addAll(vertConnected);
            }
            compConnect.addAll(nextConnected);
            currConnected = new ArrayList<Vertex>();
            currConnected.addAll(nextConnected);
            count = nextConnected.size();
        } while (count > 0);
        setAvgY(sub, start);
    }

    public void setAvgY(Graph inGraph, Vertex inVert) {
        int sum, avg;
        ArrayList<Vertex> conn;
        Pixel pix;
        conn = inGraph.getConnectedVertices(inVert);
        Collections.sort(conn);
        pix = map.get(inVert);
        if (conn.size() == 0) {
            pix.setY(staticLeaf);
            staticLeaf += staticLeafIncr;
        } else {
            sum = 0;
            for (Vertex connV : conn) {
                setAvgY(inGraph, connV);
                sum += map.get(connV).getY();
            }
            avg = Integer.valueOf(sum / conn.size());
            pix.setY(avg);
        }
    }
}
