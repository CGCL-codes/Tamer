package com.thumbuki.slipmat.examples;

import com.thumbuki.slipmat.module.synth.SynthBass;
import com.thumbuki.slipmat.*;
import com.thumbuki.slipmat.module.*;
import com.thumbuki.slipmat.module.controller.LinearSlide;

/**
 * ControllerExample.
 * <p>
 * This uses the linearSlider module to change parameters over time.
 * The end product sounds like something from an old high school
 * science video or slide show.
 * 
 * @author Jacob Joaquin
 */
public class ControllerExample {

    private static final int SCORE_DURATION = 9;

    public static void main(String[] args) {
        new ControllerExample();
        try {
            Thread.sleep(1000 * SCORE_DURATION);
        } catch (Exception ex) {
        }
    }

    public ControllerExample() {
        SynthRack synthRack = new SynthRack(false);
        LinearSlide linearSlider = new LinearSlide();
        SynthBass bass = new SynthBass();
        Output output = new Output();
        synthRack.addModule(linearSlider);
        synthRack.addModule(bass);
        synthRack.addModule(output);
        output.setInput(bass.getOutput());
        linearSlider.slide(1.0, 0.05, 2.0, bass.getOscillator1Detune());
        linearSlider.slide(1.125, 0.05, 1.5, bass.getOscillator1Detune());
        bass.playNote(0.0, 8.0, 0.5, 100);
        bass.playNote(2.5, 6.0, 0.25, 200);
        synthRack.setDuration((double) SCORE_DURATION);
        synthRack.startCsound();
    }

    /**
	 * Converts half steps into a ratio.
	 * 
	 * @param hs Half-steps
	 * @return   Ratio
	 */
    private double halfStepsToRatio(double hs) {
        return Math.pow(2.0, hs / 12.0);
    }
}
