package com.mockturtlesolutions.snifflib.testfun;

import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.datatypes.DblParamSet;
import com.mockturtlesolutions.snifflib.invprobs.StatisticalModel;
import java.util.Iterator;
import java.util.Set;

public class HollingIII extends StatisticalModel {

    public HollingIII() {
        this.Params = new DblParamSet(2);
        this.declareParameter("B0", new DblMatrix(new Double(1.0)));
        this.declareParameter("B1", new DblMatrix(new Double(1.0)));
        this.setValueToGet(StatisticalModel.PREDICTION);
        this.setName("Holling III");
        this.declareVariable("Y");
        this.declareVariable("X");
    }

    public DblMatrix getParam(String name) {
        return (this.Params.Dblget(name));
    }

    public void setParam(String name, DblMatrix value) {
        this.Params.Dblput(name, value);
    }

    public void replaceParams(DblParamSet X) {
        this.Params = X;
    }

    public String[] parameterSet() {
        Set keys = this.Params.keySet();
        Iterator iter = keys.iterator();
        String[] out = new String[keys.size()];
        int j = 0;
        while (iter.hasNext()) {
            out[j] = (String) iter.next();
            j++;
        }
        return (out);
    }

    public DblMatrix getPredictionAt(DblMatrix[] X) {
        return (this.getValueAt(X[0]));
    }

    public DblMatrix getValueAt(DblMatrix X) {
        DblMatrix B0 = this.Params.Dblget("B0");
        DblMatrix B1 = this.Params.Dblget("B1");
        DblMatrix xsq = X.pow(2);
        DblMatrix OUT = (B0.times(xsq)).divideBy((B1.pow(2)).plus(xsq));
        return (OUT);
    }

    public String about() {
        String out = new String("Standard 2 parameter Holling III equation giving a sigmoid plot.  The two parameters are B0 (maximum consumption rate) and B1 (half saturation constant).");
        return (out);
    }
}
