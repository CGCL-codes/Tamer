package musicseeder;

import musicseeder.analysis.*;
import musicseeder.generate.*;
import musicseeder.node.*;
import musicseeder.phases.*;
import java.util.*;
import javax.sound.midi.*;

public class Main {

    public static ASong song;

    public static void playSeq(Sequence seq) throws Exception {
        Synthesizer synth = MidiSystem.getSynthesizer();
        synth.open();
        Sequencer sequencer = MidiSystem.getSequencer();
        sequencer.setTempoInBPM(120);
        sequencer.getTransmitter().setReceiver(synth.getReceiver());
        sequencer.open();
        sequencer.setSequence(seq);
        sequencer.start();
        System.out.println(seq.getTickLength());
    }

    public static void main(String[] args) {
        try {
            long a;
            if (args.length > 0) {
                a = Long.parseLong(args[0]);
            } else {
                a = Util.random.nextLong();
            }
            Util.random = new Random(a);
            System.out.println("K�rer med seed: " + a);
            Song mySong = new musicseeder.generate.SimpleSong();
            song = mySong.generate();
            System.out.println(mySong.name());
            song.apply(PrettyPrint.aspectOf());
            song.apply(DoubleNotes.aspectOf());
            System.out.println("DoubleNotes");
            song.apply(PrettyPrint.aspectOf());
            song.apply(RythmicRepetition.aspectOf());
            System.out.println("RR");
            song.apply(PrettyPrint.aspectOf());
            song.apply(BindNotes.aspectOf());
            System.out.println("BindNotes");
            song.apply(PrettyPrint.aspectOf());
            song.apply(EmitChords.aspectOf());
            song.apply(EmitMelody.aspectOf());
            playSeq(song.sequence);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
