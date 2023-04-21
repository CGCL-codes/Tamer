package jokeboxjunior.core.model.video;

import jokeboxjunior.core.controller.AbstractController;
import jokeboxjunior.core.model.*;

/**
 *
 * @author B1
 */
public class VideoFile extends AbstractMediaFile {

    public VideoFile() {
        super("video_files");
    }

    @Override
    protected void _addAttribs() {
    }

    @Override
    public AbstractController getController() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
