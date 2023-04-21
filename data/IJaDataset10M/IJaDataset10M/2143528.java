package com.thumbuki.slipmat.module.controller;

import com.thumbuki.slipmat.*;

/**
 * Linear Slide.
 * <p>
 * This Module changes the current value of a control-rate channel
 * to a new value over time.
 * 
 * @author Jacob Joaquin
 */
public class LinearSlide extends Module {

    Instrument instr;

    public void setup() {
        instr = new Instrument(assignInstrumentNumber());
        addInstrument(instr);
    }

    public void compile() {
        instr.appendln("S0 strget p5");
        instr.appendln("i0 chnget S0");
        instr.appendln("k0 line i0, p3, p4");
        instr.appendln("chnset k0, S0");
    }

    public void initialScoreEvents() {
    }

    /**
	 * Slide from the current value to a new value over time.
	 * 
	 * @param time  When to start the slide.
	 * @param dur   Duration of the slide.
	 * @param value End value.
	 * @param chn   The chn to control.
	 */
    public void slide(double time, double dur, double value, ChnK chn) {
        iEvent(instr, time, dur, value, chn.getQuote());
    }
}
