package org.merak.core.persistence.jdbc.systems;

import org.merak.core.model.SettingException;
import org.merak.core.persistence.jdbc.DatabaseSystem;
import org.merak.core.persistence.jdbc.settings.ConnectionDetail;

public class Postgresql extends DatabaseSystem {

    private final String standardDriver = "org.postgresql.Driver";

    private final Integer standardPort = 5432;

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
        if (s.getHost() == null) throw new SettingException("Database Host is missing");
        StringBuffer url = new StringBuffer("jdbc:postgresql://");
        url.append(s.getHost()).append((s.getPort() != null) ? ':' + s.getPort() : "");
        url.append("/").append(s.getDatabase());
        return url.toString();
    }
}
