package net.sf.ulmac.jtunes;

import com4j.IID;
import com4j.ReturnValue;
import com4j.VTID;

/**
 * IITPlaylist Interface
 */
@IID("{3D5E072F-2A77-4B17-9E73-E03B77CCCCA9}")
public interface IITPlaylist extends net.sf.ulmac.jtunes.IITObject {

    @VTID(30)
    void addTrack(net.sf.ulmac.jtunes.IITTrack track);

    /**
     * Delete this playlist.
     */
    @VTID(15)
    void delete();

    /**
     * The total length of all songs in the playlist (in seconds).
     */
    @VTID(21)
    int duration();

    /**
     * The playlist kind.
     */
    @VTID(19)
    net.sf.ulmac.jtunes.ITPlaylistKind kind();

    /**
     * Start playing the first track in this playlist.
     */
    @VTID(16)
    void playFirstTrack();

    /**
     * Print this playlist.
     */
    @VTID(17)
    void print(boolean showPrintDialog, net.sf.ulmac.jtunes.ITPlaylistPrintKind printKind, java.lang.String theme);

    /**
     * Search tracks in this playlist for the specified string.
     */
    @VTID(18)
    net.sf.ulmac.jtunes.IITTrackCollection search(java.lang.String searchText, net.sf.ulmac.jtunes.ITPlaylistSearchField searchFields);

    /**
     * True if songs in the playlist are played in random order.
     */
    @VTID(22)
    boolean shuffle();

    /**
     * True if songs in the playlist are played in random order.
     */
    @VTID(23)
    void shuffle(boolean isShuffle);

    /**
     * The total size of all songs in the playlist (in bytes).
     */
    @VTID(24)
    double size();

    /**
     * The playback repeat mode.
     */
    @VTID(25)
    net.sf.ulmac.jtunes.ITPlaylistRepeatMode songRepeat();

    /**
     * The playback repeat mode.
     */
    @VTID(26)
    void songRepeat(net.sf.ulmac.jtunes.ITPlaylistRepeatMode repeatMode);

    /**
     * The source that contains this playlist.
     */
    @VTID(20)
    net.sf.ulmac.jtunes.IITSource source();

    /**
     * The total length of all songs in the playlist (in MM:SS format).
     */
    @VTID(27)
    java.lang.String time();

    /**
     * Returns a collection of tracks in this playlist.
     */
    @VTID(29)
    net.sf.ulmac.jtunes.IITTrackCollection tracks();

    @VTID(29)
    @ReturnValue(defaultPropertyThrough = { net.sf.ulmac.jtunes.IITTrackCollection.class })
    net.sf.ulmac.jtunes.IITTrack tracks(int index);

    /**
     * True if the playlist is visible in the Source list.
     */
    @VTID(28)
    boolean visible();
}
