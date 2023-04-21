package org.quelea.windows.main.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.quelea.Application;
import org.quelea.windows.newsong.SongEntryWindow;

/**
 * Called when the current song in the library should be edited.
 * @author Michael
 */
public class EditSongDBActionListener implements ActionListener {

    /**
     * Edit the currently selected song in the library.
     * @param e the action event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        SongEntryWindow songEntryWindow = Application.get().getMainWindow().getSongEntryWindow();
        songEntryWindow.setLocationRelativeTo(songEntryWindow.getOwner());
        songEntryWindow.resetEditSong(Application.get().getMainWindow().getMainPanel().getLibraryPanel().getLibrarySongPanel().getSongList().getSelectedValue());
        songEntryWindow.setVisible(true);
    }
}
