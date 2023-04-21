package de.ifgi.simcat.reasoner;

import java.util.ArrayList;
import de.ifgi.simcat.server.ServerConstants;

public class RoleTaxonomy {

    private ArrayList<Tuple> nodes;

    protected RoleTaxonomyNode TOP_NODE;

    public RoleTaxonomy() {
        nodes = new ArrayList<Tuple>();
        TOP_NODE = createTOPNode();
    }

    public RoleTaxonomyNode createTOPNode() {
        RoleTaxonomyNode node;
        Role topRole;
        if (TOP_NODE == null) {
            topRole = new Role(ServerConstants.TOP);
            node = new RoleTaxonomyNode(topRole);
        } else {
            return TOP_NODE;
        }
        nodes.add(new Tuple(topRole, node));
        return node;
    }

    public RoleTaxonomyNode addNode(Role role) {
        for (Tuple tuple : nodes) {
            if (((Role) tuple.getFirstElement()).getName().equals(role.getName())) {
                return (RoleTaxonomyNode) tuple.getSecondElement();
            }
        }
        RoleTaxonomyNode node = new RoleTaxonomyNode(role, TOP_NODE);
        nodes.add(new Tuple(role, node));
        return node;
    }

    public boolean containsNode(Role role) {
        for (Tuple tuple : nodes) {
            if (((Role) tuple.getFirstElement()).getName().equals(role.getName())) {
                return true;
            }
        }
        return false;
    }

    public RoleTaxonomyNode getNode(Role role) {
        for (Tuple tuple : nodes) {
            if (((Role) tuple.getFirstElement()).getName().equals(role.getName())) {
                return (RoleTaxonomyNode) tuple.getSecondElement();
            }
        }
        return addNode(role);
    }

    public RoleTaxonomyNode getTop() {
        return TOP_NODE;
    }

    public String toString() {
        return nodes.toString();
    }

    public ArrayList<Tuple> getNodes() {
        return nodes;
    }
}
