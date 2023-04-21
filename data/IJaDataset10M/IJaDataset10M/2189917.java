package ipman.app.base.businessserviceimpl;

import java.util.List;
import java.util.ArrayList;
import ipman.app.base.dto.*;
import ipman.app.base.service.*;
import ipman.app.base.businessservice.*;

public class ProjectBusinessServiceImpl implements ProjectBusinessService {

    private ProjectService service;

    public void setService(ProjectService service) {
        this.service = service;
    }

    /**
    *
    */
    public ProjectDto loadProjectById(long id) {
        return service.loadProjectById(id);
    }

    /**
    *
    */
    public List<ProjectDto> loadAllProjects() {
        return service.loadAllProjects();
    }

    /**
    *
    */
    public ProjectDto createProject(ProjectDto dto) {
        return service.createProject(dto);
    }

    /**
    *
    */
    public ProjectDto updateProject(ProjectDto dto) {
        return service.updateProject(dto);
    }

    /**
    *
    */
    public boolean deleteProject(ProjectDto dto) {
        return service.deleteProject(dto);
    }

    /**
    *
    */
    public boolean deleteProjectById(long id) {
        return service.deleteProjectById(id);
    }
}
