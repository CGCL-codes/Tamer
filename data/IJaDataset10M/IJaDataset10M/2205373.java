package com.serie402.common.bo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import com.kiss.fw.bo.AbstractBO;

public class PhotoAlbum extends AbstractBO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int albumId;

    private String description;

    private String albumName;

    private Calendar albumDate;

    private Photo thumbnail;

    private List<Photo> photos;

    public PhotoAlbum() {
    }

    public PhotoAlbum(final List<Photo> _photos) {
        photos = _photos;
    }

    public String getDescription() {
        return description;
    }

    public int getAlbumId() {
        return albumId;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getAlbumName() {
        return albumName;
    }

    public Calendar getAlbumDate() {
        return albumDate;
    }

    public Photo getThumbnail() {
        return thumbnail;
    }

    public void setAlbumName(String _albumName) {
        albumName = _albumName;
    }

    public void setDescription(final String _description) {
        description = _description;
    }

    public void setAlbumId(final int _albumId) {
        albumId = _albumId;
    }

    public void setPhotos(final List<Photo> _photos) {
        photos = _photos;
    }

    public void setThumbnail(final Photo _thumbnail) {
        thumbnail = _thumbnail;
    }

    public void setAlbumDate(final Calendar _albumDate) {
        albumDate = _albumDate;
    }

    @Override
    public void reset() {
    }
}
