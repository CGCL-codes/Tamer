package com.agical.buildmonitor.publisher.gembird;

import com.agical.buildmonitor.publisher.CommandRunner;
import com.agical.buildmonitor.publisher.Publisher;
import com.agical.buildmonitor.util.Configuration;
import com.agical.buildmonitor.listeners.Project;
import java.io.File;
import java.util.List;

public class SilverShield implements Publisher {

    private String name;

    private Configuration configuration;

    public SilverShield(String name, Configuration configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    public void reset() {
        turnOff(getFailureDevices());
        turnOff(getSuccessDevices());
    }

    public void buildFailed(List<Project> failedProjects) {
        turnOn(getFailureDevices());
        turnOff(getSuccessDevices());
    }

    public void buildPassed() {
        turnOn(getSuccessDevices());
        turnOff(getFailureDevices());
    }

    private void turnOn(List<String> sockets) {
        String state = "on";
        for (String socket : sockets) {
            changeState(state, socket);
        }
    }

    private void turnOff(List<String> sockets) {
        String state = "off";
        for (String socket : sockets) {
            changeState(state, socket);
        }
    }

    private void changeState(String state, String socket) {
        CommandRunner commandRunner = new CommandRunner();
        String device = getDeviceName();
        String binary = getBinary();
        String command = binary + " -" + state + " -" + device + " -" + socket;
        String path = getPath();
        File dir = new File(path);
        commandRunner.runCommand(command, dir);
    }

    public String getName() {
        return name;
    }

    public String getBinary() {
        String xPathExpression = "//silvershieldpublisher[@name='" + name + "']/@binary";
        return configuration.getValue(xPathExpression);
    }

    public String getPath() {
        String xPathExpression = "//silvershieldpublisher[@name='" + name + "']/@path";
        return configuration.getValue(xPathExpression);
    }

    public String getDeviceName() {
        String xPathExpression = "//silvershieldpublisher[@name='" + name + "']/device/@name";
        return configuration.getValue(xPathExpression);
    }

    public List<String> getFailureDevices() {
        String xPathExpression = "//silvershieldpublisher[@name='" + name + "']/device/failure/socket/@name";
        return configuration.getValues(xPathExpression);
    }

    public List<String> getSuccessDevices() {
        String xPathExpression = "//silvershieldpublisher[@name='" + name + "']/device/success/socket/@name";
        return configuration.getValues(xPathExpression);
    }
}
