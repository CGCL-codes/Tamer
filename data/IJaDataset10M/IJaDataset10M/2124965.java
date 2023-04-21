package org.eclipse.ui.internal.views.log;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.eclipse.ui.views.log";

    private static Activator plugin;

    /**
	 * The constructor
	 */
    public Activator() {
    }

    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Activator getDefault() {
        return plugin;
    }

    protected void initializeImageRegistry(ImageRegistry registry) {
        registry.put(SharedImages.DESC_PREV_EVENT, createImageDescriptor(SharedImages.DESC_PREV_EVENT));
        registry.put(SharedImages.DESC_NEXT_EVENT, createImageDescriptor(SharedImages.DESC_NEXT_EVENT));
        registry.put(SharedImages.DESC_ERROR_ST_OBJ, createImageDescriptor(SharedImages.DESC_ERROR_ST_OBJ));
        registry.put(SharedImages.DESC_ERROR_STACK_OBJ, createImageDescriptor(SharedImages.DESC_ERROR_STACK_OBJ));
        registry.put(SharedImages.DESC_INFO_ST_OBJ, createImageDescriptor(SharedImages.DESC_INFO_ST_OBJ));
        registry.put(SharedImages.DESC_OK_ST_OBJ, createImageDescriptor(SharedImages.DESC_OK_ST_OBJ));
        registry.put(SharedImages.DESC_WARNING_ST_OBJ, createImageDescriptor(SharedImages.DESC_WARNING_ST_OBJ));
        registry.put(SharedImages.DESC_HIERARCHICAL_LAYOUT_OBJ, createImageDescriptor(SharedImages.DESC_HIERARCHICAL_LAYOUT_OBJ));
        registry.put(SharedImages.DESC_CLEAR, createImageDescriptor(SharedImages.DESC_CLEAR));
        registry.put(SharedImages.DESC_CLEAR_DISABLED, createImageDescriptor(SharedImages.DESC_CLEAR_DISABLED));
        registry.put(SharedImages.DESC_REMOVE_LOG, createImageDescriptor(SharedImages.DESC_REMOVE_LOG));
        registry.put(SharedImages.DESC_REMOVE_LOG_DISABLED, createImageDescriptor(SharedImages.DESC_REMOVE_LOG_DISABLED));
        registry.put(SharedImages.DESC_EXPORT, createImageDescriptor(SharedImages.DESC_EXPORT));
        registry.put(SharedImages.DESC_EXPORT_DISABLED, createImageDescriptor(SharedImages.DESC_EXPORT_DISABLED));
        registry.put(SharedImages.DESC_FILTER, createImageDescriptor(SharedImages.DESC_FILTER));
        registry.put(SharedImages.DESC_FILTER_DISABLED, createImageDescriptor(SharedImages.DESC_FILTER_DISABLED));
        registry.put(SharedImages.DESC_IMPORT, createImageDescriptor(SharedImages.DESC_IMPORT));
        registry.put(SharedImages.DESC_IMPORT_DISABLED, createImageDescriptor(SharedImages.DESC_IMPORT_DISABLED));
        registry.put(SharedImages.DESC_OPEN_LOG, createImageDescriptor(SharedImages.DESC_OPEN_LOG));
        registry.put(SharedImages.DESC_OPEN_LOG_DISABLED, createImageDescriptor(SharedImages.DESC_OPEN_LOG_DISABLED));
        registry.put(SharedImages.DESC_PROPERTIES, createImageDescriptor(SharedImages.DESC_PROPERTIES));
        registry.put(SharedImages.DESC_PROPERTIES_DISABLED, createImageDescriptor(SharedImages.DESC_PROPERTIES_DISABLED));
        registry.put(SharedImages.DESC_READ_LOG, createImageDescriptor(SharedImages.DESC_READ_LOG));
        registry.put(SharedImages.DESC_READ_LOG_DISABLED, createImageDescriptor(SharedImages.DESC_READ_LOG_DISABLED));
    }

    private ImageDescriptor createImageDescriptor(String id) {
        return imageDescriptorFromPlugin(PLUGIN_ID, id);
    }
}
