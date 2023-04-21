package com.gn.mporras.xml;

import com.ibatis.common.resources.Resources;
import com.ibatis.sqlmap.client.*;
import java.io.*;
import java.util.logging.*;

public class XML {

    private static SqlMapClient sqlMapClient = null;

    static {
        System.err.println("---- Generating SqlMapClient");
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("com/gn/mporras/xml/sql-map-config.xml");
            sqlMapClient = SqlMapClientBuilder.buildSqlMapClient(reader);
        } catch (IOException ex) {
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static SqlMapClient getSqlMapClient() {
        return sqlMapClient;
    }
}
