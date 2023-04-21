package com.orientechnologies.odbms.tools;

import java.io.PrintStream;
import java.util.Hashtable;

/**
 * Base abstract class for tools.
 * Copyright (c) 2001-2002
 * Orient Technologies (www.orientechnologies.com)
 *
 * @author Orient Staff (staff@orientechnologies.com)
 * @version 1.0
 */
public abstract class GenericTool {

    protected abstract void start(String[] iArg) throws Exception;

    protected abstract void printFormat();

    protected abstract void printTitle();

    public GenericTool() {
        parameters = new Hashtable();
        userAdvisor = new ConsoleUserAdvisor();
    }

    protected final String getToolName() {
        return this.getClass().getName();
    }

    protected final void syntaxError(String iMsg) {
        printFormat();
        System.err.println();
        throw new IllegalArgumentException("Syntax error in parameter(s): " + iMsg);
    }

    protected final void throwError(String iMsg) {
        throw new RuntimeException(iMsg);
    }

    public void setUserAdvisor(UserAdvisor userAdvisor) {
        this.userAdvisor = userAdvisor;
    }

    public UserAdvisor getUserAdvisor() {
        return userAdvisor;
    }

    public void setOutput(PrintStream iOutput) {
        output = iOutput;
    }

    public void writeOutput(String iText) {
        if (output != null) output.print(iText);
    }

    protected Hashtable parameters;

    private UserAdvisor userAdvisor;

    private PrintStream output = System.out;
}
