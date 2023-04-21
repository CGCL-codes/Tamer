package org.thechiselgroup.choosel.workbench.client.workspace.dto;

import java.io.Serializable;
import org.thechiselgroup.choosel.core.client.persistence.Memento;
import org.thechiselgroup.choosel.core.client.resources.Resource;

public class ViewDTO implements Serializable {

    private static final long serialVersionUID = -8166733920666870199L;

    private String contentType;

    private String title;

    private Long id;

    private ResourceSetDTO[] resourceSets;

    private Resource[] resources;

    private Memento viewState;

    public String getContentType() {
        return contentType;
    }

    public Long getId() {
        return id;
    }

    public Resource[] getResources() {
        return resources;
    }

    public ResourceSetDTO[] getResourceSets() {
        return resourceSets;
    }

    public String getTitle() {
        return title;
    }

    public Memento getViewState() {
        return viewState;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setResources(Resource[] resources) {
        this.resources = resources;
    }

    public void setResourceSets(ResourceSetDTO[] resourceSets) {
        this.resourceSets = resourceSets;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setViewState(Memento viewState) {
        this.viewState = viewState;
    }
}
