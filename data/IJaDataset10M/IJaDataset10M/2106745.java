package ignaciohernandez.standards;

import java.util.ArrayList;
import java.util.List;
import emgeno.core.HorizontalLine;
import emgeno.core.SoundGroup;
import emgeno.music.Score;
import emgeno.music.Silence;
import emgeno.utils.WavFunctions;
import emgeno.wav.WavFactory;

public class ScoreSerieToAudio {

    public static void main(String[] args) throws Exception {
        String sInPath = "src\\ignaciohernandez\\standards\\";
        String sOutPath = "..\\emgenoMusic\\standards\\";
        double[] dd = { 1.4d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d, 2d };
        List<String> l = new ArrayList<String>();
        for (int i = 1; i <= 11; ++i) {
            String sInt = String.valueOf(i);
            if (sInt.length() == 1) sInt = "0" + sInt;
            Score s = new Score(sInPath + "Music" + sInt + ".txt");
            List<String> ss = s.validate();
            if (ss.size() > 0) {
                for (String s1 : ss) {
                    System.out.println(s1);
                }
            } else {
                SoundGroup s1 = new SoundGroup();
                double d = 1.4d;
                s1.addElement(s.getSound(), new HorizontalLine(d), 0, s.getSound().getLength());
                WavFactory.doWavFile(s1, sOutPath + "music" + sInt + ".wav");
                l.add(sOutPath + "music" + sInt + ".wav");
                l.add(sOutPath + "silence.wav");
            }
        }
        Silence sl = new Silence(2000d);
        WavFactory.doWavFile(sl, sOutPath + "silence.wav");
        WavFunctions.concatenate(l, sOutPath + "cancionesparapiano.wav");
    }
}
