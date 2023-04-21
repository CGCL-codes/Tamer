package org.fmi.bioinformatics.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

/**
 * @author mhaimel  -  Diploma Thesis
 *
 */
public class LocalDbUtil extends AbstractDbUtil {

    private static LocalDbUtil _ref = null;

    private String drivername = "";

    private String name = "";

    private String URL = "";

    private String user = "";

    private String password = "";

    private LocalDbUtil() {
        super();
    }

    public static LocalDbUtil instance() {
        if (null == LocalDbUtil._ref) {
            LocalDbUtil._ref = new LocalDbUtil();
        }
        return _ref;
    }

    @Override
    protected Connection buildConnection() throws Exception {
        this.name = this.getProp().getProperty("DS.name");
        this.URL = this.getProp().getProperty("DS.url");
        this.user = this.getProp().getProperty("DS.user");
        this.password = this.getProp().getProperty("DS.password");
        this.drivername = this.getProp().getProperty("DS.driver");
        try {
            Driver d = (Driver) Class.forName(this.drivername).newInstance();
            DriverManager.registerDriver(d);
            return DriverManager.getConnection(this.URL, this.user, this.password);
        } catch (Exception e) {
            String msg = "Problems connecting to " + this.name + ";" + this.URL + ";" + this.drivername + ";" + this.user + ";";
            throw new Exception(msg, e);
        }
    }
}
