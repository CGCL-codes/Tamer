package org.apache.tools.ant.taskdefs.optional.clearcase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.types.Commandline;

/**
 * Performs ClearCase mkelem.
 *
 * <p>
 * The following attributes are interpreted:
 * <table border="1">
 *   <tr>
 *     <th>Attribute</th>
 *     <th>Values</th>
 *     <th>Required</th>
 *   </tr>
 *   <tr>
 *      <td>viewpath</td>
 *      <td>Path to the ClearCase view file or directory that the command will operate on</td>
 *      <td>Yes</td>
 *   <tr>
 *   <tr>
 *      <td>comment</td>
 *      <td>Specify a comment. Only one of comment or cfile may be used.</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>commentfile</td>
 *      <td>Specify a file containing a comment. Only one of comment or cfile may be used.</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>nowarn</td>
 *      <td>Suppress warning messages</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>nocheckout</td>
 *      <td>Do not checkout after element creation</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>checkin</td>
 *      <td>Checkin element after creation</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>preservetime</td>
 *      <td>Preserve the modification time (for checkin)</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>master</td>
 *      <td>Assign mastership of the main branch to the current site</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>eltype</td>
 *      <td>Element type to use during element creation</td>
 *      <td>No</td>
 *   <tr>
 *   <tr>
 *      <td>failonerr</td>
 *      <td>Throw an exception if the command fails. Default is true</td>
 *      <td>No</td>
 *   <tr>
 * </table>
 *
 */
public class CCMkelem extends ClearCase {

    private String mComment = null;

    private String mCfile = null;

    private boolean mNwarn = false;

    private boolean mPtime = false;

    private boolean mNoco = false;

    private boolean mCheckin = false;

    private boolean mMaster = false;

    private String mEltype = null;

    /**
     * Executes the task.
     * <p>
     * Builds a command line to execute cleartool and then calls Exec's run method
     * to execute the command line.
     * @throws BuildException if the command fails and failonerr is set to true
     */
    public void execute() throws BuildException {
        Commandline commandLine = new Commandline();
        Project aProj = getProject();
        int result = 0;
        if (getViewPath() == null) {
            setViewPath(aProj.getBaseDir().getPath());
        }
        commandLine.setExecutable(getClearToolCommand());
        commandLine.createArgument().setValue(COMMAND_MKELEM);
        checkOptions(commandLine);
        if (!getFailOnErr()) {
            getProject().log("Ignoring any errors that occur for: " + getViewPathBasename(), Project.MSG_VERBOSE);
        }
        result = run(commandLine);
        if (Execute.isFailure(result) && getFailOnErr()) {
            String msg = "Failed executing: " + commandLine.toString();
            throw new BuildException(msg, getLocation());
        }
    }

    /**
     * Check the command line options.
     */
    private void checkOptions(Commandline cmd) {
        if (getComment() != null) {
            getCommentCommand(cmd);
        } else {
            if (getCommentFile() != null) {
                getCommentFileCommand(cmd);
            } else {
                cmd.createArgument().setValue(FLAG_NOCOMMENT);
            }
        }
        if (getNoWarn()) {
            cmd.createArgument().setValue(FLAG_NOWARN);
        }
        if (getNoCheckout() && getCheckin()) {
            throw new BuildException("Should choose either [nocheckout | checkin]");
        }
        if (getNoCheckout()) {
            cmd.createArgument().setValue(FLAG_NOCHECKOUT);
        }
        if (getCheckin()) {
            cmd.createArgument().setValue(FLAG_CHECKIN);
            if (getPreserveTime()) {
                cmd.createArgument().setValue(FLAG_PRESERVETIME);
            }
        }
        if (getMaster()) {
            cmd.createArgument().setValue(FLAG_MASTER);
        }
        if (getEltype() != null) {
            getEltypeCommand(cmd);
        }
        cmd.createArgument().setValue(getViewPath());
    }

    /**
     * Sets the comment string.
     *
     * @param comment the comment string
     */
    public void setComment(String comment) {
        mComment = comment;
    }

    /**
     * Get comment string
     *
     * @return String containing the comment
     */
    public String getComment() {
        return mComment;
    }

    /**
     * Specifies a file containing a comment.
     *
     * @param cfile the path to the comment file
     */
    public void setCommentFile(String cfile) {
        mCfile = cfile;
    }

    /**
     * Get comment file
     *
     * @return String containing the path to the comment file
     */
    public String getCommentFile() {
        return mCfile;
    }

    /**
     * If true, suppress warning messages.
     *
     * @param nwarn the status to set the flag to
     */
    public void setNoWarn(boolean nwarn) {
        mNwarn = nwarn;
    }

    /**
     * Get nowarn flag status
     *
     * @return boolean containing status of nwarn flag
     */
    public boolean getNoWarn() {
        return mNwarn;
    }

    /**
     * If true, preserve the modification time.
     *
     * @param ptime the status to set the flag to
     */
    public void setPreserveTime(boolean ptime) {
        mPtime = ptime;
    }

    /**
     * Get preservetime flag status
     *
     * @return boolean containing status of preservetime flag
     */
    public boolean getPreserveTime() {
        return mPtime;
    }

    /**
     * If true, do not checkout element after creation.
     *
     * @param co the status to set the flag to
     */
    public void setNoCheckout(boolean co) {
        mNoco = co;
    }

    /**
     * Get no checkout flag status
     *
     * @return boolean containing status of noco flag
     */
    public boolean getNoCheckout() {
        return mNoco;
    }

    /**
     * If true, checkin the element after creation
     *
     * @param ci the status to set the flag to
     */
    public void setCheckin(boolean ci) {
        mCheckin = ci;
    }

    /**
     * Get ci flag status
     *
     * @return boolean containing status of ci flag
     */
    public boolean getCheckin() {
        return mCheckin;
    }

    /**
     * If true, changes mastership of the main branch
     * to the current site
     *
     * @param master the status to set the flag to
     */
    public void setMaster(boolean master) {
        mMaster = master;
    }

    /**
     * Get master flag status
     *
     * @return boolean containing status of master flag
     */
    public boolean getMaster() {
        return mMaster;
    }

    /**
     * Specifies the element type to use.
     *
     * @param eltype to create element
     */
    public void setEltype(String eltype) {
        mEltype = eltype;
    }

    /**
     * Get element type
     *
     * @return String containing the element type
     */
    public String getEltype() {
        return mEltype;
    }

    /**
     * Get the 'comment' command
     *
     * @param cmd containing the command line string with or
     *            without the comment flag and string appended
     */
    private void getCommentCommand(Commandline cmd) {
        if (getComment() != null) {
            cmd.createArgument().setValue(FLAG_COMMENT);
            cmd.createArgument().setValue(getComment());
        }
    }

    /**
     * Get the 'commentfile' command
     *
     * @param cmd containing the command line string with or
     *            without the commentfile flag and file appended
     */
    private void getCommentFileCommand(Commandline cmd) {
        if (getCommentFile() != null) {
            cmd.createArgument().setValue(FLAG_COMMENTFILE);
            cmd.createArgument().setValue(getCommentFile());
        }
    }

    /**
     * Get the 'element type' command
     *
     * @param cmd containing the command line string with or
     *            without the comment flag and string appended
     */
    private void getEltypeCommand(Commandline cmd) {
        if (getEltype() != null) {
            cmd.createArgument().setValue(FLAG_ELTYPE);
            cmd.createArgument().setValue(getEltype());
        }
    }

    /**
     * -c flag -- comment to attach to the file
     */
    public static final String FLAG_COMMENT = "-c";

    /**
     * -cfile flag -- file containing a comment to attach to the file
     */
    public static final String FLAG_COMMENTFILE = "-cfile";

    /**
     * -nc flag -- no comment is specified
     */
    public static final String FLAG_NOCOMMENT = "-nc";

    /**
     * -nwarn flag -- suppresses warning messages
     */
    public static final String FLAG_NOWARN = "-nwarn";

    /**
     * -ptime flag -- preserves the modification time on checkin
     */
    public static final String FLAG_PRESERVETIME = "-ptime";

    /**
     * -nco flag -- do not checkout element after creation
     */
    public static final String FLAG_NOCHECKOUT = "-nco";

    /**
     * -ci flag -- checkin element after creation
     */
    public static final String FLAG_CHECKIN = "-ci";

    /**
     * -master flag -- change mastership of main branch to current site
     */
    public static final String FLAG_MASTER = "-master";

    /**
     * -eltype flag -- element type to use during creation
     */
    public static final String FLAG_ELTYPE = "-eltype";
}
