package com.mockturtlesolutions.snifflib.guitools.components;

public class IconMappingEditor {

    private String usrConfigFile;

    private IconServer server;

    public IconMappingEditor() {
        this.server = new IconServer();
        this.server.getIconMap();
    }
}
