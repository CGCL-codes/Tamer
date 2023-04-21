package com.iandallas.highlighter;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Node;

class XMLNodeSearcher {

    private String text;

    private Node[] nodes;

    private NodeWrapper[] nodeWrappers;

    private HighlightListener[] listeners;

    private HighlightList highlights;

    protected XMLNodeSearcher(Node[] nodes, boolean isSpaceInsertedBetweenElements, boolean isNodeTextConvertedToLowerCase) {
        this.nodes = nodes;
        this.nodeWrappers = new NodeWrapper[nodes.length];
        this.highlights = new HighlightList();
        StringBuffer buffer = new StringBuffer();
        int bufferLength = 0;
        for (int i = 0; i < nodes.length; i++) {
            Node node = nodes[i];
            String nodeText = node.getNodeValue();
            if (nodeText != null) {
                int nodeTextLength = nodeText.length();
                if (nodeTextLength > 0) {
                    if (isSpaceInsertedBetweenElements) {
                        buffer.append(" ");
                        bufferLength++;
                    }
                    buffer.append(nodeText);
                    int bufferStart = bufferLength;
                    bufferLength += nodeTextLength;
                    nodeWrappers[i] = new NodeWrapper(node, bufferStart, bufferLength);
                }
            }
        }
        text = buffer.toString();
        if (isNodeTextConvertedToLowerCase) {
            text = text.toLowerCase();
        }
    }

    protected void findMatches(Pattern pattern) {
        Matcher matcher = pattern.matcher(this.text);
        while (matcher.find()) {
            highlights.addHighlight(matcher.start(), matcher.end(), pattern);
        }
    }

    protected void applyHighlights(boolean areOverlappingHighlightsCombined) {
        Highlight[] sortedHighlights = highlights.sortHighlights(areOverlappingHighlightsCombined);
        for (int i = 0; i < sortedHighlights.length; i++) {
            Highlight h = sortedHighlights[i];
            this.notifyHighlighters(h.getStart(), h.getEnd(), h.getPatterns());
        }
    }

    private void notifyHighlighters(int start, int end, Pattern[] patterns) {
        NodeWrapper[] wrappers = this.getNodeWrapper(start, end);
        if (wrappers.length == 1) {
            Node node = wrappers[0].getNode();
            int nodeIndexStart = wrappers[0].bufferIndexToNodeIndex(start);
            int nodeIndexEnd = wrappers[0].bufferIndexToNodeIndex(end);
            this.notifyHighlighters(node, patterns, nodeIndexStart, nodeIndexEnd);
        } else {
            Node endNode = wrappers[wrappers.length - 1].getNode();
            int endNodeEnd = wrappers[wrappers.length - 1].bufferIndexToNodeIndex(end);
            this.notifyHighlighters(endNode, patterns, 0, endNodeEnd);
            for (int i = wrappers.length - 1; i >= 0; i--) {
                Node node = wrappers[i].getNode();
                int nodeEnd = wrappers[i].bufferIndexToNodeIndex(end);
                this.notifyHighlighters(node, patterns, 0, nodeEnd);
            }
            Node startNode = wrappers[wrappers.length - 1].getNode();
            int startNodeStart = wrappers[wrappers.length - 1].getBufferStart();
            this.notifyHighlighters(startNode, patterns, startNodeStart, startNode.getNodeValue().length());
        }
    }

    private void notifyHighlighters(Node node, Pattern[] patterns, int start, int end) {
        for (int i = 0; i < listeners.length; i++) {
            listeners[i].highlight(new HighlightEvent(start, end, node, patterns));
        }
    }

    protected void setHighlightListeners(HashSet listenersHashSet) {
        this.listeners = new HighlightListener[listenersHashSet.size()];
        listenersHashSet.toArray(listeners);
    }

    private NodeWrapper[] getNodeWrapper(int start, int end) {
        NodeWrapper startNodeWrapper = this.getNodeWrapper(start);
        NodeWrapper endNodeWrapper = this.getNodeWrapper(end);
        Node startNode = startNodeWrapper.getNode();
        Node endNode = endNodeWrapper.getNode();
        if (startNode == endNode) {
            NodeWrapper[] array = new NodeWrapper[1];
            array[0] = startNodeWrapper;
            return array;
        } else {
            int startNodeIndex = java.util.Arrays.binarySearch(nodes, startNode);
            int endNodeIndex = java.util.Arrays.binarySearch(nodes, endNode);
            int nodeCount = endNodeIndex - startNodeIndex + 1;
            NodeWrapper[] array = new NodeWrapper[nodeCount];
            array[array.length - 1] = endNodeWrapper;
            array[0] = startNodeWrapper;
            for (int i = nodeCount - 1; i > 0; i--) {
                int nodeIndex = i + startNodeIndex;
                array[i] = getNodeWrapper(i);
            }
            return array;
        }
    }

    private NodeWrapper getNodeWrapper(int bufferIndex) {
        int above = 0;
        int below = nodeWrappers.length;
        int previousIndex = 0;
        int currentIndex = 1;
        while (previousIndex != currentIndex) {
            previousIndex = currentIndex;
            currentIndex = (int) Math.ceil(((below - above) / 2) + above);
            NodeWrapper wrapper = nodeWrappers[currentIndex];
            if (wrapper.getBufferEnd() < bufferIndex) {
                above = currentIndex;
                continue;
            } else if (wrapper.getBufferStart() > bufferIndex) {
                below = currentIndex;
                continue;
            } else {
                return wrapper;
            }
        }
        throw new java.lang.IllegalArgumentException("Unable to locate node for bufferIndex = " + bufferIndex);
    }

    private static int estimateBufferCapacity(Node[] nodes) {
        int tenth = (int) Math.floor(nodes.length / 10);
        int estimate = 0;
        for (int i = 0; i < 10; i++) {
            Node node = nodes[tenth * i];
            String value = node.getNodeValue();
            if (value != null) {
                estimate += value.length();
            }
        }
        int capacity = (int) (nodes.length * (estimate / 10));
        return capacity;
    }
}

class NodeWrapper {

    private int bufferStart;

    private int bufferEnd;

    private Node node;

    public NodeWrapper(Node node, int bufferStart, int bufferEnd) {
        this.node = node;
        this.bufferStart = bufferStart;
        this.bufferEnd = bufferEnd;
    }

    public boolean isBufferIndexWithinThisNode(int index) {
        if (index >= bufferStart && index <= bufferEnd) {
            return true;
        } else {
            return false;
        }
    }

    public Node getNode() {
        return node;
    }

    public int getBufferStart() {
        return this.bufferStart;
    }

    public int getBufferEnd() {
        return this.bufferEnd;
    }

    public int bufferIndexToNodeIndex(int bufferIndex) {
        return bufferIndex - bufferStart;
    }

    public String toString() {
        return "(" + bufferStart + " - " + bufferEnd + ") " + node.getNodeValue();
    }
}
