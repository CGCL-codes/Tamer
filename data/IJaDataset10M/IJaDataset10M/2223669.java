package org.opennms.web.controller.ksc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opennms.netmgt.config.KSC_PerformanceReportFactory;
import org.opennms.netmgt.config.KscReportEditor;
import org.opennms.netmgt.config.kscReports.Graph;
import org.opennms.netmgt.config.kscReports.Report;
import org.opennms.web.MissingParameterException;
import org.opennms.web.WebSecurityUtils;
import org.opennms.web.svclayer.KscReportService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class FormProcViewController extends AbstractController implements InitializingBean {

    private KSC_PerformanceReportFactory m_kscReportFactory;

    private KscReportService m_kscReportService;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        int report_index = -1;
        String override_timespan = null;
        String override_graphtype = null;
        String report_action = WebSecurityUtils.sanitizeString(request.getParameter("action"));
        String domain = WebSecurityUtils.sanitizeString(request.getParameter("domain"));
        if (report_action == null) {
            throw new MissingParameterException("action", new String[] { "action", "report", "type" });
        }
        String report_type = WebSecurityUtils.sanitizeString(request.getParameter("type"));
        if (report_type == null) {
            throw new MissingParameterException("type", new String[] { "action", "report", "type" });
        }
        if (report_action.equals("Customize") || report_action.equals("Update")) {
            String r_index = WebSecurityUtils.sanitizeString(request.getParameter("report"));
            if (r_index != null && !r_index.equals("null")) {
                report_index = WebSecurityUtils.safeParseInt(r_index);
            } else if (domain == null) {
                throw new MissingParameterException("report or domain", new String[] { "report or domain", "type" });
            }
            override_timespan = WebSecurityUtils.sanitizeString(request.getParameter("timespan"));
            if ((override_timespan == null) || override_timespan.equals("null")) {
                override_timespan = "none";
            }
            override_graphtype = WebSecurityUtils.sanitizeString(request.getParameter("graphtype"));
            if (override_graphtype == null || override_graphtype.equals("null")) {
                override_graphtype = "none";
            }
            if (report_action.equals("Customize")) {
                KscReportEditor editor = KscReportEditor.getFromSession(request.getSession(), false);
                if (report_type.equals("node")) {
                    editor.loadWorkingReport(m_kscReportService.buildNodeReport(report_index));
                } else if (report_type.equals("domain")) {
                    editor.loadWorkingReport(m_kscReportService.buildDomainReport(domain));
                } else {
                    editor.loadWorkingReport(getKscReportFactory(), report_index);
                }
                Report working_report = editor.getWorkingReport();
                for (int i = 0; i < working_report.getGraphCount(); i++) {
                    Graph working_graph = working_report.getGraph(i);
                    if (!override_timespan.equals("none")) {
                        working_graph.setTimespan(override_timespan);
                    }
                    if (!override_graphtype.equals("none")) {
                        working_graph.setGraphtype(override_graphtype);
                    }
                }
            }
        } else {
            if (!report_action.equals("Exit")) {
                throw new ServletException("Invalid Parameter contents for report_action");
            }
        }
        if (report_action.equals("Update")) {
            ModelAndView modelAndView = new ModelAndView("redirect:/KSC/customView.htm");
            modelAndView.addObject("type", report_type);
            if (report_index >= 0) {
                modelAndView.addObject("report", report_index);
            }
            if (domain != null) {
                modelAndView.addObject("domain", domain);
            }
            if (override_timespan != null) {
                modelAndView.addObject("timespan", override_timespan);
            }
            if (override_graphtype != null) {
                modelAndView.addObject("graphtype", override_graphtype);
            }
            return modelAndView;
        } else if (report_action.equals("Customize")) {
            return new ModelAndView("redirect:/KSC/customReport.htm");
        } else if (report_action.equals("Exit")) {
            return new ModelAndView("redirect:/KSC/index.htm");
        } else {
            throw new IllegalArgumentException("parameter action of '" + report_action + "' is not supported.  Must be one of: Update, Customize, or Exit");
        }
    }

    public KSC_PerformanceReportFactory getKscReportFactory() {
        return m_kscReportFactory;
    }

    public void setKscReportFactory(KSC_PerformanceReportFactory kscReportFactory) {
        m_kscReportFactory = kscReportFactory;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(m_kscReportFactory != null, "property kscReportFactory must be set");
        Assert.state(m_kscReportService != null, "property kscReportService must be set");
    }

    public KscReportService getKscReportService() {
        return m_kscReportService;
    }

    public void setKscReportService(KscReportService kscReportService) {
        m_kscReportService = kscReportService;
    }
}
