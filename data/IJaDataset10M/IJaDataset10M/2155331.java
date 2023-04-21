package net.sourceforge.clearantlib;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.types.Commandline.Argument;

/**
 * Task/Condition to apply (and promote) a ClearCase baseline.
 * @author Kevin A. Lee
 */
public class CCApplyBl extends ClearToolExec implements Condition {

    private boolean full = false;

    private boolean checkexists = false;

    private String createdproperty;

    private String baselineselector;

    private String pvob;

    private String component;

    private String plevel;

    private boolean isTask = false;

    /**
     * Set the createdproperty flag
     * @param createdproperty the status to set the flag to
     */
    public void setCreatedproperty(String createdproperty) {
        this.createdproperty = createdproperty;
    }

    /**
     * Get createdproperty flag status
     * @return String containing status of createdproperty selector flag
     */
    public String getCreatedproperty() {
        return this.createdproperty;
    }

    /**
     * Set the baselineselector flag
     * @param baselineSelector the status to set the flag to
     */
    public void setBaselineselector(String baselineSelector) {
        this.baselineselector = baselineSelector;
    }

    /**
     * Get baselineselector flag status
     * @return String containing status of baselineselector flag
     */
    public String getBaselineselector() {
        return this.baselineselector;
    }

    /**
     * Set the pvob flag
     * @param pvob the status to set the flag to
     */
    public void setPvob(String pvob) {
        this.pvob = pvob;
    }

    /**
     * Get pvob flag status
     * @return String containing status of pvob flag
     */
    public String getPvob() {
        return this.pvob;
    }

    /**
     * Set the checkexists flag
     * @param checkexists the status to set the flag to
     */
    public void setCheckexists(boolean check) {
        this.checkexists = check;
    }

    /**
     * Get checkexists flag status
     * @return boolean containing status of checkexists flag
     */
    public boolean getCheckexists() {
        return this.checkexists;
    }

    /**
     * Set the full flag
     * @param full the status to set the flag to
     */
    public void setFull(boolean full) {
        this.full = full;
    }

    /**
     * Get full flag status
     * @return boolean containing status of full flag
     */
    public boolean getFull() {
        return this.full;
    }

    /**
     * Set the component to generate the baseline for
     * @param comp the name of the component
     */
    public void setComponent(String comp) {
        this.component = comp;
    }

    /**
     * Get the component flag status
     * @return the component the baseline is to be applied to
     */
    public String getComponent() {
        return this.component;
    }

    /**
     * Set the promotion level of the baseline
     * @param plevel promotion level to set baseline to
     */
    public void setPlevel(String plevel) {
        this.plevel = plevel;
    }

    /**
     * Get the promotion level of the baseline
     * @return the promotion level of the baseline
     */
    public String getPlevel() {
        return this.plevel;
    }

    private void checkOptions() throws BuildException {
        if (getBaselineselector() == null) {
            throw new BuildException("Required attribute baselineselector not specified");
        }
        if (getPvob() == null) {
            throw new BuildException("Required attribute pvob not specified");
        }
    }

    private String mkBlCommand() {
        return "mkbl " + (getFull() ? "-full " : "-incremental ") + (this.component != null ? "-component " + getComponent() + " " : " ") + getBaselineselector();
    }

    /**
     * Execute and evaluate the application of a baseline
     * @throws BuildException if the command fails and failonerr is set to true
     */
    public boolean eval() throws BuildException {
        Project aProj = getProject();
        String execProp = "value.ccapplybl.output";
        int propCount = 1;
        boolean wasBlApplied = true;
        boolean createBl = true;
        checkOptions();
        Argument arg = this.createArg();
        arg.setLine("pwv");
        this.setOutputproperty(execProp + propCount);
        super.execute();
        String propOutput = aProj.getProperty(execProp + propCount++);
        log("View context is: " + propOutput, Project.MSG_DEBUG);
        try {
            String viewDir = propOutput.substring(0, propOutput.indexOf("\n"));
            if (viewDir.indexOf("NONE") > 0) {
                throw new BuildException("This task requires a ClearCase view context");
            }
        } catch (IndexOutOfBoundsException e) {
            log("Caught exception: " + e.getMessage(), Project.MSG_ERR);
        }
        if (getCheckexists()) {
            this.reconfigure();
            this.setFailonerror(false);
            this.setOutputproperty(execProp + propCount);
            arg.setLine("describe -s baseline:" + getBaselineselector() + "@" + getPvob());
            super.execute();
            propOutput = aProj.getProperty(execProp + propCount++);
            if (propOutput.equals(getBaselineselector())) {
                log("The baseline " + getBaselineselector() + " already exists\n", Project.MSG_INFO);
                wasBlApplied = false;
                createBl = false;
            }
        }
        if (createBl) {
            this.reconfigure();
            this.setFailonerror(true);
            this.setOutputproperty(execProp + propCount);
            log("mkbl command is: " + mkBlCommand() + "\n", Project.MSG_DEBUG);
            arg.setLine(mkBlCommand());
            super.execute();
            propOutput = aProj.getProperty(execProp + propCount++);
            log(propOutput, Project.MSG_DEBUG);
            if (propOutput.indexOf("Error") > 0) {
                throw new BuildException("The mkbl command returned an error:\n " + propOutput);
            }
            if (this.component != null && (propOutput.indexOf("no baseline created") > 0)) {
                log("No changes made; new baseline not created\n", Project.MSG_INFO);
                wasBlApplied = false;
            } else {
                if (this.component == null) {
                    log("Created baseline " + getBaselineselector(), Project.MSG_INFO);
                } else {
                    log("Created baseline " + getBaselineselector() + " in " + getComponent(), Project.MSG_INFO);
                }
                if (this.component != null && this.plevel != null) {
                    this.reconfigure();
                    this.setFailonerror(false);
                    this.setOutputproperty(execProp + propCount);
                    log("chbl command is: chbl -level " + getPlevel() + " " + getBaselineselector() + "@" + getPvob() + "\n", Project.MSG_DEBUG);
                    arg.setLine("chbl -level " + getPlevel() + " " + getBaselineselector() + "@" + getPvob());
                    super.execute();
                    propOutput = aProj.getProperty(execProp + propCount++);
                    log(propOutput, Project.MSG_DEBUG);
                    if (propOutput.indexOf("Error") > 0) {
                        throw new BuildException("The chbl command returned an error:\n " + propOutput);
                    }
                }
                if (this.createdproperty != null) {
                    getProject().setNewProperty(this.createdproperty, "true");
                }
            }
        }
        return wasBlApplied;
    }

    /**
     * Execute the application of the baseline
     * @throws BuildException 
     */
    public void execute() throws BuildException {
        isTask = true;
        try {
            eval();
        } finally {
            isTask = false;
        }
    }
}
