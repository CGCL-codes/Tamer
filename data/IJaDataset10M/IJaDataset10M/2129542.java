package org.exist.xquery;

import java.io.Writer;
import org.exist.security.xacml.XACMLSource;
import org.exist.xquery.value.Sequence;
import org.xmldb.api.base.CompiledExpression;

/**
 * @author wolf
 */
public interface CompiledXQuery extends CompiledExpression {

    /**
     * Reset the compiled expression tree. Discard all
     * temporary expression results.
     */
    public void reset();

    /**
     * @return the {@link XQueryContext} used to create this query
     */
    public XQueryContext getContext();

    public void setContext(XQueryContext context);

    /**
     * Execute the compiled query, optionally using the specified
     * sequence as context.
     * 
     * @param contextSequence
     * @throws XPathException
     */
    public Sequence eval(Sequence contextSequence) throws XPathException;

    /**
     * Is the compiled expression still valid? Returns false if, for example,
     * the source code of one of the imported modules has changed.
     */
    public boolean isValid();

    /**
     * Writes a diagnostic dump of the expression structure to the
     * specified writer.
     */
    public void dump(Writer writer);

    /**
     * Gets the source of this query.
     *
     * @return This query's source
     */
    public XACMLSource getSource();
}
