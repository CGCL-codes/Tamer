package com.newisys.dv;

/**
 * Registered callback for DVApplication that
 * passes a string of text to be interpreted
 * filtered by the name of the interpreter.
 * 
 * @author Bill Flanders
 *
 */
public interface InterpreterCallback {

    public String getName();

    public void interpret(String text);
}
