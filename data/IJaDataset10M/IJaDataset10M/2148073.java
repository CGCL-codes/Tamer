package com.apelon.dts.db;

import com.apelon.common.sql.SQL;
import com.apelon.common.xml.XML;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;
import org.w3c.dom.Element;

/**
 * Generic superclass of db's that serve concepts.
 *
 * @since DTS 3.0
 *
 * Copyright (c) 2006 Apelon, Inc. All rights reserved.
 */
public class SimpleConceptDb extends PropertyTypeDb {

    private static final String TABLE_KEY = "SIMPLE_CONCEPT_DB";

    /**
  * Set the database connection in {@linkplain  BasicDb BasicDb}
  * and initiate by storing and preparing SQL statements.
  *
  * @param newConn a java.sql.Connection for access to DTS schema.
  *
  * @since DTS 3.0
  */
    public SimpleConceptDb(Connection newConn) throws SQLException {
        super(newConn);
        init();
    }

    private void init() throws SQLException {
    }

    public void close() throws SQLException {
        super.close();
    }

    /**
  * Excute "rollback" SQL statement.
  *
  * @param root an Xml DOM Element holding data indicating a rollback.
  *
  * @return a boolean returns true if rollback is successful.
  *
  * @since DTS 3.0
  */
    public boolean executeRollback(Element root) throws SQLException {
        String root_name = root.getTagName().toString().toUpperCase();
        if (root_name.equals("ROLLBACK")) {
            Statement stmt = conn.createStatement();
            stmt.execute("rollback");
            stmt.close();
            return true;
        }
        return false;
    }

    protected String fullColumnName(String tableName, String columnName) {
        return tableName + "." + columnName;
    }

    protected void appendNameElem(StringBuffer docString, String value) {
        XML.asTagValue(docString, nameElement(), value);
    }

    protected void appendIdElem(StringBuffer docString, int value) {
        XML.asTagValue(docString, idElement(), "" + value);
    }

    protected void appendCodeElem(StringBuffer docString, String value) {
        XML.asTagValue(docString, codeElement(), value);
    }

    protected String nameElement() {
        return "name";
    }

    protected String idElement() {
        return "id";
    }

    protected String conceptElement() {
        return "dtsConcept";
    }

    protected String conceptsElement() {
        return "dtsConcepts";
    }

    protected String codeElement() {
        return "code";
    }
}
