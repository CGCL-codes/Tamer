package com.cidero.upnp;

/**
 *  Lightweight playlist item class for use with simple file-based playlists
 *  (M3U,PLS).
 */
public class PlaylistItem {

    String artist;

    String title;

    String durationSecs;

    String resource;

    public PlaylistItem() {
    }

    public PlaylistItem(String title, String resource) {
        this(null, title, null, resource);
    }

    public PlaylistItem(String artist, String title, String durationSecs, String resource) {
        this.artist = artist;
        this.title = title;
        this.durationSecs = durationSecs;
        this.resource = resource;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDurationSecs(String durationSecs) {
        this.durationSecs = durationSecs;
    }

    public String getDurationSecs() {
        return durationSecs;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getResource() {
        return resource;
    }
}
