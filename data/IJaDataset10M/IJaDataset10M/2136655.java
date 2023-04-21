package de.linwave.music;

public class Track {

    long OID;

    String title;

    int duration;

    Genre genre;

    Artist artist;

    public Track() {
    }

    ;

    public Track(String title, int duration, Genre genre) {
        this(title, duration, genre, null);
    }

    public Track(String title, int duration, Genre genre, Artist artist) {
        this.title = title;
        this.duration = duration;
        this.genre = genre;
        this.artist = artist;
    }

    public long getOID() {
        return OID;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" [Track] OID=").append(OID);
        sb.append(", title=").append(title);
        sb.append(", duration=").append(duration);
        return sb.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
