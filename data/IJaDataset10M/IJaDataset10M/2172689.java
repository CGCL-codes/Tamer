package org.encuestame.mvc.controller.json.project;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.encuestame.mvc.controller.AbstractJsonController;
import org.encuestame.persistence.exception.EnMeNoResultsFoundException;
import org.encuestame.utils.web.UnitProjectBean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Project Json Controller.
 * @author Picado, Juan juanATencuestame.org
 * @since Nov 15, 2010 7:53:25 PM
 * @version $Id:$
 */
@Controller
public class JsonProjectController extends AbstractJsonController {

    /**
     * Log.
     */
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Return list of projects.
     * @param request
     * @param response
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_USER')")
    @RequestMapping(value = "/api/admon/project/projects.json", method = RequestMethod.GET)
    public ModelMap get(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
        try {
            final List<UnitProjectBean> projects = getProjectService().loadListProjects(getUserPrincipalUsername());
            final Map<String, Object> jsonResponse = new HashMap<String, Object>();
            jsonResponse.put("projects", projects);
            setItemResponse(jsonResponse);
        } catch (EnMeNoResultsFoundException e) {
            setError(e.getMessage(), response);
            log.error(e);
        }
        return returnData();
    }

    /**
     * Create Project.
     * @param name
     * @param dateInit
     * @param dateFinish
     * @param state
     * @param description
     * @param projectInfo
     * @param leader
     * @param priority
     * @param status
     * @param hide
     * @param notify
     * @param published
     * @param request
     * @param response
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    @PreAuthorize("hasRole('ENCUESTAME_OWNER')")
    @RequestMapping(value = "/api/admon/project/create-project.json", method = RequestMethod.POST)
    public ModelMap createUser(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "dateInit", required = true) String dateInit, @RequestParam(value = "dateFinish", required = true) String dateFinish, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "description", required = true) String description, @RequestParam(value = "projectInfo", required = true) String projectInfo, @RequestParam(value = "leader", required = true) Long leader, @RequestParam(value = "priority", required = true) String priority, @RequestParam(value = "status", required = false) Boolean status, @RequestParam(value = "hide", required = false) Boolean hide, @RequestParam(value = "notify", required = false) Boolean notify, @RequestParam(value = "published", required = false) Boolean published, HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
        try {
            final UnitProjectBean projectBean = new UnitProjectBean();
            projectBean.setDateFinish(getFormatDate(dateFinish));
            projectBean.setDateInit(getFormatDate(dateInit));
            projectBean.setDescription(description);
            projectBean.setHide(hide);
            projectBean.setLeader(leader);
            projectBean.setName(filterValue(name));
            projectBean.setNotify(notify);
            projectBean.setPriority(priority);
            projectBean.setProjectInfo(projectInfo);
            projectBean.setPublished(published);
            projectBean.setState(state);
            projectBean.setStatus(1L);
            getProjectService().createProject(projectBean, getUserPrincipalUsername());
            setSuccesResponse();
            log.debug("Project Created");
        } catch (Exception e) {
            setError(e.getMessage(), response);
            log.error(e);
        }
        return returnData();
    }
}
