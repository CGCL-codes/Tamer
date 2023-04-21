package com.gtt.pattern.behavior.chain;

public class ConcreteHandler3 extends Handler {

    @Override
    public void handleRequest(Request request) {
        System.out.println("concrete handler 3: handle request");
    }
}
