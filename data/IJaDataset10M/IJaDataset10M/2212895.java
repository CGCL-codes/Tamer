package jtq.core;

import java.util.List;

public interface ITableDef {

    public String getCatalog();

    public String getTableName();

    public List<IColumnDef> getColumns();

    public interface IColumnDef {

        public String getColumnName();

        public Type getColumnType();

        public boolean isNullable();

        public boolean isPrimaryKey();

        public boolean isGenerated();
    }
}
