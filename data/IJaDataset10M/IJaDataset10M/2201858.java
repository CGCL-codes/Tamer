package edu.iastate.aurora.supertree;

import java.util.ArrayList;
import java.util.List;
import edu.iastate.aurora.Aurora;
import edu.iastate.aurora.sto.RROperation;
import edu.iastate.aurora.sto.SPROperation;
import edu.iastate.aurora.sto.SubtreeTransferOperation;
import edu.iastate.aurora.sto.TBROperation;
import edu.iastate.aurora.struct.PhyloTree;
import edu.iastate.aurora.struct.PhyloTreeNode;
import edu.iastate.aurora.struct.Range;
import edu.iastate.aurora.struct.TripleSum;
import edu.iastate.aurora.util.Debug;

public class HillClimbTripletDistance extends SuperTreeHeuristic {

    private boolean m_DoSPROnly;

    private InputTripletSumQuerier m_TripletSum;

    private PhyloTree<Integer> m_SuperTree;

    private List<PhyloTreeNode<Integer>> m_PostOrderNodes;

    private TripleSum[][][] m_SubtreeSums;

    private TripleSum[][] m_NNISums;

    public HillClimbTripletDistance(boolean doSPROnly) {
        m_DoSPROnly = doSPROnly;
    }

    public PhyloTree<Integer> getSuperTreeInt(List<PhyloTree<Integer>> inputTrees, int taxaSize) {
        initilize(inputTrees, taxaSize);
        SubtreeTransferOperation<Integer> bestSTO = getBestSTO();
        while (bestSTO != null) {
            bestSTO.performOn(m_SuperTree);
            bestSTO = getBestSTO();
        }
        return m_SuperTree;
    }

    private void initilize(List<PhyloTree<Integer>> inputTrees, int taxaSize) {
        final int internalNodeSize = taxaSize * 2 - 1;
        m_TripletSum = new InputTripletSumQuerier(inputTrees, taxaSize);
        m_SuperTree = PhyloTree.makeRandomLeafAddingTree("super", taxaSize, m_TripletSum);
        if (Debug.IsDebugMode) {
            Aurora.writeNexusFile("init.tre", m_Converter.toString(m_SuperTree));
        }
        final TripleSum[][][] subtreeSums = new TripleSum[internalNodeSize][internalNodeSize][internalNodeSize];
        for (int i = 0; i < internalNodeSize - 2; i++) {
            final TripleSum[][] subtreeSumsI = subtreeSums[i];
            for (int j = i + 1; j < internalNodeSize - 1; j++) {
                final TripleSum[] subtreeSumsIJ = subtreeSumsI[j];
                for (int k = j + 1; k < internalNodeSize; k++) {
                    subtreeSumsIJ[k] = new TripleSum();
                }
            }
        }
        m_SubtreeSums = subtreeSums;
        final TripleSum[][] nniSums = new TripleSum[internalNodeSize][internalNodeSize];
        for (int i = 0; i < internalNodeSize; i++) {
            final TripleSum[] nniSumsI = nniSums[i];
            for (int j = 0; j < internalNodeSize; j++) {
                nniSumsI[j] = new TripleSum();
            }
        }
        m_NNISums = nniSums;
    }

    private SubtreeTransferOperation<Integer> getBestSTO() {
        m_PostOrderNodes = m_SuperTree.cacheAndGetPostOrder();
        final int internalNodeSize = m_PostOrderNodes.size();
        clearSums();
        computeSubtreeSums();
        computeNNISums();
        List<SPROperation<Integer>> sprOps = new ArrayList<SPROperation<Integer>>(internalNodeSize);
        for (int i = 0; i < internalNodeSize; i++) {
            sprOps.add(getBestSPRForSubtree(i));
        }
        if (m_DoSPROnly) {
            SPROperation<Integer> bestSPR = sprOps.get(0);
            for (SPROperation<Integer> sprOp : sprOps) {
                if (sprOp.getScoreDiff() > bestSPR.getScoreDiff()) {
                    bestSPR = sprOp;
                }
            }
            if (bestSPR.getScoreDiff() > 0) {
                return bestSPR;
            } else {
                return null;
            }
        } else {
            List<RROperation<Integer>> rrOps = new ArrayList<RROperation<Integer>>(internalNodeSize);
            for (int i = 0; i < internalNodeSize; i++) {
                rrOps.add(getBestRRForSubtree(i));
            }
            int bestSPRAndRR = 0;
            int bestScore = sprOps.get(bestSPRAndRR).getScoreDiff() + rrOps.get(bestSPRAndRR).getScoreDiff();
            for (int i = 1; i < internalNodeSize; i++) {
                final int newScore = sprOps.get(i).getScoreDiff() + rrOps.get(i).getScoreDiff();
                if (newScore > bestScore) {
                    bestSPRAndRR = i;
                    bestScore = newScore;
                }
            }
            if (bestScore > 0) {
                return new TBROperation<Integer>(sprOps.get(bestSPRAndRR), rrOps.get(bestSPRAndRR));
            } else {
                return null;
            }
        }
    }

    private void clearSums() {
        final TripleSum[][][] subtreeSums = m_SubtreeSums;
        final int internalNodeSize = subtreeSums.length;
        for (int i = 0; i < internalNodeSize - 2; i++) {
            final TripleSum[][] subtreeSumsI = subtreeSums[i];
            for (int j = i + 1; j < internalNodeSize - 1; j++) {
                final TripleSum[] subtreeSumsIJ = subtreeSumsI[j];
                for (int k = j + 1; k < internalNodeSize; k++) {
                    subtreeSumsIJ[k].clear();
                }
            }
        }
        final TripleSum[][] nniSums = m_NNISums;
        for (int i = 0; i < internalNodeSize; i++) {
            final TripleSum[] nniSumsI = nniSums[i];
            for (int j = 0; j < internalNodeSize; j++) {
                nniSumsI[j].clear();
            }
        }
    }

    private void computeSubtreeSums() {
        final InputTripletSumQuerier tripletSum = m_TripletSum;
        final List<PhyloTreeNode<Integer>> postOrderNodes = m_PostOrderNodes;
        final TripleSum[][][] subtreeSums = m_SubtreeSums;
        final int internalNodeSize = subtreeSums.length;
        for (int i = 0; i < internalNodeSize - 2; i++) {
            final PhyloTreeNode<Integer> nodeI = postOrderNodes.get(i);
            final TripleSum[][] subtreeSumsI = subtreeSums[i];
            TripleSum[][] subtreeSumsIchild0 = null;
            TripleSum[][] subtreeSumsIchild1 = null;
            if (!nodeI.isLeaf()) {
                subtreeSumsIchild0 = subtreeSums[nodeI.getChild(0).getPostOrder()];
                subtreeSumsIchild1 = subtreeSums[nodeI.getChild(1).getPostOrder()];
            }
            for (int j = i + 1; j < internalNodeSize - 1; j++) {
                final PhyloTreeNode<Integer> nodeJ = postOrderNodes.get(j);
                if (nodeJ.isAncestorOf(nodeI)) continue;
                final TripleSum[] subtreeSumsIJ = subtreeSumsI[j];
                TripleSum[] subtreeSumsIJchild0 = null;
                TripleSum[] subtreeSumsIJchild1 = null;
                if (!nodeJ.isLeaf()) {
                    subtreeSumsIJchild0 = subtreeSumsI[nodeJ.getChild(0).getPostOrder()];
                    subtreeSumsIJchild1 = subtreeSumsI[nodeJ.getChild(1).getPostOrder()];
                }
                TripleSum[] subtreeSumsIchild0J = null;
                TripleSum[] subtreeSumsIchild1J = null;
                if (!nodeI.isLeaf()) {
                    subtreeSumsIchild0J = subtreeSumsIchild0[j];
                    subtreeSumsIchild1J = subtreeSumsIchild1[j];
                }
                for (int k = j + 1; k < internalNodeSize; k++) {
                    final PhyloTreeNode<Integer> nodeK = postOrderNodes.get(k);
                    if (nodeK.isAncestorOf(nodeJ)) continue;
                    if (nodeK.isLeaf()) {
                        if (nodeJ.isLeaf()) {
                            if (nodeI.isLeaf()) {
                                tripletSum.addTo(subtreeSumsIJ[k], nodeI.getName().intValue(), nodeJ.getName().intValue(), nodeK.getName().intValue());
                            } else {
                                subtreeSumsIJ[k].add(subtreeSumsIchild0J[k]);
                                subtreeSumsIJ[k].add(subtreeSumsIchild1J[k]);
                            }
                        } else {
                            subtreeSumsIJ[k].add(subtreeSumsIJchild0[k]);
                            subtreeSumsIJ[k].add(subtreeSumsIJchild1[k]);
                        }
                    } else {
                        final int nodeKchild0Order = nodeK.getChild(0).getPostOrder();
                        final int nodeKchild1Order = nodeK.getChild(1).getPostOrder();
                        subtreeSumsIJ[k].add(subtreeSumsIJ[nodeKchild0Order]);
                        subtreeSumsIJ[k].add(subtreeSumsIJ[nodeKchild1Order]);
                    }
                }
            }
        }
    }

    /**
	 * almost fully filled, pruned subtree root x base node
	 * The subtree is always at orientation 1, base's first child orientation 2, base's second child orientation 3
	 * subtree is not root
	 * base is not leaf
	 * pruned subtree is not base, children of base, nor ancestor of base
	 * all index by post order
	 */
    private void computeNNISums() {
        final TripleSum[][][] subtreeSums = m_SubtreeSums;
        final TripleSum[][] nniSums = m_NNISums;
        final List<PhyloTreeNode<Integer>> postOrderNodes = m_PostOrderNodes;
        final int internalNodeSize = nniSums.length;
        for (int i = 0; i < internalNodeSize - 1; i++) {
            final PhyloTreeNode<Integer> subtreeNode = postOrderNodes.get(i);
            final TripleSum[] nniSumsI = nniSums[i];
            for (int j = 0; j < internalNodeSize; j++) {
                final PhyloTreeNode<Integer> baseNode = postOrderNodes.get(j);
                if (baseNode.isLeaf() || subtreeNode.isAncestorOf(baseNode) || subtreeNode.getParent() == baseNode || i == j) {
                    continue;
                } else if (baseNode.isAncestorOf(subtreeNode)) {
                    computeEmbeddedNNISum(nniSumsI[j], subtreeNode, baseNode);
                } else {
                    final int baseChild0Order = baseNode.getChild(0).getPostOrder();
                    final int baseChild1Order = baseNode.getChild(1).getPostOrder();
                    if (i < j) {
                        nniSumsI[j].add(subtreeSums[i][baseChild0Order][baseChild1Order]);
                    } else {
                        nniSumsI[j].add(subtreeSums[baseChild0Order][baseChild1Order][i], TripleSum.ORIENT_3, TripleSum.ORIENT_1, TripleSum.ORIENT_2);
                    }
                }
            }
        }
    }

    private void computeEmbeddedNNISum(TripleSum targetSum, PhyloTreeNode<Integer> subtreeNode, PhyloTreeNode<Integer> baseNode) {
        final TripleSum[][][] subtreeSums = m_SubtreeSums;
        final PhyloTreeNode<Integer> baseChild0 = baseNode.getChild(0);
        final PhyloTreeNode<Integer> baseChild1 = baseNode.getChild(1);
        final boolean isSubtreeEmbeddedInBaseChild0 = baseChild0.isAncestorOf(subtreeNode);
        final PhyloTreeNode<Integer> embeddedBase = isSubtreeEmbeddedInBaseChild0 ? baseChild0 : baseChild1;
        final PhyloTreeNode<Integer> otherBase = isSubtreeEmbeddedInBaseChild0 ? baseChild1 : baseChild0;
        final int subtreeOrder = subtreeNode.getPostOrder();
        final int otherBaseOrder = otherBase.getPostOrder();
        PhyloTreeNode<Integer> currentNode = subtreeNode;
        while (currentNode != embeddedBase) {
            final PhyloTreeNode<Integer> currentParent = currentNode.getParent();
            final PhyloTreeNode<Integer> otherChild = currentParent.getFirstChildOtherThan(currentNode);
            final int otherChildOrder = otherChild.getPostOrder();
            if (isSubtreeEmbeddedInBaseChild0) {
                TripleSum.add(targetSum, subtreeSums, subtreeOrder, otherChildOrder, otherBaseOrder);
            } else {
                TripleSum.add(targetSum, subtreeSums, subtreeOrder, otherBaseOrder, otherChildOrder);
            }
            currentNode = currentParent;
        }
    }

    private SPROperation<Integer> getBestSPRForSubtree(int subtreePostOrder) {
        final List<PhyloTreeNode<Integer>> postOrderNodes = m_PostOrderNodes;
        final PhyloTreeNode<Integer> subtreeRoot = postOrderNodes.get(subtreePostOrder);
        final TripleSum[] nniSumsBases = m_NNISums[subtreePostOrder];
        final int internalNodeSize = postOrderNodes.size();
        if (subtreePostOrder == internalNodeSize - 1) {
            return SPROperation.makeIdentity();
        }
        int[] moveDownDiffsFromRoot = new int[internalNodeSize];
        for (int i = 0; i < internalNodeSize - 1; i++) {
            final PhyloTreeNode<Integer> targetNode = postOrderNodes.get(i);
            final PhyloTreeNode<Integer> targetParent = targetNode.getParent();
            if (subtreeRoot.isAncestorOf(targetNode) || subtreeRoot.getParent() == targetParent) {
                continue;
            }
            final TripleSum nniSumsBaseTargetParent = nniSumsBases[targetParent.getPostOrder()];
            int moveDownDiff = -nniSumsBaseTargetParent.get(TripleSum.ORIENT_1);
            if (targetParent.getChild(0) == targetNode) {
                moveDownDiff += nniSumsBaseTargetParent.get(TripleSum.ORIENT_3);
            } else {
                moveDownDiff += nniSumsBaseTargetParent.get(TripleSum.ORIENT_2);
            }
            moveDownDiffsFromRoot[i] = moveDownDiff;
        }
        SPRAbsDiffCalculator calc = new SPRAbsDiffCalculator(moveDownDiffsFromRoot, subtreeRoot);
        return calc.getBestSPR();
    }

    class SPRAbsDiffCalculator {

        private int[] m_MoveDownDiffsFromRoot;

        private PhyloTreeNode<Integer> m_SubtreeRoot;

        private int[] m_AbsDiffsFromRoot;

        private PhyloTreeNode<Integer> m_CurrentBestTarget;

        private int m_CurrentBestAbsDiffsFromRoot;

        public SPRAbsDiffCalculator(int[] moveDownDiffsFromRoot, PhyloTreeNode<Integer> subtreeRoot) {
            m_MoveDownDiffsFromRoot = moveDownDiffsFromRoot;
            m_SubtreeRoot = subtreeRoot;
            m_AbsDiffsFromRoot = new int[moveDownDiffsFromRoot.length];
            m_CurrentBestTarget = m_PostOrderNodes.get(m_PostOrderNodes.size() - 1);
            m_CurrentBestAbsDiffsFromRoot = 0;
        }

        public SPROperation<Integer> getBestSPR() {
            final PhyloTreeNode<Integer> superTreeRoot = m_SuperTree.getRoot();
            for (int i = 0; i < superTreeRoot.getNumberOfChildren(); i++) {
                calculateAbsDiffs(superTreeRoot, superTreeRoot.getChild(i));
            }
            if (m_CurrentBestTarget == m_SubtreeRoot.getParent()) {
                return SPROperation.makeIdentity();
            }
            final int subtreeRootParentOrder = m_SubtreeRoot.getParent().getPostOrder();
            final int scoreDiff = m_CurrentBestAbsDiffsFromRoot - m_AbsDiffsFromRoot[subtreeRootParentOrder];
            return new SPROperation<Integer>(scoreDiff, m_SubtreeRoot, m_CurrentBestTarget);
        }

        private void calculateAbsDiffs(PhyloTreeNode<Integer> parent, PhyloTreeNode<Integer> child) {
            if (m_SubtreeRoot.isAncestorOf(child)) return;
            final int parentOrder = parent.getPostOrder();
            final int childOrder = child.getPostOrder();
            if (m_SubtreeRoot.getParent() == parent) {
                m_AbsDiffsFromRoot[childOrder] = m_AbsDiffsFromRoot[parentOrder];
            } else {
                final int childAbsDiffsFromRoot = m_AbsDiffsFromRoot[parentOrder] + m_MoveDownDiffsFromRoot[childOrder];
                if (childAbsDiffsFromRoot > m_CurrentBestAbsDiffsFromRoot) {
                    m_CurrentBestAbsDiffsFromRoot = childAbsDiffsFromRoot;
                    m_CurrentBestTarget = child;
                }
                m_AbsDiffsFromRoot[childOrder] = childAbsDiffsFromRoot;
            }
            for (int i = 0; i < child.getNumberOfChildren(); i++) {
                calculateAbsDiffs(child, child.getChild(i));
            }
        }
    }

    private RROperation<Integer> getBestRRForSubtree(int subtreePostOrder) {
        final List<PhyloTreeNode<Integer>> postOrderNodes = m_PostOrderNodes;
        final PhyloTreeNode<Integer> subtreeRoot = postOrderNodes.get(subtreePostOrder);
        final TripleSum[][] nniSums = m_NNISums;
        final int internalNodeSize = postOrderNodes.size();
        if (subtreeRoot.isLeaf()) {
            return RROperation.makeIdentity();
        }
        int[] rerootDiffsFromSubtreeRoot = new int[internalNodeSize];
        final Range subtreeRange = subtreeRoot.getSubtreeOrder();
        for (int i = subtreeRange.getBegin(); i < subtreeRange.getEnd(); i++) {
            final PhyloTreeNode<Integer> targetParent = postOrderNodes.get(i);
            if (targetParent.isLeaf()) {
                continue;
            }
            final int targetParentOrder = targetParent.getPostOrder();
            TripleSum tripleSum = new TripleSum();
            PhyloTreeNode<Integer> embeddingChild = targetParent;
            PhyloTreeNode<Integer> embeddingParent = embeddingChild.getParent();
            while (embeddingChild != subtreeRoot) {
                final int otherChildOrder = embeddingParent.getFirstChildOtherThan(embeddingChild).getPostOrder();
                tripleSum.add(nniSums[otherChildOrder][targetParentOrder]);
                embeddingChild = embeddingParent;
                embeddingParent = embeddingParent.getParent();
            }
            final int target0Order = targetParent.getChild(0).getPostOrder();
            final int target1Order = targetParent.getChild(1).getPostOrder();
            final int rerootPenalty = tripleSum.get(TripleSum.ORIENT_1);
            rerootDiffsFromSubtreeRoot[target0Order] = tripleSum.get(TripleSum.ORIENT_2) - rerootPenalty;
            rerootDiffsFromSubtreeRoot[target1Order] = tripleSum.get(TripleSum.ORIENT_3) - rerootPenalty;
        }
        RRAbsDiffCalculator calc = new RRAbsDiffCalculator(rerootDiffsFromSubtreeRoot, subtreeRoot);
        return calc.getBestRR();
    }

    static class RRAbsDiffCalculator {

        private int[] m_RerootDiffsFromSubtreeRoot;

        private PhyloTreeNode<Integer> m_SubtreeRoot;

        private int[] m_AbsDiffsFromRoot;

        private PhyloTreeNode<Integer> m_CurrentBestTarget;

        private int m_CurrentBestAbsDiffsFromRoot;

        public RRAbsDiffCalculator(int[] rerootDiffsFromSubtreeRoot, PhyloTreeNode<Integer> subtreeRoot) {
            m_RerootDiffsFromSubtreeRoot = rerootDiffsFromSubtreeRoot;
            m_SubtreeRoot = subtreeRoot;
            m_AbsDiffsFromRoot = new int[rerootDiffsFromSubtreeRoot.length];
            m_CurrentBestTarget = subtreeRoot;
            m_CurrentBestAbsDiffsFromRoot = 0;
        }

        public RROperation<Integer> getBestRR() {
            for (int i = 0; i < m_SubtreeRoot.getNumberOfChildren(); i++) {
                final PhyloTreeNode<Integer> subtreeChild = m_SubtreeRoot.getChild(i);
                for (int j = 0; j < subtreeChild.getNumberOfChildren(); j++) {
                    calculateAbsDiffs(subtreeChild, subtreeChild.getChild(j));
                }
            }
            if (m_CurrentBestTarget == m_SubtreeRoot) {
                return RROperation.<Integer>makeIdentity();
            } else {
                return new RROperation<Integer>(m_CurrentBestAbsDiffsFromRoot, m_SubtreeRoot, m_CurrentBestTarget);
            }
        }

        private void calculateAbsDiffs(PhyloTreeNode<Integer> parent, PhyloTreeNode<Integer> child) {
            final int parentOrder = parent.getPostOrder();
            final int childOrder = child.getPostOrder();
            final int childAbsDiffsFromRoot = m_AbsDiffsFromRoot[parentOrder] + m_RerootDiffsFromSubtreeRoot[childOrder];
            if (childAbsDiffsFromRoot > m_CurrentBestAbsDiffsFromRoot) {
                m_CurrentBestAbsDiffsFromRoot = childAbsDiffsFromRoot;
                m_CurrentBestTarget = child;
            }
            m_AbsDiffsFromRoot[childOrder] = childAbsDiffsFromRoot;
            for (int i = 0; i < child.getNumberOfChildren(); i++) {
                calculateAbsDiffs(child, child.getChild(i));
            }
        }
    }
}
