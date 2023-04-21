package olr.statementpool;

import java.util.List;

public interface OlrStatementPool extends DbStatementPool {

    public abstract List getInstanciableClassesForRange(List range);

    public abstract List getResourcesHavingKeyword(List keywords);
}
