package net.sourceforge.solexatools.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.solexatools.Debug;
import net.sourceforge.solexatools.business.ProjectService;
import net.sourceforge.solexatools.dao.ProjectDAO;
import net.sourceforge.solexatools.model.Project;
import net.sourceforge.solexatools.model.Sample;
import net.sourceforge.solexatools.model.Registration;

public class ProjectServiceImpl implements ProjectService {

    private ProjectDAO projectDAO = null;

    private static final Log log = LogFactory.getLog(ProjectServiceImpl.class);

    public ProjectServiceImpl() {
        super();
    }

    /**
	 * Sets a private member variable with an instance of
	 * an implementation of ProjectDAO. This method
	 * is called by the Spring framework at run time.
	 *
	 * @param		projectDAO implementation of ProjectDAO
	 * @see			ProjectDAO
	 */
    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    /**
	 * Inserts an instance of Project into the database.
	 *
	 * @param projectDAO instance of ProjectDAO
	 */
    public void insert(Project project) {
        TreeSet<Sample> list = new TreeSet<Sample>();
        project.setSamples(list);
        project.setCreateTimestamp(new Date());
        projectDAO.insert(project);
    }

    /**
	 * Updates an instance of Project in the database.
	 *
	 * @param project instance of Project
	 */
    public void update(Project project) {
        projectDAO.update(project);
    }

    public List<Project> list(Registration registration) {
        return projectDAO.list(registration);
    }

    /**
	 * Finds an instance of Project in the database by the Project
	 * emailAddress, and copies the Project properties to an instance of
	 * Project.
	 *
	 * @return instance of Project, or null if a Project cannot be
	 *		   found
	 */
    public Project findByName(String name) {
        Project project = null;
        if (name != null) {
            try {
                project = projectDAO.findByName(name.trim().toLowerCase());
            } catch (Exception exception) {
                log.debug("Cannot find Project by name " + name);
            }
        }
        return project;
    }

    public Project findByID(Integer expID) {
        Project project = null;
        if (expID != null) {
            try {
                project = projectDAO.findByID(expID);
                fillInSamples(project);
            } catch (Exception exception) {
                log.error("Cannot find Project by expID " + expID);
                log.error(exception.getMessage());
            }
        }
        return project;
    }

    /**
	 * Determines if an email address has already been used.
	 *
	 * @param oldEmail	The previous email address, or null if this
	 *		  method is being called for a new email address
	 *
	 * @param newEmail	The email address that is being checked
	 *
	 * @return		true if the newEmail has already been used, and
	 *		  false otherwise
	 */
    public boolean hasNameBeenUsed(String oldName, String newName) {
        boolean nameUsed = false;
        boolean checkName = true;
        if (newName != null) {
            if (oldName != null) {
                checkName = !newName.trim().equalsIgnoreCase(oldName.trim());
            }
            if (checkName) {
                Project project = this.findByName(newName.trim().toLowerCase());
                if (project != null) {
                    nameUsed = true;
                }
            }
        }
        return nameUsed;
    }

    private void fillInSamples(Project project) {
        Object[] samples = project.getSamples().toArray();
        Debug.put(": Array Length is " + project.getSamples().size());
        if (samples.length >= 8) {
        }
    }
}
