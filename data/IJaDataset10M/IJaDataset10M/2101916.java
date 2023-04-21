package ru.adv.mozart.controller;

public class ControllerNotFoundException extends ControllerInitializationException {

    private static final long serialVersionUID = -4319778481926790189L;

    public ControllerNotFoundException(String message) {
        super(CONTROLLER_NOT_FOUND, message);
    }
}
