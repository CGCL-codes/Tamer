package jpianotrain.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jpianotrain.ApplicationContext;
import jpianotrain.Constants;
import jpianotrain.VersionInformation;
import jpianotrain.event.ApplicationContextListener;
import jpianotrain.event.NoteDispatcherListener;
import jpianotrain.event.TuneFactoryListener;
import jpianotrain.midi.MidiThread;
import jpianotrain.midi.NoteDispatcher;
import jpianotrain.staff.Note;
import jpianotrain.staff.TuneFactory;
import org.apache.log4j.Logger;

/**
 * Keyboard which can be played, auto adjusts to
 * current {@link TuneFactory} settings and displays
 * a guide.
 *
 * @since 0
 * @author methke01
 */
@VersionInformation(lastChanged = "$LastChangedDate: 2008-11-20 16:23:22 -0500 (Thu, 20 Nov 2008) $", authors = { "Alexander Methke" }, revision = "$LastChangedRevision: 26 $", lastEditor = "$LastChangedBy: onkobu $", id = "$Id")
public class JKeyBoard extends JPanel implements MouseListener, ApplicationContextListener, NoteDispatcherListener, TuneFactoryListener {

    private static final Logger log = Logger.getLogger(JKeyBoard.class);

    /**
	 * Creates a new instance of JKeyBoard
	 *
	 * @param range Octave range.
	 */
    public JKeyBoard(final int range) {
        setSize(500, 50);
        setMinimumSize(getSize());
        setPreferredSize(getSize());
        setBackground(Color.white);
        setOctaveRange(range);
        initComponent();
        registerTuneFactory();
        NoteDispatcher.getInstance().addListener(this);
    }

    /**
	 * Sets new {@link #setHighest(int) highest} and
	 * {@link #setLowest(int) lowest} note for this
	 * keyboard.
	 * 
	 * @param r If even the resulting borders are
	 * half of r below and half of r above middle
	 * C ({@link Constants#MIDI_MIDDLE_C}). If odd
	 * incremented by 1 and same as even range. For
	 * example range is 2 you get 2 octaves, one
	 * below middle C and one above.
	 */
    public void setOctaveRange(final int r) {
        int calcRange = r;
        if (r % 2 == 1) {
            calcRange++;
        }
        calcRange /= 2;
        int diff = Constants.NOTES_PER_OCTAVE * calcRange;
        setLowest(Constants.MIDI_MIDDLE_C - diff);
        setHighest(Constants.MIDI_MIDDLE_C + diff);
    }

    public int getOctaveRange() {
        log.debug("lowest: " + getLowest());
        log.debug("highest: " + getHighest());
        log.debug("octave range: " + (getHighest() - getLowest()) / Constants.NOTES_PER_OCTAVE);
        return (getHighest() - getLowest()) / Constants.NOTES_PER_OCTAVE;
    }

    private void initComponent() {
        setLayout(null);
        removeAll();
        keyMap = new HashMap<Integer, JPianoKey>();
        int octaveRange = getOctaveRange();
        int spacePerOctave = getWidth() / octaveRange;
        int spacePerKey = spacePerOctave / 7;
        int keyHeight = getHeight();
        int halfKeyHeight = (int) Math.round(keyHeight * 0.75);
        int halfKeyOfs = (int) Math.round(spacePerKey * 0.7);
        int halfKeyWidth = (int) Math.round(spacePerKey * 0.6);
        int noteOffset = getLowest();
        if (noteOffset % Constants.NOTES_PER_OCTAVE != 0) {
            noteOffset = Constants.MIDI_MIDDLE_C - ((octaveRange / 2) * Constants.NOTES_PER_OCTAVE);
        }
        for (int i = 0; i < octaveRange; i++) {
            for (int j = 0; j < 6; j++) {
                if (j == 2) {
                    continue;
                }
                int keyOffset = 1;
                switch(j) {
                    case 0:
                        keyOffset = 1;
                        break;
                    case 1:
                        keyOffset = 3;
                        break;
                    case 3:
                        keyOffset = 6;
                        break;
                    case 4:
                        keyOffset = 8;
                        break;
                    case 5:
                        keyOffset = 10;
                        break;
                }
                JPianoKey k = new JPianoKey(i * 12 + keyOffset + noteOffset, i * 7 + j, false);
                k.setBounds(i * spacePerOctave + j * spacePerKey + halfKeyOfs, 0, halfKeyWidth, halfKeyHeight);
                k.addMouseListener(this);
                add(k);
                keyMap.put(k.getKeyNumber(), k);
            }
            for (int j = 0; j < 7; j++) {
                int keyOffset = 1;
                switch(j) {
                    case 0:
                        keyOffset = 0;
                        break;
                    case 1:
                        keyOffset = 2;
                        break;
                    case 2:
                        keyOffset = 4;
                        break;
                    case 3:
                        keyOffset = 5;
                        break;
                    case 4:
                        keyOffset = 7;
                        break;
                    case 5:
                        keyOffset = 9;
                        break;
                    case 6:
                        keyOffset = 11;
                        break;
                }
                int keyNumber = i * 12 + keyOffset + noteOffset;
                JPianoKey k = new JPianoKey(keyNumber, i * 7 + j, true);
                k.setBounds(i * spacePerOctave + j * spacePerKey, 0, spacePerKey, keyHeight);
                k.addMouseListener(this);
                keyMap.put(k.getKeyNumber(), k);
                add(k);
            }
        }
    }

    @Override
    public void doLayout() {
        Container c = getParent();
        if (c == null) {
            return;
        }
        Dimension d = new Dimension(c.getSize());
        d.height = d.height / 3;
        setSize(d);
        revalidate();
    }

    /**
	 *
	 */
    @Override
    public void revalidate() {
        log.debug("revalidating");
        if (getWidth() == 0 || getHeight() == 0 || getOctaveRange() == 0) {
            super.revalidate();
            return;
        }
        int octaveRange = getOctaveRange();
        int spacePerOctave = getWidth() / octaveRange;
        int spacePerKey = spacePerOctave / 7;
        int keyHeight = getHeight();
        int halfKeyHeight = (int) Math.round(keyHeight * 0.75);
        int halfKeyOfs = (int) Math.round(spacePerKey * 0.7);
        int halfKeyWidth = (int) Math.round(spacePerKey * 0.6);
        for (Component c : getComponents()) {
            if (c instanceof JPianoKey) {
                JPianoKey k = (JPianoKey) c;
                if (k.isWhite()) {
                    k.setBounds(k.getKeyOffset() * spacePerKey, 0, spacePerKey, keyHeight);
                } else {
                    k.setBounds(k.getKeyOffset() * spacePerKey + halfKeyOfs, 0, halfKeyWidth, halfKeyHeight);
                }
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        Component c = e.getComponent();
        if (!(c instanceof JPianoKey)) {
            return;
        }
        JPianoKey pk = (JPianoKey) c;
        pk.setPressed(true);
        int keyNum = pk.getKeyNumber();
        try {
            MidiThread.getInstance().emitNoteOn(keyNum);
        } catch (Exception ex) {
            pk.setPressed(false);
            JOptionPane.showMessageDialog(this, "Could not emit note via MIDI, try to configure first", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mouseReleased(MouseEvent e) {
        Component c = e.getComponent();
        if (!(c instanceof JPianoKey)) {
            return;
        }
        JPianoKey pk = (JPianoKey) c;
        pk.setPressed(false);
        int keyNum = pk.getKeyNumber();
        MidiThread.getInstance().emitNoteOff(keyNum);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void settingsChanged(TuneFactory tf) {
        setLowest(tf.getLowerBoundBass());
        setHighest(tf.getUpperBoundTreble());
        setOctaveRange((getHighest() - getLowest()) / 12);
        initComponent();
        repaint();
    }

    public void modeChanged() {
        registerTuneFactory();
        TuneFactory tf = null;
        switch(ApplicationContext.getInstance().getMode()) {
            case MIDI_FILES:
                {
                    throw new UnsupportedOperationException("mode is not implemented yet");
                }
            case RANDOM_NOTES:
                {
                    tf = TuneFactory.getRandomFactory();
                }
                break;
            case LESSONS:
                {
                    throw new UnsupportedOperationException("mode is not implemented yet");
                }
        }
        setLowest(tf.getLowerBoundBass());
        setHighest(tf.getUpperBoundTreble());
        setOctaveRange((getHighest() - getLowest()) / 12);
        initComponent();
        repaint();
    }

    public void startModeChanged() {
    }

    /**
	 * Doesn't do anything at all.
	 */
    public void spoolChanged() {
    }

    public void midiStatusChanged() {
    }

    public void hintColorChanged() {
        hintColor = ApplicationContext.getInstance().getKeyHintColor();
    }

    public void wrongColorChanged() {
        blinkColor = ApplicationContext.getInstance().getKeyWrongColor();
    }

    public void showHintsChanged() {
        showHints = ApplicationContext.getInstance().isShowHints();
    }

    public void showWrongKeyChanged() {
        showWrongKey = ApplicationContext.getInstance().isShowWrongKey();
    }

    /**
	 * Doesn't do anything.
	 */
    public void notePlayedRight(Note n) {
    }

    /**
	 * Maybe displays next note on keys.
	 * 
	 * @param n
	 */
    public void nextNoteRight(Note n) {
        if (!showHints) {
            return;
        }
        if (n.getPitch() < getLowest() || n.getPitch() > getHighest()) {
            return;
        }
        if (highlightedRight != null) {
            highlightedRight.setHighlighted(false);
        }
        highlightedRight = keyMap.get(n.getPitch());
        if (highlightedRight != null) {
            highlightedRight.setHighlighted(true);
        } else {
            log.error("could not find key for note " + n);
        }
    }

    /**
	 * Doesn't do anything.
	 */
    public void notePlayedLeft(Note n) {
    }

    /**
	 * Maybe displays next note on keys.
	 * 
	 * @param n
	 */
    public void nextNoteLeft(Note n) {
        if (!showHints) {
            return;
        }
        if (n.getPitch() < getLowest() || n.getPitch() > getHighest()) {
            return;
        }
        if (highlightedLeft != null) {
            highlightedLeft.setHighlighted(false);
        }
        highlightedLeft = keyMap.get(n.getPitch());
        if (highlightedLeft != null) {
            highlightedLeft.setHighlighted(true);
        } else {
            log.error("could not find key for note " + n);
        }
    }

    /**
	 * Highlights next keys on keyboard.
	 * 
	 * @param n
	 */
    public void notePlayedWrong(Note n) {
        if (!showWrongKey) {
            return;
        }
        JPianoKey key = keyMap.get(n.getPitch());
        if (key == null) {
            log.debug("Note out of range: " + n);
            return;
        }
        new BlinkThread(key).start();
    }

    public void setLowest(int lowest) {
        this.lowest = lowest;
    }

    public int getLowest() {
        return lowest;
    }

    public void setHighest(int highest) {
        this.highest = highest;
    }

    public int getHighest() {
        return highest;
    }

    public Color getHintColor() {
        return hintColor;
    }

    public void setHintColor(Color hintColor) {
        this.hintColor = hintColor;
    }

    public Color getBlinkColor() {
        return blinkColor;
    }

    public void setBlinkColor(Color blinkColor) {
        this.blinkColor = blinkColor;
    }

    private void registerTuneFactory() {
        if (oldFactory != null) {
            oldFactory.removeListener(this);
        }
        TuneFactory tf = null;
        switch(ApplicationContext.getInstance().getMode()) {
            case MIDI_FILES:
                {
                    throw new UnsupportedOperationException("mode is not implemented yet");
                }
            case RANDOM_NOTES:
                {
                    tf = TuneFactory.getRandomFactory();
                }
                break;
            case LESSONS:
                {
                    throw new UnsupportedOperationException("mode is not implemented yet");
                }
        }
        if (tf != null) {
            tf.addListener(this);
            oldFactory = tf;
        }
    }

    private static class BlinkThread extends Thread {

        public BlinkThread(JPianoKey key) {
            this.key = key;
        }

        public void run() {
            for (int i = 0; i < 3; i++) {
                key.setBlink(true);
                try {
                    sleep(500);
                } catch (Exception ex) {
                }
                key.setBlink(false);
                try {
                    sleep(500);
                } catch (Exception ex) {
                }
            }
        }

        private JPianoKey key;
    }

    private boolean showHints;

    private boolean showWrongKey;

    private int lowest;

    private int highest;

    private Color hintColor;

    private Color blinkColor;

    private JPianoKey highlightedRight;

    private JPianoKey highlightedLeft;

    private Map<Integer, JPianoKey> keyMap;

    private TuneFactory oldFactory;
}
