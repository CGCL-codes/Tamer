package pl.o2.asdluki.oszwo.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import pl.o2.asdluki.oszwo.model.MenagerProject;
import pl.o2.asdluki.oszwo.model.Project;
import pl.o2.asdluki.oszwo.model.User;
import pl.o2.asdluki.oszwo.util.ApplicationSecurityManager;

public class CreateProjectController extends SimpleFormController {

    private ApplicationSecurityManager applicationSecurityManager;

    private MenagerProject menagerProject;

    public Object formBackingObject(HttpServletRequest request) {
        Project project = new Project(request.getParameter("name"), (User) applicationSecurityManager.getUser(request));
        project.setDescription(request.getParameter("description"));
        return project;
    }

    public ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors) {
        Map<String, String> map = new HashMap<String, String>();
        if (errors.hasErrors()) {
            if (errors.getGlobalError().getCode().equals(Constant.ERROR_NAME_IS_EMPTY)) {
                map.put("message", Constant.ERROR_NAME_IS_EMPTY);
                map.put("description", (String) errors.getFieldValue("description"));
            } else if (errors.getGlobalError().getCode().equals(Constant.ERROR_NAME_IS_UNAVAILABLE)) {
                map.put("message", Constant.ERROR_NAME_IS_UNAVAILABLE);
                map.put("description", (String) errors.getFieldValue("description"));
                map.put("name", (String) errors.getFieldValue("name"));
            }
            return new ModelAndView(getFormView(), map);
        }
        return new ModelAndView(getFormView());
    }

    protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
    }

    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        response.sendRedirect("project.html?id=" + menagerProject.saveProject((Project) command));
        return new ModelAndView("index");
    }

    public ApplicationSecurityManager getApplicationSecurityManager() {
        return applicationSecurityManager;
    }

    public void setApplicationSecurityManager(ApplicationSecurityManager applicationSecurityManager) {
        this.applicationSecurityManager = applicationSecurityManager;
    }

    public MenagerProject getMenagerProject() {
        return menagerProject;
    }

    public void setMenagerProject(MenagerProject menagerProject) {
        this.menagerProject = menagerProject;
    }
}
