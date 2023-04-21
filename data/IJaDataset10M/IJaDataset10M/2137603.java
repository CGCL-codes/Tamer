package org.telscenter.sail.webapp.presentation.web.controllers.teacher.grading;

import java.io.IOException;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.sail.webapp.domain.impl.CurnitGetCurnitUrlVisitor;
import net.sf.sail.webapp.presentation.web.controllers.ControllerUtil;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.telscenter.pas.emf.pas.ECurnitmap;
import org.telscenter.sail.webapp.domain.Run;
import org.telscenter.sail.webapp.domain.project.impl.ProjectTypeVisitor;
import org.telscenter.sail.webapp.presentation.util.json.JSONException;
import org.telscenter.sail.webapp.presentation.util.json.JSONObject;
import org.telscenter.sail.webapp.service.grading.GradingService;
import org.telscenter.sail.webapp.service.offering.RunService;

/**
 * A Controller for TELS's grade by step
 *
 * @author Anthony Perritano
 * @version $Id: $
 */
public class GradeByStepController extends AbstractController {

    public static final String RUN_ID = "runId";

    public static final String CURNIT_MAP = "curnitMap";

    private static final String GRADE_BY_STEP_URL = "getGradeByStepUrl";

    private GradingService gradingService;

    private RunService runService;

    private Properties portalProperties = null;

    /**
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String runId = request.getParameter(RUN_ID);
        Run run = runService.retrieveById(new Long(runId));
        String action = request.getParameter("action");
        if (action != null) {
            if (action.equals("getGradingConfig")) {
                return handleGetGradingConfig(request, response, run);
            }
        } else {
            ProjectTypeVisitor typeVisitor = new ProjectTypeVisitor();
            String result = (String) run.getProject().accept(typeVisitor);
            if (result.equals("LDProject")) {
                String portalurl = ControllerUtil.getBaseUrlString(request);
                String getGradeByStepUrl = portalurl + "/vlewrapper/vle/gradebystep.html";
                String getGradingConfigUrl = portalurl + "/webapp/request/info.html?action=getVLEConfig&runId=" + run.getId().toString() + "&requester=grading";
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(RUN_ID, runId);
                modelAndView.addObject("run", run);
                modelAndView.addObject(GRADE_BY_STEP_URL, getGradeByStepUrl);
                modelAndView.addObject("getGradingConfigUrl", getGradingConfigUrl);
                return modelAndView;
            } else if (runId != null) {
                ECurnitmap curnitMap = gradingService.getCurnitmap(new Long(runId));
                ModelAndView modelAndView = new ModelAndView();
                modelAndView.addObject(RUN_ID, runId);
                modelAndView.addObject(CURNIT_MAP, curnitMap);
                return modelAndView;
            } else {
            }
        }
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /**
	 * Generates and returns the grading config JSON
	 * @param request
	 * @param response
	 * @param run
	 * @return
	 * @throws ObjectNotFoundException
	 * @throws IOException
	 */
    private ModelAndView handleGetGradingConfig(HttpServletRequest request, HttpServletResponse response, Run run) throws ObjectNotFoundException, IOException {
        String portalurl = ControllerUtil.getBaseUrlString(request);
        String curriculumBaseWWW = portalProperties.getProperty("curriculum_base_www");
        String getContentUrl = curriculumBaseWWW + (String) run.getProject().getCurnit().accept(new CurnitGetCurnitUrlVisitor());
        int lastIndexOfDot = getContentUrl.lastIndexOf(".");
        String getProjectMetadataUrl = getContentUrl.substring(0, lastIndexOfDot) + ".meta.json";
        int lastIndexOfSlash = getContentUrl.lastIndexOf("/");
        if (lastIndexOfSlash == -1) {
            lastIndexOfSlash = getContentUrl.lastIndexOf("\\");
        }
        String getContentBaseUrl = getContentUrl.substring(0, lastIndexOfSlash) + "/";
        String portalVLEControllerUrl = portalurl + "/webapp/student/vle/vle.html?runId=" + run.getId();
        String getUserInfoUrl = portalVLEControllerUrl + "&action=getUserInfo";
        String getRunInfoUrl = portalVLEControllerUrl + "&action=getRunInfo";
        String getRunExtrasUrl = portalVLEControllerUrl + "&action=getRunExtras";
        String getStudentDataUrl = portalurl + "/webapp/bridge/getdata.html";
        String postStudentDataUrl = portalurl + "/webapp/bridge/postdata.html";
        String getFlagsUrl = portalurl + "/webapp/bridge/getdata.html?type=flag&runId=" + run.getId().toString();
        String postFlagsUrl = portalurl + "/webapp/bridge/getdata.html?type=flag&runId=" + run.getId().toString();
        String getAnnotationsUrl = portalurl + "/webapp/bridge/request.html?type=annotation&runId=" + run.getId().toString();
        String postAnnotationsUrl = portalurl + "/webapp/bridge/request.html?type=annotation&runId=" + run.getId().toString();
        String postMaxScoreUrl = portalurl + "/webapp/teacher/grading/gradebystep.html?action=postMaxScore&runId=" + run.getId().toString();
        JSONObject config = new JSONObject();
        try {
            config.put("mode", "grading");
            config.put("runId", run.getId().toString());
            config.put("getFlagsUrl", getFlagsUrl);
            config.put("postFlagsUrl", postFlagsUrl);
            config.put("getAnnotationsUrl", getAnnotationsUrl);
            config.put("postAnnotationsUrl", postAnnotationsUrl);
            config.put("getUserInfoUrl", getUserInfoUrl);
            config.put("getContentUrl", getContentUrl);
            config.put("getContentBaseUrl", getContentBaseUrl);
            config.put("getStudentDataUrl", getStudentDataUrl);
            config.put("postStudentDataUrl", postStudentDataUrl);
            config.put("getRunInfoUrl", getRunInfoUrl);
            config.put("getProjectMetadataUrl", getProjectMetadataUrl);
            config.put("getRunExtrasUrl", getRunExtrasUrl);
            config.put("postMaxScoreUrl", postMaxScoreUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("text/xml");
        response.getWriter().print(config.toString());
        return null;
    }

    /**
	 * @param gradingService the gradingService to set
	 */
    public void setGradingService(GradingService gradingService) {
        this.gradingService = gradingService;
    }

    /**
	 * @param runService the runService to set
	 */
    public void setRunService(RunService runService) {
        this.runService = runService;
    }

    /**
	 * @param portalProperties the portalProperties to set
	 */
    @Required
    public void setPortalProperties(Properties portalProperties) {
        this.portalProperties = portalProperties;
    }
}
