package co.edu.unal.ungrid.services.client.applet.subtraction.model;

import co.edu.unal.ungrid.similarity.RealValueMeasure;

public class CompoundSimilarityMeasure {

    public CompoundSimilarityMeasure() {
        m_sm = new RealValueMeasure[NUM_MEASURES];
        for (int i = 0; i < m_sm.length; i++) {
            m_sm[i] = new RealValueMeasure();
        }
    }

    public double getMeasure(int m) {
        return (0 <= m && m < NUM_MEASURES ? m_sm[m].getValue() : RealValueMeasure.NA);
    }

    public void setMeasure(int m, double f) {
        if (0 <= m && m < NUM_MEASURES) {
            m_sm[m].setValue(f);
        }
    }

    public RealValueMeasure[] m_sm;

    public static final int CORRELATION_RATIO = 0;

    public static final int CORRELATION_COEFFICIENT = 1;

    public static final int MUTUAL_INFORMATION = 2;

    public static final int SS_INTENSITY_DISTANCE = 3;

    public static final int SS_ABSOLUTE_DISTANCE = 4;

    public static final int NUM_MEASURES = 5;
}
