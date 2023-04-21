package org.skins.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class OracleSequence implements Sequence {

    private String name;

    public OracleSequence(String name) {
        this.name = name;
    }

    public String getQuery() {
        return "select " + name + ".nextval from dual";
    }
}
