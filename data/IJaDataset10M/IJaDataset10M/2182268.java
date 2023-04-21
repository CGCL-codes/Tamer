package net.joindesk.api.jmx;

public interface IFactoryMBean {

    int getDeskletDefCount();

    boolean isStatistic();

    void setStatistic(boolean statistic);

    void clearStatistic();

    int getErrorRequestCount();

    int getRequestCount();

    int getSuccessRequestCount();

    String getTotalResponcesTime();

    long getAverageResponcesTime();

    float getRequestFreq();

    void reloadDesklet();
}
