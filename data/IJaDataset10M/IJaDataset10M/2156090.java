package edu.ipfw.nitro.persistence;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import edu.ipfw.nitro.dto.ProjectDTO;
import edu.ipfw.nitro.dto.ProjectnoteDTO;

/**
 * Home object for domain model class Project.
 * @see edu.ipfw.nitro.persistence.Project
 * @author Hibernate Tools
 */
@WebService
@Stateless
public class ProjectHome {

    private static final Log log = LogFactory.getLog(ProjectHome.class);

    @PersistenceContext
    private EntityManager entityManager;

    @WebMethod
    public boolean addNoteToProject(ProjectnoteDTO pndto, int projectId) {
        log.info("ProjectHome:addNoteToProject()");
        Project p = this.findById(projectId);
        if (p != null && p instanceof Project) {
            Projectnote pn = new Projectnote();
            try {
                BeanUtils.copyProperties(pn, pndto);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            pn.setId(null);
            pn.setProject(p);
            p.getProjectnotes().add(pn);
            entityManager.merge(p);
            log.info("Persist successful");
            return true;
        }
        return false;
    }

    @WebMethod
    public Collection<ProjectnoteDTO> findProjectnotesByProjectId(int projectId) {
        Collection<ProjectnoteDTO> results = new ArrayList<ProjectnoteDTO>();
        log.info("ProjectHome: findProjectnotesByProjectId(" + projectId + ")");
        Project p = this.findById(projectId);
        if (p != null && p instanceof Project) {
            log.info("findProjectnotesByProjectId: Project found!");
            if (p.getProjectnotes() != null && p.getProjectnotes().size() > 0) {
                log.info("findProjectnotesByProjectId: " + p.getProjectnotes().size() + " project notes found for project");
                for (Projectnote pn : p.getProjectnotes()) {
                    ProjectnoteDTO pndto = new ProjectnoteDTO();
                    try {
                        BeanUtils.copyProperties(pndto, pn);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    results.add(pndto);
                }
            }
        }
        return results;
    }

    public void persist(Project transientInstance) {
        log.debug("persisting Project instance");
        try {
            entityManager.persist(transientInstance);
            log.debug("persist successful");
        } catch (RuntimeException re) {
            log.error("persist failed", re);
            throw re;
        }
    }

    public void remove(Project persistentInstance) {
        log.debug("removing Project instance");
        try {
            entityManager.remove(persistentInstance);
            log.debug("remove successful");
        } catch (RuntimeException re) {
            log.error("remove failed", re);
            throw re;
        }
    }

    public Project merge(Project detachedInstance) {
        log.debug("merging Project instance");
        try {
            Project result = entityManager.merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public Project findById(Integer id) {
        log.debug("getting Project instance with id: " + id);
        try {
            Project instance = entityManager.find(Project.class, id);
            log.debug("get successful");
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
}
