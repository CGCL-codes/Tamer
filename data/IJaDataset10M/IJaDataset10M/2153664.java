package uk.org.toot.swingui.midiui.sequenceui;

import java.awt.Point;
import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import uk.org.toot.midi.sequence.MidiSequence;
import uk.org.toot.midi.sequence.MidiTrack;
import uk.org.toot.midi.sequence.SequencePosition;
import uk.org.toot.midi.sequence.MidiNote;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.FlowLayout;
import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import uk.org.toot.midi.sequence.NameEvent;
import uk.org.toot.midi.sequence.edit.Selection;
import uk.org.toot.midi.sequence.edit.SequenceSelection;
import uk.org.toot.midi.sequence.edit.TrackSelection;
import uk.org.toot.swingui.miscui.ClickAdapter;
import uk.org.toot.swingui.midiui.MidiColor;
import java.util.Iterator;
import java.util.List;
import java.awt.Color;
import uk.org.toot.swingui.miscui.DynamicPopup;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiEvent;
import static uk.org.toot.midi.message.NoteMsg.*;

public abstract class SequenceView extends JPanel {

    /**
     * @supplierCardinality 1 
     * @link aggregation
     * @label viewer
     */
    protected Viewer viewer;

    private boolean isZoomable = true;

    protected boolean paintGrid = true;

    protected boolean paintMarkers = true;

    private MidiTrack topTrack;

    public SequenceView(Viewer viewer, boolean isZoomable) {
        super(new FlowLayout(FlowLayout.LEADING));
        this.viewer = viewer;
        this.isZoomable = isZoomable;
        checkTopTrack();
        addMouseListener(new ClickAdapter(createPopupMenu()));
        if (!isVelocity() && viewer instanceof Editor) {
            final Editor editor = (Editor) viewer;
            addMouseListener(editor.getToolPalette());
            addMouseMotionListener(editor.getToolPalette());
            editor.getToolPalette().getPropertyChangeSupport().addPropertyChangeListener("currentTool", new PropertyChangeListener() {

                public void propertyChange(PropertyChangeEvent change) {
                    ToolPalette palette = (ToolPalette) change.getSource();
                    PaletteTool tool = palette.getCurrentTool();
                    setCursor(tool.getCursor());
                }
            });
        }
        ChangeListener listener = new ChangeListener() {

            public void stateChanged(ChangeEvent ce) {
                repaint();
            }
        };
        getSelection().getChangeSupport().addChangeListener(listener);
        getSequence().getChangeSupport().addChangeListener(listener);
    }

    protected void checkTopTrack() {
        MidiTrack[] tracks = viewer.getSequence().getMidiTracks();
        for (int i = 1; i < tracks.length; i++) {
            if (isValid(tracks[i])) {
                topTrack = tracks[i];
                return;
            }
        }
    }

    public MidiSequence getSequence() {
        return viewer.getSequence();
    }

    public int getResolution() {
        return viewer.getSequence().getResolution();
    }

    protected void paintBackground(Graphics g) {
        Rectangle drawHere = g.getClipBounds();
        g.setColor(getBackground());
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
    }

    protected void paintComponent(Graphics g) {
        paintBackground(g);
        if (paintGrid) {
            paintGrid(g);
        }
        paintSequence(g);
    }

    protected abstract void paintGrid(Graphics g);

    protected void paintSequence(Graphics g) {
        MidiTrack[] tracks = getSequence().getMidiTracks();
        int ntracks = tracks.length;
        MidiTrack top = null;
        for (int t = 0; t < ntracks; t++) {
            MidiTrack track = tracks[t];
            if (!isVisibleTrack(track)) continue;
            if (t == 0 || track.isMarkerTrack() || isValid(track)) {
                if (track == topTrack) continue;
                paintTrack(g, track);
                top = track;
            }
        }
        if (topTrack != null) {
            if (isVisibleTrack(topTrack) && (isValid(topTrack) || topTrack.isMarkerTrack())) {
                paintTrack(g, topTrack);
            }
        } else if (top != null) {
            setTopTrack(top);
        }
        paintSelection(g, getSelection(), false, 0L, 0);
    }

    protected abstract void paintTrack(Graphics g, MidiTrack track);

    protected abstract void paintNote(Graphics g, int note, int velocity, long onTick, long offTick, boolean bOutline);

    public void paintSelection(Graphics g, Selection selection, boolean bOutline, long tickOffset, int valOffset) {
        if (selection instanceof SequenceSelection) paintSelection(g, (SequenceSelection) selection, bOutline, tickOffset, valOffset); else if (selection instanceof TrackSelection) paintSelection(g, (TrackSelection) selection, bOutline, tickOffset, valOffset);
    }

    public void paintSelection(Graphics g, TrackSelection trackSel, boolean bOutline, long tickOffset, int valOffset) {
        if (trackSel.size() <= 0) return;
        float hue = (Float) trackSel.getTrack().getClientProperty("Hue");
        Iterator notes = trackSel.iterator();
        while (notes.hasNext()) {
            MidiNote note = (MidiNote) notes.next();
            int noteVal = note.getNoteValue();
            int velocity = note.getVelocity();
            g.setColor(MidiColor.asHSB(hue, 1.0f, 0.50f));
            paintNote(g, noteVal + valOffset, velocity, note.on.getTick() + tickOffset, note.off.getTick() + tickOffset, bOutline);
        }
    }

    protected void paintSelection(Graphics g, SequenceSelection selection, boolean bOutline, long tickOffset, int valOffset) {
        Rectangle drawRect = g.getClipBounds();
        if (drawRect == null) drawRect = this.getBounds();
        if (!selection.isEmpty()) {
            if (bOutline) g.setXORMode(Color.black);
            TrackSelection[] trackSels = selection.getTracks();
            for (int t = 0; t < trackSels.length; t++) {
                TrackSelection trackSel = trackSels[t];
                MidiTrack track = trackSel.getTrack();
                if (!isVisibleTrack(track) || !isValid(track)) continue;
                paintSelection(g, trackSel, bOutline, tickOffset, valOffset);
            }
            g.setPaintMode();
        }
    }

    public Point drawSelectionArea(int x1, int y1, int x2, int y2, Point drag) {
        drag.x = x2;
        drag.y = y2;
        return drag;
    }

    public Point selectionMoved(Selection sel, int x1, int y1, int x2, int y2, Point drag) {
        Graphics g = getGraphics();
        g.setXORMode(Color.black);
        g.setColor(Color.white);
        long tickOffset;
        int valOffset;
        tickOffset = tick(drag.x, drag.y) - tick(x1, y1);
        valOffset = note(drag.x, drag.y) - note(x1, y1);
        paintSelection(g, sel, true, snap(tickOffset), valOffset);
        drag.x = x2;
        drag.y = y2;
        tickOffset = tick(drag.x, drag.y) - tick(x1, y1);
        valOffset = note(drag.x, drag.y) - note(x1, y1);
        paintSelection(g, sel, true, snap(tickOffset), valOffset);
        g.dispose();
        return drag;
    }

    /**
     * default rectangular matcher, override for other geometries
     * !!! UNIFY with selectionMoved
     */
    public List<MidiNote> getMatches(MidiTrack track, int x1, int y1, int x2, int y2) {
        NormalisedDualPoint n = new NormalisedDualPoint(x1, y1, x2, y2);
        return track.getMatches(tick(n.left, n.top), note(n.left, n.top), tick(n.right, n.bottom), note(n.right, n.bottom));
    }

    public boolean isVisibleTrack(MidiTrack track) {
        return viewer.isVisibleTrack(track);
    }

    public void setVisibleTrack(MidiTrack track, boolean visible) {
        viewer.setVisibleTrack(track, visible);
    }

    protected abstract boolean isValid(MidiTrack track);

    protected abstract int note(int x, int y);

    protected abstract long tick(int x, int y);

    public abstract String noteName(int note);

    protected abstract void scrollToVisible(long tick);

    protected float saturation(int value) {
        return (float) (0.1 + 0.9 * (float) value / 128);
    }

    protected JPopupMenu createPopupMenu() {
        return new TimePopup();
    }

    public SequenceSelection getSelection() {
        return viewer.getSelection();
    }

    public void setSelection(SequenceSelection sel) {
        viewer.setSelection(sel);
    }

    public Iterator positionIterator(long tick) {
        return viewer.positionIterator(tick);
    }

    public MidiTrack getTopTrack() {
        return topTrack;
    }

    public void setTopTrack(MidiTrack topTrack) {
        MidiTrack oldTopTrack = this.topTrack;
        if (oldTopTrack != topTrack) {
            this.topTrack = topTrack;
            firePropertyChange("topTrack", oldTopTrack, topTrack);
            repaint();
        }
    }

    private int defaultVelocity = 64;

    private int snap = SequencePosition.SNAP_16;

    protected Color metaColor = Color.blue.darker();

    public int getDefaultVelocity() {
        return defaultVelocity;
    }

    public void setDefaultVelocity(int v) {
        defaultVelocity = v;
    }

    public void setSnap(int snap) {
        this.snap = snap;
    }

    public int getSnap() {
        return snap;
    }

    /**
     * if value is -1, match nearest note at the specified tick
     */
    public MidiNote match(MidiTrack t, long tick, int value, boolean bvelocity) {
        for (int i = 0; i < t.size(); i++) {
            MidiEvent ev = t.get(i);
            if (ev.getTick() > (tick + getTolerance())) break;
            MidiMessage msg = ev.getMessage();
            if (isNote(msg)) {
                if (isOn(msg)) {
                    int note = getPitch(msg);
                    int velocity = getVelocity(msg);
                    if (value != -1 && value != (bvelocity ? velocity : note)) {
                        continue;
                    }
                    for (int j = 1 + i; j < t.size(); j++) {
                        MidiEvent event = t.get(j);
                        MidiMessage m = event.getMessage();
                        if (!(isNote(m))) continue;
                        if (!isOn(m) && !isOff(m)) continue;
                        if (getPitch(m) != note) continue;
                        if (event.getTick() < (tick - getTolerance())) break;
                        return new MidiNote(ev, event);
                    }
                }
            }
        }
        return null;
    }

    public MidiTrack match(long tick, int value) {
        MidiTrack[] tracks = getSequence().getMidiTracks();
        MidiTrack topTrack = getTopTrack();
        if (topTrack != null) {
            if (match(topTrack, tick, value, isVelocity()) != null) return topTrack;
        }
        for (int t = tracks.length - 1; t > 0; t--) {
            if (tracks[t] == topTrack) continue;
            if (match(tracks[t], tick, value, isVelocity()) != null) return tracks[t];
        }
        return null;
    }

    public long snap(long tick) {
        tick = SequencePosition.bodgeTick(tick, getSnap(), getSequence().getResolution());
        SequencePosition pos = getSequence().getPosition(tick);
        pos.snap(getSnap());
        return getSequence().getTick(pos);
    }

    protected boolean isVelocity() {
        return false;
    }

    public int getTolerance() {
        return 0;
    }

    public class TimePopup extends DynamicPopup {

        private JMenu viewMenu;

        private JMenu timeMenu;

        public TimePopup() {
            viewMenu = new JMenu("View");
            timeMenu = new JMenu("Time");
            add(viewMenu);
            add(timeMenu);
        }

        protected void fillViewMenu() {
            viewMenu.removeAll();
            viewMenu.add(new PaintGridItem());
            viewMenu.add(new PaintMarkersItem());
            viewMenu.addSeparator();
            MidiTrack[] tracks = getSequence().getMidiTracks();
            for (int t = 0; t < tracks.length; t++) {
                if (!SequenceView.this.isValid(tracks[t])) continue;
                viewMenu.add(new VisibleTrackItem(t));
            }
        }

        protected void fillTimeMenu() {
            timeMenu.removeAll();
            if (SequenceView.this.isZoomable) {
                timeMenu.add(new JMenuItem("Zoom In"));
                timeMenu.add(new JMenuItem("Zoom Out"));
                timeMenu.addSeparator();
            }
            NameEvent[] markers = getSequence().getMarkers();
            for (int m = 0; m < markers.length; m++) {
                timeMenu.add(new GotoItem(markers[m]));
            }
        }

        protected void refreshMenu() {
            fillViewMenu();
            fillTimeMenu();
        }

        private class VisibleTrackItem extends JCheckBoxMenuItem implements ActionListener {

            private MidiTrack track;

            public VisibleTrackItem(int trk) {
                super(getSequence().getMidiTrack(trk).getTrackName());
                track = getSequence().getMidiTrack(trk);
                setState(isVisibleTrack(track));
                MidiTrack track = getSequence().getMidiTrack(trk);
                if (track.getChannel() >= 0) {
                    setBackground(MidiColor.asHSB((Float) track.getClientProperty("Hue"), 0.342f, 1.0f));
                    addActionListener(this);
                }
            }

            public void actionPerformed(ActionEvent e) {
                setVisibleTrack(track, getState());
                SequenceView.this.repaint();
            }
        }

        private class PaintGridItem extends JCheckBoxMenuItem implements ActionListener {

            public PaintGridItem() {
                super("Grid", SequenceView.this.paintGrid);
                addActionListener(this);
            }

            public void actionPerformed(ActionEvent e) {
                SequenceView.this.paintGrid = isSelected();
                SequenceView.this.repaint();
            }
        }

        private class PaintMarkersItem extends JCheckBoxMenuItem implements ActionListener {

            public PaintMarkersItem() {
                super("Markers", SequenceView.this.paintMarkers);
                addActionListener(this);
            }

            public void actionPerformed(ActionEvent e) {
                SequenceView.this.paintMarkers = isSelected();
                SequenceView.this.repaint();
            }
        }

        private class GotoItem extends JMenuItem implements ActionListener {

            private NameEvent event;

            public GotoItem(NameEvent event) {
                super(event.getName());
                this.event = event;
                addActionListener(this);
            }

            public void actionPerformed(ActionEvent e) {
                SequenceView.this.scrollToVisible(event.getTick());
            }
        }
    }
}
