package org.nox.web.action;

import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nox.domain.Project;
import org.nox.domain.dao.ProjectDAO;
import org.nox.domain.dto.DTOConverter;
import org.nox.domain.dto.ProjectDTO;
import org.nox.util.json.JSONContext;
import org.nox.util.json.JSONModel;
import org.nox.util.json.JSONSerializer;

@SuppressWarnings("serial")
public class ProjectAction extends BaseAction {

    private static Log logger = LogFactory.getLog(ProjectAction.class);

    private ProjectDAO projectDAO;

    public String index() {
        return SUCCESS;
    }

    public String listProjects() {
        final List<Project> projects = projectDAO.findAll();
        setJSONModel(new JSONModel() {

            public String toJSON(JSONContext context) {
                if (logger.isWarnEnabled()) logger.warn("listProjects");
                JSONSerializer serializer = context.createSerializer();
                serializer.aliasClass("projects", ProjectDTO[].class);
                ProjectDTO[] dto = new DTOConverter().convert(projects);
                return serializer.toJSON(dto);
            }
        });
        return RESULT_JSON;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public String createProject() {
        if (logger.isWarnEnabled()) logger.warn(ToStringBuilder.reflectionToString(project));
        projectDAO.saveOrUpdate(project);
        setJSONModel(new JSONModel() {

            public String toJSON(JSONContext context) {
                if (logger.isWarnEnabled()) logger.warn(ToStringBuilder.reflectionToString(project));
                JSONSerializer serializer = context.createSerializer();
                serializer.aliasClass("project", ProjectDTO.class);
                ProjectDTO dto = new DTOConverter().convert(project);
                return serializer.toJSON(dto);
            }
        });
        return RESULT_JSON;
    }

    private Project project;

    public Project getProject() {
        if (project == null) {
            project = new Project();
            project.setName("Grooarrr");
            project.setDescription("hi hi !!");
        }
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
