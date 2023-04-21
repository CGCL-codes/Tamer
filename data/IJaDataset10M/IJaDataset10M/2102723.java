package pl.umk.webclient.gridbeans;

import javax.xml.namespace.QName;

/**
 * @author Rafal Osowicki (rrafcio@mat.umk.pl)
 */
public class WebGridBeanConstants {

    public static final String GRIDBEAN_DESCRIPTION = "gridbean.xml";

    public static final String NAMESPACE = "http://gpe.intel.com/gridbeans";

    public static final String NAME = "Name";

    public static final String AUTHOR = "Author";

    public static final String VERSION = "Version";

    public static final String DESCRIPTION = "Description";

    public static final String PLUGIN_VERSION = "PluginVersion";

    public static final String GRIDBEAN = "GridBean";

    public static final String PLUGIN = "Plugin";

    public static final String APPLICATION_NAME = "ApplicationName";

    public static final String APPLICATION_VERSION = "ApplicationVersion";

    public static final String OUTPUT_GUI = "GUI-output";

    public static final String INPUT_GUI = "GUI-input";

    public static final String ICON_FILE = "Icon";

    public static final String RELEASED = "Released";

    public static final QName APPLICATION = new QName(NAMESPACE, "Application");
}
