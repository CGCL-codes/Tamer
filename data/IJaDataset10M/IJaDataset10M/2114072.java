package com.jtimetrack.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Work implements Serializable {

    private String title;

    private String description;

    private List subworks;

    private Work parent;

    public Work(String title, String description) {
        this.title = title;
        this.description = description;
        this.subworks = new ArrayList();
    }

    public void addSubWork(Work work) {
        this.subworks.add(work);
        work.setParent(this);
    }

    private void setParent(Work work) {
        this.parent = work;
    }

    private Work getParent() {
        return parent;
    }

    public String formatPathToRoot() {
        String path = "";
        Work parent = getParent();
        while (parent != null && parent.getParent() != null) {
            path = parent.getTitle() + "/" + path;
            parent = parent.getParent();
        }
        return path;
    }

    public String toString() {
        return title + " " + description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public List getSubWorks() {
        return subworks;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void removeChild(Work work) {
        this.subworks.remove(work);
    }

    public int getDuration() {
        return 0;
    }
}
