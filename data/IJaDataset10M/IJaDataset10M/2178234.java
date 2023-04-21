package com.serie402.common.bo;

import java.io.Serializable;
import java.util.Calendar;
import com.kiss.fw.bo.AbstractBO;

public class Event extends AbstractBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int category;

    private int eventId;

    private String eventName;

    private String description;

    private Calendar eventDate;

    private PhotoAlbum photoAlbum;

    public int getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public String getDescription() {
        return description;
    }

    public int getCategory() {
        return category;
    }

    public Calendar getEventDate() {
        return eventDate;
    }

    public PhotoAlbum getPhotoAlbum() {
        return photoAlbum;
    }

    public void setEventId(final int _eventId) {
        eventId = _eventId;
    }

    public void setEventName(final String _eventName) {
        eventName = _eventName;
    }

    public void setDescription(final String _description) {
        description = _description;
    }

    public void setCategory(final int _category) {
        category = _category;
    }

    public void setEventDate(final Calendar _eventDate) {
        eventDate = _eventDate;
    }

    public void setPhotoAlbum(final PhotoAlbum _photoAlbum) {
        photoAlbum = _photoAlbum;
    }

    @Override
    public void reset() {
    }
}
