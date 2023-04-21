package org.apache.rampart.tomcat.sample;

public class SimpleService {

    public String echo(String arg) throws Exception {
        System.out.println("Tomcat service accessed successfully.. :)");
        return arg;
    }
}
