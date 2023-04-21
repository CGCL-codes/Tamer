package com.abso.mp3tunes.locker.ui.actions;

import org.eclipse.jface.action.Action;
import com.abso.mp3tunes.locker.ui.LockerImages;
import com.abso.mp3tunes.locker.ui.views.LockerBrowserView;

/**
 * Action able to toggle between the different Locker browser views: artist
 * view, album view, playlist view, search.
 */
public class ToggleViewAction extends Action {

    /** The Locker browser view. */
    private LockerBrowserView lockerBrowser;

    /**
	 * The view mode of this action.
	 * 
	 * @see LockerBrowserView#ARTIST_MODE
	 * @see LockerBrowserView#ALBUM_MODE
	 * @see LockerBrowserView#PLAYLIST_MODE
	 * @see LockerBrowserView#SEARCH_MODE
	 */
    private int viewMode;

    /**
	 * Constructs a new action.
	 * 
	 * @param lockerBrowser
	 *            the Locker browser view.
	 * @param viewMode
	 *            the view mode.
	 * @see LockerBrowserView#ARTIST_MODE
	 * @see LockerBrowserView#ALBUM_MODE
	 * @see LockerBrowserView#PLAYLIST_MODE
	 * @see LockerBrowserView#SEARCH_MODE
	 */
    public ToggleViewAction(LockerBrowserView lockerBrowser, int viewMode) {
        super("", AS_RADIO_BUTTON);
        this.lockerBrowser = lockerBrowser;
        this.viewMode = viewMode;
        switch(viewMode) {
            case LockerBrowserView.ARTIST_MODE:
                setText("Artists");
                setDescription("Browse artists");
                setToolTipText("Browse Artists");
                setImageDescriptor(LockerImages.getDescriptor(LockerImages.ARTIST));
                break;
            case LockerBrowserView.ALBUM_MODE:
                setText("Albums");
                setDescription("Browse albums");
                setToolTipText("Browse Albums");
                setImageDescriptor(LockerImages.getDescriptor(LockerImages.ALBUM));
                break;
            case LockerBrowserView.PLAYLIST_MODE:
                setText("Playlists");
                setDescription("Browse playlists");
                setToolTipText("Browse Playlists");
                setImageDescriptor(LockerImages.getDescriptor(LockerImages.PLAYLIST));
                break;
            case LockerBrowserView.SEARCH_MODE:
                setText("Search");
                setDescription("Search");
                setToolTipText("Search");
                setImageDescriptor(LockerImages.getDescriptor(LockerImages.SEARCH));
                break;
        }
    }

    public void run() {
        lockerBrowser.setViewMode(viewMode);
    }
}
