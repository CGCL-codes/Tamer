package org.jcryptool.analysis.kegver.test;

import java.math.BigInteger;
import org.jcryptool.analysis.kegver.layer3.Commitment;
import org.junit.Test;

public class TestBuildingCommitment {

    @Test
    public void first() {
        int m = 1;
        BigInteger N = BigInteger.TEN;
        BigInteger g = BigInteger.TEN;
        BigInteger x = BigInteger.ONE;
        BigInteger h = BigInteger.TEN;
        Commitment.FujisajiOkamoto(m, N, g, x, h);
    }
}
