package uchicago.src.sim.network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import uchicago.src.sim.util.SimUtilities;
import cern.colt.list.IntArrayList;

/**
 * Class for constructing networks from text files in Pajek's *.net Arc/Edge
 * list file format. Will not read Pajek Arclist/Edgelist format, or Pajek
 * matrix format.  Requires that if "*Arcs" and "*Edges" both exist in file,
 * "*Arcs" must be before "*Edges" (this is Pajek's default).
 *<BR><BR>
 * Pajek is Windows-based freeware, written by Vladimir Batagelj and
 * Andrej Mrvar,University of Ljubljana,Slovenia downloadable from:
 * http://vlado.fmf.uni-lj.si/pub/networks/pajek/
 * <BR><BR>
 * currently does not read colors, edge widths, node sizes, etc..
 *
 * @version $Revision: 1.7 $ $Date: 2004/11/03 19:51:01 $
 * @author Skye Bender-deMoll e-mail skyebend@santafe.edu
 */
public class PajekNetReader extends Object {

    private BufferedReader reader;

    private ArrayList nodeList = new ArrayList();

    private IntArrayList xCoordList = new IntArrayList();

    private IntArrayList yCoordList = new IntArrayList();

    public PajekNetReader(String fileAndPath) {
        try {
            reader = new BufferedReader(new FileReader(fileAndPath));
        } catch (IOException ex) {
            try {
                if (reader != null) reader.close();
            } catch (IOException ex1) {
            }
            SimUtilities.showError("Error reading network file: " + fileAndPath, ex);
            System.exit(0);
        }
    }

    /**
   * Returns a list of nodes of type nodeClass forming network corresponding
   * to the *.net file with edges of class edgeClass and with strengths
   * corresponding to the arcs and edges in the file
   * @param nodeClass the class to construct nodes from. Must implement Node
   * and have no-argument constructor
   * @param edgeClass the class to construct nodes from. Must implement Edge
   * and have no-argument constructor
   * @throws IOException
   */
    public List getNetwork(Class nodeClass, Class edgeClass) throws IOException {
        int numNodes = 0;
        nodeList.clear();
        xCoordList.clear();
        yCoordList.clear();
        numNodes = parseHeader(reader.readLine());
        for (int n = 1; n <= numNodes; n++) {
            parseNode(nodeClass, reader.readLine().trim(), n);
        }
        String subHead = reader.readLine().trim();
        String line = "";
        if (subHead.equals("*Arcs")) {
            line = reader.readLine().trim();
            while ((line != null) && !line.equals("*Edges") && !line.equals("")) {
                parseArc(edgeClass, line.trim());
                line = reader.readLine();
            }
        }
        if (subHead.equals("*Edges") || ((line != null) && line.equals("*Edges"))) {
            line = reader.readLine().trim();
            while (line != null && !line.equals("")) {
                parseEdge(edgeClass, line.trim());
                line = reader.readLine();
            }
        }
        return nodeList;
    }

    /**
   * Returns a list of nodes of type nodeClass forming network corresponding
   * to the *.net file with edges of class edgeClass and strength corresponding
   * to the
   * arcs and edges in the file.  Coordinates of nodes are parsed from file,
   * and can be accessed via getXY(Node node) when constructing wrapper classes
   * spaceWidth and spaceHeight are needed to rescale the Pajek coordinates to
   * values used by repast.
   * @param nodeClass the class to construct nodes from. Must implement Node
   * and have no-argument constructor
   * @param edgeClass the class to construct nodes from. Must implement Edge
   * and have no-argument constructor
   * @param spaceWidth the horizontal dimension of the display in pixels
   * @param spaceHeight the vertical dimension of the display in pixels
   * @throws IOException
   */
    public List getDrawableNetwork(Class nodeClass, Class edgeClass, int spaceWidth, int spaceHeight) throws IOException {
        int numNodes = 0;
        nodeList.clear();
        xCoordList.clear();
        yCoordList.clear();
        numNodes = parseHeader(reader.readLine());
        for (int n = 1; n <= numNodes; n++) {
            parseDrawableNode(nodeClass, spaceWidth, spaceHeight, reader.readLine().trim(), n);
        }
        String subHead = reader.readLine().trim();
        String line = "";
        if (subHead.equals("*Arcs")) {
            line = reader.readLine().trim();
            while ((line != null) && !line.equals("*Edges")) {
                parseArc(edgeClass, line.trim());
                line = reader.readLine();
            }
        }
        if (subHead.equals("*Edges") || ((line != null) && line.equals("*Edges"))) {
            line = reader.readLine().trim();
            while (line != null && !line.equals("")) {
                parseEdge(edgeClass, line.trim());
                line = reader.readLine();
            }
        }
        return nodeList;
    }

    private int parseHeader(String firstLine) throws IOException {
        int returnInt = 0;
        StringTokenizer header = new StringTokenizer(firstLine.trim(), " ");
        if (header.countTokens() == 2) {
            if (header.nextToken().equals("*Vertices")) {
                try {
                    returnInt = Integer.parseInt(header.nextToken());
                } catch (NumberFormatException intParseEx) {
                    SimUtilities.showError("Unable to parse number of Vertices: ", intParseEx);
                }
            } else {
                SimUtilities.showError("File must begin with \"*Vertices:\" ", new IOException("Unable to Parse .net file"));
            }
        } else {
            SimUtilities.showError("Wrong number of entries in first line of file", new IOException("Unable to Parse .net file"));
        }
        return returnInt;
    }

    private void parseNode(Class nodeClass, String line, int lineNumber) throws IOException {
        Node node;
        String label = "";
        int nodeNumber = 0;
        StringTokenizer quoteTokenizer = new StringTokenizer(line, "\"");
        if (quoteTokenizer.countTokens() < 2) {
            SimUtilities.showError("Line " + lineNumber + "is missing entries", new IOException("Unable to Parse .net file"));
        } else {
            String first = quoteTokenizer.nextToken();
            label = quoteTokenizer.nextToken();
            StringTokenizer nodeString = new StringTokenizer(first, " ");
            try {
                nodeNumber = Integer.parseInt(nodeString.nextToken());
                if (nodeNumber != lineNumber) {
                    SimUtilities.showError("Vertex line numbers must be in sequence: ", new IOException("Unable to Parse .net file"));
                }
            } catch (NumberFormatException intParseEx) {
                SimUtilities.showError("Each vertex must be proceeded by an integer line number: ", intParseEx);
            }
        }
        try {
            node = (Node) nodeClass.newInstance();
            node.setNodeLabel(label);
            nodeList.add(node);
        } catch (IllegalAccessException e) {
            SimUtilities.showError("Error instantiating nodes", e);
        } catch (InstantiationException e) {
            SimUtilities.showError("Error instantiating nodes", e);
        }
    }

    private void parseArc(Class edgeClass, String line) throws IOException {
        Edge edge;
        int fromIndex, toIndex;
        double strength = 1;
        StringTokenizer edgeString = new StringTokenizer(line, " ");
        if (edgeString.countTokens() < 2) {
            SimUtilities.showError("An Arc is missing entries", new IOException("Unable to Parse .net file"));
        } else {
            try {
                fromIndex = Integer.parseInt(edgeString.nextToken()) - 1;
                toIndex = Integer.parseInt(edgeString.nextToken()) - 1;
                if (edgeString.hasMoreTokens()) {
                    strength = Double.parseDouble(edgeString.nextToken());
                }
                try {
                    Node fromNode = (Node) nodeList.get(fromIndex);
                    Node toNode = (Node) nodeList.get(toIndex);
                    edge = (Edge) edgeClass.newInstance();
                    edge.setFrom(fromNode);
                    edge.setTo(toNode);
                    edge.setStrength(strength);
                    fromNode.addOutEdge(edge);
                    toNode.addInEdge(edge);
                } catch (IllegalAccessException e) {
                    SimUtilities.showError("Error instantiating Edge", e);
                } catch (InstantiationException e) {
                    SimUtilities.showError("Error instantiating Edge", e);
                }
            } catch (NumberFormatException ex) {
                SimUtilities.showError("Problem with Arc entries", ex);
            }
        }
    }

    private void parseEdge(Class edgeClass, String line) {
        Edge edge;
        Edge otherEdge;
        int fromIndex, toIndex;
        double strength = 1;
        StringTokenizer edgeString = new StringTokenizer(line, " ");
        if (edgeString.countTokens() < 2) {
            SimUtilities.showError("An Edge is missing entries", new IOException("Unable to Parse .net file"));
        } else {
            try {
                fromIndex = Integer.parseInt(edgeString.nextToken()) - 1;
                toIndex = Integer.parseInt(edgeString.nextToken()) - 1;
                if (edgeString.hasMoreTokens()) {
                    strength = Double.parseDouble(edgeString.nextToken());
                }
                try {
                    Node fromNode = (Node) nodeList.get(fromIndex);
                    Node toNode = (Node) nodeList.get(toIndex);
                    edge = (Edge) edgeClass.newInstance();
                    edge.setFrom(fromNode);
                    edge.setTo(toNode);
                    edge.setStrength(strength);
                    fromNode.addOutEdge(edge);
                    toNode.addInEdge(edge);
                    otherEdge = (Edge) edgeClass.newInstance();
                    otherEdge.setFrom(toNode);
                    otherEdge.setTo(fromNode);
                    otherEdge.setStrength(strength);
                    toNode.addOutEdge(otherEdge);
                    fromNode.addInEdge(otherEdge);
                } catch (IllegalAccessException e) {
                    SimUtilities.showError("Error instantiating Edge", e);
                } catch (InstantiationException e) {
                    SimUtilities.showError("Error instantiating Edge", e);
                }
            } catch (NumberFormatException ex) {
                SimUtilities.showError("Problem with Edge entries", ex);
            }
        }
    }

    private void parseDrawableNode(Class nodeClass, int spaceWidth, int spaceHeight, String line, int lineNumber) throws IOException {
        Node node;
        double x = 0;
        double y = 0;
        String label = "";
        int nodeNumber = 0;
        StringTokenizer quoteTokenizer = new StringTokenizer(line, "\"");
        if (quoteTokenizer.countTokens() < 3) {
            SimUtilities.showError("Line " + lineNumber + "is missing entries", new IOException("Unable to Parse .net file"));
        } else {
            String first = quoteTokenizer.nextToken();
            label = quoteTokenizer.nextToken();
            String last = quoteTokenizer.nextToken();
            StringTokenizer nodeString = new StringTokenizer(first, " ");
            try {
                nodeNumber = Integer.parseInt(nodeString.nextToken());
                if (nodeNumber != lineNumber) {
                    SimUtilities.showError("Vertex line numbers must be in sequence: ", new IOException("Unable to Parse .net file"));
                }
            } catch (NumberFormatException intParseEx) {
                SimUtilities.showError("Each vertex must be proceeded by an integer line number: ", intParseEx);
            }
            nodeString = new StringTokenizer(last, " ");
            if (nodeString.countTokens() < 2) {
                SimUtilities.showError("Line " + lineNumber + "is missing entries", new IOException("Unable to Parse .net file"));
            } else {
                try {
                    x = Double.parseDouble(nodeString.nextToken());
                    y = Double.parseDouble(nodeString.nextToken());
                    x = Math.round(x * spaceWidth);
                    y = Math.round(y * spaceHeight);
                } catch (NumberFormatException doubleParseEx) {
                    SimUtilities.showError("Error reading .net file, unable to parse coordinates: ", doubleParseEx);
                }
            }
        }
        try {
            node = (Node) nodeClass.newInstance();
            node.setNodeLabel(label);
            nodeList.add(node);
            xCoordList.add((int) x);
            yCoordList.add((int) y);
        } catch (IllegalAccessException e) {
            SimUtilities.showError("Error instantiating Drawable node", e);
        } catch (InstantiationException e) {
            SimUtilities.showError("Error instantiating Drawable node", e);
        }
    }

    /**
   * Returns an int[x,y] with the coordinates of the node if they were
   * read from the Pajek *.net file.  Otherwise returns [0,0]
   * @param node the node (must be of class nodeClass) to return x and y for
   */
    public int[] getXY(Node node) {
        int[] coords = { 0, 0 };
        int index = nodeList.indexOf(node);
        if ((index != -1) && (xCoordList.size() > 0)) {
            coords[0] = xCoordList.get(index);
            coords[1] = yCoordList.get(index);
        }
        return coords;
    }
}
