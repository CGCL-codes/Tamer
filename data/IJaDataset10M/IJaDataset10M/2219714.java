package edu.uiuc.ncsa.myproxy.oa4mp.server.storage.filestore;

import edu.uiuc.ncsa.myproxy.oa4mp.server.DSTransaction;
import edu.uiuc.ncsa.security.delegation.storage.impl.FSTransactionStore;
import java.io.File;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 11/3/11 at  12:49 PM
 */
public class DSFSTransactionStore<V extends DSTransaction> extends FSTransactionStore {

    public DSFSTransactionStore(File file) {
        super(file);
    }

    public DSFSTransactionStore(File storeDirectory, File indexDirectory) {
        super(storeDirectory, indexDirectory);
    }

    @Override
    public DSTransaction create() {
        return new DSTransaction();
    }
}
