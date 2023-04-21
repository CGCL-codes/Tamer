package org.achup.elgenerador.datasource.jdbc.support.statement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import org.achup.elgenerador.datasource.DataSourceException;
import org.achup.elgenerador.datasource.jdbc.JDBCDataSource;
import org.achup.elgenerador.generator.GeneratorManager;
import org.achup.elgenerador.metadata.Column;
import org.achup.elgenerador.metadata.Table;
import org.apache.commons.math.random.RandomData;

/**
 *
 * @author Marco Bassaletti
 */
public class SelectRandomStatementSupport extends StatementSupport {

    private HashMap<Table, HashMap<Column, PreparedStatement>> getRandomItemCountMap = new HashMap<Table, HashMap<Column, PreparedStatement>>();

    private HashMap<Table, HashMap<Column, PreparedStatement>> getRandomItemSelectMap = new HashMap<Table, HashMap<Column, PreparedStatement>>();

    private HashMap<Table, RandomData> randomDataMap = new HashMap<Table, RandomData>();

    public SelectRandomStatementSupport(JDBCDataSource dataSource) {
        super(dataSource);
    }

    public Object getRandomItem(Table table, Column column) {
        try {
            RandomData randomData = null;
            if (!randomDataMap.containsKey(table)) {
                randomData = GeneratorManager.seedRandomData(table.getRandomSeed());
                randomDataMap.put(table, randomData);
            } else {
                randomData = randomDataMap.get(table);
            }
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = null;
            if (getRandomItemCountMap.containsKey(table) && getRandomItemCountMap.get(table).containsKey(column)) {
                preparedStatement = getRandomItemCountMap.get(table).get(column);
            } else {
                StringWriter strWriter = null;
                PrintWriter writer = null;
                String sql = null;
                strWriter = new StringWriter();
                writer = new PrintWriter(strWriter);
                writer.printf("SELECT COUNT(%s) FROM %s", column.getName(), table.getName());
                sql = strWriter.toString();
                writer.close();
                strWriter.close();
                preparedStatement = connection.prepareStatement(sql);
                if (!getRandomItemCountMap.containsKey(table)) {
                    getRandomItemCountMap.put(table, new HashMap<Column, PreparedStatement>());
                    getRandomItemCountMap.get(table).put(column, preparedStatement);
                } else {
                    getRandomItemCountMap.get(table).put(column, preparedStatement);
                }
            }
            int rowCount = 0;
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                rowCount = rs.getInt(1);
            }
            rs.close();
            Object item = null;
            if (rowCount > 0) {
                if (getRandomItemSelectMap.containsKey(table) && getRandomItemSelectMap.get(table).containsKey(column)) {
                    preparedStatement = getRandomItemSelectMap.get(table).get(column);
                } else {
                    StringWriter strWriter = null;
                    PrintWriter writer = null;
                    String sql = null;
                    strWriter = new StringWriter();
                    writer = new PrintWriter(strWriter);
                    writer.printf("SELECT %s FROM %s", column.getName(), table.getName());
                    sql = strWriter.toString();
                    writer.close();
                    strWriter.close();
                    preparedStatement = connection.prepareStatement(sql);
                    if (!getRandomItemSelectMap.containsKey(table)) {
                        getRandomItemSelectMap.put(table, new HashMap<Column, PreparedStatement>());
                        getRandomItemSelectMap.get(table).put(column, preparedStatement);
                    } else {
                        getRandomItemSelectMap.get(table).put(column, preparedStatement);
                    }
                }
                int selectedRow = randomData.nextInt(0, rowCount);
                int row = 0;
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    item = rs.getObject(1);
                    if (row == selectedRow) {
                        break;
                    }
                    row++;
                }
                rs.close();
            }
            return item;
        } catch (Exception ex) {
            throw new DataSourceException(ex);
        }
    }
}
