package org.deft.repository.chaptertype;

import java.net.URL;
import org.deft.resource.ResourceHandler;

public class ChapterTypeResourceHandler implements ResourceHandler {

    private ResourceHandler basicHandler;

    private ChapterType type;

    public ChapterTypeResourceHandler(ChapterType type, ResourceHandler basicHandler) {
        this.type = type;
        this.basicHandler = basicHandler;
    }

    @Override
    public URL getResourceUrl(String resourcePath) {
        return basicHandler.getResourceUrl(resourcePath);
    }

    @Override
    public String getResourceHandlerId() {
        return basicHandler.getResourceHandlerId();
    }

    public URL getChapterTemplateUrl() {
        String localChapterTemplateLocation = type.getResourceLocation() + "/" + type.getChapterTemplateLocation();
        URL url = getResourceUrl(localChapterTemplateLocation);
        return url;
    }

    public ChapterType getProjectType() {
        return type;
    }
}
