package org.posterita.beans;

public class CommandBean extends UDIBean {

    private String[] simpleCommand;

    private String[] complexCommand;

    public void setSimpleCommand(String[] simpleCommand) {
        this.simpleCommand = simpleCommand;
    }

    public String[] getSimpleCommand() {
        return simpleCommand;
    }

    public void setComplexCommand(String[] complexCommand) {
        this.complexCommand = complexCommand;
    }

    public String[] getComplexCommand() {
        return complexCommand;
    }
}
