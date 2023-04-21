package org.simpleframework.servlet;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.servlet.resolve.Chain;

class TaskFactory {

    private Definition definition;

    public TaskFactory(Definition definition) {
        this.definition = definition;
    }

    public Runnable getInstance(Request request, Response response, Chain chain) {
        return new Task(definition, request, response, chain);
    }
}
