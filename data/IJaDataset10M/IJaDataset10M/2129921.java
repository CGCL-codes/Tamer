package muvis.audio.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import muvis.Elements;
import muvis.Environment;
import muvis.view.MusicControllerView;

/**
 * Generic action for playing the next track in the MuVis audio player
 * @author Ricardo
 */
public class MusicPlayerPlayNextTrackAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        MusicControllerView controller = (MusicControllerView) Environment.getEnvironmentInstance().getViewManager().getView(Elements.MUSIC_PLAYER_VIEW);
        controller.playPreviousTrack();
    }
}
