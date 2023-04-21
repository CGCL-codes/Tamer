package Control;

import MediaPlayer.Song;
import java.util.ArrayList;

/**
 *
 * @author brelandmiley
 */
public class PlaylistManager {

    private ArrayList<Song> pls;

    public PlaylistManager() {
        pls = new ArrayList<Song>();
    }

    public void addSong(Song toAdd) {
        pls.add(toAdd);
    }

    public void addList(ArrayList<Song> toAdd) {
        pls.addAll(toAdd);
    }

    public ArrayList<Song> getCP() {
        return this.pls;
    }
}
