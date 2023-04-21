package pl.matt.media.manager.impl;

import java.io.IOException;
import pl.matt.media.extractor.FramesExtractor;
import pl.matt.media.manager.VideoManager;

public class VideoManagerImpl implements VideoManager {

    private FramesExtractor framesExtactor;

    @Override
    public boolean extractFrames(String videoFilePath, String framesDirectoryPath) throws IOException {
        return framesExtactor.extractFrames(videoFilePath, framesDirectoryPath);
    }

    public void setFramesExtactor(FramesExtractor framesExtactor) {
        this.framesExtactor = framesExtactor;
    }
}
