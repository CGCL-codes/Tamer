package org.merak.core.persistence.jdbc.systems;

import org.merak.core.model.SettingException;
import org.merak.core.persistence.jdbc.DatabaseSystem;
import org.merak.core.persistence.jdbc.settings.ConnectionDetail;

public class Hsqldb extends DatabaseSystem {

    private final String standardDriver = "org.hsqldb.jdbcDriver";

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
        StringBuffer url = new StringBuffer("jdbc:hsqldb:");
        if (s.getDriverType() == null) s.setDriverType("file");
        url.append(s.getDriverType()).append(':');
        if ("hsql".equals(s.getDriverType())) {
            if (s.getHost() == null) throw new SettingException("Database Host is missing");
            url.append("//").append(s.getHost()).append((s.getPort() != null) ? ':' + s.getPort() : "");
        }
        url.append('/').append(s.getDatabase());
        return url.toString();
    }
}
