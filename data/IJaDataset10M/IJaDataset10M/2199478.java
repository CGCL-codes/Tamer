package net.sf.linns.model.activation.tests;

import net.sf.linns.model.IActivationFunction;
import net.sf.linns.model.NeuronValidation;
import net.sf.linns.model.activation.TangensHyperbolicusFunction;
import junit.framework.TestCase;

public class TangensHyperbolicusTest extends TestCase {

    private static final float TEMPERATURE = 0.125f;

    private static final float T_SIGN = 0.5f;

    private static final float F_SIGN = -0.5f;

    private static final float T_BINARY = 1f;

    private static final float F_BINARY = 0f;

    private static final float TOLERANCE = 0.001f;

    public void testLowerBound() {
        IActivationFunction f = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY_SIGN_SCALED);
        double ret = f.getA(F_SIGN);
        assertTrue("Expected to be approx. -0,5: " + String.valueOf(ret), ret >= F_SIGN);
        assertTrue("Expected to be approx. -0,5: " + String.valueOf(ret), ret < F_SIGN + TOLERANCE);
        IActivationFunction f2 = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY);
        double ret2 = f2.getA(F_BINARY);
        assertTrue("Expected to be approx. 0: " + String.valueOf(ret2), ret2 >= F_BINARY);
        assertTrue("Expected to be approx. 0: " + String.valueOf(ret2), ret2 < F_BINARY + TOLERANCE);
    }

    public void testUpperBound() {
        IActivationFunction f = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY_SIGN_SCALED);
        double ret = f.getA(T_SIGN);
        assertTrue("Expected to be approx. 0,5: " + String.valueOf(ret), ret <= T_SIGN);
        assertTrue("Expected to be approx. 0,5: " + String.valueOf(ret), ret > T_SIGN - TOLERANCE);
        IActivationFunction f2 = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY);
        double ret2 = f2.getA(T_BINARY);
        assertTrue("Expected to be approx. 1: " + String.valueOf(ret2), ret2 <= T_BINARY);
        assertTrue("Expected to be approx. 1: " + String.valueOf(ret2), ret2 > T_BINARY - TOLERANCE);
    }

    public void testExtendLowerBound() {
        IActivationFunction f = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY_SIGN_SCALED);
        double ret = f.getA(F_SIGN);
        assertTrue("Expected to be approx. -0,5: " + String.valueOf(ret), ret >= F_SIGN);
        assertTrue("Expected to be approx. -0,5: " + String.valueOf(ret), ret < F_SIGN + TOLERANCE);
    }

    public void testExtendUpperBound() {
        IActivationFunction f = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY_SIGN_SCALED);
        double ret = f.getA(T_SIGN);
        assertTrue("Expected to be approx. 0,5: " + String.valueOf(ret), ret <= T_SIGN);
        assertTrue("Expected to be approx. 0,5: " + String.valueOf(ret), ret > T_SIGN - TOLERANCE);
    }

    public void testZeroBound() {
        IActivationFunction f = new TangensHyperbolicusFunction(TEMPERATURE, NeuronValidation.BINARY_SIGN_SCALED);
        double ret = f.getA(0);
        assertTrue("Expected to be approx. 0: " + String.valueOf(ret), ret < 0 + TOLERANCE);
        assertTrue("Expected to be approx. 0: " + String.valueOf(ret), ret > 0 - TOLERANCE);
    }
}
