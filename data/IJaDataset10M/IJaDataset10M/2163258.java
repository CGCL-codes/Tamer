package de.mpiwg.vspace.navigation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import de.mpiwg.vspace.common.properties.CommonPropertiesProvider;
import de.mpiwg.vspace.navigation.util.PropertyHandler;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "de.mpiwg.vspace.navigation";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        IPreferenceStore ps = CommonPropertiesProvider.INSTANCE.getPreferenceStore();
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_start_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_back_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_copyright_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_forward_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_bp_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_start_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_back_scene_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_back_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_copyright_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_forward_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_module_bp_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_start_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_back_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_copyright_use_default_image"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_start_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_back_use_default_text"), true);
        ps.setDefault(PropertyHandler.getInstance().getProperty("_entry_name_copyright_use_default_text"), true);
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static Activator getDefault() {
        return plugin;
    }
}
