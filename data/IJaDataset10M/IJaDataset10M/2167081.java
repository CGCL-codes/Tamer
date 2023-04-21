package com.weespers.model;

import com.weespers.util.TextUtil;

public class SearchResult implements Playable {

    protected String title;

    protected String id;

    protected int duration;

    public String getFormattedDuration() {
        return TextUtil.formatDuration(duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
