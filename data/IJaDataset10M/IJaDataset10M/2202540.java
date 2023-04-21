package maze.commons.generic;

/**
 * 
 * @author Normunds Mazurs
 */
public interface Node<C> {

    Iterable<Leaf<C>> getLeaves();
}
