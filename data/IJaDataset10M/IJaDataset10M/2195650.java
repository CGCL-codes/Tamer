package com.objectsql.common;

import com.objectsql.utility.StringUtil;
import java.util.ArrayList;
import java.util.List;

public class StatementCollection extends AbstractStatementEmptyParameters {

    private final List<Statement> statements = new ArrayList<Statement>();

    private final String statementSeparator;

    public StatementCollection(String statementSeparator) {
        this.statementSeparator = statementSeparator;
    }

    public String sql() {
        if (statements.isEmpty()) {
            return "";
        }
        return StringUtil.concat(statements, statementSeparator);
    }

    public void add(Statement statement) {
        this.statements.add(statement);
    }
}
