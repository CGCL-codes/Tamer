package scpn.admin.facades;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import scpn.model.administration.ProjectsPrivilegesRoles;

/**
 *
 * @author jrmacias <jrmacias@cnb.csic.es>
 */
@Stateless
public class ProjectsPrivilegesRolesFacade extends AbstractFacade<ProjectsPrivilegesRoles> {

    @PersistenceContext(unitName = "ScipionDBModelPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public ProjectsPrivilegesRolesFacade() {
        super(ProjectsPrivilegesRoles.class);
    }
}
