package model;

import java.util.Iterator;
import java.util.List;

public class Disk {

    private String id;

    private String title;

    private String artist;

    private String genre;

    private String year;

    private String totalTime;

    private String price;

    private int revision;

    private List<Track> tracks;

    private String coverImage;

    public Disk() {
    }

    public Disk(String id, String title, String artist, String genre, String year, String totalTime, String price, String coverImage, List<Track> tracks) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.genre = genre;
        this.year = year;
        this.totalTime = totalTime;
        this.price = price;
        this.coverImage = coverImage;
        this.tracks = tracks;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setId(String id) {
        this.id = id;
    }

    public synchronized String getTitle() {
        return title;
    }

    public synchronized void setTitle(String title) {
        this.title = title;
    }

    public synchronized String getArtist() {
        return artist;
    }

    public synchronized void setArtist(String artist) {
        this.artist = artist;
    }

    public synchronized String getGenre() {
        return genre;
    }

    public synchronized void setGenre(String genre) {
        this.genre = genre;
    }

    public synchronized String getYear() {
        return year;
    }

    public synchronized void setYear(String year) {
        this.year = year;
    }

    public synchronized String getTotalTime() {
        return totalTime;
    }

    public synchronized void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public synchronized String getPrice() {
        return price;
    }

    public synchronized void setPrice(String price) {
        this.price = price;
    }

    public synchronized List<Track> getTracks() {
        return tracks;
    }

    public synchronized void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }

    public synchronized void insertToTrackList(Track track) {
        this.tracks.add(track);
    }

    public synchronized String getCoverImage() {
        return coverImage;
    }

    public synchronized void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public synchronized int getRevision() {
        return revision;
    }

    public synchronized void setRevision(int revision) {
        this.revision = revision;
    }

    public synchronized String toString() {
        return toString(false);
    }

    public synchronized String toString(boolean includeTracks) {
        String tracksTitles = "";
        if (includeTracks) {
            tracksTitles += "\nTracks:\n";
            for (Iterator<Track> iterator = tracks.iterator(); iterator.hasNext(); ) {
                Track track = (Track) iterator.next();
                tracksTitles += track.getTitle() + "\n";
            }
        }
        return "Title: " + title + "\n" + "Artist: " + artist + "\n" + "Genre: " + genre + "\n" + "Year: " + year + "\n" + "Total Time: " + totalTime + "\n" + "Price: " + price + tracksTitles;
    }

    public synchronized String toStringShort() {
        return "Title: " + title + "\n" + "Artist: " + artist + "\n" + "Year: " + year + "\n" + "Price: " + price;
    }
}
