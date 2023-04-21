package ontorama.model.graph;

/**
 * A tagging interface for NodeTypes.
 * 
 * This interface just gives some type safety for methods mapping NodeTypes to
 * something else. Otherwise NodeTypes can be anything.
 */
public interface NodeType {

    public String getName();
}
