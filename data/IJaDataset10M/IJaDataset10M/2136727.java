package ua.orion.core.utils;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author molodec
 */
public class JDBCUtils {

    private static final String PS = java.io.File.separator;

    private static Connection con = null;

    public static final Integer OPPERATION_GET_DATA_COLUMN = 1;

    public static final Integer OPPERATION_GET_DATA_MATRIX = 2;

    /**
     * Метод подключения к базе данных
     *
     * @param userName - имя пользователя
     * @param password - пароль
     * @param driver - СУБД
     * @param URL - коннект
     * @throws SQLException
     */
    private static void connectToDataBase(String userName, String password, String URL) throws SQLException {
        Properties connInfo = new Properties();
        connInfo.put("user", userName);
        connInfo.put("password", password);
        if (URL.indexOf("sqlserver") > -1) {
        } else if (URL.indexOf("mysql") > -1) {
        } else if (URL.indexOf("firebirdsql") > -1) {
            DriverManager.registerDriver(new org.firebirdsql.jdbc.FBDriver());
            connInfo.put("lc_ctype", "WIN1251");
        }
        con = DriverManager.getConnection(URL, connInfo);
    }

    /**
     *
     * @param <T> - Расширение типа получаемых данных
     * @param db - База данных
     * @param sql - Запрос
     * @param user - Пользователь
     * @param pass - Пароль
     * @param params - Параметры для запроса
     * @param typeOpperation - Тип операции
     * @return Список данных типа, соответствующего расширению Т
     * @throws SQLException - Ошибка выполнения SQL
     * @throws IOException - Ошибка при выполнении ввода/вывода
     */
    public static <T> ArrayList<T> getDataFromDB(String db, String sql, String user, String pass, List params, Integer typeOpperation) throws SQLException, IOException {
        connectToDataBase(user, pass, db);
        ArrayList<T> data = new ArrayList<T>();
        PreparedStatement st = con.prepareStatement(sql);
        if (params != null) {
            Integer index = 1;
            for (Object o : params) {
                st.setObject(index, o);
                index++;
            }
        }
        ResultSet rs = st.executeQuery();
        int x = rs.getMetaData().getColumnCount();
        if (typeOpperation == OPPERATION_GET_DATA_COLUMN) {
            while (rs.next()) {
                String l;
                l = rs.getString(1);
                data.add((T) l);
            }
        } else if (typeOpperation == OPPERATION_GET_DATA_MATRIX) {
            while (rs.next()) {
                List<String> l = new ArrayList();
                for (int i = 1; i <= x; i++) {
                    l.add(rs.getString(i));
                }
                data.add((T) l);
            }
        }
        if (rs != null) {
            rs.close();
        }
        if (st != null) {
            st.close();
        }
        if (con != null) {
            con.close();
        }
        return data;
    }
}
