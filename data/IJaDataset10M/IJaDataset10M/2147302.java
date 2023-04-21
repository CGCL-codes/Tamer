package net.sourceforge.fluxion.portal.controller;

import net.sourceforge.fluxion.portal.DatasourceBuilderService;
import net.sourceforge.fluxion.portal.User;
import net.sourceforge.fluxion.portal.exception.UnspecifiedConfigurationError;
import net.sourceforge.fluxion.portal.exception.DatasourceBuildException;
import net.sourceforge.fluxion.portal.impl.AnonymousUser;
import net.sourceforge.fluxion.portal.impl.DatapublisherDatasourceConfiguration;
import net.sourceforge.fluxion.ajax.beans.util.ProcessListener;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Javadocs go here!
 *
 * @author Tony Burdett
 * @date 26-Sep-2008
 */
public class DatasourceController extends AbstractController {

    private DatasourceBuilderService datasourceBuilderService;

    private String successView;

    public void setDatasourceBuilderService(DatasourceBuilderService datasourceBuilderService) {
        this.datasourceBuilderService = datasourceBuilderService;
    }

    public DatasourceBuilderService getDatasourceBuilderService() {
        return datasourceBuilderService;
    }

    public void setSuccessView(String successView) {
        this.successView = successView;
    }

    public String getSuccessView() {
        return successView;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletRequest.getSession(false).setAttribute("datasource_build_service_listener", getDatasourceBuilderService().getProgressListener());
        User user = new AnonymousUser();
        String groupId = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "groupId");
        String artifactId = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "artifactId");
        String version = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "version");
        String datasourceURL = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "datasourceURL");
        String datasourceFormat = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "datasourceFormat");
        String datasourceUser = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "datasourceUser");
        String datasourcePassword = ServletRequestUtils.getRequiredStringParameter(httpServletRequest, "datasourcePassword");
        DatapublisherDatasourceConfiguration config = new DatapublisherDatasourceConfiguration(user, groupId, artifactId, version, datasourceURL, datasourceFormat, datasourceUser, datasourcePassword);
        try {
            File datasourcePublisherWAR = getDatasourceBuilderService().buildDatasource(config);
            httpServletRequest.getSession().setAttribute(Constants.DATASOURCE_PUBLISHER_FILE.key(), datasourcePublisherWAR.getAbsolutePath());
            return new ModelAndView(getSuccessView());
        } catch (DatasourceBuildException e) {
            e.printStackTrace();
            Map<String, String> messageMap = new HashMap<String, String>();
            messageMap.put("message", "Cannot build datasource publisher");
            messageMap.put("exception", e.getMessage());
            return new ModelAndView("datasource_build_failure", messageMap);
        } catch (UnspecifiedConfigurationError unspecifiedConfigurationError) {
            unspecifiedConfigurationError.printStackTrace();
            Map<String, String> messageMap = new HashMap<String, String>();
            messageMap.put("message", "Cannot open deployed datasource publisher");
            messageMap.put("exception", unspecifiedConfigurationError.getMessage());
            return new ModelAndView("datasource_build_failure", messageMap);
        }
    }
}
