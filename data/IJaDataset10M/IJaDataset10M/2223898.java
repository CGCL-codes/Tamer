package org.bff.slimserver.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bff.slimserver.SlimDatabase;
import org.bff.slimserver.SlimPlayer;
import org.bff.slimserver.SlimPlaylist;
import org.bff.slimserver.SlimSavedPlaylist;
import org.bff.slimserver.events.DatabaseScanEvent;
import org.bff.slimserver.events.PlayerChangeEvent;
import org.bff.slimserver.events.PlayerChangeListener;
import org.bff.slimserver.events.PlaylistChangeEvent;
import org.bff.slimserver.events.PlaylistChangeListener;
import org.bff.slimserver.events.SlimErrorEvent;
import org.bff.slimserver.events.SlimErrorListener;
import org.bff.slimserver.events.TrackPositionChangeEvent;
import org.bff.slimserver.events.VolumeChangeEvent;
import org.bff.slimserver.events.VolumeChangeListener;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.musicobjects.SlimObject;
import org.bff.slimserver.musicobjects.SlimPlaylistSong;

/**
 * SlimStandAloneMonitor monitors a SlimServer connection by querying the status and
 * statistics of the SlimServer server at given delay intervals.  As statistics change
 * appropriate events are fired indicating these changes.  If more detailed
 * events are desired attach listeners to the different slim objects.
 *
 * This class doesn't know any details about what caused events to occur so null
 * will be passed for specific event details such as a song being added to the
 * playlist.
 * 
 * @author Bill Findeisen
 * @version 1.0
 */
public class SlimStandAloneMonitor extends SlimEventMonitor implements Runnable {

    private SlimPlayer player;

    private SlimPlaylist playlist;

    private SlimDatabase database;

    private final int delay;

    private int oldPos;

    private int oldVolume;

    private int oldPlaylistLength;

    private List<SlimPlaylistSong> oldPlaylist;

    private List<SlimSavedPlaylist> oldSavedPlaylists;

    private int oldSongId;

    private boolean stopped;

    private int oldNumberPlayers;

    /**
     * Returns the {@link SlimPlayer} for the monitor
     * @return the {@link SlimPlayer}
     */
    public SlimPlayer getPlayer() {
        return player;
    }

    /**
     * Sets the {@link SlimPlayer} for the monitorr
     * @param player the {@link SlimPlayer}
     * @throws org.bff.slimserver.exception.SlimException if there as a connection
     * problem
     */
    public void setPlayer(SlimPlayer player) throws SlimException {
        this.player = player;
        initialize(player);
    }

    /**
     * Returns the {@link SlimPlaylist} for the monitor
     * @return the {@link SlimPlaylist}
     */
    public SlimPlaylist getPlaylist() {
        return playlist;
    }

    /**
     * Sets the {@link SlimPlaylist} for the monitor
     * @param playlist the {@link SlimPlaylist}
     */
    public void setPlaylist(SlimPlaylist playlist) {
        this.playlist = playlist;
    }

    private SlimPlayer.PlayerStatus oldStatus = SlimPlayer.PlayerStatus.STATUS_STOPPED;

    private static final int DEFAULT_DELAY = 1000;

    private List<PlayerChangeListener> playerListeners = new ArrayList<PlayerChangeListener>();

    private List<PlaylistChangeListener> playlistListeners = new ArrayList<PlaylistChangeListener>();

    private List<VolumeChangeListener> volListeners = new ArrayList<VolumeChangeListener>();

    private List<SlimErrorListener> errorListeners = new ArrayList<SlimErrorListener>();

    /**
     * Creates a new instance of SlimStandAloneMonitor using the default delay
     * of 1 second.
     * 
     * @param player the slim player to monitor
     * @throws SlimException if a connection can't be made witht the player
     */
    public SlimStandAloneMonitor(SlimPlayer player) throws SlimException {
        this(player, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of SlimStandAloneMonitor using the given delay interval
     * for queries.
     * 
     * @param player the SlimPlayer to monitor
     * @param delay the delay interval
     * @throws SlimException if there is a problem initializing the player
     */
    public SlimStandAloneMonitor(SlimPlayer player, int delay) throws SlimException {
        super();
        this.delay = delay;
        initialize(player);
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.add(pcl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removePlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlayerChangeEvent} to all registered
     * {@link PlayerChangeListener}s.
     * @param id the event id to send
     */
    protected synchronized void firePlayerChangeEvent(int id) {
        PlayerChangeEvent pce = new PlayerChangeEvent(this, id);
        for (PlayerChangeListener pcl : playerListeners) {
            pcl.playerChanged(pce);
        }
    }

    /**
     * Adds a {@link VolumeChangeListener} to this object to receive
     * {@link VolumeChangeEvent}s.
     * @param vcl the VolumeChangeListener to add
     */
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    /**
     * Removes a {@link VolumeChangeListener} from this object.
     * @param vcl the VolumeChangeListener to remove
     */
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        VolumeChangeEvent vce = new VolumeChangeEvent(this, volume);
        for (VolumeChangeListener vcl : volListeners) {
            vcl.volumeChanged(vce);
        }
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addPlaylistChangeListener(PlaylistChangeListener pcl) {
        playlistListeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     * @param pcl the PlaylistChangeListener to remove
     */
    public synchronized void removePlaylistChangeListener(PlaylistChangeListener pcl) {
        playlistListeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimObject object) {
        firePlaylistChangeEvent(id, object, 0);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimObject object, int songCount) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id, object, null, null, null);
        pce.setSongCount(songCount);
        for (PlaylistChangeListener pcl : playlistListeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     * @param id the event id to send
     * @param playlist the list of {@link SlimPlaylistSong}s
     */
    protected synchronized void firePlaylistChangeEvent(int id, SlimObject object, List<SlimPlaylistSong> playlist) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id, object, null, playlist, null);
        pce.setSongCount(playlist.size());
        for (PlaylistChangeListener pcl : playlistListeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Adds a {@link SlimErrorListener} to this object to receive
     * {@link SlimErrorEvent}s.
     * @param el the SlimErrorListener to add
     */
    public synchronized void addSlimErrorListener(SlimErrorListener el) {
        errorListeners.add(el);
    }

    /**
     * Removes a {@link SlimErrorListener} from this object.
     * @param el the SlimErrorListener to remove
     */
    public synchronized void removeSlimErrorListener(SlimErrorListener el) {
        errorListeners.remove(el);
    }

    /**
     * Sends the appropriate {@link SlimErrorListener} to all registered
     * {@link SlimErrorListener}s.
     * @param msg the event message
     */
    protected synchronized void fireSlimErrorEvent(String msg) {
        SlimErrorEvent ee = new SlimErrorEvent(this, msg);
        for (SlimErrorListener el : errorListeners) {
            el.errorEventReceived(ee);
        }
    }

    /**
     * Implements the Runnable run method to monitor the SlimServer connection.
     */
    @Override
    public void run() {
        while (!isStopped()) {
            try {
                checkPlayer();
                checkPlaylist();
                checkTrackPosition();
                checkVolume();
                checkSavedPlaylist();
                checkDatabaseScan();
                checkRepeating();
                checkNumberPlayers();
            } catch (Exception e) {
                if (e instanceof SlimConnectionException) {
                    setConnectedState(false);
                    fireConnectionChangeEvent(false);
                    boolean retry = true;
                    while (retry) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(SlimStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        checkConnection();
                        if (isConnectedState()) {
                            retry = false;
                        }
                    }
                } else {
                    fireSlimErrorEvent(e.getMessage());
                }
                e.printStackTrace();
            }
            try {
                synchronized (this) {
                    this.wait(delay);
                }
            } catch (InterruptedException ie) {
            }
        }
    }

    /**
     * Starts the monitor by creating and starting a thread using this instance
     * as the Runnable interface.
     */
    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        setStopped(true);
    }

    /**
     * Returns true if the monitor is stopped, false if the monitor is still running.
     * @return true if monitor is running, false otherwise false
     */
    public boolean isStopped() {
        return stopped;
    }

    /**
     * Returns the current status of the player.
     * @return the status of the player
     */
    public SlimPlayer.PlayerStatus getStatus() {
        return oldStatus;
    }

    /**
     * Initializes the monitor for a {@link SlimPlayer}.  Call this method if 
     * the player changes.
     * @param player the {@link SlimPlayer}
     * @throws org.bff.slimserver.exception.SlimException if there is a connection
     * problem
     */
    public synchronized void initialize(SlimPlayer player) throws SlimException {
        System.out.println("Setting player to " + player.getId());
        super.setSlimServer(player.getSlimServer());
        this.player = player;
        this.database = new SlimDatabase(player.getSlimServer());
        this.playlist = new SlimPlaylist(player);
        oldPlaylist = new ArrayList<SlimPlaylistSong>(getPlaylist().getSongIds());
        oldSavedPlaylists = new ArrayList<SlimSavedPlaylist>(getDatabase().getPlaylists());
        oldPlaylistLength = oldPlaylist.size();
        oldPos = getPlayer().getElapsedTime();
        oldSongId = getPlaylist().getCurrentSongIndex();
        oldVolume = getPlayer().getVolume();
        oldNumberPlayers = getSlimServer().getSlimPlayers().size();
        List<SlimPlaylistSong> list = new ArrayList<SlimPlaylistSong>(getPlaylist().getSongIds());
        firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_CHANGED, null, list);
        firePlaylistChangeEvent(PlaylistChangeEvent.SONG_CHANGED, null, list);
    }

    private void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private int checkNumberPlayerCount;

    private void checkNumberPlayers() throws SlimException {
        if (checkNumberPlayerCount == 10) {
            checkNumberPlayerCount = 0;
            int numPlayers = getSlimServer().getSlimPlayers().size();
            if (oldNumberPlayers < numPlayers) {
                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_ADDED);
            } else if (oldNumberPlayers > numPlayers) {
                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_DELETED);
            }
            oldNumberPlayers = numPlayers;
        } else {
            ++checkNumberPlayerCount;
        }
    }

    private int checkPlayerCount;

    private void checkPlayer() throws SlimException {
        if (checkPlayerCount == 2) {
            checkPlayerCount = 0;
            SlimPlayer.PlayerStatus newStatus = SlimPlayer.PlayerStatus.STATUS_STOPPED;
            SlimPlayer.PlayerStatus mode = getPlayer().getMode();
            switch(mode) {
                case STATUS_PLAYING:
                    newStatus = SlimPlayer.PlayerStatus.STATUS_PLAYING;
                    break;
                case STATUS_PAUSED:
                    newStatus = SlimPlayer.PlayerStatus.STATUS_PAUSED;
                    break;
            }
            if (!oldStatus.equals(newStatus)) {
                switch(newStatus) {
                    case STATUS_PLAYING:
                        switch(oldStatus) {
                            case STATUS_PAUSED:
                                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_UNPAUSED);
                                break;
                            case STATUS_STOPPED:
                                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_STARTED);
                                break;
                        }
                        break;
                    case STATUS_STOPPED:
                        firePlayerChangeEvent(PlayerChangeEvent.PLAYER_STOPPED);
                        if (getPlaylist().getCurrentSongIndex() == -1) {
                            firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_ENDED, null);
                        }
                        break;
                    case STATUS_PAUSED:
                        switch(oldStatus) {
                            case STATUS_PAUSED:
                                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_UNPAUSED);
                                break;
                            case STATUS_PLAYING:
                                firePlayerChangeEvent(PlayerChangeEvent.PLAYER_PAUSED);
                                break;
                        }
                }
                oldStatus = newStatus;
            }
        } else {
            ++checkPlayerCount;
        }
    }

    private int checkPlaylistSongs;

    private void checkPlaylist() throws SlimException {
        while (getPlaylist().isUpdating()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(SlimStandAloneMonitor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        int newPlaylistLength = getPlaylist().getSongCount();
        if (oldPlaylistLength != newPlaylistLength) {
            try {
                if (oldPlaylistLength < newPlaylistLength) {
                    firePlaylistChangeEvent(PlaylistChangeEvent.SONG_ADDED, null, newPlaylistLength);
                } else if (oldPlaylistLength > newPlaylistLength) {
                    firePlaylistChangeEvent(PlaylistChangeEvent.SONG_DELETED, null, newPlaylistLength);
                }
            } finally {
                oldPlaylistLength = newPlaylistLength;
                oldPlaylist = new ArrayList<SlimPlaylistSong>(getPlaylist().getSongIds());
            }
        } else {
            if (checkPlaylistSongs == 10) {
                checkPlaylistSongs = 0;
                List<SlimPlaylistSong> newPlaylist = new ArrayList<SlimPlaylistSong>(getPlaylist().getSongIds());
                for (int i = 0; i < oldPlaylist.size(); i++) {
                    if (!oldPlaylist.get(i).equals(newPlaylist.get(i))) {
                        oldPlaylist = newPlaylist;
                        firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_CHANGED, null, newPlaylist);
                        break;
                    }
                }
            }
            ++checkPlaylistSongs;
        }
        int newSongId = getPlaylist().getCurrentSongIndex();
        if (oldStatus == SlimPlayer.PlayerStatus.STATUS_PLAYING) {
            if (oldSongId != newSongId) {
                firePlaylistChangeEvent(PlaylistChangeEvent.SONG_CHANGED, null, newPlaylistLength);
                oldSongId = newSongId;
            }
        }
    }

    /**
     * Checking this is expensive so only do it every 30 times
     */
    private int checkSavedCount;

    private void checkSavedPlaylist() {
        if (checkSavedCount == 30) {
            checkSavedCount = 0;
            try {
                List<SlimSavedPlaylist> newPlaylist = new ArrayList<SlimSavedPlaylist>(getDatabase().getPlaylists());
                if (newPlaylist.size() != oldSavedPlaylists.size()) {
                    firePlaylistChangeEvent(PlaylistChangeEvent.SAVED_PLAYLIST_CHANGED, null, newPlaylist.size());
                    oldSavedPlaylists = newPlaylist;
                    return;
                }
            } catch (SlimConnectionException sce) {
                sce.printStackTrace();
            }
        } else {
            ++checkSavedCount;
        }
    }

    private void checkVolume() throws SlimConnectionException {
        int newVolume = getPlayer().getVolume();
        if (oldVolume != newVolume) {
            fireVolumeChangeEvent(newVolume);
            oldVolume = newVolume;
        }
    }

    private int checkRepeatCount;

    private int repeating = 0;

    protected void checkRepeating() throws SlimConnectionException {
        if (++checkRepeatCount == 3) {
            checkRepeatCount = 0;
            int rep = getPlaylist().getRepeatingStatus();
            if (repeating != rep) {
                switch(rep) {
                    case 0:
                        firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_OFF, null);
                        break;
                    case 1:
                        firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_SONG, null);
                        break;
                    case 2:
                        firePlaylistChangeEvent(PlaylistChangeEvent.REPEAT_PLAYLIST, null);
                        break;
                }
            }
            repeating = rep;
        }
    }

    private int checkDatabaseCount;

    private boolean databaseScanning;

    protected void checkDatabaseScan() {
        if (++checkDatabaseCount == 5) {
            checkDatabaseCount = 0;
            boolean rescanning = getDatabase().isRescanning();
            if (databaseScanning != rescanning) {
                System.out.println("firing event!");
                fireDatabaseScanEvent(rescanning ? DatabaseScanEvent.SCAN_STARTED : DatabaseScanEvent.SCAN_ENDED);
            }
            databaseScanning = rescanning;
        }
    }

    /**
     * Checks the track position and fires a {@link TrackPositionChangeEvent} if
     * there has been a change in track position.
     */
    protected final void checkTrackPosition() throws SlimConnectionException {
        int newPos = getPlayer().getElapsedTime();
        if (oldPos != newPos) {
            oldPos = newPos;
            fireTrackPositionChangeEvent(newPos);
        }
    }

    public SlimDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SlimDatabase database) {
        this.database = database;
    }
}
