package org.merak.core.persistence.jdbc.systems;

import org.merak.core.persistence.jdbc.DatabaseSystem;
import org.merak.core.persistence.jdbc.settings.ConnectionDetail;

public class Odbc extends DatabaseSystem {

    private final String standardDriver = "sun.jdbc.odbc.JdbcOdbcDriver";

    private final Integer standardPort = null;

    @Override
    public String getStandardDriver() {
        return this.standardDriver;
    }

    @Override
    public Integer getStandardPort() {
        return this.standardPort;
    }

    @Override
    public String formatUrl(ConnectionDetail s) throws Exception {
        return "jdbc:odbc:" + s.getDatabase();
    }
}
