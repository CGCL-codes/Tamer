package javazoom.spi.mpeg.sampled.file;

import javazoom.spi.mpeg.sampled.file.tag.MP3Tag;
import javazoom.spi.mpeg.sampled.file.tag.TagParseEvent;
import javazoom.spi.mpeg.sampled.file.tag.TagParseListener;

/**
 * This class (singleton) allow to be notified on shoutcast meta data
 * while playing the stream (such as song title).
 */
public class IcyListener implements TagParseListener {

    private static IcyListener instance = null;

    private MP3Tag lastTag = null;

    private String streamTitle = null;

    private String streamUrl = null;

    private IcyListener() {
        super();
    }

    public static synchronized IcyListener getInstance() {
        if (instance == null) {
            instance = new IcyListener();
        }
        return instance;
    }

    public void tagParsed(TagParseEvent tpe) {
        lastTag = tpe.getTag();
        String name = lastTag.getName();
        if ((name != null) && (name.equalsIgnoreCase("streamtitle"))) {
            streamTitle = (String) lastTag.getValue();
        } else if ((name != null) && (name.equalsIgnoreCase("streamurl"))) {
            streamUrl = (String) lastTag.getValue();
        }
    }

    /**
	 * @return
	 */
    public MP3Tag getLastTag() {
        return lastTag;
    }

    /**
	 * @param tag
	 */
    public void setLastTag(MP3Tag tag) {
        lastTag = tag;
    }

    /**
	 * @return
	 */
    public String getStreamTitle() {
        return streamTitle;
    }

    /**
	 * @return
	 */
    public String getStreamUrl() {
        return streamUrl;
    }

    /**
	 * @param string
	 */
    public void setStreamTitle(String string) {
        streamTitle = string;
    }

    /**
	 * @param string
	 */
    public void setStreamUrl(String string) {
        streamUrl = string;
    }

    /**
	 * Reset all properties.
	 */
    public void reset() {
        lastTag = null;
        streamTitle = null;
        streamUrl = null;
    }
}
