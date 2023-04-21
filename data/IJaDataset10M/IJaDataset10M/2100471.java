package com.duniptech.soa.service;

public interface RTSimulationServiceInterface {

    void receive(String key, String simulatorFrom, String portFrom, String simulatorTo, String portTo, String[] xmlValues) throws Exception;

    void newSimulator(String xmlAtomicAsString) throws Exception;

    void addConnection(String key, String simulatorFrom, String portFrom, String simulatorTo, String endPointServiceTo, String portTo) throws Exception;

    String getEndPoint();

    void initialize(String key) throws Exception;

    void simulate(String key) throws Exception;

    String getConsole(String clientIp) throws Exception;

    void exit(String key) throws Exception;

    String observe(String clientIp, String serverPackage, String xmlModelAsString, double observeSeconds) throws Exception;
}
