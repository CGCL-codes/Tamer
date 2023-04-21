package net.sourceforge.x360mediaserve.util.osgi;

import net.sourceforge.x360mediaserve.api.services.ContentServer;

public interface ContentServerListener {

    public void addContentServer(ContentServer server);

    public void removeContentServer(ContentServer server);
}
