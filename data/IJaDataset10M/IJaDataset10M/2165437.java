package org.nakedobjects;

/**
 * Utility class to start a server, using the default configuration file: client.properties.
 */
public final class Client extends StartUp {

    public static void main(String[] args) {
        String configurationFile = args.length > 0 ? args[0] : "client.properties";
        start(configurationFile);
    }
}
