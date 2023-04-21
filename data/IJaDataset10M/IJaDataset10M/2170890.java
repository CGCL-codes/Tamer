package build;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Random;
import java.lang.Integer;
import connect.ConnectPE2R;
import connect.ConnectR2PE;
import connect.ConnectR2R;
import connect.ConnectVec;
import engine.Engine;
import engine.addressResolutionUnit.AddressResolutionUnit;
import generate.GraphTaskAssociations;
import generate.graph.ReadGraphInfo;
import pe.PE;
import packet.PacketMemManagedVec;
import pe.task.Task;
import router.AbsAddress2DMesh;
import router.Router;
import router.RouterConfig;
import router.RouterVec;
import java.util.Vector;
import connect.*;
import packet.Packetsizetable;
import packet.Node;
import router.Routetable;

public class Linker {

    protected int tgCount = -1;

    protected int tgPeriod = -1;

    protected int tDelay = -1;

    protected int pSize = -1;

    protected String filename = "Empty";

    protected boolean template = false;

    protected int sizeX = -1;

    protected int sizeY = -1;

    protected int seed = -1;

    protected String taskMapping = "Empty";

    protected String policyAlias = "Empty";

    protected RouterConfig rc;

    protected ReadGraphInfo readGraphs;

    protected GraphTaskAssociations graphs[];

    protected Engine engine;

    protected PE processingElements[];

    protected int[][] mappedTasksOnNodesByGraph;

    protected Router routers[][];

    protected int[][][] srcGraphTaskPacketToDestTask;

    protected Packetsizetable packtable[][];

    protected Node node[][];

    protected Node nodequeue[];

    protected int queuehead = 0;

    protected int queuetail = 0;

    protected int deletednodelist[];

    protected int deletednodenum = 0;

    protected int packetnum;

    protected int roottask[];

    protected int leafnode[];

    protected int leafpacket[];

    protected int noninterferepacket[][];

    protected int noninterferepacketnum[];

    protected int rootmark[];

    protected int finalnoninterferepacket[][];

    protected int finalnoninterferenum[];

    protected int nonintersingle[][];

    protected int nonintersinglenum[][];

    protected int roortasknum;

    protected int nonpacketgroup;

    protected int nonpacketindex;

    public GraphTaskBuilder gtb;

    public RouterBuilder rb;

    protected RouterVec rv = new RouterVec();

    protected ConnectVec cv = new ConnectVec();

    protected void LoadGTB(String graphTaskFile) {
        System.out.println("Loading from file: " + graphTaskFile);
        try {
            FileInputStream in = new FileInputStream(graphTaskFile);
            ObjectInputStream s = new ObjectInputStream(in);
            gtb = (GraphTaskBuilder) s.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
            System.err.println("Cannot load file, aborting...");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            System.err.println("Aborting...");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
            System.err.println("Aborting...");
            System.exit(0);
        }
    }

    protected void LoadRB(String routerFile) {
        System.out.println("Loading from file: " + routerFile);
        try {
            FileInputStream in = new FileInputStream(routerFile);
            ObjectInputStream s = new ObjectInputStream(in);
            rb = (RouterBuilder) s.readObject();
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("FileNotFoundException: " + e.getMessage());
            System.err.println("Cannot load file, aborting...");
            System.exit(0);
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            System.err.println("Aborting...");
            System.exit(0);
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
            System.err.println("Aborting...");
            System.exit(0);
        }
    }

    public Linker(String graphTaskFile, String routerFile, String taskMapping, int seed) {
        LoadGTB(graphTaskFile);
        LoadRB(routerFile);
        this.taskMapping = taskMapping;
        this.seed = seed;
    }

    public void Link() {
        this.engine = gtb.engine;
        this.tgCount = gtb.tgCount;
        this.tgPeriod = gtb.tgPeriod;
        this.tDelay = gtb.tDelay;
        this.pSize = gtb.pSize;
        this.filename = gtb.filename;
        this.template = gtb.template;
        this.readGraphs = gtb.readGraphs;
        this.graphs = gtb.graphs;
        this.srcGraphTaskPacketToDestTask = gtb.srcGraphTaskPacketToDestTask;
        this.sizeX = rb.sizeX;
        this.sizeY = rb.sizeY;
        this.routers = rb.routers;
        this.policyAlias = rb.policyAlias;
        this.rc = rb.rc;
        this.rv = rb.rv;
        this.cv = rb.cv;
        System.out.println("Creating PEs...");
        createPEs();
        System.out.println("Mapping Tasks to PEs...");
        mapTasksToPEs();
        associatePEs();
        System.out.println("Linking routers to engine...");
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                routers[x][y].engine = engine;
                rv.elementAt(x * sizeY + y).engine = engine;
            }
        }
        engine.rv = rv;
        System.out.println("Estabilishing connections between Routers, PEs...");
        connectRoutersToPEs();
        connectPEsToRouters();
        for (int i = 0; i < cv.size(); i++) {
            ((ConnectR2R) cv.elementAt(i)).engine = engine;
        }
        engine.cv.addAll(this.cv);
        System.out.println("Adding PEs to engine...");
        for (int i = 0; i < processingElements.length; i++) {
            engine.addPE(processingElements[i]);
        }
        for (int i = 0; i < readGraphs.getGraphCount(); i++) {
            engine.setGraphImpulsePeriod(readGraphs.getGraphInfo(i).getID(), readGraphs.getGraphInfo(i).getPeriod());
        }
        engine.setAddressResolutionUnit(new AddressResolutionUnit(2, sizeX, sizeY, mappedTasksOnNodesByGraph, srcGraphTaskPacketToDestTask));
        engine.sizeX = this.sizeX;
        engine.sizeY = this.sizeY;
        if (rb.policyAlias.equalsIgnoreCase("flee-routing")) {
            System.out.println("buildroutepath...");
            buildrouterpath();
        }
    }

    public void WriteEngineToFile(String savefile) {
        System.out.println("Saving engine to file: " + savefile + "...");
        engine.SaveEngineToFile(savefile);
    }

    private void connectRoutersToPEs() {
        for (int i = 0; i < sizeX * sizeY; i++) {
            int x = i % sizeX;
            int y = i / sizeX;
            engine.addConnect(new ConnectR2PE(engine, routers[x][y], processingElements[i]));
        }
    }

    private void connectPEsToRouters() {
        for (int i = 0; i < sizeX * sizeY; i++) {
            int x = i % sizeX;
            int y = i / sizeX;
            engine.addConnect(new ConnectPE2R(engine, routers[x][y], processingElements[i]));
        }
    }

    private void createPEs() {
        processingElements = new PE[sizeX * sizeY];
        for (int peIndex = 0; peIndex < sizeX * sizeY; peIndex++) {
            processingElements[peIndex] = new PE(engine, readGraphs.getGraphCount());
        }
    }

    private void mapTasksToPEs() {
        if (taskMapping.equalsIgnoreCase("linear")) calculateLinearTaskMapping(); else if (taskMapping.equalsIgnoreCase("diagonal")) calculateDiagonalTaskMapping(); else if (taskMapping.equalsIgnoreCase("random")) calculateRandomTaskMapping(); else {
            System.err.println("Invalid Task Mapping Alias: " + taskMapping);
            System.exit(0);
        }
    }

    private void calculateDiagonalTaskMapping() {
        mappedTasksOnNodesByGraph = new int[graphs.length][];
        int nodeID;
        int startOffsetX = sizeX - 1;
        int stopOffsetX = sizeX - 1;
        int startOffsetY = 0;
        int stopOffsetY = 0;
        int offsetX = startOffsetX;
        int offsetY = startOffsetY;
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            int graphID = graphs[graphIndex].getGraphID();
            mappedTasksOnNodesByGraph[graphID] = new int[graphs[graphIndex].getTaskCount()];
            for (int taskIndex = 0; taskIndex < graphs[graphIndex].getTaskCount(); taskIndex++) {
                if (offsetY == sizeY && offsetX == 0) {
                    startOffsetX = sizeX - 1;
                    stopOffsetX = sizeX - 1;
                    startOffsetY = 0;
                    stopOffsetY = 0;
                    offsetX = startOffsetX;
                    offsetY = startOffsetY;
                }
                int taskID = ((Task) graphs[graphIndex].getTask(taskIndex)).getTaskID();
                nodeID = (offsetX + offsetY * sizeX) % (sizeX * sizeY);
                mappedTasksOnNodesByGraph[graphID][taskID] = nodeID;
                processingElements[nodeID].addTask(graphs[graphIndex].getTask(taskIndex), graphID);
                int currentOffsetX = offsetX;
                int currentOffsetY = offsetY;
                offsetX++;
                offsetY++;
                if (offsetX > stopOffsetX) {
                    startOffsetX--;
                    if (startOffsetX >= 0) offsetX = startOffsetX; else {
                        offsetX = 0;
                        stopOffsetX--;
                    }
                }
                if (offsetY > stopOffsetY) {
                    if (stopOffsetY < sizeY - 1) {
                        stopOffsetY++;
                    } else if (currentOffsetX <= currentOffsetY) startOffsetY++;
                    offsetY = startOffsetY;
                }
            }
        }
    }

    private void calculateTrippyDiagonalTaskMapping() {
        mappedTasksOnNodesByGraph = new int[graphs.length][];
        int offsetX = 0, offsetY = 0;
        int nodeID;
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            int graphID = graphs[graphIndex].getGraphID();
            mappedTasksOnNodesByGraph[graphID] = new int[graphs[graphIndex].getTaskCount()];
            for (int taskIndex = 0; taskIndex < graphs[graphIndex].getTaskCount(); taskIndex++) {
                int taskID = ((Task) graphs[graphIndex].getTask(taskIndex)).getTaskID();
                nodeID = (((offsetX % sizeX) + ((offsetY % sizeY) * sizeX)) + offsetY / sizeY + offsetX / sizeX) % (sizeX * sizeY);
                mappedTasksOnNodesByGraph[graphID][taskID] = nodeID;
                processingElements[nodeID].addTask(graphs[graphIndex].getTask(taskIndex), graphID);
                offsetX++;
                offsetY++;
            }
        }
    }

    private void calculateRandomTaskMapping() {
        mappedTasksOnNodesByGraph = new int[graphs.length][];
        Random rand_gen = new Random(seed);
        int node_mask[] = new int[sizeX * sizeY];
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            int nodeID = 0;
            int graphID = graphs[graphIndex].getGraphID();
            mappedTasksOnNodesByGraph[graphID] = new int[graphs[graphIndex].getTaskCount()];
            for (int i = 0; i < (sizeX * sizeY); i++) node_mask[i] = 0;
            for (int taskIndex = 0; taskIndex < graphs[graphIndex].getTaskCount(); taskIndex++) {
                int taskID = ((Task) graphs[graphIndex].getTask(taskIndex)).getTaskID();
                boolean collision, det_unused;
                do {
                    collision = false;
                    nodeID = rand_gen.nextInt(sizeX * sizeY);
                    for (int i = 0; i < taskIndex; i++) if (mappedTasksOnNodesByGraph[graphID][i] == nodeID) collision = true;
                    if (collision) {
                        det_unused = false;
                        for (int i = 0; i < sizeX * sizeY; i++) if (node_mask[i] == 0) det_unused = true;
                        if ((!det_unused) && (node_mask[nodeID] == 1)) collision = false;
                    }
                } while (collision);
                mappedTasksOnNodesByGraph[graphID][taskID] = nodeID;
                node_mask[nodeID]++;
                System.out.println("Mapping GraphID=" + graphs[graphIndex].getGraphID() + " TaskID=" + graphs[graphIndex].getTask(taskIndex).getTaskID() + " to NodeID=" + nodeID);
                processingElements[nodeID].addTask(graphs[graphIndex].getTask(taskIndex), graphID);
            }
        }
    }

    private void calculateLinearTaskMapping() {
        mappedTasksOnNodesByGraph = new int[graphs.length][];
        int nodeIDOffset = 0;
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            int nodeID = 0;
            int graphID = graphs[graphIndex].getGraphID();
            mappedTasksOnNodesByGraph[graphID] = new int[graphs[graphIndex].getTaskCount()];
            for (int taskIndex = 0; taskIndex < graphs[graphIndex].getTaskCount(); taskIndex++) {
                int taskID = ((Task) graphs[graphIndex].getTask(taskIndex)).getTaskID();
                mappedTasksOnNodesByGraph[graphID][taskID] = (nodeID + nodeIDOffset) % (sizeX * sizeY);
                processingElements[(nodeID + nodeIDOffset) % (sizeX * sizeY)].addTask(graphs[graphIndex].getTask(taskIndex), graphID);
                nodeID++;
            }
            nodeIDOffset++;
        }
    }

    private void associatePEs() {
        for (int i = 0; i < processingElements.length; i++) {
            if (!processingElements[i].associate()) {
                System.err.println("Error: NoCsim: processingElement " + i + " failed to associate");
                System.exit(-1);
            }
        }
    }

    public void buildrouterpath() {
        int packetgraph = 0;
        int packetallgraph = 0;
        int packetgraphmax = 0;
        int taskmax = 0;
        deletednodelist = new int[sizeX * sizeY + 1];
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            packetgraph = 0;
            generate.graph.GraphInfo graphInfo = readGraphs.getGraphInfo(graphIndex);
            int graphID = graphInfo.getID();
            if (graphInfo.getTaskCount() >= taskmax) {
                taskmax = graphInfo.getTaskCount();
            }
            for (int taskIndex = 0; taskIndex < graphInfo.getTaskCount(); taskIndex++) {
                generate.graph.TaskInfo taskInfo = graphInfo.getTaskInfo(taskIndex);
                packetgraph = packetgraph + taskInfo.getPacketCount();
            }
            packetallgraph = packetallgraph + packetgraph;
            if (packetgraph > packetgraphmax) {
                packetgraphmax = packetgraph;
            }
        }
        packtable = new Packetsizetable[graphs.length][packetgraphmax];
        roottask = new int[taskmax];
        leafnode = new int[taskmax];
        leafpacket = new int[packetgraphmax];
        noninterferepacket = new int[packetgraphmax][packetgraphmax];
        noninterferepacketnum = new int[packetgraphmax];
        rootmark = new int[packetgraphmax];
        finalnoninterferepacket = new int[packetgraphmax][packetgraphmax];
        protected int finalnoninterferenum[];
        protected int nonintersingle[][];
        protected int nonintersinglenum[][];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                this.routers[i][j].routetable = new Routetable[packetallgraph];
            }
        }
        for (int graphIndex = 0; graphIndex < graphs.length; graphIndex++) {
            packtable = new Packetsizetable[graphs.length][packetgraphmax];
            sortbypacketsize(graphIndex, packetgraphmax);
            buildrouterpathbypacketsize(graphIndex);
        }
    }

    private void sortbypacketsize(int graphIndex, int packetgraphmax) {
        int packetindexcount = 0;
        Packetsizetable key;
        int leafnodenum = 0;
        int leafpacketnum = 0;
        int roottasknum = 0;
        int finalnoninterferegroup = 0;
        int finalnoninterfereindex = 0;
        packetnum = 0;
        generate.graph.GraphInfo graphInfo = readGraphs.getGraphInfo(graphIndex);
        int graphID = graphInfo.getID();
        for (int taskIndex = 0; taskIndex < graphInfo.getTaskCount(); taskIndex++) {
            generate.graph.TaskInfo taskInfo = graphInfo.getTaskInfo(taskIndex);
            int taskID = taskInfo.getID();
            if (taskInfo.getWaitCount() <= 0) {
                roottask[roottasknum] = taskID;
                roottasknum++;
                for (int packetindex = 0; packetindex < taskInfo.getPacketCount(); packetindex++) {
                    generate.graph.PacketInfo packetInfo = taskInfo.getPacket(packetindex);
                    int packetID = packetInfo.getID();
                    int destinationtaskID = packetInfo.getDestTaskID();
                    int packetsize = packetInfo.getSize();
                    packtable[graphID][packetindexcount] = new Packetsizetable(packetID, taskID, destinationtaskID, packetsize);
                    packetnum++;
                    packetindexcount++;
                }
            } else if (taskInfo.getWaitCount() > 0) {
                if (taskInfo.getPacketCount() > 0) {
                    for (int packetindex = 0; packetindex < taskInfo.getPacketCount(); packetindex++) {
                        generate.graph.PacketInfo packetInfo = taskInfo.getPacket(packetindex);
                        int packetID = packetInfo.getID();
                        int destinationtaskID = packetInfo.getDestTaskID();
                        int packetsize = packetInfo.getSize();
                        packtable[graphID][packetindexcount] = new Packetsizetable(packetID, taskID, destinationtaskID, packetsize);
                        packetnum++;
                        packetindexcount++;
                    }
                } else {
                    leafnode[leafnodenum] = taskInfo.getID();
                    leafnodenum++;
                }
            }
        }
        for (int packetindex = 1; packetindex < packetnum; packetindex++) {
            key = packtable[graphID][packetindex];
            int i = packetindex - 1;
            while (i > 0 && packtable[graphID][i].getpacketsize() < key.getpacketsize()) {
                packtable[graphID][i + 1] = packtable[graphID][i];
                i--;
            }
            packtable[graphID][i + 1] = key;
        }
        for (int packetindex = 0; packetindex < packetnum; packetindex++) {
            for (int leafindex = 0; leafindex < leafnodenum; leafindex++) {
                if (leafnode[leafindex] == packtable[graphID][packetindex].destination) {
                    leafpacket[leafpacketnum] = packetindex;
                    leafpacketnum++;
                    break;
                }
            }
        }
        nonpacketgroup = 0;
        nonpacketindex = 0;
        for (int leafpacketindex = 0; leafpacketindex < leafpacketnum; leafpacketindex++) {
            noninterferepacket[nonpacketgroup][nonpacketindex] = leafpacket[leafpacketindex];
            findnoninterferepacket(graphID, leafpacket[leafpacketindex], roottasknum);
            noninterferepacketnum[nonpacketgroup] = nonpacketindex;
            nonpacketgroup++;
            nonpacketindex++;
        }
        finalnoninterferegroup = 0;
        finalnoninterfereindex = 0;
        for (int i = 0; i < nonpacketgroup; i++) {
            int rootmarkindex = 0;
            for (int j = 0; j < noninterferepacketnum[i]; j++) {
                if (isroot(packtable[graphID][noninterferepacket[i][j]].source, roottasknum)) {
                    rootmark[rootmarkindex] = j;
                    rootmarkindex++;
                }
            }
            for (int k = 0; k < rootmarkindex; k++) {
                for (int m = 0; m <= rootmark[k]; m++) {
                    if (k == 0) {
                        finalnoninterferepacket[finalnoninterferegroup][finalnoninterfereindex] = noninterferepacket[i][m];
                        finalnoninterfereindex++;
                    } else {
                        if (isroot(packtable[graphID][noninterferepacket[i][rootmark[k - 1] + 1]].source, roottasknum)) {
                            int startpoint = rootmark[k - 1] + 1;
                            for (int n = startpoint; n <= rootmark[k]; n++) {
                                finalnoninterferepacket[finalnoninterferegroup][finalnoninterfereindex] = noninterferepacket[i][n];
                                finalnoninterfereindex++;
                            }
                            break;
                        } else {
                            if (fatherandson(graphID, noninterferepacket[i][m], noninterferepacket[i][rootmark[k - 1] + 1])) {
                                int breakpoint1 = m;
                                int breakpoint2 = rootmark[k - 1] + 1;
                                for (int n = 0; n <= rootmark[k]; n++) {
                                    if (n <= breakpoint1 || n >= breakpoint2) {
                                        finalnoninterferepacket[finalnoninterferegroup][finalnoninterfereindex] = noninterferepacket[i][n];
                                        finalnoninterfereindex++;
                                    }
                                }
                                break;
                            }
                        }
                    }
                }
                finalnoninterferenum[finalnoninterferegroup] = finalnoninterfereindex;
                finalnoninterferegroup++;
                finalnoninterfereindex = 0;
            }
        }
        for (int k = 0; k < finalnoninterferegroup; k++) {
            for (int n = 0; n < finalnoninterferenum[finalnoninterferegroup]; n++) {
                for (int m = n + 1; m < finalnoninterferenum[finalnoninterferegroup]; m++) {
                    addinto(graphID, finalnoninterferepacket[k][n], finalnoninterferepacket[k][m]);
                    addinto(graphID, finalnoninterferepacket[k][m], finalnoninterferepacket[k][n]);
                }
            }
        }
        for (int packetindex = 0; packetindex < packetnum; packetindex++) {
            System.out.println("packet source is " + packtable[graphID][packetindex].source + " packet id is " + packtable[graphID][packetindex].packetid + " packtable size is " + packtable[graphID][packetindex].getpacketsize());
        }
    }

    private void findnoninterferepacket(int graphID, int leafpacketindex, int roottasknum) {
        int srctask = packtable[graphID][leafpacketindex].source;
        int desttask = packtable[graphID][leafpacketindex].destination;
        while (!isroot(srctask, roottasknum)) {
            desttask = srctask;
            for (int i = 0; i < packetnum; i++) {
                if (packtable[graphID][i].destination == desttask) {
                    noninterferepacket[nonpacketgroup][nonpacketindex] = i;
                    nonpacketindex++;
                    findnoninterferepacket(graphID, i, roottasknum);
                }
            }
        }
    }

    private boolean isroot(int srctask, int roottasknum) {
        for (int i = 0; i < roottasknum; i++) {
            if (srctask == roottask[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean fatherandson(int graphID, int a, int b) {
        if (packtable[graphID][a].source == packtable[graphID][b].destination) {
            return true;
        }
        return false;
    }

    private void addinto(int graphID, int a, int b) {
        for (int i = 0; i < nonintersinglenum[graphID][a]; i++) {
            if (nonintersingle[graphID][a] == b) return;
        }
        nonintersingle[graphID][nonintersinglenum[graphID][a]] = b;
        nonintersinglenum[graphID][a]++;
    }

    private void buildrouterpathbypacketsize(int graphIndex) {
        int plusXpath[][] = new int[sizeX - 1][sizeY];
        int minusXpath[][] = new int[sizeX - 1][sizeY];
        int plusYpath[][] = new int[sizeX][sizeY - 1];
        int minusYpath[][] = new int[sizeX][sizeY - 1];
        int i, j, k;
        int max = Integer.MAX_VALUE;
        node = new Node[graphs.length][sizeX * sizeY];
        nodequeue = new Node[sizeX * sizeY + 1];
        Node currentnode;
        generate.graph.GraphInfo graphInfo = readGraphs.getGraphInfo(graphIndex);
        int graphID = graphInfo.getID();
        for (i = 0; i < sizeX - 1; i++) {
            for (j = 0; j < sizeY; j++) {
                plusXpath[i][j] = 1;
                minusXpath[i][j] = 1;
            }
        }
        for (i = 0; i < sizeX; i++) {
            for (j = 0; j < sizeY - 1; j++) {
                plusYpath[i][j] = 1;
                minusYpath[i][j] = 1;
            }
        }
        for (i = 0; i < sizeX * sizeY; i++) {
            node[graphID][i] = new Node(i, max);
            node[graphID][i].getXY(sizeX);
            node[graphID][i].geteastnode(sizeX);
            node[graphID][i].getwestnode(sizeX);
            node[graphID][i].getnorthnode(sizeX);
            node[graphID][i].getsouthnode(sizeX, sizeY);
        }
        for (int m = 0; m < graphInfo.getTaskCount(); m++) {
        }
        for (i = 0; i < packetnum; i++) {
            int packetID = packtable[graphID][i].packetid;
            int packsize = packtable[graphID][i].packetsize;
            int srctaskID = packtable[graphID][i].source;
            int desttaskID = packtable[graphID][i].destination;
            int srcnodeID = mappedTasksOnNodesByGraph[graphID][srctaskID];
            int destnodeID = mappedTasksOnNodesByGraph[graphID][desttaskID];
            if (srcnodeID == destnodeID) {
                System.out.println("i is " + i);
                continue;
            }
            queuetail = 0;
            queuehead = 0;
            deletednodenum = 0;
            for (k = 0; k < sizeX * sizeY; k++) {
            }
            for (k = 0; k < sizeX * sizeY; k++) {
                nodequeue[k] = null;
                deletednodelist[k] = -1;
            }
            nodequeue[k] = null;
            deletednodelist[k] = -1;
            for (k = 0; k < sizeX * sizeY; k++) {
                node[graphID][k].weightedvalue = max;
                node[graphID][k].previousnode = -1;
            }
            node[graphID][srcnodeID].weightedvalue = 0;
            if (node[graphID][srcnodeID].eastnodenum != -1) {
                node[graphID][node[graphID][srcnodeID].eastnodenum].weightedvalue = node[graphID][srcnodeID].weightedvalue + plusXpath[node[graphID][srcnodeID].nodeX][node[graphID][srcnodeID].nodeY];
                node[graphID][node[graphID][srcnodeID].eastnodenum].previousnode = srcnodeID;
                node[graphID][node[graphID][srcnodeID].eastnodenum].predirection = "east";
                enqueue(node[graphID][node[graphID][srcnodeID].eastnodenum]);
            }
            if (node[graphID][srcnodeID].westnodenum != -1) {
                node[graphID][node[graphID][srcnodeID].westnodenum].weightedvalue = node[graphID][srcnodeID].weightedvalue + minusXpath[node[graphID][srcnodeID].nodeX - 1][node[graphID][srcnodeID].nodeY];
                node[graphID][node[graphID][srcnodeID].westnodenum].previousnode = srcnodeID;
                node[graphID][node[graphID][srcnodeID].westnodenum].predirection = "west";
                enqueue(node[graphID][node[graphID][srcnodeID].westnodenum]);
            }
            if (node[graphID][srcnodeID].northnodenum != -1) {
                node[graphID][node[graphID][srcnodeID].northnodenum].weightedvalue = node[graphID][srcnodeID].weightedvalue + minusYpath[node[graphID][srcnodeID].nodeX][node[graphID][srcnodeID].nodeY - 1];
                node[graphID][node[graphID][srcnodeID].northnodenum].previousnode = srcnodeID;
                node[graphID][node[graphID][srcnodeID].northnodenum].predirection = "north";
                enqueue(node[graphID][node[graphID][srcnodeID].northnodenum]);
            }
            if (node[graphID][srcnodeID].southnodenum != -1) {
                node[graphID][node[graphID][srcnodeID].southnodenum].weightedvalue = node[graphID][srcnodeID].weightedvalue + plusYpath[node[graphID][srcnodeID].nodeX][node[graphID][srcnodeID].nodeY];
                node[graphID][node[graphID][srcnodeID].southnodenum].previousnode = srcnodeID;
                node[graphID][node[graphID][srcnodeID].southnodenum].predirection = "south";
                enqueue(node[graphID][node[graphID][srcnodeID].southnodenum]);
            }
            System.out.println("i is " + i);
            deletednodelist[deletednodenum] = srcnodeID;
            deletednodenum++;
            while ((currentnode = dequeue()).nodenum != destnodeID) {
                if (currentnode.nodeX % 2 == 0) {
                    System.out.println("even");
                    if (currentnode.predirection.equals("east")) {
                        System.out.println("into east");
                        if (currentnode.eastnodenum != -1 && !deletednode(currentnode.eastnodenum)) {
                            if (currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.eastnodenum].weightedvalue) {
                                node[graphID][currentnode.eastnodenum].weightedvalue = currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.eastnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.eastnodenum].predirection = "east";
                                enqueue(node[graphID][currentnode.eastnodenum]);
                            }
                        }
                        if (currentnode.westnodenum != -1 && !deletednode(currentnode.westnodenum)) {
                            if (currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY] < node[graphID][currentnode.westnodenum].weightedvalue) {
                                node[graphID][currentnode.westnodenum].weightedvalue = currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY];
                                node[graphID][currentnode.westnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.westnodenum].predirection = "west";
                                enqueue(node[graphID][currentnode.westnodenum]);
                            }
                        }
                    } else {
                        if (currentnode.eastnodenum != -1 && !deletednode(currentnode.eastnodenum)) {
                            if (currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.eastnodenum].weightedvalue) {
                                node[graphID][currentnode.eastnodenum].weightedvalue = currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.eastnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.eastnodenum].predirection = "east";
                                enqueue(node[graphID][currentnode.eastnodenum]);
                            }
                        }
                        if (currentnode.westnodenum != -1 && !deletednode(currentnode.westnodenum)) {
                            if (currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY] < node[graphID][currentnode.westnodenum].weightedvalue) {
                                node[graphID][currentnode.westnodenum].weightedvalue = currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY];
                                node[graphID][currentnode.westnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.westnodenum].predirection = "west";
                                enqueue(node[graphID][currentnode.westnodenum]);
                            }
                        }
                        if (currentnode.northnodenum != -1 && !deletednode(currentnode.northnodenum)) {
                            if (currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1] < node[graphID][currentnode.northnodenum].weightedvalue) {
                                node[graphID][currentnode.northnodenum].weightedvalue = currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1];
                                node[graphID][currentnode.northnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.northnodenum].predirection = "north";
                                enqueue(node[graphID][currentnode.northnodenum]);
                            }
                        }
                        if (currentnode.southnodenum != -1 && !deletednode(currentnode.southnodenum)) {
                            if (currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.southnodenum].weightedvalue) {
                                node[graphID][currentnode.southnodenum].weightedvalue = currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.southnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.southnodenum].predirection = "south";
                                enqueue(node[graphID][currentnode.southnodenum]);
                            }
                        }
                    }
                } else {
                    System.out.println("odd");
                    if (currentnode.predirection.equals("north") || currentnode.predirection.equals("south")) {
                        if (currentnode.eastnodenum != -1 && !deletednode(currentnode.eastnodenum)) {
                            if (currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.eastnodenum].weightedvalue) {
                                node[graphID][currentnode.eastnodenum].weightedvalue = currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.eastnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.eastnodenum].predirection = "east";
                                enqueue(node[graphID][currentnode.eastnodenum]);
                            }
                        }
                        if (currentnode.northnodenum != -1 && !deletednode(currentnode.northnodenum)) {
                            if (currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1] < node[graphID][currentnode.northnodenum].weightedvalue) {
                                node[graphID][currentnode.northnodenum].weightedvalue = currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1];
                                node[graphID][currentnode.northnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.northnodenum].predirection = "north";
                                enqueue(node[graphID][currentnode.northnodenum]);
                            }
                        }
                        if (currentnode.southnodenum != -1 && !deletednode(currentnode.southnodenum)) {
                            if (currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.southnodenum].weightedvalue) {
                                node[graphID][currentnode.southnodenum].weightedvalue = currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.southnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.southnodenum].predirection = "south";
                                enqueue(node[graphID][currentnode.southnodenum]);
                            }
                        }
                    } else {
                        if (currentnode.eastnodenum != -1 && !deletednode(currentnode.eastnodenum)) {
                            if (currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.eastnodenum].weightedvalue) {
                                node[graphID][currentnode.eastnodenum].weightedvalue = currentnode.weightedvalue + plusXpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.eastnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.eastnodenum].predirection = "east";
                                enqueue(node[graphID][currentnode.eastnodenum]);
                            }
                        }
                        if (currentnode.westnodenum != -1 && !deletednode(currentnode.westnodenum)) {
                            if (currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY] < node[graphID][currentnode.westnodenum].weightedvalue) {
                                node[graphID][currentnode.westnodenum].weightedvalue = currentnode.weightedvalue + minusXpath[currentnode.nodeX - 1][currentnode.nodeY];
                                node[graphID][currentnode.westnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.westnodenum].predirection = "west";
                                enqueue(node[graphID][currentnode.westnodenum]);
                            }
                        }
                        if (currentnode.northnodenum != -1 && !deletednode(currentnode.northnodenum)) {
                            if (currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1] < node[graphID][currentnode.northnodenum].weightedvalue) {
                                node[graphID][currentnode.northnodenum].weightedvalue = currentnode.weightedvalue + minusYpath[currentnode.nodeX][currentnode.nodeY - 1];
                                node[graphID][currentnode.northnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.northnodenum].predirection = "north";
                                enqueue(node[graphID][currentnode.northnodenum]);
                            }
                        }
                        if (currentnode.southnodenum != -1 && !deletednode(currentnode.southnodenum)) {
                            if (currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY] < node[graphID][currentnode.southnodenum].weightedvalue) {
                                node[graphID][currentnode.southnodenum].weightedvalue = currentnode.weightedvalue + plusYpath[currentnode.nodeX][currentnode.nodeY];
                                node[graphID][currentnode.southnodenum].previousnode = currentnode.nodenum;
                                node[graphID][currentnode.southnodenum].predirection = "south";
                                enqueue(node[graphID][currentnode.southnodenum]);
                            }
                        }
                    }
                }
            }
            int nodebackbyone = currentnode.previousnode;
            while (nodebackbyone != srcnodeID) {
                int routetableindex = this.routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routenum;
                this.routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routetable[routetableindex] = new Routetable(graphID, packetID, srctaskID, desttaskID, currentnode.predirection);
                this.routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routenum++;
                if (currentnode.predirection.equals("east")) {
                    plusXpath[currentnode.nodeX - 1][currentnode.nodeY] = +packsize;
                }
                if (currentnode.predirection.equals("west")) {
                    minusXpath[currentnode.nodeX][currentnode.nodeY] = +packsize;
                }
                if (currentnode.predirection.equals("north")) {
                    minusYpath[currentnode.nodeX][currentnode.nodeY] = +packsize;
                }
                if (currentnode.predirection.equals("south")) {
                    plusYpath[currentnode.nodeX][currentnode.nodeY - 1] = +packsize;
                }
                currentnode = node[graphID][currentnode.previousnode];
                nodebackbyone = currentnode.previousnode;
            }
            int routetableindex = routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routenum;
            routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routetable[routetableindex] = new Routetable(graphID, packetID, srctaskID, desttaskID, currentnode.predirection);
            routers[nodebackbyone % sizeX][nodebackbyone / sizeX].routenum++;
            if (currentnode.predirection.equals("east")) {
                plusXpath[currentnode.nodeX - 1][currentnode.nodeY] = +packsize;
            }
            if (currentnode.predirection.equals("west")) {
                minusXpath[currentnode.nodeX][currentnode.nodeY] = +packsize;
            }
            if (currentnode.predirection.equals("north")) {
                minusYpath[currentnode.nodeX][currentnode.nodeY] = +packsize;
            }
            if (currentnode.predirection.equals("south")) {
                plusYpath[currentnode.nodeX][currentnode.nodeY - 1] = +packsize;
            }
        }
        print_router();
    }

    private void enqueue(Node insertnode) {
        System.out.println("into the enqueue");
        System.out.println("insert node id is " + insertnode.nodenum);
        if (queuetail == 0) {
            nodequeue[queuetail] = insertnode;
            queuetail++;
        } else {
            if (!Nodeinthequeue(insertnode)) {
                int temptail = queuetail;
                queuetail++;
                while (insertnode.weightedvalue < nodequeue[temptail - 1].weightedvalue) {
                    nodequeue[temptail] = nodequeue[temptail - 1];
                    temptail = temptail - 1;
                    if (temptail == queuehead) break;
                }
                nodequeue[temptail] = insertnode;
            }
        }
    }

    private boolean Nodeinthequeue(Node insertnode) {
        int index = 0;
        int temp = queuehead;
        Node tempnode;
        while (temp != queuetail) {
            if (nodequeue[temp].nodenum == insertnode.nodenum) {
                System.out.println("node " + insertnode.nodenum + " already int the quueue");
                nodequeue[temp].weightedvalue = insertnode.weightedvalue;
                for (int j = index; j > 0; j--) {
                    if (nodequeue[temp].weightedvalue < nodequeue[temp - 1].weightedvalue) {
                        tempnode = nodequeue[temp];
                        nodequeue[temp] = nodequeue[temp - 1];
                        nodequeue[temp - 1] = tempnode;
                        temp--;
                    } else return true;
                }
            }
            temp++;
            index++;
        }
        return false;
    }

    private Node dequeue() {
        System.out.println("into the dequeue");
        int temphead = queuehead;
        deletednodelist[deletednodenum] = nodequeue[queuehead].nodenum;
        deletednodenum++;
        queuehead++;
        System.out.println("delete node id is " + nodequeue[temphead].nodenum);
        return (nodequeue[temphead]);
    }

    private boolean deletednode(int nodeID) {
        for (int i = 0; i < deletednodenum; i++) {
            if (nodeID == deletednodelist[i]) return true;
        }
        return false;
    }

    private void print_router() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                for (int k = 0; k < this.routers[i][j].routenum; k++) {
                    System.out.println("router table " + i + " " + j);
                    System.out.println("packetID " + this.routers[i][j].routetable[k].packetID);
                    System.out.println("srctaskID " + this.routers[i][j].routetable[k].srctaskID);
                    System.out.println("desttaskID " + this.routers[i][j].routetable[k].destaskID);
                    System.out.println("direction " + this.routers[i][j].routetable[k].direction);
                }
            }
        }
    }
}
