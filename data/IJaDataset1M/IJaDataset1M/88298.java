package com.reactiveplot.library.editor.nodes;

import java.util.ArrayList;
import com.reactiveplot.library.events.InterpreterEvent;

public class Node implements Comparable<Node> {

    ArrayList<Node> inputs = new ArrayList<Node>();

    ArrayList<Node> outputs = null;

    ArrayList<Node> backLinks = null;

    int greatestDistanceFromStart = -1;

    boolean isFirstNode = false;

    String[] labelLines;

    public ArrayList<Node> getInputs() {
        return inputs;
    }

    public ArrayList<Node> getForwardLinks() {
        return outputs;
    }

    public ArrayList<Node> getBackLinks() {
        return backLinks;
    }

    public ArrayList<Node> getAllOutputs() {
        ArrayList<Node> all = new ArrayList<Node>();
        all.addAll(outputs);
        all.addAll(backLinks);
        return all;
    }

    public boolean hasBackLinkTo(Node other) {
        if (backLinks == null) return false;
        for (Node n : backLinks) {
            if (n.equals(other)) return true;
        }
        return false;
    }

    public void removeLinksUnlessListed(ArrayList<Node> allowedNodes) {
        for (int i = 0; i < outputs.size(); i++) {
            Node n = outputs.get(i);
            if (!allowedNodes.contains(n)) outputs.remove(n);
        }
        for (int i = 0; i < inputs.size(); i++) {
            Node n = inputs.get(i);
            if (!allowedNodes.contains(n)) inputs.remove(n);
        }
    }

    public void splitLabel(int maxNumberOfLinesHigh, int numCharactersPerLabelLine) {
        String s = getLabel();
        if (s == null) s = "";
        int numberOfLinesHigh = (s.length() / numCharactersPerLabelLine) + 1;
        labelLines = new String[numberOfLinesHigh];
        for (int i = 0; i < numberOfLinesHigh; i++) {
            int length = s.length();
            if (length > numCharactersPerLabelLine) length = numCharactersPerLabelLine;
            labelLines[i] = s.substring(0, length);
            s = s.substring(length);
        }
    }

    public int getNumberOfLinesHigh() {
        return labelLines.length;
    }

    public String[] getLabelLines() {
        return labelLines;
    }

    public String getLabel() {
        throw new Error("should never display a Node class");
    }

    public void addInput(Node node) {
        inputs.add(node);
    }

    public void resetGreatestDistanceFromStart() {
        greatestDistanceFromStart = -1;
    }

    public int getGreatestDistanceFromStart() {
        if (greatestDistanceFromStart != -1) return greatestDistanceFromStart;
        if (isFirstNode()) {
            greatestDistanceFromStart = 0;
            return 0;
        }
        for (Node n : inputs) {
            if (n.hasBackLinkTo(this)) continue;
            int distance = n.getGreatestDistanceFromStart();
            if (distance > greatestDistanceFromStart) greatestDistanceFromStart = distance;
        }
        if (greatestDistanceFromStart == -1) {
            return 0;
        }
        greatestDistanceFromStart++;
        return greatestDistanceFromStart;
    }

    boolean isLessThan(Node node) {
        if (node == null) return true;
        if (getGreatestDistanceFromStart() < node.getGreatestDistanceFromStart()) return true; else return false;
    }

    public int compareTo(Node node) {
        int myDistance = getGreatestDistanceFromStart();
        int nodesDistance = node.getGreatestDistanceFromStart();
        if (myDistance < nodesDistance) return -1; else if (myDistance > nodesDistance) return 1; else return 0;
    }

    public boolean isFirstNode() {
        return isFirstNode;
    }

    public void setFirstNode(boolean set) {
        isFirstNode = set;
    }

    public ArrayList<Node> internal_addToOutputLinks(ArrayList<SegmentNode> allSegmentNodes, ArrayList<EndingNode> allEndings) {
        return null;
    }

    public ArrayList<Node> generateOutputLinks(ArrayList<SegmentNode> allSegmentNodes, ArrayList<EndingNode> allEndings) {
        if (outputs != null) throw new Error("outputs already set");
        outputs = new ArrayList<Node>();
        ArrayList<Node> newNodes = internal_addToOutputLinks(allSegmentNodes, allEndings);
        for (Node n : outputs) {
            if (n == null) throw new Error("null output encountered on node " + getLabel());
            n.addInput(this);
        }
        return newNodes;
    }

    public void setBacklinks() {
        if (backLinks != null) throw new Error("backlinks already set");
        backLinks = new ArrayList<Node>();
        Node latestInput = null;
        for (Node n : inputs) {
            if ((latestInput == null) || (!n.isLessThan(latestInput))) latestInput = n;
        }
        for (int i = 0; i < outputs.size(); i++) {
            Node n = outputs.get(i);
            if ((latestInput != null) && n.isLessThan(latestInput)) {
                outputs.remove(n);
                backLinks.add(n);
                i--;
            }
        }
    }

    public InterpreterEvent getEventFromNode() {
        throw new Error("oops, attempted to find an Event from a raw Node class");
    }

    public Node() {
    }

    public ArrayList<LinkNode> createBackLinkLinkNodes() {
        if (this.backLinks == null) throw new Error("backlinks == null");
        ArrayList<LinkNode> linkNodes = new ArrayList<LinkNode>();
        for (int i = 0; i < this.backLinks.size(); i++) {
            Node output = this.backLinks.get(i);
            if (output instanceof LinkNode) {
                linkNodes.addAll(output.createBackLinkLinkNodes());
            } else if (output.isLessThan(this)) {
                LinkNode linkNode = new LinkNode(this, output, true);
                output.inputs.remove(this);
                output.inputs.add(linkNode);
                this.backLinks.remove(output);
                this.backLinks.add(linkNode);
                linkNodes.add(linkNode);
                linkNodes.addAll(linkNode.createBackLinkLinkNodes());
            }
        }
        return linkNodes;
    }

    public ArrayList<LinkNode> createOutputLinkNodes() {
        if (this.outputs == null) throw new Error("outputs == null");
        ArrayList<LinkNode> linkNodes = new ArrayList<LinkNode>();
        for (int i = 0; i < this.outputs.size(); i++) {
            Node output = this.outputs.get(i);
            if (output instanceof LinkNode) {
                linkNodes.addAll(output.createBackLinkLinkNodes());
            } else if (this.getGreatestDistanceFromStart() < (output.getGreatestDistanceFromStart() - 1)) {
                LinkNode linkNode = new LinkNode(this, output, false);
                output.inputs.remove(this);
                output.inputs.add(linkNode);
                this.outputs.remove(output);
                this.outputs.add(linkNode);
                linkNodes.add(linkNode);
                linkNodes.addAll(linkNode.createOutputLinkNodes());
            }
        }
        return linkNodes;
    }

    public void setNextNodeOnRoute(Node next) {
        throw new Error("oops, should only call this in a derived class");
    }

    public Node getNextNodeOnRoute() {
        throw new Error("oops, should only call this in a derived class");
    }

    public ArrayList<Node> getRouteStartingHere() {
        ArrayList<Node> route = new ArrayList<Node>();
        Node next = this;
        while (next != null) {
            route.add(next);
            next = next.getNextNodeOnRoute();
        }
        return route;
    }
}
