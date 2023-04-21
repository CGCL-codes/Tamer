package emgeno.utils;

import java.util.List;
import emgeno.core.SoundGroup;
import emgeno.wav.WavFactory;
import emgeno.wav.WavSample;

public class WavFunctions {

    private WavFunctions() {
    }

    public static void concatenate(List<String> wavfiles, String outputfile) throws Exception {
        SoundGroup sg = new SoundGroup();
        double ms = 0;
        for (String s : wavfiles) {
            WavSample ws = new WavSample(s);
            sg.addElement(ws, 1, ms, ms + ws.getLength());
            ms += ws.getLength();
        }
        WavFactory.doWavFile(sg, outputfile);
    }
}
