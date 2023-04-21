package cz.cuni.mff.ksi.jinfer.functionalDependencies;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.FD;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.SidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TleftSidePaths;
import cz.cuni.mff.ksi.jinfer.functionalDependencies.fd.TrightSidePaths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Support class responsible for creating and checking tree tuples.
 * @author sviro
 */
public final class TupleFactory {

    private static final Logger LOG = Logger.getLogger(TupleFactory.class);

    /**
   * Check if provided tuple is a tree tuple.
   * @param tree Tree over which is tuple defined.
   * @param tuple Tuple to be checked.
   * @return true if tuple is tree tuple.
   */
    public static boolean isTuple(final RXMLTree tree, final Tuple tuple) {
        final List<Tuple> allTuples = tree.getTuples();
        for (Tuple tuple1 : allTuples) {
            if (!tuple1.equals(tuple) && tuple1.contains(tuple)) {
                return false;
            }
        }
        return true;
    }

    /**
   * Removes tuple from tree. Also unmark nodes from tuple.
   * @param tree Tree from which is tuple removed.
   * @param tuple Tuple to be removed.
   */
    public static void removeTuple(final RXMLTree tree, final Tuple tuple) {
        unmarkNodesFromTuple(tree, tuple);
        tree.removeTuple(tuple);
    }

    /**
   * Remove all tuples provided in a set from tree.
   * @param tree Tree from which are tuples removed.
   * @param tuplesToRemove Set of tuples to be removed.
   */
    public static void removeTuples(final RXMLTree tree, final Set<Tuple> tuplesToRemove) {
        for (Tuple tuple : tuplesToRemove) {
            removeTuple(tree, tuple);
        }
    }

    private TupleFactory() {
    }

    /**
   * Create all tuples for particular tree.
   * @param tree Tree for which to create tuples.
   * @return List of all created tuples.
   * @throws InterruptedException if user interrupted repairing.
   */
    public static List<Tuple> createTuples(final RXMLTree tree) throws InterruptedException {
        LOG.info("Starting creation of Tree Tuples");
        final List<Tuple> result = new ArrayList<Tuple>();
        final List<Path> paths = tree.getPaths();
        LOG.debug("Paths from tree retreived.");
        final Queue<Tuple> lastAddedTuples = new ArrayDeque<Tuple>();
        final int firstTupleId = tree.getNewTupleID();
        final Tuple firstTuple = markNodesToTuple(tree, null, firstTupleId, null, null);
        lastAddedTuples.add(firstTuple);
        for (Path path : paths) {
            if (path.isStringPath()) {
                continue;
            }
            LOG.debug("Creating tuples from path " + path);
            final Queue<Tuple> newAddedTuples = new ArrayDeque<Tuple>();
            while (!lastAddedTuples.isEmpty()) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                final Tuple lastTuple = lastAddedTuples.poll();
                final PathAnswer pathAnswer = tree.getPathAnswerForCreatingTuple(path, lastTuple);
                if (pathAnswer == null) {
                    throw new RuntimeException("PathAnswer can't be null.");
                }
                if (!pathAnswer.hasMaxOneElement()) {
                    for (Node node : pathAnswer.getNodeAnswers()) {
                        final int newTupleID = tree.getNewTupleID();
                        final Tuple tuple = markNodesToTuple(tree, node, newTupleID, lastTuple, pathAnswer.getNodeAnswers());
                        LOG.debug("New tuple " + tuple + " has been created.");
                        newAddedTuples.add(tuple);
                    }
                    unmarkNodesFromTuple(tree, lastTuple);
                    LOG.debug("Tuple " + lastTuple + " has been removed.");
                } else {
                    newAddedTuples.add(lastTuple);
                }
            }
            lastAddedTuples.addAll(newAddedTuples);
        }
        result.addAll(lastAddedTuples);
        LOG.debug("Tree Tuples created.");
        return result;
    }

    /**
   * Get all possible pair of provided tuples.
   * @param tuples List of tuples from which are pairs created.
   * @return List of pairs of tuples.
   * @throws InterruptedException if user interrupted repairing.
   */
    public static List<Pair<Tuple, Tuple>> getTuplePairs(final List<Tuple> tuples) throws InterruptedException {
        LOG.debug("Starting creating tuple pairs");
        if (tuples == null) {
            return null;
        }
        final List<Pair<Tuple, Tuple>> result = new ArrayList<Pair<Tuple, Tuple>>();
        for (int i = 0; i < tuples.size() - 1; i++) {
            final Tuple tuple = tuples.get(i);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
            for (int j = 1 + i; j < tuples.size(); j++) {
                final Tuple tuple1 = tuples.get(j);
                result.add(new ImmutablePair<Tuple, Tuple>(tuple, tuple1));
            }
        }
        LOG.debug("Tuple pairs created");
        return result;
    }

    /**
   * Get all tuple pairs which not satisfies provided functional dependency. This
   * method is suitable for thesis algorithm.
   * @param tree Tree over which are tuples defined.
   * @param fd Functional dependency for which is checking satisfaction.
   * @return List of all pairs that do not satisfy functional dependency.
   * @throws InterruptedException if user interrupted repairing.
   */
    public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFDThesis(final RXMLTree tree, final FD fd) throws InterruptedException {
        return getTuplePairNotSatisfyingFDGeneral(tree, fd, true);
    }

    /**
   * Get all tuple pairs which not satisfies provided functional dependency.
   * @param tree Tree over which are tuples defined.
   * @param fd Functional dependency for which is checking satisfaction.
   * @return List of all pairs that do not satisfy functional dependency.
   * @throws InterruptedException if user interrupted repairing.
   */
    public static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFD(final RXMLTree tree, final FD fd) throws InterruptedException {
        return getTuplePairNotSatisfyingFDGeneral(tree, fd, false);
    }

    private static List<Pair<Tuple, Tuple>> getTuplePairNotSatisfyingFDGeneral(final RXMLTree tree, final FD fd, final boolean isThesis) throws InterruptedException {
        final List<Pair<Tuple, Tuple>> notSatisfyingTuples = new ArrayList<Pair<Tuple, Tuple>>();
        final List<Pair<Tuple, Tuple>> tuplePairs = tree.getTuplePairs();
        if (tuplePairs == null || tuplePairs.isEmpty()) {
            throw new RuntimeException("List of tuple pairs can't be null or empty.");
        }
        for (Pair<Tuple, Tuple> tuplePair : tuplePairs) {
            boolean tuplePairSatisfy;
            if (isThesis) {
                tuplePairSatisfy = tree.isTuplePairSatisfyingFDThesis(tuplePair, fd);
            } else {
                tuplePairSatisfy = tree.isTuplePairSatisfyingFD(tuplePair, fd);
            }
            if (!tuplePairSatisfy) {
                notSatisfyingTuples.add(tuplePair);
            }
        }
        return notSatisfyingTuples;
    }

    /**
   * Get list of path answers of particular side of functional dependency of particular tuple.
   * @param tree Tree over which is tuple defined.
   * @param tuple Tuple for which is get answers.
   * @param sidePaths Side of functional dependency for which is get answers.
   * @param isThesis flag indicating if thesis algorithm is used.
   * @return List of path answers of particular side.
   */
    public static List<PathAnswer> getFDSidePathAnswers(final RXMLTree tree, final Tuple tuple, final SidePaths sidePaths, final boolean isThesis) {
        LOG.debug("Creating side path answers for tuple " + tuple);
        if (tuple == null) {
            throw new RuntimeException("Tuple can't be null.");
        }
        if (sidePaths == null) {
            throw new RuntimeException("Side paths can't be null.");
        }
        final List<PathAnswer> result = new ArrayList<PathAnswer>();
        if (sidePaths instanceof TleftSidePaths) {
            final TleftSidePaths leftSide = (TleftSidePaths) sidePaths;
            for (Path path : leftSide.getPaths()) {
                final PathAnswer pathAnswer = tree.getPathAnswerForTuple(path, tuple, isThesis);
                result.add(pathAnswer);
            }
        } else if (sidePaths instanceof TrightSidePaths) {
            final TrightSidePaths rightSide = (TrightSidePaths) sidePaths;
            final PathAnswer pathAnswer = tree.getPathAnswerForTuple(rightSide.getPathObj(), tuple, isThesis);
            result.add(pathAnswer);
        }
        return result;
    }

    private static Tuple markNodesToTuple(final RXMLTree tree, final Node cuttingNode, final int tupleId, final Tuple actualTuple, final List<Node> tupleCut) {
        final Tuple result = new Tuple(tree, tupleId);
        traverseTree(tree.getDocument().getDocumentElement(), tree.getNodesMap(), cuttingNode, actualTuple, result, tupleCut, false);
        return result;
    }

    private static void unmarkNodesFromTuple(final RXMLTree tree, final Tuple lastTuple) {
        traverseTree(tree.getDocument().getDocumentElement(), tree.getNodesMap(), null, null, lastTuple, null, true);
    }

    /**
   * Unmark particular node from all tuples it is part of.
   * @param tree Tree from which node is.
   * @param node Node wich is removed from all tuples.
   * @return Collection of tuples which the node was associated with.
   */
    public static Collection<Tuple> unmarkNodeFromAllTuples(final RXMLTree tree, final Node node) {
        final NodeAttribute nodeAttribute = tree.getNodesMap().get(node);
        final Set<Tuple> result = new HashSet<Tuple>(nodeAttribute.getTuples());
        nodeAttribute.removeFromAllTuples();
        return result;
    }

    /**
   * Traverse the XML tree tuple and add or remove node into/form {@code currentTuple}. If the {@code currentTuple} is added,
   * this means that {@code actualTuple} will be divided into new tuples where each new tuple is represented
   * by one node of {@code tupleCut}(the {@code cuttingNode} is representative for this tuple).
   * @param node Node to be added into {@code currentTuple}
   * @param nodesMap Map holding information for each Node to which tuple belongs to.
   * @param cuttingNode Representative for tuple division. If it is {@code null}, there is no dividing of tuple.
   * @param actualTuple The tuple which is actually traversed. If it is {@code null}, whole xml tree is traversed.
   * @param currentTuple Tuple to which are nodes added or removed from.
   * @param tupleCut List of nodes for which dividing of node is provided.
   * @param remove Flag indicationg if {@code currentTuple} is added or removed.
   */
    private static void traverseTree(final Node node, final Map<Node, NodeAttribute> nodesMap, final Node cuttingNode, final Tuple actualTuple, final Tuple currentTuple, final List<Node> tupleCut, final boolean remove) {
        if (!nodesMap.containsKey(node)) {
            throw new RuntimeException("There must be a reference for all nodes in a tree.");
        }
        if (actualTuple == null || nodesMap.get(node).isInTuple(actualTuple)) {
            boolean wasRemoved = false;
            if (remove) {
                wasRemoved = nodesMap.get(node).removeFromTuple(currentTuple);
            } else {
                if (cuttingNode != null && tupleCut != null && tupleCut.contains(node) && !node.equals(cuttingNode)) {
                    return;
                }
                nodesMap.get(node).addToTuple(currentTuple);
            }
            if (remove && !wasRemoved) {
                return;
            }
            final NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (int j = 0; j < attributes.getLength(); j++) {
                    traverseTree(attributes.item(j), nodesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
                }
            }
            if (node.getNodeType() != Node.ATTRIBUTE_NODE) {
                final NodeList childNodes = node.getChildNodes();
                for (int i = 0; i < childNodes.getLength(); i++) {
                    final Node child = childNodes.item(i);
                    traverseTree(child, nodesMap, cuttingNode, actualTuple, currentTuple, tupleCut, remove);
                }
            }
        }
    }
}
