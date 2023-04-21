package action;

/**
 * RelationTraverser finds or creates entities that are in a particular relation
 * with the input entities.
 * @author Andr�s B�ni
 *
 * @param <T> type of input entities.
 * @param <U> type of the entities produced.
 */
public interface RelationTraverser<T, U> extends EntityContainer<U> {

    /**
     * A short text describing the relation between input and output entities.
     * @return the name of the RelationTraverser.
     */
    String getName();

    /**
     * Retrieves the type of the starting point of the relation.
     * @return the type of input entities.
     */
    Class<T> getInputType();

    /**
     * Sets the EntityContainer that might be asked for input.
     * @param input the input to set.
     */
    void setInput(EntityContainer<T> input);
}
