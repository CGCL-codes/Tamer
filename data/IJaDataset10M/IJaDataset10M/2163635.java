package edu.csula.coolstatela.dao.hibernateImpl;

import java.util.List;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import edu.csula.coolstatela.dao.ProjectDao;
import edu.csula.coolstatela.model.Project;

public class ProjectDaoImpl extends HibernateDaoSupport implements ProjectDao {

    public Long saveUpdateProject(Project project) {
        getHibernateTemplate().saveOrUpdate(project);
        return project.getProjectID();
    }

    public void deleteProject(Project project) {
        getHibernateTemplate().delete(project);
    }

    public List<Project> findAllProject() {
        return getHibernateTemplate().find("from Project");
    }
}
