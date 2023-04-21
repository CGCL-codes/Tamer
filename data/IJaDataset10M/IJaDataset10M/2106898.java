package org.project.trunks.project.connection;

import org.project.trunks.connection.*;
import java.sql.*;
import org.project.trunks.exceptions.*;
import java.util.*;
import org.project.trunks.project.dataObject.*;
import org.apache.commons.logging.*;

public class DBTRKManager extends ApplicDBManager {

    /**
   * The logger, set to this class
   */
    private static Log log = LogFactory.getLog(DBTRKManager.class);

    public DBTRKManager() {
    }

    public DBTRKManager(String connect_id) {
        super(connect_id);
    }

    /**
   * isValidCode
   * @param code
   * @param sbLIB
   * @param sbYN_C2
   * @return
   * @throws java.lang.Exception
   */
    public boolean isValidCode(String code, StringBuffer sbLIB, StringBuffer sbYN_C2) throws Exception {
        log.info("<<<<<< DBTRKManager.isValidCode(" + code + ") >>>>>> BEGIN");
        Connection con = null;
        ResultSet rs = null;
        PreparedStatement stmt = null;
        Vector v = new Vector();
        try {
            String sQuery = " SELECT LIB, YN_C2 " + "   FROM Z_CODE_TYPE1 " + "  WHERE CODE = ? ";
            log.info("<<<<<< DBTRKManager.isValidCode(" + code + ") - sQuery = '" + sQuery + "'");
            con = getConnection();
            stmt = con.prepareStatement(sQuery);
            stmt.setString(1, code);
            rs = stmt.executeQuery();
            boolean b = rs.next();
            if (b) {
                sbLIB.append(rs.getString(1));
                sbYN_C2.append(rs.getString(2));
            }
            con.commit();
            return b;
        } catch (Exception e) {
            log.error(">>>>>> DBTRKManager.isValidCode(" + code + ") - EXCEPTION '" + e.getMessage() + "'");
            rollback(con);
            throw new GeneralException("DBTRKManager.isValidCode(" + code + ")", e.getMessage());
        } finally {
            log.info("<<<<<< DBTRKManager.isValidCode(" + code + ") >>>>>> END");
            closeQueryObjects(rs, stmt);
            closeConnection(con);
        }
    }
}
