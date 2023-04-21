package org.apache.jetspeed.util.ant;

import java.io.File;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import org.apache.jetspeed.util.OverwriteProperties;

/** * <code>OverwritePropertiesTask</code> is the task definition for an Ant * interface to the <code>OverwriteProperties</code>. *  * @created January 29, 2003 * @author Eric Pugh * @version $Revision: 1.3 $ * @see org.apache.tools.ant.Task */
public class OverwritePropertiesTask extends Task {

    /** File to merge properties into */
    private File mergeBaseProperties;

    /** File to merge properties from */
    private File mergeProperties;

    /** Directory to look for includes in */
    private File includesDir;

    /** Fail on error flag */
    private boolean failonerror = true;

    /**     *  Sets the File to merge properties into     *      * @param mergeBaseProperties     *               File to merge properties into     */
    public void setMergeBaseProperties(File mergeBaseProperties) {
        this.mergeBaseProperties = mergeBaseProperties;
    }

    /**     *  Sets the File to merge properties from     *     * @param  mergeProperties  File to merge properties from     */
    public void setMergeProperties(File mergeProperties) {
        this.mergeProperties = mergeProperties;
    }

    /**     *  Sets the Directory to look for includes in     *     * @param  includesDir  Directory to look for includes in     */
    public void setIncludesDir(File includesDir) {
        this.includesDir = includesDir;
    }

    /**     * If false, note errors to the output but keep going.     * @param failonerror true or false     */
    public void setFailOnError(boolean failonerror) {
        this.failonerror = failonerror;
    }

    /**     *  Gets the File to merge properties into     *      * @return File to merge properties into     */
    public File getMergeBaseProperties() {
        return mergeBaseProperties;
    }

    /**     *  Gets the File to merge properties from     *      * @return File to merge properties from     */
    public File getMergeProperties() {
        return mergeProperties;
    }

    /**     *  Gets the Directory to look for includes in     *      * @return Directory to look for includes in     */
    public File getIncludesDir() {
        return includesDir;
    }

    /**     * Load the step and then execute it     *      * @exception BuildException     *                   Description of the Exception     */
    public void execute() throws BuildException {
        try {
            OverwriteProperties overwriteProperties = new OverwriteProperties();
            overwriteProperties.setBaseProperties(getMergeBaseProperties());
            overwriteProperties.setProperties(getMergeProperties());
            overwriteProperties.setIncludeRoot(getIncludesDir());
            overwriteProperties.execute();
        } catch (Exception e) {
            if (!this.failonerror) {
                log(e.toString());
            } else {
                throw new BuildException(e.toString());
            }
        }
    }
}
