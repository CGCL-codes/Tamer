package org.thechiselgroup.choosel.core.client.resources.ui;

public class AbstractResourceSetAvatarFactoryProvider implements ResourceSetAvatarFactoryProvider {

    private ResourceSetAvatarFactory resourceSetAvatarFactory;

    public AbstractResourceSetAvatarFactoryProvider(ResourceSetAvatarFactory resourceSetAvatarFactory) {
        this.resourceSetAvatarFactory = resourceSetAvatarFactory;
    }

    @Override
    public ResourceSetAvatarFactory get() {
        return resourceSetAvatarFactory;
    }
}
