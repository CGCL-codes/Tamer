package net.deytan.tagger.album;

import java.util.ArrayList;
import java.util.List;
import net.deytan.tagger.SongUtils;
import net.deytan.tagger.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Class which represents a song.
 * 
 * @author deytan
 */
@Component("song")
@Scope("prototype")
public class Song implements Comparable<Song> {

    private String fileName;

    private String extension;

    private String album;

    private String albumIndex;

    private final List<String> artists;

    private String comment;

    private String genre;

    private String title;

    private String track;

    private String year;

    private long duration;

    public Song() {
        this.artists = new ArrayList<String>();
    }

    public String getArtist() {
        return org.apache.commons.lang.StringUtils.join(this.getArtists(), " & ");
    }

    @Override
    public int compareTo(final Song song) {
        return this.track.compareTo(song.getTrack());
    }

    @Override
    public String toString() {
        return StringUtils.build("Song[fileName=", this.fileName, ",album=", this.album, ",artist=", StringUtils.build(this.artists), ",comment=", this.comment, ",genre=", this.genre, ",title=", this.title, ",track=", this.track, ",year=", this.year, ",duration=", this.duration, "]");
    }

    /**
	 * Set duration of the form "X:XX".
	 * 
	 * @param duration
	 */
    public void setDuration(final String duration) {
        this.setDuration(SongUtils.parseDuration(duration));
    }

    /**
	 * Set track number.
	 * 
	 * @param track
	 */
    public void setTrack(final String track) {
        if (track.contains(".")) {
            final String[] tmp = track.split(".");
            this.setAlbumIndex(tmp[0]);
            this.setTrack(Long.parseLong(tmp[1]));
        } else {
            this.setTrack(Long.parseLong(track));
        }
    }

    /**
	 * Set track number and add extra 0 if necessary.
	 * 
	 * @param track
	 */
    public void setTrack(final long track) {
        this.track = SongUtils.formatTrackNumber(track);
    }

    /**
	 * Set track number and add extra 0 if necessary. Track number should start at 0.
	 * 
	 * @param track
	 */
    public void setTrack0(final long track) {
        this.setTrack(track + 1);
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setAlbum(final String album) {
        this.album = album;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public String getGenre() {
        return this.genre;
    }

    public void setGenre(final String genre) {
        this.genre = genre;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getTrack() {
        return this.track;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(final String year) {
        this.year = year;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(final long duration) {
        this.duration = duration;
    }

    public List<String> getArtists() {
        return this.artists;
    }

    public void addArtist(final String artist) {
        this.artists.add(artist);
    }

    public void setArtist(final String artist) {
        this.artists.clear();
        this.artists.add(artist);
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }

    public String getAlbumIndex() {
        return this.albumIndex;
    }

    public void setAlbumIndex(final String albumIndex) {
        this.albumIndex = albumIndex;
    }
}
