package org.rost.web.thrithon.core;

public interface ThrithonFilter {

    public void processing(ThrithonRequestContext ctx) throws ThrithonException;

    public void postprocessing(ThrithonRequestContext ctx) throws ThrithonException;
}
