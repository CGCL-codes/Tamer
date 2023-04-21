package com.thornapple.setmanager.action;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import com.thornapple.setmanager.AddSongDialog;
import com.thornapple.setmanager.Artist;
import com.thornapple.setmanager.Song;
import com.thornapple.setmanager.adapter.PersistenceService;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author Bill
 */
public class ImportSongAction extends AbstractAction {

    public static int ADD_URL = 0;

    public static int ADD_FILE = 1;

    public static int ADD = 2;

    private int mode = ADD;

    private static final String NL = "\n";

    private InputStream is;

    private PersistenceService repository;

    private AddSongDialog addDialog;

    private EventList songs;

    /** Creates a new instance of AddSongAction */
    public ImportSongAction(EventList songs, int mode) {
        this.songs = songs;
        this.mode = mode;
    }

    public void actionPerformed(ActionEvent e) {
        if (mode == ADD_URL) {
            String url = JOptionPane.showInputDialog(null, "Enter URL", "Enter URL", JOptionPane.OK_CANCEL_OPTION);
            if (url == null) return;
            try {
                is = new URL(url).openStream();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (mode == ADD_FILE) {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.showDialog(null, "Add tab");
            File file = chooser.getSelectedFile();
            if (file == null) return;
            try {
                is = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        if (repository == null) repository = PersistenceService.getInstance();
        List artists = repository.getAllArtists();
        EventList artistList = new BasicEventList();
        artistList.addAll(artists);
        addDialog = new AddSongDialog(artistList, JOptionPane.getRootFrame(), true);
        Song s = addDialog.getSong();
        if (is != null) {
            String tab;
            try {
                tab = readTab(is);
                s.setTablature(tab);
                addDialog.setSong(s);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        addDialog.setVisible(true);
        addDialog.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent e) {
                int ok = addDialog.getReturnStatus();
                if (ok == AddSongDialog.RET_CANCEL) return;
                addSong();
            }
        });
    }

    private void addSong() {
        Song s = addDialog.getSong();
        Artist artist = s.getArtist();
        try {
            if (repository.artistExists(artist)) {
                System.out.println("exitsts as:" + artist.getId());
                repository.addOrUpdateSong(artist.getId(), s);
                songs.add(s);
            } else {
                artist.addSong(s);
                System.out.println("adding to new:" + artist.getName());
                repository.addOrUpdateArtist(artist);
                songs.add(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String readTab(InputStream is) throws Exception {
        StringBuffer buffer = new StringBuffer();
        String line = null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
            buffer.append(NL);
        }
        return buffer.toString();
    }
}
