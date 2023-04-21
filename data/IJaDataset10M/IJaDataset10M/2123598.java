package com.streamsicle;

import java.util.*;

public abstract interface IRandomSelector {

    /** Notify this randomSelector that the song with songID was just requested
     * by a user
     */
    public void addRequestedSongID(int songID);

    public void addToAvailableSongs(int songID);

    public void setAvailableFiles(Vector availableFiles);

    /** Request a new song to add to Streamsicle's queue based on whatever
     * selection method this randomSelector implements
     */
    public Integer getNextSongID();
}
