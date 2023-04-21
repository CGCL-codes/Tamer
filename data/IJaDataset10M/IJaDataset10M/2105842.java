package org.pluginbuilder.core.internal.webdav;

import org.apache.webdav.lib.WebdavResource;

public abstract class SyncCommand {

    protected final WebdavResource resource;

    public SyncCommand(WebdavResource resource) {
        this.resource = resource;
    }

    public WebdavResource getResource() {
        return resource;
    }

    public abstract boolean execute() throws WebDavSyncException;

    public abstract String description();
}
