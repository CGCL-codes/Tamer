package com.thornapple.setmanager.action;

import ca.odell.glazedlists.EventList;
import com.thornapple.setmanager.Artist;
import com.thornapple.setmanager.Song;
import com.thornapple.setmanager.adapter.PersistenceService;
import com.thornapple.setmanager.adapter.SongSearchService;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;

/**
 *
 * @author Bill
 */
public class SaveSongAction extends AbstractAction {

    private EventList<Song> songs;

    private List<Song> newSongs;

    private PersistenceService repository;

    private SongSearchService searchService = new SongSearchService();

    /** Creates a new instance of SaveSongAction */
    public SaveSongAction(EventList songs, List<Song> newSongs) {
        this.newSongs = newSongs;
        this.songs = songs;
    }

    public void actionPerformed(ActionEvent e) {
        repository = PersistenceService.getInstance();
        if (newSongs == null) return;
        for (Song song : newSongs) {
            addSong(song);
        }
    }

    private void addSong(Song song) {
        try {
            if (song == null) {
                System.out.print("song is null");
                return;
            }
            boolean add = false;
            if (song.getId() == null || song.getId() < 1) add = true;
            Artist artist = song.getArtist();
            List<Artist> artists = repository.getArtistByName(artist.getName());
            if (artists != null && artists.size() > 0) {
                artist = artists.get(0);
                repository.addOrUpdateSong(artist.getId(), song);
            } else {
                artist = song.getArtist();
                artist.clearSongs();
                artist.addSong(song);
                repository.addOrUpdateArtist(artist);
            }
            if (song.getTablature() == null) searchService.getTab(song);
            if (add) songs.add(song);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
