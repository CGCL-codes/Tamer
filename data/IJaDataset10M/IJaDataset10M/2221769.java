package org.tamacat.core;

public class CoreFactory {

    public static Core createCore() {
        return new DBCore();
    }
}
