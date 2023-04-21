package db;

import java.util.List;

public class Album {

    private long albumId;

    private String albumName;

    private User user;

    private Album parentAlbum;

    private List<Album> childAlbums;

    private List<Picture> pictures;

    public Album() {
    }

    public Album(String albumName, User user, Album parentAlbum, List<Album> childAlbums, List<Picture> pictures) {
        this.albumName = albumName;
        this.user = user;
        this.parentAlbum = parentAlbum;
        this.childAlbums = childAlbums;
        this.pictures = pictures;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public List<Album> getChildAlbums() {
        return childAlbums;
    }

    public void setChildAlbums(List<Album> childAlbums) {
        this.childAlbums = childAlbums;
    }

    public Album getParentAlbum() {
        return parentAlbum;
    }

    public void setParentAlbum(Album parentAlbum) {
        this.parentAlbum = parentAlbum;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
