package org.opennms.web.controller.ksc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.opennms.netmgt.config.KSC_PerformanceReportFactory;
import org.opennms.netmgt.config.KscReportEditor;
import org.opennms.web.springframework.security.Authentication;
import org.opennms.web.MissingParameterException;
import org.opennms.web.WebSecurityUtils;
import org.opennms.web.XssRequestWrapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class FormProcMainController extends AbstractController implements InitializingBean {

    private KSC_PerformanceReportFactory m_kscReportFactory;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String action = WebSecurityUtils.sanitizeString(request.getParameter("report_action"));
        if (action == null) {
            throw new MissingParameterException("report_action");
        }
        KscReportEditor editor = KscReportEditor.getFromSession(request.getSession(), false);
        if (action.equals("View")) {
            ModelAndView modelAndView = new ModelAndView("redirect:/KSC/customView.htm");
            modelAndView.addObject("report", getReportIndex(request));
            modelAndView.addObject("type", "custom");
            return modelAndView;
        } else if (!request.isUserInRole(Authentication.READONLY_ROLE) && (request.getRemoteUser() != null)) {
            if (action.equals("Customize")) {
                editor.loadWorkingReport(getKscReportFactory(), getReportIndex(request));
                return new ModelAndView("redirect:/KSC/customReport.htm");
            } else if (action.equals("CreateFrom")) {
                editor.loadWorkingReportDuplicate(getKscReportFactory(), getReportIndex(request));
                return new ModelAndView("redirect:/KSC/customReport.htm");
            } else if (action.equals("Delete")) {
                getKscReportFactory().deleteReportAndSave(getReportIndex(request));
                return new ModelAndView("redirect:/KSC/index.htm");
            } else if (action.equals("Create")) {
                editor.loadNewWorkingReport();
                return new ModelAndView("redirect:/KSC/customReport.htm");
            }
        }
        throw new ServletException("Invalid Parameter contents for report_action: " + action);
    }

    private int getReportIndex(HttpServletRequest request) {
        String report = WebSecurityUtils.sanitizeString(request.getParameter("report"));
        if (report == null) {
            throw new MissingParameterException("report");
        }
        return WebSecurityUtils.safeParseInt(report);
    }

    public KSC_PerformanceReportFactory getKscReportFactory() {
        return m_kscReportFactory;
    }

    public void setKscReportFactory(KSC_PerformanceReportFactory kscReportFactory) {
        m_kscReportFactory = kscReportFactory;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.state(m_kscReportFactory != null, "property kscReportFactory must be set");
    }
}
