package org.thechiselgroup.choosel.core.client.visualization.resolvers.ui;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.ManagedSlotMapping;
import org.thechiselgroup.choosel.core.client.visualization.model.managed.VisualItemValueResolverFactory;

public class EmptyWidgetResolverUIControllerFactory implements VisualItemValueResolverUIControllerFactory {

    private VisualItemValueResolverFactory resolverFactory;

    public EmptyWidgetResolverUIControllerFactory(VisualItemValueResolverFactory resolverFactory) {
        assert resolverFactory != null;
        this.resolverFactory = resolverFactory;
    }

    @Override
    public VisualItemValueResolverUIController create(ManagedSlotMapping uiModel, LightweightCollection<VisualItem> visualItem) {
        return new EmptyWidgetResolverUIController(getId());
    }

    @Override
    public String getId() {
        return resolverFactory.getId();
    }
}
