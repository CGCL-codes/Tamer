package pl.edu.pw.polygen.core.project;

import java.util.List;
import org.springframework.stereotype.Service;
import pl.edu.pw.polygen.beans.ProjectBean;
import pl.edu.pw.polygen.exception.ProjectExistException;

public interface ProjectService {

    List<ProjectBean> getAllUserProjects(String userName);

    ProjectBean createProject(String name, String description, String userName) throws ProjectExistException;

    ProjectBean updateProject(ProjectBean projectBean);

    void removeProject(String projectName, Long userId);

    ProjectBean findProject(String projectName, Long userId);

    ProjectBean findProject(Long projectId);
}
