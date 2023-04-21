package com.halotis.taskLauncher;

public class CommandRule {

    private String equals = null;

    private String startsWith = null;

    private String endsWith = null;

    private String contains = null;

    private String command = "";

    public boolean matches(String request) {
        if (equals != null && !request.equals(equals)) {
            return false;
        }
        if (startsWith != null && !request.startsWith(startsWith)) {
            return false;
        }
        if (endsWith != null && !request.endsWith(endsWith)) {
            return false;
        }
        if (contains != null && !request.contains(contains)) {
            return false;
        }
        return true;
    }

    public String getEquals() {
        return equals;
    }

    public void setEquals(String equals) {
        this.equals = equals;
    }

    public String getStartsWith() {
        return startsWith;
    }

    public void setStartsWith(String startsWith) {
        this.startsWith = startsWith;
    }

    public String getEndsWith() {
        return endsWith;
    }

    public void setEndsWith(String endsWith) {
        this.endsWith = endsWith;
    }

    public String getContains() {
        return contains;
    }

    public void setContains(String contains) {
        this.contains = contains;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
