package org.dinopolis.gpstool.plugin.export;

import java.net.URL;
import org.dinopolis.gpstool.GpsylonKeyConstants;

/**
 * This plugin writes track data to a stream.
 *
 * @author Christof Dallermassl
 * @version $Revision: 874 $
 */
public class WriteGPX10Plugin extends AbstractTemplateExportPlugin implements GpsylonKeyConstants {

    private static final String KEY_WRITEGPX10_PLUGIN_IDENTIFIER = "export.gpx10.plugin.identifier";

    private static final String KEY_WRITEGPX10_PLUGIN_VERSION = "export.gpx10.plugin.version";

    private static final String KEY_WRITEGPX10_PLUGIN_NAME = "export.gpx10.plugin.name";

    private static final String KEY_WRITEGPX10_PLUGIN_DESCRIPTION = "export.gpx10.plugin.description";

    private static final String KEY_WRITEGPX10_FILE_EXTENSION = "export.gpx10.file.extension";

    private static final String KEY_WRITEGPX10_FILE_DESCRIPTIVE_NAME = "export.gpx10.file.descriptive_name";

    static final String KEY_WRITEGPX10_TEMPLATE = "export.gpx10.template";

    /**
   * Default Constructor
   */
    public WriteGPX10Plugin() {
        super();
    }

    /**
   * Returns the url of the template to use.
   * @return the url of the template to use.
   */
    protected URL getTemplateUrl() {
        String template = getResources().getString(KEY_WRITEGPX10_TEMPLATE);
        URL url = getClass().getClassLoader().getResource(template);
        return url;
    }

    /**
 * Returns a short description of the track data that may be used e.g. in
 * a file chooser. If possible, the description should be localized.
 *
 * @return The short description of the content.
 */
    public String getContentDescription() {
        return (getResources().getString(KEY_WRITEGPX10_FILE_DESCRIPTIVE_NAME));
    }

    /**
 * Returns possible file extensions the content. This information
 * may be used in a file chooser as a filter (e.g. ["jpg","jpeg"]).
 *
 * @return The file extensions to use for this kind of data.
 */
    public String[] getContentFileExtensions() {
        return (getResources().getStringArray(KEY_WRITEGPX10_FILE_EXTENSION));
    }

    /**
 * Returns the unique id of the plugin. The id is used to identify
 * the plugin and to distinguish it from other plugins.
 *
 * @return The id of the plugin.
 */
    public String getPluginIdentifier() {
        return (getResources().getString(KEY_WRITEGPX10_PLUGIN_IDENTIFIER));
    }

    /**
 * Returns the version of the plugin. The version may be used to
 * choose between different version of the same plugin.
 *
 * @return The version of the plugin.
 */
    public float getPluginVersion() {
        return ((float) getResources().getDouble(KEY_WRITEGPX10_PLUGIN_VERSION));
    }

    /**
 * Returns the name of the Plugin. The name should be a human
 * readable and understandable name like "Save Image as JPEG". It is
 * prefereable but not necessary that the name is localized.
 *
 * @return The name of the plugin.
 */
    public String getPluginName() {
        return (getResources().getString(KEY_WRITEGPX10_PLUGIN_NAME));
    }

    /**
 * Returns a description of the Plugin. The description should be
 * human readable and understandable like "This plugin saves the
 * content of the main window as an image in jpeg format". It is
 * prefereable but not necessary that the description is localized.
 *
 * @return The description of the plugin.
 */
    public String getPluginDescription() {
        return (getResources().getString(KEY_WRITEGPX10_PLUGIN_DESCRIPTION));
    }
}
