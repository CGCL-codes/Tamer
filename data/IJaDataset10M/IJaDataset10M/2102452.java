package oracle.toplink.essentials.internal.parsing;

import oracle.toplink.essentials.queryframework.DatabaseQuery;
import oracle.toplink.essentials.queryframework.DeleteAllQuery;

/**
 * INTERNAL:
 * DeleteNode is a ModifyNode that represents an DeleteAllQuery
 */
public class DeleteNode extends ModifyNode {

    public boolean isDeleteNode() {
        return true;
    }

    /**
     * INTERNAL
     * Returns a DatabaseQuery instance representing the owning
     * ParseTree. This implementation returns a DeleteAllQuery instance.
     */
    public DatabaseQuery createDatabaseQuery(ParseTreeContext context) {
        DeleteAllQuery query = new DeleteAllQuery();
        query.setShouldDeferExecutionInUOW(false);
        return query;
    }
}
