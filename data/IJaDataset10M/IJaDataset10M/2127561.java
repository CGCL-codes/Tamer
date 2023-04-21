package com.googlecode.sqlannotations.engine;

import com.googlecode.sqlannotations.annotations.Column;
import com.googlecode.sqlannotations.annotations.Delete;
import com.googlecode.sqlannotations.annotations.Recordset;
import com.googlecode.sqlannotations.engine.jdbc.JdbcCloser;
import com.googlecode.sqlannotations.engine.jdbc.JdbcDriverLoader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLEngine<T> {

    private String driver = "";

    private String url = "";

    private String user = "";

    private String password = "";

    public SQLEngine(String driver, String url, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public ArrayList<T> getResultset(T record, Object[] params) throws SQLAnnotationException {
        loadDriver();
        validateRecordAnnotation(record);
        return query(record, params);
    }

    public ArrayList<T> getResultset(T record) throws SQLAnnotationException {
        loadDriver();
        validateRecordAnnotation(record);
        Object[] params = {};
        return query(record, params);
    }

    public void delete(T record) throws SQLAnnotationException {
        loadDriver();
        validateDeleteAnnotation(record);
        Object[] params = {};
        delete(record, params);
    }

    public void delete(T record, Object[] params) throws SQLAnnotationException {
        loadDriver();
        validateDeleteAnnotation(record);
        deleteEntries(record, params);
    }

    private void loadDriver() throws SQLAnnotationException {
        JdbcDriverLoader.loadDriver(this.getDriver());
    }

    private void validateRecordAnnotation(T record) throws SQLAnnotationException {
        Recordset annot = record.getClass().getAnnotation(Recordset.class);
        if (annot == null) {
            throw new SQLAnnotationException("Recordset class is not annotated with @Recordset.");
        }
    }

    private void validateDeleteAnnotation(T record) throws SQLAnnotationException {
        Delete annot = record.getClass().getAnnotation(Delete.class);
        if (annot == null) {
            throw new SQLAnnotationException("Delete class is not annotated with @Delete.");
        }
    }

    private ArrayList<T> query(T record, Object[] params) throws SQLAnnotationException {
        ArrayList<T> results = new ArrayList<T>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            Recordset annot = record.getClass().getAnnotation(Recordset.class);
            String sql = annot.sql();
            try {
                conn = (Connection) DriverManager.getConnection(getUrl(), getUser(), getPassword());
            } catch (SQLException ex) {
                throw new SQLAnnotationException("Failed to get database connection", ex);
            }
            try {
                ps = conn.prepareStatement(sql);
                int count = 1;
                for (Object param : params) {
                    if (param.getClass().getCanonicalName().equals("java.lang.String")) ps.setString(count, ((String) param)); else if (param.getClass().getCanonicalName().equals("java.lang.Integer")) ps.setInt(count, ((Integer) param));
                    count++;
                }
            } catch (SQLException ex) {
                throw new SQLAnnotationException("Failed to create statement", ex);
            }
            try {
                rs = ps.executeQuery();
            } catch (SQLException ex) {
                throw new SQLAnnotationException("Failed to execute query", ex);
            }
            while (rs.next()) {
                results.add(parseRow(record, rs));
            }
        } catch (SQLException ex) {
            throw new SQLAnnotationException("Failed to iterate through resultset", ex);
        } finally {
            JdbcCloser.close(rs, ps, conn);
        }
        return results;
    }

    private void deleteEntries(T record, Object[] params) throws SQLAnnotationException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            Delete annot = record.getClass().getAnnotation(Delete.class);
            String sql = annot.sql();
            try {
                conn = (Connection) DriverManager.getConnection(getUrl(), getUser(), getPassword());
            } catch (SQLException ex) {
                throw new SQLAnnotationException("Failed to get database connection", ex);
            }
            try {
                ps = conn.prepareStatement(sql);
                int count = 1;
                for (Object param : params) {
                    if (param.getClass().getCanonicalName().equals("java.lang.String")) ps.setString(count, ((String) param)); else if (param.getClass().getCanonicalName().equals("java.lang.Integer")) ps.setInt(count, ((Integer) param));
                    count++;
                }
            } catch (SQLException ex) {
                throw new SQLAnnotationException("Failed to create statement", ex);
            }
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLAnnotationException("Failed to execute delete", ex);
        } finally {
            JdbcCloser.close(null, ps, conn);
        }
    }

    private T parseRow(T record, ResultSet rs) throws SQLAnnotationException {
        T result = null;
        try {
            result = (T) record.getClass().newInstance();
        } catch (InstantiationException ex) {
            throw new SQLAnnotationException("Failed to instantiate @Recordset class", ex);
        } catch (IllegalAccessException ex) {
            throw new SQLAnnotationException("Illegal access to @Recordset class", ex);
        }
        Field fields[] = result.getClass().getFields();
        for (Field f : fields) {
            Annotation a = f.getAnnotation(Column.class);
            if (a != null) {
                Column c = (Column) a;
                try {
                    try {
                        if (f.getType() == int.class) {
                            f.set(result, rs.getInt(c.columnName()));
                        } else {
                            f.set(result, rs.getString(c.columnName()));
                        }
                    } catch (IllegalArgumentException ex) {
                        throw new SQLAnnotationException("@Column not bound", ex);
                    } catch (IllegalAccessException ex) {
                        throw new SQLAnnotationException("Illegal access to @Column", ex);
                    }
                } catch (SQLException ex) {
                    throw new SQLAnnotationException("Failed to get column value from database", ex);
                }
            }
        }
        return result;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
