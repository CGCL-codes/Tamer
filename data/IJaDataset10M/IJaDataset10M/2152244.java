package com.streamsicle.songinfo;

import java.io.*;

/**
 *  SongInfo that is the last resort so we don't have a null SongInfo.
 *
 *@author     mhall
 *@created    September 17, 2002
 */
public class SongInfoDegenerate extends SongInfo {

    private String fileName;

    /**
     *  Constructor for the SongInfoFile object
     *
     *@param  songFile  Description of the Parameter
     */
    public SongInfoDegenerate(File songFile) {
        fileName = songFile.getName();
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasTitle() {
        return true;
    }

    /**
     *  Gets the title attribute of the SongInfoFile object
     *
     *@return    The title value
     */
    public String getTitle() {
        return fileName;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasArtist() {
        return false;
    }

    /**
     *  Gets the artist attribute of the SongInfoFile object
     *
     *@return    The artist value
     */
    public String getArtist() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasAlbum() {
        return false;
    }

    /**
     *  Gets the album attribute of the SongInfoFile object
     *
     *@return    The album value
     */
    public String getAlbum() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasTrackNumber() {
        return false;
    }

    /**
     *  Gets the trackNumber attribute of the SongInfoFile object
     *
     *@return    The trackNumber value
     */
    public String getTrackNumber() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasYear() {
        return false;
    }

    /**
     *  Gets the year attribute of the SongInfoFile object
     *
     *@return    The year value
     */
    public String getYear() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasGenre() {
        return false;
    }

    /**
     *  Gets the genre attribute of the SongInfoFile object
     *
     *@return    The genre value
     */
    public String getGenre() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasComment() {
        return false;
    }

    /**
     *  Gets the comment attribute of the SongInfoFile object
     *
     *@return    The comment value
     */
    public String getComment() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasComposer() {
        return false;
    }

    /**
     *  Gets the composer attribute of the SongInfoFile object
     *
     *@return    The composer value
     */
    public String getComposer() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasOriginalArtist() {
        return false;
    }

    /**
     *  Gets the originalArtist attribute of the SongInfoFile object
     *
     *@return    The originalArtist value
     */
    public String getOriginalArtist() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasURL() {
        return false;
    }

    /**
     *  Gets the uRL attribute of the SongInfoFile object
     *
     *@return    The uRL value
     */
    public String getURL() {
        return null;
    }

    /**
     *  Description of the Method
     *
     *@return    Description of the Return Value
     */
    public boolean hasTimeLength() {
        return true;
    }

    /**
     *  Gets the timeLength attribute of the SongInfoFile object
     *
     *@return    The timeLength value
     */
    public String getTimeLength() {
        return "1";
    }
}
