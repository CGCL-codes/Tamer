package ru.adv.security.filter;

public class SecurityFilterInitializationException extends Exception {

    private static final long serialVersionUID = -7998249813922308321L;

    public SecurityFilterInitializationException(String message) {
        super(message);
    }

    public SecurityFilterInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
