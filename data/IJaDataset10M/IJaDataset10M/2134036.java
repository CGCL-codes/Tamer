package com.csii.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.csii.callback.Callback;
import com.csii.db.template.JdbcTemplate;
import com.csii.exception.DAOException;
import com.csii.vo.CatalogVO;

/**
 * @author cuiyi
 *
 */
public class ShowDataDAO implements ShowDataDAOIF {

    private JdbcTemplate template = new JdbcTemplate();

    /**
	 * �õ���ݿ���Ϣ
	 * @return
	 * @throws DAOException
	 */
    public CatalogVO getDatabaseMetaData() throws DAOException {
        try {
            return template.getDatabaseMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        }
    }

    /**
	 * �õ�����
	 * @return
	 * @throws DAOException
	 */
    public List getTables() throws DAOException {
        try {
            return template.getTables();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        }
    }

    /**
	 * ȫ��
	 * @param tableName
	 * @return
	 * @throws DAOException
	 */
    public Map getInfo(String tableName) throws DAOException {
        String sqlstatement = "SELECT * FROM " + tableName;
        System.out.println("sqlstatement = " + sqlstatement);
        Object[] obj = {};
        try {
            final Map resMap = new Hashtable();
            template.query(sqlstatement, obj, new Callback() {

                public void doResultSet(ResultSet rs) throws SQLException {
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnCount = rsmd.getColumnCount();
                    System.out.println("columnCount = " + columnCount);
                    List data = new Vector();
                    List name = new Vector();
                    List type = new Vector();
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            Object obj = rs.getObject(i);
                            data.add(obj);
                        }
                    }
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rsmd.getColumnName(i);
                        String columnType = rsmd.getColumnTypeName(i);
                        name.add(columnName);
                        type.add(columnType);
                    }
                    resMap.put("data", data);
                    resMap.put("name", name);
                    resMap.put("type", type);
                }
            });
            return resMap;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DAOException(e);
        }
    }
}
