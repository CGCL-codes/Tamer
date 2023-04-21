package net.sourceforge.cridmanager.actions;

import org.apache.log4j.Logger;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import net.sourceforge.cridmanager.CridController;
import net.sourceforge.cridmanager.ILocation;
import net.sourceforge.cridmanager.LocationFactory;
import net.sourceforge.cridmanager.Messages;
import net.sourceforge.cridmanager.Playlist;
import net.sourceforge.cridmanager.box.BoxManager;
import net.sourceforge.cridmanager.box.IBox;
import net.sourceforge.cridmanager.services.ServiceProvider;

/**
 * Eine Aktion zum Erstellen einer Audio-Playliste.
 */
public class CreatePlaylistAudioAction extends AbstractAction {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(CreatePlaylistAudioAction.class);

    /**
	 * 
	 */
    private final CridController controller;

    /**
	 * @param controller TODO
	 */
    public CreatePlaylistAudioAction(CridController controller) {
        super(Messages.getString("CridController.CreatePlaylistAudioAction"));
        this.controller = controller;
        putValue(IActions.SIZABLE_ICON_NAME, "images/Playlistensymbol_Audio.gif");
        putValue(Action.SHORT_DESCRIPTION, Messages.getString("CridController.CreatePlaylistAudioActionD"));
        putValue(Action.ACCELERATOR_KEY, (KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0)));
    }

    /**
	 *  
	 */
    public void actionPerformed(ActionEvent e) {
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - start");
        }
        BoxManager boxManager = (BoxManager) ServiceProvider.instance().getService(null, BoxManager.class);
        if (boxManager.getBoxCount() == 1) {
            IBox box = boxManager.getBoxes()[0];
            ILocation dest = this.controller.getDestination(Messages.getString("CridController.CreatePlaylistAudioActionDialog"), null);
            if (dest != null) if (dest.exists()) {
                if (LocationFactory.convertToBoxPath(dest, box).length() > 0) {
                    this.controller.startPlaylistWorker(dest, Playlist.PLAYLIST_AUDIO, box);
                } else {
                    this.controller.showErrorDialog("Die Box hat keine passende Dateifreigabe.");
                }
            } else {
                this.controller.showErrorDialog(Messages.getString("CridController.FolderMissing"));
            }
        } else if (boxManager.getBoxCount() > 1) {
            this.controller.showPlaylistMenu((Component) e.getSource(), Playlist.PLAYLIST_AUDIO);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("actionPerformed(ActionEvent) - end");
        }
    }
}
