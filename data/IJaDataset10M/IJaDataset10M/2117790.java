package MediaPlayer;

import java.util.ArrayList;

/**
 *
 * @author brelandmiley
 */
public class Playlist {

    private String name;

    private int id;

    private ArrayList<Song> songs;

    public Playlist() {
        this.name = "";
        this.id = -1;
        this.songs = new ArrayList<Song>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSong(Song toAdd) {
        if (toAdd != null) {
            this.songs.add(toAdd);
        }
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public void addSongs(ArrayList<Song> toAdd) {
        this.songs.addAll(toAdd);
    }
}
