package jokeboxjunior.gui.windows.model.audio.album;

import jokeboxjunior.gui.windows.AbstractDialogController;
import java.awt.Frame;

/**
 *
 * @author B1
 */
public class EditAudioAlbumWindowController extends AbstractDialogController {

    public static final String WINDOW_NAME = "EditAudioAlbum";

    public EditAudioAlbumWindowController(Frame thisParent) {
        super(thisParent, true);
    }

    @Override
    public String getWindowName() {
        return WINDOW_NAME;
    }
}
