package org.jp.bus.dao.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseDao {

    private static Log log = LogFactory.getLog(BaseDao.class);

    private String getSql(Object obj, String methodName) {
        Class<?> clazz = this.getClass();
        String className = clazz.getSimpleName();
        String sqlName = "/org/jp/sql/" + className + "_" + methodName + ".sql";
        log.info("SQL�ĤΥѥ���" + sqlName);
        BufferedReader read = new BufferedReader(new InputStreamReader(BaseDao.class.getResourceAsStream(sqlName)));
        String sql = "";
        try {
            while (read.ready()) {
                sql += read.readLine() + "\r";
            }
            read.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HashSet<String> set = new HashSet<String>();
        int index = 0;
        int end = 0;
        for (int i = 0; i < sql.length(); i++) {
            index = sql.indexOf("/*", i);
            if (index != -1) {
                end = sql.indexOf("*/", index);
                i = index + 1;
                if (end != -1) {
                    set.add(sql.substring(index + 2, end));
                }
            }
        }
        if (obj == null) {
            log.info("SQL:" + sql);
            return sql;
        }
        Class<?> clazzParam = obj.getClass();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String field = it.next();
            Field fieldName = null;
            try {
                fieldName = clazzParam.getDeclaredField(field);
            } catch (Exception e) {
            }
            if (fieldName != null) {
                fieldName.setAccessible(true);
                Class<?> paramTypeClass = fieldName.getType();
                try {
                    if (paramTypeClass == int.class || paramTypeClass == Integer.class) {
                        int value = (Integer) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", "" + value);
                    } else if (paramTypeClass == BigDecimal.class) {
                        DecimalFormat format = new DecimalFormat("#.############");
                        BigDecimal value = (BigDecimal) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", format.format(value));
                    } else if (paramTypeClass == float.class || paramTypeClass == Float.class) {
                        float value = (Float) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", "" + value);
                    } else if (paramTypeClass == double.class || paramTypeClass == Double.class) {
                        double value = (Double) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", "" + value);
                    } else if (paramTypeClass == java.util.Date.class) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyy/MM/dd");
                        java.util.Date value = (java.util.Date) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", "'" + sdf.format(value) + "'");
                    } else if (paramTypeClass == String.class) {
                        String value = (String) fieldName.get(obj);
                        sql = sql.replace("/*" + field + "*/", "'" + value + "'");
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        log.info("SQL:" + sql);
        return sql;
    }

    /**
	 * sql�ˤ�ꡢ�ǩ`������
	 * @param <T>
	 * @param paramObj
	 * @param resultClass
	 * @return
	 */
    public <T> List<T> search(Object paramObj, String methodName, Class<T> resultClass) {
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        List<T> searchResult = new ArrayList<T>();
        try {
            con = getConnection();
            st = con.createStatement();
            rs = st.executeQuery(getSql(paramObj, methodName));
            ResultSetMetaData rsm = rs.getMetaData();
            int cloumnCnt = rsm.getColumnCount();
            while (rs.next()) {
                T t = resultClass.newInstance();
                for (int i = 0; i < cloumnCnt; i++) {
                    String columnLabel = rsm.getColumnLabel(i + 1);
                    Object value = rs.getObject(columnLabel);
                    Field field = resultClass.getDeclaredField(columnLabel);
                    if (field != null) {
                        field.setAccessible(true);
                        field.set(t, value);
                    }
                }
                searchResult.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally {
            free(rs, st);
        }
        return searchResult;
    }

    /**
	 * sql�ˤ�ꡢ�ǩ`������
	 * @param <T>
	 * @param paramObj
	 * @param resultClass
	 * @return
	 */
    public <T> List<T> search(String methodName, Class<T> resultClass) {
        return search(null, methodName, resultClass);
    }

    public int updateOrDeleteOrInsert(Object paramObj, String methodName) {
        Connection con = null;
        Statement st = null;
        int cnt = 0;
        try {
            con = getConnection();
            st = con.createStatement();
            String sql = getSql(paramObj, methodName);
            cnt = st.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            free(null, st);
        }
        return cnt;
    }

    public int updateOrDeleteOrInsert(String methodName) {
        return updateOrDeleteOrInsert(null, methodName);
    }

    public int update(String methodName) {
        return updateOrDeleteOrInsert(null, methodName);
    }

    public int update(Object paramObj, String methodName) {
        return updateOrDeleteOrInsert(paramObj, methodName);
    }

    public int delete(String methodName) {
        return updateOrDeleteOrInsert(null, methodName);
    }

    public int delete(Object paramObj, String methodName) {
        return updateOrDeleteOrInsert(paramObj, methodName);
    }

    public int insert(String methodName) {
        return updateOrDeleteOrInsert(null, methodName);
    }

    public int insert(Object paramObj, String methodName) {
        return updateOrDeleteOrInsert(paramObj, methodName);
    }

    @SuppressWarnings({ "finally" })
    private Connection getConnection() {
        Connection con = null;
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/mysql");
            con = ds.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return con;
        }
    }

    private void free(ResultSet rs, Statement st) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
