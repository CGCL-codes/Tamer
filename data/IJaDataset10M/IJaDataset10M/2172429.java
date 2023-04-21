package com.android.ide.eclipse.adt.internal.sdk;

import com.android.ide.eclipse.adt.internal.resources.IResourceRepository;
import com.android.ide.eclipse.adt.internal.resources.ResourceItem;
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the {@link IResourceRepository} interface to hold the system resource Ids
 * parsed by {@link AndroidTargetParser}. 
 */
final class FrameworkResourceRepository implements IResourceRepository {

    private Map<ResourceType, List<ResourceItem>> mResourcesMap;

    public FrameworkResourceRepository(Map<ResourceType, List<ResourceItem>> systemResourcesMap) {
        mResourcesMap = systemResourcesMap;
    }

    public ResourceType[] getAvailableResourceTypes() {
        if (mResourcesMap != null) {
            Set<ResourceType> types = mResourcesMap.keySet();
            if (types != null) {
                return types.toArray(new ResourceType[types.size()]);
            }
        }
        return null;
    }

    public ResourceItem[] getResources(ResourceType type) {
        if (mResourcesMap != null) {
            List<ResourceItem> items = mResourcesMap.get(type);
            if (items != null) {
                return items.toArray(new ResourceItem[items.size()]);
            }
        }
        return null;
    }

    public boolean hasResources(ResourceType type) {
        if (mResourcesMap != null) {
            List<ResourceItem> items = mResourcesMap.get(type);
            return (items != null && items.size() > 0);
        }
        return false;
    }

    public boolean isSystemRepository() {
        return true;
    }
}
