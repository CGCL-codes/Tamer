package geovista.sound;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.EventListenerList;

public class SonicRampPicker extends JPanel implements ComponentListener, ActionListener {

    public SonicRampSwatch[] panSet;

    private boolean[] anchored;

    private Color[] colors;

    private SonicRamp ramp;

    private int previousPlay = -1;

    private int nSwatches;

    public static final String COMMAND_SWATCH_COLOR_CHANGED = "color_changed";

    public static final String COMMAND_SWATCH_TEXTURE_CHANGED = "texture_changed";

    public static final int DEFAULT_NUM_SWATCHES = 8;

    public static final Color DEFAULT_LOW_COLOR = new Color(255, 255, 255);

    public static final Color DEFAULT_HIGH_COLOR_DARK = new Color(0, 0, 0);

    public static final int DEFAULT_LOW_KEY = 30;

    public static final int DEFAULT_HIGH_KEY = 90;

    public static final int X_AXIS = 0;

    public static final int Y_AXIS = 1;

    private transient int currOrientation = 0;

    private JComboBox insturmentCombo;

    private Synthesizer synthesizer;

    protected static final Logger logger = Logger.getLogger(SonicRampPicker.class.getName());

    private Instrument instruments[];

    static Soundbank sb;

    public SonicRampPicker() {
        init();
    }

    private void init() {
        openSoundbank();
        insturmentCombo = new JComboBox();
        insturmentCombo.addActionListener(this);
        insturmentCombo.setPreferredSize(new Dimension(80, 20));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        ramp = new SonicRamp();
        nSwatches = SonicRampPicker.DEFAULT_NUM_SWATCHES;
        colors = new Color[nSwatches];
        colors[0] = SonicRampPicker.DEFAULT_LOW_COLOR;
        colors[nSwatches - 1] = SonicRampPicker.DEFAULT_HIGH_COLOR_DARK;
        anchored = new boolean[nSwatches];
        makeRamp(nSwatches);
        rampSwatches();
        ramp.rampColors(colors, anchored);
        setPreferredSize(new Dimension(365, 20));
        addComponentListener(this);
        initSound();
    }

    private void initSound() {
        try {
            if (synthesizer == null) {
                if ((synthesizer = MidiSystem.getSynthesizer()) == null) {
                    logger.severe("getSynthesizer() failed!");
                    return;
                }
            }
            synthesizer.open();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        if (sb != null) {
            instruments = sb.getInstruments();
            synthesizer.loadInstrument(instruments[0]);
        }
        for (Instrument element : instruments) {
            insturmentCombo.addItem(element.getName());
        }
    }

    static void openSoundbank() {
        Class clazz = SonicRampPicker.class;
        try {
            sb = MidiSystem.getSoundbank(clazz.getResourceAsStream("resources/soundbank.gm"));
        } catch (InvalidMidiDataException e) {
            logger.throwing(clazz.getName(), "initSound", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == insturmentCombo) {
            for (SonicRampSwatch element : panSet) {
                element.setInstrument(insturmentCombo.getSelectedIndex());
            }
        }
    }

    public void makeRamp(int nSwatches) {
        int len = 0;
        if (colors != null) {
            len = colors.length;
            removeAll();
        }
        panSet = new SonicRampSwatch[nSwatches];
        for (int i = 0; i < panSet.length; i++) {
            if (i == 0) {
                panSet[i] = new SonicRampSwatch(this, true, true);
                panSet[i].setSwatchColor(getLowColor());
            } else if (i == nSwatches - 1) {
                panSet[i] = new SonicRampSwatch(this, true, true);
                panSet[i].setSwatchColor(getHighColor());
            } else {
                if (i < len - 1) {
                    boolean anch = anchored[i];
                    Color c = colors[i];
                    panSet[i] = new SonicRampSwatch(this, anch, false);
                    panSet[i].setSwatchColor(c);
                } else {
                    panSet[i] = new SonicRampSwatch(this, false, false);
                    panSet[i].setSwatchColor(Color.white);
                }
            }
            this.add(panSet[i]);
            this.add(insturmentCombo);
        }
        anchored = new boolean[nSwatches];
        colors = new Color[nSwatches];
    }

    public void rampSwatches() {
        if (panSet.length <= 0) {
            return;
        }
        for (int j = 0; j < panSet.length; j++) {
            colors[j] = panSet[j].getSwatchColor();
            if (panSet[j].getAnchored()) {
                anchored[j] = true;
            } else {
                anchored[j] = false;
            }
        }
        ramp.rampColors(colors, anchored);
        for (int j = 0; j < panSet.length; j++) {
            panSet[j].setSwatchColor(colors[j]);
            int numPans = panSet.length;
            int panStep = (SonicRampPicker.DEFAULT_HIGH_KEY - SonicRampPicker.DEFAULT_LOW_KEY) / (numPans - 1);
            int panKey = j * panStep + SonicRampPicker.DEFAULT_LOW_KEY;
            panSet[j].setKeyNum(panKey);
            if (logger.isLoggable(Level.FINEST)) {
                logger.finest("penKey = " + panKey);
            }
        }
    }

    public void swatchChanged() {
        rampSwatches();
        fireActionPerformed(SonicRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
    }

    private void changeOrientation(int orientation) {
        if (orientation == currOrientation) {
            return;
        } else if (orientation == SonicRampPicker.X_AXIS) {
            Component[] comps = new Component[getComponentCount()];
            int counter = 0;
            for (int i = getComponentCount() - 1; i > -1; i--) {
                comps[counter] = getComponent(i);
                counter++;
            }
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            for (Component element : comps) {
                this.add(element);
            }
            currOrientation = SonicRampPicker.X_AXIS;
            revalidate();
        } else if (orientation == SonicRampPicker.Y_AXIS) {
            Component[] comps = new Component[getComponentCount()];
            for (int i = 0; i < getComponentCount(); i++) {
                comps[i] = getComponent(i);
            }
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            for (int i = getComponentCount() - 1; i > -1; i--) {
                this.add(comps[i]);
            }
            currOrientation = SonicRampPicker.Y_AXIS;
            revalidate();
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        float ratio = ((float) getWidth() / (float) getHeight());
        if (ratio >= 1 && currOrientation == SonicRampPicker.Y_AXIS) {
            changeOrientation(SonicRampPicker.X_AXIS);
        }
        if (ratio < 1 && currOrientation == SonicRampPicker.X_AXIS) {
            changeOrientation(SonicRampPicker.Y_AXIS);
        }
    }

    public void setPanSet(SonicRampSwatch[] panSet) {
        this.panSet = panSet;
    }

    public SonicRampSwatch[] getPanSet() {
        return panSet;
    }

    public void setAnchored(boolean[] anchored) {
        this.anchored = anchored;
    }

    public boolean[] getAnchored() {
        return anchored;
    }

    public void setColors(Color[] colors) {
        nSwatches = colors.length;
        this.colors = colors;
        anchored = new boolean[colors.length];
        for (int i = 0; i < colors.length; i++) {
            anchored[i] = true;
        }
        setLowColor(colors[0]);
        setHighColor(colors[colors.length - 1]);
        this.colors = colors;
        rampSwatches();
        this.repaint();
    }

    public Color[] getColors() {
        return colors;
    }

    public void playKey(int swatch) {
        if (previousPlay >= 0 && previousPlay < panSet.length) {
            SonicRampSwatch swat = panSet[previousPlay];
            swat.setNoteState(SonicRampSwatch.OFF);
            swat.off();
        }
        SonicRampSwatch swat = panSet[swatch];
        swat.setNoteState(SonicRampSwatch.ON);
        swat.on();
        previousPlay = swatch;
    }

    public void setRamp(SonicRamp ramp) {
        this.ramp = ramp;
    }

    public SonicRamp getRamp() {
        return ramp;
    }

    public void setLowColor(Color lowColor) {
        colors[0] = lowColor;
        panSet[0].setSwatchColor(lowColor);
        makeRamp(nSwatches);
        rampSwatches();
        this.repaint();
    }

    public Color getLowColor() {
        return colors[0];
    }

    public void setHighColor(Color highColor) {
        colors[colors.length - 1] = highColor;
        makeRamp(nSwatches);
        rampSwatches();
        this.repaint();
        fireActionPerformed(SonicRampPicker.COMMAND_SWATCH_COLOR_CHANGED);
    }

    public Color getHighColor() {
        return colors[colors.length - 1];
    }

    public void setNSwatches(int nSwatches) {
        this.nSwatches = nSwatches;
        makeRamp(nSwatches);
        rampSwatches();
        validate();
        this.repaint();
    }

    public int getNSwatches() {
        return nSwatches;
    }

    /**
	 * implements ActionListener
	 */
    public void addActionListener(ActionListener l) {
        listenerList.add(ActionListener.class, l);
    }

    /**
	 * removes an ActionListener from the component
	 */
    public void removeActionListener(ActionListener l) {
        listenerList.remove(ActionListener.class, l);
    }

    /**
	 * Notify all listeners that have registered interest for notification on
	 * this event type. The event instance is lazily created using the
	 * parameters passed into the fire method.
	 * 
	 * @see EventListenerList
	 */
    private void fireActionPerformed(String command) {
        Object[] listeners = listenerList.getListenerList();
        ActionEvent e = null;
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ActionListener.class) {
                if (e == null) {
                    e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, command);
                }
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
    }

    /**
	 * Stores MidiChannel information.
	 */
    class ChannelData {

        MidiChannel channel;

        boolean solo;

        boolean mono;

        boolean mute;

        boolean sustain;

        int velocity;

        int pressure;

        int bend;

        int reverb;

        int row;

        int col;

        int num;

        public ChannelData(MidiChannel channel, int num) {
            this.channel = channel;
            this.num = num;
            velocity = pressure = bend = reverb = 64;
        }
    }
}
