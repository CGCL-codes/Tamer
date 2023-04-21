package com.mockturtlesolutions.snifflib.util;

import junit.framework.*;
import java.util.Vector;
import com.mockturtlesolutions.snifflib.datatypes.DblMatrix;
import com.mockturtlesolutions.snifflib.invprobs.FunctionEvaluationException;
import com.mockturtlesolutions.snifflib.util.SnifflibUnknownOptionException;
import com.mockturtlesolutions.snifflib.util.LogGamma;
import com.mockturtlesolutions.snifflib.util.IncGamma;

public class GammaTest extends TestCase {

    public GammaTest(String test) {
        super(test);
    }

    public GammaTest() {
        super();
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public void testIncGamma() {
        IncGamma gam = new IncGamma();
        DblMatrix V1 = gam.incGamma(new DblMatrix(1.9), new DblMatrix(3.4));
        V1.show("Value");
        DblMatrix V2 = gam.incGamma(new DblMatrix(3.4), new DblMatrix(1.9));
        V2.show("Value");
    }

    public void testErf() {
        Erf erf = new Erf();
        DblMatrix V1 = erf.erf(new DblMatrix(1.3));
        V1.show("Erf Value");
    }

    public void testInvErf() {
        Erf erf = new Erf();
        DblMatrix V1 = erf.invErf(new DblMatrix(0.935));
        V1.show("Inv Erf Value");
    }
}
