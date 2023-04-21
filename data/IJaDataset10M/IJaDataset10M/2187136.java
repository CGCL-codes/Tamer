package dsb.mbc.odbc;

public class ConnectionConfiguration {

    static final class Defaults {

        public static final String DSN = "Unit 4 Multivers";

        public static final String Username = "ODBC";

        public static final String Password = "odbc";

        public static final String Hostname = "MVLOCAL";

        public static final String Database = "MVL20040";
    }

    private String dsn;

    private String username;

    private String password;

    private String hostname;

    private String database;

    public ConnectionConfiguration() {
        this(Defaults.DSN, Defaults.Username, Defaults.Password, Defaults.Hostname, Defaults.Database);
    }

    public ConnectionConfiguration(String dsn, String username, String password, String hostname, String database) {
        this.dsn = dsn;
        this.username = username;
        this.password = password;
        this.hostname = hostname;
        this.database = database;
    }

    public String getDSN() {
        return this.dsn;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getHostname() {
        return this.hostname;
    }

    public String getDatabase() {
        return this.database;
    }
}
