package jmplayer.ui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import jmplayer.playlist.PlaylistFile;
import jmplayer.playlist.PlaylistItemArray;
import jmplayer.ui.PlaylistFrame;
import jmplayer.ui.filefilter.PlaylistFilesFilter;

/**
 * 
 * @author lois
 * @version $Revision: 1.4 $
 *
 */
public class PlaylistSaveAction extends AbstractAction {

    private static final long serialVersionUID = 3222156473663604132L;

    private PlaylistFrame playlist;

    private PlaylistItemArray itemArray;

    public PlaylistSaveAction(PlaylistFrame playlist) {
        super("save");
        this.playlist = playlist;
        this.itemArray = playlist.getItemArray();
    }

    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new PlaylistFilesFilter());
        int returnVal = fc.showOpenDialog(playlist);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            PlaylistFile.storeJMP(itemArray, fc.getSelectedFile());
        }
    }
}
