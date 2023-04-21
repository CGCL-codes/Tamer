package uk.org.toot.demo;

import java.io.File;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Soundbank;
import javax.swing.JFrame;
import uk.co.simphoney.music.virtualband.Score;
import uk.co.simphoney.music.virtualband.ScoreWithChordSheet;
import uk.co.simphoney.music.virtualband.VirtualBand;
import uk.org.toot.music.composition.RhythmicComposer;
import uk.org.toot.music.composition.Timing;
import uk.org.toot.music.composition.TonalComposer;
import uk.org.toot.music.tonality.Scales;
import com.frinika.audio.toot.FrinikaAudioSystem;
import com.frinika.chart.Chart;
import com.frinika.chart.ChartPanel;
import com.frinika.global.FrinikaConfig;
import com.frinika.project.MidiDeviceDescriptor;
import com.frinika.project.ProjectContainer;
import com.frinika.project.gui.ProjectFrame;
import com.frinika.sequencer.MidiResource;
import com.frinika.sequencer.gui.mixer.SynthWrapper;
import com.frinika.sequencer.model.MidiLane;
import com.frinika.sequencer.patchname.MyPatch;
import com.frinika.sequencer.patchname.PatchNameMap;

public class AutomatedBandDemo {

    public AutomatedBandDemo() {
        MidiDeviceDescriptor desc = null;
        File soundbankfilename = new File(FrinikaConfig.SOUNDFONT_DIRECTORY, "8MBGMFX.SF2");
        FrinikaAudioSystem.getAudioServer().start();
        ProjectContainer project = null;
        try {
            project = new ProjectContainer();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        SynthWrapper midiDevice;
        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                MidiDevice dev = MidiSystem.getMidiDevice(info);
                if (dev.getMaxReceivers() != 0) {
                    if (info.toString().equals("Gervill")) {
                        midiDevice = new SynthWrapper(project, dev);
                        try {
                            desc = project.addMidiOutDevice(midiDevice);
                            Soundbank soundbank = MidiSystem.getSoundbank((soundbankfilename));
                            midiDevice.loadAllInstruments(soundbank);
                        } catch (MidiUnavailableException e2) {
                            e2.printStackTrace();
                        }
                        break;
                    }
                    System.out.println(info);
                }
            } catch (Exception e) {
            }
        }
        VirtualBand band = new VirtualBand(project);
        MidiResource midiResource = project.getMidiResource();
        MidiLane bassLane = project.createMidiLane();
        int channel = 0;
        bassLane.setMidiChannel(channel);
        bassLane.setMidiDevice(desc.getMidiDevice());
        PatchNameMap patchMap = midiResource.getVoiceList(desc.getMidiDevice(), channel);
        List<MyPatch> patches = patchMap.getPatchesWithNamesLike("acoustic bass");
        bassLane.setProgram(patches.get(0));
        MidiLane pianoLaneLeft = project.createMidiLane();
        channel = 1;
        pianoLaneLeft.setMidiChannel(channel);
        pianoLaneLeft.setMidiDevice(desc.getMidiDevice());
        patches = patchMap.getPatchesWithNamesLike("piano");
        pianoLaneLeft.setProgram(patches.get(0));
        MidiLane pianoLaneRight = project.createMidiLane();
        channel = 2;
        pianoLaneRight.setMidiChannel(channel);
        pianoLaneRight.setMidiDevice(desc.getMidiDevice());
        patches = patchMap.getPatchesWithNamesLike("piano");
        pianoLaneRight.setProgram(patches.get(0));
        MidiLane drumLane = project.createMidiLane();
        channel = 9;
        drumLane.setMidiChannel(channel);
        drumLane.setMidiDevice(desc.getMidiDevice());
        ProjectFrame projectFrame;
        try {
            projectFrame = new ProjectFrame(project);
            projectFrame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Scales.LydianChromaticConcept.init();
        new TootBassPlayer(bassLane, band);
        new TootRightHandKeyboardPlayer(pianoLaneRight, band);
        new TootLeftHandKeyboardPlayer(pianoLaneLeft, band);
        new TootDrumKitPlayer(drumLane, band);
        Chart chart = new Chart();
        chart.setDefaultKey("C", "Major");
        chart.appendBar("Cmaj", 4);
        chart.appendBar("Am7", 4);
        chart.appendBar("Fmaj", 4);
        chart.appendBar("Dm / Cmaj /", 4);
        chart.appendBar("Am / / Fmaj", 4);
        chart.appendBar("G7", 4);
        ChartPanel panel = new ChartPanel(chart);
        JFrame frame = new JFrame();
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
        final Score score = new ScoreWithChordSheet(chart);
        score.addObserver(band);
        NoteUtil.setTicksPerBeat(16);
        band.interpret(score);
    }

    public static void main(String[] args) {
        new AutomatedBandDemo();
    }
}
