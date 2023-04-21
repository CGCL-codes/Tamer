package org.eclipse.emf.exporter;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.converter.ConverterPlugin;

/**
 * The <b>Plugin</b> for the model exporter.
 * @since 2.2.0
 */
public final class ExporterPlugin extends EMFPlugin {

    /**
   * The singleton instance of the plugin.
   */
    public static final ExporterPlugin INSTANCE = new ExporterPlugin();

    /**
   * The one instance of this class.
   */
    private static Implementation plugin;

    public static final String ID = "org.eclipse.emf.exporter";

    /**
   * Creates the singleton instance.
   */
    private ExporterPlugin() {
        super(new ResourceLocator[] { ConverterPlugin.INSTANCE });
    }

    @Override
    public ResourceLocator getPluginResourceLocator() {
        return plugin;
    }

    /**
   * Returns the singleton instance of the Eclipse plugin.
   * @return the singleton instance.
   */
    public static Implementation getPlugin() {
        return plugin;
    }

    /**
   * The actual implementation of the Eclipse <b>Plugin</b>.
   */
    public static class Implementation extends EclipsePlugin {

        /**
     * Creates an instance.
     */
        public Implementation() {
            super();
            plugin = this;
        }
    }
}
