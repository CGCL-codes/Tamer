package filePackage;

import headFrame.PublicFrameActions;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import mainpackage.Lyricscatcher;

public class FilesListener implements ChangeListener, java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3079222603217707884L;

    @Override
    public void stateChanged(ChangeEvent evt) {
        if (Lyricscatcher.mframe == null) return;
        if (Lyricscatcher.mframe.mfa == null) return;
        if (Lyricscatcher.mframe.mfa.updatetablethread == null) return;
        if (PublicFrameActions.dontreportFileChanges) return;
        if (evt.getSource() instanceof MP3FileInformation) {
            Lyricscatcher.mframe.mfa.updatetablethread.updateMP3((MP3FileInformation) evt.getSource());
            PublicFrameActions.reloadSelection();
        }
        if (evt.getSource() instanceof LyricsFile) {
            Lyricscatcher.mframe.mfa.updatetablethread.updateLyrics((LyricsFile) evt.getSource());
            PublicFrameActions.reloadSelection();
        }
    }
}
