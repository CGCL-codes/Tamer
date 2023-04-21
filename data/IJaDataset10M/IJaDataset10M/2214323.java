package joshua.discriminative.semiring_parsing;

public class ExpectationSemiringVector implements CompositeSemiring {

    private double logProb;

    private SignedValue[] factor1;

    int vectorSize;

    public ExpectationSemiringVector(int vectorSize_) {
        vectorSize = vectorSize_;
        factor1 = new SignedValue[vectorSize];
        for (int i = 0; i < factor1.length; i++) factor1[i] = new SignedValue();
    }

    public ExpectationSemiringVector(double logProb_, SignedValue[] factor1_) {
        logProb = logProb_;
        factor1 = factor1_;
        vectorSize = factor1.length;
    }

    public void setToZero(AtomicSemiring atomic) {
        logProb = atomic.ATOMIC_ZERO_IN_SEMIRING;
        for (int i = 0; i < vectorSize; i++) {
            factor1[i].setZero();
        }
    }

    public void setToOne(AtomicSemiring atomic) {
        logProb = atomic.ATOMIC_ONE_IN_SEMIRING;
        for (int i = 0; i < vectorSize; i++) {
            factor1[i].setZero();
        }
    }

    public void add(CompositeSemiring b, AtomicSemiring atomic) {
        ExpectationSemiringVector b2 = (ExpectationSemiringVector) b;
        this.logProb = atomic.add_in_atomic_semiring(this.logProb, b2.logProb);
        for (int i = 0; i < vectorSize; i++) {
            this.factor1[i] = SignedValue.add(this.factor1[i], b2.factor1[i]);
        }
    }

    public void multi(CompositeSemiring b, AtomicSemiring atomic) {
        ExpectationSemiringVector b2 = (ExpectationSemiringVector) b;
        for (int i = 0; i < vectorSize; i++) {
            this.factor1[i] = SignedValue.add(SignedValue.multi(this.logProb, b2.factor1[i]), SignedValue.multi(b2.logProb, this.factor1[i]));
        }
        this.logProb = atomic.multi_in_atomic_semiring(this.logProb, b2.logProb);
    }

    public void normalizeFactors() {
        for (int i = 0; i < vectorSize; i++) {
            this.factor1[i] = SignedValue.multi(-logProb, this.factor1[i]);
        }
    }

    public void printInfor() {
        System.out.println("prob: " + logProb);
        System.out.print("factor1:");
        for (int i = 0; i < vectorSize; i++) {
            System.out.print(" " + factor1[i].convertRealValue());
        }
        System.out.print("\n");
    }

    public void printInfor2() {
        SignedValue[] true_factor1 = new SignedValue[vectorSize];
        for (int i = 0; i < vectorSize; i++) {
            true_factor1[i] = SignedValue.multi(-logProb, this.factor1[i]);
        }
        System.out.println("prob: " + logProb + "; factor1: " + factor1 + "; factor2: " + factor1);
        System.out.println("true factor1: " + true_factor1 + "; true factor2: " + true_factor1);
    }

    public double getLogProb() {
        return logProb;
    }

    public SignedValue[] getFactor1() {
        return factor1;
    }
}
