package org.jerger.almostcms.domain;

import static org.jerger.common.infrastructur.text.ParseUtils.replaceAll;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import de.domainframework.common.types.web.PathKey;

public class AbstractPage {

    @NotNull
    protected PathKey key;

    @NotNull
    protected String content;

    protected List<EmbeddedResource> embeddedResources = new ArrayList<EmbeddedResource>();

    public AbstractPage(PathKey key, String content, List<EmbeddedResource> embeddedResources) {
        this.key = key;
        this.content = content;
        if (embeddedResources != null) {
            this.embeddedResources.addAll(embeddedResources);
        }
    }

    public PathKey getKey() {
        return key;
    }

    public String getContent() {
        return content;
    }

    public String getContentWithLinksAdjusted(PathKey context, Map<String, EmbeddedNode> embeddedNodes) {
        StringBuffer resultBuffer = new StringBuffer(content);
        for (String originalLink : embeddedNodes.keySet()) {
            PathKey replacePath = context.relativizePath(embeddedNodes.get(originalLink).getKey());
            resultBuffer = replaceAll(resultBuffer, originalLink, replacePath.getPathAsString());
        }
        for (EmbeddedResource embeddedResource : embeddedResources) {
            String replacePath = embeddedResource.getKey().getPathAsString();
            String origPath = embeddedResource.getKey().getPathAsString();
            resultBuffer = replaceAll(resultBuffer, origPath, replacePath);
        }
        return resultBuffer.toString();
    }

    public void addEmbeddedResource(EmbeddedResource embeddedResource) {
        embeddedResources.add(embeddedResource);
    }

    public List<EmbeddedResource> getEmbeddedResources() {
        return embeddedResources;
    }

    @Override
    public String toString() {
        return "AbstractPage [key=" + key + "]";
    }
}
