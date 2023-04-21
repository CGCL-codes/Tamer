package be.oniryx.lean.session;

import be.oniryx.lean.entity.*;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.*;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import java.util.*;

/**
 * User: cedric
 * Date: May 3, 2009
 */
@Stateful
@Name("permission")
@Scope(ScopeType.CONVERSATION)
public class MyPermissionResolverImpl implements MyPermissionResolver {

    @In(create = true)
    private Project project;

    @In(create = true)
    private Person person;

    @In
    private EntityManager em;

    private Map<String, Set<Object>> grants;

    private Map<String, Set<Object>> getGrants() {
        if (grants == null) {
            List<RoleGrant> roleGrants = em.createNamedQuery("RoleGrant.findByPersonAndProjectOrderedByActionName").setParameter("projectId", project.getId()).setParameter("personId", person.getId()).getResultList();
            grants = new HashMap<String, Set<Object>>();
            String action = null;
            Set<Object> resources = null;
            for (RoleGrant roleGrant : roleGrants) {
                if (action == null) {
                    action = roleGrant.getAction().getName();
                    resources = new HashSet();
                } else if (!action.equals(roleGrant.getAction().getName())) {
                    grants.put(action, resources);
                    action = roleGrant.getAction().getName();
                    resources = new HashSet();
                }
                String resourceName = roleGrant.getResource().getName();
                Class resourceClass = getResourceClass(resourceName);
                if (resourceClass == null) throw new IllegalArgumentException("The resource '" + resourceName + "' is not managed");
                resources.add(resourceClass);
            }
            if (action != null) {
                grants.put(action, resources);
            }
        }
        return grants;
    }

    private Class getResourceClass(String resourceName) {
        Class resourceClass;
        if (Resource.CLIENT_REQUEST.equals(resourceName)) resourceClass = ClientRequest.class; else if (Resource.ITEM.equals(resourceName)) resourceClass = Item.class; else if (Resource.PROJECT_MEMBER.equals(resourceName)) resourceClass = PersonInProject.class; else if (Resource.SECURITY_SETTING.equals(resourceName)) resourceClass = GroupRole.class; else if (Resource.VERSION.equals(resourceName)) resourceClass = Version.class; else if (Resource.ACTIVITYREPORT.equals(resourceName)) resourceClass = ActivityReport.class; else if (Resource.METHODOLOGY.equals(resourceName)) resourceClass = Methodology.class; else if (Resource.REPORT.equals(resourceName)) resourceClass = Report.class; else if (Resource.BUDGET.equals(resourceName)) resourceClass = PersonCost.class; else resourceClass = null;
        return resourceClass;
    }

    private Class getResourceClass(Object resource) {
        Class resourceClass;
        if (resource instanceof Class) resourceClass = (Class) resource; else if (resource instanceof String) {
            String r = (String) resource;
            resourceClass = getResourceClass(r);
        } else {
            if (resource != null) resourceClass = resource.getClass(); else resourceClass = null;
        }
        return resourceClass;
    }

    private String getStandardAction(String action) {
        if ("insert".equals(action)) return "create";
        return action;
    }

    public boolean hasPermission(Object resource, String action) {
        String standardAction = getStandardAction(action);
        Set<Object> resources = getGrants().get(standardAction);
        Class resourceClass = getResourceClass(resource);
        return (resources != null) && resources.contains(resourceClass);
    }

    public void filterSetByAction(Set<Object> resourcesToFilter, String action) {
        Set<Object> resources = getGrants().get(action);
        for (Iterator it = resourcesToFilter.iterator(); it.hasNext(); ) {
            Object resource = it.next();
            if (!resources.contains(resource)) it.remove();
        }
    }

    public List<Project> findProjectsWithGrant(String actionName, String resourceName) {
        return em.createNamedQuery("Project.findProjectsWithGrant").setParameter("actionName", actionName).setParameter("resourceName", resourceName).setParameter("personId", person.getId()).getResultList();
    }

    @Remove
    @Destroy
    public void remove() {
    }
}
