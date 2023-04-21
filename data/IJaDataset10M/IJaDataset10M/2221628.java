package at.ac.tuwien.j3dvnaddoncollection.layout;

import java.util.ArrayList;
import java.util.Collection;
import at.ac.tuwien.j3dvn.model.Relation;
import at.ac.tuwien.j3dvn.view.LayoutAddon;
import at.ac.tuwien.j3dvn.view.Position;

/**
 *
 */
public class Random implements LayoutAddon {

    private static final String NAME = "Random Layout Addon";

    private final Collection<Position> nodes = new ArrayList<Position>();

    public Random() {
        super();
    }

    public void addNode(Position node) {
        nodes.add(node);
    }

    public String getName() {
        return NAME;
    }

    public void calculate() {
        java.util.Random random = new java.util.Random();
        for (Position node : nodes) {
            {
                node.x = 10 * (random.nextDouble() - .5);
                node.y = 10 * (random.nextDouble() - .5);
                node.z = 10 * (random.nextDouble() - .5);
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                }
            }
        }
    }

    public Collection<Position> getNodes() {
        return nodes;
    }

    public void removeAllNodes() {
        nodes.clear();
    }

    public boolean removeNode(Position node) {
        return nodes.remove(node);
    }

    public Class<Relation> getEdgeType() {
        return null;
    }

    public void setEdgeType(Class<Relation> edgeType) {
    }
}
