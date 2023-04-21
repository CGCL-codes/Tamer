package net.charabia.ac.spedia;

import net.charabia.ac.*;
import java.util.*;

public class PathFinder {

    private Database m_base;

    private Hashtable m_entryToNode = new Hashtable();

    private HashSet m_nodes = new HashSet();

    private LinkedList m_edgesToParse = new LinkedList();

    private Node m_startNode;

    private Node m_endNode;

    private class Edge {

        public Node fromNode;

        public Node toNode;

        public double weight;

        public Edge(Node from, Node to, double w) {
            fromNode = from;
            toNode = to;
            weight = w;
        }
    }

    private class Node {

        public Entry entry;

        public Location location;

        public Vector nextNodes = new Vector();

        public Vector prevNodes = new Vector();

        public double shortestWeight = 100000000.0;

        public Vector shortestRoute = new Vector();

        public Node(Entry e) {
            entry = e;
            location = e.Loc;
        }

        public Node(Location l) {
            entry = null;
            location = l;
        }
    }

    public PathFinder(Database db, Location start, Location end) {
        m_base = db;
        int warnings = 0;
        int total = 0;
        int nodecount = 0;
        int edgecount = 0;
        Collection all = db.itemsFor("portal");
        for (Iterator iter = all.iterator(); iter.hasNext(); ) {
            total++;
            Entry e = (Entry) iter.next();
            if ((e.Loc != null) && (e.ExitLocation != null)) {
                Node portal = new Node(e);
                nodecount++;
                m_nodes.add(portal);
                Node exitportal = new Node(e.ExitLocation);
                nodecount++;
                m_nodes.add(exitportal);
                m_entryToNode.put(e, portal);
                Edge edge = new Edge(portal, exitportal, 0.0);
                edgecount++;
                portal.nextNodes.add(edge);
                exitportal.prevNodes.add(portal);
            } else {
                warnings++;
                System.out.println("WARNING: portal " + e.Name + " has no exit !");
            }
        }
        System.out.println("Warnings : " + warnings);
        System.out.println("Total : " + total);
        for (Iterator iter = m_nodes.iterator(); iter.hasNext(); ) {
            Node n = (Node) iter.next();
            if (n.entry == null) {
                for (Iterator destiter = m_nodes.iterator(); destiter.hasNext(); ) {
                    Node dest = (Node) destiter.next();
                    if ((n != dest) && (dest.entry != null)) {
                        Edge edge = new Edge(n, dest, n.location.distanceFrom(dest.location));
                        edgecount++;
                        n.nextNodes.add(edge);
                        dest.prevNodes.add(n);
                    }
                }
            }
        }
        Node startNode = new Node(start);
        nodecount++;
        Node endNode = new Node(end);
        nodecount++;
        Edge directEdge = new Edge(startNode, endNode, start.distanceFrom(end));
        startNode.nextNodes.add(directEdge);
        endNode.prevNodes.add(startNode);
        for (Iterator iter = m_nodes.iterator(); iter.hasNext(); ) {
            Node n = (Node) iter.next();
            if (n.entry != null) {
                Edge edge = new Edge(startNode, n, start.distanceFrom(n.location));
                edgecount++;
                startNode.nextNodes.add(edge);
                n.prevNodes.add(startNode);
            } else {
                Edge edge = new Edge(n, endNode, end.distanceFrom(n.location));
                edgecount++;
                n.nextNodes.add(edge);
                endNode.prevNodes.add(n);
            }
        }
        m_startNode = startNode;
        m_endNode = endNode;
        m_nodes.add(endNode);
        System.out.println("Nodes : " + nodecount);
        System.out.println("Edges : " + edgecount);
    }

    public List findPath() {
        int edgecount = 0;
        m_startNode.shortestWeight = 0.0;
        for (Iterator iter = m_startNode.nextNodes.iterator(); iter.hasNext(); ) {
            Edge e = (Edge) iter.next();
            edgecount += e.toNode.nextNodes.size();
            m_edgesToParse.add(e);
        }
        process();
        System.out.println("Start : " + m_startNode.location);
        System.out.println("End   : " + m_endNode.location);
        System.out.println("DISTANCE : " + m_endNode.shortestWeight);
        LinkedList result = new LinkedList();
        if (m_startNode.entry != null) result.add(m_startNode.entry); else result.add(m_startNode.location);
        for (Iterator i = m_endNode.shortestRoute.iterator(); i.hasNext(); ) {
            Node n = (Node) i.next();
            if (n.entry != null) {
                result.add(n.entry);
                i.next();
            } else {
                result.add(n.location);
            }
        }
        return result;
    }

    private void process() {
        while (m_edgesToParse.size() != 0) {
            Edge edge = (Edge) m_edgesToParse.getFirst();
            m_edgesToParse.removeFirst();
            double count = edge.fromNode.shortestWeight + edge.weight;
            if (count < edge.toNode.shortestWeight) {
                Vector shortest = new Vector();
                shortest.addAll(edge.fromNode.shortestRoute);
                shortest.add(edge.toNode);
                edge.toNode.shortestRoute = shortest;
                edge.toNode.shortestWeight = count;
                m_edgesToParse.addAll(edge.toNode.nextNodes);
            }
            edge.toNode.prevNodes.remove(edge.fromNode);
        }
    }

    public static void main(String[] args) throws Exception {
        PathFinder pt = new PathFinder(Database.getDatabase(), new Location(0.5, 0.5, false), new Location(0.8, 0.3, false));
        pt.findPath();
    }
}
