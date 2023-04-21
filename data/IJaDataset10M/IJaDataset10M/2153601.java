package org.jpf.growing;

import org.java.plugin.Plugin;

/**
 * @version $Id: DBBPlugin.java,v 1.1 2007/03/04 13:00:49 ddimon Exp $
 */
public final class GPlugin extends Plugin {

    /**
     * This plug-in ID.
     */
    public static final String PLUGIN_ID = "org.jpf.growing";

    private Configuration configuration;

    /**
     * @see org.java.plugin.Plugin#doStart()
     */
    @Override
    protected void doStart() throws Exception {
        configuration = Configuration.load();
    }

    /**
     * @see org.java.plugin.Plugin#doStop()
     */
    @Override
    protected void doStop() throws Exception {
        Configuration.save(configuration);
    }

    Configuration getConfiguration() {
        return configuration;
    }
}
