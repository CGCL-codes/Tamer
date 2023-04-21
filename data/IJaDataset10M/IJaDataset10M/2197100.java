package org.openi.web.controller.admin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.openi.analysis.Analysis;
import org.openi.menu.AnalysisCollectionMenuVisitor;
import org.openi.project.Overview;
import org.openi.project.ProjectContext;
import org.openi.web.controller.FormItem;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

/**
 * Controller to configure dashboard
 */
public class ConfigureOverviewFormController extends SimpleFormController {

    private static Logger logger = Logger.getLogger(ConfigureOverviewFormController.class);

    private String selectOverviewFormView;

    private String configureOverviewFormView;

    /**
     * This method will create the command object and set it in its initial state.
     * This method is called before the user is directed to the first page of the wizard
     *
     * @param request HttpServletRequest
     * @return Object
     * @throws Exception
     *
     */
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        logger.debug("handling formBackingObject, request: " + request.getParameterMap());
        ProjectContext projectContext = (ProjectContext) request.getSession().getAttribute("projectContext");
        logger.debug("test config for select view: " + this.getSelectOverviewFormView());
        logger.debug("test config for configure view: " + this.getConfigureOverviewFormView());
        Map model = new HashMap();
        return model;
    }

    /**
     * Submit callback with all parameters. Called in case of submit without
     * errors reported by the registered validator, or on every submit if no validator.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param command Object
     * @param errors BindException
     * @return ModelAndView
     * @throws Exception
     */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ProjectContext projectContext = (ProjectContext) request.getSession().getAttribute("projectContext");
        Map model = (Map) command;
        logger.debug("command map: " + model);
        String action = request.getParameter("action");
        if (action == null) {
            if (request.getParameter("save") != null) action = "save";
        }
        if (action == null) {
            if (request.getParameter("cancel") != null) action = "cancel";
        }
        logger.debug("action: " + action);
        if (action.equalsIgnoreCase("configure")) {
            logger.debug("action=configure");
            String overviewKey = (String) request.getParameter("overviewKey");
            model.put("overviewKey", overviewKey);
            logger.debug("selecting overview key: " + overviewKey);
            Overview overview = projectContext.getProject().getOverview(overviewKey);
            model.put("targetOverview", overview);
            AnalysisCollectionMenuVisitor visitor = new AnalysisCollectionMenuVisitor(projectContext);
            projectContext.buildMenu().accept(visitor);
            model.put("analyses", wrap(visitor.getAnalyses(), overview));
            return new ModelAndView(this.getConfigureOverviewFormView(), model);
        } else if (action.equalsIgnoreCase("save")) {
            logger.debug("save action");
            String overviewKey = request.getParameter("overviewKey");
            if (projectContext.getProject().getOverviews() == null) {
                projectContext.getProject().setOverviews(new HashMap());
            }
            Overview overview = projectContext.getProject().getOverview(overviewKey);
            if (overview == null) {
                overview = new Overview();
                Map overviews = projectContext.getProject().getOverviews();
                overviews.put(overviewKey, overview);
            }
            String widthParam = request.getParameter("thumbnailWidth");
            if (widthParam != null && widthParam.length() > 0) {
                overview.setThumbnailWidth(this.parseInt(widthParam, 320));
            }
            String heightParam = request.getParameter("thumbnailHeight");
            if (heightParam != null && heightParam.length() > 0) {
                overview.setThumbnailHeight(this.parseInt(heightParam, 240));
            }
            model.put("targetOverview", overview);
            String[] analyses = request.getParameterValues("analyses");
            List links = new LinkedList();
            for (int i = 0; analyses != null && i < analyses.length; i++) {
                links.add(analyses[i]);
            }
            overview.setLinks(links);
            projectContext.saveProject();
        } else if (action.equalsIgnoreCase("new")) {
            logger.debug("new action");
            Overview overview = new Overview();
            model.put("targetOverview", overview);
            AnalysisCollectionMenuVisitor visitor = new AnalysisCollectionMenuVisitor(projectContext);
            projectContext.buildMenu().accept(visitor);
            model.put("analyses", wrap(visitor.getAnalyses(), overview));
            return new ModelAndView(this.getConfigureOverviewFormView(), model);
        } else if (action.equalsIgnoreCase("delete")) {
            logger.debug("delete action");
            Map overviews = projectContext.getProject().getOverviews();
            String overviewKey = request.getParameter("overviewKey");
            if (overviews.get(overviewKey) != null) {
                overviews.remove(overviewKey);
                projectContext.saveProject();
            }
            return new ModelAndView(this.getSelectOverviewFormView(), "model", model);
        }
        return super.onSubmit(request, response, command, errors);
    }

    private int parseInt(String parameter, int defaultValue) {
        int retVal = defaultValue;
        try {
            if (parameter != null && parameter.length() > 0) {
                retVal = Integer.parseInt(parameter);
            }
            if (retVal == 0) {
                retVal = defaultValue;
            }
        } catch (Exception e) {
        }
        return retVal;
    }

    private Map wrap(Map analyses, Overview targetOverview) {
        Map wrapped = new HashMap();
        Iterator keys = analyses.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Analysis value = (Analysis) analyses.get(key);
            FormItem formItem = new FormItem(value);
            formItem.setSelected(targetOverview.getLinks().contains(key));
            wrapped.put(key, formItem);
        }
        return wrapped;
    }

    public String getConfigureOverviewFormView() {
        return configureOverviewFormView;
    }

    public void setConfigureOverviewFormView(String configureOverviewFormView) {
        this.configureOverviewFormView = configureOverviewFormView;
    }

    public String getSelectOverviewFormView() {
        return selectOverviewFormView;
    }

    public void setSelectOverviewFormView(String selectOverviewFormView) {
        this.selectOverviewFormView = selectOverviewFormView;
    }
}
