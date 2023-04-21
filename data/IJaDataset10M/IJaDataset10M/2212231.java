package net.sourceforge.thegreymenstool.dao.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import net.sourceforge.thegreymenstool.dao.ProjectActivityDao;
import net.sourceforge.thegreymenstool.domain.ProjectActivity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ProjectActivityDaoImpl extends BaseDaoImpl<ProjectActivity> implements ProjectActivityDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public ProjectActivity create(String name) {
        ProjectActivity project = new ProjectActivity(name);
        project.setName(name);
        em.persist(project);
        return project;
    }

    @Override
    public ProjectActivity findByName(String name) {
        Query q = em.createQuery("select p from ProjectActivity p where p.name = :name");
        q.setParameter("name", name);
        return (ProjectActivity) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ProjectActivity> findAll() {
        Query q = em.createQuery("select p from ProjectActivity p");
        return q.getResultList();
    }
}
