package com.reactiveplot.library.editor.views;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.reactiveplot.library.editor.nodes.CloseUpSegmentNode;
import com.reactiveplot.library.editor.nodes.DecisionNode;
import com.reactiveplot.library.editor.nodes.EndingNode;
import com.reactiveplot.library.editor.nodes.LinkNode;
import com.reactiveplot.library.editor.nodes.LogicNode;
import com.reactiveplot.library.editor.nodes.Node;
import com.reactiveplot.library.editor.nodes.SegmentNode;
import com.reactiveplot.library.editor.nodes.SequenceNode;
import com.reactiveplot.library.events.InterpreterEvent;
import com.reactiveplot.library.events.ReturnEvent;
import com.reactiveplot.library.scripts.Script;
import com.reactiveplot.library.scripts.Segment;

public class EverythingZoomNodeList implements NodeList {

    ArrayList<SegmentNode> allSegmentNodes = new ArrayList<SegmentNode>();

    ArrayList<EndingNode> allEndingNodes = null;

    ArrayList<Node> allNodes = new ArrayList<Node>();

    SegmentNode start = null;

    @SuppressWarnings({ "unchecked" })
    public EverythingZoomNodeList(Script script, boolean showEndings, boolean createLanes) {
        ArrayList<Segment> segments = script.segments;
        ArrayList<ReturnEvent> returnEvents = new ArrayList<ReturnEvent>();
        for (Segment s : segments) {
            allSegmentNodes.add(new CloseUpSegmentNode(s));
            returnEvents.addAll(s.getEndings());
        }
        allEndingNodes = EndingNode.createUniqueEndingsList(returnEvents);
        allNodes.addAll(allSegmentNodes);
        if (showEndings) allNodes.addAll(allEndingNodes);
        for (SegmentNode n : allSegmentNodes) {
            if ("start".equals(n.getID())) {
                start = n;
                n.setFirstNode(true);
                break;
            }
        }
        ArrayList<Node> currentNodes = (ArrayList<Node>) allNodes.clone();
        for (Node n : currentNodes) {
            ArrayList<Node> newNodes = n.generateOutputLinks(allSegmentNodes, allEndingNodes);
            generateOutputLinksForNewNodes(newNodes);
        }
        if (start == null) throw new Error("couldn't find start segment");
        for (Node n : allNodes) n.setBacklinks();
        if (createLanes) {
            ArrayList<LinkNode> linkNodes = new ArrayList<LinkNode>();
            for (Node n : allNodes) linkNodes.addAll(n.createBackLinkLinkNodes());
            for (Node n : allNodes) linkNodes.addAll(n.createOutputLinkNodes());
            allNodes.addAll(linkNodes);
            for (Node n : allNodes) n.resetGreatestDistanceFromStart();
        }
        Collections.sort(allNodes);
    }

    void generateOutputLinksForNewNodes(ArrayList<Node> newNodes) {
        if ((newNodes == null) || newNodes.isEmpty()) return;
        allNodes.addAll(newNodes);
        for (Node n : newNodes) {
            ArrayList<Node> more = n.generateOutputLinks(allSegmentNodes, allEndingNodes);
            generateOutputLinksForNewNodes(more);
        }
    }

    @Override
    public Node get(int index) {
        return allNodes.get(index);
    }

    @Override
    public int size() {
        return allNodes.size();
    }

    @Override
    public Node findNodeFor(InterpreterEvent event) {
        for (Node n : allNodes) {
            if (n instanceof SegmentNode) {
            } else if (n instanceof DecisionNode) {
                DecisionNode d = (DecisionNode) n;
                if (d.getSpeechOptionEvent().equals(event)) return d;
            } else if (n instanceof EndingNode) {
                EndingNode e = (EndingNode) n;
                if (e.getReturnEvent().equals(event)) return e;
            } else if (n instanceof LogicNode) {
                LogicNode l = (LogicNode) n;
                if (l.getIfEvent().equals(event)) return l;
            } else if (n instanceof SequenceNode) {
                SequenceNode s = (SequenceNode) n;
                List<InterpreterEvent> events = s.getEvents();
                for (InterpreterEvent e : events) {
                    if (e.equals(event)) return s;
                }
            }
        }
        throw new Error("couldn't find node for " + event);
    }

    @Override
    public Node getStartNode() {
        return start;
    }
}
