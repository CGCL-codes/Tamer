package com.ecs.etrade.bo.reports;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.ecs.etrade.da.Reports;
import com.ecs.etrade.da.ReportsDAO;

/**
 * @author Alok Ranjan
 *
 */
public class ReportsManagerImpl implements ReportsManager {

    private static final Log log = LogFactory.getLog(ReportsManagerImpl.class);

    private ReportsDAO reportsDAO;

    /**
	 * 
	 */
    public ReportsManagerImpl() {
    }

    public ArrayList<Reports> getAllReports() throws Exception {
        try {
            ArrayList<Reports> reportsList = (ArrayList<Reports>) reportsDAO.findAll();
            return reportsList;
        } catch (Exception e) {
            String logStr = "Failed to retrieve all the reports. " + e.getMessage();
            log.error(logStr);
            throw new Exception(logStr);
        }
    }

    public Reports getReport(final String reportName) throws Exception {
        try {
            ArrayList<Reports> reportsList = (ArrayList<Reports>) reportsDAO.findByProperty(ReportsDAO.REPORT_NAME, reportName);
            if (reportsList != null && reportsList.size() > 0) {
                return reportsList.get(0);
            } else {
                return null;
            }
        } catch (Exception e) {
            String logStr = "Failed to retrieve reports for REPORT_NAME = " + reportName + ". " + e.getMessage();
            log.error(logStr);
            throw new Exception(logStr);
        }
    }

    /**
	 * @return the reportsDAO
	 */
    public ReportsDAO getReportsDAO() {
        return reportsDAO;
    }

    /**
	 * @param reportsDAO the reportsDAO to set
	 */
    public void setReportsDAO(ReportsDAO reportsDAO) {
        this.reportsDAO = reportsDAO;
    }
}
