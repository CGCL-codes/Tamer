package org.newsclub.net.unix.demo.rmi;

import java.io.IOException;

/**
 * The implementation of the very simple {@link HelloWorld} service.
 * 
 * @author Christian Kohlschütter
 */
public class HelloWorldImpl implements HelloWorld {

    public String hello() throws IOException {
        System.out.println("Received call to hello()");
        return "Hello world";
    }
}
