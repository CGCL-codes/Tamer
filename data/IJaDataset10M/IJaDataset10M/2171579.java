package org.jdesktop.jdic.mpcontrol.itunes;

import org.jdesktop.jdic.mpcontrol.IExtendedSongInfo;

/**
 * @author Zsombor
 *
 */
class ITunesSongInfo implements IExtendedSongInfo {

    private String album;

    private String artist;

    private String title;

    private String path;

    private int trackNumber;

    public ITunesSongInfo(String album, String artist, String title, int number, String path) {
        this.album = album;
        this.artist = artist;
        this.path = path;
        this.title = title;
        trackNumber = number;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getSongTitle() {
        return artist + " - " + album + " - " + title;
    }

    public String getPath() {
        return path;
    }

    public int getTrackNumber() {
        return trackNumber;
    }
}
