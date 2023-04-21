package com.lemu.leco.play;

import java.util.ArrayList;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import com.lemu.music.jmjs.JmJsUtil;
import jm.JMC;
import jm.midi.MidiSynth;
import jm.music.data.Score;
import ren.gui.ParameterMap;
import ren.gui.seqEdit.BaseBeatTracker;
import ren.gui.seqEdit.LPlayerBeatTracker;
import ren.music.Player.Playable;
import ren.music.Player.PrePostPlayerEventListener;

/**
 * Class for LeCo playback using JavaSound
 * 
 * @author Rene Wooller
 * 
 */
public class JSPlayer implements Playable, UserModifiedBeatListener, MetaEventListener {

    private JmJsUtil jmjs = new JmJsUtil();

    private Sequencer sequencer;

    private Score score = new Score();

    private ArrayList<PrePostPlayerEventListener> prepoplavlis = new ArrayList<PrePostPlayerEventListener>(10);

    private BaseBeatTracker beatTracker;

    private float masterTempo = 120;

    private double startPos = 0.0;

    public JSPlayer() {
        super();
        initSequencer();
        beatTracker = new LPlayerBeatTracker();
        beatTracker.construct(new ParameterMap().construct(32, 16777216, 512, "scope"));
        beatTracker.addUserModBeatListener(this);
        sequencer.addMetaEventListener(this);
    }

    public void initSequencer() {
        try {
            if (MidiSystem.getSequencer() == null) {
                System.err.println("MidiSystem Sequencer Unavailable");
            } else {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void play(Sequence seq) throws InvalidMidiDataException {
        if (sequencer.isRunning()) {
            sequencer.stop();
        }
        sequencer.setSequence(seq);
        sequencer.setTempoInBPM(masterTempo);
        sequencer.start();
    }

    public void go() {
        firePreGoEvent();
        try {
            if (sequencer.getSequence() == null || sequencer.getSequence().getTickLength() <= 0) {
                play(jmjs.scoreToSeq(score));
            } else {
                sequencer.start();
            }
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    protected void firePreGoEvent() {
        for (PrePostPlayerEventListener p : prepoplavlis) {
            p.preGoEventTriggered(this);
        }
    }

    protected void firePostStopEvent() {
        for (PrePostPlayerEventListener p : prepoplavlis) {
            p.postStopEventTriggered();
        }
    }

    public boolean isPlaying() {
        return this.sequencer.isRunning();
    }

    public void stop() {
        if (this.sequencer.isRunning()) this.sequencer.stop();
        startPos = 0;
        this.sequencer.setTickPosition(jmjs.beatToTick(startPos));
        beatTracker.setBeat(startPos);
        firePostStopEvent();
    }

    public void pause() {
        sequencer.stop();
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public void addPrePostPlayerEventListener(PrePostPlayerEventListener pregoli) {
        this.prepoplavlis.add(pregoli);
    }

    public BaseBeatTracker getBeatTracker() {
        return beatTracker;
    }

    /**
	 * Purely for user modified beat information
	 * 
	 * @param newBeat
	 */
    public void beatFired(double newBeat) {
        sequencer.setTickPosition(jmjs.beatToTick(newBeat));
    }

    public void setBeatTracker(BaseBeatTracker bt) {
        this.beatTracker = bt;
    }

    public void meta(MetaMessage metaEvent) {
        switch(metaEvent.getType()) {
            case 70:
                beatTracker.increment();
                break;
            case JmJsUtil.StopType:
                stop();
                break;
        }
    }
}
