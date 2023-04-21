package vizz3d_data.layouts;

import grail.interfaces.DirectedGraphInterface;
import grail.interfaces.DirectedNodeInterface;
import grail.interfaces.GraphInterface;
import grail.interfaces.NodeInterface;
import grail.interfaces.UndirectedGraphInterface;
import grail.interfaces.UndirectedNodeInterface;
import grail.iterators.NodeIterator;
import grail.properties.GraphProperties;
import java.util.Hashtable;
import visgraph.VisualProperties;
import visgraph.visualgraph.interfaces.VisualGraphInterface;
import visgraph.visualgraph.interfaces.VisualNodeInterface;
import visgraph.visualgraph.iterators.VisualNodeIterator;
import vizz3d.common.abstractClasses.LayoutEngine;
import vizz3d.common.interfaces.AnimateInterface;
import vizz3d.common.interfaces.LayoutInterface;
import vizz3d.common.interfaces.Vector3d;
import vizz3d.util.LayoutDebug;

/**
 * Gem3D Layout algorithm
 * 
 * @author Welf Löwe
 */
public class Gem3D extends LayoutEngine implements LayoutInterface {

    private GraphInterface dataGraph;

    int animations = 10;

    int iterations = 1000;

    double desiredEdgeDistance = 3;

    double tGlobal = 0;

    double tMinGlobal = desiredEdgeDistance / 3;

    double tInitLocal = 5 * desiredEdgeDistance;

    double randBound = desiredEdgeDistance / 2;

    double gravitation = 0.5;

    double sigmaA = 0.1;

    double sigmaO = 0.1;

    double sigmaR = 0.1;

    Vector3d gravityCenter = new Vector3d(0, 0, 0);

    Vector3d constGravityCenter = new Vector3d(0, 0, 0);

    Hashtable temperatures = new Hashtable();

    Hashtable directions = new Hashtable();

    Hashtable impulses = new Hashtable();

    private String nameLayout = "Gem3D";

    /**
     * Creates a new instance of Leveller
     */
    public Gem3D() {
        super("Gem3D", "Arne's Gem 3D Algorithm");
        defaultZoomFactor = 150;
    }

    public void reLayout() {
        retrieve();
        doLayout();
    }

    public void retrieve() {
        getArguments(nameLayout);
        iterations = Integer.valueOf(getArgument(nameLayout, "Max iterations")).intValue();
        animations = Integer.valueOf(getArgument(nameLayout, "Animation")).intValue();
        desiredEdgeDistance = Double.valueOf(getArgument(nameLayout, "Desired Edge Distance")).intValue();
        gravitation = Double.valueOf(getArgument(nameLayout, "Gravitation")).doubleValue();
        registerZoomFactor(defaultZoomFactor);
    }

    void localInit() {
        tMinGlobal = desiredEdgeDistance;
        tInitLocal = 2 * desiredEdgeDistance;
        randBound = desiredEdgeDistance / 2;
        VisualNodeIterator ni = currentGraph.sceneGraphNodes();
        while (ni.hasNext()) {
            VisualNodeInterface node = ni.next();
            if (!(node.isVisible())) continue;
            temperatures.put(node, new Double(tInitLocal));
            directions.put(node, new Double(0));
            impulses.put(node, new Vector3d(0, 0, 0));
        }
        updateGravityCenterAndGlobalTemp(constGravityCenter, currentGraph);
    }

    public void init(VisualGraphInterface graph, AnimateInterface ani) {
        dataGraph = vizzGUI.getPlugIn().getDataGraph();
        anim = ani;
        currentGraph = graph;
        init();
        if (!graphsInit.contains(graph)) {
            iterations = graph.getVisibleNodeSize() * 100;
            setArgument(nameLayout, "Max iterations", iterations, 0, 2 * iterations, true);
            setArgument(nameLayout, "Animation", animations, 0, 100, true);
            setArgument(nameLayout, "Desired Edge Distance", desiredEdgeDistance, true);
            setArgument(nameLayout, "Gravitation", gravitation, true);
            setArgument(nameLayout, "Description", "Gem3D Algorithm by Arne Frick. \n" + "Implementation by Welf Löwe, Thomas Panas\nVaxjo University. 25Jul2004.");
            graphsInit.add(graph);
        } else retrieve();
        showDialog("Gem3D", this);
    }

    /**
     * We need to randomize the coordinates in the beginning. Dont leave them in
     * the origin for this algorithm, tps
     */
    private void init() {
        VisualNodeIterator ni = currentGraph.sceneGraphNodes();
        int random = currentGraph.nodesSize() * 2;
        while (ni.hasNext()) {
            VisualNodeInterface node = ni.next();
            if (!(node.isVisible())) continue;
            double x = (float) (Math.random() * random);
            double y = (float) (Math.random() * random);
            double z = (float) (Math.random() * random);
            node.setFinalPosition((float) x, (float) y, (float) z);
        }
    }

    public synchronized boolean doLayout() {
        if (stopButton != null) stopButton.setVisible(this);
        boolean success = super.runThread();
        System.err.println(" running Gem3D success: " + success);
        return success;
    }

    @Override
    public synchronized void run() {
        while (true) {
            localInit();
            gem3d(currentGraph);
            sendToOrigin();
            anim.animate(animations);
            if (stopButton != null) stopButton.setVisible(false);
            super.pauseThread();
        }
    }

    private synchronized void gem3d(VisualGraphInterface graph) {
        int round = 1;
        double min = tMinGlobal * getVisibleNodes();
        LayoutDebug.printlnErrMessage("Start status: tGlobal=" + tGlobal + " Min=" + min + " round=" + round);
        stopButton.setMaximum(iterations);
        while ((tGlobal > min) && (round < iterations)) {
            stopButton.increase();
            if (stopMe) {
                stopMe = false;
                return;
            }
            if ((round % ((int) (iterations / animations))) == 0) {
                sendToOrigin();
                anim.animate(animations);
            }
            int i = (int) (Math.random() * (getVisibleNodes() + 1));
            int j = -1;
            VisualNodeInterface node = null;
            VisualNodeIterator ni = currentGraph.sceneGraphNodes();
            while (ni.hasNext() && (j != i)) {
                node = ni.next();
                if (!(node.isVisible())) continue;
                j++;
            }
            Vector3d imp = calculateImpulse(node, graph);
            updatePositionAndTemperature(node, imp, getVisibleNodes());
            updateGravityCenterAndGlobalTemp(gravityCenter, graph);
            round++;
        }
    }

    public void sendToOrigin() {
        VisualNodeIterator ni = currentGraph.sceneGraphNodes();
        int first = 0;
        double dx = 0;
        double dy = 0;
        double dz = 0;
        while (ni.hasNext()) {
            VisualNodeInterface node = ni.next();
            if (!(node.isVisible())) continue;
            if (first == 0) {
                first = 1;
                dx = node.getFinalPosition().x;
                dy = node.getFinalPosition().y;
                dz = node.getFinalPosition().z;
            }
            double finalX = node.getFinalPosition().x;
            double finalY = node.getFinalPosition().y;
            double finalZ = node.getFinalPosition().z;
            finalX = finalX - dx;
            finalY = finalY - dy;
            finalZ = finalZ - dz;
            node.setFinalPosition((float) finalX, (float) finalY, (float) finalZ);
        }
    }

    private void updateGravityCenterAndGlobalTemp(Vector3d gravityCenterP, VisualGraphInterface graph) {
        int size = getVisibleNodes();
        tGlobal = 0;
        VisualNodeIterator ni = currentGraph.sceneGraphNodes();
        while (ni.hasNext()) {
            VisualNodeInterface node = ni.next();
            if (!(node.isVisible())) continue;
            double dX = node.getFinalPosition().x;
            double dY = node.getFinalPosition().y;
            double dZ = node.getFinalPosition().z;
            gravityCenterP.x += dX;
            gravityCenterP.y += dY;
            gravityCenterP.z += dZ;
            tGlobal += ((Double) temperatures.get(node)).doubleValue();
        }
        gravityCenterP.x /= size;
        gravityCenterP.y /= size;
        gravityCenterP.z /= size;
    }

    private Vector3d calculateImpulse(VisualNodeInterface node, VisualGraphInterface graph) {
        double dX = node.getFinalPosition().x;
        double dY = node.getFinalPosition().y;
        double dZ = node.getFinalPosition().z;
        double sX = 0;
        double sY = 0;
        double sZ = 0;
        double dd = desiredEdgeDistance * desiredEdgeDistance;
        double deltaX;
        double deltaY;
        double deltaZ;
        double delta2;
        double radius = 1.0;
        if (node.getDataNode().getProperty(VisualProperties.SCENE_NODE_RADIUS) != null) radius = ((Double) node.getDataNode().getProperty(VisualProperties.SCENE_NODE_RADIUS)).doubleValue();
        double calls = 1.0;
        if (node.getDataNode().getProperty(GraphProperties.EDGE_RADIUS) != null) calls = ((Double) node.getDataNode().getProperty(GraphProperties.EDGE_RADIUS)).doubleValue();
        VisualNodeIterator ni = currentGraph.sceneGraphNodes();
        while (ni.hasNext()) {
            VisualNodeInterface tmp = ni.next();
            if (!(node.isVisible())) continue;
            if (tmp != node) {
                double tmp_dX = tmp.getFinalPosition().x;
                double tmp_dY = tmp.getFinalPosition().y;
                double tmp_dZ = tmp.getFinalPosition().z;
                deltaX = dX - tmp_dX;
                deltaY = dY - tmp_dY;
                deltaZ = dZ - tmp_dZ;
                delta2 = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
                sX += ((deltaX * dd) / (delta2) / (radius * calls));
                sY += ((deltaY * dd) / (delta2) / (radius * calls));
                sZ += ((deltaZ * dd) / (delta2) / (radius * calls));
            }
        }
        int deg = 0;
        int indeg = 0;
        int outdeg = 0;
        if (dataGraph instanceof DirectedGraphInterface) {
            NodeInterface dataNode = graph.getNode(node);
            indeg = ((DirectedGraphInterface) dataGraph).inDegree((DirectedNodeInterface) dataNode);
            outdeg = ((DirectedGraphInterface) dataGraph).outDegree((DirectedNodeInterface) dataNode);
            deg = indeg + outdeg;
            NodeIterator ni2 = ((DirectedGraphInterface) dataGraph).getPredecessors((DirectedNodeInterface) dataNode);
            while (ni2.hasNext()) {
                ni2.next();
                NodeInterface tmp2 = ni2.getNode();
                VisualNodeInterface tmp = graph.getNode(tmp2);
                if (!(node.isVisible())) continue;
                if (tmp != node) {
                    double tmp_dX = tmp.getFinalPosition().x;
                    double tmp_dY = tmp.getFinalPosition().y;
                    double tmp_dZ = tmp.getFinalPosition().z;
                    deltaX = dX - tmp_dX;
                    deltaY = dY - tmp_dY;
                    deltaZ = dZ - tmp_dZ;
                    delta2 = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
                    sX += ((-deltaX * delta2) / (dd) * (radius * calls));
                    sY += ((-deltaY * delta2) / (dd) * (radius * calls));
                    sZ += ((-deltaZ * delta2) / (dd) * (radius * calls));
                }
            }
            NodeIterator ni3 = ((DirectedGraphInterface) dataGraph).getSuccessors((DirectedNodeInterface) dataNode);
            while (ni3.hasNext()) {
                ni3.next();
                NodeInterface tmp3 = ni3.getNode();
                VisualNodeInterface tmp = graph.getNode(tmp3);
                if (!(tmp.isVisible())) continue;
                if (tmp != node) {
                    double tmp_dX = tmp.getFinalPosition().x;
                    double tmp_dY = tmp.getFinalPosition().y;
                    double tmp_dZ = tmp.getFinalPosition().z;
                    deltaX = dX - tmp_dX;
                    deltaY = dY - tmp_dY;
                    deltaZ = dZ - tmp_dZ;
                    delta2 = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
                    sX += ((-deltaX * delta2) / (dd) * (radius * calls));
                    sY += ((-deltaY * delta2) / (dd) * (radius * calls));
                    sZ += ((-deltaZ * delta2) / (dd) * (radius * calls));
                }
            }
        } else {
            NodeInterface dataNode = graph.getNode(node);
            deg = ((UndirectedGraphInterface) dataGraph).degree((UndirectedNodeInterface) dataNode);
            NodeIterator ni4 = ((UndirectedGraphInterface) dataGraph).neighbors((UndirectedNodeInterface) dataNode);
            while (ni4.hasNext()) {
                ni4.next();
                NodeInterface tmp4 = ni4.getNode();
                VisualNodeInterface tmp = graph.getNode(tmp4);
                if (!(tmp.isVisible())) continue;
                if (tmp != node) {
                    double tmp_dX = tmp.getFinalPosition().x;
                    double tmp_dY = tmp.getFinalPosition().y;
                    double tmp_dZ = tmp.getFinalPosition().z;
                    deltaX = dX - tmp_dX;
                    deltaY = dY - tmp_dY;
                    deltaZ = dZ - tmp_dZ;
                    delta2 = (deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ);
                    sX += ((-deltaX * delta2) / (dd) * (radius * calls));
                    sY += ((-deltaY * delta2) / (dd) * (radius * calls));
                    sZ += ((-deltaZ * delta2) / (dd) * (radius * calls));
                }
            }
        }
        double phi = (1 + (deg / 2));
        deltaX = gravityCenter.x - dX;
        deltaY = gravityCenter.y - dY;
        deltaZ = gravityCenter.z - dZ;
        sX += (gravitation * deltaX * phi);
        sY += (gravitation * deltaY * phi);
        sZ += (gravitation * deltaZ * phi);
        deltaX = constGravityCenter.x - dX;
        deltaY = constGravityCenter.y - dY;
        deltaZ = constGravityCenter.z - dZ;
        sX += (gravitation * deltaX);
        sY += (gravitation * deltaY);
        sZ += (gravitation * deltaZ);
        sX += (((Math.random() * randBound) / 2) - randBound);
        sY += (((Math.random() * randBound) / 2) - randBound);
        sZ += (((Math.random() * randBound) / 2) - randBound);
        delta2 = (sX * sX) + (sY * sY) + (sZ * sZ);
        double delta = Math.sqrt(delta2);
        double tmp = ((Double) temperatures.get(node)).doubleValue();
        return new Vector3d((tmp * sX) / delta, (tmp * sY) / delta, (tmp * sZ) / delta);
    }

    private void updatePositionAndTemperature(VisualNodeInterface node, Vector3d w, int size) {
        double tmp_dX = node.getFinalPosition().x + w.x;
        double tmp_dY = node.getFinalPosition().y + w.y;
        double tmp_dZ = node.getFinalPosition().z + w.z;
        node.setFinalPosition((float) tmp_dX, (float) tmp_dY, (float) tmp_dZ);
        Vector3d v = (Vector3d) impulses.get(node);
        if ((v.x == 0.0) && (v.y == 0.0) && (v.z == 0.0)) {
            impulses.put(node, w);
            return;
        }
        double absV = Math.sqrt((v.x * v.x) + (v.y * v.y) + (v.z * v.z));
        double absW = Math.sqrt((w.x * w.x) + (w.y * w.y) + (w.z * w.z));
        double vw = (v.x * w.x) + (v.y * w.y) + (v.z * w.z);
        double cosVW = vw / (absV * absW);
        Vector3d vXw = new Vector3d((v.x + v.y + v.z) * w.x, (v.x + v.y + v.z) * w.y, (v.x + v.y + v.z) * w.z);
        double sinVW = (Math.sqrt((vXw.x * vXw.x) + (vXw.y * vXw.y) + (vXw.z * vXw.z))) / (absV * absW);
        double tmp = ((Double) temperatures.get(node)).doubleValue();
        if (cosVW > 0.7071) {
            tmp = tmp + (sigmaA * cosVW);
        } else if (cosVW <= -0.7071) {
            tmp = tmp + (sigmaO * cosVW);
        } else {
            double dir = ((Double) directions.get(node)).doubleValue();
            dir = dir + (sigmaR * ((sinVW >= 0) ? 1 : (-1)));
            tmp = tmp * (1 - (Math.abs(dir) / size));
            directions.put(node, new Double(dir));
        }
        temperatures.put(node, new Double(tmp));
        impulses.put(node, w);
    }
}
