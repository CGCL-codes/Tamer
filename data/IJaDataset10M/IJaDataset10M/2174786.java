package de.wolff.config;

public class ConfigNotFoundException extends Exception {

    /**
     * Default-Konstruktor
     */
    public ConfigNotFoundException() {
        super();
    }

    /**
     * Konstruktor mit Text
     */
    public ConfigNotFoundException(String reason) {
        super(reason);
    }
}
