package com.pbxworkbench.pbx;

public class CallerIdentity {

    private String name;

    private String number;

    public CallerIdentity(String callingName, String callingNumber) {
        this.name = callingName;
        this.number = callingNumber;
    }

    public String getCallingName() {
        return name;
    }

    public String getCallingNumber() {
        return number;
    }
}
