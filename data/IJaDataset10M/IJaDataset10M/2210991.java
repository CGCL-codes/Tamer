package org.jampa.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.jampa.controllers.Controller;
import org.jampa.gui.views.PlaylistView;
import org.jampa.utils.Log;

public class CreatePlaylistAction extends Action {

    private String _playlistName;

    public CreatePlaylistAction(String name) {
        _playlistName = name;
    }

    public void run() {
        if (!_playlistName.isEmpty()) {
            Log.getInstance(OpenPlaylistAction.class).debug("Creating empty playlist : (" + _playlistName + ").");
            boolean result = Controller.getInstance().getPlaylistController().addEmptyPlaylist(_playlistName);
            Log.getInstance(OpenPlaylistAction.class).debug("Creating empty playlist : " + ((result) ? "Ok" : "!Ok") + ".");
            if (result) {
                try {
                    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                    page.showView(PlaylistView.ID, _playlistName, IWorkbenchPage.VIEW_ACTIVATE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
