package com.googlecode.sarasvati.visual.process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import com.googlecode.sarasvati.Arc;
import com.googlecode.sarasvati.ArcToken;
import com.googlecode.sarasvati.Graph;
import com.googlecode.sarasvati.GraphProcess;
import com.googlecode.sarasvati.Node;
import com.googlecode.sarasvati.NodeToken;
import com.googlecode.sarasvati.visual.ProcessLookAndFeel;
import com.googlecode.sarasvati.visual.graph.GraphLayoutTree;

public class ProcessTree {

    protected ProcessLookAndFeel lookAndFeel;

    protected GraphLayoutTree graphTree;

    protected Map<NodeToken, ProcessTreeNode> nodeTokenMap = new HashMap<NodeToken, ProcessTreeNode>();

    protected Map<Node, ProcessTreeNode> nodeMap = new HashMap<Node, ProcessTreeNode>();

    protected List<NodeToken> sortedTokenList = null;

    protected List<ProcessTreeNode> queue = new LinkedList<ProcessTreeNode>();

    private static class ParentTree {

        protected List<List<NodeToken>> tree = new LinkedList<List<NodeToken>>();

        public ParentTree(final NodeToken token) {
            Set<NodeToken> processed = new HashSet<NodeToken>();
            List<NodeToken> nextLayer = new LinkedList<NodeToken>();
            nextLayer.add(token);
            while (!nextLayer.isEmpty()) {
                tree.add(nextLayer);
                List<NodeToken> prevLayer = nextLayer;
                nextLayer = new LinkedList<NodeToken>();
                for (NodeToken ancestor : prevLayer) {
                    for (ArcToken arcToken : ancestor.getParentTokens()) {
                        if (!processed.contains(arcToken.getParentToken())) {
                            nextLayer.add(arcToken.getParentToken());
                            processed.add(arcToken.getParentToken());
                        }
                    }
                }
            }
            tree.remove(0);
        }

        public int getDistance(final ParentTree other) {
            int localDepth = 0;
            for (List<NodeToken> localLayer : tree) {
                int otherDepth = 0;
                for (List<NodeToken> otherLayer : other.tree) {
                    if (!Collections.disjoint(localLayer, otherLayer)) {
                        return localDepth + otherDepth;
                    }
                    otherDepth++;
                }
                localDepth++;
            }
            return Integer.MAX_VALUE;
        }
    }

    protected ProcessTreeNode getProcessTreeNode(final ProcessTreeNode parent, final Node node) {
        NodeToken parentToken = parent.getParentToken();
        NodeToken current = null;
        int currentDistance = 0;
        ParentTree parentTree = null;
        for (NodeToken token : sortedTokenList) {
            if (token.getNode().equals(node)) {
                if (current == null) {
                    current = token;
                } else {
                    if (parentTree == null) {
                        parentTree = new ParentTree(parentToken);
                        currentDistance = parentTree.getDistance(new ParentTree(current));
                    }
                    int distance = parentTree.getDistance(new ParentTree(token));
                    if (distance < currentDistance) {
                        current = token;
                        currentDistance = distance;
                    }
                }
            }
        }
        if (current != null) {
            return nodeTokenMap.get(current);
        }
        return getNonTokenProcessTreeNode(parent, node);
    }

    public ProcessTreeNode getNonTokenProcessTreeNode(final ProcessTreeNode parent, final Node node) {
        ProcessTreeNode endNode = nodeMap.get(node);
        if (endNode == null) {
            endNode = new ProcessTreeNode(parent, node);
            nodeMap.put(node, endNode);
            queue.add(endNode);
        }
        return endNode;
    }

    public boolean isBackArc(final Arc arc) {
        return lookAndFeel.isBackArc(arc, graphTree.isBackArc(arc.getStartNode(), arc.getEndNode()));
    }

    public ProcessTree(final GraphProcess process, final ProcessLookAndFeel lookAndFeel) {
        Graph graph = process.getGraph();
        this.graphTree = new GraphLayoutTree(graph);
        this.lookAndFeel = lookAndFeel;
        for (NodeToken token : process.getNodeTokens()) {
            nodeTokenMap.put(token, new ProcessTreeNode(token));
        }
        sortedTokenList = new ArrayList<NodeToken>(process.getNodeTokens());
        Collections.sort(sortedTokenList, new Comparator<NodeToken>() {

            @Override
            public int compare(final NodeToken o1, final NodeToken o2) {
                return o1.getCreateDate().compareTo(o2.getCreateDate());
            }
        });
        for (NodeToken token : nodeTokenMap.keySet()) {
            ProcessTreeNode childNode = nodeTokenMap.get(token);
            for (ArcToken parentArc : token.getParentTokens()) {
                ProcessTreeNode parentNode = nodeTokenMap.get(parentArc.getParentToken());
                ProcessTreeArc processTreeArc = new ProcessTreeArc(parentArc, parentNode, childNode);
                parentNode.addChild(processTreeArc);
                childNode.addParent(processTreeArc);
            }
        }
        List<ProcessTreeNode> nextLayer = new LinkedList<ProcessTreeNode>();
        List<List<ProcessTreeNode>> layers = new LinkedList<List<ProcessTreeNode>>();
        Set<ProcessTreeNode> processed = new HashSet<ProcessTreeNode>();
        for (ProcessTreeNode ptNode : nodeTokenMap.values()) {
            if (ptNode.isStartTokenNode()) {
                ptNode.setDepth(0);
                ptNode.addToLayer(nextLayer);
                processed.add(ptNode);
            }
        }
        int depth = 1;
        while (!nextLayer.isEmpty()) {
            layers.add(nextLayer);
            List<ProcessTreeNode> prevLayer = nextLayer;
            nextLayer = new LinkedList<ProcessTreeNode>();
            for (ProcessTreeNode treeNode : prevLayer) {
                for (ProcessTreeArc ptArc : treeNode.getChildren()) {
                    if (!processed.contains(ptArc.getChild())) {
                        ProcessTreeNode childNode = ptArc.getChild();
                        boolean allInputsProcessed = true;
                        for (ProcessTreeArc parentArc : childNode.getParents()) {
                            if (!processed.contains(parentArc.getParent()) || nextLayer.contains(parentArc.getParent())) {
                                allInputsProcessed = false;
                                break;
                            }
                        }
                        if (allInputsProcessed) {
                            childNode.setDepth(depth);
                            childNode.addToLayer(nextLayer);
                            processed.add(childNode);
                        }
                    }
                }
            }
            depth++;
        }
        for (ArcToken arcToken : process.getActiveArcTokens()) {
            ProcessTreeNode parentNode = nodeTokenMap.get(arcToken.getParentToken());
            ProcessTreeNode childNode = getNonTokenProcessTreeNode(parentNode, arcToken.getArc().getEndNode());
            ProcessTreeArc ptArc = new ProcessTreeArc(arcToken, parentNode, childNode);
            parentNode.addChild(ptArc);
            childNode.addParent(ptArc);
        }
        for (ProcessTreeNode ptNode : nodeTokenMap.values()) {
            for (Arc arc : graph.getOutputArcs(ptNode.getNode())) {
                if (!ptNode.isTokenOnArc(arc) && !arc.isSelfArc()) {
                    ProcessTreeNode child = ptNode.getToken().isComplete() || isBackArc(arc) ? getProcessTreeNode(ptNode, arc.getEndNode()) : getNonTokenProcessTreeNode(ptNode, arc.getEndNode());
                    ProcessTreeArc arcTokenWrapper = new ProcessTreeArc(arc, ptNode, child);
                    ptNode.addChild(arcTokenWrapper);
                    child.addParent(arcTokenWrapper);
                }
            }
        }
        while (!queue.isEmpty()) {
            ProcessTreeNode ptNode = queue.remove(0);
            for (Arc arc : graph.getOutputArcs(ptNode.getNode())) {
                ProcessTreeNode child = null;
                if (arc.isSelfArc()) {
                    child = ptNode;
                } else if (isBackArc(arc)) {
                    child = getProcessTreeNode(ptNode, arc.getEndNode());
                } else {
                    child = getNonTokenProcessTreeNode(ptNode, arc.getEndNode());
                }
                ProcessTreeArc arcTokenWrapper = new ProcessTreeArc(arc, ptNode, child);
                ptNode.addChild(arcTokenWrapper);
                child.addParent(arcTokenWrapper);
            }
        }
        processed.clear();
        nextLayer = layers.get(0);
        processed.addAll(nextLayer);
        depth = 1;
        while (!nextLayer.isEmpty()) {
            List<ProcessTreeNode> prevLayer = nextLayer;
            nextLayer = layers.size() > depth ? layers.get(depth) : new LinkedList<ProcessTreeNode>();
            for (ProcessTreeNode treeNode : prevLayer) {
                for (ProcessTreeArc ptArc : treeNode.getChildren()) {
                    ProcessTreeNode child = ptArc.getChild();
                    if (processed.contains(child)) {
                        continue;
                    }
                    if (child.getDepth() >= 0) {
                        processed.add(child);
                        continue;
                    }
                    boolean allInputsProcessed = true;
                    for (ProcessTreeArc parentArc : child.getParents()) {
                        if ((!processed.contains(parentArc.getParent()) || nextLayer.contains(parentArc.getParent())) && !isParentAlsoChild(parentArc.getParent(), child)) {
                            allInputsProcessed = false;
                            break;
                        }
                    }
                    if (allInputsProcessed) {
                        child.setDepth(depth);
                        child.addToLayer(nextLayer);
                        processed.add(child);
                    }
                }
            }
            depth++;
        }
    }

    private boolean isParentAlsoChild(final ProcessTreeNode parent, final ProcessTreeNode child) {
        final Set<ProcessTreeNode> processed = new HashSet<ProcessTreeNode>();
        final Queue<ProcessTreeNode> checkQueue = new LinkedList<ProcessTreeNode>();
        processed.add(parent);
        checkQueue.addAll(parent.getNodeParents());
        while (!checkQueue.isEmpty()) {
            ProcessTreeNode node = checkQueue.remove();
            if (node.equals(child)) {
                return true;
            }
            processed.add(node);
            for (ProcessTreeNode input : node.getNodeParents()) {
                if (!processed.contains(input)) {
                    checkQueue.add(input);
                }
            }
        }
        return false;
    }

    public Iterable<ProcessTreeNode> getProcessTreeNodes() {
        ArrayList<ProcessTreeNode> result = new ArrayList<ProcessTreeNode>(nodeTokenMap.size() + nodeMap.size());
        result.addAll(nodeTokenMap.values());
        result.addAll(nodeMap.values());
        return result;
    }
}
