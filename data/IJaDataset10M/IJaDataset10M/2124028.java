package org.streets.eis.ext.analysis.internal.impl;

import org.slf4j.Logger;
import org.streets.database.SQLConnection;
import org.streets.database.datadict.DataDict;
import org.streets.database.datadict.TableRelations;
import org.streets.eis.entity.User;
import org.streets.eis.ext.analysis.entities.QueryStruct;
import org.streets.eis.ext.analysis.internal.SqlExtender;

public class SqlExtenderImpl implements SqlExtender {

    private static final long serialVersionUID = 1L;

    protected DataDict _dict;

    private TableRelations _relations;

    private Logger _logger;

    protected final SQLConnection _connection;

    public SqlExtenderImpl(Logger logger, DataDict dict, TableRelations relations, SQLConnection connection) {
        this._dict = dict;
        this._relations = relations;
        this._logger = logger;
        this._connection = connection;
    }

    public QueryStruct extend(User user, QueryStruct struct) {
        return null;
    }
}
