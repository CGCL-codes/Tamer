package com.google.code.sagetvaddons.sagealert.server.events;

import gkusnick.sagetv.api.PlaylistAPI.Playlist;
import com.google.code.sagetvaddons.sagealert.server.CoreEventsManager;
import com.google.code.sagetvaddons.sagealert.shared.Client;
import com.google.code.sagetvaddons.sagealert.shared.SageAlertEventMetadata;

/**
 * @author dbattams
 *
 */
public class PlaylistAddedEvent extends PlaylistEvent {

    /**
	 * @param playlist
	 * @param clnt
	 * @param data
	 */
    public PlaylistAddedEvent(Playlist playlist, Client clnt, SageAlertEventMetadata data) {
        super(playlist, clnt, data);
    }

    @Override
    public String getSource() {
        return CoreEventsManager.PLAYLIST_ADDED;
    }
}
