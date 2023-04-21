package com.musparke.midi.musicxml;

/**
 * self defined exception.
 * @author Mao
 *
 */
public abstract class MusicXmlException extends Exception {

    private static final long serialVersionUID = -7782473886031155402L;

    public MusicXmlException() {
        super();
    }

    public MusicXmlException(String message) {
        super(message);
    }
}
