package dao;

import vo.PersistentObject;
import java.util.List;
import java.sql.*;

public abstract class DataAccessObject {

    protected Connection con = null;

    protected String usuario = "root";

    protected String pass = "";

    protected String url = "com.mysql.jdbc.Driver";

    public abstract void insert(PersistentObject p) throws DataAccessException {
    }

    public abstract void delete(PersistentObject p) throws DataAccessException;

    public abstract List select(PersistentObject p) throws DataAccessException;

    public abstract List selectAll() throws DataAccessException;

    public abstract void update(PersistentObject p) throws DataAccessException;

    public Connection getConnection(String url, String user, String pass) {
        Class ForName = "";
        con.DriverManager.getConnection(url, user, pass);
        con.setAutoCommit(false);
    }

    public void Commit() {
        con.commit();
    }

    public void Cerrar() {
        con.close();
    }
}
