package com.google.gwt.sample.expenses.shared;

import com.google.gwt.sample.expenses.server.domain.Report;
import com.google.web.bindery.requestfactory.shared.InstanceRequest;
import com.google.web.bindery.requestfactory.shared.Request;
import com.google.web.bindery.requestfactory.shared.RequestContext;
import com.google.web.bindery.requestfactory.shared.Service;
import java.util.List;

/**
 * Builds requests for the Report service.
 */
@Service(Report.class)
public interface ReportRequest extends RequestContext {

    /**
   * @return a request object
   */
    Request<Long> countReports();

    /**
   * @return a request object
   */
    Request<Long> countReportsBySearch(Long employeeId, String department, String startsWith);

    /**
   * @return a request object
   */
    Request<List<ReportProxy>> findAllReports();

    /**
   * @return a request object
   */
    Request<ReportProxy> findReport(Long id);

    /**
   * @return a request object
   */
    Request<List<ReportProxy>> findReportEntries(int firstResult, int maxResults);

    /**
   * @return a request object
   */
    Request<List<ReportProxy>> findReportEntriesBySearch(Long employeeId, String department, String startsWith, String orderBy, int firstResult, int maxResults);

    /**
   * @return a request object
   */
    Request<List<ReportProxy>> findReportsByEmployee(Long employeeId);

    /**
   * @return a request object
   */
    InstanceRequest<ReportProxy, Void> persist();

    /**
   * @return a request object
   */
    InstanceRequest<ReportProxy, Void> remove();
}
