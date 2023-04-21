package org.shake.lastfm.ui.playlist;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.shake.lastfm.event.EventTopic;
import org.shake.lastfm.event.TrackSelectedEvent;
import org.shake.lastfm.model.Track;
import org.shake.lastfm.swing.border.RoundCornerBorder;
import org.shake.lastfm.ui.StatusPane;

public class PlaylistPane extends JXPanel {

    private static final long serialVersionUID = -1527394944309627631L;

    private JXList trackList;

    private TrackListModel trackListModel;

    private TracksHandler tracksHandler;

    public PlaylistPane() {
        super();
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        Border emptyBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border border = BorderFactory.createCompoundBorder(new RoundCornerBorder(), emptyBorder);
        JXPanel contentPanel = new JXPanel(new BorderLayout());
        this.trackListModel = createTrackModel();
        this.trackList = new JXList(this.trackListModel);
        this.trackList.setBorder(BorderFactory.createEmptyBorder());
        this.trackList.setBackground(getBackground());
        this.trackList.setCellRenderer(new TrackRenderer());
        this.trackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.trackList.getSelectionModel().addListSelectionListener(new PlaylistSelectionAdapter());
        JScrollPane sp = new JScrollPane(this.trackList);
        sp.setBorder(BorderFactory.createEmptyBorder());
        JXPanel trackListPanel = new JXPanel(new BorderLayout());
        trackListPanel.setBorder(border);
        trackListPanel.add(sp, BorderLayout.CENTER);
        contentPanel.add(trackListPanel, BorderLayout.CENTER);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        ProgressPanel progressPanel = new ProgressPanel();
        progressPanel.setBorder(emptyBorder);
        contentPanel.add(progressPanel, BorderLayout.SOUTH);
        UrlPanel urlPanel = new UrlPanel();
        urlPanel.setBorder(emptyBorder);
        contentPanel.add(urlPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        StatusPane statusPane = new StatusPane();
        add(statusPane, BorderLayout.SOUTH);
    }

    private TrackListModel createTrackModel() {
        TrackListModel model = new TrackListModel();
        this.tracksHandler = new TracksHandler(model);
        AnnotationProcessor.process(this.tracksHandler);
        return model;
    }

    private class PlaylistSelectionAdapter implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int selIdx = PlaylistPane.this.trackList.getSelectedIndex();
            Track track = null;
            if (selIdx != -1) {
                int realIdx = PlaylistPane.this.trackList.convertIndexToModel(selIdx);
                track = (Track) PlaylistPane.this.trackListModel.getElementAt(realIdx);
            }
            EventBus.publish(EventTopic.TRACK_SELECTED, new TrackSelectedEvent(track));
        }
    }
}
