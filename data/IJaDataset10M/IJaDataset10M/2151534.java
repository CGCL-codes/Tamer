package com.agical.buildmonitor.listeners;

/**
 * Simple dto to hold project information
 *
 * @author Thomas Sundberg
 */
public class Project {

    private String server;

    private String name;

    public Project(String server, String name) {
        this.server = server;
        this.name = name;
    }

    public String getServer() {
        return server;
    }

    public String getName() {
        return name;
    }
}
