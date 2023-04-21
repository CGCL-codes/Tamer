package com.ubb.damate.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import com.ubb.damate.model.Project;
import com.ubb.damate.model.Workspace;
import com.ubb.damate.service.ProjectServiceLocal;
import com.ubb.damate.service.ProjectServiceRemote;
import com.ubb.damate.service.WorkspaceServiceLocal;

/** Session Bean implementation class ProjectService */
@Stateless
public class ProjectService implements ProjectServiceRemote, ProjectServiceLocal {

    @PersistenceContext(unitName = "damate-pu")
    private EntityManager em;

    @EJB(beanName = "WorkspaceService")
    private WorkspaceServiceLocal workspaceService;

    @Override
    public Project createOrUpdate(Project project) {
        return em.merge(project);
    }

    @Override
    public void delete(Project project) {
        em.remove(em.merge(project));
    }

    @Override
    public Project find(Project project) {
        return em.find(com.ubb.damate.model.Project.class, project);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> find(String username) {
        List<Workspace> workspaces = workspaceService.find(username);
        List<Project> projects = new ArrayList<Project>();
        for (Workspace workspace : workspaces) {
            String queryStr = "SELECT NEW com.ubb.damate.model.Project(p.creationDate) " + "FROM Project AS p WHERE p.id.workspaceId = ?1";
            TypedQuery<Project> query = em.createQuery(queryStr, Project.class);
            query.setParameter(1, workspace.getWorkspaceId());
            projects.addAll(query.getResultList());
        }
        return projects;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Project> allProject() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
        Query query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
