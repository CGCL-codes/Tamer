package codesounding.jsyn;

import java.util.Arrays;
import java.util.List;
import codesounding.SilentProcessor;
import codesounding.jsyn.grains.GrainMixer;
import codesounding.jsyn.grains.GrainMixerTester;
import codesounding.jsyn.grains.GrainOnBus;
import com.softsynth.jsyn.LineOut;
import com.softsynth.jsyn.Synth;

public abstract class AbstractGrainProcessor extends SilentProcessor {

    private final int GRAIN_DURATION = 50;

    private final double[] DEFAULT_ENVELOPE = { (GRAIN_DURATION / 1000d) / 5, 0.8, (GRAIN_DURATION / 1000d) / 5, 0.8, (GRAIN_DURATION / 1000d) / 5, 0.8, (GRAIN_DURATION / 1000d) / 5, 0.8, (GRAIN_DURATION / 1000d) / 5, 0.0 };

    private final int MAX_SCALING = 300;

    private final int INSTRUCTIONS_TO_SKIP = 1000;

    private int instructionCounter = 0;

    protected ScalableGrain var, open, close, forr, iff, doo, whilee, returnn, breakk, continuee, throww;

    private List<ScalableGrain> allGrains;

    protected GrainMixer adder;

    private GrainMixerTester adderEditor;

    private SimpleFader skipFader;

    private SimpleFader scalingFader;

    protected LineOut lineOut;

    public AbstractGrainProcessor() {
        Synth.startEngine(0);
        init();
        startAll();
    }

    private void init() {
        adder = new GrainMixer(DEFAULT_ENVELOPE);
        lineOut = new LineOut();
        setOutput();
        setGrains();
        allGrains = Arrays.asList(new ScalableGrain[] { var, open, close, forr, iff, doo, whilee, returnn, breakk, continuee, throww });
        adderEditor = codesounding.jsyn.grains.GrainMixerTester.startEditor(adder, DEFAULT_ENVELOPE);
        skipFader = SimpleFader.openFader("Edit", "Instructions to skip", INSTRUCTIONS_TO_SKIP, 0, 10000);
        scalingFader = SimpleFader.openFader("Edit", "Max scaling", MAX_SCALING, 0, 10000);
        customFaders();
    }

    protected abstract void setOutput();

    protected abstract void setGrains();

    protected void customFaders() {
    }

    protected void startAll() {
        lineOut.start();
        adder.start();
    }

    public void getVarDeclaration() {
        skipOrPlay(var);
    }

    public void getStartBlock() {
        skipOrPlay(open);
    }

    public void getEndBlock() {
        skipOrPlay(close);
    }

    public void getIfStatement() {
        skipOrPlay(iff);
    }

    public void getForStatement() {
        skipOrPlay(forr);
    }

    public void getDoStatement() {
        skipOrPlay(doo);
    }

    public void getWhileStatement() {
        skipOrPlay(whilee);
    }

    public void getReturnStatement() {
        skipOrPlay(returnn);
    }

    public void getBreakStatement() {
        skipOrPlay(breakk);
    }

    public void getContinueStatement() {
        skipOrPlay(continuee);
    }

    public void getThrowStatement() {
        skipOrPlay(throww);
    }

    private void skipOrPlay(ScalableGrain countingGrain) {
        instructionCounter++;
        countingGrain.inc();
        if (instructionCounter > skipFader.getValue()) {
            double[] newEnvelope = adderEditor.getLastEnvelope();
            for (ScalableGrain g : allGrains) {
                g.scale(newEnvelope, scalingFader.getValue());
            }
            customPrePlay();
            countingGrain.play();
            resetCounters();
        }
    }

    protected void customPrePlay() {
    }

    private void resetCounters() {
        instructionCounter = 0;
        for (ScalableGrain g : allGrains) {
            g.resetCounter();
        }
    }

    public class ScalableGrain {

        private int counter;

        private GrainOnBus innerGrain;

        private String name;

        public ScalableGrain(GrainOnBus grain, String name) {
            this.innerGrain = grain;
            this.name = name;
        }

        public void inc() {
            counter++;
        }

        public int getCounted() {
            return counter;
        }

        public void resetCounter() {
            counter = 0;
        }

        public void scale(double[] referenceEnvelope, double maxScaling) {
            double scalingFactor = 1 - (counter / maxScaling);
            if (scalingFactor <= 0) {
                scalingFactor = 1;
            }
            double[] scaledEnvelope = new double[referenceEnvelope.length];
            for (int i = 0; i <= scaledEnvelope.length - 2; i += 2) {
                scaledEnvelope[i] = referenceEnvelope[i];
                scaledEnvelope[i + 1] = referenceEnvelope[i + 1] * scalingFactor;
            }
            innerGrain.overrideEnvelopePoints(scaledEnvelope);
        }

        public void play() {
            System.err.println("\t" + name);
            innerGrain.appendSound();
        }
    }
}
