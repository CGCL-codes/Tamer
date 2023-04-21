package bagaturchess.search.impl.utils;

public class SliderVariable {

    private static final boolean DEBUG = false;

    private String name;

    private int leftWeight;

    private int rightWeight;

    private final long scoresDistribution[];

    private int pointer;

    private long wholeIntegral;

    private long leftIntegral;

    private long rightIntegral;

    public SliderVariable(String _name, int _leftWeight, int _rightWeight, int domainSize) {
        name = _name;
        leftWeight = _leftWeight;
        rightWeight = _rightWeight;
        scoresDistribution = new long[domainSize];
        pointer = 0;
        leftIntegral = 0;
        rightIntegral = 0;
        wholeIntegral = 0;
    }

    public int getDomainSize() {
        return scoresDistribution.length;
    }

    public int getPointer() {
        return pointer;
    }

    public void updatePositive(int value) {
        if (value <= 0) {
            throw new IllegalStateException("value=" + value);
        } else if (value >= scoresDistribution.length) {
            value = scoresDistribution.length - 1;
        }
        scoresDistribution[value]++;
        wholeIntegral += 1;
        if (value > pointer) {
            rightIntegral += 1;
            pointer2Right();
        } else {
            leftIntegral += 1;
            pointer2Left();
        }
        if (DEBUG) {
            verify();
        }
    }

    public void updateNegative(int value) {
        if (value <= 0) {
            throw new IllegalStateException("value=" + value);
        } else if (value >= scoresDistribution.length) {
            value = scoresDistribution.length - 1;
        }
        scoresDistribution[value]--;
        wholeIntegral -= 1;
        if (value > pointer) {
            rightIntegral -= 1;
            pointer2Left();
        } else {
            leftIntegral -= 1;
            pointer2Right();
        }
        if (DEBUG) {
            verify();
        }
    }

    private void pointer2Right() {
        while (pointer < scoresDistribution.length - 1 && leftIntegral * leftWeight < rightIntegral * rightWeight) {
            leftIntegral += scoresDistribution[pointer];
            rightIntegral -= scoresDistribution[pointer];
            pointer++;
        }
    }

    private void pointer2Left() {
        if (pointer >= scoresDistribution.length) {
            pointer = scoresDistribution.length - 1;
        }
        while (pointer > 0 && leftIntegral * leftWeight >= rightIntegral * rightWeight) {
            leftIntegral -= scoresDistribution[pointer];
            rightIntegral += scoresDistribution[pointer];
            pointer--;
        }
    }

    public String toString() {
        return name + "->" + pointer;
    }

    private void verify() {
        for (int i = 0; i < scoresDistribution.length; i++) {
            long cur = scoresDistribution[i];
            if (cur < 0) {
                throw new IllegalStateException("cur=" + cur);
            }
        }
        long wholeIntegral_test = 0;
        for (int i = 0; i < scoresDistribution.length; i++) {
            wholeIntegral_test += scoresDistribution[i];
        }
        if (wholeIntegral_test != wholeIntegral) {
            throw new IllegalStateException("");
        }
        if (wholeIntegral != (leftIntegral + rightIntegral)) {
            throw new IllegalStateException("");
        }
        long leftIntegral_test = 0;
        for (int i = 0; i < pointer; i++) {
            leftIntegral_test += scoresDistribution[i];
        }
        if (leftIntegral_test != leftIntegral) {
            throw new IllegalStateException("leftIntegral_test=" + leftIntegral_test + ", leftIntegral=" + leftIntegral);
        }
        long rightIntegral_test = 0;
        for (int i = pointer; i < scoresDistribution.length; i++) {
            rightIntegral_test += scoresDistribution[i];
        }
        if (rightIntegral_test != rightIntegral) {
            throw new IllegalStateException();
        }
    }
}
