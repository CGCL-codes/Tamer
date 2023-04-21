package org.waveprotocol.wave.model.operation.testing;

import java.util.Random;
import org.waveprotocol.wave.model.operation.testing.RationalDomain.Affine;

public class AffineGenerator implements RandomOpGenerator<RationalDomain.Data, Affine> {

    @Override
    public Affine randomOperation(RationalDomain.Data state, Random random) {
        int neg1 = random.nextBoolean() ? -1 : 1;
        int neg2 = random.nextBoolean() ? -1 : 1;
        long mulNum, mulDenom;
        return new Affine(state.value, new Rational(neg1 * (random.nextInt(9) + 1), random.nextInt(9) + 1), new Rational(neg2 * (random.nextInt(10)), random.nextInt(9) + 1));
    }
}
