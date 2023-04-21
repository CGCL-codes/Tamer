package org.zkoss.eclipse.devframe.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import org.eclipse.core.resources.IProject;
import org.eclipse.jst.server.core.IJ2EEModule;
import org.eclipse.jst.server.core.IWebModule;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerCore;
import org.eclipse.wst.server.core.ServerPort;
import org.eclipse.wst.server.core.ServerUtil;

/**
 * @author Ian Tsai
 *
 */
public class RuntimeWebModule {

    private IModule module;

    private IWebModule j2eeModule;

    private IProject project;

    private IServer server;

    /**
	 * 
	 */
    public RuntimeWebModule(IProject prj, RuntimeRepository repository) {
        project = prj;
        RuntimeRepository.Entry en = repository.get(prj);
        if (en != null) {
            module = en.module;
            server = en.server;
            j2eeModule = (IWebModule) module.loadAdapter(IJ2EEModule.class, null);
        } else {
            doSearch();
            repository.add(prj, server, module);
        }
    }

    /**
	 * 
	 * @param project
	 */
    private void doSearch() {
        IServer[] servers = ServerCore.getServers();
        IModule[] modules = ServerUtil.getModules(project);
        for (IServer server : servers) for (IModule module : modules) if (ServerUtil.containsModule(server, module, null)) {
            this.module = module;
            this.server = server;
            j2eeModule = (IWebModule) module.loadAdapter(IJ2EEModule.class, null);
            String contextRoot = j2eeModule.getContextRoot();
            System.out.println(" project \"" + project.getName() + "\"'s module is:\"" + module.getName() + "\" with context root=\"" + contextRoot + "\"");
            return;
        }
    }

    /**
	 * 
	 * @return
	 */
    public boolean isTargetRuntimeExists() {
        return this.module != null && this.server != null;
    }

    /**
	 * 
	 * @return
	 */
    public String getContextRoot() {
        if (!isTargetRuntimeExists()) throw new IllegalStateException("the wrapped project is not a Java Web Module");
        return ((IWebModule) j2eeModule).getContextRoot();
    }

    /**
	 * 
	 * @return
	 */
    public IServer getTargetServer() {
        return server;
    }

    /**
	 * 
	 * @return
	 */
    public int getServerHttpPort() {
        return resolveHttpPort(server);
    }

    /**
     * 
     * @param server
     * @return
     */
    private static int resolveHttpPort(IServer server) {
        for (ServerPort port : server.getServerPorts(null)) {
            System.out.println("Server port is:\"" + port.getName() + "\" : \"" + port.getProtocol() + "\" : \"" + port.getPort() + "\"");
            if (port.getProtocol().toLowerCase().equals("http")) return port.getPort();
        }
        return -1;
    }
}
