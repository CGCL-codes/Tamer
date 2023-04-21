package dplayer.gui.commands;

import dplayer.queue.PriorizedQueueItem;
import dplayer.queue.commands.TrackListCommand;
import dplayer.scanner.Directory;

/**
 * Command to set a new track list.
 */
public class SelectTracksCommand extends PriorizedQueueItem implements TrackListCommand {

    private Directory mDirectory;

    private boolean mFlat;

    public SelectTracksCommand(final Directory directory, final boolean flat) {
        assert directory != null;
        mDirectory = directory;
        mFlat = flat;
        setPriority(Priority.HIGH);
    }

    public Directory getDirectory() {
        return mDirectory;
    }

    public boolean isFlat() {
        return mFlat;
    }

    /** {@inheritDoc} */
    public String getInfo() {
        return "SelectTracksCommand (directory = " + mDirectory + ", flat = " + mFlat + ")";
    }
}
