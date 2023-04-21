package com.hyk.proxy.framework;

import java.util.concurrent.ThreadPoolExecutor;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hyk.proxy.framework.appdata.AppData;
import com.hyk.proxy.framework.common.Constants;
import com.hyk.proxy.framework.common.Misc;
import com.hyk.proxy.framework.config.Config;
import com.hyk.proxy.framework.event.HttpProxyEventServiceFactory;
import com.hyk.proxy.framework.httpserver.HttpLocalProxyServer;
import com.hyk.proxy.framework.management.ManageResource;
import com.hyk.proxy.framework.management.UDPManagementServer;
import com.hyk.proxy.framework.plugin.PluginManager;
import com.hyk.proxy.framework.prefs.Preferences;
import com.hyk.proxy.framework.trace.Trace;
import com.hyk.proxy.framework.update.Updater;

/**
 *
 */
public class Framework implements ManageResource {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private HttpLocalProxyServer server;

    private UDPManagementServer commandServer;

    private HttpProxyEventServiceFactory esf = null;

    private PluginManager pm;

    private Updater updater;

    private boolean isStarted = false;

    private Trace trace;

    public Framework(Trace trace) {
        this.trace = trace;
        Preferences.init();
        pm = PluginManager.getInstance();
        Config config = Config.loadConfig();
        ThreadPoolExecutor workerExecutor = new OrderedMemoryAwareThreadPoolExecutor(config.getThreadPoolSize(), 0, 0);
        Misc.setGlobalThreadPool(workerExecutor);
        Misc.setTrace(trace);
        pm.loadPlugins(trace);
        pm.activatePlugins(trace);
        updater = new Updater(this);
    }

    public void stop() {
        try {
            if (null != commandServer) {
                commandServer.stop();
                commandServer = null;
            }
            if (null != server) {
                server.close();
                server = null;
            }
            if (null != esf) {
                esf.destroy();
            }
            isStarted = false;
        } catch (Exception e) {
            logger.error("Failed to stop framework.", e);
        }
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean start() {
        return restart();
    }

    public boolean restart() {
        try {
            stop();
            Config config = Config.loadConfig();
            esf = HttpProxyEventServiceFactory.Registry.getHttpProxyEventServiceFactory(config.getProxyEventServiceFactory());
            if (esf == null) {
                logger.error("No event service factory found with name:" + config.getProxyEventServiceFactory());
                return false;
            }
            esf.init();
            server = new HttpLocalProxyServer(config.getLocalProxyServerAddress(), Misc.getGlobalThreadPool(), esf);
            commandServer = new UDPManagementServer(config.getLocalProxyServerAddress());
            Misc.setManagementServer(commandServer);
            commandServer.addManageResource(this);
            commandServer.addManageResource(pm);
            Misc.getGlobalThreadPool().execute(commandServer);
            trace.notice("Local HTTP Server Running...\nat " + config.getLocalProxyServerAddress());
            isStarted = true;
            return true;
        } catch (Exception e) {
            logger.error("Failed to launch local proxy server.", e);
        }
        return false;
    }

    @Override
    public String handleManagementCommand(String cmd) {
        if (cmd.equals(Constants.STOP_CMD)) {
            System.exit(1);
        }
        return null;
    }

    @Override
    public String getName() {
        return Constants.FRAMEWORK_NAME;
    }
}
