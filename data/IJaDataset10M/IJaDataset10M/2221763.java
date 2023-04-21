package org.cilogon.d2.storage.impl.filestore.provider;

import edu.uiuc.ncsa.myproxy.oa4mp.server.XMLConfigTags;
import edu.uiuc.ncsa.security.storage.FSProvider;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.cilogon.d2.storage.provider.MultiUserStoreProvider;
import org.cilogon.d2.storage.impl.filestore.CILFSArchivedUserStore;
import org.cilogon.d2.util.ServiceTokenFactoryProvider;
import java.io.File;

/**
 * <p>Created by Jeff Gaynor<br>
 * on 3/19/12 at  7:30 PM
 */
public class CILFSArchivedUserStoreProvider extends FSProvider<CILFSArchivedUserStore> implements XMLConfigTags {

    MultiUserStoreProvider usp;

    ServiceTokenFactoryProvider serviceTokenFactory;

    public CILFSArchivedUserStoreProvider(ConfigurationNode config, ServiceTokenFactoryProvider serviceTokenFactory, MultiUserStoreProvider userStore) {
        super(config, FILE_STORE, ARCHIVED_USERS);
        this.serviceTokenFactory = serviceTokenFactory;
        this.usp = userStore;
    }

    @Override
    protected CILFSArchivedUserStore produce(File dataPath, File indexPath) {
        return new CILFSArchivedUserStore(dataPath, indexPath, usp.get(), serviceTokenFactory.get());
    }
}
