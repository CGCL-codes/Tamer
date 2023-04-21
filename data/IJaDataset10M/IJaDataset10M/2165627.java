package org.thechiselgroup.choosel.visualization_component.graph.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.thechiselgroup.choosel.core.client.resources.Resource;

/**
 * @deprecated Use {@link ResourceNeighbourhood} instead.
 */
@Deprecated
public class NeighbourhoodServiceResult implements Serializable {

    private static final long serialVersionUID = 3287669903192891528L;

    private HashSet<Resource> neighbours = new HashSet<Resource>();

    private ArrayList<Relationship> relationships = new ArrayList<Relationship>();

    private Resource resource;

    private NeighbourhoodServiceResult() {
    }

    public NeighbourhoodServiceResult(Resource resource) {
        assert resource != null;
        this.resource = resource;
    }

    private void addNeighbour(Resource resource) {
        assert resource != null;
        if (resource.equals(this.resource)) {
            return;
        }
        neighbours.add(resource);
    }

    public Relationship addRelationship(Resource source, Resource target) {
        addNeighbour(source);
        addNeighbour(target);
        Relationship r = new Relationship(source, target);
        relationships.add(r);
        return r;
    }

    public Set<Resource> getNeighbours() {
        return neighbours;
    }

    public List<Relationship> getRelationships() {
        return relationships;
    }

    public Resource getResource() {
        return resource;
    }
}
