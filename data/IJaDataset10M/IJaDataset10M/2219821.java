package org.sink.media;

import org.sink.exceptions.SinkException;

/**
 * Gets thrown if a stream contains no valid RTP tracks.
 */
public class SinkNoValidRTPTracksFoundException extends SinkException {

    public SinkNoValidRTPTracksFoundException() {
        super("Error", "The Java Media Framework, which is base of Sink, does not support your capture device. Feel free to comlain to Sun Microsystems about it.");
    }
}
