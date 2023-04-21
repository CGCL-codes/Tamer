package org.openprojectservices.opsadmin.dao;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openprojectservices.opsadmin.ImplementationTask;
import org.openprojectservices.opsadmin.ImplementationTaskStatus;
import org.openprojectservices.opsadmin.Project;
import org.openprojectservices.opsadmin.QuoteTask;
import org.openprojectservices.opsadmin.config.LocalConfig;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

public class TaskDaoImpl implements TaskDAO {

    /** location and object type in ldap (constants)*/
    private static final String ORGANISATIONAL_UNIT = "ou";

    private static final String OBJECTCLASS = "objectclass";

    private static Log log = LogFactory.getLog(ProjectDaoImpl.class);

    /** Starting location for ldap queries. In this case: emp-tay :) */
    public static final String BASE_DN = "";

    /** ldap template contains a connection to the ldap server */
    private LdapTemplate ldapTemplate;

    /** localConfig contains all ldap settings */
    private LocalConfig localConfig;

    public List<QuoteTask> listProjectTasks(final String projectName) {
        final AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(OBJECTCLASS, localConfig.getQuoteTaskObjectClass()));
        final String searchLocation = localConfig.getProjectName() + "=" + projectName + ",ou=" + localConfig.getOrgUnitForProjects();
        try {
            return ldapTemplate.search(searchLocation, filter.encode(), new QuoteTaskAttributesMapper(searchLocation));
        } catch (final org.springframework.ldap.NamingException ne) {
            log.error("Exception while retrieving tasks for project " + projectName, ne);
            throw ne;
        }
    }

    public void updateProjectTasks(final Project project) throws DAOException {
        try {
            removeTasksFromProject(project);
        } catch (final org.springframework.ldap.NamingException ne) {
            log.error("Exception while updating tasks for project, could not delete old tasks" + project.getName(), ne);
            throw new DAOException(ne);
        }
        for (final QuoteTask task : project.getQuoteTasks()) {
            try {
                createQuoteTaskForProject(task, project);
            } catch (final org.springframework.ldap.NamingException ne) {
                log.error("Exception while updating tasks for project, could not write new tasks" + project.getName(), ne);
                throw new DAOException(ne);
            }
        }
    }

    private void removeTasksFromProject(final Project project) {
        final List<QuoteTask> deathRow = listProjectTasks(project.getName());
        for (final QuoteTask doomedTask : deathRow) {
            final DistinguishedName dn = buildQuoteTaskDn(doomedTask, project);
            if (doomedTask.getImplementationTasks() != null) {
                for (final ImplementationTask iTask : doomedTask.getImplementationTasks()) {
                    final DistinguishedName iTaskName = new DistinguishedName(dn);
                    iTaskName.add(localConfig.getImplementationTaskName(), iTask.getName());
                    ldapTemplate.unbind(iTaskName);
                }
            }
            ldapTemplate.unbind(dn);
        }
    }

    /**
	 * Stores a QuoteTask for a project in ldap
	 * @param qtask
	 * @param project
	 */
    private void createQuoteTaskForProject(final QuoteTask qtask, final Project project) {
        final DistinguishedName dn = buildQuoteTaskDn(qtask, project);
        final DirContextAdapter context = new DirContextAdapter(dn);
        mapToContext(qtask, context);
        ldapTemplate.bind(dn, context, null);
        if (qtask.getImplementationTasks() != null) {
            for (final ImplementationTask itask : qtask.getImplementationTasks()) {
                final DistinguishedName taskDN = new DistinguishedName(dn);
                taskDN.add(localConfig.getImplementationTaskName(), itask.getName());
                final DirContextAdapter iTaskContext = new DirContextAdapter(dn);
                mapToContext(itask, iTaskContext);
                ldapTemplate.bind(taskDN, iTaskContext, null);
            }
        }
    }

    /** Maps project properties from ldap to QuoteTask bean */
    private class QuoteTaskAttributesMapper implements AttributesMapper {

        /** get where we are at, for retrieving implementation tasks */
        String projectLocation;

        public QuoteTaskAttributesMapper(final String projectLocation) {
            super();
            this.projectLocation = projectLocation;
        }

        public Object mapFromAttributes(final Attributes attrs) throws NamingException, NoSuchElementException {
            final QuoteTask task = new QuoteTask();
            task.setName((String) attrs.get(localConfig.getQuoteTaskName()).get());
            final String estimateKey = localConfig.getQuoteTaskEstimate();
            if (attrs.get(estimateKey) != null && attrs.get(estimateKey).get() != null) {
                task.setEstimate(Long.valueOf((String) attrs.get(estimateKey).get()));
            }
            task.setImplementationTasks(getImplementationTasks(task.getName()));
            return task;
        }

        private Collection<ImplementationTask> getImplementationTasks(final String qTaskName) {
            final AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter(OBJECTCLASS, localConfig.getImplementationTaskObjectClass()));
            final String completeSearchLocation = localConfig.getQuoteTaskName() + "=" + qTaskName + "," + projectLocation;
            return ldapTemplate.search(completeSearchLocation, filter.encode(), new ImplementationTaskAttributesMapper());
        }
    }

    /** Maps project properties from ldap to ImplementationTask bean */
    private class ImplementationTaskAttributesMapper implements AttributesMapper {

        public Object mapFromAttributes(final Attributes attrs) throws NamingException, NoSuchElementException {
            final ImplementationTask task = new ImplementationTask();
            task.setName((String) attrs.get(localConfig.getImplementationTaskName()).get());
            if (attrs.get(localConfig.getImplementationTaskStatus()).get() != null) {
                task.setStatus(checkTaskStatus((String) attrs.get(localConfig.getImplementationTaskStatus()).get()));
            }
            if (attrs.get(localConfig.getImplementationTaskTotalEstimate()) != null && attrs.get(localConfig.getImplementationTaskTotalEstimate()).get() != null) {
                task.setTotalEstimate(Long.valueOf((String) attrs.get(localConfig.getImplementationTaskTotalEstimate()).get()));
            }
            if (attrs.get(localConfig.getImplementationTaskToDoEstimate()) != null && attrs.get(localConfig.getImplementationTaskToDoEstimate()).get() != null) {
                task.setTodoEstimate(Long.valueOf((String) attrs.get(localConfig.getImplementationTaskToDoEstimate()).get()));
            }
            return task;
        }
    }

    /** Checks whether String is equal to element of ProjectStatus */
    private ImplementationTaskStatus checkTaskStatus(final String status) {
        ImplementationTaskStatus taskStatus;
        try {
            taskStatus = Enum.valueOf(ImplementationTaskStatus.class, status);
        } catch (final Exception e) {
            taskStatus = null;
        }
        return taskStatus;
    }

    private DistinguishedName buildQuoteTaskDn(final QuoteTask task, final Project project) {
        final DistinguishedName dn = new DistinguishedName(BASE_DN);
        dn.add(ORGANISATIONAL_UNIT, localConfig.getOrgUnitForProjects());
        dn.add(localConfig.getProjectName(), project.getName());
        dn.add(localConfig.getQuoteTaskName(), task.getName());
        return dn;
    }

    /**
	 * Maps attribute values to task beans.
	 * @param task the QuoteTask to map
	 * @param context
	 */
    private void mapToContext(final QuoteTask task, final DirContextAdapter context) {
        context.setAttributeValues(OBJECTCLASS, new String[] { localConfig.getQuoteTaskObjectClass() });
        context.setAttributeValue(localConfig.getQuoteTaskName(), task.getName());
        final Long estimate = task.getEstimate();
        if (estimate != null) {
            context.setAttributeValue(localConfig.getQuoteTaskEstimate(), String.valueOf(estimate));
        }
    }

    /**
	 * Maps attribute values to task beans.
	 * @param task the QuoteTask to map
	 * @param context
	 */
    private void mapToContext(final ImplementationTask task, final DirContextAdapter context) {
        context.setAttributeValues(OBJECTCLASS, new String[] { localConfig.getImplementationTaskObjectClass() });
        context.setAttributeValue(localConfig.getImplementationTaskName(), task.getName());
        context.setAttributeValue(localConfig.getImplementationTaskStatus(), task.getStatus().toString());
        final Long todoEstimate = task.getTodoEstimate();
        if (todoEstimate != null) {
            context.setAttributeValue(localConfig.getImplementationTaskToDoEstimate(), String.valueOf(todoEstimate));
        }
        final Long totalEstimate = task.getTotalEstimate();
        if (totalEstimate != null) {
            context.setAttributeValue(localConfig.getImplementationTaskTotalEstimate(), String.valueOf(totalEstimate));
        }
    }

    /** setter for spring LdapTemplate, used when the ProjectDaoImpl is initiated in the applicationContext.xml */
    public void setLdapTemplate(final LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void setLocalConfig(final LocalConfig localConfig) {
        this.localConfig = localConfig;
    }

    public void afterPropertiesSet() throws Exception {
        if (localConfig == null) {
            log.error("Need a: " + LocalConfig.class);
            throw new ExceptionInInitializerError("Need a: " + LocalConfig.class);
        }
        if (ldapTemplate == null) {
            log.error("Need a: " + LdapTemplate.class);
            throw new ExceptionInInitializerError("Need a: " + LdapTemplate.class);
        }
    }
}
