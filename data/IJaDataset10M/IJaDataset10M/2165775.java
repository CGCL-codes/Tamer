package com.antmanager.fetch;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import com.antmanager.execute.Execute;
import com.antmanager.fetch.update.CheckoutValidation;
import com.antmanager.fetch.update.UpToDate;
import com.antmanager.jobs.Job;
import com.antmanager.project.ProjectModule;

/**
 * Searches a project on an external deposit and stores it on a given place on
 * the local hard disk.
 * 
 * <ul>
 * Standart supportet deposits are:
 * <li>CVS
 * <li>FILESYSTEM
 * </ul>
 * To add new protocolls, add a new class to the <code>com.antmanager.fetch.tasks</code> package,
 * named with the name of the protocoll and implementing the <code>com.antmanager.fetch.IFetchTask</code> interface.
 * 
 * @see com.antmanager.jobs.Job
 * @see com.antmanager.fetch.IFetchTask
 * 
 * @author Marmsoler Diego
 * 
 */
public class Fetch extends Job {

    /**
	 * Constants used for logging
	 */
    public static final int LOGGER = Project.MSG_INFO;

    public static final int DEBUG = Project.MSG_DEBUG;

    public static final String OVERWRITE = "overwrite";

    public static final String UPDATE = "update";

    /**
	 * Kind of server on which the project will be searched.
	 * <p>
	 * IFetchTask is an interface which must be implemnented by the
	 * different server classes(cvs, ftp, get, file).
	 * <p>
	 * Specific konfigurations will be read from a konfiguration file.
	 */
    private IFetchTask task;

    /**
	 * the module for which fecth is requested.
	 */
    private ProjectModule module;

    /**
	 * Default constructor.
	 * 
	 * @param module
	 *            project to checkout
	 */
    public Fetch(ProjectModule module) {
        super("checkout-" + module.getProjectName() + module.getHomeDirectory(), module.getProject());
        this.setProject(module.getProject());
        this.module = module;
        this.setTaskName(this.toString());
        this.task = Fetch.getTask(this.module);
    }

    public static final IFetchTask getTask(ProjectModule pm) {
        IFetchTask returnTask = null;
        try {
            String extentionForTest = new String();
            if (pm.getProject().getProperty("isTestModeActive") != null && (pm.getProject().getProperty("isTestModeActive").equals("true"))) {
                extentionForTest = "test.";
            }
            returnTask = (IFetchTask) Class.forName("com.antmanager.fetch.tasks." + extentionForTest + pm.getSourceLocation()).newInstance();
        } catch (InstantiationException e) {
            throw new BuildException("Exception while searching for corresponding target class, mess was: " + e);
        } catch (IllegalAccessException e) {
            throw new BuildException("Exception while searching for corresponding target class, mess was: " + e);
        } catch (ClassNotFoundException e) {
            throw new BuildException("This type of deposit is not supportet by AntManager: " + pm.getSourceLocation());
        }
        return returnTask;
    }

    /**
	 * checks a project out if this is not already done or <code>update</code>
	 * is required.
	 */
    public void execute() {
        log("execute fetch of project: " + this.module.getProjectName(), Execute.LOGGER);
        log("-------------------------------------------------------------------------------", Execute.LOGGER);
        log("-------------------------------------------------------------------------------", Execute.LOGGER);
        new File(this.module.getHomeDirectory()).mkdirs();
        try {
            UpToDate update = new UpToDate(this.module);
            update.init();
            update.execute();
            if (this.module.getFetchMode().equals(Fetch.OVERWRITE)) {
                task.init(this.module);
                if (update.isAlreadyCheckedOut()) {
                    log("overwrite component " + this.module.getProjectName(), Project.MSG_INFO);
                } else {
                    log("checkout componente " + this.module.getProjectName(), Project.MSG_INFO);
                }
                log("***********************************************************************", Execute.LOGGER);
                try {
                    task.execute(this.module);
                } catch (RuntimeException e) {
                    throw new BuildException("Error while trying to checkout " + this.module.getProjectName(), e);
                }
            } else if (this.module.getFetchMode().equals(Fetch.UPDATE)) {
                task.init(this.module);
                try {
                    if (update.isAlreadyCheckedOut()) {
                        log("update component " + this.module.getProjectName(), Project.MSG_INFO);
                        log("***********************************************************************", Execute.LOGGER);
                        task.execute(this.module);
                    } else {
                        log("checkout componente " + this.module.getProjectName(), Project.MSG_INFO);
                        log("***********************************************************************", Execute.LOGGER);
                        task.update(this.module);
                    }
                } catch (RuntimeException e) {
                    throw new BuildException("Error while trying to update " + this.module.getProjectName(), e);
                }
            } else {
                throw new BuildException("Project already checked out and no update required => in this case it is undesirable to start a fetch thread");
            }
            CheckoutValidation cv = new CheckoutValidation(this.module);
            cv.init();
            cv.execute();
        } catch (RuntimeException e) {
            throw new BuildException("Error during fetch of " + this.module.getProjectName(), e);
        }
    }

    /**
	 * returns a String to identificate this object
	 */
    public String toString() {
        return "FETCH FOR " + this.module;
    }
}
