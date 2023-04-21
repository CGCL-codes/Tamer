package net.sf.myway.map;

import net.sf.myway.map.bl.MapBL;
import net.sf.myway.map.bl.impl.MapBLImpl;
import net.sf.myway.map.da.MapDA;
import net.sf.myway.map.da.impl.MapDAImpl;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class MapPlugin extends Plugin {

    private static Logger _log = Logger.getLogger(MapPlugin.class);

    public static final String PLUGIN_ID = "net.sf.myway.map";

    private static MapPlugin plugin;

    private static MapDA _da;

    private static MapBL _bl;

    public static MapBL getBL() {
        if (_bl == null) _bl = new MapBLImpl();
        return _bl;
    }

    public static MapDA getDA() {
        if (_da == null) _da = new MapDAImpl();
        return _da;
    }

    /**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
    public static MapPlugin getDefault() {
        return plugin;
    }

    /**
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
    @Override
    public void start(final BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    /**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
    @Override
    public void stop(final BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
}
