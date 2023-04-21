package scotlandyard.engine.impl;

import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import scotlandyard.engine.spec.IBMap;

/**
 * boardMap object which implements IBMap interface
 * construct new board map object that use for the game
 * @author simon
 * @version 3.0
 */
public final class BMap implements IBMap {

    final Random random = new Random();

    private int[][] nodes;

    public String[] positions;

    public String name;

    /**
	 * constructor of BMap
	 * @param name
	 * @param nodesCount
	 */
    public BMap(String name, int nodesCount) {
        this.nodes = new int[nodesCount][nodesCount];
        this.positions = new String[nodesCount];
        this.name = name;
    }

    /**
	 * read xml map file
	 * @param mapFile
	 * @throws Exception
	 */
    public BMap(String mapFile) throws Exception {
        this.name = mapFile;
        this.prepareMap(mapFile);
    }

    /**
	 * sets one connection between nodes of map,
	 * @param a - one end of node of the connection
	 * @param b - another end of node of the connection
	 * @param transport - transport type of the connection
	 */
    @Override
    public void connect(int a, int b, int transport) {
        this.nodes[a][b] += (int) Math.pow(2, transport);
        this.nodes[b][a] = this.nodes[a][b];
    }

    /**
	 * gets all the possible move nodes that the current node links to
	 * @param current
	 * @return array of nodes
	 */
    @Override
    public int[] getPossibleMoves(int current) {
        return this.nodes[current];
    }

    /**
	 * gets nodes that currently has no player on it
	 * @return any empty position chosen randomly
	 */
    @Override
    public int getEmptyPosition() {
        int result;
        do {
            result = random.nextInt(positions.length);
        } while (positions[result] != null);
        return result;
    }

    /**
	 * reads the xml file that stored information about the map, sets nodes and paths with specify transport type
	 * @param fileName - xml file which stored information about the given map
	 * @throws Exception
	 */
    @Override
    public void prepareMap(final String fileName) throws Exception {
        final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        final Document doc = docBuilder.parse(fileName);
        int coordinates = doc.getElementsByTagName("coordinate").getLength() + 1;
        this.nodes = new int[coordinates][coordinates];
        final NodeList linkList = doc.getElementsByTagName("link");
        for (int i = 0; i < linkList.getLength(); i++) {
            NamedNodeMap nodeMap = linkList.item(i).getAttributes();
            final int a = Integer.parseInt(nodeMap.getNamedItem("from").getNodeValue());
            final int b = Integer.parseInt(nodeMap.getNamedItem("to").getNodeValue());
            final String transport = nodeMap.getNamedItem("type").getNodeValue();
            this.connect(a, b, convTransport(transport));
        }
        this.positions = new String[coordinates];
    }

    /**
	 * convert transport to integer representation
	 * @param transport - string of the transport
	 * @return - integer of the transport represent
	 */
    public static int convTransport(String transport) {
        if ("BUS".equals(transport)) return BUS;
        if ("UG".equals(transport)) return UG;
        if ("WATER".equals(transport)) return WATER;
        if ("TAXI".equals(transport)) return TAXI;
        if ("DOUBLE".equals(transport)) return DOUBLE;
        return -1;
    }

    /**
	 * convert transport representation to string
	 * @param transport - integer of the transport represent
	 * @return - the string of name of the transport
	 */
    public static String convTransport(int transport) {
        if (BUS == (transport)) return "BUS";
        if (UG == (transport)) return "UG";
        if (WATER == (transport)) return "WATER";
        if (TAXI == (transport)) return "TAXI";
        if (DOUBLE == (transport)) return "DOUBLE";
        return "UNKNOWN";
    }

    /**
	 * get the array of positions for a map
	 * @return a list of the nodes on the map
	 */
    @Override
    public String[] getPositions() {
        return this.positions;
    }
}
