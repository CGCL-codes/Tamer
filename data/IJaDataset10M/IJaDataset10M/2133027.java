package com.teleca.jamendo.api.impl;

import java.util.Comparator;
import com.teleca.jamendo.api.Track;

public class TrackComparator implements Comparator<Track> {

    @Override
    public int compare(Track track1, Track track2) {
        if (track1.getNumAlbum() > track2.getNumAlbum()) {
            return 1;
        } else if (track1.getNumAlbum() < track2.getNumAlbum()) {
            return -1;
        } else return 0;
    }
}
