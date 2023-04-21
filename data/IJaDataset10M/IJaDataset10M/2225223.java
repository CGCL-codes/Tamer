package com.mm.apps.airdb.sql;

import java.sql.Connection;

public class SqlAdapter extends SqlAdapterAbstract {

    protected SqlAdapter(Connection conn) {
        super(conn);
    }

    public String execute(IQuery query) {
        return null;
    }
}
