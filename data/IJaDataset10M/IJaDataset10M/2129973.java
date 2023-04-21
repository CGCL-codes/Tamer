package dr.evolution.coalescent.structure;

import dr.evolution.coalescent.DemographicFunction;
import dr.evolution.colouring.ColourChangeMatrix;
import dr.evolution.tree.SimpleNode;
import dr.evolution.tree.SimpleTree;
import dr.evolution.tree.Tree;
import dr.evolution.util.TaxonList;
import dr.evolution.util.TimeScale;

/**
 * This class provides the basic engine for coalescent simulation of a given demographic model over a given time period.
 *
 * @author Andrew Rambaut
 *         <p/>
 *         $Id: StructuredCoalescentSimulator.java,v 1.7 2006/01/12 11:12:42 rambaut Exp $
 */
public class StructuredCoalescentSimulator {

    public static final String COALESCENT_TREE = "structuredCoalescentTree";

    public static final String COALESCENT_SIMULATOR = "structuredCoalescentSimulator";

    public static final String ROOT_HEIGHT = "rootHeight";

    public StructuredCoalescentSimulator() {
    }

    /**
     * Simulates a coalescent tree, given a taxon list.
     *
     * @param taxa          the set of taxa to simulate a coalescent tree between
     * @param demoFunctions the demographic function to use
     */
    public Tree simulateTree(TaxonList[] taxa, DemographicFunction[] demoFunctions, ColourChangeMatrix colourChangeMatrix) {
        SimpleNode[][] nodes = new SimpleNode[taxa.length][];
        for (int i = 0; i < taxa.length; i++) {
            nodes[i] = new SimpleNode[taxa[i].getTaxonCount()];
            for (int j = 0; j < taxa[i].getTaxonCount(); j++) {
                nodes[i][j] = new SimpleNode();
                nodes[i][j].setTaxon(taxa[i].getTaxon(j));
            }
        }
        dr.evolution.util.Date mostRecent = null;
        boolean usingDates = false;
        for (int i = 0; i < taxa.length; i++) {
            for (int j = 0; j < taxa[i].getTaxonCount(); j++) {
                if (TaxonList.Utils.hasAttribute(taxa[i], j, dr.evolution.util.Date.DATE)) {
                    usingDates = true;
                    dr.evolution.util.Date date = (dr.evolution.util.Date) taxa[i].getTaxonAttribute(j, dr.evolution.util.Date.DATE);
                    if ((date != null) && (mostRecent == null || date.after(mostRecent))) {
                        mostRecent = date;
                    }
                } else {
                    nodes[i][j].setHeight(0.0);
                }
            }
        }
        if (usingDates) {
            assert mostRecent != null;
            TimeScale timeScale = new TimeScale(mostRecent.getUnits(), true, mostRecent.getAbsoluteTimeValue());
            for (int i = 0; i < taxa.length; i++) {
                for (int j = 0; j < taxa[i].getTaxonCount(); j++) {
                    dr.evolution.util.Date date = (dr.evolution.util.Date) taxa[i].getTaxonAttribute(j, dr.evolution.util.Date.DATE);
                    if (date == null) {
                        throw new IllegalArgumentException("Taxon, " + taxa[i].getTaxonId(j) + ", is missing its date");
                    }
                    nodes[i][j].setHeight(timeScale.convertTime(date.getTimeValue(), date));
                }
                if (demoFunctions[0].getUnits() != mostRecent.getUnits()) {
                }
            }
        }
        return new SimpleTree(simulateCoalescent(nodes, demoFunctions, colourChangeMatrix));
    }

    /**
     * @return the root node of the given array of nodes after simulation of the coalescent under the given demographic model.
     */
    public SimpleNode simulateCoalescent(SimpleNode[][] nodes, DemographicFunction[] demographic, ColourChangeMatrix colourChangeMatrix) {
        SimpleNode[] rootNode = simulateCoalescent(nodes, demographic, colourChangeMatrix, 0.0, Double.POSITIVE_INFINITY);
        int attempts = 0;
        while (rootNode.length > 1 && attempts < 1000) {
            rootNode = simulateCoalescent(nodes, demographic, colourChangeMatrix, 0.0, Double.POSITIVE_INFINITY);
            attempts += 1;
        }
        if (rootNode.length > 1) {
            throw new RuntimeException(rootNode.length + " nodes found where there should have been 1, after 1000 tries!");
        }
        return rootNode[0];
    }

    public SimpleNode[] simulateCoalescent(SimpleNode[][] nodes, DemographicFunction[] demographic, ColourChangeMatrix colourChangeMatrix, double currentHeight, double maxHeight) {
        return null;
    }
}
