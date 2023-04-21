package net.sf.xsnapshot.example.dao;

import java.util.List;
import net.sf.xsnapshot.example.data.model.Project;

/**
 * DAO interface for Project objects.
 * @author Daniel Kokotov
 */
public interface ProjectDao {

    List retrieveAllProjects();

    Project retrieveProjectById(Long id);

    Long saveProject(Project project);

    void updateProject(Project project);
}
