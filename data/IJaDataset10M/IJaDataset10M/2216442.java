package com.mockturtlesolutions.snifflib.invprobs;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.DblParamSet;
import java.util.Set;

/**
Provides the OptimizableScalar object that can be optimized by 
Simplex or other methods.
*/
class GCVObjective implements OptimizableScalar {

    private Lpreg Regression;

    private DblParamSet Params;

    public GCVObjective(Lpreg F) {
        this.Regression = F;
        this.Params = new DblParamSet();
        this.Params.Dblput("Smooth", 1.0);
    }

    public DblMatrix getParam(String name) {
        return (this.Params.Dblget(name));
    }

    public boolean hasParameter(String name) {
        return (this.Params.hasParameter(name));
    }

    public void setParam(String name, DblMatrix value) {
        this.Params.Dblput(name, value);
    }

    public void replaceParams(DblParamSet X) {
        this.Params = X;
    }

    public String[] parameterSet() {
        return ((String[]) this.Params.keySet().toArray());
    }

    public DblMatrix getValueAt(DblMatrix X) {
        DblMatrix current_smooth = this.Params.Dblget("Smooth");
        this.Params.Dblput("Smooth", X);
        DblMatrix out = this.getValueToMinimize();
        this.Params.Dblput("Smooth", current_smooth);
        return (out);
    }

    public DblMatrix getValueToMinimize() {
        AbstractBandwidthMethod BW = this.Regression.getBandwidthMethod();
        FixedSmoothMethod SM = (FixedSmoothMethod) BW.getSmoothMethod();
        SM.setSmoothParameter(this.Params.Dblget("Smooth"));
        DblMatrix[] Xobs = this.Regression.getXdata();
        DblMatrix FIT = this.Regression.getGCV();
        return (FIT);
    }
}
