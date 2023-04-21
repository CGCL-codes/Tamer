package streamcruncher.innards.impl;

import streamcruncher.api.DBName;
import streamcruncher.api.artifact.IndexSpec;
import streamcruncher.innards.core.partition.aggregate.AbstractAggregatedColumnDDLHelper;
import streamcruncher.innards.db.DatabaseInterface;
import streamcruncher.innards.impl.artifact.FirebirdIndexSpec;
import streamcruncher.innards.impl.query.DDLHelper;
import streamcruncher.innards.impl.query.FirebirdDDLHelper;
import streamcruncher.innards.impl.query.FirebirdParser;
import streamcruncher.innards.query.Parser;

public class FirebirdDatabaseInterface extends DatabaseInterface {

    @Override
    public Class<? extends Parser> getParser() {
        return FirebirdParser.class;
    }

    @Override
    public DBName getDBName() {
        return DBName.Firebird;
    }

    @Override
    public AbstractAggregatedColumnDDLHelper getAggregatedColumnDDLHelper() {
        return new FirebirdDDLHelper();
    }

    @Override
    public DDLHelper getDDLHelper() {
        return new FirebirdDDLHelper();
    }

    @Override
    public IndexSpec createIndexSpec(String schema, String name, String tableName, boolean unique, String columnName, boolean ascending) {
        return new FirebirdIndexSpec(schema, name, tableName, unique, columnName, ascending);
    }

    @Override
    public IndexSpec createIndexSpec(String schema, String name, String tableName, boolean unique, String[] columnNames, boolean[] ascending) {
        return new FirebirdIndexSpec(schema, name, tableName, unique, columnNames, ascending);
    }
}
