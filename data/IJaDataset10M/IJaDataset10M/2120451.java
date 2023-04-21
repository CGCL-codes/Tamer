package emgeno.examples;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import emgeno.core.HorizontalLine;
import emgeno.core.SoundGroup;
import emgeno.music.SampledSound;
import emgeno.utils.Refactor;
import emgeno.wav.WavFactory;
import emgeno.wav.WavSample;

public class Example6 {

    public static void main(String[] args) throws Exception {
        WavSample wavSample = new WavSample("..\\emgenoMusic\\examples\\sound.piano.wav");
        OutputStream out;
        SoundGroup s = new SoundGroup();
        SoundGroup s1 = new SoundGroup();
        SoundGroup s2 = new SoundGroup();
        s1.addElement(new SampledSound(wavSample, 0.5), new HorizontalLine(0.7), 0, 2000);
        s1.addElement(new SampledSound(wavSample, 0.6), new HorizontalLine(0.7), 0, 2000);
        s1.addElement(new SampledSound(wavSample, 0.7), new HorizontalLine(0.7), 1000, 2000);
        s1.addElement(new SampledSound(wavSample, 0.5), new HorizontalLine(0.7), 1500, 3000);
        s1.addElement(new SampledSound(wavSample, 0.6), new HorizontalLine(0.7), 2000, 4000);
        s1.addElement(new SampledSound(wavSample, 0.7), new HorizontalLine(0.7), 1900, 3500);
        s1.name = "s1";
        s1.location = "..\\emgenoMusic\\examples\\Example6_s1.xml";
        out = new FileOutputStream(new File("..\\emgenoMusic\\examples\\Example6_s1.xml"));
        out.write(s1.toString().getBytes());
        out.close();
        s2.addElement(new SampledSound(s1, 6), new HorizontalLine(1), 1, 1000);
        s2.name = "s2";
        s2.location = "..\\emgenoMusic\\examples\\Example6_s2.xml";
        out = new FileOutputStream(new File("..\\emgenoMusic\\examples\\Example6_s2.xml"));
        out.write(s2.toString().getBytes());
        out.close();
        s.addElement(s1, new HorizontalLine(1), 0, 4000);
        s.addElement(s1, new HorizontalLine(1), 4000, 8000);
        s.addElement(s2, new HorizontalLine(0.5), 3000, 3500);
        s.name = "Example6";
        out = new FileOutputStream(new File("..\\emgenoMusic\\examples\\Example6.xml"));
        out.write(s.toString().getBytes());
        out.close();
        Refactor r = new Refactor();
        SoundGroup sg = r.doSoundGroup(new File("..\\emgenoMusic\\examples\\Example6.xml"));
        WavFactory.doWavFile(sg, "..\\emgenoMusic\\examples\\Example6.wav");
    }
}
