package org.thechiselgroup.choosel.core.client.visualization.resolvers;

import java.util.Map;
import org.thechiselgroup.choosel.core.client.resources.Resource;
import org.thechiselgroup.choosel.core.client.resources.ResourceCategorizer;
import org.thechiselgroup.choosel.core.client.util.collections.CollectionFactory;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItem;
import org.thechiselgroup.choosel.core.client.visualization.model.VisualItemValueResolverContext;

public class VisualItemColorResolver extends AbstractBasicVisualItemValueResolver {

    private static final String[] COLORS = new String[] { "#6495ed", "#b22222", "#A9C0B1" };

    private ResourceCategorizer categorizer;

    private Map<String, String> resourceTypeToColor = CollectionFactory.createStringMap();

    public VisualItemColorResolver(ResourceCategorizer categorizer) {
        this.categorizer = categorizer;
    }

    @Override
    public boolean canResolve(VisualItem visualItem, VisualItemValueResolverContext context) {
        return true;
    }

    @Override
    public Object resolve(VisualItem visualItem, VisualItemValueResolverContext context) {
        if (visualItem.getResources().isEmpty()) {
            return COLORS[0];
        }
        Resource resource = visualItem.getResources().getFirstElement();
        String resourceType = categorizer.getCategory(resource);
        if (!resourceTypeToColor.containsKey(resourceType)) {
            resourceTypeToColor.put(resourceType, COLORS[resourceTypeToColor.size()]);
        }
        return resourceTypeToColor.get(resourceType);
    }
}
