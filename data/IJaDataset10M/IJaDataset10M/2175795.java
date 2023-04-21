package com.jujunie.project1901;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import com.jujunie.project1901.report.Parameter;
import com.jujunie.project1901.report.Report;
import com.jujunie.project1901.report.ReportManager;
import com.jujunie.service.Container;
import com.jujunie.service.web.CommandResult;
import com.jujunie.service.web.HttpServletRequestContainer;
import com.jujunie.service.web.WebController;

public class CMDReportOpen extends CMDBase {

    @Override
    protected CommandResult execute1901Command(WebController wc, HttpServletRequest req) throws Project1901Exception {
        Container container = new HttpServletRequestContainer(req);
        SessionManager sman = SessionManager.getInstance(req);
        Report r = sman.getReports().getCustom(req.getParameter("code"));
        ReportManager.store(r, container);
        Map<String, Object> params = new HashMap<String, Object>();
        String reqParam = null;
        Organisation organisation = sman.getOrganisation();
        for (Parameter rp : r.getParameters()) {
            switch(rp.getType()) {
                case ORGANISATION:
                    params.put(rp.getKey(), organisation.getCode());
                    break;
                case EXERCICE:
                    reqParam = req.getParameter("exercice");
                    Exercice exercice = null;
                    if (StringUtils.isBlank(reqParam)) {
                        exercice = sman.getCurrentExercice();
                    } else {
                        exercice = organisation.getExercice(reqParam);
                    }
                    params.put(rp.getKey(), exercice.getCode());
                    break;
                case SECTION:
                    reqParam = req.getParameter("section");
                    if (StringUtils.isNotBlank(reqParam)) {
                        params.put(rp.getKey(), organisation.getSection(reqParam).getCode());
                    }
                    break;
                case CONSTANT:
                    params.put(rp.getKey(), rp.getDefaultValue());
                    break;
                case LOCALE:
                    params.put(rp.getKey(), req.getLocale());
                    break;
                case RESOURCE_BUNDLE:
                    params.put(rp.getKey(), DILang.getResourceBundle(req.getLocale()));
                    break;
                default:
                    throw new IllegalStateException("Unknown parameter type: " + rp.getType());
            }
        }
        ReportManager.storeParams(params, container);
        return null;
    }
}
