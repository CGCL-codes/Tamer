package com.slychief.javalastfm.query.artist;

import com.slychief.javalastfm.query.*;
import com.slychief.lastfmapi.Artist;
import com.slychief.lastfmapi.Lfm;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alexander Schindler
 */
public class ArtistGetInfo extends AbstractQuerierType {

    private static final String METHOD_NAME = "artist.getinfo";

    private static final String ENTITY_NAME = "com.slychief.lastfmapi.Artist";

    private static final Map<Integer, String> ERROR_MESSAGES = new HashMap<Integer, String>() {

        {
            put(2, "Invalid service -This service does not exist");
            put(3, "Invalid Method - No method with that name in this package");
            put(4, "Authentication Failed - You do not have permissions to a ccess the service");
            put(5, "Invalid format - This service doesn't exist in that format");
            put(6, "Invalid parameters - Your request is missing a required parameter");
            put(7, "Invalid resource specified");
            put(9, "Invalid session key - Please re-authenticate");
            put(10, "Invalid API key - You must be granted a valid key by last.fm");
            put(11, "Service Offline - This service is temporarily offline. Try again later.");
            put(12, "Subscribers Only - This service is only available to paid last.fm subscribers");
        }
    };

    private String artist;

    private String lang;

    private String mbid;

    /**
     * Constructs ...
     *
     */
    public ArtistGetInfo() {
    }

    /**
     * Constructs ...
     *
     *
     * @param _artist
     * @param _mbid
     * @param _lang
     */
    public ArtistGetInfo(String _artist, String _mbid, String _lang) {
        artist = _artist;
        mbid = _mbid;
        lang = _lang;
    }

    @Override
    protected Query buildQuery() {
        Query q = new Query();
        q.setEntityName(ENTITY_NAME);
        q.setMethodName(METHOD_NAME);
        q.addParameter(ILastfmProperties.PARAM_ARTIST, "name", artist, QueryConjunction.AND);
        q.addParameter(ILastfmProperties.PARAM_MBID, mbid, QueryConjunction.AND);
        q.addParameter(ILastfmProperties.PARAM_LANG, lang, QueryConjunction.AND);
        return q;
    }

    /**
     * @return the artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @return the mbid
     */
    public String getMbid() {
        return mbid;
    }

    @Override
    protected Object getWebserviceResult(Lfm lfm) {
        return lfm.getArtist();
    }

    /**
     * @param artist the artist to set
     */
    public void setArtist(String artist) {
        this.artist = artist;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * @param mbid the mbid to set
     */
    public void setMbid(String mbid) {
        this.mbid = mbid;
    }
}
