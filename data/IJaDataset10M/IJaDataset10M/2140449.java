package kr.ac.ssu.imc.durubi.report.designer.components.database;

public class DRJDBCDataSource extends DRDataSource {

    private DRJDBCDriverType driverType;

    private String host;

    private String port;

    private String id;

    private String password;

    private String schema;

    public DRJDBCDataSource(DRDataSourceType type, String name, DRJDBCDriverType driverType, String host, String port, String id, String password, String schema) {
        super(type, name);
        this.driverType = driverType;
        this.host = host;
        this.port = port;
        this.id = id;
        this.password = password;
        this.schema = schema;
    }

    public DRJDBCDriverType getDriverType() {
        return driverType;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getSchema() {
        return schema;
    }
}
