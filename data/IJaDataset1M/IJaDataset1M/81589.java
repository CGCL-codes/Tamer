package org.fpdev.core.data;

import java.sql.SQLException;

public interface CoreDB {

    public boolean initialized();

    public abstract void init() throws SQLException;

    public abstract String jdbcClassname();

    public abstract void close();

    public void connect();

    public void createQueryLogTables();

    public void createTripLinksTable();

    public void dropTable(String table);

    public void logTripQuery(int refid, int sessid, int scenid, int tripType, String start, String end, int time, String serviceID, int timeMode, double nmSpeed, double nmRadius, double bikeTypeFactor, double bikeTopFactor, int result);

    public void logLocationQuery(int refid, int sessid, int scenid, String loc, int result);

    public int initTripLinks(String links);

    public String getLinkList(int id);

    public void cleanup();

    public void ping();
}
