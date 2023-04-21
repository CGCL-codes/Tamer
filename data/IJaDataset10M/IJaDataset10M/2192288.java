package net.sf.incanto;

import java.io.File;
import java.io.IOException;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteStreamHandler;
import org.apache.tools.ant.taskdefs.LogOutputStream;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;
import org.apache.tools.ant.types.Commandline;

/**
 * @author <a href="mailto:alex.karnstedt@gmx.de">Alexander Karnstedt</a>
 * @version $Id: AbstractIncantoTask.java,v 1.4 2007/10/06 16:49:55 alexrk Exp $
 */
public abstract class AbstractIncantoTask extends Task {

    protected Commandline cmdl = new Commandline();

    private String resultProperty;

    private File directory;

    protected ExecuteStreamHandler executeStreamHandler;

    protected boolean failOnError = false;

    public AbstractIncantoTask() {
        super();
    }

    /**
     * helper method to set result property to the
     * passed in value if appropriate
     *
     * @param result value desired for the result property value
     */
    protected void maybeSetResultPropertyValue(int result) {
        if (resultProperty != null) {
            String res = Integer.toString(result);
            getProject().setProperty(resultProperty, res);
        }
    }

    /**
     * Fail if the command exits with a non-zero return code.
     *
     * @param fail if true fail the command on non-zero return code.
     */
    public void setFailonerror(boolean fail) {
        this.failOnError = fail;
    }

    /**
     * Sets the name of a property in which the return code of the
     * command should be stored. Only of interest if failonerror=false.
     *
     * @param resultProperty name of property
     */
    public void setResultProperty(String resultProperty) {
        this.resultProperty = resultProperty;
    }

    /**
     * The directory to run the command in, defaults to the
     * project's base directory.
     * 
     * @param directory the directory to run the command in
     */
    public void setDir(File directory) {
        this.directory = directory;
    }

    /**   
     * execute 
     * @throws BuildException when it all goes a bit pear shaped
     */
    public void execute() throws BuildException {
        int returnCode = -1;
        Commandline toExecute = setupCommand();
        ExecuteStreamHandler streamHandler = setupStreamHandler();
        Execute exe = new Execute(streamHandler);
        File dir = getProject().getBaseDir();
        if (directory != null) {
            dir = directory;
        }
        exe.setWorkingDirectory(dir);
        exe.setCommandline(toExecute.getCommandline());
        log(toExecute.describeCommand(), Project.MSG_VERBOSE);
        try {
            returnCode = exe.execute();
            maybeSetResultPropertyValue(returnCode);
            if (failOnError && (returnCode != 0)) throw new BuildException("Incanto task returned error code: " + returnCode, getLocation());
        } catch (IOException e) {
            throw new BuildException(e, getLocation());
        } finally {
            postExecute();
        }
    }

    /**
     * Deriving classes should assemble the commandline within this
     * method
     */
    protected abstract Commandline setupCommand();

    /**
     * Hook for derived classes to do work after executing the
     * command line util
     */
    protected void postExecute() {
    }

    protected ExecuteStreamHandler setupStreamHandler() {
        return new PumpStreamHandler(new LogOutputStream(this, Project.MSG_INFO), new LogOutputStream(this, Project.MSG_WARN));
    }
}
