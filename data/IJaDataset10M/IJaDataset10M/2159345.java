package org.toobsframework.performance.data;

public interface IPerformanceInfo {

    void reportTime(String name, double time);

    String getType();
}
