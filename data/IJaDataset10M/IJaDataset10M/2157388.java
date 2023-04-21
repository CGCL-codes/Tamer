package mp3player;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class to represent a MP3 stream.
 */
public class MP3Stream extends MP3 {

    URL url;

    /**
	 * Creates a new MP3Stream object for the given URL.
	 * @param url URL to MP3-formatted data.
	 * @throws MalformedURLException If the URL string is invalid.
	 */
    public MP3Stream(String url) throws MalformedURLException {
        this(new URL(url));
    }

    /**
	 * Creates a new MP3Stream object for the given URL.
	 * @param url URL to MP3-formatted data.
	 */
    public MP3Stream(URL url) {
        this.url = url;
    }

    public InputStream getStream() throws IOException {
        return url.openStream();
    }

    public String getDisplayString() {
        return "URL: " + url;
    }

    /**
	 * Returns the URL for this MP3 stream.
	 */
    public URL getURL() {
        return url;
    }

    public int getFrames() {
        return 0;
    }

    public float getLength() {
        return 0;
    }

    public boolean isSeekable() {
        return false;
    }

    public boolean isStream() {
        return true;
    }

    public int getClosestFrame(float ms) {
        return 0;
    }

    public float getFrameTime(int frame) {
        return 0.0f;
    }

    public String getAlbum() {
        return null;
    }

    public String getArtist() {
        return null;
    }

    public String getComment() {
        return null;
    }

    public String getGenre() {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public int getTrack() {
        return -1;
    }

    public int getYear() {
        return -1;
    }
}
