package com.joejag.mavenstats.client.views.sidebar.children;

import com.google.gwt.user.client.ui.TreeItem;
import com.joejag.mavenstats.client.dto.ProjectDTO;

public class ProjectTreeItem extends TreeItem {

    public ProjectDTO project;

    public ProjectTreeItem(String s, ProjectDTO project) {
        super(s);
        this.project = project;
    }
}
