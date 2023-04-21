package net.sf.magicmap.client.model.node;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SimpleInfoObject implements InfoObject {

    private Set<InfoObjectTag> tags = new HashSet<InfoObjectTag>();

    private String depiction;

    private String description;

    private String title;

    private String type;

    private String url;

    private String serviceUrl;

    public SimpleInfoObject() {
    }

    public void addTag(InfoObjectTag tag) {
        tags.add(tag);
    }

    public String getDepiction() {
        return depiction;
    }

    public String getDescription() {
        return description;
    }

    public String getInfoTitle() {
        return title;
    }

    public String getInfoType() {
        return type;
    }

    public String getInfoUrl() {
        return url;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public Set<InfoObjectTag> getTags() {
        return tags;
    }

    public Iterator<InfoObjectTag> iterator() {
        return tags.iterator();
    }

    public void setDepiction(String depiction) {
        this.depiction = depiction;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public void setTags(Set<InfoObjectTag> tags) {
        this.tags = (tags);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
