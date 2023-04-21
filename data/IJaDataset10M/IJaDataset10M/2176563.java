package net.java.amateras.db.dialect;

import java.sql.Types;
import net.java.amateras.db.Messages;
import net.java.amateras.db.visual.model.ColumnModel;
import net.java.amateras.db.visual.model.RootModel;
import net.java.amateras.db.visual.model.TableModel;

public class H2Dialect extends AbstractDialect {

    private static final IColumnType[] COLUMN_TYPES = { new ColumnType("INT", Messages.getResourceString("type.integer"), false, Types.INTEGER), new ColumnType("INTEGER", Messages.getResourceString("type.integer"), false, Types.INTEGER), new ColumnType("BOOLEAN", Messages.getResourceString("type.boolean"), false, Types.BOOLEAN), new ColumnType("BIT", Messages.getResourceString("type.boolean"), false, Types.BOOLEAN), new ColumnType("BOOL", Messages.getResourceString("type.boolean"), false, Types.BOOLEAN), new ColumnType("TINYINT", Messages.getResourceString("type.integer"), false, Types.TINYINT), new ColumnType("BIGINT", Messages.getResourceString("type.integer"), false, Types.BIGINT), new ColumnType("DECIMAL", Messages.getResourceString("type.numeric"), true, Types.DECIMAL), new ColumnType("NUMBER", Messages.getResourceString("type.numeric"), true, Types.DECIMAL), new ColumnType("NUMERIC", Messages.getResourceString("type.numeric"), true, Types.DECIMAL), new ColumnType("DOUBLE", Messages.getResourceString("type.real"), false, Types.DOUBLE), new ColumnType("FLOAT", Messages.getResourceString("type.real"), false, Types.DOUBLE), new ColumnType("REAL", Messages.getResourceString("type.real"), false, Types.REAL), new ColumnType("TIME", Messages.getResourceString("type.time"), false, Types.TIME), new ColumnType("DATE", Messages.getResourceString("type.date"), false, Types.DATE), new ColumnType("TIMESTAMP", Messages.getResourceString("type.datetime"), false, Types.TIMESTAMP), new ColumnType("DATETIME", Messages.getResourceString("type.datetime"), false, Types.TIMESTAMP), new ColumnType("BINATY", Messages.getResourceString("type.binary"), true, Types.BINARY), new ColumnType("OBJECT", Messages.getResourceString("type.object"), false, Types.OTHER), new ColumnType("VARCHAR", Messages.getResourceString("type.string"), true, Types.VARCHAR), new ColumnType("VARCHAR_CASESENSITIVE", Messages.getResourceString("type.string"), true, Types.VARCHAR), new ColumnType("VARCHAR_IGNORECASE", Messages.getResourceString("type.string"), true, Types.VARCHAR), new ColumnType("CHAR", Messages.getResourceString("type.char"), true, Types.CHAR), new ColumnType("CHARACTER", Messages.getResourceString("type.char"), true, Types.CHAR), new ColumnType("BLOB", Messages.getResourceString("type.binary"), false, Types.BLOB), new ColumnType("CLOB", Messages.getResourceString("type.string"), true, Types.CLOB), new ColumnType("TEXT", Messages.getResourceString("type.string"), true, Types.CLOB), new ColumnType("ARRAY", Messages.getResourceString("type.string"), true, Types.ARRAY) };

    public H2Dialect() {
        super(COLUMN_TYPES);
    }

    @Override
    protected String createColumnDDL(RootModel root, TableModel tableModel, ColumnModel columnModel, boolean schema, boolean alterTable, StringBuilder additions) {
        StringBuffer sb = new StringBuffer();
        sb.append(columnModel.getColumnName());
        if (columnModel.isAutoIncrement()) {
            sb.append(" IDENTITY");
        } else {
            sb.append(" ").append(columnModel.getColumnType().getName());
            if (columnModel.getColumnType().supportSize() && columnModel.getSize().length() > 0) {
                sb.append("(").append(columnModel.getSize()).append(")");
            }
        }
        if (columnModel.getDefaultValue().length() != 0) {
            sb.append(" DEFAULT ").append(columnModel.getDefaultValue());
        }
        if (columnModel.isNotNull()) {
            sb.append(" NOT NULL");
        }
        return sb.toString();
    }

    @Override
    public String createTableDDL(RootModel root, TableModel model, boolean schema, boolean drop, boolean alterTable, boolean comment, StringBuilder additions) {
        String ddl = super.createTableDDL(root, model, schema, drop, alterTable, comment, additions);
        StringBuilder sb = new StringBuilder(ddl);
        ColumnModel[] columns = model.getColumns();
        if (model.getLogicalName() != null) {
            sb.append(LS);
            sb.append("COMMENT ON TABLE " + model.getTableName() + " IS '" + model.getLogicalName() + "'").append(separator).append(LS);
        }
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getLogicalName() != null) {
                sb.append("COMMENT ON COLUMN ");
                sb.append(model.getTableName() + "." + columns[i].getColumnName());
                sb.append(" IS '" + columns[i].getLogicalName() + "'");
                sb.append(separator).append(LS);
            }
        }
        return sb.toString();
    }

    @Override
    public String getColumnMetadataSQL(String tableName) {
        return super.getColumnMetadataSQL(tableName) + "  LIMIT 1";
    }
}
