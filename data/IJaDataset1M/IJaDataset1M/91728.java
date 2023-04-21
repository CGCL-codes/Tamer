package com.teleca.jamendo.util.download;

import com.teleca.jamendo.api.JamendoGet2Api;
import com.teleca.jamendo.api.PlaylistEntry;

/**
 * Various helper functions
 * 
 * @author Lukasz Wisniewski
 */
public class DownloadHelper {

    public static String getFileName(PlaylistEntry playlistEntry, String downloadFormat) {
        String pattern = "%02d - %s";
        if (downloadFormat.equals(JamendoGet2Api.ENCODING_MP3)) {
            pattern += ".mp3";
        } else {
            pattern += ".ogg";
        }
        return String.format(pattern, playlistEntry.getTrack().getNumAlbum(), playlistEntry.getTrack().getName());
    }

    public static String getRelativePath(PlaylistEntry playlistEntry) {
        return String.format("/%s/%s", playlistEntry.getAlbum().getArtistName(), playlistEntry.getAlbum().getName());
    }

    public static String getAbsolutePath(PlaylistEntry playlistEntry, String destination) {
        return destination + getRelativePath(playlistEntry);
    }
}
