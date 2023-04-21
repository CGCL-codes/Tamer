package org.dcopolis.problem.ctaems;

public class QualityAccumulationFunction {

    public static int Q_SUM = 1;

    public static int Q_SYNC_SUM = 2;

    public static int Q_MAX = 4;

    public static int Q_MIN = 8;

    public static int Q_FIRST = 16;

    private int value;

    public QualityAccumulationFunction(int value) {
        this.value = value;
    }

    public float apply(float[] qualities) {
        return apply(value, qualities);
    }

    public static float apply(int value, float[] qualities) {
        int i;
        if (value == Q_SUM || value == Q_SYNC_SUM) {
            float sum = 0;
            for (i = 0; i < qualities.length; i++) {
                sum += qualities[i];
            }
            return sum;
        } else if (value == Q_MAX) {
            float max = Float.MIN_VALUE;
            for (i = 0; i < qualities.length; i++) {
                if (qualities[i] > max) max = qualities[i];
            }
            return max;
        } else if (value == Q_MIN) {
            float min = Float.MAX_VALUE;
            for (i = 0; i < qualities.length; i++) {
                if (qualities[i] < min) min = qualities[i];
            }
            return min;
        } else {
            System.err.println("ERROR: QAF value " + value + " not implemented!");
        }
        return 0;
    }

    public int getValue() {
        return value;
    }
}
