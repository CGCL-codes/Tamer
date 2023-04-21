package net.sf.lfscontrol.core.model;

import java.util.List;

public class Project {

    /** Project name. */
    private final String fName;

    /** Project servers. */
    private final List<Server> fServers;

    /** Cache of project servers; set to null every time fServers has changed. */
    private Server[] fServersCache = null;

    /**
	 * Constructor.
	 *
	 * @param name project name
	 * @param servers LFS servers of the project
	 */
    public Project(final String name, final List<Server> servers) {
        fName = name;
        fServers = servers;
    }

    public String getName() {
        return fName;
    }

    public Server[] getServers() {
        if (fServersCache == null) {
            fServersCache = fServers.toArray(new Server[fServers.size()]);
        }
        return fServersCache;
    }
}
