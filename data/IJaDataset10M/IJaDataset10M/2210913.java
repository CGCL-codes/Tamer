package org.simpleframework.servlet;

import org.simpleframework.http.Address;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.resource.Resource;
import org.simpleframework.servlet.resolve.Chain;
import org.simpleframework.servlet.resolve.Registry;

class DefinitionResource implements Resource {

    private final TaskFactory factory;

    private final Registry registry;

    public DefinitionResource(Definition definition) {
        this.factory = new TaskFactory(definition);
        this.registry = definition.getRegistry();
    }

    public void handle(Request request, Response response) {
        Address address = request.getAddress();
        Chain chain = registry.resolve(address);
        handle(request, response, chain);
    }

    private void handle(Request request, Response response, Chain chain) {
        Runnable task = factory.getInstance(request, response, chain);
        if (task != null) {
            task.run();
        }
    }
}
