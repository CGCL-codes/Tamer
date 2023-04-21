package org.sdf4j.optimisations.clustering.mfa;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import org.sdf4j.SDFMath;
import org.sdf4j.demo.SDFAdapterDemo;
import org.sdf4j.importer.GMLSDFImporter;
import org.sdf4j.importer.InvalidFileException;
import org.sdf4j.model.parameters.InvalidExpressionException;
import org.sdf4j.model.sdf.SDFAbstractVertex;
import org.sdf4j.model.sdf.SDFEdge;
import org.sdf4j.model.sdf.SDFGraph;
import org.sdf4j.model.sdf.visitors.SDFHierarchyFlattening;
import org.sdf4j.model.visitors.SDF4JException;
import org.sdf4j.optimisations.clustering.Clusterize;

/**
 * Class implementing the MFA graph partitioning algorithm
 * 
 * @author jpiat
 * 
 */
public class MFAPartitioning {

    private static final String CLUSTER = "cluster";

    @SuppressWarnings("unused")
    private static final Color[] colorSet = { Color.red, Color.blue, Color.green, Color.gray, Color.pink, Color.yellow, Color.orange };

    /**
	 * Testing the algorithm
	 * 
	 * @param args
	 * @throws InvalidExpressionException
	 * @throws SDF4JException
	 */
    public static void main(String[] args) throws InvalidExpressionException, SDF4JException {
        SDFAdapterDemo afterApplet = new SDFAdapterDemo();
        GMLSDFImporter importer = new GMLSDFImporter();
        SDFGraph graph = null;
        SDFGraph flattGraph = null;
        try {
            graph = importer.parse(new File("D:\\Preesm\\trunk\\tests\\ProdMatVect\\Algo\\TestMatMat.graphml"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFileException e) {
            e.printStackTrace();
        }
        if ((graph != null && !graph.validateModel(Logger.getAnonymousLogger())) || graph == null) {
            return;
        }
        SDFHierarchyFlattening flatHier = new SDFHierarchyFlattening();
        flatHier.flattenGraph(graph, 2);
        flattGraph = flatHier.getOutput();
        MFAPartitioning mfa = new MFAPartitioning(flattGraph, 2, 8, 1);
        mfa.computeV2(1);
        afterApplet.init(flattGraph);
    }

    /**
	 * Indicates the state of the spins system
	 */
    public boolean hold = false;

    private List<Node> nodes = null;

    private double equCoef = 1;

    private double depCoef = 1;

    private HashMap<Node, Integer> randomNode = null;

    private int nbPartitions;

    private SDFGraph base;

    /**
	 * New instance of the MFA partitioning
	 * 
	 * @param g
	 *            The graph to part
	 * @param nbPartitions
	 *            The number of partitions
	 * @param equCoef
	 *            Equilibrium coefficient to use while computing cost function
	 * @param depCoef
	 *            Dependency cut coefficient to use while computing cost
	 *            function
	 */
    public MFAPartitioning(SDFGraph g, int nbPartitions, double equCoef, double depCoef) {
        nodes = new ArrayList<Node>();
        base = g;
        this.nbPartitions = nbPartitions;
        randomNode = new HashMap<Node, Integer>();
        for (SDFAbstractVertex vertex : g.vertexSet()) {
            try {
                for (int i = 0; i < vertex.getNbRepeatAsInteger(); i++) {
                    Node n = new Node(vertex, i);
                    n.setNbPartitions(nbPartitions);
                    randomNode.put(n, 1);
                    nodes.add(n);
                }
            } catch (InvalidExpressionException e) {
                e.printStackTrace();
                return;
            }
        }
        refreshRandomNodes();
        this.equCoef = equCoef;
        this.depCoef = depCoef;
    }

    /**
	 * Compute the partitioning with a system starting temperature of tMax
	 * 
	 * @param tMax
	 *            The starting temperature of the system
	 * @throws InvalidExpressionException
	 */
    public void compute(double tMax) throws InvalidExpressionException {
        double T = tMax;
        double oldValue = computeEnergie();
        double newValue = 0;
        System.out.println(nodesSummary());
        while (newValue != oldValue && T >= 0) {
            oldValue = computeEnergie();
            while (!hold) {
                double denum = 0;
                Node randNode = getRandomNode();
                List<Double> hamVector = new ArrayList<Double>();
                List<Double> oldValues = new ArrayList<Double>();
                for (int i = 0; i < nbPartitions; i++) {
                    oldValues.add(randNode.getSpin(i).getValue());
                }
                for (int i = 0; i < nbPartitions; i++) {
                    randNode.setOne(i);
                    hamVector.add(computeEnergie());
                }
                for (int j = 0; j < nbPartitions; j++) {
                    double hamValue = hamVector.get(j);
                    denum += Math.exp(-(hamValue / T));
                }
                if (denum == Double.POSITIVE_INFINITY) {
                    double min = 0;
                    int minIndex = 0;
                    for (int i = 0; i < nbPartitions; i++) {
                        if (hamVector.get(i) < min) {
                            minIndex = i;
                            min = hamVector.get(i);
                        }
                    }
                    double nb = new Double(nbPartitions);
                    for (int i = 0; i < nbPartitions; i++) {
                        if (i == minIndex) {
                            randNode.getSpin(i).setValue(oldValues.get(i), (nb - 1) * (1 / nb));
                        } else {
                            randNode.getSpin(i).setValue(oldValues.get(i), ((1 / nb) / (nb - 1)));
                        }
                    }
                } else {
                    for (int i = 0; i < nbPartitions; i++) {
                        double num = 0;
                        double hamValue = hamVector.get(i);
                        num = Math.exp(-(hamValue / T));
                        if (num == Double.POSITIVE_INFINITY) {
                            num = Double.MAX_VALUE;
                        } else if (num == Double.NEGATIVE_INFINITY) {
                            num = Double.MIN_VALUE;
                        }
                        double val = num / denum;
                        if (((Double) val).isNaN()) {
                            System.out.println("not a number");
                        }
                        randNode.getSpin(i).setValue(oldValues.get(i), val);
                    }
                }
                refreshHold();
            }
            refreshRandomNodes();
            newValue = computeEnergie();
            reset();
            T = T - 0.01;
            System.out.println("T = " + T);
            System.out.println(nodesSummary());
        }
        try {
            System.out.println(summary());
        } catch (NoMigrationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Compute the partitioning with a system starting temperature of tMax
	 * 
	 * @param tMax
	 *            The starting temperature of the system
	 * @throws InvalidExpressionException
	 */
    public void computeV2(double tMax) throws InvalidExpressionException {
        double T = tMax;
        double oldValue = computeEnergie();
        double newValue = 0;
        System.out.println(nodesSummary());
        while (newValue != oldValue && T >= 0) {
            oldValue = computeEnergie();
            while (!allTreated()) {
                double denum = 0;
                Node randNode = getRandomNode();
                List<Double> hamVector = new ArrayList<Double>();
                List<Double> oldValues = new ArrayList<Double>();
                for (int i = 0; i < nbPartitions; i++) {
                    oldValues.add(randNode.getSpin(i).getValue());
                }
                for (int i = 0; i < nbPartitions; i++) {
                    randNode.setOne(i);
                    double Henergy = computeEnergie();
                    hamVector.add(Henergy);
                    denum += Math.exp(-(Henergy / T));
                }
                for (int i = 0; i < nbPartitions; i++) {
                    double num = 0;
                    double hamValue = hamVector.get(i);
                    num = Math.exp(-(hamValue / T));
                    double val = num / denum;
                    if (((Double) val).isNaN()) {
                        System.out.println("not a number");
                    }
                    randNode.getSpin(i).setValue(oldValues.get(i), val);
                }
            }
            refreshRandomNodes();
            newValue = computeEnergie();
            reset();
            T = T * 0.95;
            System.out.println("T = " + T);
            System.out.println(nodesSummary());
        }
        try {
            System.out.println(summary());
        } catch (NoMigrationException e) {
            e.printStackTrace();
        }
        Map<SDFAbstractVertex, Map<Integer, Integer>> nodeSplit = new HashMap<SDFAbstractVertex, Map<Integer, Integer>>();
        for (Node n : nodes) {
            Map<Integer, Integer> partitions;
            if ((partitions = nodeSplit.get(n.getVertex())) == null) {
                partitions = new HashMap<Integer, Integer>();
                nodeSplit.put(n.getVertex(), partitions);
            }
            if (partitions.get(n.getBelongsToPartition()) == null) {
                partitions.put(n.getBelongsToPartition(), 1);
            } else {
                partitions.put(n.getBelongsToPartition(), partitions.get(n.getBelongsToPartition()) + 1);
            }
        }
        System.out.println(nodeSplit);
    }

    /**
	 * Compute the Energy of this Spin's system
	 * 
	 * @return the Energy of the System
	 * @throws InvalidExpressionException
	 */
    public double computeEnergie() throws InvalidExpressionException {
        double result = 0;
        double max = 1;
        for (Node nI : nodes) {
            for (Node nJ : nodes) {
                if (nI != nJ) {
                    for (int i = 0; i < nbPartitions; i++) {
                        double locEnergy = computeLocalEnergie(nI.getSpin(i), nJ.getSpin(i), base.getAllEdges(nI.getVertex(), nJ.getVertex()));
                        result += locEnergy;
                        if (Math.abs(locEnergy) > max) {
                            max = Math.abs(locEnergy);
                        }
                    }
                }
            }
        }
        return result / nodes.size();
    }

    /**
	 * Computes the local energy of two spins interaction
	 * 
	 * @param spinI
	 *            The first particle
	 * @param spinJ
	 *            The second particle
	 * @param links
	 *            Set of links between the particles
	 * @return The computed local energy
	 * @throws InvalidExpressionException
	 */
    public double computeLocalEnergie(Spin spinI, Spin spinJ, Set<SDFEdge> links) throws InvalidExpressionException {
        int linksWeight = 1;
        int size = 1;
        for (SDFEdge edge : links) {
            size = links.size();
            if (edge.getTarget().equals(spinI)) {
                linksWeight += edge.getCons().intValue();
            } else {
                linksWeight += edge.getProd().intValue();
            }
        }
        double result = (spinI.getValue() * (1 - spinJ.getValue())) * ((depCoef * (linksWeight / size)) - equCoef);
        return result;
    }

    private Node getRandomNode() {
        Random rand = new Random(System.nanoTime());
        int index = rand.nextInt(nodes.size());
        while (randomNode.get(nodes.get(index)) == 0) {
            index = rand.nextInt(nodes.size());
        }
        int oldValue = randomNode.get(nodes.get(index));
        if (oldValue > 0) {
            int newValue = oldValue - 1;
            randomNode.put(nodes.get(index), newValue);
        }
        return nodes.get(index);
    }

    private boolean allTreated() {
        for (Node n : randomNode.keySet()) {
            if (randomNode.get(n) > 0) {
                return false;
            }
        }
        return true;
    }

    private String nodesSummary() {
        String res = "\n";
        for (Node n : nodes) {
            res += n.toString() + "\n";
        }
        return res;
    }

    /**
	 * CLusterize the found cluster
	 * 
	 * @throws InvalidExpressionException
	 * @throws SDF4JException
	 */
    public void performClustering() throws InvalidExpressionException, SDF4JException {
        Map<Integer, List<SDFAbstractVertex>> clusters = new HashMap<Integer, List<SDFAbstractVertex>>();
        for (Node nodeI : nodes) {
            int spinIquant = nodeI.getBelongsToPartition();
            if (clusters.get(spinIquant) == null) {
                clusters.put(spinIquant, new ArrayList<SDFAbstractVertex>());
            }
            clusters.get(spinIquant).add(nodeI.getVertex());
        }
        for (Integer key : clusters.keySet()) {
            if (clusters.get(key).size() > 1) {
                Clusterize.culsterizeBlocks(base, clusters.get(key), "cluster x" + key);
            }
        }
    }

    /**
	 * Refresh this system holding state
	 */
    public void refreshHold() {
        hold = true;
        for (int i = 0; i < nbPartitions; i++) {
            hold &= nodes.get(i).hold();
        }
    }

    private void refreshRandomNodes() {
        for (Node n : randomNode.keySet()) {
            randomNode.put(n, nodes.size() / nbPartitions);
        }
    }

    /**
	 * Reset this Spin
	 */
    public void reset() {
        hold = false;
    }

    /**
	 * Gives a Summary
	 * 
	 * @return a String representing the System
	 * @throws NoMigrationException
	 * @throws InvalidExpressionException
	 */
    public String summary() throws NoMigrationException, InvalidExpressionException {
        double nbCut = 0;
        double nb[] = new double[nbPartitions];
        int nbTot = base.edgeSet().size();
        int gcd[] = new int[nbPartitions];
        String res = new String();
        for (Node nodeI : nodes) {
            int spinIquant = nodeI.getBelongsToPartition();
            nodeI.getVertex().getPropertyBean().setValue(CLUSTER, spinIquant);
            nb[spinIquant]++;
            int vrb = (Integer) nodeI.getVertex().getNbRepeat();
            if (gcd[spinIquant] == 0) {
                gcd[spinIquant] = vrb;
            } else {
                gcd[spinIquant] = SDFMath.gcd(gcd[spinIquant], vrb);
            }
            for (Node nodeJ : nodes) {
                if (nodeI != nodeJ) {
                    int links;
                    links = base.getAllEdges(nodeI.getVertex(), nodeJ.getVertex()).size();
                    int spinJquant = nodeJ.getBelongsToPartition();
                    if (spinIquant != spinJquant) {
                        nbCut += links;
                    }
                }
            }
        }
        res = "Nombre de sommets dans le graph : " + nodes.size();
        for (int i = 0; i < nb.length; i++) {
            res += "\n Nombre de sommets dans la partition " + i + " : " + nb[i] + "\n Facteur de repetition dans la partition " + i + " : " + gcd[i];
        }
        res += "\n Nombre de d�pendances total : " + nbTot + "\n Nombre de d�pendances coup�s : " + nbCut;
        res += "\n";
        for (Node n : nodes) {
            res += n.toString() + "\n";
        }
        res += " final hamiltonian := " + computeEnergie() + "\n";
        return res;
    }
}
