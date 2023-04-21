package de.zeiban.loppe.dbcore;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParamProvider {

    void fillParams(PreparedStatement stmt) throws SQLException;
}
