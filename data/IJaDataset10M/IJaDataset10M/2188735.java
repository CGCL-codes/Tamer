package com.life.audiotageditor.tables;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import com.life.audiotageditor.audio.AudioTag;
import com.life.audiotageditor.model.AudioFile;
import com.life.audiotageditor.model.AudioFileInfo;

public class AudioTagSorter extends ViewerSorter {

    private static final int TITLE = 1;

    private static final int ARTIST = 2;

    private static final int ALBUM = 3;

    private static final int YEAR = 4;

    private static final int TRACK = 5;

    private static final int GENRE = 6;

    private static final int COMMENT = 7;

    private static final int ALBUM_ARTIST = 8;

    public static final AudioTagSorter TITLE_ASC = new AudioTagSorter(TITLE);

    public static final AudioTagSorter TITLE_DESC = new AudioTagSorter(-TITLE);

    public static final AudioTagSorter ARTIST_ASC = new AudioTagSorter(ARTIST);

    public static final AudioTagSorter ARTIST_DESC = new AudioTagSorter(-ARTIST);

    public static final AudioTagSorter ALBUM_ASC = new AudioTagSorter(ALBUM);

    public static final AudioTagSorter ALBUM_DESC = new AudioTagSorter(-ALBUM);

    public static final AudioTagSorter YEAR_ASC = new AudioTagSorter(YEAR);

    public static final AudioTagSorter YEAR_DESC = new AudioTagSorter(-YEAR);

    public static final AudioTagSorter TRACK_ASC = new AudioTagSorter(TRACK);

    public static final AudioTagSorter TRACK_DESC = new AudioTagSorter(-TRACK);

    public static final AudioTagSorter GENRE_ASC = new AudioTagSorter(GENRE);

    public static final AudioTagSorter GENRE_DESC = new AudioTagSorter(-GENRE);

    public static final AudioTagSorter COMMENT_ASC = new AudioTagSorter(COMMENT);

    public static final AudioTagSorter COMMENT_DESC = new AudioTagSorter(-COMMENT);

    public static final AudioTagSorter ALBUM_ARTIST_ASC = new AudioTagSorter(ALBUM_ARTIST);

    public static final AudioTagSorter ALBUM_ARTIST_DESC = new AudioTagSorter(-ALBUM_ARTIST);

    private int sortType;

    private AudioTagSorter(int sortType) {
        this.sortType = sortType;
    }

    public int compare(Viewer viewer, Object e1, Object e2) {
        if (!(e1 instanceof AudioFile) || !(e2 instanceof AudioFile)) {
            return 0;
        }
        AudioTag a1 = ((AudioFileInfo) ((AudioFile) e1).getAudioModelInfo()).getAudioTag();
        AudioTag a2 = ((AudioFileInfo) ((AudioFile) e2).getAudioModelInfo()).getAudioTag();
        switch(sortType) {
            case TITLE:
                {
                    return a1.getTitle().compareTo(a2.getTitle());
                }
            case -TITLE:
                {
                    return a2.getTitle().compareTo(a1.getTitle());
                }
            case ARTIST:
                {
                    return a1.getArtist().compareTo(a2.getArtist());
                }
            case -ARTIST:
                {
                    return a2.getArtist().compareTo(a1.getArtist());
                }
            case ALBUM:
                {
                    return a1.getAlbum().compareTo(a2.getAlbum());
                }
            case -ALBUM:
                {
                    return a2.getAlbum().compareTo(a1.getAlbum());
                }
            case YEAR:
                {
                    return a1.getYear().compareTo(a2.getYear());
                }
            case -YEAR:
                {
                    return a2.getYear().compareTo(a1.getYear());
                }
            case TRACK:
                {
                    return a1.getTrack().compareTo(a2.getTrack());
                }
            case -TRACK:
                {
                    return a2.getTrack().compareTo(a1.getTrack());
                }
            case COMMENT:
                {
                    return a1.getComment().compareTo(a2.getComment());
                }
            case -COMMENT:
                {
                    return a2.getComment().compareTo(a1.getComment());
                }
            case ALBUM_ARTIST:
                {
                    return a1.getAlbumArtist().compareTo(a2.getAlbumArtist());
                }
            case -ALBUM_ARTIST:
                {
                    return a2.getAlbumArtist().compareTo(a1.getAlbumArtist());
                }
        }
        return 0;
    }
}
