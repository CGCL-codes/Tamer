package org.slasoi.businessManager.reporting.store;

import java.util.ArrayList;
import java.util.Date;
import org.slasoi.businessManager.reporting.report.Report;
import uk.ac.city.soi.database.EntityManagerInterface;

/**
 * @author Davide Lorenzoli
 * 
 * @date Feb 14, 2011
 */
public interface ReportEntityManager extends EntityManagerInterface<Report> {

    /**
	 * @param slaId
	 * @param reportingPolicyId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
    public ArrayList<Report> selectBySlaId(String slaId, String reportingPolicyId, Date fromDate, Date toDate, String format);

    /**
	 * @param slaId
	 * @param reportingPolicyId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
    public int countBySlaId(String slaId, String reportingPolicyId, Date fromDate, Date toDate);
}
