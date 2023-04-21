package org.amiwall.instrument;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import org.amiwall.db.ConnectionPool;
import org.amiwall.policy.DateHolder;
import org.amiwall.policy.DateHolderComparator;
import org.apache.log4j.Logger;

/**
 *  Description of the Class
 *
 *@author    Nick Cunnah
 */
public abstract class AbstractDbPsInstrument extends AbstractDbInstrument {

    Logger log = Logger.getLogger("org.amiwall.instrument.AbstractDbPsInstrument");

    /**
     *  Description of the Field
     */
    protected String calcMetricSql;

    /**
     *  Gets the name attribute of the TotalResponseBandwidth object
     *
     *@return    The name value
     */
    public String getName() {
        return "WebMailRequests";
    }

    /**
     *  Description of the Method
     *
     *@exception  Exception  Description of the Exception
     */
    public void activate() throws Exception {
        if (log.isDebugEnabled()) log.debug("activate");
        super.activate();
        calcMetricSql = getSql();
        if (calcMetricSql == null) {
            throw new Exception("Cant find resource calcmetric.sql");
        }
    }

    /**
     *  Gets the sql attribute of the AbstractDbPsInstrument object
     *
     *@return                The sql value
     *@exception  Exception  Description of the Exception
     */
    public abstract String getSql() throws Exception;

    /**
     *  Description of the Method
     *
     *@param  userId         Description of the Parameter
     *@param  startTime      Description of the Parameter
     *@param  endTime        Description of the Parameter
     *@return                Description of the Return Value
     *@exception  Exception  Description of the Exception
     */
    public long calcMetric(String userId, long startTime, long endTime) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        long metric = 0;
        try {
            con = ConnectionPool.getConnection();
            pstmt = con.prepareStatement(calcMetricSql);
            pstmt.setString(1, userId);
            pstmt.setLong(2, startTime);
            pstmt.setLong(3, endTime);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                metric = rs.getLong(1);
            }
            rs.close();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        if (log.isDebugEnabled()) {
            SimpleDateFormat df = new SimpleDateFormat();
            if (log.isDebugEnabled()) log.debug("userId=" + userId + " " + df.format(new Date(startTime)) + "->" + df.format(new Date(endTime)) + " metric=" + metric);
        }
        return metric;
    }
}
