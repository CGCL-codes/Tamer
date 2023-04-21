package client;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * @author Ryan Gannon
 * 
 */
public class Song {

    @Attribute
    private int no;

    @Element
    private String artist;

    @Element
    private String name;

    @Element
    private String album;

    @Element
    private int year;

    public Song() {
    }

    public Song(int no, String artist, String name, String album, int year) {
        this.no = no;
        this.artist = artist;
        this.name = name;
        this.album = album;
        this.year = year;
    }

    public int getNo() {
        return no;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public String getAlbum() {
        return album;
    }

    public int getYear() {
        return year;
    }
}
