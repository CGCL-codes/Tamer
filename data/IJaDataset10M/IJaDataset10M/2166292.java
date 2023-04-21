package org.happycomp.radio.downloader.impl;

/**
 * Spatny playlist 
 * @author pavels
 */
public class IvalidPlaylistContentException extends Exception {

    private static final long serialVersionUID = 1L;

    private String content;

    public IvalidPlaylistContentException(String cnt) {
        super();
        this.content = cnt;
    }

    public IvalidPlaylistContentException(String message, Throwable cause, String cnt) {
        super(message, cause);
        this.content = cnt;
    }

    public IvalidPlaylistContentException(String message, String cnt) {
        super(message);
        this.content = cnt;
    }

    public IvalidPlaylistContentException(Throwable cause, String cnt) {
        super(cause);
        this.content = cnt;
    }

    public String getContent() {
        return content;
    }
}
