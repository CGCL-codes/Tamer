package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/**
 * <p>This class provides a stable normalized random generator. It samples from a stable
 * distribution with location parameter 0 and scale 1.</p>
 *
 * <p>The implementation uses the Chambers-Mallows-Stuck method as described in
 * <i>Handbook of computational statistics: concepts and methods</i> by
 * James E. Gentle, Wolfgang H&auml;rdle, Yuichi Mori.</p>
 *
 * @version $Id: StableRandomGenerator.java 1244107 2012-02-14 16:17:55Z erans $
 * @since 3.0
 */
public class StableRandomGenerator implements NormalizedRandomGenerator {

    /** Underlying generator. */
    private final RandomGenerator generator;

    /** stability parameter */
    private final double alpha;

    /** skewness parameter */
    private final double beta;

    /** cache of expression value used in generation */
    private final double zeta;

    /**
     * Create a new generator.
     *
     * @param generator underlying random generator to use
     * @param alpha Stability parameter. Must be in range (0, 2]
     * @param beta Skewness parameter. Must be in range [-1, 1]
     */
    public StableRandomGenerator(final RandomGenerator generator, double alpha, double beta) {
        if (generator == null) {
            throw new NullArgumentException();
        }
        if (!(alpha > 0d && alpha <= 2d)) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT, alpha, 0, 2);
        }
        if (!(beta >= -1d && beta <= 1d)) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_SIMPLE, beta, -1, 1);
        }
        this.generator = generator;
        this.alpha = alpha;
        this.beta = beta;
        if (alpha < 2d && beta != 0d) {
            zeta = beta * FastMath.tan(FastMath.PI * alpha / 2);
        } else {
            zeta = 0d;
        }
    }

    /**
     * Generate a random scalar with zero location and unit scale.
     *
     * @return a random scalar with zero location and unit scale
     */
    public double nextNormalizedDouble() {
        double omega = -FastMath.log(generator.nextDouble());
        double phi = FastMath.PI * (generator.nextDouble() - 0.5);
        if (alpha == 2d) {
            return FastMath.sqrt(2d * omega) * FastMath.sin(phi);
        }
        double x;
        if (beta == 0d) {
            if (alpha == 1d) {
                x = FastMath.tan(phi);
            } else {
                x = FastMath.pow(omega * FastMath.cos((1 - alpha) * phi), 1d / alpha - 1d) * FastMath.sin(alpha * phi) / FastMath.pow(FastMath.cos(phi), 1d / alpha);
            }
        } else {
            double cosPhi = FastMath.cos(phi);
            if (FastMath.abs(alpha - 1d) > 1e-8) {
                double alphaPhi = alpha * phi;
                double invAlphaPhi = phi - alphaPhi;
                x = (FastMath.sin(alphaPhi) + zeta * FastMath.cos(alphaPhi)) / cosPhi * (FastMath.cos(invAlphaPhi) + zeta * FastMath.sin(invAlphaPhi)) / FastMath.pow(omega * cosPhi, (1 - alpha) / alpha);
            } else {
                double betaPhi = FastMath.PI / 2 + beta * phi;
                x = 2d / FastMath.PI * (betaPhi * FastMath.tan(phi) - beta * FastMath.log(FastMath.PI / 2d * omega * cosPhi / betaPhi));
                if (alpha != 1d) {
                    x = x + beta * FastMath.tan(FastMath.PI * alpha / 2);
                }
            }
        }
        return x;
    }
}
