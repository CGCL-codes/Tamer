package cz.muni.fi.pclis.web.controllers.student.team;

import cz.muni.fi.pclis.domain.UploadedFile;
import cz.muni.fi.pclis.web.annotations.Role;
import cz.muni.fi.pclis.domain.team.TeamProject;
import cz.muni.fi.pclis.service.file.FileStorageService;
import cz.muni.fi.pclis.service.file.FileStorageServiceImpl;
import cz.muni.fi.pclis.service.team.TeamProjectService;
import cz.muni.fi.pclis.web.annotations.AllowedRole;
import cz.muni.fi.pclis.web.beans.MultipartFileUploadBean;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Ľuboš Pecho
 * Date: 28.3.2010
 * Time: 1:50:50
 * To change this template use File | Settings | File Templates.
 */
@Component("addFileToProjectController")
@AllowedRole(Role.STUDENT)
public class AddFileToProjectController extends SimpleFormController {

    private FileStorageService fileStorageService = new FileStorageServiceImpl();

    private TeamProjectService teamProjectService;

    public AddFileToProjectController() {
        setCommandClass(MultipartFileUploadBean.class);
        setFormView("addFileToProject");
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map map = new HashMap();
        map.put("projectId", request.getParameter("projectId"));
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        MultipartFileUploadBean bean = (MultipartFileUploadBean) command;
        MultipartFile file = bean.getFile();
        TeamProject teamProject = teamProjectService.getById(Long.parseLong(request.getParameter("projectId")));
        if (file != null) {
            String fileName = file.getOriginalFilename();
            fileStorageService.createDirRoot("project" + File.separator + teamProject.getId());
            File targetFile = fileStorageService.getTargetFile("project" + File.separator + teamProject.getId() + File.separator + fileName);
            file.transferTo(targetFile);
            teamProject.addFile(new UploadedFile(fileName, targetFile.getAbsolutePath(), file.getContentType()));
            teamProjectService.update(teamProject);
        }
        return new ModelAndView("redirect:teamProjectDetailStudent.htm?id=" + teamProject.getId());
    }

    @Resource
    public void setTeamProjectService(TeamProjectService teamProjectService) {
        this.teamProjectService = teamProjectService;
    }
}
