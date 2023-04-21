package editor.view.tab.tracks;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import editor.model.xml.datasheet.TrackElement;
import editor.model.xml.datasheet.TracksElement;
import editor.view.action.AbstractLocaleAction;

/**
 * Abstract class for removing a track from the tracks model
 */
public abstract class AbstractRemoveTrackAction extends AbstractLocaleAction {

    private static final String ACTION_NAME = "action.remove_track";

    private final TracksElement oTracks;

    private final TrackElement oTrack;

    private final AbstractTracksTab oFrame;

    /**
     * Constructor
     *
     * @param frame  to be updated after removing the track
     * @param tracks the tracks model
     * @param track  to be removed
     */
    public AbstractRemoveTrackAction(final AbstractTracksTab frame, final TracksElement tracks, final TrackElement track) {
        super(ACTION_NAME);
        oFrame = frame;
        oTracks = tracks;
        oTrack = track;
    }

    /**
     * @return the tracks model
     */
    protected final TracksElement getTracksElement() {
        return oTracks;
    }

    /**
     * @return the view
     */
    protected final AbstractTracksTab getView() {
        return oFrame;
    }

    /**
     * @return the track
     */
    protected final TrackElement getTrack() {
        return oTrack;
    }

    /**
     * updates the text to the current locale
     *
     * @see base.gui.action.UpdateAction Interface
     */
    @Override
    public final void updateLocale() {
        super.updateLocale();
        putValue(Action.MNEMONIC_KEY, null);
    }

    /**
     * implemented by subclasses
     *
     * @param e ActionEvent
     */
    public abstract void actionPerformed(final ActionEvent e);
}
