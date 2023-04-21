package joshua.decoder.chart_parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import joshua.decoder.JoshuaConfiguration;
import joshua.decoder.ff.FeatureFunction;
import joshua.decoder.ff.state_maintenance.DPState;
import joshua.decoder.ff.tm.Rule;
import joshua.decoder.hypergraph.HGNode;
import joshua.decoder.hypergraph.HyperEdge;

/**
 * this class implement functions:
 * (1) combine small itesm into larger ones using rules, and create
 *     items and hyper-edges to construct a hyper-graph,
 * (2) evaluate model score for items,
 * (3) cube-pruning
 * Note: Bin creates Items, but not all Items will be used in the
 * hyper-graph
 *
 * @author Zhifei Li, <zhifei.work@gmail.com>
 * @version $LastChangedDate: 2010-02-03 15:58:06 -0500 (Wed, 03 Feb 2010) $
 */
class Cell {

    private Chart chart = null;

    public BeamPruner<HGNode> beamPruner;

    private int goalSymID;

    private HashMap<String, HGNode> nodesSigTbl = new HashMap<String, HGNode>();

    private Map<Integer, SuperNode> superNodesTbl = new HashMap<Integer, SuperNode>();

    /** sort values in nodesSigTbl, 
	 * we need this list when necessary
	 */
    private List<HGNode> sortedNodes = null;

    private static final Logger logger = Logger.getLogger(Cell.class.getName());

    public Cell(Chart chart, int goalSymID) {
        this.chart = chart;
        this.goalSymID = goalSymID;
        if (JoshuaConfiguration.useBeamAndThresholdPrune) {
            PriorityQueue<HGNode> nodesHeap = new PriorityQueue<HGNode>(1, HGNode.logPComparator);
            beamPruner = new BeamPruner<HGNode>(nodesHeap, JoshuaConfiguration.relative_threshold, JoshuaConfiguration.max_n_items);
        }
    }

    void transitToGoal(Cell bin, List<FeatureFunction> featureFunctions, int sentenceLength) {
        this.sortedNodes = new ArrayList<HGNode>();
        HGNode goalItem = null;
        for (HGNode antNode : bin.getSortedNodes()) {
            if (antNode.lhs == this.goalSymID) {
                double logP = antNode.bestHyperedge.bestDerivationLogP;
                List<HGNode> antNodes = new ArrayList<HGNode>();
                antNodes.add(antNode);
                double finalTransitionLogP = ComputeNodeResult.computeCombinedTransitionLogP(featureFunctions, null, antNodes, 0, sentenceLength, null, this.chart.segmentID);
                List<HGNode> previousItems = new ArrayList<HGNode>();
                previousItems.add(antNode);
                HyperEdge dt = new HyperEdge(null, logP + finalTransitionLogP, finalTransitionLogP, previousItems, null);
                if (null == goalItem) {
                    goalItem = new HGNode(0, sentenceLength + 1, this.goalSymID, null, dt, logP + finalTransitionLogP);
                    this.sortedNodes.add(goalItem);
                } else {
                    goalItem.addHyperedgeInNode(dt);
                }
            }
        }
        if (logger.isLoggable(Level.INFO)) {
            if (null == goalItem) {
                logger.severe("goalItem is null!");
            } else {
                logger.info(String.format("Sentence id=" + this.chart.segmentID + "; BestlogP=%.3f", goalItem.bestHyperedge.bestDerivationLogP));
            }
        }
        ensureSorted();
        int itemsInGoalBin = getSortedNodes().size();
        if (1 != itemsInGoalBin) {
            throw new RuntimeException("the goal_bin does not have exactly one item");
        }
    }

    /**create a hyperege, and add it into the chart if not got prunned
	 * */
    HGNode addHyperEdgeInCell(ComputeNodeResult result, Rule rule, int i, int j, List<HGNode> ants, SourcePath srcPath, boolean noPrune) {
        HGNode res = null;
        HashMap<Integer, DPState> dpStates = result.getDPStates();
        double expectedTotalLogP = result.getExpectedTotalLogP();
        double transitionLogP = result.getTransitionTotalLogP();
        double finalizedTotalLogP = result.getFinalizedTotalLogP();
        if (noPrune == false && beamPruner != null && beamPruner.relativeThresholdPrune(expectedTotalLogP)) {
            this.chart.nPreprunedEdges++;
            res = null;
        } else {
            HyperEdge dt = new HyperEdge(rule, finalizedTotalLogP, transitionLogP, ants, srcPath);
            res = new HGNode(i, j, rule.getLHS(), dpStates, dt, expectedTotalLogP);
            HGNode oldNode = this.nodesSigTbl.get(res.getSignature());
            if (null != oldNode) {
                this.chart.nMerged++;
                if (res.getPruneLogP() > oldNode.getPruneLogP()) {
                    if (beamPruner != null) {
                        oldNode.setDead();
                        beamPruner.incrementDeadObjs();
                    }
                    res.addHyperedgesInNode(oldNode.hyperedges);
                    addNewNode(res, noPrune);
                } else {
                    oldNode.addHyperedgesInNode(res.hyperedges);
                }
            } else {
                this.chart.nAdded++;
                addNewNode(res, noPrune);
            }
        }
        return res;
    }

    List<HGNode> getSortedNodes() {
        ensureSorted();
        return this.sortedNodes;
    }

    Map<Integer, SuperNode> getSortedSuperItems() {
        ensureSorted();
        return this.superNodesTbl;
    }

    /**two cases this function gets called
	 * (1) a new hyperedge leads to a non-existing node signature
	 * (2) a new hyperedge's signature matches an old node's signature, but the best-logp of old node is worse than the new hyperedge's logP
	 * */
    private void addNewNode(HGNode node, boolean noPrune) {
        this.nodesSigTbl.put(node.getSignature(), node);
        this.sortedNodes = null;
        if (beamPruner != null) {
            if (noPrune == false) {
                List<HGNode> prunedNodes = beamPruner.addOneObjInHeapWithPrune(node);
                this.chart.nPrunedItems += prunedNodes.size();
                for (HGNode prunedNode : prunedNodes) nodesSigTbl.remove(prunedNode.getSignature());
            } else {
                beamPruner.addOneObjInHeapWithoutPrune(node);
            }
        }
        SuperNode si = this.superNodesTbl.get(node.lhs);
        if (null == si) {
            si = new SuperNode(node.lhs);
            this.superNodesTbl.put(node.lhs, si);
        }
        si.nodes.add(node);
    }

    /** get a sorted list of Nodes in the cell, and also make
	 * sure the list of node in any SuperItem is sorted, this
	 * will be called only necessary, which means that the list
	 * is not always sorted, mainly needed for goal_bin and
	 * cube-pruning
	 */
    private void ensureSorted() {
        if (null == this.sortedNodes) {
            HGNode[] nodesArray = new HGNode[this.nodesSigTbl.size()];
            int i = 0;
            for (HGNode node : this.nodesSigTbl.values()) nodesArray[i++] = node;
            Arrays.sort(nodesArray, HGNode.inverseLogPComparator);
            this.sortedNodes = new ArrayList<HGNode>();
            for (HGNode node : nodesArray) {
                this.sortedNodes.add(node);
            }
            List<SuperNode> tem_list = new ArrayList<SuperNode>(this.superNodesTbl.values());
            for (SuperNode t_si : tem_list) {
                t_si.nodes.clear();
            }
            for (HGNode it : this.sortedNodes) {
                SuperNode si = this.superNodesTbl.get(it.lhs);
                if (null == si) {
                    throw new RuntimeException("Does not have super Item, have to exist");
                }
                si.nodes.add(it);
            }
            List<Integer> toRemove = new ArrayList<Integer>();
            for (Integer k : this.superNodesTbl.keySet()) {
                if (this.superNodesTbl.get(k).nodes.size() <= 0) {
                    toRemove.add(k);
                }
            }
            for (Integer t : toRemove) {
                this.superNodesTbl.remove(t);
            }
        }
    }
}
