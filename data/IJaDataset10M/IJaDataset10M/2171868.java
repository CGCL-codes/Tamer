package org.auramp.ui.playlist;

import java.io.File;
import org.auramp.mplayer.Track;
import org.auramp.util.OSToolkit;
import org.auramp.util.StringTools;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.Tag;
import com.trolltech.qt.gui.QStandardItem;

public class PlaylistItem implements Comparable<PlaylistItem> {

    private Track track;

    private QStandardItem stdItm;

    private int gatheredState = 0;

    public PlaylistItem(Track track) {
        this.track = track;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

    public String getCommon(Playlist.HeaderType type) {
        if (type == Playlist.HeaderType.ALBUM) {
            return getAlbum();
        } else if (type == Playlist.HeaderType.ARTIST) {
            return getArtist();
        } else if (type == Playlist.HeaderType.COMMENT) {
            return getComment();
        } else if (type == Playlist.HeaderType.COMPOSER) {
            return getComposer();
        } else if (type == Playlist.HeaderType.GENRE) {
            return getGenre();
        } else if (type == Playlist.HeaderType.LENGTH) {
            return getLength();
        } else if (type == Playlist.HeaderType.NAME) {
            return getTitle();
        } else if (type == Playlist.HeaderType.PRODUCER) {
            return getProducer();
        } else if (type == Playlist.HeaderType.TITLE) {
            return getTitle();
        } else if (type == Playlist.HeaderType.TRACK) {
            return getTrackNumber();
        } else if (type == Playlist.HeaderType.YEAR) {
            return getYear();
        } else {
            return "";
        }
    }

    public String getTitle() {
        if (track.getMetaInfo().getTitle() != null && !track.getMetaInfo().getTitle().equals("")) {
            return track.getMetaInfo().getTitle();
        } else {
            track.getMetaInfo().setMetaInfo("title", track.getURL().substring(track.getURL().lastIndexOf(File.separator) + 1));
            return track.getMetaInfo().getTitle();
        }
    }

    public String getArtist() {
        return track.getMetaInfo().getArtist();
    }

    public String getGenre() {
        return track.getMetaInfo().get("genre");
    }

    public String getAlbum() {
        return track.getMetaInfo().get("album");
    }

    public String getYear() {
        return track.getMetaInfo().get("year");
    }

    public String getTrackNumber() {
        return track.getMetaInfo().get("track");
    }

    public String getComposer() {
        return track.getMetaInfo().get("composer");
    }

    public String getComment() {
        return track.getMetaInfo().get("comment");
    }

    public String getProducer() {
        return track.getMetaInfo().get("producer");
    }

    public String getLength() {
        return track.getMetaInfo().get("length");
    }

    public void setQStandardItem(QStandardItem itm) {
        stdItm = itm;
    }

    public QStandardItem getQStandardItem() {
        return stdItm;
    }

    public int getGatheredState() {
        return gatheredState;
    }

    public void setGatheredState(int gathered) {
        this.gatheredState = gathered;
    }

    @Override
    public int compareTo(PlaylistItem o) {
        if (o == this) {
            return 0;
        }
        return -1;
    }
}
