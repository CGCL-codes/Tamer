package de.lulusoft.anothertorrent.gui.views.Providers;

import java.io.Serializable;
import de.lulusoft.anothertorrent.core.servers.Server;

public class TorrentListViewInput implements Serializable {

    private static final long serialVersionUID = 1462650132783662768L;

    private Server server = null;

    private String viewName = "";

    public TorrentListViewInput(Server server, String viewName) {
        super();
        this.server = server;
        this.viewName = viewName;
    }

    public Server getServer() {
        return server;
    }

    public String getViewName() {
        return viewName;
    }
}
