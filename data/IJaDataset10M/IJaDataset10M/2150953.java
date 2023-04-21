package org.rich.charlesmurphy.core;

import org.rich.charlesmurphy.model.Request;
import org.rich.charlesmurphy.model.Resource;
import org.rich.charlesmurphy.model.ResourceType;
import org.rich.charlesmurphy.model.StatusCode;

/**
 * @author Rastaman
 */
public class CharliesUtils {

    public static ResourceType determineResourceType(String path) {
        if (path.matches(".*iframe_history\\.html.*")) {
            return ResourceType.OTHER;
        }
        int idx = path.lastIndexOf(".");
        String extension = path.substring(idx + 1);
        for (ResourceType rt : ResourceType.values()) {
            if (rt.hasExtension(extension)) {
                return rt;
            }
        }
        return ResourceType.OTHER;
    }

    public static boolean isRequestForResource(Request request, Resource resource) {
        return request.getPath().equals(resource.getPath());
    }

    public static boolean wasCached(String statusCode) {
        return StatusCode.fromCode(statusCode) == StatusCode.CACHED;
    }
}
