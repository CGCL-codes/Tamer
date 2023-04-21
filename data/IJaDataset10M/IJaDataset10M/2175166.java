package de.maramuse.soundcomp.test;

import static de.maramuse.soundcomp.process.StandardParameters.IN;
import static de.maramuse.soundcomp.process.StandardParameters.IN_IMAG;
import static de.maramuse.soundcomp.process.StandardParameters.OUT;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import de.maramuse.soundcomp.files.FileFormats;
import de.maramuse.soundcomp.files.OutputFile;
import de.maramuse.soundcomp.generator.CustomWave;
import de.maramuse.soundcomp.generator.CustomWaveform;
import de.maramuse.soundcomp.generator.Envelope;
import de.maramuse.soundcomp.generator.PWM;
import de.maramuse.soundcomp.generator.Sine;
import de.maramuse.soundcomp.math.mul;
import de.maramuse.soundcomp.process.ConstStream;
import de.maramuse.soundcomp.process.Mixer;
import de.maramuse.soundcomp.process.ProcessElement;
import de.maramuse.soundcomp.process.Scale;
import de.maramuse.soundcomp.process.StandardParameters;
import de.maramuse.soundcomp.process.Time;
import de.maramuse.soundcomp.process.WellTemperedScale;
import de.maramuse.soundcomp.util.AdvancerRegistry;
import de.maramuse.soundcomp.util.ComplexTable;
import de.maramuse.soundcomp.util.GlobalParameters;
import de.maramuse.soundcomp.util.NativeObjects;

public class MonkeyTest extends TestCase {

    private static final int fileBufferSize = 8192;

    private AdvancerRegistry advancerRegistry = new AdvancerRegistry();

    private GlobalParameters globalParameters = new GlobalParameters(44100);

    /**
   * @param args
   */
    public static void main(String[] args) {
        new MonkeyTest().testMonkey();
    }

    /**
   * Creates a test file with a small simple melody. Employs the envelope generator for amplitudes. The trigger signals
   * are created by a PWM generator. The pitches employ the well tempered scale, and are modulated in addition.
   */
    public void testMonkey() {
        try {
            advancerRegistry.clear();
            globalParameters.setSampleRate(44100);
            ProcessElement env1 = new Envelope();
            advancerRegistry.registerAdvancer(env1);
            Time time = new Time();
            Mixer mix = new Mixer();
            PWM gate = new PWM();
            advancerRegistry.registerAdvancer(mix);
            advancerRegistry.registerAdvancer(time);
            advancerRegistry.registerAdvancer(env1);
            advancerRegistry.registerAdvancer(gate);
            Scale s = new WellTemperedScale();
            ConstStream[] notes = new ConstStream[] { new ConstStream(s.getValueFor("c#")), new ConstStream(s.getValueFor("e")), new ConstStream(s.getValueFor("A")) };
            ConstStream beats = new ConstStream(1);
            ConstStream A = new ConstStream(0.13);
            ConstStream D = new ConstStream(1.25);
            ConstStream S = ConstStream.c(0.25);
            ConstStream R = new ConstStream(2.55);
            ConstStream A2 = new ConstStream(0.05);
            ConstStream D2 = new ConstStream(2.25);
            ConstStream S2 = ConstStream.c(0.25);
            ConstStream R2 = new ConstStream(1.55);
            ComplexTable<Double> ct = new ComplexTable<Double>();
            ct.put(0.0, 0);
            ct.put(0.25, 1);
            ct.put(1.0, 0);
            ct.put(1.25, -1);
            ct.put(2.0, 0);
            ProcessElement si = new Sine();
            Mixer fmix = new Mixer();
            mul fmul = new mul();
            mul fmul2 = new mul();
            Mixer fmix2 = new Mixer();
            Envelope fmenv = new Envelope();
            CustomWaveform ts = new CustomWaveform();
            ts.setTable(ct);
            ProcessElement cw = new CustomWave();
            advancerRegistry.registerAdvancer(cw);
            advancerRegistry.registerAdvancer(si);
            advancerRegistry.registerAdvancer(mix);
            advancerRegistry.registerAdvancer(fmix);
            advancerRegistry.registerAdvancer(fmix2);
            advancerRegistry.registerAdvancer(fmul2);
            advancerRegistry.registerAdvancer(fmenv);
            advancerRegistry.registerAdvancer(fmul);
            advancerRegistry.registerAdvancer(env1);
            try {
                mix.setSource(-1, cw, StandardParameters.OUT.i);
                mix.setSource(-1, env1, StandardParameters.OUT.i);
                gate.setSource(StandardParameters.FREQUENCY.i, beats, StandardParameters.OUT.i);
                gate.setSource(StandardParameters.DUTYCYCLE.i, ConstStream.c(0.5), StandardParameters.OUT.i);
                cw.setSource(StandardParameters.FREQUENCY.i, fmix2, StandardParameters.OUT.i);
                cw.setSource(StandardParameters.TABLE.i, ts, StandardParameters.TABLE.i);
                env1.setSource(StandardParameters.A.i, A, StandardParameters.OUT.i);
                env1.setSource(StandardParameters.D.i, D, StandardParameters.OUT.i);
                env1.setSource(StandardParameters.S.i, S, StandardParameters.OUT.i);
                env1.setSource(StandardParameters.R.i, R, StandardParameters.OUT.i);
                env1.setSource(StandardParameters.SYNC.i, gate, StandardParameters.OUT.i);
                fmenv.setSource(StandardParameters.A.i, A2, StandardParameters.OUT.i);
                fmenv.setSource(StandardParameters.D.i, D2, StandardParameters.OUT.i);
                fmenv.setSource(StandardParameters.S.i, S2, StandardParameters.OUT.i);
                fmenv.setSource(StandardParameters.R.i, R2, StandardParameters.OUT.i);
                fmenv.setSource(StandardParameters.SYNC.i, gate, StandardParameters.OUT.i);
                si.setSource(StandardParameters.FREQUENCY.i, fmix, StandardParameters.OUT.i);
                fmix.setSource(-1, fmenv, StandardParameters.OUT.i);
                fmix.setSource(-1, new ConstStream(-10), StandardParameters.OUT.i);
                fmix.setSource(-2, new ConstStream(10), StandardParameters.OUT.i);
                fmix2.setSource(-1, si, StandardParameters.OUT.i);
                fmix2.setSource(-1, fmenv, StandardParameters.OUT.i);
                fmix2.setSource(-1, new ConstStream(12), StandardParameters.OUT.i);
                fmul.setSource(StandardParameters.IN.i, new ConstStream(s.getValueFor("A")), StandardParameters.OUT.i);
                fmul.setSource(StandardParameters.IN_IMAG.i, new ConstStream(1), StandardParameters.OUT.i);
                fmix2.setSource(-2, fmul, StandardParameters.OUT.i);
            } catch (Exception te) {
                fail(te.getMessage());
            }
            OutputFile ws1 = new OutputFile(2);
            mul m1 = new mul();
            mul m2 = new mul();
            ws1.setSource(0, m1, OUT.i);
            ws1.setSource(-1, m2, OUT.i);
            m1.setSource(IN_IMAG.i, ConstStream.c(0.5), OUT.i);
            m2.setSource(IN_IMAG.i, ConstStream.c(0.5), OUT.i);
            m1.setSource(IN.i, mix, OUT.i);
            m2.setSource(IN.i, gate, OUT.i);
            advancerRegistry.registerAdvancer(ws1);
            double lastVal = 0;
            int lastIndex = 0;
            int sampleCounter = 0;
            long starttime = System.currentTimeMillis();
            for (; time.getValue(StandardParameters.OUT.i) < 1.0; ) {
                advancerRegistry.advanceAll();
                sampleCounter++;
                double newVal = gate.getValue(StandardParameters.OUT.i);
                if (lastVal < 1 && newVal > 0) {
                    fmul.setSource(StandardParameters.IN.i, notes[lastIndex], StandardParameters.OUT.i);
                    lastIndex = (lastIndex + 1) % notes.length;
                }
                lastVal = gate.getValue(StandardParameters.OUT.i);
                if ((sampleCounter % 1000) == 0) System.out.println("calculated " + sampleCounter + " samples in " + (System.currentTimeMillis() - starttime) / 1000 + " seconds");
            }
            try {
                File f = new File("monkeytest.wav");
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f), fileBufferSize);
                System.out.println("started writing wav file after " + (System.currentTimeMillis() - starttime) / 1000 + " seconds");
                ws1.setFormat(FileFormats.FMT_WAVE_S16);
                ws1.write(fos);
                System.out.println("finished writing wav file after " + (System.currentTimeMillis() - starttime) / 1000 + " seconds");
                fos.close();
            } catch (IOException ex) {
                fail(ex.getMessage());
            }
            try {
                File f = new File("monkeytest.ape");
                BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(f), fileBufferSize);
                System.out.println("started writing ape file after " + (System.currentTimeMillis() - starttime) / 1000 + " seconds");
                ws1.setFormat(FileFormats.FMT_MONKEY_16);
                ws1.write(fos);
                System.out.println("finished writing ape file after " + (System.currentTimeMillis() - starttime) / 1000 + " seconds");
                fos.close();
            } catch (IOException ex) {
                fail(ex.getMessage());
            }
            advancerRegistry.unregisterAdvancer(cw);
            advancerRegistry.unregisterAdvancer(mix);
            advancerRegistry.unregisterAdvancer(env1);
            advancerRegistry.unregisterAdvancer(time);
            advancerRegistry.unregisterAdvancer(gate);
            advancerRegistry.unregisterAdvancer(si);
            advancerRegistry.unregisterAdvancer(fmix);
            advancerRegistry.unregisterAdvancer(fmix2);
            advancerRegistry.unregisterAdvancer(fmul);
            advancerRegistry.unregisterAdvancer(fmul2);
            advancerRegistry.unregisterAdvancer(fmenv);
            advancerRegistry.clear();
            NativeObjects.unregisterNativeObject(mix);
            NativeObjects.unregisterNativeObject(time);
            NativeObjects.unregisterNativeObject(ws1);
            NativeObjects.unregisterNativeObject(gate);
            NativeObjects.unregisterNativeObject(si);
            NativeObjects.unregisterNativeObject(fmix);
            NativeObjects.unregisterNativeObject(fmix2);
            NativeObjects.unregisterNativeObject(fmul);
            NativeObjects.unregisterNativeObject(fmul2);
            NativeObjects.unregisterNativeObject(fmenv);
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
}
