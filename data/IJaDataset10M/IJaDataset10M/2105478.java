package uk.co.pointofcare.echobase.neoutils.rendering.interfaces;

import org.neo4j.graphdb.PropertyContainer;

public interface NeoPathQueryContainer<IN extends PropertyContainer, OUT extends PropertyContainer> extends NeoSingleInput<IN>, NeoMultiOutput<OUT> {

    /** This method allows for the adding of subcontainers or widgets to this container for display of a result set
	 * or for iteration through a result set. 
	 * @param subContainerOrWidget
	 * @return returns this class for subsequent modification or decoration by other widgets & containers
	 */
    public NeoPathQueryContainer<IN, OUT> add(NeoMultiInput<OUT> subContainerOrWidget);

    /** This method will modify the output of the path query by incorporating the output of the other query container.
	 * If the other query container is a clone of the original then the subjects will be 
	 * @param queryContainer
	 * @return
	 */
    public NeoPathQueryContainer<IN, OUT> union(NeoPathQueryContainer<? extends PropertyContainer, OUT> queryContainer);

    public NeoPathQueryContainer<IN, OUT> intersection(NeoPathQueryContainer<? extends PropertyContainer, OUT> queryContainer);

    public boolean isAncestorOfSelf(IN testSubject);

    public void setMaxRecursionDepth(int depth);
}
