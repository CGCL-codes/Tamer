package org.slasoi.businessManager.reporting.service;

import java.util.ArrayList;
import java.util.Date;
import org.slaatsoi.business.schema.ReportFormatType;
import org.slasoi.businessManager.reporting.report.Report;
import org.slasoi.slamodel.sla.SLA;

/**
 * This interface must be implemented by the post-sale reporting manager service.
 * It provides the methods for querying the generated report historical database
 * and for managing reporting policies.
 * 
 * @author Davide Lorenzoli
 * 
 * @date Feb 24, 2011
 */
public interface BSLAMReportingManagerService {

    /**
	 * Configure the post-sale reporting manager. Post-sale reporting manager
	 * reads reporting policies and sets up reporting generation and delivery
	 * accordingly.
	 * 
	 * @param test.org.slasoi.businessManager.reporting.utils The <code>SLA</code> object containing the reporting policy
	 * @return <code>true</code> if the operation succeed, <code>false</code> otherwise
	 */
    public boolean addSLA(SLA sla);

    /**
	 * Remove all reporting policies contained by an already added SLA
	 * whose unique identifier matches the given one.
	 * 
	 * @param slaId The SLA unique identifier
	 * @return <code>true</code> if the operation succeed, <code>false</code> otherwise
	 */
    public boolean removeSLA(String slaId);

    /**
	 * Selects, from the reporting manager historical database, already generated
	 * reports matching the selection criteria
	 *  
	 * @param slaId The SLA unique identifier
	 * @param reportingPolicyId The reporting policy unique identifier
	 * @param fromDate The past time point from which generated reports are considered
	 * @param toDate The past time point to which generated reports are considered
	 * @param format The report format type 
	 * @return The generated reports matching the selection criteria
	 */
    public ArrayList<Report> select(String slaId, String reportingPolicyId, Date fromDate, Date toDate, ReportFormatType format);
}
