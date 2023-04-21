package org.slasoi.businessManager.reporting.store;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import org.slasoi.businessManager.reporting.report.Report;
import uk.ac.city.soi.database.EntityManagerCommons;

/**
 * @author Davide Lorenzoli
 * @email Davide.Lorenzoli.1@soi.city.ac.uk
 * @date Apr 18, 2011
 */
public class ReportMyMSQLDatabaseManager extends EntityManagerCommons implements ReportDatabaseManager {

    private static Logger logger = Logger.getLogger(ReportMyMSQLDatabaseManager.class);

    private static final String DATABASE_TABLE_NAME = "reports";

    /**
	 * @param connection
	 */
    public ReportMyMSQLDatabaseManager(Connection connection) {
        super(connection);
        this.setDatabaseTable(DATABASE_TABLE_NAME);
    }

    /**
	 * @see org.slasoi.businessManager.reporting.store.ReportDatabaseManager#insert(org.slasoi.businessManager.reporting.report.Report)
	 */
    @Override
    public int insert(Report report) {
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        int result = 0;
        try {
            Blob blob = toBlob(report);
            pstmt = getConnection().prepareStatement("INSERT INTO " + getDatabaseTable() + " " + "(report_id, sla_id, creation_date, format, report_object) " + "VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, report.getReportId());
            pstmt.setString(2, report.getSlaId());
            pstmt.setLong(3, report.getCreationDate().getTime());
            pstmt.setString(4, report.getFormat().toString());
            pstmt.setBlob(5, blob);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return result;
    }

    /**
	 * @see org.slasoi.businessManager.reporting.store.ReportDatabaseManager#selectBySlaId(java.lang.String, java.lang.String, java.util.Date, java.util.Date, java.lang.String)
	 */
    @Override
    public ArrayList<Report> selectBySlaId(String slaId, String reportingPolicyId, Date fromDate, Date toDate, String format) {
        ArrayList<Report> reports = new ArrayList<Report>();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = getConnection().prepareStatement("SELECT report_object " + "FROM " + getDatabaseTable() + " " + "WHERE report_id = ? AND sla_id = ? AND creation_date >= ? AND creation_date <= ? AND format = ?");
            pstmt.setString(1, reportingPolicyId);
            pstmt.setString(2, slaId);
            pstmt.setLong(3, fromDate.getTime());
            pstmt.setLong(4, toDate.getTime());
            pstmt.setString(5, format);
            resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                do {
                    Blob blob = resultSet.getBlob("report_object");
                    Report report;
                    try {
                        report = (Report) toObject(blob);
                        reports.add(report);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return reports;
    }

    /**
	 * @see org.slasoi.businessManager.reporting.store.ReportDatabaseManager#countBySlaId(java.lang.String, java.lang.String, java.util.Date, java.util.Date)
	 */
    @Override
    public int countBySlaId(String slaId, String reportingPolicyId, Date fromDate, Date toDate) {
        int reports = 0;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = getConnection().prepareStatement("SELECT COUNT(*) AS reports " + "FROM " + getDatabaseTable() + " " + "WHERE report_id = ? AND sla_id = ? AND creation_date >= ? AND creation_date <= ?");
            pstmt.setString(1, reportingPolicyId);
            pstmt.setString(2, slaId);
            pstmt.setLong(3, fromDate.getTime());
            pstmt.setLong(4, toDate.getTime());
            resultSet = pstmt.executeQuery();
            if (resultSet.first()) {
                do {
                    reports = resultSet.getInt("reports");
                } while (resultSet.next());
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        } finally {
            releaseResources(pstmt, resultSet);
        }
        return reports;
    }

    /**
	 * @see org.slasoi.businessManager.reporting.store.ReportDatabaseManager#count()
	 */
    @Override
    public int count() {
        return super.count();
    }

    @Override
    public int update(Report paramT) {
        return 0;
    }

    @Override
    public int delete(Report paramT) {
        return 0;
    }

    @Override
    public int deleteByPrimaryKey(String paramString) {
        return 0;
    }

    @Override
    public Report selectByPrimaryKey(String paramString) {
        return null;
    }

    @Override
    public ArrayList<Report> select() {
        return null;
    }

    @Override
    public ArrayList<Report> executeQuery(String paramString) {
        return null;
    }
}
