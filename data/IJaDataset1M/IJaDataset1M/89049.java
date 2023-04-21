package edu.uiuc.ncsa.security.delegation.server.storage.impl;

import com.google.inject.Inject;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientApproval;
import edu.uiuc.ncsa.security.delegation.server.storage.ClientApprovalStore;
import edu.uiuc.ncsa.security.storage.FileStore;
import java.io.File;

/**
 * A store for client approvals.
 * <p>Created by Jeff Gaynor<br>
 * on 11/3/11 at  3:43 PM
 */
public abstract class FSClientApprovalStore<V extends ClientApproval> extends FileStore<String, V> implements ClientApprovalStore<V> {

    @Inject
    protected FSClientApprovalStore(File storeDirectory, File indexDirectory) {
        super(storeDirectory, indexDirectory);
    }

    protected FSClientApprovalStore(File file) {
        super(new File(file, "cas"));
    }

    @Override
    public boolean isApproved(String identifier) {
        ClientApproval ca = get(identifier);
        if (ca == null) {
            return false;
        }
        return ca.isApproved();
    }
}
