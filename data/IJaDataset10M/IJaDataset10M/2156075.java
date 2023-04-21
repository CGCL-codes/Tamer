package com.soode.openospc.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;
import com.soode.db.Db;
import com.soode.db.DbException;

/**
 * This class will search, edit, delete, add  to module.
 * @author Terakhir bin Wih
 * @email khirzrn@gmail.com
 * @version 1.0
 */
public class Module {

    /**
	 * Initialize Module class
	 *
	 */
    public Module() {
    }

    /**
	 * Add new module to Database
	 * @param h The Hashtable contain new module information
	 * @return The New module Id
	 * @throws DbException
	 * @throws SQLException
	 * @author khir
	 */
    public int addModule(Hashtable h) throws DbException, SQLException {
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        String sql = null;
        ResultSet rs = null;
        sql = "insert into Module (Name, " + "Description) values ('" + h.get("moduleName") + "','" + h.get("description") + "')";
        stmt.setQueryTimeout(60);
        stmt.execute(sql);
        int autoIncKeyFromFunc = -1;
        rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
        if (rs.next()) {
            autoIncKeyFromFunc = rs.getInt(1);
        }
        db.close();
        return autoIncKeyFromFunc;
    }

    /**
	 * This method will delete specified Moduel
	 * @param id The Module Id that want to delete
	 * @throws Exception
	 * @author khir
	 */
    public void deleteModule(String id) throws Exception {
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        String sql = null;
        sql = " delete from Module where Id = '" + id + "'";
        stmt.executeUpdate(sql);
        db.close();
    }

    /**
	 * Get the module data/Information in database. 
	 * @param id the Module Id to search or get
	 * @return The Hashtable of module Information
	 * @throws Exception
	 * @author khir
	 */
    public Hashtable getModule(String id) throws Exception {
        Hashtable h = new Hashtable();
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        ResultSet rs = null;
        String sql = "SELECT * FROM Module where Id = '" + id + "' ";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            h.put("name", this.getString(rs, "Name"));
            h.put("id", this.getString(rs, "Id"));
            h.put("description", this.getString(rs, "Description"));
            Menu menu = new Menu();
            h.put("menuList", menu.getMenuList(this.getString(rs, "Id")));
        }
        return h;
    }

    /**
	 * This method will seach and List all module in Database 
	 * @return The list of module.
	 * @throws SQLException
	 * @throws Exception
	 * @author khir
	 */
    public Vector getModuleList() throws SQLException, Exception {
        Vector v = new Vector();
        Db db = new Db("properties.openospc");
        Statement stmt = db.getStatement();
        ResultSet rs = null;
        String sql = "SELECT * FROM Module order by Name";
        rs = stmt.executeQuery(sql);
        while (rs.next()) {
            Hashtable h = new Hashtable();
            h.put("name", this.getString(rs, "Name"));
            h.put("id", this.getString(rs, "Id"));
            h.put("description", this.getString(rs, "Description"));
            v.addElement(h);
        }
        return v;
    }

    /**
	 * Method for the null String data to "".
	 * @param rs the database resulset.
	 * @param fieldname
	 * @return the "" or the data String value
	 * @throws Exception
	 * @author khir
	 */
    private String getString(ResultSet rs, String fieldname) throws Exception {
        if (rs.getString(fieldname) == null) return ""; else return rs.getString(fieldname);
    }
}
