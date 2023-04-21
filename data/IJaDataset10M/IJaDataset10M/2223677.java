package org.sf.monyserv.host;

import java.util.List;
import org.sf.monyserv.IHost;
import org.sf.monyserv.IMonitorContext;
import org.sf.monyserv.IPlugin;
import org.sf.monyserv.util.PluginUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:shomburg@hsofttec.com">S.Homburg</a>
 * @version $Id: HostThread.java 51 2007-09-09 02:54:08Z shomburg $
 */
public class HostThread extends Thread {

    private static Logger _logger = LoggerFactory.getLogger(HostThread.class);

    private IHost _host;

    /**
     * Allocates a new <code>Thread</code> object. This constructor has
     * the same effect as <code>Thread(null, null, name)</code>.
     */
    public HostThread(IHost host) {
        super(host.getName());
        _host = host;
    }

    /**
     * If this thread was constructed using a separate
     * <code>Runnable</code> run object, then that
     * <code>Runnable</code> object's <code>run</code> method is called;
     * otherwise, this method does nothing and returns.
     * <p/>
     * Subclasses of <code>Thread</code> should override this method.
     *
     * @see #start()
     * @see #stop()
     * @see #Thread(ThreadGroup,Runnable,String)
     */
    public void run() {
        List<IPlugin> pluginList = PluginUtils.getHostPlugins(getContextClassLoader(), _host);
        IMonitorContext monitorContext = _host.getHostContext().getMonitorContext();
        for (IPlugin plugin : pluginList) {
            if (plugin == null || plugin.getPluginContext().isEscalated() || plugin.getPluginContext().isDeactivated()) continue;
            monitorContext.getMonitor().addPluginToTimer(plugin);
            try {
                sleep(monitorContext.getPluginStartIntervall());
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}
