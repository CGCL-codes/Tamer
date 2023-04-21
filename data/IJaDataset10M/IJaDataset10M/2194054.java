package com.google.code.jdde.event;

import com.google.code.jdde.client.DdeClient;
import com.google.code.jdde.ddeml.CallbackParameters;
import com.google.code.jdde.misc.DdeApplication;
import com.google.code.jdde.server.DdeServer;

/**
 * 
 * @author Vitor Costa
 *
 * @param <A>
 */
public abstract class RegisterEvent<A extends DdeApplication> extends DdeEvent<A> {

    private String service;

    private String specificService;

    public RegisterEvent(A application, CallbackParameters parameters) {
        super(application);
        service = parameters.getHsz1();
        specificService = parameters.getHsz2();
    }

    public String getService() {
        return service;
    }

    public String getSpecificService() {
        return specificService;
    }

    public static class ClientRegisterEvent extends RegisterEvent<DdeClient> {

        public ClientRegisterEvent(DdeClient client, CallbackParameters parameters) {
            super(client, parameters);
        }
    }

    public static class ServerRegisterEvent extends RegisterEvent<DdeServer> {

        public ServerRegisterEvent(DdeServer server, CallbackParameters parameters) {
            super(server, parameters);
        }
    }
}
