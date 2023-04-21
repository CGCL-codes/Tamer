package org.codecover.ant;

import java.io.File;
import org.apache.tools.ant.BuildException;
import org.codecover.model.extensions.PluginUtils;

/**
 * @author Steffen Kieß
 * @version 1.0 ($Id: AddPluginDirCommand.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
public class AddPluginDirCommand extends Command {

    File dir;

    /**
     * Sets the plugin directory.
     * 
     * @param dir
     *                the {@link File} to set.
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    @Override
    public void run(Context context) {
        if (this.dir == null) {
            throw new BuildException("The attribute 'dir' is missing.");
        }
        PluginUtils.loadPluginsFromDirectory(context.getPluginManager(), context.getLogger(), this.dir);
    }
}
