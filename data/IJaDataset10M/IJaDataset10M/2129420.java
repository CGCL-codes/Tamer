package ti.mcore.tests.u;

/**
 * Plugin related utilities
 *
 * @author alex.k@ti.com
 */
public class PluginUtil {

    /**
	 * @param plugin
	 * @return absolute path to the Plugin's <code>src</code> directory.
	 * return <code>null</code> if plugin is <code>null</code>
	 *
	 * @author alex.k@ti.com
	 */
    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }
}
