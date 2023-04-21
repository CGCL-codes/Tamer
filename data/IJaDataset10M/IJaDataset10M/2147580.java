package com.emental.mindraider.graph.spiders;

/**
 * Node descriptor. Check SpidersGraph.render(); for details on id/label/uri differences.
 * 
 * @author Martin.Dvorak
 */
public class NodeDescriptor {

    public String id;

    public String label;

    public String uri;

    /**
     * Constructor.
     */
    public NodeDescriptor() {
    }

    /**
     * Constructor.
     * 
     * @param id
     * @param label
     * @param uri
     */
    public NodeDescriptor(String id, String label, String uri) {
        this.id = id;
        this.label = label;
        this.uri = uri;
    }
}
