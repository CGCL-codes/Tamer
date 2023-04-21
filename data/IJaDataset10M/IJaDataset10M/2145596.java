package dryven.request.controller;

import dryven.model.di.LocalThreadStorage;

public interface ControllerFactory {

    public Object instanciateController(ControllerDescription cd, LocalThreadStorage storage);
}
