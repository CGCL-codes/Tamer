package org.nakedobjects.xmlpersistence.profilestore;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.userprofile.UserProfileStore;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;

public class XmlUserProfileLoaderInstaller implements UserProfileStoreInstaller {

    public String getName() {
        return "xml";
    }

    public void init() {
    }

    public void shutdown() {
    }

    public UserProfileStore createUserProfileStore(NakedObjectConfiguration configuration) {
        return new XmlUserProfileStore(configuration);
    }
}
