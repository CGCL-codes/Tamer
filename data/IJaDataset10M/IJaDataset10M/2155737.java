package ignaciohernandez.binaryseries;

import emgeno.core.GetSampleException;
import emgeno.core.ISoundSource;
import emgeno.music.SampledSound;
import emgeno.wav.WavSample;

public class Draft9 {

    public static void main(String[] args) throws Exception {
        Draft8.create(new CombinedSoundSource(), 12, 0, Factory.ALL, Factory.NOLINKED, Factory.HORIZONTAL, "1111111", "", "..\\emgenoMusic\\binaryseries\\draft9.5.combined.pianoC2.bass");
    }

    public static class CombinedSoundSource implements ISoundSource {

        double lastms = 0;

        ISoundSource issa = new SampledSound(new WavSample("..\\emgenoMusic\\binaryseries\\sound.pianoC2.wav", 200, 5000), 0.95, 0.6);

        ISoundSource issb = new SampledSound(new WavSample("..\\emgenoMusic\\binaryseries\\sound.bass.wav", 200, 5000), 0.95, 0.6);

        ISoundSource issc = issa;

        public double getLength() {
            return issc.getLength();
        }

        public double[] getSample(double ms) throws GetSampleException {
            if (ms < lastms) {
                if (issc == issa) {
                    issc = issb;
                } else {
                    issc = issa;
                }
            }
            lastms = ms;
            return issc.getSample(ms);
        }
    }
}
