package com.mockturtlesolutions.snifflib.invprobs;

import com.mockturtlesolutions.snifflib.datatypes.*;
import com.mockturtlesolutions.snifflib.invprobs.OptimizableScalar;

public class test_simplex {

    public static void main(String args[]) {
        System.out.println("Starting...");
        OptimizableScalar MyModel = new MyQuadratic();
        DblParamSet initGuess = new DblParamSet();
        initGuess.Dblput("X1", 1);
        initGuess.Dblput("X2", 1);
        NMSimplex MySimplex = new NMSimplex(MyModel, initGuess);
        NMSimplexConfiguration config = MySimplex.getConfiguration();
        config.setConfigValue("Verbose", "on");
        MySimplex.solve();
        MySimplex.getBestEstimate().show();
        MySimplex.getBestValue().show();
        System.out.println("Done!!");
        return;
    }
}
