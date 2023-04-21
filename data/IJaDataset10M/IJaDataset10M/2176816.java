package nickyb.sqleonardo.environment.ctrl.explorer;

import nickyb.sqleonardo.common.jdbc.ConnectionAssistant;
import nickyb.sqleonardo.environment.ctrl.MetadataExplorer;

public class UoDriver {

    public String name = new String("new driver name");

    public String library = null;

    public String classname = new String();

    public String example = new String();

    public String message = null;

    public String getKey() {
        return library + "$" + classname;
    }

    public String toString() {
        return name;
    }

    public static void loadDefaults(MetadataExplorer explorer) {
        UoDriver[] drivers = new UoDriver[10];
        drivers[0] = new UoDriver();
        drivers[0].name = "ODBC Bridge";
        drivers[0].classname = "sun.jdbc.odbc.JdbcOdbcDriver";
        drivers[0].example = "jdbc:odbc:<data source name>";
        drivers[1] = new UoDriver();
        drivers[1].name = "Apache Derby";
        drivers[1].classname = "org.apache.derby.jdbc.ClientDriver";
        drivers[1].example = "jdbc:derby:net://<host>:<port1527>/<databaseName>";
        drivers[2] = new UoDriver();
        drivers[2].name = "HSQLDB - Embedded";
        drivers[2].classname = "org.hsqldb.jdbcDriver";
        drivers[2].example = "jdbc:hsqldb:<database>";
        drivers[3] = new UoDriver();
        drivers[3].name = "HSQLDB - Server";
        drivers[3].classname = "org.hsqldb.jdbcDriver";
        drivers[3].example = "jdbc:hsqldb:hsql://<host>:<port>";
        drivers[4] = new UoDriver();
        drivers[4].name = "jTDS - SQL Server";
        drivers[4].classname = "net.sourceforge.jtds.jdbc.Driver";
        drivers[4].example = "jdbc:jtds:sqlserver://<server>[:<port>][/<database>]";
        drivers[5] = new UoDriver();
        drivers[5].name = "jTDS - Sybase";
        drivers[5].classname = "net.sourceforge.jtds.jdbc.Driver";
        drivers[5].example = "jdbc:jtds:sybase://<server>[:<port>][/<database>]";
        drivers[6] = new UoDriver();
        drivers[6].name = "MySQL";
        drivers[6].classname = "com.mysql.jdbc.Driver";
        drivers[6].example = "jdbc:mysql://<host>:<port3306>/<database>";
        drivers[7] = new UoDriver();
        drivers[7].name = "Oracle Thin";
        drivers[7].classname = "oracle.jdbc.OracleDriver";
        drivers[7].example = "jdbc:oracle:thin@<host>:<port1521>:<SID>";
        drivers[8] = new UoDriver();
        drivers[8].name = "Oracle OCI";
        drivers[8].classname = "oracle.jdbc.OracleDriver";
        drivers[8].example = "jdbc:oracle:oci@<host>:<port1521>:<SID>";
        drivers[9] = new UoDriver();
        drivers[9].name = "PostgreSQL";
        drivers[9].classname = "org.postgresql.Driver";
        drivers[9].example = "jdbc:postgresql://<host>:<port5432>/<database>";
        for (int i = 0; i < drivers.length; i++) {
            try {
                ConnectionAssistant.declare(null, drivers[i].classname, true);
            } catch (Exception e) {
                drivers[i].message = e.toString();
            } finally {
                explorer.getNavigator().add(drivers[i], false);
            }
        }
        explorer.getNavigator().sort(explorer.getNavigator().getRootNode());
    }
}
