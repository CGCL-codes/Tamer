package com.shieldsbetter.riot;

import com.shieldsbetter.paramour.data.MakesNoContributionException;
import com.shieldsbetter.paramour.data.StandardSampleSource;
import com.shieldsbetter.paramour.resources.ResourceException;
import com.shieldsbetter.paramour.resources.ResourceManager;
import com.shieldsbetter.paramour.soundgraph.MultiplexerSound;
import com.shieldsbetter.paramour.soundgraph.Sound;
import com.shieldsbetter.paramour.soundgraph.SoundChangeListener;
import com.shieldsbetter.paramour.time.Extent;
import com.shieldsbetter.riot.models.AnnotatedDoubleLineModel;
import com.shieldsbetter.riot.models.BooleanModel;
import com.shieldsbetter.riot.models.BooleanModelToggleView;
import com.shieldsbetter.riot.models.Model;
import com.shieldsbetter.riot.models.ModelTableModel;
import com.shieldsbetter.riot.models.StringModel;
import com.shieldsbetter.riot.models.StringModelEditableLabelView;
import com.shieldsbetter.riot.models.EventScheduleModelTimelineView;
import com.shieldsbetter.riot.models.JComponentSelectionModel;
import com.shieldsbetter.riot.swing.AnnotationSet;
import com.shieldsbetter.riot.swing.JTimeline;
import com.shieldsbetter.riot.swing.JTimelineView;
import com.shieldsbetter.riot.swing.Span;
import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author hamptos
 */
public class Soundscape extends Model implements Sound {

    public static final Object TRACK_NAME = "TrackName";

    public static final Object TIMELINE = "Timeline";

    public static final Object ARM_BUTTON = "ArmButton";

    public static final Object CURSOR = "Cursor";

    public final SoundscapePlayer player = new SoundscapePlayer(this);

    private MultiplexerSound myMultiplexer = new MultiplexerSound();

    private ModelTableModel myTracks = new ModelTableModel();

    private Environment myEnvironment;

    private ResourceManager myResourceManager;

    private AnnotatedDoubleLineModel myAnnotationsModel = new AnnotatedDoubleLineModel();

    private final AnnotatedDoubleLineModel.Annotation myCursor;

    public Soundscape(Environment e) {
        this(e, e.getResourceManager());
    }

    public Soundscape(Environment e, ResourceManager m) {
        myEnvironment = e;
        myResourceManager = m;
        myCursor = myAnnotationsModel.addAnnotation(1.0, CURSOR);
        myTracks.addModelChangeListener(new PrivateModelChangeListener(this));
        Factory<Model> pf = new PrototypeFactory(new StringModel("Unnamed Track"));
        myTracks.createColumn(TRACK_NAME, pf, new StringModelEditableLabelView());
        pf = new PrototypeFactory(new BooleanModel(false));
        myTracks.createColumn(ARM_BUTTON, pf, new BooleanModelToggleView("Arm", Color.BLACK, new Color(0.7f, 0.5f, 0.5f), new Color(1.0f, 0.4f, 0.4f), new Color(0.9f, 0.0f, 0.0f)));
        pf = new TrackModelFactory(myEnvironment, myResourceManager);
        myTracks.createColumn(TIMELINE, pf, new EventScheduleModelTimelineView());
        createNewTrack();
    }

    public ResourceManager getResourceManager() {
        return myResourceManager;
    }

    public Track[] getArmedTracks() {
        List<Track> armedTracks = new LinkedList<Track>();
        BooleanModel armedState;
        int trackCount = myTracks.getRowCount();
        for (int trackNumber = 0; trackNumber < trackCount; trackNumber++) {
            armedState = (BooleanModel) myTracks.getCell(ARM_BUTTON, trackNumber);
            if (armedState.getValue()) {
                armedTracks.add(new Track(myTracks.getRow(trackNumber)));
            }
        }
        return armedTracks.toArray(new Track[0]);
    }

    public void setCursorPosition(double seconds) {
        setCursorPositionWithouthAffectingPlayback(seconds);
        if (player.isPlaying()) {
            player.play();
        }
    }

    void setCursorPositionWithouthAffectingPlayback(double seconds) {
        myCursor.move(seconds);
    }

    public double getCursorPosition() {
        return myCursor.getPosition();
    }

    public AnnotatedDoubleLineModel getAnnotationsModel() {
        return myAnnotationsModel;
    }

    public void createNewTrack() {
        int newTrackIndex = myTracks.createRow();
        myMultiplexer.addSound((Sound) myTracks.getCell(TIMELINE, newTrackIndex));
    }

    public JComponent getView(List columns) {
        return myTracks.getView(columns);
    }

    public JTimelineView getTimelineView(double pixelsPerSecond, int trackHeight, int spacing, AnnotationSet renderers) {
        JTimelineView retval = new JTimelineView(this, pixelsPerSecond, trackHeight, spacing, myAnnotationsModel, renderers);
        List columns = new LinkedList();
        columns.add(TIMELINE);
        myTracks.rebuildView(columns, retval);
        for (JTimeline t : retval) {
            t.setPixelsPerSecond(pixelsPerSecond);
        }
        retval.setSelectionModel(new JComponentSelectionModel<Span>());
        return retval;
    }

    public StandardSampleSource getContributionOver(Extent window, float sampleRate) throws MakesNoContributionException, ResourceException, IOException {
        return myMultiplexer.getContributionOver(window, sampleRate);
    }

    public Extent getContributionExtent() throws MakesNoContributionException {
        return myMultiplexer.getContributionExtent();
    }

    public void addSoundChangeListener(SoundChangeListener listener) {
        myMultiplexer.addSoundChangeListener(listener);
    }

    public void removeSoundChangeListener(SoundChangeListener listener) {
        myMultiplexer.removeSoundChangeListener(listener);
    }

    private class PrivateModelChangeListener implements ChangeListener {

        private final Model myParent;

        public PrivateModelChangeListener(Model parent) {
            myParent = parent;
        }

        public void stateChanged(ChangeEvent e) {
            myParent.fireModelChangeEvent();
        }
    }
}
