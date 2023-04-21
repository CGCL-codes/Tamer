package com.velocityme.utility;

public class DurationRemaining {

    private int m_duration_min;

    private int m_remaining_min;

    public DurationRemaining(int duration_min, int remaining_min) {
        m_duration_min = duration_min;
        m_remaining_min = remaining_min;
    }

    public int getDuration_min() {
        return m_duration_min;
    }

    public int getRemaining_min() {
        return m_remaining_min;
    }

    public void add(DurationRemaining other) {
        m_duration_min += other.m_duration_min;
        m_remaining_min += other.m_remaining_min;
    }
}
