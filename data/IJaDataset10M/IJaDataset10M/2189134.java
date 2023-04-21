package com.apelon.matchpack.db.config;

import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.LogConfigLoader;
import com.apelon.common.sql.SQL;
import com.apelon.common.util.db.DBSystemConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

/**
 * <p>Title: MatchLoadConfig</p>
 * <p>Description: A table(s) loading class for the Matching application</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Apelon, Inc.</p>
 * @version 1.0
 */
public class MatchLoadConfig implements DBSystemConfig {

    private static final String DTD = "matchconfig.dtd";

    private String MATCH_SPEC_TABLE = "DTS_MATCH_SPEC";

    private String MATCH_DB_TABLE = "DTS_MATCH";

    public MatchLoadConfig() {
    }

    public String getDaoDTDFileName() {
        return DTD;
    }

    public String getDaoDTDURL() {
        return "http://apelon.com/dtd/matchpack/" + DTD;
    }

    public Class getDaoDTDClass() {
        return MatchLoadConfig.class;
    }

    public InputStream getDaoConfig(String connectionType) throws IOException {
        URL url = null;
        InputStream inStream = null;
        if (connectionType.equals(SQL.ORACLE)) {
            url = com.apelon.matchpack.db.config.MatchLoadConfig.class.getResource("oracle.xml");
        } else if (connectionType.equals(SQL.SQL2K)) {
            url = com.apelon.matchpack.db.config.MatchLoadConfig.class.getResource("sql2k.xml");
        } else if (connectionType.equals(SQL.CACHE)) {
            url = com.apelon.matchpack.db.config.MatchLoadConfig.class.getResource("cache.xml");
        } else if (connectionType.equals(SQL.DB2)) {
            url = com.apelon.matchpack.db.config.MatchLoadConfig.class.getResource("db2.xml");
        } else {
            Categories.dataXml().error("* Problem: Unknown connection type: " + connectionType);
        }
        try {
            inStream = url.openStream();
        } catch (NullPointerException npe) {
            Categories.dataXml().error("* Problem: Undefined resource URL: " + npe.getMessage());
            throw npe;
        }
        return inStream;
    }

    public void init(Connection sourceConn, Connection targetConn) {
        this.sourceConn = sourceConn;
        this.targetConn = targetConn;
    }

    /**
   * This method returns an inputstream of table lists to be loaded
   * into the target schema.
   * The invoker will eventually read the list of tables from this stream
   * and invoke its corresponding class i.e. MATCH_SPEC will result in
   * invoking the TableMATCH_SPEC class.
   *
   * @return inputstream
   * @throws IOException
   */
    public InputStream getTableLists() throws IOException {
        String sb = new String("<?xml version=\"1.0\" ?>" + "<!DOCTYPE DTSDbConfig SYSTEM \"matchconfig.dtd\">" + "<DTSDbConfig>" + "<property name=\"prefix\" value=\"com.apelon.matchpack.db.table\" />" + "<table name=\"" + MATCH_SPEC_TABLE + "\" />" + "<table name=\"" + MATCH_DB_TABLE + "\" />" + "</DTSDbConfig>");
        Categories.dataXml().debug("Table List generated for the Matchpack : \n" + sb);
        byte[] bytes = sb.getBytes("UTF-8");
        InputStream input = new ByteArrayInputStream(bytes);
        return input;
    }

    public LogConfigLoader logConfigLoader() {
        return new LogConfigLoader("matchloadlog.xml", MatchLoadConfig.class);
    }

    public void validateInput(Map propertyMap) throws Exception {
    }

    public String[] getConnectionProperties() {
        return null;
    }

    private Connection sourceConn = null;

    private Connection targetConn = null;
}
