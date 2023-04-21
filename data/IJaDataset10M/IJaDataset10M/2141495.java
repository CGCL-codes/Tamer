package topology.graphParsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.LineNumberReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import algorithms.centralityAlgorithms.betweenness.brandes.preprocessing.DataWorkshop;
import algorithms.shortestPath.ShortestPathAlgorithmInterface;
import server.common.DummyProgress;
import server.common.LoggingManager;
import server.common.ServerConstants;
import server.execution.AbstractExecution;
import topology.GraphInterface;
import topology.GraphRegularExpressions;
import topology.GraphAsHashMap;
import topology.VertexInfo;
import Opus5.BinarySearchTree;
import Opus5.Comparable;
import Opus5.Enumeration;
import javolution.util.FastList;
import javolution.util.FastMap;
import javolution.util.Index;

/**
 * @author Omer Zohar
 *
 */
public class AsRelationshipParser {

    public AsRelationshipParser() {
    }

    private class AS_Links implements Comparable {

        private int m_iASvertice = 0;

        private FastList<Index> m_llLinks = new FastList<Index>();

        ;

        public AS_Links(int vertice) {
            m_iASvertice = vertice;
            m_llLinks = new FastList<Index>();
        }

        public int compare(Comparable obj) {
            if (obj instanceof AS_Links) {
                AS_Links tmp = (AS_Links) obj;
                if (this.m_iASvertice < tmp.getASvertex()) return -1; else if (this.m_iASvertice > tmp.getASvertex()) return 1; else return 0;
            } else return -2;
        }

        public void addLink(int AStag) {
            this.m_llLinks.addFirst(Index.valueOf(AStag));
        }

        public int getASvertex() {
            return m_iASvertice;
        }

        public int getLinkedAS() {
            return this.m_llLinks.removeFirst().intValue();
        }

        public boolean isLinkedASEmpty() {
            return this.m_llLinks.isEmpty();
        }

        public boolean isEQ(Comparable object) {
            return false;
        }

        public boolean isGE(Comparable object) {
            return false;
        }

        public boolean isGT(Comparable object) {
            return false;
        }

        public boolean isLE(Comparable object) {
            return false;
        }

        public boolean isLT(Comparable object) {
            return false;
        }

        public boolean isNE(Comparable object) {
            return false;
        }
    }

    private class ASRelationShipStructure {

        private int m_iCount;

        private BinarySearchTree m_bstVertices;

        public ASRelationShipStructure() {
            m_iCount = 0;
            m_bstVertices = new BinarySearchTree();
        }

        public int getVerticesCount() {
            return m_iCount;
        }

        public int addASVertex(int vertex) {
            AS_Links as = new AS_Links(vertex);
            try {
                m_bstVertices.insert(as);
                m_iCount++;
            } catch (IllegalArgumentException ex) {
                return -1;
            }
            return 1;
        }

        public void addAsLink(AS_Links as, int dest) {
            AS_Links asadd = (AS_Links) m_bstVertices.find(as);
            if (asadd != null) {
                asadd.addLink(dest);
            } else {
                as.addLink(dest);
                m_bstVertices.insert(as);
                m_iCount++;
            }
        }

        public Enumeration getEnumerator() {
            return m_bstVertices.getEnumeration();
        }
    }

    public int lineCount(String filename) {
        int count = 0;
        try {
            RandomAccessFile randFile = new RandomAccessFile(filename, "r");
            long lastRec = randFile.length();
            randFile.close();
            FileReader fileRead = new FileReader(filename);
            LineNumberReader lineRead = new LineNumberReader(fileRead);
            lineRead.skip(lastRec);
            count = lineRead.getLineNumber() - 1;
            fileRead.close();
            lineRead.close();
            randFile.close();
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem("Couldn't Read from " + filename + "\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "LineCount", ex);
        }
        return count;
    }

    public GraphInterface<Index> loadFile(String filename, AbstractExecution progress, double percentage) {
        File file = new File(filename);
        int Linesinfile = 0;
        FileInputStream fis = null;
        GraphInterface<Index> graph = null;
        Linesinfile = lineCount(filename);
        try {
            fis = new FileInputStream(file);
            graph = analyzeFile(fis, progress, percentage, Linesinfile);
        } catch (FileNotFoundException ex) {
            LoggingManager.getInstance().writeSystem("Couldn't find the AS Relationship network file: " + filename + "\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "loadFile", ex);
        } finally {
            try {
                if (fis != null) fis.close();
            } catch (IOException ex) {
                LoggingManager.getInstance().writeSystem("Couldn't close FileInputStream to: " + file.getAbsoluteFile() + "\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "loadFile", ex);
            }
        }
        return graph;
    }

    private void updateLoadProgress(AbstractExecution progress, double percentage) {
        double p = progress.getProgress();
        p += 0.5 * percentage;
        progress.setProgress(p);
    }

    private void cleanClose(BufferedReader reader, String msg) {
        System.err.println(msg);
        try {
            if (reader != null) reader.close();
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem("An exception has occured while closing BufferedReader.\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "cleanClose", ex);
        }
    }

    private GraphInterface<Index> analyzeFile(InputStream in, AbstractExecution progress, double percentage, int Linesinfile) {
        GraphInterface<Index> graph = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ASRelationShipStructure relationstruct = new ASRelationShipStructure();
        int res = fillASRelationsList(relationstruct, reader, Linesinfile);
        if (res != -1) {
            graph = new GraphAsHashMap<Index>();
        } else {
            cleanClose(reader, "Vertices Could not be read from File or File is empty, exiting the program.");
            graph = null;
        }
        updateLoadProgress(progress, percentage);
        res = writeVerticesToGraph(relationstruct, graph);
        if (res == -1) {
            cleanClose(reader, "Could not write vertices to graph properly, exiting the program.");
            graph = null;
        }
        updateLoadProgress(progress, percentage);
        try {
            if (reader != null) reader.close();
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem("An exception has occured while closing BufferedReader.\n" + ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "analyzeFile", ex);
            graph = null;
        }
        return graph;
    }

    public int fillASRelationsList(ASRelationShipStructure relationstruct, BufferedReader reader, int Linesinfile) {
        String line = null;
        try {
            for (int i = 0; (i < Linesinfile) && ((line = reader.readLine()) != null); i++) {
                while (line.charAt(0) == '#') {
                    line = reader.readLine();
                }
                Matcher verticesLine = GraphRegularExpressions.ASRELATIONSHIP.matcher(line);
                if (verticesLine.find()) {
                    int arg1 = Integer.parseInt(verticesLine.group(1));
                    int arg2 = Integer.parseInt(verticesLine.group(2));
                    int direction = Integer.parseInt(verticesLine.group(3));
                    relationstruct.addASVertex(arg1);
                    relationstruct.addASVertex(arg2);
                    switch(direction) {
                        case -1:
                            {
                                relationstruct.addAsLink(new AS_Links(arg2), arg1);
                                break;
                            }
                        case 1:
                            {
                                relationstruct.addAsLink(new AS_Links(arg1), arg2);
                                break;
                            }
                        default:
                            {
                                relationstruct.addAsLink(new AS_Links(arg2), arg1);
                                relationstruct.addAsLink(new AS_Links(arg1), arg2);
                            }
                    }
                }
            }
        } catch (IOException ex) {
            LoggingManager.getInstance().writeSystem(ex.getMessage() + "\n" + ex.getStackTrace(), "AsRelationshipParser", "FillASRelationsList", ex);
            return -1;
        }
        return 1;
    }

    public int writeVerticesToGraph(ASRelationShipStructure relationstruct, GraphInterface<Index> graph) {
        Enumeration ASenumarator = relationstruct.getEnumerator();
        int serial = 0;
        FastMap<Index, Index> AStoserial = new FastMap<Index, Index>();
        while (ASenumarator.hasMoreElements()) {
            Object AS = ASenumarator.nextElement();
            if (AS instanceof AS_Links) {
                int originvertice = ((AS_Links) AS).getASvertex();
                if (AStoserial.get(Index.valueOf(originvertice)) == null) {
                    try {
                        graph.addVertex(Index.valueOf(serial), new VertexInfo(serial, Integer.toString(originvertice)));
                        AStoserial.put(Index.valueOf(originvertice), Index.valueOf(serial));
                        serial++;
                    } catch (Exception ex) {
                        LoggingManager.getInstance().writeSystem("Duplicates in temp structure, this is not suppose to happen" + ex.getMessage() + "\n" + ex.getStackTrace(), "WriteVerticesToGraph", "WriteVerticesToGraph", ex);
                        graph = null;
                        return -1;
                    }
                }
            } else {
                LoggingManager.getInstance().writeSystem("Tree Node is not in the right format" + "\n", "WriteVerticesToGraph", "WriteVerticesToGraph", new Exception());
            }
        }
        ASenumarator = relationstruct.getEnumerator();
        while (ASenumarator.hasMoreElements()) {
            Object AS = ASenumarator.nextElement();
            if (AS instanceof AS_Links) {
                int originvertice = ((AS_Links) AS).getASvertex();
                int originindex = AStoserial.get(Index.valueOf(originvertice)).intValue();
                while (!((AS_Links) AS).isLinkedASEmpty()) {
                    int destvertice = ((AS_Links) AS).getLinkedAS();
                    int destindex = AStoserial.get(Index.valueOf(destvertice)).intValue();
                    if (!graph.isEdge(Index.valueOf(originindex), Index.valueOf(destindex))) graph.addEdge(Index.valueOf(originindex), Index.valueOf(destindex));
                }
            } else {
                LoggingManager.getInstance().writeSystem("Tree Node is not in the right format" + "\n", "WriteVerticesToGraph", "WriteVerticesToGraph", new Exception());
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        AsRelationshipParser n = new AsRelationshipParser();
        GraphInterface<Index> g = n.loadFile("res/cutted_as-rel.txt", new DummyProgress(), 1);
        System.out.println(g.getNumberOfVertices());
        System.out.println(g.getNumberOfEdges());
        try {
            DataWorkshop dw = new DataWorkshop(ShortestPathAlgorithmInterface.DEFAULT, g, true, new DummyProgress(), 1);
            dw.saveToDisk(ServerConstants.DATA_DIR + "as.dw", new DummyProgress(), 1);
        } catch (Exception ex) {
            System.out.print(ex);
        }
    }
}
