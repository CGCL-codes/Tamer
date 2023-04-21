package edu.nctu.csie.jichang.database.command;

import edu.nctu.csie.jichang.database.model.builder.ISQLBuilder;
import edu.nctu.csie.jichang.database.model.cell.ColumnInfo;
import edu.nctu.csie.jichang.database.model.cell.DBDatabase;
import edu.nctu.csie.jichang.database.model.cell.DBSchema;
import edu.nctu.csie.jichang.database.model.cell.DBTable;
import edu.nctu.csie.jichang.database.util.DBException;
import edu.nctu.csie.jichang.database.util.StringUtil;

public class DiffStructureCommand implements ISQLCommand {

    private DBDatabase current = null;

    private DBDatabase base = null;

    private ISQLBuilder builder = null;

    public String execute() {
        checkParameter();
        StringBuffer tOut = new StringBuffer();
        for (int i = 0; i < base.getAllSchema().length; i++) {
            DBSchema tBaseSchema = base.getAllSchema()[i];
            DBSchema tCurrentSchema = current != null ? current.getAllSchema()[i] : new DBSchema(tBaseSchema.getName());
            tOut.append(compareSchema(tCurrentSchema, tBaseSchema));
        }
        return tOut.toString();
    }

    private String compareSchema(DBSchema pCurrent, DBSchema pBase) {
        StringBuffer tOut = new StringBuffer();
        for (String tTable : pBase.getTableNames()) {
            if (pCurrent.hasTable(tTable)) {
                String tSQLTable = compareTable(pCurrent.getTable(tTable), pBase.getTable(tTable));
                if (StringUtil.isNotEmptyOrSpace(tSQLTable)) {
                    tOut.append(tSQLTable);
                }
            } else {
                tOut.append(builder.getSQLTableAdd(pBase.getTable(tTable)));
            }
        }
        String[] tTables = pCurrent.getTableNames();
        for (int i = tTables.length - 1; i >= 0; i--) {
            String tTable = tTables[i];
            if (StringUtil.isNotEmptyOrSpace(tTable) && !pBase.hasTable(tTable)) {
                tOut.append(builder.getSQLTableRemove(pCurrent.getTable(tTable)));
            }
        }
        return builder.getSQLSchema(pCurrent.getName(), tOut.toString());
    }

    private String compareTable(DBTable pCurrent, DBTable pBase) {
        StringBuffer tOut = new StringBuffer();
        tOut.append(builder.getSQLConstraintRemove(pCurrent, pBase));
        for (String s : pBase.getTableHeader().getColumnNames()) {
            if (pCurrent.getTableHeader().hasColumn(s)) {
                ColumnInfo tCurrent = pCurrent.getTableHeader().getColumnType(s);
                ColumnInfo tBase = pBase.getTableHeader().getColumnType(s);
                if (!tBase.isSame(tCurrent)) {
                    tOut.append(builder.getSQLColumnModify(pBase, tCurrent, tBase));
                }
            } else {
                tOut.append(builder.getSQLColumnAdd(pBase, pBase.getTableHeader().getColumnType(s)));
            }
        }
        for (String s : pCurrent.getTableHeader().getColumnNames()) {
            if (!pBase.getTableHeader().hasColumn(s)) {
                tOut.append(builder.getSQLColumnRemove(pCurrent, pCurrent.getTableHeader().getColumnType(s)));
            }
        }
        tOut.append(builder.getSQLConstraintAdd(pCurrent, pBase));
        return tOut.toString();
    }

    public void setOperand(ISQLBuilder pBuilder, DBDatabase pBase) {
        builder = pBuilder;
        base = pBase;
    }

    public void setOperand(ISQLBuilder pBuilder, DBDatabase pCurrent, DBDatabase pBase) {
        builder = pBuilder;
        current = pCurrent;
        base = pBase;
    }

    private void checkParameter() {
        if (builder == null || base == null) {
            throw new DBException("參數輸入錯誤!");
        }
    }
}
