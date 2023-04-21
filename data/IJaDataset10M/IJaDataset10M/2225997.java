package org.masterview.showcase.user.client;

import org.masterview.user.client.IsMasterviewBean;
import java.util.Date;

public class Album implements IsMasterviewBean {

    private String name;

    private String artist;

    private int year;

    private String genre;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Album() {
    }

    public Album(String name, String artist, int year, String genre) {
        this.name = name;
        this.artist = artist;
        this.year = year;
        this.genre = genre;
    }
}
