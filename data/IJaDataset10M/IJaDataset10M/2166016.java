package com.pv.mf.db.mock.entity;

import com.pv.mf.db.mock.IMockController;
import com.pv.mf.db.mock.lang.IQuery;
import com.pv.mf.db.mock.lang.IQueryBuilder;
import com.pv.mf.db.mock.statement.MockQuery;

public interface IEntityHandler {

    public enum SqlMode {

        SELECT, DELETE, UPDATE, INSERT
    }

    public IQuery generateQuery(MockQuery query, SqlMode mode);

    public int executeQuery(IMockController controller, IQuery query, SqlMode mode);

    public IQueryBuilder getQueryBuilder();
}
