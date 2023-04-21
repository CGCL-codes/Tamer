package edu.uci.ics.jung.io.graphml;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata structure for the 'node' GraphML element.
 *
 * @author Nathan Mittler - nathan.mittler@gmail.com
 * 
 * @see "http://graphml.graphdrawing.org/specification.html"
 */
public class NodeMetadata extends AbstractMetadata {

    private String id;

    private String description;

    private Object vertex;

    private final List<PortMetadata> ports = new ArrayList<PortMetadata>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void addPort(PortMetadata port) {
        ports.add(port);
    }

    public List<PortMetadata> getPorts() {
        return ports;
    }

    public Object getVertex() {
        return vertex;
    }

    public void setVertex(Object vertex) {
        this.vertex = vertex;
    }

    public MetadataType getMetadataType() {
        return MetadataType.NODE;
    }
}
