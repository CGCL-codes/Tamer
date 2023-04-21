package org.thechiselgroup.choosel.workbench.client.workspace;

import org.thechiselgroup.choosel.workbench.client.authentication.AuthenticationManager;
import com.google.inject.Inject;

public class DefaultShareConfigurationFactory implements ShareConfigurationFactory {

    private final ViewSaver viewSaver;

    private final AuthenticationManager manager;

    @Inject
    public DefaultShareConfigurationFactory(ViewSaver viewSaver, AuthenticationManager manager) {
        assert viewSaver != null;
        assert manager != null;
        this.viewSaver = viewSaver;
        this.manager = manager;
    }

    @Override
    public ShareConfiguration createShareConfiguration() {
        return new DefaultShareConfiguration(viewSaver, manager);
    }
}
