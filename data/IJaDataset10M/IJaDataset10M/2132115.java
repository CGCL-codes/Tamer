package ru.yandex.strictweb.scriptjava.base;

public interface InputValidator {

    public boolean isValid(Node n);

    public String getMessage();
}
