package net.meblabs.commons.progress;

public interface ComplexProgress extends Progress {

    ProgressState getSubProgressState();

    void subProgressInitialize(String message);

    void subProgressInitialize(int total, String message);

    void setSubProgressTotal(int total);

    void subProgressAdvance(String message);

    void setSubProgressCompleted();
}
