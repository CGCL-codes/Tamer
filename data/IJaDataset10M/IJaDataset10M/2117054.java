package uk.org.toot.swingui.midiui.sequenceui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JToggleButton;
import javax.swing.JComponent;
import javax.swing.JViewport;
import uk.org.toot.midi.core.MidiSystem;
import uk.org.toot.midi.sequence.MidiTrack;
import uk.org.toot.midi.sequence.SequencePosition;
import uk.org.toot.swingui.miscui.TootBar;

public class GridEditor extends Editor {

    /**
     * @label music (pitch / ridm) split
     * @supplierCardinality 1
     * @link aggregationByValue 
     */
    protected SplitView musicSplitPane;

    /**
     * @label pitch split
     * @supplierCardinality 1
     * @link aggregationByValue 
     */
    protected DomainSplitView pitchSplitPane;

    /**
     * @supplierCardinality 1
     * @label ridm split
     * @link aggregationByValue 
     */
    protected DomainSplitView ridmSplitPane;

    protected ViewScrollPane pitchScrollPane;

    protected ViewScrollPane pitchVelScrollPane;

    protected ViewScrollPane ridmScrollPane;

    protected ViewScrollPane ridmVelScrollPane;

    public GridEditor(OpenSequenceUI openSeqUI, MidiSystem rack) {
        super(openSeqUI, rack);
    }

    protected JComponent createContent() {
        Dimension minimumSize = new Dimension(320, 96);
        pitchScrollPane = new ViewScrollPane(new PitchView(this), true);
        pitchVelScrollPane = new ViewScrollPane(new PitchVelocityView(this), false);
        pitchSplitPane = new DomainSplitView(pitchScrollPane, pitchVelScrollPane);
        pitchSplitPane.setResizeWeight(1.0);
        GridPane pitchPane = new GridPane(pitchSplitPane, pitchScrollPane);
        ridmScrollPane = new ViewScrollPane(new RidmView(this), true);
        ridmVelScrollPane = new ViewScrollPane(new RidmVelocityView(this), false);
        ridmSplitPane = new DomainSplitView(ridmScrollPane, ridmVelScrollPane);
        ridmSplitPane.setResizeWeight(1.0);
        GridPane ridmPane = new GridPane(ridmSplitPane, ridmScrollPane);
        musicSplitPane = new SplitView(pitchPane, ridmPane);
        musicSplitPane.setResizeWeight(1.0);
        pitchScrollPane.setMinimumSize(minimumSize);
        ridmScrollPane.setMinimumSize(minimumSize);
        pitchVelScrollPane.setMinimumSize(minimumSize);
        ridmVelScrollPane.setMinimumSize(minimumSize);
        timeBar = ridmScrollPane.getHorizontalScrollBar();
        pitchScrollPane.setHorizontalScrollBar(timeBar);
        pitchVelScrollPane.setHorizontalScrollBar(timeBar);
        ridmScrollPane.setHorizontalScrollBar(timeBar);
        ridmVelScrollPane.setHorizontalScrollBar(timeBar);
        setTicksPerPixel(getSequence().getResolution() * 32 / 640);
        return musicSplitPane;
    }

    public void updatePosition(long tick) {
        float tpp = getTicksPerPixel();
        int x = (int) (tick / tpp);
        JViewport viewport = pitchSplitPane.getTopScrollPane().getViewport();
        java.awt.Point topleft = viewport.getViewPosition();
        Dimension viewsize = viewport.getExtentSize();
        if (follow && (x > (topleft.x + (viewsize.width * 7 / 8)) || x < topleft.x)) {
            SequencePosition pos = getSequence().getPosition(tick);
            pos = new SequencePosition(pos.bar);
            long tick0 = getSequence().getTick(pos);
            tick0 -= getSequence().getResolution();
            if (tick0 < 0) tick0 = 0;
            setStartTick(tick0);
        } else {
            repaintPositionCursor();
        }
    }

    public void setStartTick(long tick) {
        int newx = (int) (tick / getTicksPerPixel());
        pitchSplitPane.setStartPosition(newx);
        ridmSplitPane.setStartPosition(newx);
        repaintScrollPanes();
    }

    public long getStartTick() {
        return (long) (pitchSplitPane.getTopScrollPane().getViewport().getViewPosition().x * getTicksPerPixel());
    }

    protected void repaintScrollPanes() {
        repaint(50);
    }

    protected void repaintPositionCursor() {
        long pos = getSequenceUI().getSequencer().getTickPosition();
        pitchSplitPane.repaintPositionCursor(pos);
        ridmSplitPane.repaintPositionCursor(pos);
    }

    public void setTicksPerPixel(float ticksPerPixel) {
        super.setTicksPerPixel(ticksPerPixel);
        repaintScrollPanes();
    }

    public void setVisibleTrack(MidiTrack track, boolean visible) {
        super.setVisibleTrack(track, visible);
        repaintScrollPanes();
    }

    public boolean canTruncate() {
        return true;
    }

    protected JToolBar createToolBar() {
        return new ToolBar();
    }

    private class GridLeftBar extends TootBar {

        private String PZOOMOUT = "Pitch Zoom Out";

        private String PZOOMIN = "Pitch Zoom In";

        private String PITCH = "P";

        private String PITCHVEL = "Pv";

        private String DRUM = "D";

        private String DRUMVEL = "Dv";

        private JToggleButton pitchTB;

        private JToggleButton pitchVelTB;

        private JToggleButton drumTB;

        private JToggleButton drumVelTB;

        public GridLeftBar() {
            super();
            setFloatable(false);
            add(makeButton("general/ZoomOut16", PZOOMOUT, PZOOMOUT, PZOOMOUT, true));
            add(makeButton("general/ZoomIn16", PZOOMIN, PZOOMIN, PZOOMIN, true));
            add(pitchTB = makeToggleButton("tools/Pitch16", PITCH, PITCH, PITCH, true));
            add(pitchVelTB = makeToggleButton("tools/PitchVel16", PITCHVEL, PITCHVEL, PITCHVEL, true));
            add(drumTB = makeToggleButton("tools/Drum16", DRUM, DRUM, DRUM, true));
            add(drumVelTB = makeToggleButton("tools/DrumVel16", DRUMVEL, DRUMVEL, DRUMVEL, true));
        }

        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (PZOOMIN.equals(cmd)) {
                pitchSplitPane.getTopScrollPane().zoomInY();
                ridmSplitPane.getTopScrollPane().zoomInY();
            } else if (PZOOMOUT.equals(cmd)) {
                pitchSplitPane.getTopScrollPane().zoomOutY();
                ridmSplitPane.getTopScrollPane().zoomOutY();
            } else if (PITCH.equals(cmd)) {
                pitchTB.setSelected(toggleVisibility(pitchSplitPane, pitchSplitPane.getTopScrollPane()));
            } else if (PITCHVEL.equals(cmd)) {
                pitchVelTB.setSelected(toggleVisibility(pitchSplitPane, pitchSplitPane.getBottomScrollPane()));
            } else if (DRUM.equals(cmd)) {
                drumTB.setSelected(toggleVisibility(ridmSplitPane, ridmSplitPane.getTopScrollPane()));
            } else if (DRUMVEL.equals(cmd)) {
                drumVelTB.setSelected(toggleVisibility(ridmSplitPane, ridmSplitPane.getBottomScrollPane()));
            }
        }

        private boolean toggleVisibility(DomainSplitView splitView, JScrollPane scrollPane) {
            scrollPane.setVisible(!scrollPane.isVisible());
            splitView.adjustDivider();
            ensureTimeScrollVisible();
            return scrollPane.isVisible();
        }
    }

    protected void ensureTimeScrollVisible() {
        if (ridmVelScrollPane.isVisible()) {
            ridmVelScrollPane.setHorizontalScrollBar(timeBar);
        } else if (ridmScrollPane.isVisible()) {
            ridmScrollPane.setHorizontalScrollBar(timeBar);
        } else if (pitchVelScrollPane.isVisible()) {
            pitchVelScrollPane.setHorizontalScrollBar(timeBar);
        } else if (pitchScrollPane.isVisible()) {
            pitchScrollPane.setHorizontalScrollBar(timeBar);
        }
    }

    /**
     * ToolBar inner class
     */
    private class ToolBar extends TootBar {

        public ToolBar() {
            super(getSequence().getName());
            add(new GridLeftBar());
            add(new TimeZoomBar());
            add(new TrackBar());
        }
    }
}
