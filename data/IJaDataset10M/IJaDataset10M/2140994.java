package nl.utwente.ewi.portunes.model.algorithm.noderepresentation;

import groove.graph.Node;

/**
* The NodeRepresentation is used to pass on up to two nodes with a certain type.
* These types are specified in the implementing classes.
*/
public interface NodeRepresentation {

    public Node getFirstNode();

    public Node getSecondNode();

    public byte getType();

    public String toString();
}
