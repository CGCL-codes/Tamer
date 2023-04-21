package net.sourceforge.solexatools.dao.hibernate;

import java.util.ArrayList;
import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sourceforge.solexatools.Debug;
import net.sourceforge.solexatools.dao.ProjectDAO;
import net.sourceforge.solexatools.model.Project;
import net.sourceforge.solexatools.model.Registration;

public class ProjectDAOHibernate extends HibernateDaoSupport implements ProjectDAO {

    public ProjectDAOHibernate() {
        super();
    }

    public void insert(Project project) {
        this.getHibernateTemplate().save(project);
    }

    public void update(Project project) {
        this.getHibernateTemplate().update(project);
    }

    public List<Project> list(Registration registration) {
        ArrayList<Project> projects = new ArrayList<Project>();
        if (registration == null) return projects;
        List expmts = null;
        for (Object project : expmts) {
            projects.add((Project) project);
        }
        return projects;
    }

    /**
	 * Finds an instance of Project in the database by the Project
	 * name.
	 *
	 * @param name	name of the Project
	 * @return Project or null if not found
	 */
    public Project findByName(String name) {
        String query = "from project as project where project.name = ?";
        Project project = null;
        Object[] parameters = { name };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            project = (Project) list.get(0);
        }
        return project;
    }

    /**
	 * Finds an instance of Project in the database by the Project
	 * ID.
	 *
	 * @param expID	ID of the Project
	 * @return Project or null if not found
	 */
    public Project findByID(Integer expID) {
        String query = "from Project as project where project.projectId = ?";
        Project project = null;
        Object[] parameters = { expID };
        List list = this.getHibernateTemplate().find(query, parameters);
        if (list.size() > 0) {
            project = (Project) list.get(0);
        }
        return project;
    }
}
