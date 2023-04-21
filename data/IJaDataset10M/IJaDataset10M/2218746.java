package de.objectcode.time4u.util.indepdb.builder;

import de.objectcode.time4u.util.indepdb.Select;
import de.objectcode.time4u.util.indepdb.Table;

public class SAPDBSQLBuilder extends DefaultSQLBuilder {

    /**
   * @see de.objectcode.time4u.util.indepdb.ISQLBuilder#getSelectIdentity()
   */
    public String getSelectIdentity() {
        return "select hibernate_sequence.currval from dual";
    }

    /**
   * @see de.objectcode.time4u.util.indepdb.ISQLBuilder#getSchemaKey()
   */
    public String getSchemaKey() {
        return "sapdb";
    }

    protected String getInsertIdentityColumn() {
        return "hibernate_sequence.nextval";
    }

    protected String buildJoin(Table[] tables, boolean addWhere, Select.Join[] joins) {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        for (int i = 0; i < tables.length; i++) {
            if (i > 0) buffer.append(",");
            buffer.append(tables[i].getName());
        }
        buffer.append(" where ");
        for (int i = 0; i < joins.length; i++) {
            String fromTable = joins[i].getFromTable().getName();
            String[] fromColumns = joins[i].getFromColumns();
            String toTable = joins[i].getToTable().getName();
            String[] toColumns = joins[i].getToColumns();
            boolean outer = joins[i].isOuter();
            for (int j = 0; j < fromColumns.length; j++) {
                if (!first) buffer.append(" and ");
                buffer.append(fromTable).append(".").append(fromColumns[j]);
                buffer.append("=");
                buffer.append(toTable).append(".").append(toColumns[j]);
                if (outer) buffer.append("(+)");
                first = false;
            }
        }
        if (addWhere) buffer.append(" and ");
        return buffer.toString();
    }

    public String getBuilderName() {
        return "SAPDB";
    }
}
