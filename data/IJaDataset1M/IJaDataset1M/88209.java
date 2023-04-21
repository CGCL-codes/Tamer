package com.apelon.dts.db.admin;

import com.apelon.common.util.db.dao.GeneralDAO;
import java.sql.Connection;
import java.util.*;

/**
 * Context utility with database related parameters</p>
 *
 * @since DTS 3.4.0
 * Copyright (c) 2006 Apelon, Inc. All rights reserved.
 */
public class DBContext {

    public DBContext(Connection conn) {
        this.conn = conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public GeneralDAO getDao() {
        return dao;
    }

    public void setDao(GeneralDAO dao) {
        this.dao = dao;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List getTableNames() {
        return tableNames;
    }

    public void setTableNames(List tableNames) {
        this.tableNames = tableNames;
    }

    public List getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(List namespaces) {
        this.namespaces = namespaces;
    }

    public void addProperty(Object key, Object value) {
        this.properties.put(key, value);
    }

    public Object getProperty(Object key) {
        return this.properties.get(key);
    }

    private HashMap properties = new HashMap();

    private String contentType = "";

    private List tableNames;

    private List namespaces;

    private Connection conn;

    private GeneralDAO dao;
}
