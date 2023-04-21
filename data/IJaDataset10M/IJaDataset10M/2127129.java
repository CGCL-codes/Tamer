package com.webdeninteractive.sbie.service;

import com.webdeninteractive.sbie.Service;
import com.webdeninteractive.sbie.Client;
import com.webdeninteractive.sbie.ProtocolHandler;

public class TestServiceTwo extends ServiceBase implements Service {

    /** Have this service perform its task. */
    public void runService() {
        String id = getParameter("id");
        if (id == null) {
            id = "TestServiceTwo";
            setParameter("id", id);
        }
        String message = getParameter("message");
        if (message == null) {
            message = "(No message parameter set)";
        }
        System.out.println("Message from " + id + "> " + message);
    }
}
