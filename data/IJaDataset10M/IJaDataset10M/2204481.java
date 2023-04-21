package com.mchange.v2.c3p0.impl;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

final class NullStatementSetManagedResultSet extends SetManagedResultSet {

    NullStatementSetManagedResultSet(Set activeResultSets) {
        super(activeResultSets);
    }

    NullStatementSetManagedResultSet(ResultSet inner, Set activeResultSets) {
        super(inner, activeResultSets);
    }

    public Statement getStatement() {
        return null;
    }
}
