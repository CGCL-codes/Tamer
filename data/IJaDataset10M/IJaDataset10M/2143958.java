package mipt.math.sys.alt.transform;

import mipt.math.Number;

/**
 * Transforms scale of numbers forward and back:
 *  getInverse().transform(transform(x)).equals(x).
 * For convenience stores scale maximum (Saaty's 9 by default;
 *  scale minimum is assumed to be 1 but not stored).
 * @author Evdokimov
 */
public abstract class ScaleTransformer {

    private double scaleMax = 9.;

    public abstract static class AbstractTransformer extends ScaleTransformer {

        protected ScaleTransformer inverse;

        public AbstractTransformer(ScaleTransformer inverse) {
            this.inverse = inverse;
        }

        public final ScaleTransformer getInverse() {
            return inverse;
        }
    }

    public ScaleTransformer() {
        super();
    }

    public ScaleTransformer(double scaleMax) {
        setScaleMax(scaleMax);
    }

    public final double getScaleMax() {
        return scaleMax;
    }

    public void setScaleMax(double scaleMax) {
        this.scaleMax = scaleMax;
    }

    public abstract Number transform(Number x);

    public abstract ScaleTransformer getInverse();
}
