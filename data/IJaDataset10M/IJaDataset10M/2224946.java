package com.thornapple.setmanager.action;

import ca.odell.glazedlists.EventList;
import com.thornapple.setmanager.SongSet;
import com.thornapple.setmanager.adapter.PersistenceService;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;

/**
 *
 * @author Bill
 */
public class SaveSongSetAction extends AbstractAction {

    private EventList songSets;

    private SongSet set;

    private List songsToAdd;

    private PersistenceService repository;

    /** Creates a new instance of SaveSongSetAction */
    public SaveSongSetAction(EventList songSets, SongSet set, List songsToAdd) {
        super();
        this.songSets = songSets;
        this.set = set;
        this.songsToAdd = songsToAdd;
    }

    public void actionPerformed(ActionEvent e) {
        repository = PersistenceService.getInstance();
        if (songsToAdd == null) return;
        set.addSongs(songsToAdd);
        try {
            boolean newSet = set.getId() == null;
            repository.addOrUpdateSongSet(set);
            if (newSet) songSets.add(set);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
