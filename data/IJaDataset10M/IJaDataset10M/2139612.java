package net.sourceforge.javalogging;

import java.util.logging.*;

/*** A simple Java Hello World program, in the tradition of Kernighan and Richie.*/
public class HelloWorld {

    private static Logger theLogger = Logger.getLogger(HelloWorld.class.getName());

    public static void main(String[] args) {
        HelloWorld hello = new HelloWorld("Hello Adam!");
        hello.sayHello();
        LogManager.getLogManager().reset();
    }

    private String theMessage;

    public HelloWorld(String message) {
        theMessage = message;
    }

    public void sayHello() {
        theLogger.info(theMessage);
        theLogger.severe("Does this work");
        theLogger.info("Anything");
        theLogger.log(Level.SEVERE, "Threw an exception", new Exception("Test exception"));
    }
}
