package org.stjs.javascript;

public abstract class Location {

    public String hash;

    public String host;

    public String hostname;

    public String href;

    public String pathname;

    public int port;

    public String protocol;

    public String search;

    public abstract void assign(String url);

    public abstract void reload();

    public abstract void replace(String url);
}
