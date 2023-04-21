package importer.models.lyricswiki;

import importer.models.common.ISearchResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlRootElement(name = "LyricsResult")
public class LyricsResult implements ISearchResult {

    @XmlElement
    private String lyrics;

    @XmlElement
    private String artist;

    @XmlElement
    private String song;

    public String artist() {
        return artist;
    }

    public String getLyrics() {
        return lyrics;
    }

    public boolean isExactMatch() {
        return true;
    }

    public String trackName() {
        return song;
    }
}
