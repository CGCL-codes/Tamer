package edu.uiuc.ncsa.security.delegation.server.storage.impl;

import com.google.inject.Inject;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientStore;
import edu.uiuc.ncsa.security.delegation.storage.Client;
import edu.uiuc.ncsa.security.storage.FileStore;

/**
 * File-based storage for clients.
 * <p>Created by Jeff Gaynor<br>
 * on 11/3/11 at  3:40 PM
 */
public abstract class ClientFileStore<V extends Client> extends FileStore<String, V> implements ClientStore<V> {

    @Inject
    protected ClientFileStore(String storeDirectory, String indexDirectory) {
        super(storeDirectory, indexDirectory);
    }
}
