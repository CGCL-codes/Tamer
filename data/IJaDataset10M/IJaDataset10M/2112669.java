package fi.arcusys.acj.util.search;

/**
 * A <em>visitor</em> interface for {@link SearchExpression} visitors.
 * 
 * <p>See <a href="http://en.wikipedia.org/wiki/Visitor_pattern">
 * Visitor pattern</a> for more details about the <em>visitor</em> 
 * pattern.</p>
 * 
 * @author mikko
 * @version 1.0 $Rev: 474 $
 * 
 */
public interface SearchExpressionVisitor {

    /**
	 * Visit a {@link AbstractSearchExpression} object.
	 * @param expression the object to visit
	 */
    Object visit(SearchExpression expression);
}
