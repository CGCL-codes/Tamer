package org.drftpd.vfs.index.lucene.extensions.mp3;

import org.drftpd.dynamicdata.Key;

/**
 * @author scitz0
 * @version $Id: MP3QueryParams.java 2484 2011-07-09 10:25:43Z scitz0 $
 */
public class MP3QueryParams {

    public static final Key<MP3QueryParams> MP3QUERYPARAMS = new Key<MP3QueryParams>(MP3QueryParams.class, "mp3queryparams");

    private String _genre;

    private String _title;

    private String _artist;

    private String _album;

    private Integer _fromYear;

    private Integer _toYear;

    public String getGenre() {
        return _genre;
    }

    public String getTitle() {
        return _title;
    }

    public String getArtist() {
        return _artist;
    }

    public String getAlbum() {
        return _album;
    }

    public Integer getMinYear() {
        return _fromYear;
    }

    public Integer getMaxYear() {
        return _toYear;
    }

    public void setGenre(String genre) {
        _genre = genre;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public void setArtist(String artist) {
        _artist = artist;
    }

    public void setAlbum(String album) {
        _album = album;
    }

    public void setMinYear(Integer year) {
        _fromYear = year;
    }

    public void setMaxYear(Integer year) {
        _toYear = year;
    }
}
