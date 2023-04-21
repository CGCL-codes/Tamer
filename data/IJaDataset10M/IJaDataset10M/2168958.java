package uchicago.src.sim.network;

import java.util.ArrayList;
import java.util.Iterator;
import uchicago.src.sim.util.Random;
import uchicago.src.sim.util.RepastException;
import uchicago.src.sim.util.SimUtilities;

public class RolfeNet {

    Class nodeClass;

    Class edgeClass;

    int nNodes = -1;

    double density = -1;

    double clustring = -1;

    double reciProb = -1;

    int steps = -1;

    NetUtilities netUtil = new NetUtilities();

    public RolfeNet() {
    }

    public RolfeNet(Class node, Class edge) {
        nodeClass = node;
        edgeClass = edge;
    }

    public RolfeNet(Class node, Class edge, int size) {
        nodeClass = node;
        edgeClass = edge;
        nNodes = size;
    }

    public RolfeNet(Class node, Class edge, int size, double dens, double clust, double reci, int step) {
        nodeClass = node;
        edgeClass = edge;
        nNodes = size;
        density = dens;
        clustring = clust;
        reciProb = reci;
        steps = step;
    }

    /**
	*Returns the Class of nodes to be used in constructing the network.  Must be set before
	*makeRolfeNet() is called.
	**/
    public Class getNodeClass() {
        return nodeClass;
    }

    /**
	*Sets the Class of nodes to be used in constructing the network.  Must be set before
	*makeRolfeNet() is called.
	**/
    public void setNodeClass(Class node) {
        nodeClass = node;
    }

    /**
	*Returns the Class of edges to be used in constructing the network.  Must be set before
	*makeRolfeNet() is called.
	**/
    public Class getEdgeClass() {
        return edgeClass;
    }

    /**
	*Sets the Class of edges to be used in constructing the network.  Must be set before
	*makeRolfeNet() is called.
	**/
    public void setEdgeClass(Class edge) {
        edgeClass = edge;
    }

    /**
	*Returns the int for the size (number of nodes) in the network to be constructed.  Must be set before
	*makeRolfeNet() is called.
	**/
    public int getSize() {
        return nNodes;
    }

    /**
	*Sets the int for the size (number of nodes) in the network to be constructed.  Must be set before
	*makeRolfeNet() is called.
	**/
    public void setSize(int size) {
        nNodes = size;
    }

    /**
	*Returns the double of the desired density of the network (ratio of number of existing
	*edges to the maximum possible number of edges)  Must be set before
	*makeRolfeNet() is called.
	**/
    public double getDensity() {
        return density;
    }

    /**
	*Sets the double of the desired density of the network (ratio of number of existing
	*edges to the maximum possible number of edges)  Must be set before
	*makeRolfeNet() is called.
	**/
    public void setDensity(double dens) {
        density = dens;
    }

    /**Gets the double of the parameter for the desired clustring (density of ego network).
	*Must be set before makeRolfeNet() is called.
	**/
    public double getClustring() {
        return clustring;
    }

    public void setClustring(double clust) {
        clustring = clust;
    }

    public double getReciProb() {
        return reciProb;
    }

    public void setReciProb(double reci) {
        reciProb = reci;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int step) {
        steps = step;
    }

    /**
	*DO NOT USE! Experimental test class for generating nets with a particular
	*distribution
	@throws IllegalAccessException, InstantiationException
	*/
    public ArrayList createRolfeNet() throws IllegalAccessException, InstantiationException {
        if ((nodeClass == null) || (edgeClass == null)) {
            String error = "Unable to construct randomDensityNetwork: nodeClass or edgeClass was not set.";
            RepastException exception = new RepastException(error);
            SimUtilities.showError(error, exception);
        }
        ArrayList nodes = new ArrayList(nNodes);
        int jiTie = 0;
        int numParents = 0;
        double clustringMod = 0.0;
        double Pij = 0.0;
        for (int i = 0; i < nNodes; i++) {
            nodes.add((Node) nodeClass.newInstance());
        }
        for (int s = 0; s < steps; s++) {
            Node iNode = (Node) nodes.get(Random.uniform.nextIntFromTo(0, nNodes - 1));
            Node jNode = iNode;
            while (jNode.equals(iNode)) {
                jNode = (Node) nodes.get(Random.uniform.nextIntFromTo(0, nNodes - 1));
            }
            numParents = NetUtilities.getNumDirectTriads(iNode, jNode);
            jiTie = NetUtilities.getIJTie(jNode, iNode);
            if (numParents <= 2) {
                clustringMod = -0.1;
            } else {
                clustringMod = 0.1;
            }
            double degreeCap = (1 / (Math.pow(Math.E, ((NetUtilities.getOutDegree(iNode) - 5))) + 1));
            Pij = (1 - (1 - density) * Math.pow(Math.E, (-(clustring + clustringMod) * numParents - reciProb * jiTie))) * degreeCap + (.5 * NetUtilities.getIJTie(iNode, jNode));
            if (Random.uniform.nextDoubleFromTo(0, 1) < Pij) {
                if (NetUtilities.getIJTie(iNode, jNode) < 1) {
                    Edge edge = (Edge) edgeClass.newInstance();
                    edge.setFrom(iNode);
                    edge.setTo(jNode);
                    iNode.addOutEdge(edge);
                    jNode.addInEdge(edge);
                }
                if (NetUtilities.getIJTie(iNode, jNode) < 1) {
                    Edge edge = (Edge) edgeClass.newInstance();
                    edge.setFrom(jNode);
                    edge.setTo(iNode);
                    jNode.addOutEdge(edge);
                    iNode.addInEdge(edge);
                }
            } else {
                Iterator arcIter = ((ArrayList) iNode.getOutEdges()).iterator();
                while (arcIter.hasNext()) {
                    Edge arc = (Edge) arcIter.next();
                    if (((Node) arc.getTo()).equals(jNode)) {
                        iNode.removeOutEdge(arc);
                        jNode.removeInEdge(arc);
                        break;
                    }
                }
                arcIter = ((ArrayList) jNode.getOutEdges()).iterator();
                while (arcIter.hasNext()) {
                    Edge arc = (Edge) arcIter.next();
                    if (((Node) arc.getTo()).equals(iNode)) {
                        jNode.removeOutEdge(arc);
                        iNode.removeInEdge(arc);
                        break;
                    }
                }
            }
            if ((s % 10000) == 0) {
                System.out.println("Step=" + s + " clust=" + NetUtilities.calcClustCoef(nodes) + " Dens=" + NetUtilities.calcDensity(nodes));
            }
        }
        return nodes;
    }

    public ArrayList createRolfeNet(Class node, Class edge, int size, double dens, double clust, double reci, int step) throws IllegalAccessException, InstantiationException {
        nodeClass = node;
        edgeClass = edge;
        nNodes = size;
        density = dens;
        clustring = clust;
        reciProb = reci;
        steps = step;
        ArrayList nodeList = createRolfeNet();
        return nodeList;
    }
}
