package jokeboxjunior.core.model.volume.io;

import java.io.File;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import jokeboxjunior.core.model.volume.*;

/**
 *
 * @author B1
 */
public class FTPMediaVolumeIO extends AbstractMediaVolumeIO {

    public FTPMediaVolumeIO(MediaVolume thisMediaVolume) {
        super(thisMediaVolume);
    }

    @Override
    public boolean existsFile(String thisFilePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public File getFile(String thisFilePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
