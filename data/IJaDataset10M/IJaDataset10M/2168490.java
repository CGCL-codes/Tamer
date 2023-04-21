package net.sourceforge.thegreymenstool.dao.impl;

import java.util.List;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.sourceforge.thegreymenstool.dao.ProjectDao;
import net.sourceforge.thegreymenstool.domain.Project;
import net.sourceforge.thegreymenstool.domain.ProjectActivity;
import net.sourceforge.thegreymenstool.utils.BidiController;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProjectDaoImpl extends BaseDaoImpl<Project> implements ProjectDao {

    @PersistenceContext
    private EntityManager em;

    @Resource(name = "projectProjectActivityController")
    private BidiController projectProjectActivityController;

    @Override
    @Transactional
    public Project create(String name) {
        Project project = new Project();
        project.setName(name);
        em.persist(project);
        return project;
    }

    @Override
    public Project findByName(String name) {
        Query q = em.createQuery("select p from Project p where p.name = :name");
        q.setParameter("name", name);
        return (Project) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Project> findAll() {
        Query q = em.createQuery("select p from Project p");
        return q.getResultList();
    }

    @Override
    public void addProjectActivity(Project p, ProjectActivity projectActivity) {
        projectProjectActivityController.addManyToOne(projectActivity, p);
    }
}
