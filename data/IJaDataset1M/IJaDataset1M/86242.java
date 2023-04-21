package org.achup.generador.datasource.jdbc.sqlserver;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.achup.generador.datasource.DataSource;
import org.achup.generador.datasource.DataSourceException;
import org.achup.generador.datasource.jdbc.DataMapper;
import org.achup.generador.datasource.jdbc.JDBCHelper;
import org.achup.generador.datasource.jdbc.JDBCProperties;
import org.achup.generador.datasource.jdbc.util.SQLHelper;
import org.achup.generador.print.PrintManager;
import org.achup.generador.model.Column;
import org.achup.generador.model.Index;
import org.achup.generador.model.Model;
import org.achup.generador.model.Row;
import org.achup.generador.model.Table;
import org.achup.generador.model.types.DataType;
import org.achup.generador.model.types.GenericDataType;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;

/**
 *
 * @author Marco Bassaletti
 */
public class SQLServerDataSource implements DataSource, DataMapper {

    private JDBCProperties properties;

    private Statement stm = null;

    private HashMap<Long, RandomData> randomDataHashMap = new HashMap<Long, RandomData>();

    public SQLServerDataSource(JDBCProperties properties) {
        this.properties = properties;
    }

    @Override
    public void prepare(Model model) {
        try {
            stm = getProperties().getConnection().createStatement();
        } catch (Exception ex) {
        }
        if (!model.isCreateTables()) {
            Table[] tables = model.getTables();
            for (Table table : tables) {
                if (table.getTableGenerator() != null && table.getTableGenerator().getRowCount() > 0) {
                    try {
                        String stmStr = String.format("DELETE FROM %s", table.getName());
                        PrintManager.out.printf("SQL: %s\n", stmStr);
                        stm.execute(stmStr);
                    } catch (SQLException ex) {
                        PrintManager.out.printf("SQL FAILED: %s\n", ex);
                    }
                }
            }
            return;
        }
        List<String> databaseTables = JDBCHelper.getDatabaseTables(getProperties());
        LinkedList<Table> processedTables = new LinkedList<Table>();
        Table[] tables = model.getTables();
        while (processedTables.size() < tables.length) {
            for (Table table : tables) {
                if (!processedTables.contains(table)) {
                    String metadataName = null;
                    String dbTableName = null;
                    boolean containTable = false;
                    for (String tableName : databaseTables) {
                        if (tableName.toUpperCase().compareTo(table.getName().toUpperCase()) == 0) {
                            dbTableName = tableName;
                            metadataName = table.getName();
                            containTable = true;
                        }
                    }
                    if (!containTable) {
                        processedTables.add(table);
                    } else {
                        String stmStr = null;
                        try {
                            if (dbTableName != null) {
                                table.setName(dbTableName);
                            }
                            stmStr = String.format("DROP TABLE %s", table.getName());
                            if (dbTableName != null) {
                                table.setName(metadataName);
                            }
                            PrintManager.out.printf("SQL: %s\n", stmStr);
                            stm.execute(stmStr);
                            processedTables.add(table);
                        } catch (Exception ex) {
                            PrintManager.out.printf("SQL FAILED: %s\n", ex);
                        }
                    }
                }
            }
        }
        processedTables.clear();
        while (processedTables.size() < tables.length) {
            for (Table table : tables) {
                if (!processedTables.contains(table)) {
                    String stmStr = null;
                    try {
                        stmStr = SQLHelper.getCreateTable(this, table);
                        PrintManager.out.printf("SQL: %s\n", stmStr);
                        stm.execute(stmStr);
                        processedTables.add(table);
                    } catch (Exception ex) {
                        PrintManager.out.printf("SQL FAILED: %s\n", ex);
                    }
                }
            }
        }
        for (Table table : tables) {
            if (table.getIndexes() != null) {
                for (Index index : table.getIndexes()) {
                    String stmStr = null;
                    try {
                        stmStr = SQLHelper.getCreateIndex(table, index);
                        PrintManager.out.printf("SQL: %s\n", stmStr);
                        stm.execute(stmStr);
                        processedTables.add(table);
                    } catch (Exception ex) {
                        PrintManager.out.printf("SQL FAILED: %s\n", ex);
                    }
                }
            }
        }
    }

    @Override
    public Object getRandomItem(Table table, Column column) {
        RandomData randomData = null;
        if (randomDataHashMap.containsKey(column.getRandomSeed())) {
            randomData = randomDataHashMap.get(column.getRandomSeed());
        } else {
            JDKRandomGenerator randomGenerator = new JDKRandomGenerator();
            randomGenerator.setSeed(column.getRandomSeed());
            randomData = new RandomDataImpl(randomGenerator);
            randomDataHashMap.put(column.getRandomSeed(), randomData);
        }
        int rowCount = -1;
        String stmStr = SQLHelper.getSelectCount(table);
        try {
            ResultSet rs = stm.executeQuery(stmStr);
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
            rs.close();
        } catch (Exception ex) {
            PrintManager.out.printf("SQL FAILED: %s\n", stmStr);
            PrintManager.out.printf("SQL FAILED: %s\n", ex);
        }
        Object retVal = null;
        if (rowCount > 0) {
            int selectedIndex = randomData.nextInt(1, rowCount);
            stmStr = SQLHelper.getSelectItems(table, new Column[] { column });
            try {
                ResultSet rs = stm.executeQuery(stmStr);
                int i = 1;
                while (rs.next()) {
                    if (i == selectedIndex) {
                        retVal = rs.getObject(1);
                        if (retVal == null) {
                            retVal = ObjectUtils.NULL;
                        }
                        break;
                    }
                    i++;
                }
                rs.close();
            } catch (Exception ex) {
                PrintManager.out.printf("SQL FAILED: %s\n", stmStr);
                PrintManager.out.printf("SQL FAILED: %s\n", ex);
            }
        }
        return retVal;
    }

    @Override
    public void close() {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (Exception ex) {
            PrintManager.out.printf("Error closing Statement: %s\n", ex);
        }
    }

    @Override
    public boolean hasItem(Table table, Row row) {
        boolean retVal = false;
        String stmStr = SQLHelper.getCountItems(this, table, row);
        try {
            ResultSet rs = stm.executeQuery(stmStr);
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    retVal = true;
                }
            }
            rs.close();
        } catch (Exception ex) {
            PrintManager.out.printf("SQL FAILED: %s\n", stmStr);
            PrintManager.out.printf("SQL FAILED: %s\n", ex);
        }
        return retVal;
    }

    @Override
    public boolean hasItem(Table table, Column column, Object item) {
        boolean retVal = false;
        String stmStr = SQLHelper.getCountItem(this, table, column, item);
        try {
            ResultSet rs = stm.executeQuery(stmStr);
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count > 0) {
                    retVal = true;
                }
            }
            rs.close();
        } catch (Exception ex) {
            PrintManager.out.printf("SQL FAILED: %s\n", stmStr);
            PrintManager.out.printf("SQL FAILED: %s\n", ex);
        }
        return retVal;
    }

    @Override
    public void insert(Table table, Row row) {
        String stmStr = SQLHelper.getInsert(this, table, row);
        try {
            stm.execute(stmStr);
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }

    public JDBCProperties getProperties() {
        return properties;
    }

    @Override
    public String getTypeDefinition(Column column) {
        String dt = null;
        if (column.getDataType().getTypeClass() == DataType.TypeClass.GENERIC) {
            if (column.getDataType().isSameType(GenericDataType.BOOL)) {
                dt = "integer";
            } else if (column.getDataType().isSameType(GenericDataType.INT)) {
                dt = "integer";
            } else if (column.getDataType().isSameType(GenericDataType.LONG)) {
                dt = "bigint";
            } else if (column.getDataType().isSameType(GenericDataType.REAL)) {
                dt = "real";
            } else if (column.getDataType().isSameType(GenericDataType.STRING)) {
                dt = "varchar(" + column.getSize() + ")";
            } else if (column.getDataType().isSameType(GenericDataType.DATE)) {
                dt = "date";
            } else if (column.getDataType().isSameType(GenericDataType.TIME)) {
                dt = "time";
            } else if (column.getDataType().isSameType(GenericDataType.TIMESTAMP)) {
                dt = "timestamp";
            }
        } else if (column.getDataType().getTypeClass() == DataType.TypeClass.DATASOURCE) {
            DataType dataType = column.getDataType();
            String dataTypeName = dataType.getName().toUpperCase();
            if (dataTypeName.compareTo("CHAR") == 0 || dataTypeName.compareTo("NCHAR") == 0 || dataTypeName.compareTo("VARCHAR") == 0 || dataTypeName.compareTo("NVARCHAR") == 0) {
                dt = String.format("%s(%d)", dataTypeName, column.getSize());
            } else {
                dt = column.getDataType().getTypeDefinition();
            }
        }
        return dt;
    }

    @Override
    public String quoteData(Object item) {
        String val = null;
        if (item == null) {
            item = ObjectUtils.NULL;
        }
        if (item instanceof Boolean) {
            Boolean aBoolean = (Boolean) item;
            if (aBoolean) {
                val = "1";
            } else {
                val = "0";
            }
        } else if (item instanceof String) {
            val = String.format("'%s'", (String) item);
        } else if (item instanceof Date) {
            val = String.format("'%s'", ((Date) item).toString());
        } else if (item instanceof Time) {
            val = String.format("'%s'", ((Time) item).toString());
        } else if (item instanceof Timestamp) {
            val = String.format("'%s'", ((Timestamp) item).toString());
        } else if (item instanceof ObjectUtils.Null) {
            val = "NULL";
        } else {
            val = item.toString();
        }
        return val;
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Type getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Model getMetadata() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
