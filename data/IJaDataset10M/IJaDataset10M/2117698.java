package org.mobicents.servlet.management.client.configuration;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ConfigurationServiceAsync {

    void setQueueSize(int queueSize, AsyncCallback<Void> callback);

    void getQueueSize(AsyncCallback<Integer> callback);

    void setMemoryThreshold(int memoryThreshold, AsyncCallback<Void> callback);

    void getMemoryThreshold(AsyncCallback<Integer> callback);

    void getConcurrencyControlMode(AsyncCallback<String> callback);

    void setConcurrencyControlMode(String mode, AsyncCallback<Void> callback);

    void getCongestionControlPolicy(AsyncCallback<String> callback);

    void setCongestionControlPolicy(String policy, AsyncCallback<Void> callback);

    void setCongestionControlCheckingInterval(long interval, AsyncCallback<Void> callback);

    void getCongestionControlCheckingInterval(AsyncCallback<Long> callback);

    void setBaseTimerInterval(int baseTimerInterval, AsyncCallback<Void> callback);

    void getBaseTimerInterval(AsyncCallback<Integer> callback);

    void setT2Interval(int t2Interval, AsyncCallback<Void> callback);

    void getT2Interval(AsyncCallback<Integer> callback);

    void setT4Interval(int t4Interval, AsyncCallback<Void> callback);

    void getT4Interval(AsyncCallback<Integer> callback);

    void setTimerDInterval(int timerDInterval, AsyncCallback<Void> callback);

    void getTimerDInterval(AsyncCallback<Integer> callback);

    void getLoggingMode(AsyncCallback<String> callback);

    void setLoggingMode(String loggingMode, AsyncCallback<Void> callback);

    void listLoggingProfiles(AsyncCallback<String[]> callback);
}
