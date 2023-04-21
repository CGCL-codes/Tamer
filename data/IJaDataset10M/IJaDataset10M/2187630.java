package com.neo.flow.jmx;

public interface AdminMBean {

    String listProcessingInvocations();

    String listAllInvocations();

    String listCompleteInvocations();

    String removeCompleteInvocations();

    String listNewInvocations();

    String listAllSessions();

    String listInvocationsAgainstSession(String sessionName);

    void runPricingTest(Integer amountOfWork);
}
