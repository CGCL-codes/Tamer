package org.paquitosoft.namtia.vo.musicbrainz;

import java.util.Collection;
import javax.swing.ImageIcon;
import org.paquitosoft.namtia.vo.GenericVO;

/**
 *
 * @author telemaco
 */
public class MBRelease extends GenericVO {

    private String title;

    private String type;

    private String asin;

    private String score;

    private String idArtist;

    private Collection releaseEventList;

    private Collection discList;

    private Collection tracksList;

    private ImageIcon cover;

    private String year;

    /** Creates a new instance of MBRelease */
    public MBRelease() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public void setIdArtist(String idArtist) {
        this.idArtist = idArtist;
    }

    public Collection getReleaseEventList() {
        return releaseEventList;
    }

    public void setReleaseEventList(Collection releaseEventList) {
        this.releaseEventList = releaseEventList;
    }

    public Collection getDiscList() {
        return discList;
    }

    public void setDiscList(Collection discList) {
        this.discList = discList;
    }

    public Collection getTracksList() {
        return tracksList;
    }

    public void setTracksList(Collection tracksList) {
        this.tracksList = tracksList;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ImageIcon getCover() {
        return cover;
    }

    public void setCover(ImageIcon cover) {
        this.cover = cover;
    }

    public String toString() {
        return this.title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
