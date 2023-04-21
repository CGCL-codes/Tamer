package org.paquitosoft.namtia.view.actions;

import org.paquitosoft.namtia.common.NamtiaUtilities;
import org.paquitosoft.namtia.view.facade.ViewFacade;
import org.paquitosoft.namtia.view.util.PlayListModel;
import org.paquitosoft.namtia.vo.SongVO;

/**
 *
 * @author telemaco
 */
public class PlaySongViewAction {

    private SongVO currentSong;

    /** Creates a new instance of PlaySongViewAction */
    public PlaySongViewAction() {
    }

    public PlaySongViewAction(SongVO song) {
        this.currentSong = song;
    }

    public void execute() {
        try {
            if (this.currentSong != null) {
                new ViewFacade().setCurrentSong(this.currentSong);
            } else if (new ViewFacade().getCurrentSong() == null) {
                PlayListModel playlist = new ViewFacade().getPlaylistModel();
                if (playlist.getSize() > 0) {
                    new ViewFacade().setCurrentSong(playlist.getSong(0));
                } else {
                    return;
                }
            }
            System.out.println("cancion actual -> " + new ViewFacade().getCurrentSong());
            new ViewFacade().playCurrentSong();
        } catch (Exception e) {
            e.printStackTrace();
        }
        new FillInfoPanelAction().start();
    }
}
