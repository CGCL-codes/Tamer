package com.antmanager.fetch.tasks;

import java.io.File;
import com.antmanager.fetch.IFetchTask;
import com.antmanager.project.ProjectModule;
import java.net.MalformedURLException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.taskdefs.Delete;
import org.tigris.subversion.svnant.Checkout;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * An adapted Subversion Task to connect to a svn and check out files or directories.
 *
 * <ul>The class takes the Module from the SVN witch is named:
 * <li>either by a ANT-property <code>projectname.cvs.modulename</code>
 * <li>or if such a property is not set takes the name of the project
 * </ul>
 *
 * The class implements the <code>IFetchTask</code> interface which contains
 * an abstract methods:
 * <ul>
 * <li>execute to check out files
 * <li>init to initialize
 * </ul>
 * <p>
 *
 * @see com.antmanager.fetch.IFetchTask
 *
 * @author Marmsoler Diego
 */
public class svn extends org.tigris.subversion.svnant.SvnTask implements IFetchTask {

    private String module;

    private String protocoll;

    private String tag;

    private String repository;

    private String host;

    /**
     * Default Konstructor.
     *
     * Sets several Parameters.
     *
     * @param checkOutPath 			the path where to store the checked out files or directories
     */
    public svn() {
        super();
    }

    /**
     * initialises the svn checkout by setting some properties.
     *
     * must be called for makeing a checkout, otherwise the object will not
     * be work correct.
     */
    public void init(ProjectModule pm) {
        super.setProject(pm.getProject());
        this.setTaskName(this.toString() + " " + pm.getProjectName());
        this.repository = pm.getProjectProperty("svn.repository", true);
        this.protocoll = pm.getProjectProperty("svn.protocoll", true);
        this.host = pm.getProjectProperty("svn.host", true);
        super.setUsername(pm.getProjectProperty("svn.user", true));
        super.setPassword(pm.getProjectProperty("svn.passwd", true));
        if (!(pm.getProjectProperty("svn.modulename", false) == null)) {
            this.module = pm.getProjectProperty("svn.modulename", true);
        } else {
            this.module = pm.getProjectName();
        }
        if (!(pm.getVersion() == null)) {
            if (!pm.getVersion().toLowerCase().equals("trunk")) {
                this.tag = "tags/" + pm.getVersion();
            }
        }
        if (this.tag == null) {
            this.tag = "trunk";
        }
    }

    /**
     * Makes a new directory if it doesn't yet exists.
     *
     * @param path					path of the directory
     */
    private File makeDirectory(String path) {
        File file = new File(path);
        if (file.exists()) {
            Delete d = new Delete();
            d.setProject(getProject());
            d.init();
            d.setTaskName("Prepare svn checkout");
            d.setDir(file);
            d.execute();
        }
        file.mkdir();
        return file;
    }

    /**
     * Takes a project from a svn repository and checks it out to the given <code>checkOutDestPath</code>.
     *
     * This method is inherited by the interface <code>LocationServer</code> and must be implemented.
     * It will be used by the <code>Fetch</code> class to take a project.
     */
    public void execute(ProjectModule pm) {
        Checkout co = new Checkout();
        String url = this.protocoll + "://" + this.host + "/" + this.repository + "/" + this.module + "/" + this.tag;
        try {
            File dest = makeDirectory(pm.getHomeDirectory());
            co.setUrl(new SVNUrl(url));
            co.setDestpath(dest);
            super.setJavahl(false);
            super.addCheckout(co);
            super.execute();
        } catch (MalformedURLException ex) {
            throw new BuildException("Maleformed url: " + url);
        }
    }

    /**
     * returns a String to identificate this object
     */
    @Override
    public String toString() {
        return "svn checkout";
    }

    public void update(ProjectModule pm) {
        File dest = makeDirectory(pm.getHomeDirectory());
        super.execute();
    }

    public boolean isUpToDate(ProjectModule pm) {
        return false;
    }

    public void tag(ProjectModule pm) {
        super.execute();
    }

    public void unTag(ProjectModule pm) {
        super.execute();
    }

    public void branch(ProjectModule pm) {
        super.execute();
    }
}
