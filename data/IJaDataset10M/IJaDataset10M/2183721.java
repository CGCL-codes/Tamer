package neuralmusic;

import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.core.AudioProcess;
import com.frinika.global.FrinikaConfig;

public class OscillatorNode implements AudioProcess {

    double freq1;

    double freq2;

    double amp1;

    double amp2;

    double phaseRef;

    double phase;

    double dphase1;

    double dphase2;

    public boolean active = false;

    boolean steady = false;

    static final double twoPI = Math.PI * 2.0;

    void start(double freq, double amp, double phaseRef) {
        this.amp1 = 0.0;
        this.amp2 = amp;
        this.freq1 = this.freq2 = freq;
        this.dphase1 = this.dphase2 = 2.0 * Math.PI * freq / FrinikaConfig.sampleRate;
        phase = phaseRef;
        if (phase < 0) phase += twoPI;
        active = true;
    }

    public OscillatorNode() {
        active = false;
        steady = false;
    }

    public void close() {
    }

    public double getAmp() {
        return amp2;
    }

    public double getFreq() {
        return freq2;
    }

    public void open() {
    }

    public int processAudio(AudioBuffer buffer) {
        float buff[] = buffer.getChannel(0);
        int n = buffer.getSampleCount();
        if (steady) {
            for (int i = 0; i < n; i++) {
                phase += dphase2;
                if (phase >= twoPI) {
                    phase -= twoPI;
                }
                buff[i] += amp2 * FloatSinTable.sinFast(phase);
            }
        } else {
            double ddphase = (dphase2 - dphase1) / n;
            double ddamp = (amp2 - amp1) / n;
            for (int i = 0; i < n; i++) {
                phase += dphase1;
                dphase1 += ddphase;
                if (phase >= twoPI) {
                    phase -= twoPI;
                }
                buff[i] += amp1 * FloatSinTable.sinFast(phase);
                amp1 += ddamp;
            }
        }
        steady = true;
        active = amp2 != 0;
        return AUDIO_OK;
    }

    void setNext(double freq, double amp, double phaseRef) {
        this.amp2 = amp;
        this.freq2 = freq;
        this.dphase2 = 2.0 * Math.PI * freq2 / FrinikaConfig.sampleRate;
        steady = false;
        active = true;
    }

    @Override
    public String toString() {
        return "f:" + freq2 + "  a:" + amp2;
    }

    public boolean active() {
        return active;
    }

    public void silence() {
        this.amp2 = 0;
        steady = false;
        active = true;
    }
}
