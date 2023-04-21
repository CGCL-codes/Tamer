package com.mockturtlesolutions.snifflib.integration;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;

/**
Adaptive stepsize Euler method.
This class provides a wrapper for an adaptive step-size integration utilizing an Euler update.
*/
public class OdeAE extends OdeInt {

    public OdeAE(Ode Model, DblMatrix Tstart, DblMatrix Tstop, DblMatrix Yinit) {
        super(Model, Tstart, Tstop, Yinit);
        AdaptiveInterval FI = new AdaptiveInterval(this.Model, this.Tstart, this.Tstop, this.Yinit, this.Options);
        FI.setReporter(this.Reporter);
        FI.setdT(this.Options.InitDt);
        Euler E = new Euler(this.Model, this.Tstart, this.Tstop, this.Yinit, this.Options);
        E.setReporter(this.Reporter);
        this.Reporter.addToSolutionTrace(Tstart, Yinit);
        FI.setStepIntegrator(E);
        this.setIntervalIntegrator(FI);
    }
}
