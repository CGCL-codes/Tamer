package org.thechiselgroup.choosel.core.client.visualization.model.managed;

import org.thechiselgroup.choosel.core.client.util.collections.LightweightCollection;

public interface VisualItemValueResolverFactoryProvider {

    VisualItemValueResolverFactory get(String id);

    LightweightCollection<VisualItemValueResolverFactory> getAll();
}
