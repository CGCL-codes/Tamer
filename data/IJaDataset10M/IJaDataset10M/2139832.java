package tags;

import java.util.Iterator;
import java.util.List;
import entagged.audioformats.*;

/**
 *
 * Interface for combination different libraries
 * @author alexog
 */
public interface MediaFile {

    /**
     *
     * @return
     */
    public int getBitrate();

    /**
     *
     * @return
     */
    public int getChannelNumber();

    /**
     *
     * @return
     */
    public String getEncodingType();

    /**
     *
     * @return
     */
    public String getExtraEncodingInfos();

    /**
     *
     * @return
     */
    public int getLength();

    /**
     *
     * @return
     */
    public float getPreciseLength();

    /**
     *
     * @return
     */
    public int getSamplingRate();

    /**
     *
     * @return
     */
    public Tag getTag();

    /**
     *
     * @param s
     * @return
     */
    public List get(String s);

    /**
     *
     * @return
     */
    public List getAlbum();

    /**
     *
     * @return
     */
    public List getArtist();

    /**
     *
     * @return
     */
    public List getComment();

    /**
     *
     * @return
     */
    public Iterator getFields();

    /**
     *
     * @return
     */
    public String getFirstAlbum();

    /**
     *
     * @return
     */
    public String getFirstArtist();

    /**
     *
     * @return
     */
    public String getFirstComment();

    /**
     *
     * @return
     */
    public String getFirstGenre();

    /**
     *
     * @return
     */
    public String getFirstTitle();

    /**
     *
     * @return
     */
    public String getFirstTrack();

    /**
     *
     * @return
     */
    public String getFirstYear();

    /**
     *
     * @return
     */
    public List getGenre();

    /**
     *
     * @return
     */
    public List getTitle();

    /**
     *
     * @return
     */
    public List getTrack();

    /**
     *
     * @return
     */
    public List getYear();

    /**
     *
     * @return
     */
    public boolean hasCommonFields();

    /**
     *
     * @param s
     * @return
     */
    public boolean hasField(String s);

    /**
     *
     * @return
     */
    public boolean isEmpty();
}
