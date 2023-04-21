package pl.matt.media.manager;

import java.io.IOException;

/**
 * @author mateusz
 *
 */
public interface VideoManager {

    /**
	 * @param videoFilePath
	 * @param framesDirectoryPath
	 * @return
	 * @throws IOException 
	 */
    public boolean extractFrames(String videoFilePath, String framesDirectoryPath) throws IOException;
}
