package com.kitten.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.logging.Logger;
import com.kitten.dao.KittenConnectionDao;
import com.kitten.utilities.KittenProperties;

public class KittenFetchDatabaseDisplayData {

    private static Logger log = Logger.getLogger(KittenFetchDatabaseDisplayData.class.getName());

    private KittenProperties kittenProperties;

    private KittenConnectionDao kittenConnectionDao;

    public KittenFetchDatabaseDisplayData(KittenProperties kittenProperties, KittenConnectionDao kittenConnectionDao) {
        super();
        this.kittenProperties = kittenProperties;
        this.kittenConnectionDao = kittenConnectionDao;
        try {
            log.addHandler(kittenProperties.getLogFileHandler());
        } catch (Exception e) {
        }
    }

    public Object[][] fetchDisplayObjects(String select) {
        Object[][] result = null;
        if (this.kittenConnectionDao.getConnection() == null) {
            log.warning("No database connection to fetch tree data");
        } else if (select == null || select.equals("")) {
            log.warning("No SQL command to fetch tree data");
        } else {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            try {
                log.finest("Fetching tree data.....statement: " + select);
                preparedStatement = this.kittenConnectionDao.getConnection().prepareStatement(select, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                resultSet = preparedStatement.executeQuery();
                ResultSetMetaData meta = resultSet.getMetaData();
                int numOfCols = meta.getColumnCount();
                resultSet.last();
                int numOfRows = resultSet.getRow();
                resultSet.first();
                result = new Object[numOfRows][numOfCols];
                if (numOfRows > 0) {
                    for (int a = 0; a < numOfRows; a++) {
                        for (int b = 0; b < numOfCols; b++) {
                            result[a][b] = resultSet.getObject(b + 1);
                        }
                        resultSet.next();
                    }
                }
            } catch (SQLException sql) {
                log.severe("Problems during execution of SQL statement while fetch tree data! " + sql);
            } finally {
                try {
                    resultSet.close();
                    preparedStatement.close();
                } catch (Exception e) {
                }
            }
        }
        return result;
    }
}
