package mfb2.tools.obclipse.handleproduct;

import java.io.File;
import java.util.List;
import mfb2.tools.obclipse.ObclipseProps;
import mfb2.tools.obclipse.exceptions.ObclipseException;
import mfb2.tools.obclipse.io.FileOperations;
import mfb2.tools.obclipse.io.ZipCreator;
import mfb2.tools.obclipse.util.Msg;

public class BuildAppJars {

    public boolean buildAppJars(List<String> extracedPluginDirs) throws ObclipseException {
        boolean success = true;
        File appPluginDir = new File(ObclipseProps.get(ObclipseProps.APP_PLUGIN_DIR));
        if (appPluginDir.exists()) {
            for (String pluginDir : extracedPluginDirs) {
                File plugin = new File(pluginDir);
                if (plugin.exists()) {
                    Msg.verbose("Create '" + pluginDir + ".jar'...");
                    if (ZipCreator.createZipFile(pluginDir + ".jar", plugin)) {
                        success &= FileOperations.deleteDir(plugin);
                    }
                }
            }
        }
        return success;
    }
}
