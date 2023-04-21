package uk.ac.bolton.archimate.templates;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Activator
 * Implement IStartup so that Action Delegates are initialised
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateEditorTemplatesPlugin extends AbstractUIPlugin implements IStartup {

    public static final String PLUGIN_ID = "uk.ac.bolton.archimate.templates";

    /**
     * The shared instance
     */
    public static ArchimateEditorTemplatesPlugin INSTANCE;

    /**
     * The File location of this plugin folder
     */
    private static File fPluginFolder;

    public ArchimateEditorTemplatesPlugin() {
        INSTANCE = this;
    }

    @Override
    public void earlyStartup() {
    }

    /**
     * @return The templates folder
     */
    public File getTemplatesFolder() {
        URL url = FileLocator.find(getBundle(), new Path("$nl$/templates"), null);
        try {
            url = FileLocator.resolve(url);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new File(url.getPath());
    }

    /**
     * @return The File Location of this plugin
     */
    public File getPluginFolder() {
        if (fPluginFolder == null) {
            URL url = getBundle().getEntry("/");
            try {
                url = FileLocator.resolve(url);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            fPluginFolder = new File(url.getPath());
        }
        return fPluginFolder;
    }
}
