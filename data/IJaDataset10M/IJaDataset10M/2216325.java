package net.sf.groofy.listeners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.groofy.GroofyApp;
import net.sf.groofy.datamodel.MatchesPlaylist;
import net.sf.groofy.datamodel.TrackMatch;
import net.sf.groofy.i18n.Messages;
import net.sf.groofy.jobs.Job;
import net.sf.groofy.jobs.JobStatus;
import net.sf.groofy.logger.GroofyLogger;
import net.sf.groofy.preferences.GroofyConstants;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class SearchAgainSelectionListener implements SelectionListener {

    @Override
    public void widgetDefaultSelected(SelectionEvent e) {
    }

    @Override
    public void widgetSelected(SelectionEvent e) {
        MatchesPlaylist playlist = GroofyApp.getInstance().getMatchWindow().getContents();
        final List<TrackMatch> emptyTracks = new ArrayList<TrackMatch>();
        if (playlist != null) {
            for (TrackMatch trackMatch : playlist.getMatches()) {
                if (trackMatch.getMatch() == null) {
                    emptyTracks.add(trackMatch);
                }
            }
            if (emptyTracks.size() == 0) {
                GroofyApp.getInstance().showInfo(Messages.getString("SearchAgainSelectionListener.NotEmptyTracks"));
                GroofyLogger.getInstance().log(Messages.getString("SearchAgainSelectionListener.NotEmptyTracks"));
                return;
            } else {
                GroofyLogger.getInstance().log(String.format(Messages.getString("SearchAgainSelectionListener.SearchingForXTracks"), emptyTracks.size()));
            }
            Job loadGroovesharkCorrespondences = new Job(Messages.getString("SearchAgainSelectionListener.RetrievingGsInfo"), new Image(Display.getDefault(), getClass().getResourceAsStream(GroofyConstants.PATH_16_GROOVESHARK)), emptyTracks.size()) {

                @Override
                public JobStatus run() {
                    String message;
                    for (int i = 0; i < emptyTracks.size(); i++) {
                        TrackMatch trackMatch = emptyTracks.get(i);
                        if (isCancelled()) return JobStatus.CANCELLED;
                        try {
                            trackMatch.autoFillCorrespondences();
                        } catch (IOException e) {
                            GroofyLogger.getInstance().logException(e);
                        }
                        message = String.format(Messages.getString("SearchAgainSelectionListener.ElementXofY"), i + 1, emptyTracks.size(), trackMatch.toString());
                        setDescription(message);
                        GroofyLogger.getInstance().log(message);
                        GroofyApp.getInstance().getMatchWindow().refresh(true, true);
                        worked(1);
                    }
                    GroofyLogger.getInstance().log(Messages.getString("SearchAgainSelectionListener.searchFinished"));
                    return JobStatus.OK;
                }
            };
            loadGroovesharkCorrespondences.schedule();
        }
    }
}
