package org.paquitosoft.namtia.view.actions;

import java.awt.Cursor;
import java.lang.Thread.State;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import org.paquitosoft.namtia.view.ViewController;
import org.paquitosoft.namtia.view.facade.ViewFacade;
import org.paquitosoft.namtia.vo.SongVO;

/**
 *
 * @author paquitosoft
 */
public class FindSongLyrics extends Thread {

    private static FindSongLyrics instance;

    private SongVO song;

    /** Creates a new instance of FindSongLyrics */
    private FindSongLyrics(SongVO song) {
        this.song = song;
    }

    public static FindSongLyrics getInstance(SongVO song) {
        if (instance != null && instance.isAlive()) {
            instance = null;
        } else if (instance == null || !instance.isAlive()) {
            instance = new FindSongLyrics(song);
        }
        return instance;
    }

    public void run() {
        ViewController.getInstance().getLyricsWindow().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Collection results = new ViewFacade().findSongLyrics(this.song.getTitle(), (this.song.getArtist() != null) ? this.song.getArtist().getName() : null);
        if (results.size() == 1) {
            ViewController.getInstance().getLyricsWindow().getLyricsTA().setText(results.toArray()[0].toString());
        } else {
            DefaultListModel model = new DefaultListModel();
            for (Iterator it = results.iterator(); it.hasNext(); ) {
                model.addElement(it.next());
            }
            ViewController.getInstance().getLyricsWindow().getResultsList().setModel(model);
        }
        ViewController.getInstance().getLyricsWindow().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
}
