package streamcruncher.innards.impl;

import java.util.Properties;
import streamcruncher.api.DBName;
import streamcruncher.api.artifact.IndexSpec;
import streamcruncher.innards.core.partition.aggregate.AbstractAggregatedColumnDDLHelper;
import streamcruncher.innards.db.DatabaseInterface;
import streamcruncher.innards.impl.artifact.PointBaseIndexSpec;
import streamcruncher.innards.impl.query.DDLHelper;
import streamcruncher.innards.impl.query.PointBaseDDLHelper;
import streamcruncher.innards.impl.query.PointBaseParser;
import streamcruncher.innards.query.Parser;

public class PointBaseDatabaseInterface extends DatabaseInterface {

    @Override
    public void start(Object... params) throws Exception {
        Properties props = (Properties) params[0];
        Properties sysProps = System.getProperties();
        sysProps.put("pointbase.ini", props.getProperty("db.pointbase.ini"));
        super.start(params);
    }

    @Override
    public Class<? extends Parser> getParser() {
        return PointBaseParser.class;
    }

    @Override
    public DBName getDBName() {
        return DBName.PointBase;
    }

    @Override
    public AbstractAggregatedColumnDDLHelper getAggregatedColumnDDLHelper() {
        return new PointBaseDDLHelper();
    }

    @Override
    public DDLHelper getDDLHelper() {
        return new PointBaseDDLHelper();
    }

    @Override
    public IndexSpec createIndexSpec(String schema, String name, String tableName, boolean unique, String columnName, boolean ascending) {
        return new PointBaseIndexSpec(schema, name, tableName, unique, columnName, ascending);
    }

    @Override
    public IndexSpec createIndexSpec(String schema, String name, String tableName, boolean unique, String[] columnNames, boolean[] ascending) {
        return new PointBaseIndexSpec(schema, name, tableName, unique, columnNames, ascending);
    }
}
