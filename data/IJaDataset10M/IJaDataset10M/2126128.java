package org.mobicents.media.server.impl.resource.mediaplayer.mpeg;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 
 * @author amit bhayani
 * 
 */
public class AudioTrack extends RTPTrack {

    public AudioTrack(TrackBox audioTrackBox, TrackBox audioHintTrackBox, File file) throws FileNotFoundException {
        super(audioTrackBox, audioHintTrackBox, file);
    }
}
