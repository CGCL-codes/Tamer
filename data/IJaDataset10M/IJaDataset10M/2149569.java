package org.promotego.api.geocoder.util;

public class Range {

    private double m_minimum;

    private double m_maximum;

    public Double getMaximum() {
        return m_maximum;
    }

    public void setMaximum(Double rangeMaximum) {
        m_maximum = rangeMaximum;
    }

    public Double getMinimum() {
        return m_minimum;
    }

    public void setMinimum(Double rangeMinimum) {
        m_minimum = rangeMinimum;
    }

    public boolean isInRange(double value) {
        if (m_minimum < m_maximum) {
            return m_minimum <= value && value <= m_maximum;
        } else {
            return m_minimum >= value || m_maximum <= value;
        }
    }
}
