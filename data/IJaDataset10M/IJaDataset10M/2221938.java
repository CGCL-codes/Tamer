package org.opennms.web.svclayer;

import org.opennms.api.reporting.parameter.ReportParameters;
import org.springframework.webflow.execution.RequestContext;

/**
 * <p>DatabaseReportService interface.</p>
 *
 * @author ranger
 * @version $Id: $
 * @since 1.8.1
 */
public interface DatabaseReportService {

    /**
     * <p>execute</p>
     *
     * @param criteria a {@link org.opennms.api.reporting.parameter.ReportParameters} object.
     * @param context a {@link org.springframework.webflow.execution.RequestContext} object.
     * @return a {@link java.lang.String} object.
     */
    public String execute(ReportParameters criteria, RequestContext context);
}
