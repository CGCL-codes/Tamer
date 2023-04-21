package blue.soundObject.editor.pianoRoll;

import java.awt.Color;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import blue.soundObject.PianoRoll;
import blue.soundObject.pianoRoll.PianoNote;
import blue.soundObject.pianoRoll.Scale;

/**
 * @author steven
 * 
 */
public class PianoNoteView extends JPanel implements PropertyChangeListener {

    private static final int OCTAVES = 16;

    private static Border NORMAL_BORDER = BorderFactory.createBevelBorder(BevelBorder.RAISED);

    private static Color NORMAL_COLOR = Color.GRAY;

    private static Color SELECTED_COLOR = Color.WHITE;

    private PianoNote note;

    private PianoRoll p;

    public PianoNoteView(PianoNote note, PianoRoll p) {
        this.note = note;
        this.p = p;
        this.setBackground(NORMAL_COLOR);
        this.setBorder(NORMAL_BORDER);
        this.setOpaque(true);
        updatePropertiesFromNote();
        note.addPropertyChangeListener(this);
        this.addHierarchyListener(new HierarchyListener() {

            public void hierarchyChanged(HierarchyEvent e) {
                if (getParent() == null) {
                    removeAsListener();
                }
            }
        });
    }

    public void removeAsListener() {
        note.removePropertyChangeListener(this);
    }

    public PianoNote getPianoNote() {
        return this.note;
    }

    public void updateNoteStartFromLocation() {
        int pixelSecond = p.getPixelSecond();
        int scaleDegrees;
        int total;
        int yVal;
        if (p.getPchGenerationMethod() == PianoRoll.GENERATE_MIDI) {
            scaleDegrees = 12;
            total = 128 * p.getNoteHeight();
            yVal = total - this.getY();
        } else {
            scaleDegrees = p.getScale().getNumScaleDegrees();
            total = scaleDegrees * p.getNoteHeight() * OCTAVES;
            yVal = total - this.getY();
        }
        int layerIndex = (yVal / p.getNoteHeight()) - 1;
        int oct = layerIndex / scaleDegrees;
        int pch = layerIndex % scaleDegrees;
        float start = this.getX() / (float) pixelSecond;
        this.note.setStart(start);
        this.note.setOctave(oct);
        this.note.setScaleDegree(pch);
    }

    private void updatePropertiesFromNote() {
        int pixelSecond = p.getPixelSecond();
        int x = (int) (note.getStart() * pixelSecond);
        int y;
        if (p.getPchGenerationMethod() == PianoRoll.GENERATE_MIDI) {
            y = getMIDIY(note.getOctave(), note.getScaleDegree());
        } else {
            y = getScaleY(p.getScale(), note.getOctave(), note.getScaleDegree());
        }
        int w = (int) (note.getDuration() * pixelSecond);
        this.setLocation(x, y);
        this.setSize(w, p.getNoteHeight());
    }

    private int getMIDIY(int octave, int scaleDegree) {
        int scaleDegrees = 12;
        int noteHeight = p.getNoteHeight();
        int totalHeight = noteHeight * 128;
        int yVal = ((octave * scaleDegrees) + scaleDegree + 1) * noteHeight;
        return (totalHeight - yVal);
    }

    private int getScaleY(Scale scale, int octave, int scaleDegree) {
        int scaleDegrees = scale.getNumScaleDegrees();
        int noteHeight = p.getNoteHeight();
        int totalHeight = noteHeight * scaleDegrees * OCTAVES;
        int yVal = ((octave * scaleDegrees) + scaleDegree + 1) * noteHeight;
        return (totalHeight - yVal);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() == note) {
            updatePropertiesFromNote();
        }
    }

    /**
     * 
     */
    public void setSelected(boolean selected) {
        if (selected) {
            setBackground(SELECTED_COLOR);
        } else {
            setBackground(NORMAL_COLOR);
        }
    }

    /**
     * 
     */
    public void updateNoteDurFromWidth() {
        int pixelSecond = p.getPixelSecond();
        this.note.setDuration((float) this.getWidth() / pixelSecond);
    }
}
