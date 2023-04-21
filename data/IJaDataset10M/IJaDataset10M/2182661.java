package org.groovyflow.db;

import java.sql.ResultSet;

public interface ResultSetCallback {

    void receive(ResultSet rs);
}
