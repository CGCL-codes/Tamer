package com.google.code.sagetvaddons.swl.client;

public final class Version {

    public static final String getVersion() {
        return "@@VER_NUM@@";
    }

    public static final String getBuild() {
        return "@@BLD_NUM@@";
    }

    public static final String getFullVersion() {
        return getVersion() + "." + getBuild();
    }
}
