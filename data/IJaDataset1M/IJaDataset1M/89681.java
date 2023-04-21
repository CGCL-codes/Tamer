package olr.statementpool;

import olr.om.Model;

public final class StatementPoolFactory {

    private StatementPoolFactory() {
    }

    public static OlrStatementPoolImpl newInstance(Model model) {
        return new OlrStatementPoolImpl(model);
    }
}
