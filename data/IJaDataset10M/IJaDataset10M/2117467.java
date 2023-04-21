package org.fao.fenix.web.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.fao.fenix.domain.project.Project;
import org.fao.fenix.domain.project.ProjectObject;
import org.fao.fenix.persistence.perspective.ProjectDao;
import org.fao.fenix.web.client.FenixResource;
import org.fao.fenix.web.client.services.ProjectService;
import org.fao.fenix.web.client.vo.FenixResourceMetadataVo;
import org.fao.fenix.web.utils.DomainVoMapper;
import org.fao.fenix.web.utils.FenixResourceBuilder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProjectServiceImpl extends RemoteServiceServlet implements ProjectService {

    DomainVoMapper mapper = new DomainVoMapper();

    ProjectDao projectDao;

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public List<FenixResourceMetadataVo> getProjectList() {
        List<Project> projectList = projectDao.findAll();
        List<FenixResourceMetadataVo> list = new ArrayList<FenixResourceMetadataVo>();
        for (Project project : projectList) {
            list.add(mapper.do2vo(project));
        }
        return list;
    }

    public void addResourceTo(Long projectId, Long resourceId) {
        Project project = projectDao.findById(projectId);
        ProjectObject projectObject = projectDao.findProjectObjectById(resourceId);
        project.addProjectObject(projectObject);
        projectDao.update(project);
    }

    public Long create(FenixResourceMetadataVo project) {
        Project domainObject = new Project();
        domainObject.setAbstractAbstract(project.getAbstractAbstract());
        domainObject.setContact(project.getContact());
        domainObject.setCategories(project.getCategories());
        domainObject.setDateLastUpdate(new Date());
        domainObject.setEndDate(mapper.string2Date(project.getEndDate()));
        domainObject.setKeywords(project.getKeywords());
        domainObject.setPeriodTypeCode(project.getPeriodTypeCode());
        domainObject.setProvider(project.getProvider());
        domainObject.setRegion(project.getRegion());
        domainObject.setSharingCode(project.getSharingCode());
        domainObject.setSource(project.getSource());
        domainObject.setStartDate(mapper.string2Date(project.getStartDate()));
        domainObject.setTitle(project.getTitle());
        projectDao.save(domainObject);
        Long projectId = domainObject.getResourceId();
        System.out.println("projectId = " + projectId);
        return projectId;
    }

    public Long saveProject(String title) {
        Project project = new Project();
        project.setTitle(title);
        projectDao.save(project);
        return project.getResourceId();
    }

    public Map getNewProjectMetadata(Map metadata) {
        final Long id = saveProject((String) metadata.get("title"));
        metadata.put("id", id);
        return metadata;
    }

    public FenixResource createProjectFenixResource(Long id) {
        Project project = projectDao.findById(id);
        FenixResource fenixResource = null;
        if (project != null) {
            fenixResource = FenixResourceBuilder.build(projectDao.findById(id));
        }
        return fenixResource;
    }

    public void deleteResourceFrom(Long projectId, Long resourceId) {
        Project project = projectDao.findById(projectId);
        ProjectObject projectObject = projectDao.findProjectObjectById(resourceId);
        project.removeProjectObject(projectObject);
        projectDao.update(project);
    }

    public void deleteProject(Long projectId) {
        Project project = projectDao.findById(projectId);
        projectDao.delete(project);
    }

    public List<FenixResourceMetadataVo> getResourceListFrom(Long projectId) {
        Project project = projectDao.findById(projectId);
        List<ProjectObject> projectList = project.getProjectObjectList();
        List<FenixResourceMetadataVo> projectListVo = new ArrayList<FenixResourceMetadataVo>();
        for (ProjectObject projectObject : projectList) {
            projectListVo.add(mapper.do2vo(projectObject));
        }
        return projectListVo;
    }
}
