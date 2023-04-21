package de.objectcode.time4u.server.web.gwt.report.server.dao;

import java.util.Map;
import de.objectcode.time4u.server.ejb.seam.api.report.BaseReportDefinition;
import de.objectcode.time4u.server.ejb.seam.api.report.parameter.BaseParameterValue;
import de.objectcode.time4u.server.web.gwt.report.client.service.ReportTableData;

public interface IReportDao {

    ReportTableData generateReport(BaseReportDefinition reportDefinition, Map<String, BaseParameterValue> parameters, String personId);
}
