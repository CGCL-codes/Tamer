package net.sf.ttd.core.utils;

import java.util.Set;
import org.eclipse.core.resources.IResource;

/**
 * @author pkrupets
 */
public final class ResourcesUtils {

    public static void addResourceAndParents(Set<IResource> resources, IResource resource) {
        while (resource != null && resource.getType() != IResource.ROOT) {
            resources.add(resource);
            resource = resource.getParent();
        }
    }
}
