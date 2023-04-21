package playground.rost.eaflow.ea_flow;

import java.util.Hashtable;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.network.NetworkLayer;

public class Distances {

    /**----------- Fields -------------*/
    final NetworkLayer network;

    private Hashtable<Node, Integer> distLables = new Hashtable<Node, Integer>();

    /**----------- Constructor -------------*/
    public Distances(final NetworkLayer network) {
        this.network = network;
        for (Node node : network.getNodes().values()) {
            distLables.put(node, Integer.MAX_VALUE);
        }
    }

    public Distances(final NetworkLayer network, Node specialNode) {
        this.network = network;
        for (Node node : network.getNodes().values()) {
            if (node == specialNode) {
                distLables.put(node, 0);
            } else {
                distLables.put(node, Integer.MAX_VALUE);
            }
        }
    }

    public boolean setDistance(Node node, int time) {
        if (time > distLables.get(node)) {
            System.out.println("Distances konnten oder sollten nicht gesetzt werden.");
            return false;
        }
        distLables.put(node, time);
        return true;
    }

    public Integer getDistance(Node node) {
        return this.getMinTime(node);
    }

    /**----------- Other Methods -------------*/
    public Integer getMinTime(Node node) {
        return distLables.get(node);
    }

    public boolean isReachable(Node node, int time) {
        if (distLables.get(node) <= time) {
            return true;
        }
        return false;
    }

    /**----------- Print -------------*/
    public void printAll() {
        for (Node n : network.getNodes().values()) {
            print(n);
        }
        System.out.println();
    }

    public void print(Node node) {
        System.out.print("Node " + node.getId() + " ist ");
        if (distLables.get(node).equals(Integer.MAX_VALUE)) {
            System.out.println("nicht erreichbar.");
        } else {
            System.out.println("erreichbar ab Zeitpunkt " + distLables.get(node));
        }
    }

    public void print(int time) {
        System.out.println("Folgende Knoten sint zum Zeitpunkt " + time + " erreichbar:");
        for (Node node : network.getNodes().values()) {
            if (distLables.get(node) == time) {
                System.out.println(node.getId());
            }
        }
        System.out.println();
    }
}
