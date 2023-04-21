package clubmixer.client.handler.playlistChange;

import com.slychief.clubmixer.server.library.entities.SongArray;
import java.util.EventObject;

/**
 *
 * @author Alexander Schindler
 */
public class PlaylistChangeEvent extends EventObject {

    private SongArray nextSongs;

    public PlaylistChangeEvent(Object _source, SongArray _songList) {
        super(_source);
        this.nextSongs = _songList;
    }

    /**
     * @return the nextSongs
     */
    public SongArray getNextSongs() {
        return nextSongs;
    }
}
