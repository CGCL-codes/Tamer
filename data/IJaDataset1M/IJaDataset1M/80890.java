package aerie.response;

import groovy.util.ResourceException;

public interface Responder {

    void respond() throws ResourceException;

    String getDestination();
}
