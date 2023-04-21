package org.hl7.types.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hl7.types.BL;
import org.hl7.types.CD;
import org.hl7.types.CV;
import org.hl7.types.CodeValueFactory;
import org.hl7.types.ST;
import org.hl7.types.UID;

public class CVimplUsingUMLS extends CVimpl implements CV {

    protected CVimplUsingUMLS(ST code, UID codeSystem, String cui, String sab) {
        super(code, codeSystem);
        _cui = cui;
        _sab = sab;
    }

    private final String _cui;

    private final String _sab;

    public static CV valueOf(Properties parameters, ST code, UID codeSystem, ST originalText, ST displayName, ST codeSystemName, ST codeSystemVersion) {
        try {
            initConnection(parameters.getProperty("jdbc-driver"), parameters.getProperty("jdbc-url"));
            initCheckStatement(parameters.getProperty("jdbc-check-sql"));
        } catch (SQLException x) {
            throw new CodeValueFactory.Exception(x);
        }
        String sab = parameters.getProperty("SAB");
        if (sab == null) throw new CodeValueFactory.Exception("SAB is not known for codeSystem " + codeSystem);
        String cui = lookupCUI(code.toString(), sab);
        if (cui == null) throw new CodeValueFactory.Exception("unknown code " + code + " not known for SAB " + sab);
        CVimplUsingUMLS value = new CVimplUsingUMLS(code, codeSystem, sab, cui);
        value._originalText = originalText;
        value._displayName = displayName;
        value._codeSystemName = codeSystemName;
        value._codeSystemVersion = codeSystemVersion;
        return value;
    }

    public BL implies(CD x) {
        BL equality = this.equal(x);
        if (equality.isTrue()) return equality; else throw new UnsupportedOperationException();
    }

    public CD mostSpecificGeneralization(CD x) {
        throw new UnsupportedOperationException();
    }

    private static PreparedStatement _checkStatement = null;

    private static Connection _connection = null;

    private static void initCheckStatement(String sql) throws SQLException {
        if (sql != null && _checkStatement == null) {
            _checkStatement = _connection.prepareStatement(sql);
        }
    }

    private static void initConnection(String driver, String url) throws SQLException {
        if (url != null && _connection == null) {
            if (driver != null) try {
                Class.forName(driver);
            } catch (Exception x) {
                throw new SQLException("exception when loading driver class: " + x.getMessage());
            }
            _connection = DriverManager.getConnection(url);
        }
    }

    public static String lookupCUI(String code, String sab) {
        try {
            if (_checkStatement == null) throw new CodeValueFactory.Exception("jdbc-check-sql not initialized");
            synchronized (_checkStatement) {
                _checkStatement.setString(1, sab);
                _checkStatement.setString(2, code);
                ResultSet rs = _checkStatement.executeQuery();
                String cui = null;
                if (rs.next()) {
                    cui = rs.getString(1);
                    if (rs.wasNull()) return null;
                }
                return cui;
            }
        } catch (SQLException x) {
            throw new CodeValueFactory.Exception(x);
        }
    }
}

;
