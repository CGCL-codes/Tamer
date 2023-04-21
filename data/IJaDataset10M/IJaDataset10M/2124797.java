package org.openorb.orb.examples.interceptors;

public class ServerImpl extends ServerTestPOA {

    public java.lang.String print(java.lang.String message) {
        return "Hello from the client side ( in response to " + message + " )";
    }
}
