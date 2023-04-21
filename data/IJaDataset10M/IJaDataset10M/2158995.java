package com.arm.framework.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.mysql.jdbc.CommunicationsException;

/**
 * @author <a href="mailto:todushyant@gmail.com">todushyant@gmail.com</a>
 * 
 */
public class MySql<E> extends MySqlCore<E> {

    /**
	 * @return
	 */
    public static MySql<?> get() {
        return new MySql<Object>();
    }

    /**
	 * @return
	 */
    public static MySql<?> create() {
        return new MySql<Object>();
    }

    public MySql() {
    }

    /**
	 * @param query
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
    public void execute(String query) throws ClassNotFoundException, SQLException, CommunicationsException {
        getDatabase().executeUpdate(query);
    }

    /**
	 * @param query
	 * @return
	 * @throws ClassNotFoundException
	 * @throws CommunicationsException
	 * @throws SQLException
	 */
    public ResultSet select(String query) throws ClassNotFoundException, CommunicationsException, SQLException {
        return getDatabase().executeQuery(query);
    }

    /**
	 * @param className
	 * @param condition
	 * @return
	 * @throws CommunicationsException
	 * @throws SQLException
	 */
    public ResultSet selectWhere(Class<E> className, String condition) throws CommunicationsException, SQLException {
        String tableName = className.getSimpleName().toLowerCase();
        String query = "select * from " + tableName + " where ( " + condition + " )";
        p(query);
        return getDatabase().executeQuery(query);
    }

    /**
	 * @param className
	 * @return
	 */
    public ResultSet selectall(Class<E> className) {
        try {
            loadDriver();
            String query = "select * from " + className.getSimpleName().toLowerCase();
            ResultSet rs = getDatabase().executeQuery(query);
            p(query);
            return rs;
        } catch (CommunicationsException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * @param className
	 * @param e
	 * @return
	 */
    public ResultSet update(Class<E> className, E e) {
        try {
            loadDriver();
            String query = "update " + className.getSimpleName() + " set name='dush' where id=";
            ResultSet rs = getDatabase().executeQuery(query);
            p(query);
            return rs;
        } catch (CommunicationsException e1) {
        } catch (SQLException e1) {
        } catch (ClassNotFoundException e2) {
        }
        return null;
    }

    /**
	 * @param string
	 */
    private void p(String string) {
        System.out.println(string);
    }
}
