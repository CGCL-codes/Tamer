package org.jampa.gui.actions;

import org.eclipse.jface.action.Action;
import org.jampa.controllers.Controller;
import org.jampa.utils.Log;

public class PlayPreviousAction extends Action {

    public void run() {
        Log.getInstance(PlayAction.class).debug("doActionPrevious()");
        Controller.getInstance().getPlaylistController().playPreviousInPlaylist();
    }
}
