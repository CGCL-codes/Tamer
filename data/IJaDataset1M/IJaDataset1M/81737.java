package com.ibm.tuningfork.infra.stream.expression.builtin;

import com.ibm.tuningfork.infra.feed.FeedGroup;
import com.ibm.tuningfork.infra.stream.expression.base.Expression;

/**
 * A special kind of SymbolDefinition to be implemented by symbols that need awareness of their encompassing
 *   FeedGroup
 */
public interface FeedGroupSymbolDefinition extends SymbolDefinition {

    /**
     * Get the specific expression to use for a FeedGroup
     * @param feedGroup the feed group for which an expression is required
     * @return the Expression to use for this symbol for the requested feed group
     */
    public Expression getDefinitionForFeedGroup(FeedGroup feedGroup);
}
