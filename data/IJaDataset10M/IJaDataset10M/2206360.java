package org.orbeon.oxf.resources;

import java.util.Map;

public class FilesystemResourceManagerFactory implements ResourceManagerFactoryFunctor {

    private Map props;

    public FilesystemResourceManagerFactory(Map props) {
        this.props = props;
    }

    public ResourceManager makeInstance() {
        return new FilesystemResourceManagerImpl(props);
    }
}
