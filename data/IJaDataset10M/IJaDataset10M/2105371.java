package net.tourbook.srtm;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "net.tourbook.ext.srtm";

    private static Activator plugin;

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }

    /**
	 * Returns an image descriptor for images in the plug-in icons path.
	 * 
	 * @param path
	 *            relative path to the <i>icons/</i> directory
	 * @return Returns the image descriptor
	 */
    public static ImageDescriptor getImageDescriptor(final String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, "icons/" + path);
    }

    /**
	 * The constructor
	 */
    public Activator() {
    }

    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * @param sectionName
	 * @return Returns the dialog setting section for the sectionName, a section is always returned
	 *         even when it's empty
	 */
    public IDialogSettings getDialogSettingsSection(final String sectionName) {
        final IDialogSettings dialogSettings = getDialogSettings();
        IDialogSettings section = dialogSettings.getSection(sectionName);
        if (section == null) {
            section = dialogSettings.addNewSection(sectionName);
        }
        return section;
    }
}
